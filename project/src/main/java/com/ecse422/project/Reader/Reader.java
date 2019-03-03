package com.ecse422.project.Reader;

import com.ecse422.project.Model.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    /**
     * Used to keep track which stage the reader is in
     */
    enum Stage {
        NUM_OF_NODES,
        RELIABILITY,
        COST,
        TERMINATED
    }

    /**
     * Reads information from a textfile and stores them to a Model object.
     *
     * @param input An absolute or relative path to be read.
     * @return A Model object if the input file is valid.
     * @throws IllegalArgumentException if the file was not found.
     */
    public static Model readFromFile(String input) throws IllegalArgumentException {
        // Attempt to read file from storage
        File file = new File(input);
        if (!file.exists()) {
            throw new IllegalArgumentException(input + " was not found");
        }

        int numOfNodes = -1;
        double[] reliability = {};
        int[] cost = {};
        Stage stage = Stage.NUM_OF_NODES;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(input));
            String line;
            boolean canContinue = true;
            while ((line = reader.readLine()) != null && canContinue) {
                // Remove all extra whitespaces
                line = line.trim().replaceAll(" +", " ");

                // Skips comments
                if (line.charAt(0) == '#') {
                    continue;
                }

                // Verifies which stage of input the file is in
                switch (stage) {
                    case NUM_OF_NODES:
                        stage = Stage.RELIABILITY;
                        numOfNodes = extractNodes(line);
                        break;
                    case RELIABILITY:
                        stage = Stage.COST;
                        reliability = extractReliability(line, numOfNodes);
                        break;
                    case COST:
                        stage = Stage.TERMINATED;
                        cost = extractCost(line, numOfNodes);
                        break;
                    case TERMINATED:
                        canContinue = false;
                        break;
                }
            }
            reader.close();
        } catch (IOException err) {
            throw new IllegalArgumentException(input + " could not be read");
        } catch (NumberFormatException err) {
            throw new IllegalArgumentException(err.getLocalizedMessage());
        }

        return new Model(reliability, cost, numOfNodes);
    }

    /**
     * Extracts the number of nodes
     *
     * @param input String - An integer number in String format
     * @return 0 to 2^31-1
     * @throws NumberFormatException if the number of nodes is badly formatted.
     */
    private static int extractNodes(String input) throws NumberFormatException {
        try {
            int output = Integer.parseInt(input);

            // Verifies that the number of nodes is greater than 0
            if (output < 0) {
                throw new NumberFormatException();
            }
            return output;
        } catch (NumberFormatException err) {
            throw new NumberFormatException("Invalid entry for number of nodes");
        }
    }

    /**
     * Extracts an array of reliability from a String.
     *
     * @param input      A String from which information will be extracted.
     * @param numOfNodes Number of nodes inside the network.
     * @return An array of double of reliability.
     * @throws NumberFormatException if the input String is badly formatted.
     */
    private static double[] extractReliability(String input, int numOfNodes)
            throws NumberFormatException {
        String[] tokens = tokenizeEntry(input, numOfNodes);
        int numOfEntries = tokens.length;
        double[] output = new double[numOfEntries];

        // Parse through the entries
        for (int i = 0; i < numOfEntries; i++) {
            try {
                output[i] = Double.parseDouble(tokens[i]);
            } catch (NumberFormatException err) {
                throw new NumberFormatException("Invalid entry for reliability");
            }
        }

        return output;
    }

    /**
     * Extracts an array of cost from a String.
     *
     * @param input      A String from which information will be extracted.
     * @param numOfNodes Number of nodes inside the network.
     * @return An array of int of cost.
     * @throws NumberFormatException if the input String is badly formatted.
     */
    private static int[] extractCost(String input, int numOfNodes)
            throws NumberFormatException {
        String[] tokens = tokenizeEntry(input, numOfNodes);
        int numOfEntries = tokens.length;
        int[] output = new int[numOfEntries];

        // Parse through the entries
        for (int i = 0; i < numOfEntries; i++) {
            try {
                output[i] = Integer.parseInt(tokens[i]);
            } catch (NumberFormatException err) {
                throw new NumberFormatException("Invalid entry for cost");
            }
        }
        return output;
    }

    /**
     * Tokenizes the input String by whitespace.
     *
     * @param input      A String to be tokenized by whitespace.
     * @param numOfNodes Number of nodes inside the network.
     * @return Tokens (an array of String).
     * @throws NumberFormatException if the input String is badly formatted.
     */
    private static String[] tokenizeEntry(String input, int numOfNodes)
            throws NumberFormatException {
        if (numOfNodes < 1) {
            throw new NumberFormatException("Invalid entry for number of nodes");
        }

        String[] output = input.split(" ");

        int numOfEntries = (numOfNodes * (numOfNodes - 1)) / 2;

        // Checks if the number of entries is correct
        if (output.length != numOfEntries) {
            throw new NumberFormatException("Invalid number of tokens expected!");
        }
        return output;
    }
}
