package ml.projectfive.node;

public abstract class Node {

    private double currentVal;
    private boolean isObserved;

    public Node(double value) {
        this.currentVal = value;
    }

    public double getCurrentVal() {
        return currentVal;
    }

    public void setCurrentVal(double value) {
        this.currentVal = value;
    }

    public boolean isObserved() {
        return isObserved;
    }

    public void setObserved(boolean observed) {
        isObserved = observed;
    }

}
