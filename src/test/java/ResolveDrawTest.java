
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.junit.Test;

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

		List<List<String>> biggestCycles = new ArrayList<>();
		List<String> results;
		List<String> l1 = new ArrayList<>(Arrays.asList(new String[]{"TP1", "TP2", "TP4", "TP5", "TP7"}));
		biggestCycles.add(l1);

		List<String> l2 = new ArrayList<>(Arrays.asList(new String[]{"TP1", "TP4", "TP6", "TP2", "TP3"}));
		biggestCycles.add(l2);

		DefaultDirectedWeightedGraph<String, ERList> inputGraph = new DefaultDirectedWeightedGraph<>(ERList.class);
		for (int i = 1; i <= 7; i++) {
			inputGraph.addVertex("TP" + i);
		}

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

		results = SwapSolver.resolveDraw(biggestCycles, inputGraph);
		System.out.println(results);
		Boolean res = true;
		for (String result : results) {
			if (!l1.contains(result)) {
				res = false;
			}
			if (res == false) {
				break;
			}
		}
		assertTrue(res);
	}

}
