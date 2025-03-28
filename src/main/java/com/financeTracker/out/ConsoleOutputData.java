package com.financeTracker.out;

/**
 * Implementation of the OutputData interface for console output.
 * This class provides methods to output data to the console's standard output and error streams.
 */
public class ConsoleOutputData implements OutputData {

    /**
     * Outputs the given data to the standard output stream.
     *
     * @param data the data to output
     */
    @Override
    public void output(Object data) {
        System.out.println(data.toString());
    }

    /**
     * Outputs the given data to the error output stream.
     *
     * @param data the data to output as an error
     */
    @Override
    public void errOutput(Object data) {
        System.err.println(data.toString());
    }
}