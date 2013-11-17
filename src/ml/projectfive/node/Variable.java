package ml.projectfive.node;

import ml.MLException;

import java.util.ArrayList;
import java.util.List;

public abstract class Variable extends Node {

    private List<Variable> children = new ArrayList<Variable>();
    private List<Categorical> parents = new ArrayList<Categorical>();
    private List<List<Node>> parameters = new ArrayList<List<Node>>();
    private int dimensions;

    public Variable(double value) {
        super(value);
    }

    public abstract double conditional_probability(double val);

    public abstract void sample();

    public List<Variable> getChildren() {
        return children;
    }

    public List<Categorical> getParents() {
        return parents;
    }

    public List<List<Node>> getParameters() {
        return parameters;
    }

    public void addChild(Variable child) {
        children.add(child);
    }

    protected void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public void addCatParent(Categorical parent) {
        if (parent.numValues() == 0) {
            throw new MLException("Categorical parent does not have any possible values.");
        }
        parents.add(parent);
    }

    public void buildTable() {
        int rows = getNumPermutations();
        resizeTable(rows, dimensions);
    }

    protected int checkParents() {
        int v = 0, m = 1;
        for (Categorical parent : parents) {
            v += (m * parent.getCurrentVal());
            m *= parent.getCurrentVal();
        }
        return v;
    }

    private int getNumPermutations() {
        int m = 1;
        for (Categorical parent : getParents()) {
            m *= parent.numValues();
        }
        return m;
    }

    private void resizeTable(int m, int k) {
        parameters.clear();
        for (int i = 0; i < m; i++) {
            List<Node> row = new ArrayList<Node>();
            for (int j = 0; j < k; j++) {
                row.add(new Constant(0));
            }
            parameters.add(row);
        }
    }

}
