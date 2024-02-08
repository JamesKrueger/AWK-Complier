
import java.util.Optional;

public class OperationNode extends StatementNode {
	
    public enum OperationType {
        EQS, NE, LT, LE, GT, GE, AND, OR, NOT, MATCH, NOTMATCH, DOLLAR,
        PREINC, POSTINC, PREDEC, POSTDEC, UNARYPOS, UNARYNEG, IN,
        EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, CONCATENATION, TENARY,EQTWO
    }

    private Optional <Node> left;
    private Optional<Node> right;
    private OperationType operation;

    public OperationNode(int lineNumber, int charPosition, OperationType operation, Node operand) {
        super(lineNumber, charPosition);
        this.operation = operation;
        this.right = Optional.of(operand);
    }

    public OperationNode(int lineNumber, int charPosition, Node left, OperationType operation, Node right) {
        super(lineNumber, charPosition);
        this.left = Optional.ofNullable(left);
        this.operation = operation;
        this.right = Optional.ofNullable(right);
    }

    public Optional<Node> getLeft() {
        return left;
    }

    public Optional<Node> getRight() {
        return right;
    }

    public OperationType getOperation() {
        return operation;
    }

    public String toString() {
    	
        String leftStr = left.toString();
        
        String rightStr = right.toString();

        switch (operation) {
            case EQS:
                return leftStr + " = " + rightStr;
            case NE:
                return leftStr + " != " + rightStr;
            case LT:
                return leftStr + " < " + rightStr;
            case LE:
                return leftStr + " <= " + rightStr;
            case GT:
                return leftStr + " > " + rightStr;
            case GE:
                return leftStr + " >= " + rightStr;
            case AND:
                return leftStr + " && " + rightStr;
            case OR:
                return leftStr + " || " + rightStr;
            case NOT:
                return "!" + rightStr;
            case EXPONENT:
            	return leftStr + "^" + rightStr;
            	
            case EQTWO:
            	return leftStr + "==" + rightStr;
            	
            case POSTINC:
            	return leftStr + "++";
            	
            case POSTDEC:
            	return leftStr + "--";
            
            case ADD:
            	return leftStr + "+" + rightStr;
            	
            case DOLLAR:
            	return leftStr + "$" +rightStr;
                
            default:
                return ""; 
        }
    }
}
