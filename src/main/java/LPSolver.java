import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Class responsible for solving the exchanges using linear programming models
 */
public class LPSolver {
    /**
     * The name of the solver object
     */
    private static final String solverName = "swap-solver";

    /**
     * A map of the constraints used to ensure that all shifts maintain the same number of enrollments
     *
     * The key is a string of the format "Course_Shift"
     */
    private static Map<String, MPConstraint> constraints;

    /**
     * The solver object
     */
    private MPSolver solver;

    /**
     * Default constructor
     */
    public LPSolver() {
        this.solver = new MPSolver(solverName, MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
        this.constraints = new HashMap<>();
    }

    /**
     * Creates the linear programming model from the given exchange requests
     * @param requestedExchanges the exchange requests used to create the model
     */
    public void setup(List<ExchangeRequest> requestedExchanges) {
        this.solver.objective().setOptimizationDirection(true);
        for (ExchangeRequest exchangeRequest : requestedExchanges) {
            MPConstraint singleConstraint = this.solver.makeConstraint(0, 1);

            for (ConditionalExchange conditional : exchangeRequest.getExchanges()) {
                MPVariable var = this.solver.makeNumVar(0.0, 1.0, Integer.toString(conditional.getId()));
                var.setInteger(true);
                this.solver.objective().setCoefficient(var, 1);
                singleConstraint.setCoefficient(var, 1.0);

                for (Exchange exchange : conditional.getExchanges()) {
                    addExchangeToShiftConstraint(var, exchange.getCourse(), exchange.getFromShift(), 1.0);
                    addExchangeToShiftConstraint(var, exchange.getCourse(), exchange.getToShift(), -1.0);
                }
            }
        }
    }

    /**
     * Solves the lnear programming model
     * @return the value of the solution
     * @throws IllegalStateException if the problem is impossible / has an infinite solution
     * @throws IOException if the model could not be solved
     */
    public int solve() throws IllegalStateException, IOException {
        MPSolver.ResultStatus status = solver.solve();

        switch (status) {
            case OPTIMAL:
                return (int) solver.objective().value();
            case INFEASIBLE:
                throw new IllegalStateException("Impossible problem");
            case UNBOUNDED:
                throw new IllegalStateException("Unlimited solution");
            default:
                throw new IOException("Invalid model");
        }
    }

    /**
     * Parse the solution to a list of strings corresponding to the ids of the accepted exchanges
     * @return A list of strings corresponding to the ids of the accepted exchanges
     */
    public List<String> parseSolution() {
        List<String> result = new ArrayList<>();

        for (MPVariable var : this.solver.variables()) {
            if (var.solutionValue() > 0.0) {
                result.add(var.name());
            }
        }

        return result;
    }

    /**
     * Adds the given exchange to the shift contraints
     * @param variable the variable corresponding to whether or not the exchange is accepted
     * @param course the name of the course
     * @param shift the name of the shift
     * @param coefficient the coefficient of the variable (1 for leaving, -1 for entering the shift)
     */
    private void addExchangeToShiftConstraint(MPVariable variable, String course, String shift, double coefficient) {
        String key = String.format("%s_%s", course, shift);
        MPConstraint courseConstraint;
        if (this.constraints.containsKey(key)) {
            courseConstraint = this.constraints.get(key);
        } else {
            courseConstraint = this.solver.makeConstraint(0.0, 0.0);
            this.constraints.put(key, courseConstraint);
        }

        courseConstraint.setCoefficient(variable, coefficient);
    }
}