package ml.projectfive.node;

import java.util.ArrayList;
import java.util.List;
import static helpers.MathUtility.sum;
import static helpers.Rand.nextDouble;

public class Categorical extends Variable {

    private List<Double> labelValues = new ArrayList<Double>();

    public Categorical(double value, int dimensions) {
        super(value);
        setDimensions(dimensions);
    }

    @Override
    public double conditional_probability(double val) {
        int row = checkParents();
        return getParameters().get(row).get((int) val).getCurrentVal();
    }

    /**
     * Gibbs sampling
     */
    @Override
    public void sample() {
        List<Double> probs = new ArrayList<Double>();
        for (int i = 0; i < labelValues.size(); i++) {
            double vi = labelValues.get(i);
            setCurrentVal(vi);
            double probi = conditional_probability(vi);
            probs.add(probi);
            for (Variable child : getChildren()) {
                double w = child.getCurrentVal();
                probs.set(i, probi * child.conditional_probability(w));
            }
        }
        normalizeSample(probs);
        int val = drawN(probs);
        setCurrentVal(labelValues.get(val));
    }

    public void addLabelValue(double value) {
        labelValues.add(value);
    }

    public int numValues() {
        return labelValues.size();
    }

    private static void normalizeSample(List<Double> probs) {
        double sum = sum(probs);
        for (int i = 0; i < probs.size(); i++) {
            probs.set(i, probs.get(i) / sum);
        }
    }

    private static int drawN(List<Double> probs) {
        double curr = 0, rand = nextDouble();
        int index = 0;
        while (rand > curr) {
            curr += probs.get(index++);
        }
        return index - 1;
    }

}
