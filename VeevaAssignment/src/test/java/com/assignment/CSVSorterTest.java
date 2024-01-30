package com.assignment;

import com.assignment.service.CSVSorter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVSorterTest {

    private static final String TEST_INVALID_FORMAT_FILE = "src/main/resources/test/testStringValues.csv";
    private static final String TEST_NUMBERS_AND_STRING = "src/main/resources/test/testNumbersAndString.csv";
    private static final String TEST_1_VALUE_FILE = "src/main/resources/test/test1.csv";
    private static final String TEST_50_VALUES_FILE = "src/main/resources/test/test50.csv";
    private static final String TEST_10000_VALUE_FILE = "src/main/resources/test/test10000.csv";
    private static final String TEST_EMPTY_FILE = "src/main/resources/test/testEmpty.csv";
    private static final String TEST_OUTPUT_FILE = "src/main/resources/test/output_test.csv";

    @AfterEach
    void clear() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_OUTPUT_FILE));
    }

    @Test
    void test_sort50Values() throws IOException, InterruptedException, ExecutionException {
        CSVSorter csvSorter = new CSVSorter(10, 1);
        csvSorter.sortCSV(TEST_50_VALUES_FILE, TEST_OUTPUT_FILE);
        List<Integer> sortedData = readDataFromFile(TEST_OUTPUT_FILE);
        List<Integer> expectedSortedData = new ArrayList<>(sortedData);
        Collections.sort(expectedSortedData);

        assertEquals(expectedSortedData, sortedData);
    }

    @Test
    void test_emptyFile() throws IOException, ExecutionException, InterruptedException {
        CSVSorter csvSorter = new CSVSorter(10, 1);
        csvSorter.sortCSV(TEST_EMPTY_FILE, TEST_OUTPUT_FILE);
        List<Integer> sortedData = readDataFromFile(TEST_OUTPUT_FILE);

        assertEquals(new ArrayList(), sortedData);
    }

    @Test
    void test_oneValue() throws IOException, ExecutionException, InterruptedException {
        CSVSorter csvSorter = new CSVSorter(10, 1);
        csvSorter.sortCSV(TEST_1_VALUE_FILE, TEST_OUTPUT_FILE);
        List<Integer> sortedData = readDataFromFile(TEST_OUTPUT_FILE);
        List<Integer> expectedSortedData = new ArrayList<>(sortedData);
        Collections.sort(expectedSortedData);

        assertEquals(expectedSortedData, sortedData);
    }

    @Test
    void test_10000Values() throws IOException, ExecutionException, InterruptedException {
        CSVSorter csvSorter = new CSVSorter(100, 1);
        csvSorter.sortCSV(TEST_10000_VALUE_FILE, TEST_OUTPUT_FILE);
        List<Integer> sortedData = readDataFromFile(TEST_OUTPUT_FILE);
        List<Integer> expectedSortedData = new ArrayList<>(sortedData);
        Collections.sort(expectedSortedData);

        assertEquals(expectedSortedData, sortedData);
    }

    @Test
    void test_numbersAndString() throws IOException, ExecutionException, InterruptedException {
        CSVSorter csvSorter = new CSVSorter(5, 1);
        csvSorter.sortCSV(TEST_NUMBERS_AND_STRING, TEST_OUTPUT_FILE);
        List<Integer> sortedData = readDataFromFile(TEST_OUTPUT_FILE);
        List<Integer> expectedSortedData = new ArrayList<>(sortedData);
        Collections.sort(expectedSortedData);

        assertEquals(expectedSortedData, sortedData);
    }
    @Test
    void test_invalidFormat() throws IOException, ExecutionException, InterruptedException {
        CSVSorter csvSorter = new CSVSorter(10, 1);
        csvSorter.sortCSV(TEST_INVALID_FORMAT_FILE, TEST_OUTPUT_FILE);
        List<String> outputData = Files.readAllLines(Paths.get(TEST_OUTPUT_FILE));
        outputData.remove(0); // remove headline
        assertTrue(outputData.isEmpty());

    }

    private List<Integer> readDataFromFile(String filePath) throws IOException {
        List<Integer> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                data.add(Integer.parseInt(line.trim()));
            }
        }
        return data;
    }

}
