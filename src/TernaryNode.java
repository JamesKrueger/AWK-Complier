
public class TernaryNode extends Node {
    private final Node condition;
    private final Node trueBranch;
    private final Node falseBranch;

    public TernaryNode(int lineNumber, int charPosition, Node condition, Node trueBranch, Node falseBranch) {
        super(lineNumber, charPosition);
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getTrueBranch() {
        return trueBranch;
    }

    public Node getFalseBranch() {
        return falseBranch;
    }

    @Override
    public String toString() {
        return "TernaryNode{" +
                "condition=" + condition +
                ", trueBranch=" + trueBranch +
                ", falseBranch=" + falseBranch +
                '}';
    }
}
