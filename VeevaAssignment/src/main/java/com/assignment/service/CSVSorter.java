package com.assignment.service;

import com.assignment.models.ValueReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class CSVSorter {
    private int maxElementsInMemory;
    private int awaitTermination;

    public CSVSorter(int maxElementsInMemory, int awaitTermination) {
        this.maxElementsInMemory = maxElementsInMemory;
        this.awaitTermination = awaitTermination;
    }

    /**
     * this method reads "maxElementsInMemory" values from the input file, merges them and saved it in file in the disk.
     * @param inputFile - path of the input file
     * @param outputFile - path of the output file
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void sortCSV(String inputFile, String outputFile) throws IOException, InterruptedException, ExecutionException {
        List<File> tempFiles = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<File>> futures = new ArrayList<>();
        String headerLine;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            List<Integer> values = new ArrayList<>();
             headerLine = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    values.add(Integer.parseInt(line.trim()));
                    if (values.size() == maxElementsInMemory) {
                        List<Integer> records = new ArrayList<>(values);
                        values.clear();
                        futures.add(executor.submit(() -> sortAndSave(records)));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid number: " + e.getMessage());
                }
            }
            if (!values.isEmpty()) {
                futures.add(executor.submit(() -> sortAndSave(values)));
            }
        }
        for (Future<File> future : futures) {
            tempFiles.add(future.get());
        }

        executor.shutdown();
        executor.awaitTermination(awaitTermination, TimeUnit.HOURS);

        mergeFiles(tempFiles, outputFile, headerLine);

        for (File file : tempFiles) {
            file.delete();
        }
    }

    /**
     * this method gets the numbers that were reader from csv file, sorts them(merge sort) and creates temp file in the disk
     * @param values - numbers from the csv file
     * @return File - file holds sorted numbers
     * @throws IOException in case that file creation has a problem
     */
    private File sortAndSave(List<Integer> values) throws IOException {
        Collections.sort(values);

        File tempFile = File.createTempFile("sortedFile_", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            for (int value : values) {
                writer.write(Integer.toString(value));
                writer.newLine();
            }
        }
        return tempFile;
    }

    /**
     * this method reads all the sorted files in the disk, merges them by using priority queue and writes them to the output file
     * @param files - list the holds references of the temp files in the disk
     * @param outputFile - path for the output file
     * @param headerLine - header value
     * @throws IOException
     */
    private void mergeFiles(List<File> files, String outputFile, String headerLine) throws IOException {
        PriorityQueue<ValueReader> queue = new PriorityQueue<>();
        for (File file : files) {
            ValueReader reader = new ValueReader(file);
            if (reader.next()) {
                queue.add(reader);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            if (headerLine != null) {
                writer.write(headerLine);
                writer.newLine();
            }
            while (!queue.isEmpty()) {
                ValueReader reader = queue.poll();
                writer.write(reader.getValue());
                writer.newLine();
                if (reader.next()) {
                    queue.add(reader);
                } else {
                    reader.close();
                }
            }
        }
    }
}
