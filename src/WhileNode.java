
public class WhileNode extends StatementNode {
    private final Node condition;
    private final BlockNode block;

    public WhileNode(int lineNumber, int charPosition, Node condition, BlockNode block) {
        super(lineNumber, charPosition);
        this.condition = condition;
        this.block = block;
    }

    public Node getCondition() {
        return condition;
    }

    public BlockNode getBlock() {
        return block;
    }

    public String toString() {
        return "WhileNode{" +
                "condition=" + condition +
                ", block=" + block +
                '}';
    }
}

