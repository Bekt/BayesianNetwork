package ml.projectfive.node;

import java.util.List;
import static helpers.MathUtility.inverseGammaPdf;

public class InverseGamma extends Continuous {

    public InverseGamma(double value) {
        super(value);
        setDimensions(2);
    }

    @Override
    public double conditional_probability(double val) {
        int row = checkParents();
        List<Node> distribution = getParameters().get(row);
        double alpha = distribution.get(0).getCurrentVal();
        double beta = distribution.get(1).getCurrentVal();
        return inverseGammaPdf(val, alpha, beta);
    }

}
