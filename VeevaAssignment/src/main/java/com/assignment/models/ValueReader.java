package com.assignment.models;

import java.io.*;

/**
 * this class holds value from the file and BufferReader for reading the values from file
 */
public class ValueReader implements Comparable<ValueReader>, Closeable {
    private String value;
    private BufferedReader reader;

   public ValueReader(File file) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(file));
    }

    public String getValue() {
        return value;
    }

    public boolean next() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return false;
        }
        value = line;
        return true;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public int compareTo(ValueReader other) {
        return Integer.compare(Integer.parseInt(this.value.trim()), Integer.parseInt(other.value.trim()));
    }
}
