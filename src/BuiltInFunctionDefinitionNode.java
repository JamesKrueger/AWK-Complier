import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

public class BuiltInFunctionDefinitionNode extends FunctionDefinitionNode {
    
	private boolean isVariadic;
    
	private int lineNumber;
    
	private int charPosition;
   
	public BuiltInFunctionDefinitionNode(Function<HashMap<String, InterpreterDataType>, String> Execute, boolean isVariadic, int lineNumber, int charPosition) {
	    super(lineNumber, charPosition);
	    this.Execute = Execute;
	    this.isVariadic = isVariadic;
	}

	public boolean isVariadic() {
    return isVariadic;
	}

	public Function <HashMap <String,InterpreterDataType> , String> Execute;

    
}
