import static java.util.stream.Collectors.toList;
import static spark.Spark.initExceptionHandler;
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
import spark.Request;
import spark.Response;
import spark.Spark;
import com.google.ortools.Loader;

/**
 * Main application class
 */
public class SwapSolver {

	public static void main(String[] args) throws JSONException {

		Loader.loadNativeLibraries();

		threadPool(8);
		Spark.post("/", (req, res) -> processRequest(req, res));
	}

	public static String processRequest(Request request, Response response) {
		try {
			List<ConditionalExchange> exchangeRequests = parseRequest(request.body());
			if(!validateRequest(exchangeRequests)) {
				response.status(400);
				return errorToJSON("Set of exchanges is not valid");
			}
		} catch(JSONException) {
			response.status(400);
			return errorToJSON("Body not a valid JSON object");
		}

		response.status(200);
		return "Batata";
	}

	//TODO

	/**
	 * Determines whether or not a list of exchanges corresponds to a valid request
	 *
	 * TODO: DEFINE VALID REQUEST
	 *
	 * @param requestedExchanges the list of requested exchanges
	 * @return whether or not the requested exchanges make up a valid request
	 */
	private static boolean validateRequest(List<ConditionalExchange> requestedExchanges) {
		return true;
	}

	/**
	 * Converts an error message to a JSON string
	 * @param error the error message
	 * @return the JSON string
	 */
	private static String errorToJSON(String error) {
		return String.format("{\"error\": \"%s\"}", error);
	}

	/**
	 * Parses the request's body to the list of requested exchanges
	 * @param request the JSON string of the request
	 * @return the list of requested exchanges
	 * @throws JSONException if the string is not valid JSON or it is not of the correct format
	 */
	private static List<ConditionalExchange> parseRequest(String request) throws JSONException {
		List<ConditionalExchange> requestedExchanges = new ArrayList<>();
		JSONObject object = new JSONObject(request);
		JSONArray array = object.getJSONArray("requested_exchanges");

		for(int i = 0; i < array.length(); i++) {
			requestedExchanges.add(new ConditionalExchange(array.getJSONObject(i)));
		}

		return requestedExchanges;
	}

	/**
	 * Converts a JSONArray to a List&lt;ExchangeRequest&gt;
	 *
	 * @param courseExchanges
	 *            the JSONArray containing exchange requests from students
	 * @return Returns a List of ExchangeRequest objects containing the same
	 *         information as the given parameter
	 */
	/*public static List<ExchangeRequest> parseRequestsFromJSON(JSONArray courseExchanges) {
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
	}*/
}
