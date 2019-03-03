package com.ecse422.project.Analyzer;

import com.ecse422.project.Model.Model;

public class Analyzer {
    public static void analyze(Model model, double targetReliability, int targetCost) {
        if (targetReliability == -1.0) {
            // Maximize reliability subject to given cost
            // TODO
            System.out.println("MAXIMIZE RELIABILITY SUBJECT TO COST");

            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true);
            mst.printGraph();
        } else if (targetCost == 0) {
            // Reach target reliability
            // TODO
            System.out.println("REACH TARGET RELIABILITY");

            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), false);
            mst.printGraph();
        } else {
            // Reach target reliability subject to given cost
            // TODO
            System.out.println("REACH TARGET RELIABILITY SUBJECT TO COST");

            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true);
            mst.printGraph();
        }
    }
}
