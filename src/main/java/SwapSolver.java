import static java.util.stream.Collectors.toList;
import static spark.Spark.initExceptionHandler;
import static spark.Spark.threadPool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogManager;

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

	/**
	 * Main application entry point
	 * @param args program arguments
	 */
	public static void main(String[] args) {

		Loader.loadNativeLibraries();

		threadPool(8);
		Spark.post("/", (req, res) -> processRequest(req, res));
	}


	/**
	 * Main request handler
	 * @param request the incoming request
	 * @param response the outgoing response object
	 * @return the response body
	 */
	public static String processRequest(Request request, Response response) {

		try {
			List<ExchangeRequest> exchangeRequests = parseRequest(request.body());
			if(!validateRequest(exchangeRequests)) {
				response.status(422);
				return errorToJSON("Set of exchanges is not valid");
			}

			LPSolver solver = new LPSolver();
			solver.setup(exchangeRequests);

			int result = solver.solve();
			return resultToJSON(solver.parseSolution());

		} catch(JSONException e) {
			response.status(400);
			return errorToJSON(e.getMessage());
		} catch(IllegalStateException e) {
			response.status(422);
			return errorToJSON(e.getMessage());
		} catch(IOException e) {
			response.status(500);
			return errorToJSON(e.getMessage());
		}
	}

	/**
	 * Converts the result from the solver to a JSON string
	 * @param solvedExchanges the list of all solved exchanges
	 * @return the JSON string to send to the client
	 */
	private static String resultToJSON(List<String> solvedExchanges) {
		JSONObject obj = new JSONObject();
		obj.put("solved_exchanges", solvedExchanges);
		return obj.toString();
	}

	/**
	 * Determines whether or not a list of exchanges corresponds to a valid request
	 *
	 * TODO: DEFINE VALID REQUEST
	 *
	 * @param requestedExchanges the list of requested exchanges
	 * @return whether or not the requested exchanges make up a valid request
	 */
	private static boolean validateRequest(List<ExchangeRequest> requestedExchanges) {
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
	private static List<ExchangeRequest> parseRequest(String request) throws JSONException {
		List<ExchangeRequest> requestedExchanges = new ArrayList<>();
		JSONObject object = new JSONObject(request);
		JSONArray array = object.getJSONArray("requested_exchanges");

		for(int i = 0; i < array.length(); i++) {
			requestedExchanges.add(new ExchangeRequest(array.getJSONArray(i)));
		}

		return requestedExchanges;
	}
}
