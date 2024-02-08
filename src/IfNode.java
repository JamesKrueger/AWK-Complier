import java.util.Optional;

public class IfNode extends StatementNode {
    private Node condition;
    private BlockNode trueBlock;  // block for the 'if' part
    private Optional<IfNode> nextIf;  // for 'else if'
    private Optional<BlockNode> elseBlock;  // block for the 'else' part, if present
    private int lineNumber;
    private int charPosition;
    
    public IfNode(int lineNumber, int charPosition, Node condition, BlockNode trueBlock, Optional<IfNode> nextIf, Optional<BlockNode> elseBlock) {
    	super(lineNumber, charPosition);
    	this.condition = condition;
        this.trueBlock = trueBlock;
        this.nextIf = nextIf;
        this.elseBlock = elseBlock;
    }

    public Node getCondition() {
        return condition;
    }

    public BlockNode getTrueBlock() {
        return trueBlock;
    }

    public Optional<IfNode> getNextIf() {
        return nextIf;
    }

    public Optional<BlockNode> getElseBlock() {
        return elseBlock;
    }
    public String toString() {
        return "If: " + condition + "\n" +
               "Then: " + trueBlock + "\n" +
               "Next: " + nextIf + "\n" +
               "Else: " + elseBlock;
    }

}

