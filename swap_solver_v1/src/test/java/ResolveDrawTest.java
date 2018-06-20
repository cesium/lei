
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import swap.swap_solver_v1.ERList;
import swap.swap_solver_v1.ExchangeRequest;
import swap.swap_solver_v1.SwapSolver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Bruno
 */
public class ResolveDrawTest {

    @Test
    public void teste1() {
        List<List<String>> biggestCycles = null;
        List<String> results;
        List<String> l1 = null;
        l1.add("TP1");
        l1.add("TP2");
        l1.add("TP4");
        l1.add("TP5");
        l1.add("TP7");
        biggestCycles.add(l1);

        List<String> l2 = null;
        l1.add("TP1");
        l1.add("TP4");
        l1.add("TP6");
        l1.add("TP2");
        l1.add("TP3");
        biggestCycles.add(l2);
        List<String> expected = null;
        expected.add("a1");
        expected.add("a7");
        expected.add("a5");
        expected.add("a10");
        expected.add("a11");
        DefaultDirectedWeightedGraph<String, ERList> inputGraph = new DefaultDirectedWeightedGraph<>(ERList.class);
        inputGraph.addVertex("TP1");
        inputGraph.addVertex("TP2");
        inputGraph.addVertex("TP3");
        inputGraph.addVertex("TP4");
        inputGraph.addVertex("TP5");
        inputGraph.addVertex("TP6");
        inputGraph.addVertex("TP7");
        ExchangeRequest er;
        
        er = new ExchangeRequest(1, "TP1", "TP2", "a1");
        ERList tp1_tp2 = new ERList();
        tp1_tp2.addExchangeRequest(er);
        inputGraph.addEdge("TP1", "TP2", tp1_tp2);
        
        er = new ExchangeRequest(1, "TP1", "TP4", "a2");
        ERList tp1_tp4 = new ERList();
        tp1_tp4.addExchangeRequest(er);
        inputGraph.addEdge("TP1", "TP4", tp1_tp4);
        
        er = new ExchangeRequest(20, "TP2", "TP3", "a3");
        ERList tp2_tp3 = new ERList();
        tp2_tp3.addExchangeRequest(er);
        inputGraph.addEdge("TP2", "TP3", tp2_tp3);
        
        er = new ExchangeRequest(3, "TP3", "TP1", "a4");
        ERList tp3_tp1 = new ERList();
        tp3_tp1.addExchangeRequest(er);
        inputGraph.addEdge("TP3", "TP1", tp3_tp1);
        
        er = new ExchangeRequest(4, "TP4", "TP5", "a5");
        ERList tp4_tp5 = new ERList();
        tp4_tp5.addExchangeRequest(er);
        inputGraph.addEdge("TP4", "TP5", tp4_tp5);
        
        er = new ExchangeRequest(5, "TP5", "TP1", "a6");
        ERList tp5_tp1 = new ERList();
        tp5_tp1.addExchangeRequest(er);
        inputGraph.addEdge("TP5", "TP1", tp5_tp1);
        
        er = new ExchangeRequest(2, "TP2", "TP4", "a7");
        ERList tp2_tp4 = new ERList();
        tp2_tp4.addExchangeRequest(er);
        inputGraph.addEdge("TP2", "TP4", tp2_tp4);
        
        er = new ExchangeRequest(4, "TP4", "TP6", "a8");
        ERList tp4_tp6 = new ERList();
        tp4_tp6.addExchangeRequest(er);
        inputGraph.addEdge("TP4", "TP6", tp4_tp6);
        
        er = new ExchangeRequest(6, "TP6", "TP2", "a9");
        ERList tp6_tp2 = new ERList();
        tp6_tp2.addExchangeRequest(er);
        inputGraph.addEdge("TP6", "TP2", tp6_tp2);
        
        er = new ExchangeRequest(5, "TP5", "TP7", "a10");
        ERList tp5_tp7 = new ERList();
        tp5_tp7.addExchangeRequest(er);
        inputGraph.addEdge("TP5", "TP7", tp5_tp7);
        
        er = new ExchangeRequest(7, "TP7", "TP1", "a11");
        ERList tp7_tp1 = new ERList();
        tp7_tp1.addExchangeRequest(er);
        inputGraph.addEdge("TP7", "TP1", tp7_tp1);
        
        results = SwapSolver.resolveDraw(biggestCycles, inputGraph);
        Boolean res = true;
        for (String result : results) {
            if (!expected.contains(result)) {
                res = false;
                break;
            }
        }
        assertTrue(res);
    }

}
