package com.ecse422.project;

import com.ecse422.project.Model.Model;
import com.ecse422.project.Reader.Reader;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Obtain from the user a file to be read
            System.out.println("Please enter path to the input file: ");
            String path = scanner.nextLine();
            if (path == null) {
                continue;
            }

            // Attempt to read and analyse the file
            try {
                Model model = Reader.readFromFile(path);
                System.out.println(model.toString());
            } catch (IllegalArgumentException err) {
                System.out.println("Critical exception: " + err.getLocalizedMessage() + "\n");
            }
        }
    }
}
