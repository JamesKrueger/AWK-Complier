
import java.util.LinkedList;

class FunctionDefinitionNode extends Node {
   
	private String functionName;
    
	private LinkedList<String> parameters;
    
	private LinkedList<StatementNode> statements;

    public FunctionDefinitionNode(String functionName, LinkedList<String> parameters, LinkedList<StatementNode> statements, int lineNumber, int charPosition) {
        
    	super(lineNumber, charPosition);
    
        this.functionName = functionName;
        
        this.parameters = parameters;
        
        this.statements = statements;
    }
    public FunctionDefinitionNode(int lineNumber, int charPosition) {
    	super(lineNumber, charPosition);

    }
    public String getFunctionName() {
        return functionName;
    }

    public LinkedList<String> getParameters() {
        return parameters;
    }

    public LinkedList<StatementNode> getStatements() {
        return statements;
    }

    public String toString() 
    {
        return "FunctionDefinitionNode{" +
                "functionName='" + functionName + '\'' +
                ", parameters=" + parameters +
                ", statements=" + statements +
                ", lineNumber=" + getLineNumber() +
                ", charPosition=" + getCharPosition() +
                '}';
    }
}
