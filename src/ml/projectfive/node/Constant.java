package ml.projectfive.node;

public class Constant extends Node {

    public Constant(double value) {
        super(value);
        setObserved(true);
    }

}
