package ml.projectfive;

public class Constant extends Node {

    public Constant(double value) {
        super(value);
        setObserved(true);
    }

    @Override
    public void setCurrentVal(double value) {
        throw new UnsupportedOperationException("Cannot modify constant node's value.");
    }

    @Override
    public boolean isObserved() {
        return true;
    }

}
