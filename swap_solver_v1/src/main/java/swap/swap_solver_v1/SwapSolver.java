
package swap.swap_solver_v1;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Spark;

public class SwapSolver {
    public static void main(String[] args) throws JSONException, FileNotFoundException {
        Spark.post("/", (req,res) -> {
            String received = req.body();
            JSONObject receivedJSON = new JSONObject(received);
            JSONArray requests = receivedJSON.getJSONArray("exchange_requests");
                
            ArrayList<ExchangeRequest> courseExchangeRequests = parseRequestsFromJSON(requests);
            DefaultDirectedWeightedGraph<String,ERList> graph = buildGraph(courseExchangeRequests);            

            TarjanSimpleCycles tsc;
            tsc=new TarjanSimpleCycles(); 
            tsc.setGraph(graph);
            List<List<String> > cycleList= tsc.findSimpleCycles();

            if(cycleList.size()>0){
            //Existem ciclos no grafo
                int longest=cycleList.stream().mapToInt(List::size).max().orElse(-1);
                List<List<String>> biggestCycles= cycleList.stream().filter(x->x.size()==longest).collect(toList());

                for(List<String> ciclo : biggestCycles) ciclo.add(ciclo.get(0));
                List<String> cycleToSolve;

                if(biggestCycles.size()>1){
                 // There is a draw in deciding which set of exchanges will be resolved
                    cycleToSolve = resolveDraw(biggestCycles,graph);
                }
                else{
                    cycleToSolve = biggestCycles.get(0);
                }
                
                ArrayList<String> solvedExchanges = new ArrayList<>();
                for(int k=0;k<cycleToSolve.size()-1;++k){
                    ERList pAr=graph.getEdge(cycleToSolve.get(k),cycleToSolve.get(k+1));
                    ExchangeRequest pc=pAr.getMinExchangeRequest();
                    solvedExchanges.add(pc.id);
                }
                return "{solved_exchanges:" + solvedExchanges.toString() + "}";
            }
            else{ // There aren't any cycles to solve on the graph 
                return "{solved_exchanges:[]}";
            }
            
        });    
    }
    
    private static int minimumDateOfCycle(DefaultDirectedWeightedGraph<String, ERList> graph,
                                            List<String> cycle,
                                            int minimumCommonDate) {
        int minimumDateOfCycle = Integer.MAX_VALUE;
        for(int k=0;k<cycle.size()-1;++k){
            ERList erl = graph.getEdge(cycle.get(k),cycle.get(k+1));
            int minimumDateOfEdge = erl.getMinDate();
            if( minimumDateOfEdge > minimumCommonDate && minimumDateOfEdge < minimumDateOfCycle){
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
         * impossible since we limit the number of iterations)
        */
        return minimumDateOfCycle;
    }
    
    private static DefaultDirectedWeightedGraph<String, ERList> buildGraph(ArrayList<ExchangeRequest> edges){
        DefaultDirectedWeightedGraph<String,ERList> graph = new DefaultDirectedWeightedGraph<>(ERList.class);
        
        for(ExchangeRequest edge: edges){
            String fromShift = edge.getFrom_shift_id();
            String toShift = edge.getTo_shift_id();
            if(!graph.vertexSet().contains(fromShift)){
                graph.addVertex(fromShift);
            }
            if(!graph.vertexSet().contains(toShift)){
                graph.addVertex(toShift);
            }
            
            ERList erList=graph.getEdge(edge.getFrom_shift_id(),edge.getTo_shift_id());
            if(erList!=null){
                graph.getEdge(edge.getFrom_shift_id(),edge.getTo_shift_id()).addExchangeRequest(edge);
            }
            else{
                ERList newERList= new ERList();
                newERList.addExchangeRequest(edge);
                graph.addEdge(edge.getFrom_shift_id(),edge.getTo_shift_id(),newERList);
            }     
        }   
        return graph;
    }

    private static ArrayList<ExchangeRequest> parseRequestsFromJSON(JSONArray courseExchanges) {
        ArrayList<ExchangeRequest> requests = new ArrayList<>();
        for(int j=0;j<courseExchanges.length();j++){ 
            // iterate over every exchange request of a given course
            JSONObject exchange = courseExchanges.getJSONObject(j); 
            String fromShift=exchange.get("from_shift_id").toString();
            String toShift=exchange.get("to_shift_id").toString();
            String exchangeID=exchange.get("id").toString();
            Integer requestDate=(Integer) exchange.get("created_at");
            ExchangeRequest er= new ExchangeRequest(requestDate, fromShift,toShift,exchangeID);
            requests.add(er);
        }
        return requests;
    }

    private static List<String> resolveDraw(List<List<String>> cycles,
                                            DefaultDirectedWeightedGraph<String,ERList> graph) {
        int minDate =0;
        HashMap<Integer,List<List<String>>> cyclesBySize;
        boolean isResolved;
        int maxIters = graph.vertexSet().size() -1;
        int iter = 0;
        do{
            cyclesBySize = new HashMap<>();
            for(List<String> cycle: cycles){
                int cycleMinimumDate=minimumDateOfCycle(graph,cycle,minDate);
                if(cyclesBySize.containsKey(cycleMinimumDate)){
                    cyclesBySize.get(cycleMinimumDate).add(cycle);
                }
                else{
                    List<List<String>> cycleList = new ArrayList<>();
                    cycleList.add(cycle);
                    cyclesBySize.put(cycleMinimumDate, cycleList);
                }
            }
            minDate = Collections.min(cyclesBySize.keySet());
            isResolved = cyclesBySize.get(minDate).size() == 1;
            if(!isResolved){
                cycles = cyclesBySize.get(minDate);
            }
        }
        while(!isResolved && (++iter != maxIters));
        if(iter == maxIters) System.out.println("\nmaximum number of iterations reached!!!\n");
        return cyclesBySize.get(minDate).get(0);
    }
}
