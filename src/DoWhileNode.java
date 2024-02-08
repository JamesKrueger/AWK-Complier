
public class DoWhileNode extends StatementNode {
    private final Node condition;
    private final BlockNode block;

    public DoWhileNode(int lineNumber, int charPosition,Node condition, BlockNode block) {
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
        return "DoWhileNode{" +
                "condition=" + condition +
                ", block=" + block +
                '}';
    }
}

