
abstract class StatementNode extends Node {

	public StatementNode(int lineNumber, int charPosition) 
	{
		super(lineNumber, charPosition);	
	}
	
	public abstract String toString();
}
