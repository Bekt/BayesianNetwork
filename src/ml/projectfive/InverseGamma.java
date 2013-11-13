package ml.projectfive;

import java.util.List;

public class InverseGamma extends Continuous {

    public InverseGamma(double value) {
        super(value);
    }

    @Override
    public double conditional_probability(double val) {
        int row = checkParents();
        List<Node> distribution = getParameters().get(row);
        double shape = distribution.get(0).getCurrentVal();
        double scale = distribution.get(1).getCurrentVal();

        // TODO: return inverseGammaPdf(val, shape, scale);

        throw new UnsupportedOperationException("Not implemented");
    }

}
