
abstract class Node 
{
	
    private int lineNumber;  //Line number in source code
    protected int charPosition;  //Character position in source code

    public Node(int lineNumber, int charPosition) {
        this.lineNumber = lineNumber;
        this.charPosition = charPosition;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getCharPosition() {
        return charPosition;
    }
    public abstract String toString();
}
