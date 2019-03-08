package com.ecse422.project;

import com.ecse422.project.Analyzer.Analyzer;
import com.ecse422.project.Analyzer.MST;
import com.ecse422.project.Model.Edge;
import com.ecse422.project.Model.Model;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NetworkOptimizerTest {
    private static double[] reliability = {0.94, 0.91, 0.96, 0.93, 0.92, 0.94, 0.97, 0.91, 0.92, 0.94, 0.90, 0.94, 0.93, 0.96, 0.91};
    private static int[] cost = {10, 25, 10, 20, 30, 10, 10, 25, 20, 20, 40, 10, 20, 10, 30};
    private static int numOfNodes = 6;

    private static double targetReliability = 0.95;

    private static int targetCost = 75;

    @Test
    public void testMSTOptimizeByCost() {
        Model model = new Model(reliability, cost, numOfNodes);
        List<Edge> edges = (new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true)).getEdges();

        assert (edges.get(0).getSource() == 0);
        assert (edges.get(0).getDestination() == 1);
        assert (edges.get(0).getCost() == 10);
        assert (edges.get(0).getReliability() == 0.94);

        assert (edges.get(1).getSource() == 0);
        assert (edges.get(1).getDestination() == 3);
        assert (edges.get(1).getCost() == 10);
        assert (edges.get(1).getReliability() == 0.96);

        assert (edges.get(2).getSource() == 1);
        assert (edges.get(2).getDestination() == 2);
        assert (edges.get(2).getCost() == 10);
        assert (edges.get(2).getReliability() == 0.94);

        assert (edges.get(3).getSource() == 2);
        assert (edges.get(3).getDestination() == 5);
        assert (edges.get(3).getCost() == 10);
        assert (edges.get(3).getReliability() == 0.94);

        assert (edges.get(4).getSource() == 0);
        assert (edges.get(4).getDestination() == 4);
        assert (edges.get(4).getCost() == 20);
        assert (edges.get(4).getReliability() == 0.93);
    }

    @Test
    public void testMSTOptimizeByReliability() {
        Model model = new Model(reliability, cost, numOfNodes);
        List<Edge> edges = (new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), false)).getEdges();

        assert (edges.get(0).getSource() == 1);
        assert (edges.get(0).getDestination() == 3);
        assert (edges.get(0).getCost() == 10);
        assert (edges.get(0).getReliability() == 0.97);

        assert (edges.get(1).getSource() == 3);
        assert (edges.get(1).getDestination() == 0);
        assert (edges.get(1).getCost() == 10);
        assert (edges.get(1).getReliability() == 0.96);

        assert (edges.get(2).getSource() == 3);
        assert (edges.get(2).getDestination() == 5);
        assert (edges.get(2).getCost() == 10);
        assert (edges.get(2).getReliability() == 0.96);

        assert (edges.get(3).getSource() == 1);
        assert (edges.get(3).getDestination() == 2);
        assert (edges.get(3).getCost() == 10);
        assert (edges.get(3).getReliability() == 0.94);

        assert (edges.get(4).getSource() == 0);
        assert (edges.get(4).getDestination() == 4);
        assert (edges.get(4).getCost() == 20);
        assert (edges.get(4).getReliability() == 0.93);
    }

//    @Test
//    public void testComputeReliability() {
//        Model model = new Model(reliability, cost, numOfNodes);
//        assertEquals(Analyzer.computeReliability(numOfNodes,
//                (new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), false)).getEdges()),
//                0.781, 0.001);
//        assertEquals(Analyzer.computeReliability(numOfNodes,
//                (new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true)).getEdges()),
//                0.742, 0.001);
//        Analyzer.optimize(model, 0.95, 0);
//    }

    @Test
    public void testMaximizeReliability(){
        Model model = new Model(reliability, cost, numOfNodes);
        List<Edge> edges = Analyzer.optimize(model, -1.0, 105);
        double r = Analyzer.computeReliability(numOfNodes,edges);
        System.out.println(r);
        assertTrue(true);
    }
}
