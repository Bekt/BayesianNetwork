import ml.projectfive.node.Categorical;
import ml.projectfive.node.Node;

import java.util.List;

/**
 * Simple test from 3.4.3.1 in the book.
 */
public class GibbsTest {

    public static void main(String[] args) {

        Categorical a = new Categorical(0, 2);
        Categorical b = new Categorical(0, 2);
        Categorical c = new Categorical(0, 2);

        for (int i = 1; i >= 0; i--) {
            a.addLabelValue(i);
            b.addLabelValue(i);
            c.addLabelValue(i);
        }

        b.addCatParent(a);
        a.addChild(b);

        c.addCatParent(b);
        b.addChild(c);

        a.buildTable();
        b.buildTable();
        c.buildTable();

        List<List<Node>> tableA = a.getParameters();
        tableA.get(0).get(0).setCurrentVal(0.4);
        tableA.get(0).get(1).setCurrentVal(0.6);

        List<List<Node>> tableB = b.getParameters();
        tableB.get(0).get(0).setCurrentVal(2.0 / 3);
        tableB.get(0).get(1).setCurrentVal(1.0 / 3);
        tableB.get(1).get(0).setCurrentVal(3.0 / 7);
        tableB.get(1).get(1).setCurrentVal(4.0 / 7);

        List<List<Node>> tableC = c.getParameters();
        tableC.get(0).get(0).setCurrentVal(0.5);
        tableC.get(0).get(1).setCurrentVal(0.5);
        tableC.get(1).get(0).setCurrentVal(1.0 / 3);
        tableC.get(1).get(1).setCurrentVal(2.0 / 3);

        int[] res = new int[2];
        int n = 20000;

        a.setCurrentVal(0);
        c.setCurrentVal(0);
        for (int i = 0; i < n; i++) {
            b.sample();
            res[(int) b.getCurrentVal()]++;
        }

        // Should be ~0.75
        System.out.println((res[0] / (double) n));

        res = new int[2];

        a.setCurrentVal(1);
        c.setCurrentVal(1);
        for (int i = 0; i < n; i++) {
            b.sample();
            res[(int) b.getCurrentVal()]++;
        }

        // Should be ~0.64
        System.out.println((res[1] / (double) n));
    }

}
