import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A conditional exchange. A conditional exchange contains a set of "simple exchanges" that should either all be performed
 * or else none can be performed. For example, "Shift from TP1 -> TP2 in course A if and only if shift from TP3 -> TP4 in
 * course B
 */
public class ConditionalExchange {
    /**
     * The unique identifier of the conditional exchange
     */
    private int id;

    /**
     * The timestamp of the exchange, i.e., when it was requested
     */
    private long timestamp;

    /**
     * The student who requested the exchange
     */
    private String student;

    /**
     * The "simple" exchanges that make up the conditional exchange
     */
    private List<Exchange> exchanges;

    /**
     * Parameterized constructor
     * @param id the identifier of the exchange
     * @param timestamp the timestamp of the exchange
     * @param student the student who requested the exchange
     * @param exchanges the "simple" exchanges that make up the conditional exchange
     */
    public ConditionalExchange(int id, long timestamp, String student, List<Exchange> exchanges) {
        this.id = id;
        this.timestamp = timestamp;
        this.student = student;
        this.exchanges = exchanges;
    }

    /**
     * Constructor from a JSON object
     * @param object the JSON object
     * @throws JSONException if the JSON object is not of the correct format
     */
    public ConditionalExchange(JSONObject object) throws JSONException {
        this(object.getInt("id"), object.getLong("timestamp"), object.getString("student"),
                ConditionalExchange.listFromJSON(object.getJSONArray("exchanges")));
    }

    /**
     * Returns the unique identifier of the exchange
     * @return the id of the exchange
     */
    public int getId() {
        return this.id;
    }

    public List<Exchange> getExchanges() {
        return this.exchanges;
    }
    /**
     * Converts the JSON Array of the "simple" exchanges to a list of exchanges
     * @param array the JSON array
     * @return the list of exchanges
     * @throws JSONException if the provided JSON is not valid
     */
    private static List<Exchange> listFromJSON(JSONArray array) throws JSONException {
        List<Exchange> exchanges = new ArrayList<>();

        for(int i = 0; i < array.length(); i++) {
            exchanges.add(new Exchange(array.getJSONObject(i)));
        }

        return exchanges;
    }
}
