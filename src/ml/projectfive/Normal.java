package ml.projectfive;

import java.util.List;
import static helpers.MathUtility.normalPdf;
import static java.lang.Math.sqrt;

public class Normal extends Continuous {

    public Normal(double value) {
        super(value);
    }

    @Override
    public double conditional_probability(double val) {
        int row = checkParents();
        List<Node> distribution = getParameters().get(row);
        double mu = distribution.get(0).getCurrentVal();
        double variance = distribution.get(1).getCurrentVal();

        // TODO: can we store stddev instead of variance?
        return normalPdf(val, mu, sqrt(variance));
    }

}
