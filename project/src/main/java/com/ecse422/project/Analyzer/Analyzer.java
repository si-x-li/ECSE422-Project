package com.ecse422.project.Analyzer;

import com.ecse422.project.Model.Edge;
import com.ecse422.project.Model.Model;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.*;

public class Analyzer {
    /**
     * Optimizes a model based on the specified parameters.
     *
     * @param model             A model to be optimized
     * @param targetReliability The reliability target
     * @param targetCost        The cost target
     * @return A List of edges that fits the design criteria
     */
    public static Set<Edge> optimize(Model model, double targetReliability, int targetCost) {
        Set<Edge> edges = new HashSet();

        if (targetReliability < 0) {
            // Maximize reliability subject to given cost
            System.out.println("MAXIMIZE RELIABILITY SUBJECT TO COST");

            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true);
            edges.addAll(mst.getEdges());
            maximizeReliability(model, edges, targetCost);
        } else if (targetCost == 0) {
            // Reach target reliability
            System.out.println("REACH TARGET RELIABILITY");

            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), false);
            edges.addAll(mst.getEdges());
            reachReliability(model, edges, targetReliability);
        } else {
            // Reach target reliability subject to given cost
            System.out.println("REACH TARGET RELIABILITY SUBJECT TO COST");

            MST mst = new MST(model.getNumOfNodes(), model.getCost(), model.getReliability(), true);
            edges.addAll(mst.getEdges());
            reachCostAndReliability(model, edges, targetCost, targetReliability);
        }
        return edges;
    }

    /**
     * Removes edges that are already chosen.
     *
     * @param completeEdges A set of all edges
     * @param edges         A set of currently chosen edges
     * @return A set of edges that are left to chose
     */
    private static Set<Edge> filterCompleteEdges(Set<Edge> completeEdges, Set<Edge> edges) {
        HashSet<Edge> filteredEdges = new HashSet(completeEdges);

        // Remove from completeEdges, edges that have already been used
        for (Edge edge : edges) {
            for (Edge edgeInComplete : completeEdges) {
                if ((edge.getSource() == edgeInComplete.getSource() &&
                        edge.getDestination() == edgeInComplete.getDestination()) ||
                        (edge.getSource() == edgeInComplete.getDestination() &&
                                edge.getDestination() == edgeInComplete.getSource())) {
                    filteredEdges.remove(edgeInComplete);
                    for (Edge e: filteredEdges){
                        e.updateRatio(edgeInComplete);
                    }
                }
            }
        }

        return filteredEdges;
    }

    /**
     * Picks the best edge to add based on reliability/cost ratio.
     *
     * @param edges A set of possible edges
     * @return The best Edge from a ratio (reliability / cost) perspective.
     */
    private static Edge pickBestEdge(Set<Edge> edges) {
        Edge toAdd = null;
        double ratio = 0.0;
        for (Edge edge : edges) {
            double newRatio = edge.getRatio();
            if (newRatio > ratio) {
                toAdd = edge;
                ratio = newRatio;
            }
        }
        return toAdd;
    }

    /**
     * Reaches a target reliability.
     *
     * @param model             The model object
     * @param edges             A set of edges
     * @param targetReliability The target reliability
     */
    public static void reachReliability(Model model, Set<Edge> edges, double targetReliability) {
        HashSet<Edge> completeEdges = (HashSet) model.getAllEdges();
        HashSet<Edge> filteredEdges = (HashSet) filterCompleteEdges(completeEdges, edges);
        double reliability = computeReliability(model.getNumOfNodes(), edges);

        while (reliability < targetReliability) {
            Edge toAdd = pickBestEdge(filteredEdges);

            if (toAdd == null) {
                break;
            }
            edges.add(toAdd);
            filteredEdges.remove(toAdd);
            for (Edge e : filteredEdges){
                e.updateRatio(toAdd);
            }
            reliability = computeReliability(model.getNumOfNodes(), edges);
        }

        if (reliability < targetReliability) {
            System.out.println("Could not reach target reliability of: " + targetReliability);
        }
        reliability = computeReliability(model.getNumOfNodes(), edges);
        System.out.println("Reliability: " + reliability);
        System.out.println("Cost: " + computeCost(edges));
        System.out.println("Graph: " + edges.toString());
    }

    /**
     * Maximizes the reliability. Since the problem is NP-Hard, maximization is based on reliability-cost ratio.
     *
     * @param model      The model object
     * @param edges      The set of edges
     * @param targetCost The maximum cost
     */
    public static void maximizeReliability(Model model, Set<Edge> edges, int targetCost) {
        HashSet<Edge> completeEdges = (HashSet) model.getAllEdges();
        HashSet<Edge> filteredEdges = (HashSet) filterCompleteEdges(completeEdges, edges);

        while (true) {
            Edge toAdd = pickBestEdge(filteredEdges);

            // No more nodes to add
            if (toAdd == null) {
                break;
            }

            // Exceeded cost
            if ((computeCost(edges) + toAdd.getCost()) > targetCost) {
                filteredEdges.remove(toAdd);
                continue;
            }

            edges.add(toAdd);
            filteredEdges.remove(toAdd);
            for (Edge e : filteredEdges){
                e.updateRatio(toAdd);
            }
        }
        double reliability = computeReliability(model.getNumOfNodes(), edges);
        System.out.println("Maximum reliability: " + reliability);
        System.out.println("Cost of graph: " + computeCost(edges));
        System.out.println("Graph: " + edges.toString());
    }

    /**
     * Reaches a target reliability given cost constraint.
     *
     * @param model             The model object
     * @param edges             The set of edges
     * @param targetCost        The maximum cost
     * @param targetReliability The target reliability
     */
    public static void reachCostAndReliability(Model model, Set<Edge> edges, int targetCost, double targetReliability) {
        HashSet<Edge> completeEdges = (HashSet) model.getAllEdges();
        HashSet<Edge> filteredEdges = (HashSet) filterCompleteEdges(completeEdges, edges);
        double reliability = computeReliability(model.getNumOfNodes(), edges);

        while (reliability < targetReliability) {
            Edge toAdd = pickBestEdge(filteredEdges);

            // No new edges found
            if (toAdd == null) {
                break;
            }

            // Exceeded cost
            if ((computeCost(edges) + toAdd.getCost()) > targetCost) {
                filteredEdges.remove(toAdd);
                continue;
            }

            edges.add(toAdd);
            filteredEdges.remove(toAdd);
            reliability = computeReliability(model.getNumOfNodes(), edges);
            for (Edge e : filteredEdges){
                e.updateRatio(toAdd);
            }
        }

        reliability = computeReliability(model.getNumOfNodes(), edges);
        if (reliability < targetReliability) {
            System.out.println("Could not reach reliability given cost");
        }
        System.out.println("Reliability: " + reliability);
        System.out.println("Cost: " + computeCost(edges));
        System.out.println("Graph: " + edges.toString());
    }

    /**
     * Computes the network reliability.
     *
     * @param numOfNodes Number of nodes
     * @param edges      Possible edges in a network
     * @return The network reliability
     */
    public static double computeReliability(int numOfNodes, Set<Edge> edges) {
        return computeReliability(numOfNodes, new ArrayList(edges));
    }

    /**
     * Computes the network reliability by approximating.
     *
     * @param numOfNodes Number of nodes
     * @param edges      Possible edges in a network
     * @return The network reliability
     */
    public static double computeReliability(int numOfNodes, List<Edge> edges) {
        double reliability = 0.0;

        // Uniquely enumerate the edges
        Set<Integer> edgesSet = new HashSet<>();
        for (int i = 0; i < edges.size(); i++) {
            edgesSet.add(new Integer(i));
        }

        if (edges.size() > 35) {
            for (int i = edges.size() - 5; i <= edges.size(); i++) {
                System.out.println("Computing " + i + " out of " + edges.size());
                Set<Set<Integer>> combinations = Sets.combinations(edgesSet, i);
                reliability += computeReliabilityOfSubgraph(numOfNodes, edges, combinations, edgesSet);
            }
        } else if (edges.size() > 29) {
            for (int i = edges.size() - 6; i <= edges.size(); i++) {
                System.out.println("Computing " + i + " out of " + edges.size());
                Set<Set<Integer>> combinations = Sets.combinations(edgesSet, i);
                reliability += computeReliabilityOfSubgraph(numOfNodes, edges, combinations, edgesSet);
            }
        } else if (edges.size() > 22) {
            for (int i = edges.size() - 7; i <= edges.size(); i++) {
                System.out.println("Computing " + i + " out of " + edges.size());
                Set<Set<Integer>> combinations = Sets.combinations(edgesSet, i);
                reliability += computeReliabilityOfSubgraph(numOfNodes, edges, combinations, edgesSet);
            }
        } else {
            for (int i = (numOfNodes - 1); i <= edges.size(); i++) {
                System.out.println("Computing " + i + " out of " + edges.size());
                // If the number of combinations exceeds the maximum integer value skip this
                if (factorial(edges.size()) / (factorial(edges.size() - i) * factorial(i)) > Integer.MAX_VALUE) {
                    continue;
                }
                Set<Set<Integer>> combinations = Sets.combinations(edgesSet, i);
                reliability += computeReliabilityOfSubgraph(numOfNodes, edges, combinations, edgesSet);
            }
        }


        return reliability;
    }

    /**
     * @deprecated
     * Computes the network reliability by approximating.
     *
     * @param numOfNodes Number of nodes
     * @param edges      Possible edges in a network
     * @return The network reliability
     */
    public static double fastComputeReliability(int numOfNodes, Set<Edge> edges) {
        return fastComputeReliability(numOfNodes, new ArrayList(edges));
    }

    /**
     * @deprecated
     * Computes the network reliability by approximating.
     *
     * @param numOfNodes Number of nodes
     * @param edges      Possible edges in a network
     * @return The network reliability
     */
    public static double fastComputeReliability(int numOfNodes, List<Edge> edges) {
        double reliability = 0.0;

        // Uniquely enumerate the edges
        HashSet<Integer> edgesSet = new HashSet<>();
        for (int i = 0; i < edges.size(); i++) {
            edgesSet.add(new Integer(i));
        }

        if (edges.size() > 7) {
            // Approximate reliability using up to E - 6 edges
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
     * Computes the network reliability for a set of subgraphs using parallel processing.
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
        // Split task across n cores
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        if (availableProcessors < 1) {
            availableProcessors = 1;
        }

        List<Set<Set<Integer>>> partitions = new ArrayList(availableProcessors);
        for (int i = 0; i < availableProcessors; i++) {
            partitions.add(new HashSet<>());
        }

        int count = 0;
        for (Set<Integer> combination : combinations) {
            partitions.get(count++ % (availableProcessors)).add(combination);
        }

        double reliability = 0.0;

        ExecutorService pool = Executors.newFixedThreadPool(availableProcessors);
        for (int i = 0; i < availableProcessors; i++) {
            try {
                reliability += (pool.submit(new ComputationReliability(numOfNodes, edges, partitions.get(i), edgesSet))).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pool.shutdown();
        return reliability;
    }

    /**
     * Performs a breadth-first-search to see if the entire graph is reachable.
     *
     * @param numOfNodes Number of nodes in the graph
     * @param edges      A list of edges
     * @return True if all nodes can be reached. False otherwise.
     */
    public static boolean bfsGraph(int numOfNodes, Set<Edge> edges) {
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
    public static boolean bfsGraph(int numOfNodes, List<Edge> edges) {
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
    public static int computeCost(Set<Edge> edges) {
        return computeCost(new ArrayList(edges));
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
    private static double factorial(int n) {
        double output = 1;
        for (int i = 1; i <= n; i++) {
            output *= i;
        }
        return output;
    }
}

/**
 * Employs Callable to parallel process reliability computation.
 */
class ComputationReliability implements Callable<Double> {
    private volatile int numOfNodes;
    private volatile List<Edge> edges;
    private volatile Set<Set<Integer>> combinations;
    private volatile Set<Integer> edgesSet;

    public ComputationReliability(int numOfNodes,
                                  List<Edge> edges,
                                  Set<Set<Integer>> combinations,
                                  Set<Integer> edgesSet) {
        this.numOfNodes = numOfNodes;
        this.edges = edges;
        this.combinations = combinations;
        this.edgesSet = edgesSet;
    }

    /**
     * Computes the reliability using callback.
     *
     * @return The reliability of the network
     */
    public Double call() {
        double reliability = 0.0;
        int count = 0;
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
            if (!Analyzer.bfsGraph(numOfNodes, potentialGraph)) {
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
}
