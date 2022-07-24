import org.json.JSONException;
import org.json.JSONObject;

/**
 * A "simple" exchange. This is an exchange between two shifts of the same course. For example,shift from TP1 to TP2 in
 * course A
 */
public class Exchange {
	/**
	 * The unique identifier of the exchange
	 */
	private int id;
	/**
	 * The course the exchange refers to
	 */
	private String course;
	/**
	 * The current shift the student is enrolled in
	 */
	private String fromShift;
	/**
	 * The shift the student requested to move to
	 */
	private String toShift;

	/**
	 * Parameterized constructor
	 * @param id the id of the exchange
	 * @param course the course the exchange refers to
	 * @param fromShift the current shift
	 * @param toShift the destination shift
	 */
	public Exchange(int id, String course, String fromShift, String toShift) {
		this.id = id;
		this.fromShift = fromShift;
		this.toShift = toShift;
		this.course = course;
	}

	/**
	 * Constructor from a JSONObject
	 * @param object the JSONObject
	 * @throws JSONException if the given JSONObject is not of the right format
	 */
	public Exchange(JSONObject object) throws JSONException {
		this(object.getInt("id"), object.getString("course"), object.getString("from_shift"),
				object.getString("to_shift"));
	}

	/* GETTERS */

	/**
	 * Returns the id of the exchange
	 * @return the id of the exchange
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Returns the current shift of the student
	 * @return the current shift of the student
	 */
	public String getFromShift() {
		return this.fromShift;
	}

	/**
	 * Returns the shift the student wants to shift to
	 * @return the shift the student wants to shift to
	 */
	public String getToShift() {
		return this.toShift;
	}

	/**
	 * Returns the course the exchange refers to
	 * @return the course the exchange refers to
	 */
	public String getCourse() {
		return this.course;
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;

		if(o == null || o.getClass() != this.getClass())
			return false;

		Exchange exchange = (Exchange)o;

		return this.getId() == exchange.getId() && this.getCourse().equals(exchange.getCourse()) &&
				this.getFromShift().equals(exchange.getFromShift())	&& this.getToShift().equals(exchange.getToShift());
	}
}
