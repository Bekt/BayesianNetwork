package ml.projectfive;

import java.util.ArrayList;
import java.util.List;

public class Variable extends Node {

    private List<Variable> children = new ArrayList<Variable>();
    private List<Categorical> catParents = new ArrayList<Categorical>();
    private List<List<Node>> parameters = new ArrayList<List<Node>>();
    private int tableSize = 1;

    public Variable(double value) {
        super(value);
    }

    public List<Variable> getChildren() {
        return children;
    }

    public List<Categorical> getCatParents() {
        return catParents;
    }

    public List<List<Node>> getParameters() {
        return parameters;
    }

    public void addChild(Variable child) {
        children.add(child);
    }

    public void addCatParent(Categorical parent) {
        catParents.add(parent);
        tableSize *= parent.numValues();
        // TODO: resize parameters
    }

    public double conditional_probability(double val) {
        throw new UnsupportedOperationException("Not implemented");
    }

    protected int checkParents() {
        int v = 0, m = 1;
        for (Categorical parent : catParents) {
            v += (m * parent.getCurrentVal());
            m *= parent.getCurrentVal();
        }
        return v;
    }

}
