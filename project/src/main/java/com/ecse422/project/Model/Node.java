package com.ecse422.project.Model;

public class Node {
    private int source;
    private int destination;
    private int cost;
    private double reliability;

    public Node(int source, int destination, int cost, double reliability) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
        this.reliability = reliability;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public int getCost() {
        return cost;
    }

    public double getReliability() {
        return reliability;
    }

    public String toString() {
        return source + " <-> " + destination + " Cost: " + cost + " Reliability: " + reliability;
    }
}
