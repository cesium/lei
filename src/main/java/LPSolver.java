import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class LPSolver
{
    public static String solve(String jsonText)
    {
        long start = System.currentTimeMillis();

        MPSolver solver = new MPSolver("cena", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
        StudentSwapsList list = new StudentSwapsList(jsonText);
        long parsed = System.currentTimeMillis();

        list.setModel(solver);
        long modeled = System.currentTimeMillis();
        MPSolver.ResultStatus status = solver.solve();
        long solved = System.currentTimeMillis();

        System.out.println("Score: " + (int)solver.objective().value() + "/" + solver.numVariables());
        System.out.println("Parsing: " + (parsed - start) + "ms");
        System.out.println("Modeling: " + (modeled - parsed) + "ms");
        System.out.println("Solving: " + (solved - modeled) + "ms");

        switch (status)
        {
            case OPTIMAL:   //success
                String out = list.output();
                return out;

            default:
                System.err.println("ups. status = " + status);
                return "";
        }
    }
}

/**
 * Represents a set of conjunct swaps from a single student
 * Ex: Analise_TP1 -> Analise_TP2 && PI_TP4 -> PI_TP2
 */
class ConjunctSwap
{
    private final ShiftSwap[] swaps;
    private MPVariable variable;

    //an expansion is "valid" if there is no pair of mutually exclusive swaps
    static boolean isValidExpression(LinkedList<ShiftSwap> expansion) {
        //TODO: optimize (hash table or something)
        for (ShiftSwap s1 : expansion)
            for (ShiftSwap s2 : expansion)
                if (s1 != s2 && s1.excludes(s2)) //I'm comparing the pointers ON PURPOSE, it's not a typo
                    return false;
        return true;
    }

    ConjunctSwap(LinkedList<ShiftSwap> swaps) {
        this.swaps = swaps.toArray(ShiftSwap.class);
    }

    void addVariable(MPSolver solver) {
        this.variable = solver.makeNumVar(0, 1, "");
        this.variable.setInteger(true); //TODO: remove when pure linear programming solution is found (I'm losing hope ngl)
        solver.objective().setCoefficient(this.variable, 1);
    }

    void setCoefficient(MPConstraint c, double coeff) {
        c.setCoefficient(this.variable, coeff);
    }

    void setShiftCoefficients(MPConstraint c, String shiftId)
    {
        for (ShiftSwap s : this.swaps)
        {
            double coeff = 0;

            if (s.from.equals(shiftId))
                coeff--;
            if (s.to.equals(shiftId))
                coeff++;

            c.setCoefficient(this.variable, coeff);
        }
    }

    /**
     * Returns whether this swap is executed. Call only after solving the model
     */
    boolean perform() {
        //if (this.variable.solutionValue() != 0 && this.variable.solutionValue() != 1)
        //    System.err.println("Deu bosta");

        return this.variable.solutionValue() == 1;
    }

    //TEMP
    String output()
    {
        StringBuilder ans = new StringBuilder();

        if (this.perform())
            for (int i = 0; i < this.swaps.length; i++)
                ans.append(i == 0 ? "" : ", ").append(this.swaps[i].from).append(" -> ").append(this.swaps[i].to);

        return ans.toString();
    }
}

class Dependency
{
    public ConjunctSwap swap;
    public ArrayList<ConjunctSwap> dependencies; //the swaps that must be performed for the antecedent to be able to be performed
}

/**
 * Represents all swaps requested by a single student, and additional restrictions.
 * The swaps are all in the form of conjunct swaps
 *
 * Ex: A: Analise_TP1 -> Analise_TP2, PI_TP4 -> PI_TP2
 *     B: Analise_TP1 -> Analise_TP3, PI_TP4 -> PI_TP2
 *     C: LI2_TP2 -> LI2_TP3
 *     Either A or B (or none, hence not a XOR)   (A X B)
 *     C only if B / C is dependent on B          (C → B)
 */
class StudentSwaps
{
    private final String student;
    private final List<ConjunctSwap> swaps;
    private final List<List<ConjunctSwap>> exclusive;
    //private ArrayList<Dependency> dependent;

    StudentSwaps(String student) {
        this.student = student;
        this.swaps = new ArrayList<>();
        this.exclusive = new ArrayList<>();
        //this.dependent = new ArrayList<>();
    }

    //the number of independent swap groups
    int getSwapsNumber() {
        return swaps.size();
    }

    //returns the number of ConjunctSwaps added
    int addExpression(SwapExpression expression)
    {
        int start = this.swaps.size();
        int ans = 0;

        for (LinkedList<ShiftSwap> s : expression.expand()) {
            if (ConjunctSwap.isValidExpression(s)) {
                this.swaps.add(new ConjunctSwap(s));
                ans++;
            }
        }

        if (ans > 1) {
            ArrayList<ConjunctSwap> aux = new ArrayList<>(ans);
            aux.addAll(start, this.swaps);
            exclusive.add(aux);
        }

        return ans;
    }

    //user inputted restrictions on their swaps
    void addExclusiveRestriction(int[] conjunctSwaps)
    {
        ArrayList<ConjunctSwap> aux = new ArrayList<>(conjunctSwaps.length);

        for (int i : conjunctSwaps)
            aux.add(this.swaps.get(i));

        exclusive.add(aux);
    }

    //void addDependentRestriction() { }

    void addVariables(MPSolver solver) {
        for (ConjunctSwap s : this.swaps)
            s.addVariable(solver);
    }

    void addConstraints(MPSolver solver)
    {
        for (List<ConjunctSwap> e : this.exclusive) {
            MPConstraint c = solver.makeConstraint(0, 1);
            for (ConjunctSwap ss : e)
                ss.setCoefficient(c, 1);
        }

        /*for (Dependency d : this.dependent) {
            MPConstraint c = solver.makeConstraint(0, Double.POSITIVE_INFINITY);
            for (ConjunctSwap ss : d.dependencies)
                ss.setCoefficient(c, 1);
            //Coefficients other than {-1,0,1} may make the optimal solution non-binary. More research is needed
            d.swap.setCoefficient(c, -d.dependencies.size());
        }*/
    }

    void setShiftCoefficients(MPConstraint c, String shiftId) {
        for (ConjunctSwap s : this.swaps)
            s.setShiftCoefficients(c, shiftId);
    }

    //TEMP
    String output()
    {
        String aux = student + ": {";
        StringBuilder ans = new StringBuilder(aux);

        for (ConjunctSwap s : this.swaps) {
            String out = s.output();
            if (!out.equals("") && !ans.toString().equals(aux))
                ans.append(", ").append(out);
            else
                ans.append(out);
        }

        if (ans.toString().equals(aux))
            return "";
        else
            return ans + "}";
    }

    //call only after the model is solved
    List<ExchangeRequest> getSolvedExchanges()  //Set<ExchangeRequest> instead?
    {
        //TODO
        return null;
    }
}

/**
 * Represents all StudentSwaps by all students
 */
class StudentSwapsList
{
    private final List<StudentSwaps> swaps; //preserves order of the input (TODO: include order in studentSwaps somehow?)
    private final Set<String> shiftIds;
    private final Map<String, StudentSwaps> studentsSwaps;

    StudentSwapsList(String jsonText)
    {
        this.swaps = new ArrayList<>();
        this.shiftIds = new HashSet<>();
        this.studentsSwaps = new HashMap<>();

        JSONObject receivedJSON = new JSONObject(jsonText);
        JSONArray requests = receivedJSON.getJSONArray("exchange_requests");
        List<ExchangeRequest> courseExchangeRequests = SwapSolver.parseRequestsFromJSON(requests);

        //Register each request under its student's swaps
        for (ExchangeRequest r : courseExchangeRequests)
        {
            StudentSwaps student = this.studentsSwaps.get(r.id);
            if (student == null) {
                student = new StudentSwaps(r.id);
                this.studentsSwaps.put(r.id, student);
                this.swaps.add(student);
            }

            ShiftSwap s = new ShiftSwap(r.from_shift_id, r.to_shift_id);
            student.addExpression(s);

            //Register mentioned shifts
            this.shiftIds.add(s.from);
            this.shiftIds.add(s.to);
        }

        /*  For testing purposes
        for (StudentSwaps ss : this.swaps)
        {
            int n = ss.getSwapsNumber();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++)
                arr[i] = i;
            ss.addExclusiveRestriction(arr);
        }
        */
    }

    void setModel(MPSolver solver)
    {
        for (StudentSwaps s : this.swaps)
        {
            s.addVariables(solver);
            s.addConstraints(solver);
        }

        //Each shift's number of enrolled students must stay the same
        for (String id : this.shiftIds)
        {
            MPConstraint c = solver.makeConstraint(0, 0); //TODO: allow increase/decrease in number of enrolled students?

            for (StudentSwaps s : this.swaps)
                s.setShiftCoefficients(c, id);
        }

        //maximize number of performed conjunct swaps
        solver.objective().setOptimizationDirection(true);
    }

    //TEMP
    String output()
    {
        String aux = "{";
        String ans = aux;

        for (StudentSwaps s : this.swaps) {
            String out = s.output();
            if (!out.equals("") && !ans.equals(aux))
                ans += ", " + out;
            else
                ans += out;
        }

        return ans + "}";
    }
}