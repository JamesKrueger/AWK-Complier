import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class ProgramNode {
    
	private List<BlockNode> beginBlocks;
    
	private List<BlockNode> endBlocks;
    
	private List<BlockNode> otherBlocks;
    
	private List<FunctionDefinitionNode> functionNodes;

    public ProgramNode() {
    
    	this.beginBlocks = new LinkedList<>();
        
    	this.endBlocks = new LinkedList<>();
        
    	this.otherBlocks = new LinkedList<>();
        
    	this.functionNodes = new LinkedList<>();
    }

    public List<BlockNode> getBeginBlocks() {
        return beginBlocks;
    }

    public List<BlockNode> getEndBlocks() {
        return endBlocks;
    }

    public List<BlockNode> getOtherBlocks() {
        return otherBlocks;
    }

    public List<FunctionDefinitionNode> getFunctionNodes() {
        return functionNodes;
    }

    public void addBeginBlock(BlockNode blockNode) {
        beginBlocks.add(blockNode);
    }

    public void addEndBlock(BlockNode blockNode) {
        endBlocks.add(blockNode);
    }

    public void addOtherBlock(BlockNode blockNode, Optional<Node> operation) {
        otherBlocks.add(blockNode);
    }

    public void addFunctionNode(FunctionDefinitionNode functionNode) {
        functionNodes.add(functionNode);
    }  

        public String toString() {
            
    	  StringBuilder result = new StringBuilder();
            
            result.append("Begin Blocks:\n");
          
            for (BlockNode block : beginBlocks) {
            
            	result.append(block.toString()).append("\n");
            }

            result.append("\nEnd Blocks:\n");
            
            for (BlockNode block : endBlocks) {
            
            	result.append(block.toString()).append("\n");
            }

            result.append("\nOther Blocks:\n");
            
            for (BlockNode block : otherBlocks) {
            
            	result.append(block.toString()).append("\n");
            }

            result.append("\nFunction Nodes:\n");
            
            for (FunctionDefinitionNode function : functionNodes) {
            
            	result.append(function.toString()).append("\n");
            }

            return result.toString();
        }
    }

    

