import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * An exchange request.
 *
 * An exchange request comprises several conditional exchanges, of which only one can be accepted
 */
public class ExchangeRequest {
    /**
     * The list of conditional exchanges. Of these, only one can be performed
     */
    private List<ConditionalExchange> exchanges;

    /**
     * Parameterized constructor
     * @param conditionals the list of conditional exchanges
     */
    public ExchangeRequest(List<ConditionalExchange> conditionals) {
        this.exchanges = conditionals;
    }

    /**
     * Constructor from a JSONArray
     * @param array the JSONArray
     */
    public ExchangeRequest(JSONArray array) {
        this.exchanges = new ArrayList<>();

        for(int i = 0; i < array.length(); i++) {
            this.exchanges.add(new ConditionalExchange(array.getJSONObject(i)));
        }
    }

    /**
     * Gets the exchanges that make up the exchange request
     * @return the exchanges that make up the request
     */
    public List<ConditionalExchange> getExchanges() {
        return this.exchanges;
    }
}
