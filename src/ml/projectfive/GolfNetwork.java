package ml.projectfive;

import helpers.Pair;
import ml.*;
import ml.projectfive.node.*;
import java.io.IOException;
import java.util.*;

public class GolfNetwork {

    private static final int TOURNAMENTS = 42;
    private static final int GOLFERS = 604;
    private static int BURN_INS = 10000;

    private Matrix data;
    private List<Normal> difficulties = new ArrayList<Normal>();
    private List<Normal> errors = new ArrayList<Normal>();
    private List<Variable> network = new ArrayList<Variable>();
    private Map<Normal, Integer> emap = new HashMap<Normal, Integer>();

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            throw new MLException("No data set given.");
        }
        if (args.length == 2) {
            BURN_INS = Integer.valueOf(args[1]);
        }

        Matrix data = ARFFParser.loadARFF(args[0]);

        GolfNetwork golfNetwork = new GolfNetwork(data);
        golfNetwork.buildNetwork();

        List<Pair<String, Double>> top10 = golfNetwork.getTopGolfers(10);
        for (Pair<String, Double> score : top10) {
            System.out.printf("%20s: %.5f%n", score.first, score.second);
        }
    }

    public GolfNetwork(Matrix data) {
        this.data = data;
    }

    public void buildNetwork() {
        Normal nodeA = new Normal(1);
        nodeA.buildTable();
        nodeA.getParameters().get(0).get(0).setCurrentVal(71.82);
        nodeA.getParameters().get(0).get(1).setCurrentVal(2.42);
        network.add(nodeA);

        InverseGamma nodeB = new InverseGamma(1);
        nodeB.buildTable();
        nodeB.getParameters().get(0).get(0).setCurrentVal(18);
        nodeB.getParameters().get(0).get(1).setCurrentVal(66.667);
        network.add(nodeB);

        InverseGamma nodeC = new InverseGamma(1);
        nodeC.buildTable();
        nodeC.getParameters().get(0).get(0).setCurrentVal(18);
        nodeC.getParameters().get(0).get(1).setCurrentVal(66.667);
        network.add(nodeC);

        InverseGamma nodeD = new InverseGamma(1);
        nodeD.buildTable();
        nodeD.getParameters().get(0).get(0).setCurrentVal(83);
        nodeD.getParameters().get(0).get(1).setCurrentVal(714.29);
        network.add(nodeD);

        for (int i = 0; i < TOURNAMENTS; i++) {
            Normal difficulty = new Normal(1);
            difficulty.buildTable();
            difficulty.getParameters().get(0).set(0, nodeA);
            difficulty.getParameters().get(0).set(1, nodeB);
            difficulties.add(difficulty);
            network.add(difficulty);

            nodeA.addChild(difficulty);
            nodeB.addChild(difficulty);
        }

        for (int i = 0; i < GOLFERS; i++) {
            Normal error = new Normal(1);
            error.buildTable();
            error.getParameters().get(0).get(0).setCurrentVal(0);
            error.getParameters().get(0).set(1, nodeC);
            errors.add(error);
            network.add(error);

            nodeC.addChild(error);
            emap.put(error, i);
        }

        for (int i = 0; i < data.getNumRows(); i++) {
            List<Double> row = data.getRow(i);
            double name = row.get(0), score = row.get(1), tourn = row.get(2);

            Normal tournament = difficulties.get(((int) tourn) - 1);
            Normal error = errors.get((int) name);
            SumNode mu = new SumNode(tournament, error);

            Normal swing = new Normal(score);
            swing.buildTable();
            swing.getParameters().get(0).set(0, mu);
            swing.getParameters().get(0).set(1, nodeD);

            nodeD.addChild(swing);
            tournament.addChild(swing);
            error.addChild(swing);
        }
    }

    public List<Pair<String, Double>> getTopGolfers(int limit) {
        if (limit < 0 || limit >= data.getNumRows()) {
            throw new IndexOutOfBoundsException();
        }

        List<List<Double>> samples = new ArrayList<List<Double>>();
        for (int i = 0; i < GOLFERS; i++) {
            samples.add(new ArrayList<Double>());
        }
        for (int i = 0; i < BURN_INS; i++) {
            Collections.shuffle(network);
            for (Variable node : network) {
                node.sample();
            }
        }
        for (int i = 0; i < BURN_INS; i++) {
            Collections.shuffle(network);
            for (Variable node : network) {
                node.sample();
                if (emap.containsKey(node)) {
                    samples.get(emap.get(node)).add(node.getCurrentVal());
                }
            }
        }

        List<Pair<String, Double>> medians = new ArrayList<Pair<String, Double>>();
        for (int i = 0; i < samples.size(); i++) {
            List<Double> sample = samples.get(i);
            String name = data.getColumnAttributes(0).getValue(i);
            Collections.sort(sample);
            double median = sample.get(sample.size() / 2);
            medians.add(new Pair<String, Double>(name, median));
        }

        sortPairs(medians);

        return medians.subList(0, limit);
    }

    /**
     * TODO: Make this generic and move it to Pair.java
     */
    private static void sortPairs(List<Pair<String, Double>> pairs) {
        Collections.sort(pairs, new Comparator<Pair<String, Double>>() {
            @Override
            public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
                return o1.second.compareTo(o2.second);
            }
        });
    }

}
