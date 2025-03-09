package com.financeTracker.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Implementation of the Input interface for console input.
 * This class provides methods to read input from the console and close the input stream.
 */
public class ConsoleInput implements Input {

    private final BufferedReader br;

    /**
     * Constructs a ConsoleInput instance, initializing the buffered reader to read from System.in.
     */
    public ConsoleInput() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Reads a line of input from the console.
     *
     * @return the input read from the console
     * @throws RuntimeException if an I/O error occurs while reading input
     */
    @Override
    public Object in() {
        String readData;
        try {
            readData = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return readData;
    }

    /**
     * Closes the input stream associated with the console input.
     *
     * @throws RuntimeException if an I/O error occurs while closing the input stream
     */
    @Override
    public void closeIn() {
        try {
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}