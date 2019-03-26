package com.ecse422.project;

import com.ecse422.project.Analyzer.Analyzer;
import com.ecse422.project.Analyzer.MST;
import com.ecse422.project.Model.Edge;
import com.ecse422.project.Model.Model;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NetworkOptimizerTest {
    private static double[] reliability = {0.94, 0.91, 0.96, 0.93, 0.92, 0.94, 0.97, 0.91, 0.92, 0.94, 0.90, 0.94, 0.93, 0.96, 0.91};
    private static int[] cost = {10, 25, 10, 20, 30, 10, 10, 25, 20, 20, 40, 10, 20, 10, 30};
    private static int numOfNodes = 6;

    private static double[] rr10 = {0.9950273696720713, 0.8023244244548774, 0.9773361146336055, 0.87383859858307, 0.9692894516331773, 0.9129209494820049, 0.9684851426837116, 0.8860395761519965, 0.9773029954547298, 0.9259429901766899, 0.8711914380853081, 0.9770638221692616, 0.8783386262358904, 0.8423588586657524, 0.9523471413041572, 0.9802483335960194, 0.8458809903287401, 0.9148169769121862, 0.879520243545892, 0.88102740725347, 0.8777538550264672, 0.8819391227558312, 0.8782862615308233, 0.8797885090858849, 0.9744681238151325, 0.98897720989285, 0.8797558393192595, 0.9467299058114289, 0.8487611059401187, 0.9679820574157678, 0.8000260984204258, 0.9825843509054407, 0.9496008045117066, 0.8016194624346777, 0.8314132237323615, 0.9126621498353503, 0.9323404591021767, 0.8979522443658025, 0.9825414233252878, 0.9284067449794995, 0.9401416348868962, 0.9412026866481611, 0.8951147943810815, 0.8302202690519315, 0.9078446502748065};
    private static int[] cc10 = {13, 18, 14, 18, 16, 15, 10, 19, 18, 18, 10, 11, 17, 19, 17, 14, 10, 19, 11, 16, 11, 18, 18, 18, 16, 17, 10, 11, 12, 17, 20, 13, 17, 14, 15, 18, 11, 12, 14, 17, 17, 13, 14, 11, 18};
    private static int nn10 = 10;

    private static Random mRandom = new Random();

    private static double targetReliability = 0.95;

    private static int targetCost = 75;

    private static double[] generateReliability(int size, double lowerbound, double upperbound) {
        int flatten_size = (size * (size - 1)) / 2;
        double[] res = new double[flatten_size];
        for (int i = 0; i < flatten_size; i++) {
            res[i] = lowerbound + mRandom.nextDouble() * (upperbound - lowerbound);
        }
        return res;
    }

    private static int[] generateCost(int size, int lowerbound, int upperbound) {
        int flatten_size = (size * (size - 1)) / 2;
        int[] res = new int[flatten_size];
        for (int i = 0; i < flatten_size; i++) {
            res[i] = lowerbound + mRandom.nextInt() * (upperbound - lowerbound);
        }
        return res;
    }

    @Test
    public void testMSTOptimizeByCost() {
        Model model = new Model(reliability, cost, numOfNodes);
        List<Edge> edges = (new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true)).getEdges();

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

    @Test
    public void testComputeReliability() {
        Model model = new Model(reliability, cost, numOfNodes);
        assertEquals(Analyzer.computeReliability(numOfNodes,
                (new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), false)).getEdges()),
                0.781, 0.001);
        assertEquals(Analyzer.computeReliability(numOfNodes,
                (new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true)).getEdges()),
                0.781, 0.001);
        Analyzer.optimize(model, 0.995, 0);
        Analyzer.optimize(model, 0.975, 120);
    }

    @Test
    public void testMaximizeReliability() {
        Model model = new Model(reliability, cost, numOfNodes);
        Set<Edge> edges = Analyzer.optimize(model, -1.0, 105);
        double r = Analyzer.computeReliability(numOfNodes, edges);
        System.out.println(r);
        assertTrue(true);
    }

    @Test
    public void testTimeComplexity() {
//        int matrix_size = 8;
//        double[] reliability10 = generateReliability(matrix_size, 0.5, 1.0);
//        int[] cost10 = generateCost(matrix_size, 10, 20);
        long startTime = System.nanoTime();
        Model model = new Model(rr10, cc10, nn10);
        Set<Edge> edges = Analyzer.optimize(model, -1.0, 300);

        long endTime = System.nanoTime();
        System.out.println(endTime - startTime);
        double r = Analyzer.computeReliability(nn10, edges);
        System.out.println(r);
        assertTrue(true);
    }
}
