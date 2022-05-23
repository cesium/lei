/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExchangeResolverTest {

    @Test
    public void test1() {
        String jsonInputString = "{\"exchange_requests\":[\n" +
                "		{\n" +
                "			\"id\": \"a1\",\n" +
                "			\"from_shift_id\": \"TP1\",\n" +
                "			\"to_shift_id\": \"TP2\",\n" +
                "			\"created_at\": 1\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a2\",\n" +
                "			\"from_shift_id\": \"TP1\",\n" +
                "			\"to_shift_id\": \"TP3\",\n" +
                "			\"created_at\": 1\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a3\",\n" +
                "			\"from_shift_id\": \"TP1\",\n" +
                "			\"to_shift_id\": \"TP4\",\n" +
                "			\"created_at\": 1\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a4\",\n" +
                "			\"from_shift_id\": \"TP2\",\n" +
                "			\"to_shift_id\": \"TP1\",\n" +
                "			\"created_at\": 2\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a5\",\n" +
                "			\"from_shift_id\": \"TP2\",\n" +
                "			\"to_shift_id\": \"TP3\",\n" +
                "			\"created_at\": 2\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a6\",\n" +
                "			\"from_shift_id\": \"TP2\",\n" +
                "			\"to_shift_id\": \"TP4\",\n" +
                "			\"created_at\": 2\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a7\",\n" +
                "			\"from_shift_id\": \"TP3\",\n" +
                "			\"to_shift_id\": \"TP1\",\n" +
                "			\"created_at\": 3\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a8\",\n" +
                "			\"from_shift_id\": \"TP3\",\n" +
                "			\"to_shift_id\": \"TP2\",\n" +
                "			\"created_at\": 3\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a9\",\n" +
                "			\"from_shift_id\": \"TP3\",\n" +
                "			\"to_shift_id\": \"TP4\",\n" +
                "			\"created_at\": 3\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a10\",\n" +
                "			\"from_shift_id\": \"TP4\",\n" +
                "			\"to_shift_id\": \"TP1\",\n" +
                "			\"created_at\": 4\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a11\",\n" +
                "			\"from_shift_id\": \"TP4\",\n" +
                "			\"to_shift_id\": \"TP2\",\n" +
                "			\"created_at\": 4\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a12\",\n" +
                "			\"from_shift_id\": \"TP4\",\n" +
                "			\"to_shift_id\": \"TP3\",\n" +
                "			\"created_at\": 4\n" +
                "		}		\n" +
                "	]\n" +
                "}";
        String jsonResultString = "{\"solved_exchanges\":[\"a1\", \"a5\", \"a9\", \"a10\"]}";

        JSONObject output = new JSONObject(SwapSolver.resolveExchanges(jsonInputString));
        JSONObject expectedResult = new JSONObject(jsonResultString);
        assertTrue(output.similar(expectedResult));


    }

    @Test
    public void test2() {
        String jsonInputString = "{\"exchange_requests\":[\n" +
                "		{\n" +
                "			\"id\": \"a1\",\n" +
                "			\"from_shift_id\": \"TP1\",\n" +
                "			\"to_shift_id\": \"TP5\",\n" +
                "			\"created_at\": 1\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a2\",\n" +
                "			\"from_shift_id\": \"TP1\",\n" +
                "			\"to_shift_id\": \"TP3\",\n" +
                "			\"created_at\": 1\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a3\",\n" +
                "			\"from_shift_id\": \"TP2\",\n" +
                "			\"to_shift_id\": \"TP4\",\n" +
                "			\"created_at\": 2\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a4\",\n" +
                "			\"from_shift_id\": \"TP2\",\n" +
                "			\"to_shift_id\": \"TP6\",\n" +
                "			\"created_at\": 2\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a5\",\n" +
                "			\"from_shift_id\": \"TP3\",\n" +
                "			\"to_shift_id\": \"TP2\",\n" +
                "			\"created_at\": 3\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a6\",\n" +
                "			\"from_shift_id\": \"TP5\",\n" +
                "			\"to_shift_id\": \"TP2\",\n" +
                "			\"created_at\": 5\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a7\",\n" +
                "			\"from_shift_id\": \"TP4\",\n" +
                "			\"to_shift_id\": \"TP1\",\n" +
                "			\"created_at\": 4\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a8\",\n" +
                "			\"from_shift_id\": \"TP6\",\n" +
                "			\"to_shift_id\": \"TP1\",\n" +
                "			\"created_at\": 6\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a9\",\n" +
                "			\"from_shift_id\": \"TP6\",\n" +
                "			\"to_shift_id\": \"TP7\",\n" +
                "			\"created_at\": 6\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a10\",\n" +
                "			\"from_shift_id\": \"TP6\",\n" +
                "			\"to_shift_id\": \"TP9\",\n" +
                "			\"created_at\": 6\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a11\",\n" +
                "			\"from_shift_id\": \"TP9\",\n" +
                "			\"to_shift_id\": \"TP6\",\n" +
                "			\"created_at\": 9\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a12\",\n" +
                "			\"from_shift_id\": \"TP8\",\n" +
                "			\"to_shift_id\": \"TP9\",\n" +
                "			\"created_at\": 8\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a13\",\n" +
                "			\"from_shift_id\": \"TP2\",\n" +
                "			\"to_shift_id\": \"TP8\",\n" +
                "			\"created_at\": 2\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a14\",\n" +
                "			\"from_shift_id\": \"TP7\",\n" +
                "			\"to_shift_id\": \"TP4\",\n" +
                "			\"created_at\": 7\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a15\",\n" +
                "			\"from_shift_id\": \"TP7\",\n" +
                "			\"to_shift_id\": \"TP2\",\n" +
                "			\"created_at\": 7\n" +
                "		}\n" +
                "	]\n" +
                "}";
        String jsonResultString = "{\"solved_exchanges\":[\"a2\", \"a5\", \"a13\", \"a12\", \"a11\", \"a9\", \"a14\", \"a7\"]}";

        JSONObject output = new JSONObject(SwapSolver.resolveExchanges(jsonInputString));
        JSONObject expectedResult = new JSONObject(jsonResultString);
        assertTrue(output.similar(expectedResult));


    }

    @Test
    public void test3(){
        String jsonInputString = "{\"exchange_requests\":[\n" +
                "		{\n" +
                "			\"id\": \"a1\",\n" +
                "			\"from_shift_id\": \"TP1\",\n" +
                "			\"to_shift_id\": \"TP2\",\n" +
                "			\"created_at\": 1\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a2\",\n" +
                "			\"from_shift_id\": \"TP1\",\n" +
                "			\"to_shift_id\": \"TP4\",\n" +
                "			\"created_at\": 1\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a3\",\n" +
                "			\"from_shift_id\": \"TP2\",\n" +
                "			\"to_shift_id\": \"TP3\",\n" +
                "			\"created_at\": 20\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a4\",\n" +
                "			\"from_shift_id\": \"TP3\",\n" +
                "			\"to_shift_id\": \"TP1\",\n" +
                "			\"created_at\": 3\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a5\",\n" +
                "			\"from_shift_id\": \"TP4\",\n" +
                "			\"to_shift_id\": \"TP5\",\n" +
                "			\"created_at\": 4\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a6\",\n" +
                "			\"from_shift_id\": \"TP5\",\n" +
                "			\"to_shift_id\": \"TP1\",\n" +
                "			\"created_at\": 5\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a7\",\n" +
                "			\"from_shift_id\": \"TP2\",\n" +
                "			\"to_shift_id\": \"TP4\",\n" +
                "			\"created_at\": 2\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a8\",\n" +
                "			\"from_shift_id\": \"TP4\",\n" +
                "			\"to_shift_id\": \"TP6\",\n" +
                "			\"created_at\": 4\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a9\",\n" +
                "			\"from_shift_id\": \"TP6\",\n" +
                "			\"to_shift_id\": \"TP2\",\n" +
                "			\"created_at\": 6\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a10\",\n" +
                "			\"from_shift_id\": \"TP5\",\n" +
                "			\"to_shift_id\": \"TP7\",\n" +
                "			\"created_at\": 5\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a11\",\n" +
                "			\"from_shift_id\": \"TP7\",\n" +
                "			\"to_shift_id\": \"TP1\",\n" +
                "			\"created_at\": 7\n" +
                "		}\n" +
                "	]\n" +
                "}\n" +
                "		\n" +
                "	";
        String jsonResultString = "{\"solved_exchanges\":[\"a1\", \"a7\", \"a5\", \"a10\", \"a11\"]}";

        JSONObject output = new JSONObject(SwapSolver.resolveExchanges(jsonInputString));
        JSONObject expectedResult = new JSONObject(jsonResultString);
        assertTrue(output.similar(expectedResult));


    }

    @Test
    public void test4() {
        String jsonInputString = "{\"exchange_requests\":[\n" +
                "		{\n" +
                "			\"id\": \"a1\",\n" +
                "			\"from_shift_id\": \"TP1\",\n" +
                "			\"to_shift_id\": \"TP2\",\n" +
                "			\"created_at\": 1\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a2\",\n" +
                "			\"from_shift_id\": \"TP1\",\n" +
                "			\"to_shift_id\": \"TP4\",\n" +
                "			\"created_at\": 1\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a3\",\n" +
                "			\"from_shift_id\": \"TP2\",\n" +
                "			\"to_shift_id\": \"TP3\",\n" +
                "			\"created_at\": 20\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a4\",\n" +
                "			\"from_shift_id\": \"TP3\",\n" +
                "			\"to_shift_id\": \"TP1\",\n" +
                "			\"created_at\": 3\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a5\",\n" +
                "			\"from_shift_id\": \"TP4\",\n" +
                "			\"to_shift_id\": \"TP5\",\n" +
                "			\"created_at\": 4\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a6\",\n" +
                "			\"from_shift_id\": \"TP5\",\n" +
                "			\"to_shift_id\": \"TP1\",\n" +
                "			\"created_at\": 5\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a7\",\n" +
                "			\"from_shift_id\": \"TP2\",\n" +
                "			\"to_shift_id\": \"TP4\",\n" +
                "			\"created_at\": 2\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a8\",\n" +
                "			\"from_shift_id\": \"TP4\",\n" +
                "			\"to_shift_id\": \"TP6\",\n" +
                "			\"created_at\": 4\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a9\",\n" +
                "			\"from_shift_id\": \"TP6\",\n" +
                "			\"to_shift_id\": \"TP2\",\n" +
                "			\"created_at\": 6\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a10\",\n" +
                "			\"from_shift_id\": \"TP5\",\n" +
                "			\"to_shift_id\": \"TP7\",\n" +
                "			\"created_at\": 5\n" +
                "		},\n" +
                "		{\n" +
                "			\"id\": \"a11\",\n" +
                "			\"from_shift_id\": \"TP7\",\n" +
                "			\"to_shift_id\": \"TP1\",\n" +
                "			\"created_at\": 7\n" +
                "		}\n" +
                "	]\n" +
                "}";
        String jsonResultString = "{\"solved_exchanges\":[\"a1\", \"a7\", \"a5\", \"a10\", \"a11\"]}";

        JSONObject output = new JSONObject(SwapSolver.resolveExchanges(jsonInputString));
        JSONObject expectedResult = new JSONObject(jsonResultString);
        assertTrue(output.similar(expectedResult));


    }
}
