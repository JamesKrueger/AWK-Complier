
public class ConstantNode extends Node {
    
	private String value;

    public ConstantNode(int lineNumber, int charPosition, String value) {
    
    	super(lineNumber, charPosition);
        
    	this.value = value;
    }

    public String getValue() {
        return value;
    }

    
    public String toString() {
        return value;
    }
}
