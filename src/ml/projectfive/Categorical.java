package ml.projectfive;

import java.util.ArrayList;
import java.util.List;

public class Categorical extends Variable {

    private List<Double> labelValues = new ArrayList<Double>();

    public Categorical(double value) {
        super(value);
    }

    @Override
    public double conditional_probability(double val) {
        int row = checkParents();

        throw new UnsupportedOperationException("Not implemented");

        // TODO: is this right?
        // return getParameters().get(row).get((int) val).getCurrentVal();
    }

    public double getLabelValue(int i) {
        return labelValues.get(i);
    }

    public void addLabelValue(double value) {
        labelValues.add(value);
    }

    public int numValues() {
        return labelValues.size();
    }

    public void gibbsSample(){
        throw new UnsupportedOperationException("Not implemented.");
    }

}
