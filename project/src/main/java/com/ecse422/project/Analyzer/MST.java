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
        double maximumReliability = -1.0;
        double minimumCost = Integer.MAX_VALUE;
        int x = 0;
        int y = 0;

        // Find the edge with the highest reliability that's connected to an already connected node
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                if (set[i] == true && set[j] == false && reliability[i][j] > maximumReliability ||
                        set[i] == true && set[j] == false && reliability[i][j] == maximumReliability && cost[i][j] < minimumCost) {
                    x = i;
                    y = j;
                    maximumReliability = reliability[i][j];
                    minimumCost = cost[i][j];
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
        double maximumReliability = -1.0;
        int minimumCost = Integer.MAX_VALUE;
        int x = 0;
        int y = 0;

        // Find the edge with the highest reliability
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                if (reliability[i][j] > maximumReliability ||
                        reliability[i][j] == maximumReliability && cost[i][j] < minimumCost) {
                    x = i;
                    y = j;
                    maximumReliability = reliability[i][j];
                    minimumCost = cost[i][j];
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
        double maximumReliability = 0.0;
        int minimumCost = Integer.MAX_VALUE;
        int x = 0;
        int y = 0;

        // Find the edge with the lowest cost
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                if (cost[i][j] < minimumCost ||
                        cost[i][j] == minimumCost && reliability[i][j] > maximumReliability) {
                    x = i;
                    y = j;
                    minimumCost = cost[i][j];
                    maximumReliability = reliability[i][j];
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
        int minimumCost = Integer.MAX_VALUE;
        double maximumReliability = 0.0;
        int x = 0;
        int y = 0;

        // Find the edge with the lowest cost that's connected to an already connected node
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                if (set[i] == true && set[j] == false && cost[i][j] < minimumCost ||
                        set[i] == true && set[j] == false && cost[i][j] == minimumCost && reliability[i][j] > maximumReliability) {
                    x = i;
                    y = j;
                    minimumCost = cost[i][j];
                    maximumReliability = reliability[i][j];
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
