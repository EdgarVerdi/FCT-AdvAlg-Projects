import scpsolver.constraints.Constraint;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;

import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //MaximumIndependentSet s = new MaximumIndependentSet();
        //List<List<Integer>> testCase = s.example1();
        //Set<Integer> answer = s.randomizedColoringMIS(testCase.size(), testCase);
        //System.out.println(Arrays.toString(answer.toArray()));
        MetricTravellingSalesman t = new MetricTravellingSalesman();
        FullGraph graph = new FullGraph(t.test());

        int result = t.solveMetricTSP1(graph);
        System.out.println(result);
        //System.out.println(t.isMetric(t.test()));
    }

    public static void test(){
        double[] objective = { 150.0, 175.0 };
        double[] constraintCoefficients = {  77.0,  80.0,  9.0, 6.0 };
        double[][] variableConstraints = {
                {  7.0, 11.0 },
                { 10.0,  8.0 },
                {  1.0,  0.0 },
                {  0.0,  1.0 },
        };
        double[] lowerBound = {0.0, 0.0};
        LinearProgram lp = new LinearProgram(objective);
        lp.setMinProblem(false);
        for (int i = 0; i<constraintCoefficients.length; i++)
            lp.addConstraint(new LinearSmallerThanEqualsConstraint(variableConstraints[i], constraintCoefficients[i], "c"+i));
        lp.setLowerbound(lowerBound);
        LinearProgramSolver solver  = SolverFactory.newDefault();
        double[] x = solver.solve(lp);
    }

    public static void showLP(LinearProgram lp) {
        System.out.println("*********** LINEAR PROGRAMMING PROBLEM ***********");
        String fs;
        if (lp.isMinProblem()) System.out.print("  minimize: ");
        else System.out.print("  maximize: ");
        double[] cf = lp.getC();
        for (int i = 0; i<cf.length; i++) if (cf[i] != 0) {
            fs = String.format(Locale.US,"%+7.1f", cf[i]);
            System.out.print(fs + "*x["+i+"]");
        }
        System.out.println("");
        System.out.print("subject to: ");
        ArrayList<Constraint> lcstr = lp.getConstraints();
        double aij;
        double[] ci = null;
        String str = null;
        for (int i = 0; i<lcstr.size(); i++) {
            if (lcstr.get(i) instanceof LinearSmallerThanEqualsConstraint) {
                str = " <= ";
                ci = ((LinearSmallerThanEqualsConstraint) lcstr.get(i)).getC();
            }
            if (lcstr.get(i) instanceof LinearBiggerThanEqualsConstraint) {
                str = " >= ";
                ci = ((LinearBiggerThanEqualsConstraint) lcstr.get(i)).getC();
            }
            if (lcstr.get(i) instanceof LinearEqualsConstraint) {
                str = " == ";
                ci = ((LinearEqualsConstraint) lcstr.get(i)).getC();
            }
            str = str + String.format(Locale.US,"%6.1f", lcstr.get(i).getRHS());
            if (i != 0) System.out.print("            ");
            for(int j=0;j<lp.getDimension();j++) {
                aij = ci[j];
                if (aij != 0) {
                    fs = String.format(Locale.US,"%+7.1f", aij);
                    System.out.print(fs + "*x["+j+"]");
                }
                else System.out.print("            ");
            }
            System.out.println(str);
        }
    }
}
