package com.ecse422.project.Model;

import java.util.Arrays;

public class Model {
    private double[] reliability;
    private int[] cost;
    private int numOfNodes;

    public Model(double[] reliability, int[] cost, int numOfNodes) {
        this.reliability = reliability;
        this.cost = cost;
        this.numOfNodes = numOfNodes;
    }

    public double[] getReliability() {
        return reliability;
    }

    public int[] getCost() {
        return cost;
    }

    public int getNumOfNodes() {
        return numOfNodes;
    }

    public String toString() {
        return "Reliability: " + Arrays.toString(reliability) + "\n" +
                "Cost: " + Arrays.toString(cost) + "\n" +
                "Number of nodes: " + numOfNodes + "\n";
    }
}
