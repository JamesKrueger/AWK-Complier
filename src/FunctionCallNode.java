import java.util.LinkedList;
import java.util.Optional;

public class FunctionCallNode extends StatementNode {
    private final Optional<Token> functionName;
    private final LinkedList <Node> paramaters;

    public FunctionCallNode(Optional<Token> wordToken, LinkedList<Node> paramaters, int lineNumber, int charPosition) {
        super(lineNumber, charPosition);
        this.functionName = wordToken;
        this.paramaters = paramaters;
    }

    public Optional<Token> getFunctionName() {
        return functionName;
    }

    public LinkedList<Node> getParamaters() {
        return paramaters;
    }

    public String toString() {
        return "FunctionCallNode{" +
                "functionName ='" + functionName + '\'' +
                ", paramaters" + paramaters +
                '}';
    }
}

