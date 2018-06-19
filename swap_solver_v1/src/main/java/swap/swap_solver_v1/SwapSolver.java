package swap.swap_solver_v1;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Spark;

public class SwapSolver {

    public static void main(String[] args) throws JSONException, FileNotFoundException {
        Spark.post("/", (req, res) -> {
            Spark.threadPool(2);
            return resolveExchanges(req.body());
        });
    }

    /**
     * Receives a string representing a json object containing a list of
     * exchange requests from students and returns a list of identifiers of the
     * exchange requests to be actually made
     *
     * @param jsonString a string with textual representation of a json object
     * containing a list of exchange requests from students
     * @return Returns a string with textual representation of a json object
     * containing a list of identifiers (can be an empty list if there are no
     * possible exchanges)
     */
    public static String resolveExchanges(String jsonString) {
        JSONObject receivedJSON = new JSONObject(jsonString);
        JSONArray requests = receivedJSON.getJSONArray("exchange_requests");
        
        List<ExchangeRequest> courseExchangeRequests = parseRequestsFromJSON(requests);
        DefaultDirectedWeightedGraph<String, ERList> graph = buildGraph(courseExchangeRequests);

        TarjanSimpleCycles tsc;
        tsc = new TarjanSimpleCycles();
        tsc.setGraph(graph);
        List<List<String>> cycleList = tsc.findSimpleCycles();

        if (cycleList.size() > 0) {
            //Existem ciclos no grafo
            int longest = cycleList.stream().mapToInt(List::size).max().orElse(-1);
            List<List<String>> biggestCycles = cycleList.stream().filter(x -> x.size() == longest).collect(toList());

            for (List<String> ciclo : biggestCycles) {
                ciclo.add(ciclo.get(0));
            }
            List<String> cycleToSolve;

            if (biggestCycles.size() > 1) {
                // There is a draw in deciding which set of exchanges will be resolved
                cycleToSolve = resolveDraw(biggestCycles, graph);
            } else {
                cycleToSolve = biggestCycles.get(0);
            }

            ArrayList<String> solvedExchanges = new ArrayList<>();
            for (int k = 0; k < cycleToSolve.size() - 1; ++k) {
                ERList pAr = graph.getEdge(cycleToSolve.get(k), cycleToSolve.get(k + 1));
                ExchangeRequest pc = pAr.getMinExchangeRequest();
                solvedExchanges.add("\"" + pc.id + "\"");
            }
            System.out.println(""+Spark.activeThreadCount());
            return "{\"solved_exchanges\":" + solvedExchanges.toString() + "}";
        } else { // There aren't any cycles to solve on the graph 
            return "{\"solved_exchanges\":[]}";
        }
    }

    /**
     * Calculates the timestamp of the exchange request made earlier in the
     * given cycle but isn't earlier than the given minimumCommonDate
     *
     * @param graph the graph in which the cycle is present
     * @param cycle the cycle where the minimum date is to be found
     * @param minimumCommonDate the lower bound for the return value
     * @return Returns the timestamp of the exchange request made earlier in the
     * given cycle but isn't earlier than the given minimumCommonDate
     */
    public static int minimumDateOfCycle(DefaultDirectedWeightedGraph<String, ERList> graph,
            List<String> cycle,
            int minimumCommonDate) {
        int minimumDateOfCycle = Integer.MAX_VALUE;
        for (int k = 0; k < cycle.size() - 1; ++k) {
            ERList erl = graph.getEdge(cycle.get(k), cycle.get(k + 1));
            int minimumDateOfEdge = erl.getMinDate();
            if (minimumDateOfEdge > minimumCommonDate && minimumDateOfEdge < minimumDateOfCycle) {
                minimumDateOfCycle = minimumDateOfEdge;
            }
        }
        /*     At this point we have the guarantee that minimumDateOfCycle is
         * different than Integer.MAX_VALUE
         *     Every edge in a cycle belongs to a different person, therefore
         * every edge has a different minimumDateOfEdge
         *     If at this point minimumDateOfCycle were equal to
         * Integer.MAX_VALUE it would mean that every edge in this cycle would
         * have a minimunDateOfEdge smaller than minimumCommonDate (which is
         * impossible since the number of iterations is limited in the "resolveDraw" method)
         */
        return minimumDateOfCycle;
    }

    /**
     * Converts a List&lt;ExchangeRequest&gt containing exchange requests from
     * students to a directed graph in which each edge has a ERList associated;
     *
     * @param edges list of exchange requests from students
     * @return Returns the constructed directed graph from the exchange requests
     */
    public static DefaultDirectedWeightedGraph buildGraph(List<ExchangeRequest> edges) {
        DefaultDirectedWeightedGraph<String, ERList> graph = new DefaultDirectedWeightedGraph<>(ERList.class);

        for (ExchangeRequest edge : edges) {
            String fromShift = edge.getFrom_shift_id();
            String toShift = edge.getTo_shift_id();
            if (!graph.vertexSet().contains(fromShift)) {
                graph.addVertex(fromShift);
            }
            if (!graph.vertexSet().contains(toShift)) {
                graph.addVertex(toShift);
            }

            ERList erList = graph.getEdge(edge.getFrom_shift_id(), edge.getTo_shift_id());
            if (erList != null) {
                graph.getEdge(edge.getFrom_shift_id(), edge.getTo_shift_id()).addExchangeRequest(edge);
            } else {
                ERList newERList = new ERList();
                newERList.addExchangeRequest(edge);
                graph.addEdge(edge.getFrom_shift_id(), edge.getTo_shift_id(), newERList);
            }
        }
        return graph;
    }

    /**
     * Converts a JSONArray to a List&lt;ExchangeRequest&gt;
     *
     * @param courseExchanges the JSONArray containing exchange requests from
     * students
     * @return Returns a List of ExchangeRequest objects containing the same
     * information as the given parameter
     */
    public static List<ExchangeRequest> parseRequestsFromJSON(JSONArray courseExchanges) {
        ArrayList<ExchangeRequest> requests = new ArrayList<>();
        for (int j = 0; j < courseExchanges.length(); j++) {
            // iterate over every exchange request of a given course
            JSONObject exchange = courseExchanges.getJSONObject(j);
            String fromShift = exchange.get("from_shift_id").toString();
            String toShift = exchange.get("to_shift_id").toString();
            String exchangeID = exchange.get("id").toString();
            Integer requestDate = (Integer) exchange.get("created_at");
            ExchangeRequest er = new ExchangeRequest(requestDate, fromShift, toShift, exchangeID);
            requests.add(er);
        }
        return requests;
    }

    /**
     * Resolves the cycle to be solved when there is a draw in deciding which
     * cycle to solve
     *
     * @param cycles a List of cycles (each cycle is a List of nodes represented
     * by a String)
     * @param graph the graph which contains the cycles given
     * @return Returns the cycle which contains the request(s) made earlier than
     * the requests in other cycles
     */
    private static List<String> resolveDraw(List<List<String>> cycles,
            DefaultDirectedWeightedGraph<String, ERList> graph) {
        int minDate = 0;
        HashMap<Integer, List<List<String>>> cyclesBySize;
        boolean isResolved;
        int maxIters = graph.vertexSet().size() - 1;
        int iter = 0;
        do {
            cyclesBySize = new HashMap<>();
            for (List<String> cycle : cycles) {
                int cycleMinimumDate = minimumDateOfCycle(graph, cycle, minDate);
                if (cyclesBySize.containsKey(cycleMinimumDate)) {
                    cyclesBySize.get(cycleMinimumDate).add(cycle);
                } else {
                    List<List<String>> cycleList = new ArrayList<>();
                    cycleList.add(cycle);
                    cyclesBySize.put(cycleMinimumDate, cycleList);
                }
            }
            minDate = Collections.min(cyclesBySize.keySet());
            isResolved = cyclesBySize.get(minDate).size() == 1;
            if (!isResolved) {
                cycles = cyclesBySize.get(minDate);
            }
        } while (!isResolved && (++iter != maxIters));
        if (iter == maxIters) {
            System.out.println("\nmaximum number of iterations reached!!!\n");
        }
        return cyclesBySize.get(minDate).get(0);
    }

    public static boolean isEqualGraph(DefaultDirectedWeightedGraph graphA, DefaultDirectedWeightedGraph graphB) {
        Set<ERList> edgesA = graphA.edgeSet();
        Set<ERList> edgesB = graphB.edgeSet();
        Set<String> vertexA = graphA.vertexSet();
        Set<String> vertexB = graphB.vertexSet();
        int j = 0;
        boolean res = true;

        for (ERList erA : edgesA) {
            j = 0;
            for (ERList erB : edgesB) {
                if (erB.equalsERL(erA)) {
                    j = -1;
                    break;
                }
            }
            if (j == 0) {
                res = false;
                break;
            }
        }
        System.out.println(vertexA.equals(vertexB));
        return res && (vertexA.equals(vertexB));
    }
}
