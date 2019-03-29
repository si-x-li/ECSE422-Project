package com.ecse422.project.Model;

public class Edge {
    private int source;
    private int destination;
    private int cost;
    private double reliability;
    private double ratio;

    /**
     * Constructor
     *
     * @param source      Source node
     * @param destination Destination node
     * @param cost        Cost of the edge
     * @param reliability Reliability of the edge
     */
    public Edge(int source, int destination, int cost, double reliability) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
        this.reliability = reliability;
        this.ratio = reliability/cost;
    }

    /**
     * @return The source node
     */
    public int getSource() {
        return source;
    }

    /**
     * @return The destination node
     */
    public int getDestination() {
        return destination;
    }

    /**
     * @return The cost of the edge
     */
    public int getCost() {
        return cost;
    }

    /**
     * @return The reliability of the edge
     */
    public double getReliability() {
        return reliability;
    }

    public double getRatio(){
        return ratio;
    }

    public void updateRatio(Edge e){
        int newEdgeSrc = e.getSource();
        int newEdgeDest = e.getDestination();
        double newEdgeReliability = e.getReliability();
        if (newEdgeSrc == source || newEdgeSrc == destination || newEdgeDest == source || newEdgeDest == destination){
            ratio *= (1.0 - newEdgeReliability/1.19); // Random formula I figured, should works to a reasonable degree
        }
    }

    /**
     * Stringify the object.
     *
     * @return A String representing the instance of the object
     */
    public String toString() {
        return source + " <-> " + destination + " Cost: " + cost + " Reliability: " + reliability;
    }
}
