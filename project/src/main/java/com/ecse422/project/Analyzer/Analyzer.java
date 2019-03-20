package com.ecse422.project.Analyzer;

import com.ecse422.project.Model.Edge;
import com.ecse422.project.Model.Model;
import com.google.common.collect.Sets;

import java.util.*;

public class Analyzer {
    /**
     * Optimizes a model based on the specified parameters.
     *
     * @param model             A model to be optimized
     * @param targetReliability The reliability target
     * @param targetCost        The cost target
     * @return A List of edges that fits the design criteria
     */
    public static List<Edge> optimize(Model model, double targetReliability, int targetCost) {
        List<Edge> edges = new ArrayList();
        Set<Edge> completeEdges = model.getCompleteEdgeSet();
        Set<Edge> maximizedSet;
        int maxCost = Math.min(targetCost, sumEdgeCost(completeEdges));
        int minCost = targetCost - model.getCheapestEdgeCost();

        if (targetReliability < 0) {
            // Maximize reliability subject to given cost
            // TODO maximize reliability subject to given cost
            System.out.println("MAXIMIZE RELIABILITY SUBJECT TO COST");

//            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true);
//            edges.addAll(mst.getEdges());

            try {
                maximizedSet = maximizeReliability(model.getNumOfNodes(),
                        maxCost - model.getCheapestEdgeCost(), maxCost, completeEdges);
                edges.addAll(maximizedSet);
            } catch (Exception e) {
                System.out.printf("There is no solution %s\n", e.toString());
            }


        } else if (targetCost == 0) {
            // Reach target reliability
            // TODO reach target reliability
            System.out.println("REACH TARGET RELIABILITY");

            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), false);
            edges.addAll(mst.getEdges());
            Set<Edge> e = new HashSet<Edge>(edges);
            while (computeReliability(model.getNumOfNodes(), e) < targetReliability) {
                addNextEdge(model.getNumOfNodes(), targetCost, completeEdges, e);
            }
            edges = new ArrayList<Edge>(e);

//            try {
//                maximizedSet = maximizeReliability(model.getNumOfNodes(),
//                        maxCost - model.getCheapestEdgeCost(), maxCost, completeEdges);
//                edges.addAll(maximizedSet);
//            } catch (Exception e){
//                System.out.println("There is no solution");
//            }

        } else {
            // Reach target reliability subject to given cost
            // TODO reach target reliability subject to given cost
            System.out.println("REACH TARGET RELIABILITY SUBJECT TO COST");

//            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true);
//            edges.addAll(mst.getEdges());
//            try {
            maximizedSet = maximizeReliability(model.getNumOfNodes(),
                    maxCost - model.getCheapestEdgeCost(), maxCost, completeEdges);
            edges.addAll(maximizedSet);
//            } catch (Exception e) {
//                System.out.println("There is no solution");
//            } // FIXME

            if (computeReliability(model.getNumOfNodes(), edges) < targetReliability) {
                System.out.println("There is no solution");
            }

        }

        System.out.println(computeReliability(model.getNumOfNodes(), edges));
        System.out.println(computeCost(edges));
        System.out.println(edges.toString());

        return edges;
    }


    private static boolean addNextEdge(int numOfNodes, int targetCost, Set<Edge> completeEdges, Set<Edge> currentEdges) {
        Sets.SetView<Edge> difference = Sets.difference(completeEdges, currentEdges);
        double maxReliability = 0.;
        Edge bestEdge = null;
        for (Edge e : difference) {
            currentEdges.add(e);
            double r = computeReliability(numOfNodes, currentEdges);
            if (r > maxReliability) {
                maxReliability = r;
                bestEdge = e;
            }
            currentEdges.remove(e);
        }
        if (bestEdge != null) {
            currentEdges.add(bestEdge);
            return true;
        }
        System.out.print(currentEdges.toString());
        throw new RuntimeException("Ran out of edges");
    }


    private static Set<Edge> maximizeReliability(int numOfNodes, int minCost, int maxCost, Set<Edge> edges) {
        double maximizedReliability = 0.;
        Set<Edge> maximizedEdgeSet = null;
        Set<Set<Edge>> combinations = Sets.powerSet(edges);
        for (Set<Edge> combination : combinations) {
            int cost = sumEdgeCost(combination);
            if (cost <= minCost || cost > maxCost) {
                continue;
            }
            if (!bfsGraph(numOfNodes, combination)) {
                continue;
            }
            double reliability = computeReliability(numOfNodes, combination);
            if (reliability > maximizedReliability) {
                maximizedReliability = reliability;
                maximizedEdgeSet = combination;
            }
        }
        return maximizedEdgeSet;
    }


    private static int sumEdgeCost(Set<Edge> edges) {
        int sum = 0;
        for (Edge edge : edges) {
            sum += edge.getCost();
        }
        return sum;
    }

    /**
     * Computes the network reliability.
     *
     * @param numOfNodes Number of nodes
     * @param edges      Possible edges in a network
     * @return The network reliability
     */
    public static double computeReliability(int numOfNodes, Set<Edge> edges) {
        List<Edge> e = new ArrayList<>(edges);
        return computeReliability(numOfNodes, e);
    }

    /**
     * Computes the network reliability.
     *
     * @param numOfNodes Number of nodes
     * @param edges      Possible edges in a network
     * @return The network reliability
     */
    public static double computeReliability(int numOfNodes, List<Edge> edges) {
        double reliability = 0.0;

        // Uniquely enumerate the edges
        Set<Integer> edgesSet = new HashSet();
        for (int i = 0; i < edges.size(); i++) {
            edgesSet.add(new Integer(i));
        }

        if (edges.size() > 7) {
            // Approximate reliability using up to E - 6 edges\
            for (int i = edges.size() - 6; i <= edges.size(); i++) {
                Set<Set<Integer>> combinations = Sets.combinations(edgesSet, i);
                reliability += computeReliabilityOfSubgraph(numOfNodes, edges, combinations, edgesSet);
            }
        } else {
            // Use full powerset to compute reliability for graphs with less than 5 edges
            Set<Set<Integer>> combinations = Sets.powerSet(edgesSet);
            reliability = computeReliabilityOfSubgraph(numOfNodes, edges, combinations, edgesSet);
        }
        return reliability;
    }

    /**
     * Computes the network reliability for a set of subgraphs.
     *
     * @param numOfNodes   Number of Nodes
     * @param edges        Possible edges in a network
     * @param combinations Set of combinations with E edges
     * @param edgesSet     Set of numbered edges in the network
     * @return The reliability of the subgraph
     */
    private static double computeReliabilityOfSubgraph(int numOfNodes,
                                                       List<Edge> edges,
                                                       Set<Set<Integer>> combinations,
                                                       Set<Integer> edgesSet) {
        double reliability = 0.0;
        for (Set<Integer> combination : combinations) {
            // Checks that all nodes are connected
            boolean[] set = new boolean[numOfNodes];
            for (Integer edge : combination) {
                set[edges.get(edge).getSource()] = true;
                set[edges.get(edge).getDestination()] = true;
            }

            boolean skip = false;
            for (int i = 0; i < numOfNodes; i++) {
                if (!set[i]) {
                    skip = true;
                    break;
                }
            }

            if (skip) {
                continue;
            }

            // Checks that all nodes can be reached
            List<Edge> potentialGraph = new ArrayList();
            for (Integer edge : combination) {
                potentialGraph.add(edges.get(edge));
            }
            if (!bfsGraph(numOfNodes, potentialGraph)) {
                continue;
            }

            double tempReliability = 1.0;
            // Calculate reliability
            for (Integer edge : edgesSet) {
                if (combination.contains(edge)) {
                    // Edge is turned on
                    tempReliability *= edges.get(edge).getReliability();
                } else {
                    // Edge is turned off
                    tempReliability *= 1 - edges.get(edge).getReliability();
                }
            }
            reliability += tempReliability;
        }
        return reliability;
    }

    /**
     * Performs a breadth-first-search to see if the entire graph is reachable.
     *
     * @param numOfNodes Number of nodes in the graph
     * @param edges      A list of edges
     * @return True if all nodes can be reached. False otherwise.
     */
    private static boolean bfsGraph(int numOfNodes, Set<Edge> edges) {
        List<Edge> e = new ArrayList<>(edges);
        return bfsGraph(numOfNodes, e);
    }


    /**
     * Performs a breadth-first-search to see if the entire graph is reachable
     *
     * @param numOfNodes Number of nodes in the graph
     * @param edges      A list of edges
     * @return True if all nodes can be reached. False otherwise
     */
    private static boolean bfsGraph(int numOfNodes, List<Edge> edges) {
        boolean visited[] = new boolean[numOfNodes];

        Queue<Integer> queue = new LinkedList();
        queue.add(edges.get(0).getSource());

        while (queue.size() != 0) {
            int node = queue.remove();
            visited[node] = true;
            for (Edge edge : edges) {
                if (edge.getSource() == node && !visited[edge.getDestination()]) {
                    queue.add(edge.getDestination());
                } else if (edge.getDestination() == node && !visited[edge.getSource()]) {
                    queue.add(edge.getSource());
                }
            }
        }

        // Checks that all nodes are reachable, i.e. no isolated pockets
        for (boolean aVisited : visited) {
            if (!aVisited) {
                return false;
            }
        }

        return true;
    }

    /**
     * Computes cost of the network.
     *
     * @param edges A list of edges
     * @return The total cost of the network
     */
    public static int computeCost(List<Edge> edges) {
        int cost = 0;
        for (Edge edge : edges) {
            cost += edge.getCost();
        }
        return cost;
    }

    /**
     * Calculates the factorial of a number.
     *
     * @param n
     * @return The factorial of the input number
     */
    private static int factorial(int n) {
        int output = 1;
        for (int i = 1; i <= n; i++) {
            output *= i;
        }
        return output;
    }
}
