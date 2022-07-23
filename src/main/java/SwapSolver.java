import static java.util.stream.Collectors.toList;
import static spark.Spark.threadPool;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Spark;

public class SwapSolver {

	public static void main(String[] args) throws JSONException {

		com.google.ortools.Loader.loadNativeLibraries();

		threadPool(8);
		Spark.post("/", (req, res) -> LPSolver.solve(req.body()));
	}

	/**
	 * Converts a JSONArray to a List&lt;ExchangeRequest&gt;
	 *
	 * @param courseExchanges
	 *            the JSONArray containing exchange requests from students
	 * @return Returns a List of ExchangeRequest objects containing the same
	 *         information as the given parameter
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
}
