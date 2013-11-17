package ml.projectfive.node;

import java.util.List;
import static helpers.MathUtility.normalPdf;

public class Normal extends Continuous {

    public Normal(double value) {
        super(value);
        setDimensions(2);
    }

    @Override
    public double conditional_probability(double val) {
        int row = checkParents();
        List<Node> distribution = getParameters().get(row);
        double mu = distribution.get(0).getCurrentVal();
        double dev = distribution.get(1).getCurrentVal();
        return normalPdf(val, mu, dev);
    }

}
