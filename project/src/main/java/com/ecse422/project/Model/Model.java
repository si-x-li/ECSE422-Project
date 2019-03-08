package com.ecse422.project.Model;

import sun.rmi.server.InactiveGroupException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Model {
    private double[][] reliability;
    private int[][] cost;
    private int numOfNodes;
    private int cheapestEdgeCost;
    private Set<Edge> completeEdgeSet;

    /**
     * Constructor
     *
     * @param reliability An array of reliability
     * @param cost        An array of cost
     * @param numOfNodes  The number of nodes
     */
    public Model(double[] reliability, int[] cost, int numOfNodes) {
        int count = 0;
        this.reliability = new double[numOfNodes][numOfNodes];
        for (int i = 0; i < numOfNodes; i++) {
            for (int j = i + 1; j < numOfNodes; j++) {
                this.reliability[i][j] = reliability[count++];
            }
        }

        count = 0;
        for (int i = 0; i < numOfNodes; i++) {
            for (int j = i + 1; j < numOfNodes; j++) {
                this.reliability[j][i] = reliability[count++];
            }
        }

        count = 0;
        this.cost = new int[numOfNodes][numOfNodes];
        for (int i = 0; i < numOfNodes; i++) {
            for (int j = i + 1; j < numOfNodes; j++) {
                this.cost[i][j] = cost[count++];
            }
        }

        count = 0;
        for (int i = 0; i < numOfNodes; i++) {
            for (int j = i + 1; j < numOfNodes; j++) {
                this.cost[j][i] = cost[count++];
            }
        }
        this.numOfNodes = numOfNodes;

        this.cheapestEdgeCost = Integer.MAX_VALUE;
        for (int i = 0; i < cost.length; i++) {
            int edgeCost = cost[i];
            if (edgeCost < this.cheapestEdgeCost && edgeCost>0 && reliability[i]>0){
                this.cheapestEdgeCost = edgeCost;
            }
        }

        // Initialize comple set of edges with reliability > 0
        count = 0;
        this.completeEdgeSet = new HashSet<>();
        for (int i = 0; i < numOfNodes; i++) {
            for (int j = i + 1; j < numOfNodes; j++) {
                if ( reliability[count] > 0){
                    completeEdgeSet.add(new Edge(i, j, cost[count], reliability[count]));
                }
                count++;
            }
        }

    }

    public Set<Edge> getCompleteEdgeSet(){ return completeEdgeSet; }

    /**
     * Returns the cheapest edge with non-zero cost and non-zero reliability
     * @return
     */
    public int getCheapestEdgeCost(){ return cheapestEdgeCost; }

    /**
     * Returns the reliability matrix.
     *
     * @return A 2D matrix of double
     */
    public double[][] getReliability() {
        return reliability;
    }

    /**
     * Returns the cost matrix.
     *
     * @return A 2D matrix of int
     */
    public int[][] getCost() {
        return cost;
    }

    /**
     * Returns the number of nodes.
     *
     * @return An integer
     */
    public int getNumOfNodes() {
        return numOfNodes;
    }

    /**
     * @return The stringified object
     */
    public String toString() {
        String output = "Reliability: \n";
        for (int i = 0; i < numOfNodes; i++) {
            output += Arrays.toString(reliability[i]) + "\n";
        }

        output += "Cost: \n";
        for (int i = 0; i < numOfNodes; i++) {
            output += Arrays.toString(cost[i]) + "\n";
        }

        output += "Number of nodes: " + numOfNodes + "\n";
        return output;
    }
}
