import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class BlockNode extends StatementNode {
    
	private List<StatementNode> statements;
    
	private Optional<Node> condition;

    public BlockNode(int lineNumber, int charPosition, Optional<Node> condition,List<StatementNode> statements
)
    {
        super(lineNumber, charPosition);
    
        this.statements = new LinkedList<>();
        
        this.condition = Optional.empty();
    }
    public List<StatementNode> getStatements() {
        return statements;
    }

    public Optional<Node> getCondition() {
        return condition;
    }

    public void setCondition(Node condition) {
        this.condition = Optional.of(condition);
    }

    public void addStatement(StatementNode statementNode) {
        statements.add(statementNode);
    }	
    
    public String toString() {
        
    	StringBuilder result = new StringBuilder();

        result.append("Block ").append(super.getLineNumber()).append(", Char ").append(super.getCharPosition()).append(")\n");

        if (condition.isPresent()) {
        
        	result.append("Condition: ").append(condition.get().toString()).append("\n");
        }

        result.append("Statements:\n");
        
        for (StatementNode statement : statements) {
        
        	result.append(statement.toString()).append("\n");
        }

        return result.toString();
    }

}