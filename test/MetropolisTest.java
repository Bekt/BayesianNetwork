import ml.projectfive.node.Normal;
import ml.projectfive.node.Variable;
import java.util.ArrayList;
import java.util.List;
import static helpers.MathUtility.sum;

public class MetropolisTest {

    public static void main(String[] args) {

        Normal parent = new Normal(1);
        parent.buildTable();

        testOne(parent);

        Normal child = new Normal(1);
        parent.addChild(child);
        child.buildTable();

        testTwo(parent, child);

        testThree(parent, child);

    }

    /**
     * Mean should be ~7.0, and sttdev should be ~3.0
     */
    static void testOne(Normal node) {
        final int n = 100000;
        node.getParameters().get(0).get(0).setCurrentVal(7);
        node.getParameters().get(0).get(1).setCurrentVal(1.732);

        List<Double> samples = new ArrayList<Double>();
        for (int i = 0; i < n; i++) {
            node.sample();
        }
        for (int i = 0; i < n; i++) {
            node.sample();
            samples.add(node.getCurrentVal());
        }
        double mean = sum(samples) / n;
        double diffSum = 0;

        for (double d : samples) {
            double val = d - mean;
            diffSum += (val * val);
        }

        System.out.println("mean: " + mean);
        System.out.println("variance: " + diffSum / (n - 1));
        System.out.println();
    }

    /**
     * Mean should be ~7.0, and sttdev should be ~3.0
     */
    static void testTwo(Variable parent, Variable child) {
        final int n = 100000;
        child.getParameters().get(0).set(0, parent);
        child.getParameters().get(0).get(1).setCurrentVal(1);

        List<Double> samples = new ArrayList<Double>();
        for (int i = 0; i < n; i++) {
            child.sample();
            parent.sample();
        }
        for (int i = 0; i < n; i++) {
            child.sample();
            parent.sample();
            samples.add(parent.getCurrentVal());
        }
        double mean = sum(samples) / n;
        double diffSum = 0;

        for (double d : samples) {
            double val = d - mean;
            diffSum += (val * val);
        }

        System.out.println("mean: " + mean);
        System.out.println("variance: " + diffSum / (n - 1));
        System.out.println();
    }

    /**
     * Mean should be ~13.75, and sttdev should be ~0.75
     */
    static void testThree(Variable parent, Variable child) {
        final int n = 100000;
        child.setCurrentVal(16.0);
        child.setObserved(true);

        List<Double> samples = new ArrayList<Double>();
        for (int i = 0; i < n; i++) {
            parent.sample();
        }
        for (int i = 0; i < n; i++) {
            parent.sample();
            samples.add(parent.getCurrentVal());
        }
        double mean = sum(samples) / n;
        double diffSum = 0;

        for (double d : samples) {
            double val = d - mean;
            diffSum += (val * val);
        }

        System.out.println("mean: " + mean);
        System.out.println("variance: " + diffSum / (n - 1));
        System.out.println();
    }

}
