import java.util.Optional;

public class VariableReferenceNode extends Node {
    
	private String name;
    
	private Optional<Node> indexExpression;

    public VariableReferenceNode(int lineNumber, int charPosition, String name, Optional<Node> indexExpression) {
    
    	super(lineNumber, charPosition);
        
    	this.name = name;
        
    	this.indexExpression = indexExpression;
    }

    public String getName() {
        return name;
    }
   
    public Optional<Node> getIndexExpression() {
        return indexExpression;
    }

    public String toString() {
       
    	if (indexExpression.isPresent()) {
        
    		return name + "[" + indexExpression.get().toString() + "]";
        } 
    	
    	else {
        
    		return name;
        }
    }
}
