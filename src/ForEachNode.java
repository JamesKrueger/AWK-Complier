
public class ForEachNode extends StatementNode {
Node condition;
BlockNode block;

    public ForEachNode(Node condition,  BlockNode block) {
    	super(condition.getCharPosition(), condition.getLineNumber());
       this.condition = condition;
       this.block = block;
    }

  public Node getCondition() {
	  return condition;
  }

    

    public BlockNode getBlock() {
        return block;
    }

    
    public String toString() {
    	return "ForEachNode(" + condition + ") block {" + block + "}";
    }
}