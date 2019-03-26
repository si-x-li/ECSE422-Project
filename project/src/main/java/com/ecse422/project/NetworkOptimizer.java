package com.ecse422.project;

import com.ecse422.project.Analyzer.Analyzer;
import com.ecse422.project.Model.Model;
import com.ecse422.project.Reader.Reader;

import java.util.Scanner;

public class NetworkOptimizer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Obtain from the user a file to be read
            System.out.println("Please enter path to the input file: ");
            String path = scanner.nextLine();
            if (path == null) {
                continue;
            }

            boolean choiceConfirmed = false;
            int cost = 0;
            double reliability = -1.0;
            while (!choiceConfirmed) {
                System.out.println(
                        "What would you like to do?\n" +
                                "[1] - Meet a given reliability goal\n" +
                                "[2] - Meet a given reliability goal subject to a given cost constraint\n" +
                                "[3] - Maximize reliability subject to a given cost constraint");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        choiceConfirmed = true;
                        reliability = promptReliability();
                        break;
                    case "2":
                        cost = promptCost();
                        reliability = promptReliability();
                        choiceConfirmed = true;
                        break;
                    case "3":
                        cost = promptCost();
                        choiceConfirmed = true;
                        break;
                    default:
                        System.out.println("Could not understand input");
                        break;
                }
            }

            // Attempt to read and analyse the file
            try {
                Model model = Reader.readFromFile(path);
                System.out.println(
                        "--------------------------------------------------------------------------------\n" +
                                "                                   PARAMETERS                                   \n" +
                                "--------------------------------------------------------------------------------");
                System.out.print(model.toString());
                if (reliability == -1.0) {
                    System.out.println("Reliability: Unconstrained");
                } else {
                    System.out.println("Reliability: " + reliability);
                }
                if (cost == 0) {
                    System.out.println("Cost: Unconstrained");
                } else {
                    System.out.println("Cost: " + cost);
                }
                System.out.println(
                        "--------------------------------------------------------------------------------\n" +
                                "********************************************************************************\n" +
                                "--------------------------------------------------------------------------------");

                // Optimize the graph
                Analyzer.optimize(model, reliability, cost);
            } catch (IllegalArgumentException err) {
                System.out.println("Critical exception: " + err.getLocalizedMessage() + "\n");
            }
        }
    }

    /**
     * Prompts the user for the target reliability of the network.
     *
     * @return The target reliability of the network.
     */
    private static double promptReliability() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Please input reliability: ");
                double reliability = Double.parseDouble(scanner.nextLine());

                // Checks if the number is between acceptable bounds
                if (reliability < 0.0 || reliability > 1.0) {
                    throw new NumberFormatException();
                }

                return reliability;
            } catch (NumberFormatException err) {
                System.out.println("Could not understand input - Expected a floating point number between 0.0 and 1.0");
            }
        }
    }

    /**
     * Prompts the user for the target cost of the network.
     *
     * @return The target cost of the network.
     */
    private static int promptCost() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Please input cost: ");
                int cost = Integer.parseInt(scanner.nextLine());

                // Checks if the number is between acceptable bounds
                if (cost <= 0) {
                    throw new NumberFormatException();
                }

                return cost;
            } catch (NumberFormatException err) {
                System.out.println("Could not understand input - Expecting an integer between 1 and 2^31 - 1");
            }
        }
    }
}
