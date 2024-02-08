
public class ReturnNode extends StatementNode {
    private final Node expression;

    public ReturnNode(int lineNumber, int charPosition,Node expression) {
        super(lineNumber, charPosition);
        this.expression = expression;
    }

    public Node getExpression() {
        return expression;
    }

 
    public String toString() {
        return "ReturnNode{" +
                "expression=" + expression +
                '}';
    }
}

