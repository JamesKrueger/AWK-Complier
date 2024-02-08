
public class ForNode extends StatementNode {
    private Node initialization;
    private  Node condition;
    private  Node incDec;
    private BlockNode block;

    public ForNode(int lineNumber, int charPosition, Node initialization, Node condition, Node incDec, BlockNode block) {
        super(lineNumber, charPosition);
        this.initialization = initialization;
        this.condition = condition;
        this.incDec = incDec;
        this.block = block;
    }

    public Node getInitialization() {
        return initialization;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getincDec() {
        return incDec;
    }

    public BlockNode getBlock() {
        return block;
    }

    
    public String toString() {
        return "ForNode{" +
                "initialization =" + initialization +
                ", condition=" + condition +
                ", inc/dec =" + incDec +
                ", block=" + block +
                '}';
    }
}

