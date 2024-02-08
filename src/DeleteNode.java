import java.util.List;
import java.util.Optional;

public class DeleteNode extends StatementNode {
	
    private Node target;
   

    public DeleteNode(Node target) {
        super(target.getLineNumber(), target.getCharPosition());
        this.target = target;
    }

    public Node getTarget() {
        return target;
    }

 

    public String toString() {
        return "DeleteNode{" +
                "target=" + target +
                '}';
    }
}
