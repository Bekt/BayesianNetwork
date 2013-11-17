package ml.projectfive;

import ml.ARFFParser;
import ml.MLException;
import ml.Matrix;

import java.io.IOException;

public class Main {

    static final int nFoldSize = 2;
    static final int repetitions = 5;

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

        System.out.println(mse);
    }

}
