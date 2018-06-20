/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;
import swap.swap_solver_v1.ERList;
import swap.swap_solver_v1.ExchangeRequest;
import swap.swap_solver_v1.SwapSolver;

public class MinimuDateOfCycle {

    @Test
    public void test1() {
        DefaultDirectedWeightedGraph<String, ERList> resultgraph = new DefaultDirectedWeightedGraph<>(ERList.class);

        
        resultgraph.addVertex("TP1");
        resultgraph.addVertex("TP2");
        resultgraph.addVertex("TP3");
        resultgraph.addVertex("TP4");
        ExchangeRequest er;
        er = new ExchangeRequest(1, "TP1", "TP2", "a1");
        ERList tp1_tp2 = new ERList();
        tp1_tp2.addExchangeRequest(er);
        resultgraph.addEdge("TP1", "TP2", tp1_tp2);

        ERList tp2_tp1 = new ERList();
        er = new ExchangeRequest(2, "TP2", "TP1", "a4");
        tp2_tp1.addExchangeRequest(er);
        resultgraph.addEdge("TP2", "TP1", tp2_tp1);

        ERList tp1_tp3 = new ERList();
        er = new ExchangeRequest(1, "TP1", "TP3", "a2");
        tp1_tp3.addExchangeRequest(er);
        resultgraph.addEdge("TP1", "TP3", tp1_tp3);

        ERList tp3_tp1 = new ERList();
        er = new ExchangeRequest(3, "TP3", "TP1", "a7");
        tp3_tp1.addExchangeRequest(er);
        resultgraph.addEdge("TP3", "TP1", tp3_tp1);

        ERList tp1_tp4 = new ERList();
        er = new ExchangeRequest(1, "TP1", "TP4", "a3");
        tp1_tp4.addExchangeRequest(er);
        resultgraph.addEdge("TP1", "TP4", tp1_tp4);

        ERList tp4_tp1 = new ERList();
        er = new ExchangeRequest(4, "TP4", "TP1", "a10");
        tp4_tp1.addExchangeRequest(er);
        resultgraph.addEdge("TP4", "TP1", tp4_tp1);

        ERList tp2_tp3 = new ERList();
        er = new ExchangeRequest(2, "TP2", "TP3", "a5");
        tp2_tp3.addExchangeRequest(er);
        resultgraph.addEdge("TP2", "TP3", tp2_tp3);

        ERList tp3_tp2 = new ERList();
        er = new ExchangeRequest(3, "TP3", "TP2", "a8");
        tp3_tp2.addExchangeRequest(er);
        resultgraph.addEdge("TP3", "TP2", tp3_tp2);

        ERList tp3_tp4 = new ERList();
        er = new ExchangeRequest(3, "TP3", "TP4", "a9");
        tp3_tp4.addExchangeRequest(er);
        resultgraph.addEdge("TP3", "TP4", tp3_tp4);

        ERList tp4_tp3 = new ERList();
        er = new ExchangeRequest(4, "TP4", "TP3", "a12");
        tp4_tp3.addExchangeRequest(er);
        resultgraph.addEdge("TP4", "TP3", tp4_tp3);

        ERList tp2_tp4 = new ERList();
        er = new ExchangeRequest(2, "TP2", "TP4", "a6");
        tp2_tp4.addExchangeRequest(er);
        resultgraph.addEdge("TP2", "TP4", tp2_tp4);

        ERList tp4_tp2 = new ERList();
        er = new ExchangeRequest(4, "TP4", "TP2", "a11");
        tp4_tp2.addExchangeRequest(er);
        resultgraph.addEdge("TP4", "TP2", tp4_tp2);

        String jsonResultString = "{\"solved_exchanges\":[\"a1\", \"a5\", \"a9\", \"a10\"]}";

        List<String> testCycle = new ArrayList<>();
        testCycle.add("TP1");
        testCycle.add("TP3");
        testCycle.add("TP2");
        testCycle.add("TP1");
        int expectedTime = 2;
        //o terceiro parâmetro da função minimumDateOfCycle nunca pode ser igual à maxima data.
        //Se for, a função retornará MAX_INTEGER
        System.out.println(SwapSolver.minimumDateOfCycle(resultgraph, testCycle, 1));
        assertTrue(SwapSolver.minimumDateOfCycle(resultgraph, testCycle, 1) == expectedTime);

    }

    @Test
    public void test2() {
        String jsonInputString = "{\"exchange_requests\":[\n"
                + "		{\n"
                + "			\"id\": \"a1\",\n"
                + "			\"from_shift_id\": \"TP1\",\n"
                + "			\"to_shift_id\": \"TP5\",\n"
                + "			\"created_at\": 1\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a2\",\n"
                + "			\"from_shift_id\": \"TP1\",\n"
                + "			\"to_shift_id\": \"TP3\",\n"
                + "			\"created_at\": 1\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a3\",\n"
                + "			\"from_shift_id\": \"TP2\",\n"
                + "			\"to_shift_id\": \"TP4\",\n"
                + "			\"created_at\": 2\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a4\",\n"
                + "			\"from_shift_id\": \"TP2\",\n"
                + "			\"to_shift_id\": \"TP6\",\n"
                + "			\"created_at\": 2\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a5\",\n"
                + "			\"from_shift_id\": \"TP3\",\n"
                + "			\"to_shift_id\": \"TP2\",\n"
                + "			\"created_at\": 3\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a6\",\n"
                + "			\"from_shift_id\": \"TP5\",\n"
                + "			\"to_shift_id\": \"TP2\",\n"
                + "			\"created_at\": 5\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a7\",\n"
                + "			\"from_shift_id\": \"TP4\",\n"
                + "			\"to_shift_id\": \"TP1\",\n"
                + "			\"created_at\": 4\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a8\",\n"
                + "			\"from_shift_id\": \"TP6\",\n"
                + "			\"to_shift_id\": \"TP1\",\n"
                + "			\"created_at\": 6\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a9\",\n"
                + "			\"from_shift_id\": \"TP6\",\n"
                + "			\"to_shift_id\": \"TP7\",\n"
                + "			\"created_at\": 6\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a10\",\n"
                + "			\"from_shift_id\": \"TP6\",\n"
                + "			\"to_shift_id\": \"TP9\",\n"
                + "			\"created_at\": 6\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a11\",\n"
                + "			\"from_shift_id\": \"TP9\",\n"
                + "			\"to_shift_id\": \"TP6\",\n"
                + "			\"created_at\": 9\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a12\",\n"
                + "			\"from_shift_id\": \"TP8\",\n"
                + "			\"to_shift_id\": \"TP9\",\n"
                + "			\"created_at\": 8\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a13\",\n"
                + "			\"from_shift_id\": \"TP2\",\n"
                + "			\"to_shift_id\": \"TP8\",\n"
                + "			\"created_at\": 2\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a14\",\n"
                + "			\"from_shift_id\": \"TP7\",\n"
                + "			\"to_shift_id\": \"TP4\",\n"
                + "			\"created_at\": 7\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a15\",\n"
                + "			\"from_shift_id\": \"TP7\",\n"
                + "			\"to_shift_id\": \"TP2\",\n"
                + "			\"created_at\": 7\n"
                + "		}\n"
                + "	]\n"
                + "}";
        String jsonResultString = "{\"solved_exchanges\":[\"a2\", \"a5\", \"a13\", \"a12\", \"a11\", \"a9\", \"a14\", \"a7\"]}";

        JSONObject output = new JSONObject(SwapSolver.resolveExchanges(jsonInputString));
        JSONObject expectedResult = new JSONObject(jsonResultString);
        assertTrue(output.similar(expectedResult));

    }

    @Test
    public void test3() {
        String jsonInputString = "{\"exchange_requests\":[\n"
                + "		{\n"
                + "			\"id\": \"a1\",\n"
                + "			\"from_shift_id\": \"TP1\",\n"
                + "			\"to_shift_id\": \"TP2\",\n"
                + "			\"created_at\": 1\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a2\",\n"
                + "			\"from_shift_id\": \"TP1\",\n"
                + "			\"to_shift_id\": \"TP4\",\n"
                + "			\"created_at\": 1\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a3\",\n"
                + "			\"from_shift_id\": \"TP2\",\n"
                + "			\"to_shift_id\": \"TP3\",\n"
                + "			\"created_at\": 20\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a4\",\n"
                + "			\"from_shift_id\": \"TP3\",\n"
                + "			\"to_shift_id\": \"TP1\",\n"
                + "			\"created_at\": 3\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a5\",\n"
                + "			\"from_shift_id\": \"TP4\",\n"
                + "			\"to_shift_id\": \"TP5\",\n"
                + "			\"created_at\": 4\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a6\",\n"
                + "			\"from_shift_id\": \"TP5\",\n"
                + "			\"to_shift_id\": \"TP1\",\n"
                + "			\"created_at\": 5\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a7\",\n"
                + "			\"from_shift_id\": \"TP2\",\n"
                + "			\"to_shift_id\": \"TP4\",\n"
                + "			\"created_at\": 2\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a8\",\n"
                + "			\"from_shift_id\": \"TP4\",\n"
                + "			\"to_shift_id\": \"TP6\",\n"
                + "			\"created_at\": 4\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a9\",\n"
                + "			\"from_shift_id\": \"TP6\",\n"
                + "			\"to_shift_id\": \"TP2\",\n"
                + "			\"created_at\": 6\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a10\",\n"
                + "			\"from_shift_id\": \"TP5\",\n"
                + "			\"to_shift_id\": \"TP7\",\n"
                + "			\"created_at\": 5\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a11\",\n"
                + "			\"from_shift_id\": \"TP7\",\n"
                + "			\"to_shift_id\": \"TP1\",\n"
                + "			\"created_at\": 7\n"
                + "		}\n"
                + "	]\n"
                + "}\n"
                + "		\n"
                + "	";
        String jsonResultString = "{\"solved_exchanges\":[\"a1\", \"a7\", \"a5\", \"a10\", \"a11\"]}";

        JSONObject output = new JSONObject(SwapSolver.resolveExchanges(jsonInputString));
        JSONObject expectedResult = new JSONObject(jsonResultString);
        assertTrue(output.similar(expectedResult));

    }

    @Test
    public void test4() {
        String jsonInputString = "{\"exchange_requests\":[\n"
                + "		{\n"
                + "			\"id\": \"a1\",\n"
                + "			\"from_shift_id\": \"TP1\",\n"
                + "			\"to_shift_id\": \"TP2\",\n"
                + "			\"created_at\": 1\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a2\",\n"
                + "			\"from_shift_id\": \"TP1\",\n"
                + "			\"to_shift_id\": \"TP4\",\n"
                + "			\"created_at\": 1\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a3\",\n"
                + "			\"from_shift_id\": \"TP2\",\n"
                + "			\"to_shift_id\": \"TP3\",\n"
                + "			\"created_at\": 20\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a4\",\n"
                + "			\"from_shift_id\": \"TP3\",\n"
                + "			\"to_shift_id\": \"TP1\",\n"
                + "			\"created_at\": 3\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a5\",\n"
                + "			\"from_shift_id\": \"TP4\",\n"
                + "			\"to_shift_id\": \"TP5\",\n"
                + "			\"created_at\": 4\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a6\",\n"
                + "			\"from_shift_id\": \"TP5\",\n"
                + "			\"to_shift_id\": \"TP1\",\n"
                + "			\"created_at\": 5\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a7\",\n"
                + "			\"from_shift_id\": \"TP2\",\n"
                + "			\"to_shift_id\": \"TP4\",\n"
                + "			\"created_at\": 2\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a8\",\n"
                + "			\"from_shift_id\": \"TP4\",\n"
                + "			\"to_shift_id\": \"TP6\",\n"
                + "			\"created_at\": 4\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a9\",\n"
                + "			\"from_shift_id\": \"TP6\",\n"
                + "			\"to_shift_id\": \"TP2\",\n"
                + "			\"created_at\": 6\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a10\",\n"
                + "			\"from_shift_id\": \"TP5\",\n"
                + "			\"to_shift_id\": \"TP7\",\n"
                + "			\"created_at\": 5\n"
                + "		},\n"
                + "		{\n"
                + "			\"id\": \"a11\",\n"
                + "			\"from_shift_id\": \"TP7\",\n"
                + "			\"to_shift_id\": \"TP1\",\n"
                + "			\"created_at\": 7\n"
                + "		}\n"
                + "	]\n"
                + "}";
        String jsonResultString = "{\"solved_exchanges\":[\"a1\", \"a7\", \"a5\", \"a10\", \"a11\"]}";

        JSONObject output = new JSONObject(SwapSolver.resolveExchanges(jsonInputString));
        JSONObject expectedResult = new JSONObject(jsonResultString);
        assertTrue(output.similar(expectedResult));

    }
}
