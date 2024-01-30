package com.assignment;

import com.assignment.service.CSVSorter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class CSVSorterMain {


    public static void main(String[] args) {
        try {
            int maxElementsInMemory = Integer.parseInt(args[0]);
            String inputFile = args[1];
            int awaitTermination = args.length > 2 ? Integer.valueOf(args[2]) : 1;

            String outputFile = "src/main/resources/sortedCsv.csv";
            File outputDir = new File("output");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            CSVSorter sorter = new CSVSorter(maxElementsInMemory, awaitTermination);
            sorter.sortCSV(inputFile, outputFile);
            System.out.println("Sorting is completed. Output file: " + outputFile);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

