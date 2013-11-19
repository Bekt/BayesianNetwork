package ml.projectfive;

import ml.ARFFParser;
import ml.MLException;
import ml.Matrix;
import ml.SupervisedLearner;
import ml.projectfive.node.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.sqrt;

/**
 * Naive Bayes network for the iris data set.
 */
public class NaiveBayes extends SupervisedLearner {

    private static final int nFoldSize = 2;
    private static final int repetitions = 5;
    private static final int BURN_INS = 100;

    private Matrix features, labels;
    private Categorical parent;
    private List<Variable> children;

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            throw new MLException("No data set given.");
        }

        Matrix matrix = ARFFParser.loadARFF(args[0]);
        int cols = matrix.getNumCols();
        Matrix features = matrix.subMatrixCols(0, cols - 1);
        Matrix labels = matrix.subMatrixCols(cols - 1, cols);

        NaiveBayes learner = new NaiveBayes();
        double mse = learner.repeatNFoldCrossValidation(features, labels, nFoldSize, repetitions);

        System.out.printf("%.4f", mse);
    }

    @Override
    public void train(Matrix features, Matrix labels) {
        this.features = features;
        this.labels = labels;
        this.children = new ArrayList<Variable>();

        if (!labels.isCategorical(0)
                || labels.getColumnAttributes(0).size() == 0) {
            throw new MLException("Only categorical labels are supported.");
        }

        buildNetwork();
        calculateLabelWeights();
        calculateFeatureWeights();
    }

    @Override
    public List<Double> predict(List<Double> in) {
        for (int j = 0; j < children.size(); j++) {
            children.get(j).setCurrentVal(in.get(j));
        }

        int[] count = new int[parent.numValues()];
        for (int i = 0; i < BURN_INS; i++) {
            parent.sample();
            count[(int) parent.getCurrentVal()]++;
        }

        int ind = 0, max = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] > max) {
                max = count[i];
                ind = i;
            }
        }

        List<Double> out = new ArrayList<Double>();
        out.add((double) ind);
        return out;
    }

    private void buildNetwork() {
        int possibleValues = labels.getColumnAttributes(0).size();
        parent = new Categorical(0, possibleValues);

        for (int i = 0; i < possibleValues; i++) {
            parent.addLabelValue(i);
        }
        for (int i = 0; i < features.getNumCols(); i++) {
            Normal child = new Normal(0);
            parent.addChild(child);
            child.addCatParent(parent);
            children.add(child);
        }

        parent.buildTable();
        for (Variable child : parent.getChildren()) {
            child.buildTable();
        }
    }

    private void calculateLabelWeights() {
        int rows = labels.getNumRows();
        int[] labelFrequency = calculateLabelFrequency(labels);
        List<Node> row = parent.getParameters().get(0);
        for (int i = 0; i < row.size(); i++) {
            double weight = (double) labelFrequency[i] / rows;
            row.get(i).setCurrentVal(weight);
        }
    }

    private void calculateFeatureWeights() {
        int rows = labels.getNumRows();
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

    private int[] calculateLabelFrequency(Matrix labels) {
        int col = 0, size = labels.getColumnAttributes(col).size();
        int[] weights = new int[size];

        for (int i = 0; i < labels.getNumRows(); i++) {
            int value = labels.getRow(i).get(col).intValue();
            weights[value]++;
        }
        return weights;
    }

}
