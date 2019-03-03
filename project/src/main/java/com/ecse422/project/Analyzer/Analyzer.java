package com.ecse422.project.Analyzer;

import com.ecse422.project.Model.Model;

public class Analyzer {
    public static void analyze(Model model, double reliability, int cost) {
        if (reliability == -1.0) {
            // Maximize reliability subject to given cost
            // TODO
            System.out.println("MAXIMIZE RELIABILITY SUBJECT TO COST");
        } else if (cost == 0) {
            // Reach target reliability
            // TODO
            System.out.println("REACH TARGET RELIABILITY");
        } else {
            // Reach target reliability subject to given cost
            // TODO
            System.out.println("REACH TARGET RELIABILITY SUBJECT TO COST");
        }
    }
}
