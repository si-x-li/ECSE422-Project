package com.ecse422.project.Analyzer;

import com.ecse422.project.Model.Edge;
import com.ecse422.project.Model.Model;

import java.util.ArrayList;
import java.util.List;

public class Analyzer {
    /**
     *
     * @param model
     * @param targetReliability
     * @param targetCost
     * @return
     */
    public static List<Edge> optimize(Model model, double targetReliability, int targetCost) {
        List<Edge> edges = new ArrayList();
        if (targetReliability == -1.0) {
            // Maximize reliability subject to given cost
            // TODO
            System.out.println("MAXIMIZE RELIABILITY SUBJECT TO COST");

            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true);
            edges.addAll(mst.getEdges());
        } else if (targetCost == 0) {
            // Reach target reliability
            // TODO
            System.out.println("REACH TARGET RELIABILITY");

            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), false);
            edges.addAll(mst.getEdges());
        } else {
            // Reach target reliability subject to given cost
            // TODO
            System.out.println("REACH TARGET RELIABILITY SUBJECT TO COST");

            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true);
            edges.addAll(mst.getEdges());
        }

        return edges;
    }
}
