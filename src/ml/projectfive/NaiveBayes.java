package ml.projectfive;

import ml.MLException;
import ml.Matrix;
import ml.SupervisedLearner;
import ml.projectfive.node.*;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.sqrt;

/**
 * Naive Bayes network for the iris data set.
 */
public class NaiveBayes extends SupervisedLearner {

    private Matrix features, labels;
    private Categorical parent;
    private List<Variable> children = new ArrayList<Variable>();

    @Override
    public void train(Matrix features, Matrix labels) {
        this.features = features;
        this.labels = labels;
        this.children.clear();
        int rows = features.getNumRows();

        if (!labels.isCategorical(0) || labels.getColumnAttributes(0).size() == 0) {
            throw new MLException("Only categorical labels are supported.");
        }

        int possibleValues = labels.getColumnAttributes(0).size();
        Categorical parent = new Categorical(0, possibleValues);
        this.parent = parent;

        for (int i = 0; i < possibleValues; i++) {
            parent.addLabelValue(i);
        }

        for (int i = 0; i < features.getNumCols(); i++) {
            Normal child = new Normal(0);
            parent.addChild(child);
            child.addCatParent(parent);
            this.children.add(child);
        }

        parent.buildTable();

        for (Variable child : parent.getChildren()) {
            child.buildTable();
        }

        // Label weights
        int[] labelFrequency = calculateLabelFrequency(labels);
        List<Node> row = parent.getParameters().get(0);
        for (int i = 0; i < row.size(); i++) {
            double weight = (double) labelFrequency[i] / rows;
            row.get(i).setCurrentVal(weight);
        }

        // Features weights
        List<Variable> children = parent.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Normal child = (Normal) children.get(i);
            List<List<Node>> table = child.getParameters();
            for (int j = 0; j < table.size(); j++) {
                double sum = 0;
                int n = 0;
                for (int k = 0; k < rows; k++) {
                    if (j == labels.getRow(k).get(0).intValue()) {
                        sum += features.getRow(k).get(i);
                        n++;
                    }
                }
                double mean = sum / n;
                double diffSum = 0;
                for (int k = 0; k < rows; k++) {
                    if (j == labels.getRow(k).get(0).intValue()) {
                        double val = features.getRow(k).get(i) - mean;
                        diffSum += (val * val);
                    }
                }
                double stddev = sqrt(diffSum / (n - 1));
                table.get(j).get(0).setCurrentVal(mean);
                table.get(j).get(1).setCurrentVal(stddev);
            }
        }
    }

    @Override
    public List<Double> predict(List<Double> in) {
        final int burnIns = 1000;

        for (int j = 0; j < children.size(); j++) {
            children.get(j).setCurrentVal(in.get(j));
        }

        int[] res = new int[3];
        for (int i = 0; i < burnIns; i++) {
            parent.sample();
            res[(int) parent.getCurrentVal()]++;
        }

        int ind = -1;
        double max = 0;

        if (res[0] > max) {
            max = res[0];
            ind = 0;
        }
        if (res[1] > max) {
            max = res[1];
            ind = 1;
        }
        if (res[2] > max) {
            ind = 2;
        }

        List<Double> out = new ArrayList<Double>();
        out.add((double) ind);

        return out;
    }

    private int[] calculateLabelFrequency(Matrix labels) {
        int col = 0, size = labels.getColumnAttributes(col).size();
        int[] weights = new int[size];

        for (int i = 0; i < labels.getNumRows(); i++) {
            int value = labels.getRow(i).get(col).intValue();
            weights[value]++;
        }
        return weights;
    }

    private void testTables(Categorical parent) {
        System.out.println("Parent parameters:");
        printTable(parent);

        System.out.println("Children parameters:");
        for (Variable child : parent.getChildren()) {
            printTable(child);
            System.out.println();
        }
    }

    private void printTable(Variable node) {
        List<List<Node>> table = node.getParameters();
        for (List<Node> row : table) {
            for (Node cell : row) {
                System.out.print(cell.getCurrentVal() + " ");
            }
            System.out.println();
        }
    }

}
