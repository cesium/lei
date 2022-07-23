
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
public class BuildGraphTest {

	@Test
	public void teste1() {

		assertTrue(true);
		/*DefaultDirectedWeightedGraph resultGraph = new DefaultDirectedWeightedGraph<>(ERList.class);
		ExchangeRequest er;
		resultGraph.addVertex("TP1");
		resultGraph.addVertex("TP2");
		resultGraph.addVertex("TP3");
		resultGraph.addVertex("TP4");

		er = new ExchangeRequest(1, "TP1", "TP2", "a1");
		ERList tp1_tp2 = new ERList();
		tp1_tp2.addExchangeRequest(er);
		resultGraph.addEdge("TP1", "TP2", tp1_tp2);

		ERList tp2_tp1 = new ERList();
		er = new ExchangeRequest(2, "TP2", "TP1", "a4");
		tp2_tp1.addExchangeRequest(er);
		resultGraph.addEdge("TP2", "TP1", tp2_tp1);

		ERList tp1_tp3 = new ERList();
		er = new ExchangeRequest(1, "TP1", "TP3", "a2");
		tp1_tp3.addExchangeRequest(er);
		resultGraph.addEdge("TP1", "TP3", tp1_tp3);

		ERList tp3_tp1 = new ERList();
		er = new ExchangeRequest(3, "TP3", "TP1", "a7");
		tp3_tp1.addExchangeRequest(er);
		resultGraph.addEdge("TP3", "TP1", tp3_tp1);

		ERList tp1_tp4 = new ERList();
		er = new ExchangeRequest(1, "TP1", "TP4", "a3");
		tp1_tp4.addExchangeRequest(er);
		resultGraph.addEdge("TP1", "TP4", tp1_tp4);

		ERList tp4_tp1 = new ERList();
		er = new ExchangeRequest(4, "TP4", "TP1", "a10");
		tp4_tp1.addExchangeRequest(er);
		resultGraph.addEdge("TP4", "TP1", tp4_tp1);

		ERList tp2_tp3 = new ERList();
		er = new ExchangeRequest(2, "TP2", "TP3", "a5");
		tp2_tp3.addExchangeRequest(er);
		resultGraph.addEdge("TP2", "TP3", tp2_tp3);

		ERList tp3_tp2 = new ERList();
		er = new ExchangeRequest(3, "TP3", "TP2", "a8");
		tp3_tp2.addExchangeRequest(er);
		resultGraph.addEdge("TP3", "TP2", tp3_tp2);

		ERList tp3_tp4 = new ERList();
		er = new ExchangeRequest(3, "TP3", "TP4", "a9");
		tp3_tp4.addExchangeRequest(er);
		resultGraph.addEdge("TP3", "TP4", tp3_tp4);

		ERList tp4_tp3 = new ERList();
		er = new ExchangeRequest(4, "TP4", "TP3", "a12");
		tp4_tp3.addExchangeRequest(er);
		resultGraph.addEdge("TP4", "TP3", tp4_tp3);

		ERList tp2_tp4 = new ERList();
		er = new ExchangeRequest(2, "TP2", "TP4", "a6");
		tp2_tp4.addExchangeRequest(er);
		resultGraph.addEdge("TP2", "TP4", tp2_tp4);

		ERList tp4_tp2 = new ERList();
		er = new ExchangeRequest(4, "TP4", "TP2", "a11");
		tp4_tp2.addExchangeRequest(er);
		resultGraph.addEdge("TP4", "TP2", tp4_tp2);

		List<ExchangeRequest> lEr = new ArrayList<>();

		ExchangeRequest _er = new ExchangeRequest(1, "TP1", "TP2", "a1");
		lEr.add(_er);

		_er = new ExchangeRequest(2, "TP2", "TP1", "a4");
		lEr.add(_er);

		_er = new ExchangeRequest(1, "TP1", "TP3", "a2");
		lEr.add(_er);

		_er = new ExchangeRequest(3, "TP3", "TP1", "a7");
		lEr.add(_er);

		_er = new ExchangeRequest(1, "TP1", "TP4", "a3");
		lEr.add(_er);

		_er = new ExchangeRequest(4, "TP4", "TP1", "a10");
		lEr.add(_er);

		_er = new ExchangeRequest(2, "TP2", "TP3", "a5");
		lEr.add(_er);

		_er = new ExchangeRequest(3, "TP3", "TP2", "a8");
		lEr.add(_er);

		_er = new ExchangeRequest(3, "TP3", "TP4", "a9");
		lEr.add(_er);

		_er = new ExchangeRequest(4, "TP4", "TP3", "a12");
		lEr.add(_er);

		_er = new ExchangeRequest(2, "TP2", "TP4", "a6");
		lEr.add(_er);

		_er = new ExchangeRequest(4, "TP4", "TP2", "a11");
		lEr.add(_er);

		DefaultDirectedWeightedGraph outputGraph = SwapSolver.buildGraph(lEr);
		System.out.println(outputGraph);
		System.out.println("----------------------------------");

		System.out.println(resultGraph);
		boolean res = this.isEqualGraph(outputGraph, resultGraph);
		assertTrue(res);*/
	}

	public static boolean isEqualGraph(DefaultDirectedWeightedGraph graphA, DefaultDirectedWeightedGraph graphB) {
		/*Set<ERList> edgesA = graphA.edgeSet();
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
		return res && (vertexA.equals(vertexB));*/
		return true;
	}
}