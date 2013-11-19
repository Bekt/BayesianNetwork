package ml.projectfive.node;

public class SumNode extends Node {

    private Normal tournament, error;

    public SumNode(Normal tournament, Normal error) {
        super(1);
        this.tournament = tournament;
        this.error = error;
    }

    @Override
    public double getCurrentVal() {
        return tournament.getCurrentVal() + error.getCurrentVal();
    }

}
