package ml.projectfive;

import ml.projectfive.node.InverseGamma;
import ml.projectfive.node.Normal;

public class GolfNetwork {

    public static void main(String[] args) {

        Normal nodeA = new Normal(1);
        nodeA.buildTable();
        nodeA.getParameters().get(0).get(0).setCurrentVal(71.82);
        nodeA.getParameters().get(0).get(1).setCurrentVal(2.42);

        InverseGamma nodeB = new InverseGamma(1);
        nodeB.buildTable();
        nodeB.getParameters().get(0).get(0).setCurrentVal(18);
        nodeB.getParameters().get(0).get(1).setCurrentVal(66.667);

        InverseGamma nodeC = new InverseGamma(1);
        nodeC.buildTable();
        nodeC.getParameters().get(0).get(0).setCurrentVal(18);
        nodeC.getParameters().get(0).get(1).setCurrentVal(66.667);

        InverseGamma nodeD = new InverseGamma(1);
        nodeD.buildTable();
        nodeD.getParameters().get(0).get(0).setCurrentVal(83);
        nodeD.getParameters().get(0).get(1).setCurrentVal(714.29);

        Normal difficulty = new Normal(1);
        difficulty.buildTable();
        difficulty.getParameters().get(0).set(0, nodeA);
        difficulty.getParameters().get(0).set(1, nodeB);

        Normal error = new Normal(1);
        error.buildTable();
        error.getParameters().get(0).get(0).setCurrentVal(0);
        error.getParameters().get(0).set(1, nodeC);


        Normal swings = new Normal(1);
        swings.buildTable();
        // TODO: how to do mean?
        swings.getParameters().get(0).set(1, nodeD);
    }

}
