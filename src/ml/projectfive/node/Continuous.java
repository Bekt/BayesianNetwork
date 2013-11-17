package ml.projectfive.node;

import static helpers.Rand.*;
import static java.lang.Math.log;

public abstract class Continuous extends Variable {

    public Continuous(double value) {
        super(value);
    }

    /**
     * Metropolis sampling
     */
    @Override
    public void sample() {
        double mu = getCurrentVal();
        double s = nextGaussian() + mu;
        setCurrentVal(s);
        double e = log(conditional_probability(s));
        for (Variable child : getChildren()) {
            e += log(child.conditional_probability(child.getCurrentVal()));
        }
        setCurrentVal(mu);
        double f = log(conditional_probability(mu));
        for (Variable child : getChildren()) {
            f += log(child.conditional_probability(child.getCurrentVal()));
        }
        if (log(nextDouble()) < e - f) {
            setCurrentVal(s);
        }
    }

}
