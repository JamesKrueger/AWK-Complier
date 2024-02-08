
import java.util.Optional;

public class AssignmentNode extends StatementNode {
    private Node expression;

    public AssignmentNode(int lineNumber, int charPosition, Node expression) {
        super(lineNumber, charPosition);
        this.expression = expression;
    }

  

    public Node getExpression() {
        return expression;
    }

    
    public String toString() {
        return "AssignmentNode " + expression.toString();
    }
}
