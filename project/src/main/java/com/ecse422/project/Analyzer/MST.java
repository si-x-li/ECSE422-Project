package com.ecse422.project.Analyzer;

import com.ecse422.project.Model.Edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MST {
    // Number of nodes
    private int n;

    // Keeps track of which nodes have been used
    private boolean[] set;

    // Keeps track of the list of edges
    private List<Edge> edges;

    /**
     * @param n            Number of nodes in the graph
     * @param cost         The cost matrix of the graph
     * @param reliability  The reliability matrix of the graph
     * @param minimizeCost Minimizes graph based on cost or not
     */
    public MST(int n, int[][] cost, double[][] reliability, boolean minimizeCost) {
        edges = new ArrayList();
        this.n = n;
        this.set = new boolean[n];
        int[][] costCopy = Arrays.stream(cost)
                .map((int[] row) -> row.clone())
                .toArray((int length) -> new int[length][]);

        double[][] reliabilityCopy = Arrays.stream(reliability)
                .map((double[] row) -> row.clone())
                .toArray((int length) -> new double[length][]);

        if (!minimizeCost) {
            primsMaxReliability(costCopy, reliabilityCopy);
        } else {
            primsMinCost(costCopy, reliabilityCopy);
        }
    }

    /**
     * Finds the edge with the highest reliability that's connected to an already connected node.
     *
     * @param cost        The cost matrix of the graph
     * @param reliability The reliability matrix of the graph
     */
    private void findMaxReliability(int[][] cost, double[][] reliability) {
        double maximum = -1.0;
        int x = 0;
        int y = 0;

        // Find the edge with the highest reliability that's connected to an already connected node
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (set[i] == true && set[j] == false && reliability[i][j] > maximum) {
                    x = i;
                    y = j;
                    maximum = reliability[i][j];
                }
            }
        }

        set[x] = true;
        set[y] = true;
        edges.add(new Edge(x, y, cost[x][y], reliability[x][y]));
    }

    /**
     * Finds the edge with the highest reliability.
     *
     * @param cost        The cost matrix of the graph
     * @param reliability The reliability matrix of the graph
     */
    private void initializeMaxReliability(int[][] cost, double[][] reliability) {
        double maximum = -1.0;
        int x = 0;
        int y = 0;

        // Find the edge with the highest reliability
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (reliability[i][j] > maximum) {
                    x = i;
                    y = j;
                    maximum = reliability[i][j];
                }
            }
        }

        set[x] = true;
        set[y] = true;
        edges.add(new Edge(x, y, cost[x][y], reliability[x][y]));
    }

    /**
     * Finds the edge with the lowest cost.
     *
     * @param cost        The cost matrix of the graph
     * @param reliability The reliability matrix of the graph
     */
    private void initializeMinCost(int[][] cost, double[][] reliability) {
        int minimum = Integer.MAX_VALUE;
        int x = 0;
        int y = 0;

        // Find the edge with the lowest cost
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cost[i][j] < minimum && cost[i][j] != 0) {
                    x = i;
                    y = j;
                    minimum = cost[i][j];
                }
            }
        }

        set[x] = true;
        set[y] = true;
        edges.add(new Edge(x, y, cost[x][y], reliability[x][y]));
    }

    /**
     * Finds an edge with the lowest cost that's connected to an already connected node.
     *
     * @param cost        The cost matrix of the graph
     * @param reliability The reliability matrix of the graph
     */
    private void findMinCost(int[][] cost, double[][] reliability) {
        int minimum = Integer.MAX_VALUE;
        int x = 0;
        int y = 0;

        // Find the edge with the lowest cost that's connected to an already connected node
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (set[i] == true && set[j] == false && cost[i][j] < minimum && cost[i][j] != 0) {
                    x = i;
                    y = j;
                    minimum = cost[i][j];
                }
            }
        }

        set[x] = true;
        set[y] = true;
        edges.add(new Edge(x, y, cost[x][y], reliability[x][y]));
    }

    /**
     * Applies Prim's algorithm to find the minimum spanning tree based on the maximal reliability.
     *
     * @param cost        The cost matrix of the graph
     * @param reliability The reliability matrix of the graph
     */
    private void primsMaxReliability(int[][] cost, double[][] reliability) {
        initializeMaxReliability(cost, reliability);

        for (int i = 0; i < n - 2; i++) {
            findMaxReliability(cost, reliability);
        }
    }

    /**
     * Applies Prim's algorithm to find the minimum spanning tree based on the minimal cost.
     *
     * @param cost        The cost matrix of the graph
     * @param reliability The reliability matrix of the graph
     */
    private void primsMinCost(int[][] cost, double[][] reliability) {
        initializeMinCost(cost, reliability);

        // Add n - 2 vertices
        for (int i = 0; i < n - 2; i++) {
            findMinCost(cost, reliability);
        }
    }

    /**
     * Returns all edges within the minimum spanning tree
     *
     * @return A list of edges
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Prints the minimum spanning tree
     */
    public void printGraph() {
        for (Edge edge : edges) {
            System.out.println(edge.toString());
        }
    }
}
