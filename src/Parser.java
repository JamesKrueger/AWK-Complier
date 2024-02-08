import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Parser {

	List<Token> tokens = new LinkedList<>();//List of my tokens
	
	private TokenManager tokenManager;

	private ProgramNode programNode;
	
	
	  public Parser(ProgramNode programNode, TokenManager tokenManager) {
	       
		  this.programNode = programNode;
	      
		  this.tokenManager = tokenManager;
	    }
	  
	  
	  /**
	   * Loops over and sees if what we have is a Function or a Action by checking the programNode
	   * @return a programNode
	   * @throws Exception if neither 
	   */
	  
	  public ProgramNode Parse() throws Exception {
		  
			 programNode = new ProgramNode();

			    while (tokenManager.moreTokens()) 
			    {
			  AcceptSeperators();
			   
			  if (ParseFunction(programNode)) {
			        
			  		continue; //Worked
			  
			  } 
		    	else if (ParseAction(programNode)) {
			        
		    		continue; //Worked
			  
		    	} 
		
		    	else {
			        
			   		throw new Exception("Unexpected token at line " + tokenManager.peek(0).get().getLineNumber() +
			                   ", position " + tokenManager.peek(0).get().getCharPos());
			        }
			    }

			    return programNode;
			}

	  
	  /**
	   * Checks if token is separator by using matchAndRemove
	   * @return true if we find a separator token
	   */
	  public boolean AcceptSeperators() 
	  {
		  
		    boolean foundSeparator = false;//Set to false before loop

		    while (tokenManager.matchAndRemove(Token.TokenType.SEPARATOR).isPresent()) //Checks for Separator Tokens 
		    {
		        foundSeparator = true;//Set boolean to true

		    }
			
		    return foundSeparator;

		    
		}




	 

/**
 * Checks for KeyWords Begin and End which for now is the start of an action
 * @param programNode is where we are adding our blocks, either a begin or end one rn
 * @return false if there is no Begin or End
 */
	boolean ParseAction(ProgramNode programNode) 
	
	{
		
		    if (tokenManager.matchAndRemove(Token.TokenType.BEGIN).isPresent()) {//Checks for the Begin token
		        
		    	AcceptSeperators();
		    	BlockNode beginBlock = ParseBlock();//We gonna then use parseBlock to parse the begin block
		    	programNode.addBeginBlock(beginBlock);//then adding that to the ProgramNode
		        
		    	return true;
		    }
		    
		    
		    AcceptSeperators();
		    
		    if(tokenManager.matchAndRemove(Token.TokenType.BEGIN) == null )
	    		throw new IllegalArgumentException("Missing BEGIN token");

		    
		    
		    if (tokenManager.matchAndRemove(Token.TokenType.END).isPresent()) {//Checking for End token

		    	   AcceptSeperators();
		    	BlockNode endBlock = ParseBlock();//Then gonna use parseBlock to parse the end block
		        
		        programNode.addEndBlock(endBlock);//Then add it to the programNode as a endBlock
		        
		        return true;
		    }
		    AcceptSeperators();
		    if(tokenManager.matchAndRemove(Token.TokenType.END) == null )
	    		throw new IllegalArgumentException("Missing END token");
		    
		    else
		    {
		    	
		    	Optional<Node> operation = Optional.ofNullable(ParseOperation());//Gonna use parseOperation to parse things that isnt a begin or end block
		    
		    	BlockNode otherBlock = ParseBlock();
		    	
		    	programNode.addOtherBlock(otherBlock,operation);
		    }

		   
		    
		    return false;
		}
	

	 /** 
	  * Checks for a FunctionDefiniton which needs a certain syntax
	  * @param programNode is where we are adding the FunctionBlock
	  * @return false if no keyWords are present 
	  */
	 public boolean ParseFunction(ProgramNode programNode) {
		    // Check if the current token is the "function" keyword
		 
		    Optional<Token> functionToken = tokenManager.matchAndRemove(Token.TokenType.FUNCTION);//Checks for keyword function 
		 
		   	if (functionToken.isPresent()) {//If function is there
		        // Get the function name
		    
		    	Optional<Token> functionNameToken = tokenManager.matchAndRemove(Token.TokenType.WORD);//We check for a word 
		        
		    	if (!functionNameToken.isPresent()) {//If its not there function missing a name
		        
		    		throw new IllegalArgumentException("Missing function name");
		        }
		        String functionName = functionNameToken.get().getValue();//Get the value of the word. 

		        // Check for opening parenthesis
		        
		        if (!tokenManager.matchAndRemove(Token.TokenType.LEFTPAR).isPresent()) {//Checks if the ( is not there 
		            
		        	throw new IllegalArgumentException("Missing opening parenthesis in function definition");
		        }

		        //Create a list to store parameters
		        
		        LinkedList<String> parameters = new LinkedList<>();

		        //Parse parameter list
		        
		        	Optional<Token> parameterToken = tokenManager.matchAndRemove(Token.TokenType.WORD);//Checks for token Word

		        	while (parameterToken.isPresent()) {//While its there
		        	
		        		parameters.add(parameterToken.get().getValue());//Get the value

		        	   
		        	 //Accept separators before comma
			        	
		        		if(tokenManager.matchAndRemove(Token.TokenType.SEPARATOR).isPresent())//check if separator is there
			        		 
		        			AcceptSeperators();//call method to take it
			        	  
		        	    if (tokenManager.matchAndRemove(Token.TokenType.COMMA).isEmpty()) {//Check for comma
		        	        
		        	    	break;//Break so we can reset 
		        	    }
		        	    
		        	    //Accept separators after comma
		        	    if(tokenManager.matchAndRemove(Token.TokenType.SEPARATOR).isPresent())//Check for another separator after comma
			        		
		        	    	AcceptSeperators();//Call method

		        	    	parameterToken = tokenManager.matchAndRemove(Token.TokenType.WORD);
		        	}


		        //Check for closing parenthesis
		        
		        	if (!tokenManager.matchAndRemove(Token.TokenType.RIGHTPAR).isPresent()) {
		            
		        		throw new IllegalArgumentException("Missing closing parenthesis in function definition");
		        }

		        
		        	AcceptSeperators();
		        	BlockNode blockNode = ParseBlock();

		        FunctionDefinitionNode functionDefinitionNode = new FunctionDefinitionNode(functionName, parameters, (LinkedList<StatementNode>) blockNode.getStatements(), 0, 0);

		        //Add FunctionDef to functionNode		        
		        programNode.addFunctionNode(functionDefinitionNode);

		        return true; //Worked
		    }
		
		    	return false;
	 }		


	
	 
	 

	  public Node ParseOperation() 
	  {
		   return parseAssignment();
		}
	 
	  
	  
	  
/**
 * Code checks for the certian tokenType using matchAndRemove, 
 * stores the value and we check if its there due to us needing the "value/name" of it. 
 * @return ParselValue or  null
 */
	 
	 public Node ParseBottomLevel() {
		    int charPosition = tokenManager.peek(0).get().getCharPos();
		    int lineNumber = tokenManager.peek(0).get().getLineNumber();

		  

		    Optional<Token> stringLiteralToken = tokenManager.matchAndRemove(Token.TokenType.STRINGLIT);
		    if (stringLiteralToken.isPresent()) {
		        return new ConstantNode(lineNumber, charPosition, stringLiteralToken.get().getValue());
		    }

		    Optional<Token> numberToken = tokenManager.matchAndRemove(Token.TokenType.NUMBER);
		    if (numberToken.isPresent()) {
		        return new ConstantNode(lineNumber, charPosition, numberToken.get().getValue());
		    }

		    Optional<Token> patternToken = tokenManager.matchAndRemove(Token.TokenType.PATTERN);
		    if (patternToken.isPresent()) {
		        return new PatternNode(lineNumber, charPosition, patternToken.get().getValue());
		    }

		    if (tokenManager.matchAndRemove(Token.TokenType.LEFTPAR).isPresent()) {
		    	
		        Optional<Node> operationOptional = Optional.ofNullable(ParseOperation());

		        if (operationOptional.isPresent()) {
		            
		            return operationOptional.get();
		        } else {
		            throw new IllegalArgumentException("Invalid expression inside parentheses");
		        }
		    }

		   
		    Token.TokenType opType = tokenManager.peek(0).get().getType();

		    if (opType == Token.TokenType.EXCLIMATIONPOINT ||
		        opType == Token.TokenType.SUB ||
		        opType == Token.TokenType.ADD ||
		        opType == Token.TokenType.DOUBLEADD ||
		        opType == Token.TokenType.DOUBLESUB) {
		            
		        Token.TokenType operatorType = tokenManager.matchAndRemove(opType).get().getType();
		        Node operand = ParseOperation();
		        OperationNode.OperationType operationType = null;

		        switch (operatorType) {
		            case EXCLIMATIONPOINT:
		                operationType = OperationNode.OperationType.NOT;
		                break;
		            case SUB:
		                operationType = OperationNode.OperationType.UNARYNEG;
		                break;
		            case ADD:
		                operationType = OperationNode.OperationType.UNARYPOS;
		                break;
		            case DOUBLEADD:
		                operationType = OperationNode.OperationType.PREINC;
		                break;
		            case DOUBLESUB:
		                operationType = OperationNode.OperationType.PREDEC;
		                break;
		        }

		        return new OperationNode(lineNumber, charPosition, operationType, operand);
		    }


		    // If none of the above conditions are met, try parsing an LValue
		    return ParseLValue().orElse(null);
		}

	 public Optional<Node> ParseLValue() {

		    int lineNumber = tokenManager.peek(0).get().getLineNumber();
		    
		    int charPosition = tokenManager.peek(0).get().getCharPos();

		    Optional<Token> dollarSignToken = tokenManager.matchAndRemove(Token.TokenType.MONEYSIGN);

		    if (dollarSignToken.isPresent()) {

		        // Ensure the next token isn't another $, or any token that would recursively call ParseLValue
		        if (tokenManager.peek(0).get().getType() == Token.TokenType.MONEYSIGN) {
		            throw new IllegalArgumentException("Unexpected token after $ at line " + lineNumber);
		        }

		        Node result = ParseBottomLevel();

		        if (result != null) {
		            return Optional.of(new OperationNode(lineNumber, charPosition, OperationNode.OperationType.DOLLAR, result));
		        }
		    }

		    Optional<Token> wordToken = tokenManager.matchAndRemove(Token.TokenType.WORD);

		    if (wordToken.isPresent()) {

		        String name = wordToken.get().getValue();

		        Optional<Token> leftBracketToken = tokenManager.matchAndRemove(Token.TokenType.LEFTBRA);

		        if (leftBracketToken.isPresent()) 
		        
		        {

		            Node index = ParseOperation();
		            // Ensure we consume the closing bracket, if present
		            if (!tokenManager.matchAndRemove(Token.TokenType.RIGHTBRA).isPresent()) {
		            
		            }

		            return Optional.of(new VariableReferenceNode(lineNumber, charPosition, name, Optional.of(index)));
		        }

		        return Optional.of(new VariableReferenceNode(lineNumber, charPosition, name, Optional.empty()));
		    }

		    return Optional.empty();
		}



	 
	 
	/**
	 * Looks for token types of times divide and modulo
	 * @return operationNode with the desired operation
	 */
	public Node ParseTerm() {
	    
		Node left = ParseExponents();
	    
		while (true) {
	    
			Optional<Token> op = tokenManager.matchAndRemove(Token.TokenType.TIMES);
	        
			if (op.isEmpty()) 
				op = tokenManager.matchAndRemove(Token.TokenType.BACKSLASH);
			
			if (op.isEmpty())
				op = tokenManager.matchAndRemove(Token.TokenType.PERCENT);

	        if (op.isEmpty()) 
	            return left;
	        
	        Node right = ParseExponents();
	        OperationNode.OperationType operationType = null;

	        switch (op.get().getType()) {
	           
	        case TIMES:
	        
	        	operationType = OperationNode.OperationType.MULTIPLY;
	            
	        	break;
	            
	        case BACKSLASH:
	        
	        	operationType = OperationNode.OperationType.DIVIDE;
	            
	        	break;
	                
	            
	        case PERCENT:
	            
	            	operationType = OperationNode.OperationType.MODULO;
	                
	            	break;	                
	        }

	        left = new OperationNode(left.getLineNumber(), left.getCharPosition(), left, operationType, right);
	    }
	}
	
	/**
	 * Looks for token type Add and subtract 
	 * @return and operation node with the desired operation
	 */
public Node ParseExpression() {
	    
		Node left = ParseTerm();
	    
		while (true) {
	   
			Optional<Token> op = tokenManager.matchAndRemove(Token.TokenType.ADD);
	   
			if (op.isEmpty()) 
				op = tokenManager.matchAndRemove(Token.TokenType.SUB);
	        
			if (op.isEmpty()) 
				return left;

	        Node right = ParseTerm();
	        
	        OperationNode.OperationType operationType = null;

	        switch (op.get().getType()) 
	        
	        {
	        
	        case ADD:
	        
	        	operationType = OperationNode.OperationType.ADD;
	            
	        	break;
	            
	        case SUB:
	        
	        	operationType = OperationNode.OperationType.SUBTRACT;
	            
	        	break;
	        
	        }

	        left = new OperationNode(left.getLineNumber(), left.getCharPosition(), left, operationType, right);
	    }
	}
	
	/**
	 * Looks for token type DoubleAdd and DoubleSub 
	 * @return Creates the operationNode with the desired increment 
	 */
public Node ParsePostIncDec() {
	
    Node result = ParseBottomLevel();
    
 
    
    OperationNode.OperationType operationType; 

    while (tokenManager.moreTokens()) {
        Token.TokenType opType = tokenManager.peek(0).get().getType();

        if (opType == Token.TokenType.DOUBLEADD) {
            operationType = OperationNode.OperationType.POSTINC;         
            } else if (opType == Token.TokenType.DOUBLESUB) {
            operationType = OperationNode.OperationType.POSTDEC; 
        } else {
            break; 
        }

        tokenManager.matchAndRemove(opType); 

       
        result = new AssignmentNode(result.getLineNumber(), result.getCharPosition(),
        		new OperationNode(result.getLineNumber(), result.getCharPosition(), result,operationType,null));

    }

    return result;
}

	
/**
 * Looks for token type upArrow (carrot), uses right assoicativty and recursivly builds the left side 
 * @return operationNode with desired base and exponent 
 */
	 public Node ParseExponents() {
		 
		    Node right = ParsePostIncDec();

		    while (tokenManager.moreTokens()) 
		    {
		        Optional<Token> caretToken = tokenManager.matchAndRemove(Token.TokenType.UPARROW);

		        if (caretToken.isPresent()) {
		            
		        	Node left = ParseExponents();
		            
		        	right = new OperationNode(right.getLineNumber(), right.getCharPosition(), right, OperationNode.OperationType.EXPONENT ,left);
		            
		        } else {
		            break;
		        }
		    }

		    return right;
		}
	 /**
	  * Using left assoicatvity we check for string by calling higher level and recursively builds right
	  * @return operationNode with concatenation and left and right node 
	  */
	public Node ParseConcatenation() {

	    Node left = ParseExpression(); 

	    while (tokenManager.moreTokens()) {
	    	
	        Node right = ParseConcatenation();

	        if (right == null) {
	            return left;
	        }

	        left = new OperationNode(left.getLineNumber(), left.getCharPosition(), left, OperationNode.OperationType.CONCATENATION, right);
	    }
		return left;
	}



	 /**
	  * Looks for token certain token types that are comparisons and assigns the tokens to an opType
	  * @return OperationNode with desired comparison 
	  */
	 public Node ParseComparison() {
		 
		    Node result = ParseExpression();

		    while (tokenManager.moreTokens()) 
		    {
		        Token.TokenType opType = tokenManager.peek(0).get().getType();

		        if (opType == Token.TokenType.LEFTARROW ||
		    
		        		opType == Token.TokenType.LESSEQUAL ||
		                
		        		opType == Token.TokenType.FACTEQUAL ||
		               
		                
		        		opType == Token.TokenType.GREATEREQUAL ||
		                
		        		opType == Token.TokenType.RIGHTARROW 
		                
		       
		                
		        		) {

		            Optional<Token> operatorToken = tokenManager.matchAndRemove(opType);

		            if (operatorToken.isPresent()) {
		                
		            	Token.TokenType operatorType = operatorToken.get().getType();
		                
		            	Node rightOperand = ParseComparison();

		            	
		                OperationNode.OperationType operationType = null;

		                switch (operatorType) {
		                
		                case LEFTARROW:
		                
		                	operationType = OperationNode.OperationType.LT;
		                    
		                	break;
		                    
		                case LESSEQUAL:
		                
		                	operationType = OperationNode.OperationType.LE;
		                    
		                	break;
		                    
		                case FACTEQUAL:
		                
		                	operationType = OperationNode.OperationType.NE;
		                    
		                	break;
		                    
		             
		                    
		                case RIGHTARROW:
		                
		                	operationType = OperationNode.OperationType.GT;
		                    
		                	break;
		                    
		                case GREATEREQUAL:
		                
		                	operationType = OperationNode.OperationType.GE;
		                    
		                	break;
		                    
		            
		                }

		                result = new OperationNode( result.getLineNumber(), result.getCharPosition(), result, operationType, rightOperand );
		            }
		        } else {
		            break;
		        }
		    }

		    return result;
		}
	 
	 /**
	  * Checks for token type ~ and !~ 
	  * @return the operationNode with desired match operation for either match
	  */
	 public Node ParseMatch() {
		    
		 Node result = ParseComparison();

		    while (tokenManager.moreTokens()) {

		    	Token.TokenType opType = tokenManager.peek(0).get().getType();

		        if (opType == Token.TokenType.SWINGLY) {
		        
		        	Optional<Token> operatorToken = tokenManager.matchAndRemove(opType);

		            if (operatorToken.isPresent()) {
		            
		            	Token.TokenType operatorType = operatorToken.get().getType();
		                
		            Node rightOperand = ParseMatch();

		                OperationNode.OperationType operationType = null;

		                switch (operatorType) {
		                
		                case SWINGLY:
		                        
		                	operationType = OperationNode.OperationType.MATCH;
		                
		                        break;
		                }

		                result = new OperationNode(result.getLineNumber(), result.getCharPosition(), result, operationType, rightOperand);
		            }
		        } 
		        
		        if (opType == Token.TokenType.FACTSWINGLY) {
		            
		        	Optional<Token> operatorToken = tokenManager.matchAndRemove(Token.TokenType.FACTSWINGLY);

		            if (operatorToken.isPresent()) {
		            
		            	Node rightOperand = ParseMatch();

		                OperationNode.OperationType operationType = OperationNode.OperationType.NOTMATCH;

		                result = new OperationNode(result.getLineNumber(), result.getCharPosition(),result, operationType, rightOperand);
		            }
		        } else {
		            break;
		        }
		    }

		    return result; 
		    
		}
	 /**
	  * Looks for keyWord in to see for an expression
	  * @return ArrayMembershipNode with expression then the arrayName.
	  */
	 public Node ParseArrayMembership() {
		 
		    Node expression = ParseMatch(); 

		    if (tokenManager.moreTokens()) {
		        Token.TokenType opType = tokenManager.peek(0).get().getType();

		        if (opType == Token.TokenType.IN) {
		            Optional<Token> inToken = tokenManager.matchAndRemove(Token.TokenType.IN);

		            if (inToken.isPresent()) {
		                Node arrayNameNode = ParseArrayMembership();

		                OperationNode operationNode = 
		                new OperationNode(expression.getLineNumber(), expression.getCharPosition(),  
		                	expression, OperationNode.OperationType.IN, arrayNameNode
		                );

		                return operationNode;
		            } else {
		                throw new IllegalArgumentException("Unexpected token: " + opType);
		            }
		        }
		    }

		    return expression;
		}




/**
 * Looks for token type && and then assigns the operation type and
 * @return operationNode with AND operation with a left and right
 */
	 public Node ParseLogicalAnd() {
		    
		 Node result = ParseArrayMembership();

		    while (tokenManager.moreTokens()) {
		 
		    	Token.TokenType opType = tokenManager.peek(0).get().getType();

		        if (opType == Token.TokenType.DOUBLEAND) {
		        
		        	Optional<Token> operatorToken = tokenManager.matchAndRemove(Token.TokenType.DOUBLEAND);

		            if (operatorToken.isPresent()) {
		           		                
		            	Node rightOperand = ParseLogicalAnd();

		                OperationNode.OperationType operationType = OperationNode.OperationType.AND;

		                result = new OperationNode( result.getLineNumber(), result.getCharPosition(), result,operationType, rightOperand);
		            }
		        } else {
		            break;
		        }
		    }

		    return result;
		}
/**
 * Looks for token type || and then assigns the operation type OR to it 
 * @return operationNode with operation OR with left and right
 */
	 public Node ParseLogicalOr() {
		    
		 Node result = ParseLogicalAnd();

		    while (tokenManager.moreTokens()) {
		 
		    	Token.TokenType opType = tokenManager.peek(0).get().getType();

		        if (opType == Token.TokenType.DOUBLEOR) {
		        
		        	Optional<Token> operatorToken = tokenManager.matchAndRemove(opType);

		            if (operatorToken.isPresent()) {
		            
		            	Token.TokenType operatorType = operatorToken.get().getType();
		                
		            	Node rightOperand = ParseLogicalAnd();

		                OperationNode.OperationType operationType = null;

		                switch (operatorType) {
		                
		                case DOUBLEOR:
		                
		                	operationType = OperationNode.OperationType.OR;
		                    
		                	break;
		                }

		                result = new OperationNode(result.getLineNumber(), result.getCharPosition(), result, operationType, rightOperand);
		            }
		        } else {
		            break;
		        }
		    }

		    return result;
		}
	 
	 /**
	  * Looks for token type ? and : and recursively gets the true branch 
	  * @return OperationNode with the condition true branch and false branch
	  */
	 public Node ParseTenary() {
		 
		    Node condition = ParseLogicalOr();

		    if (tokenManager.matchAndRemove(Token.TokenType.QUESTIONMARK).isPresent()) {
		      
		    	Node falseBranch = ParseLogicalOr();

		        tokenManager.matchAndRemove(Token.TokenType.COLON); 

		        Node trueBranch = ParseTenary();

		        if (trueBranch != null && falseBranch != null) {
		        
		        	TernaryNode ternaryNode = new TernaryNode(condition.getLineNumber(), condition.getCharPosition(), condition, trueBranch, falseBranch);
		            
		        	return ternaryNode;
		        } 
		        else {
		        
		        	throw new IllegalArgumentException("Invalid conditional expression");
		        }
		    }

		    return condition;
		}
	 
	 /**
	  * Uses right assoicatvity, Looks for certain token types that are comparisons 
	  * @return An assignment node with and operationNode due to having operators with our assignments 
	  */
	 public Node parseAssignment() {
		 
	        Node rValue = ParseTenary(); 

	        while (tokenManager.moreTokens()) 
	        {
	            Token.TokenType opType = tokenManager.peek(0).get().getType();

	            switch (opType) {
	                
	            	case ARROWEQUAL:
	                
	                case PERCENTEQUAL:
	                
	                case TIMESEQUAL:
	                
	                case DIVIDEEQUAL:
	                
	                case ADDEQUAL:
	                
	                case SUBEQUAL:
	                
	                case EQUAL:
	                
	                case DOUBLEEQUAL:
	                    
	                	Optional<Token> operatorToken = tokenManager.matchAndRemove(opType);
	                    
	                	if (operatorToken.isPresent()) {
	                    
	                		Node lValue = parseAssignment(); 

	                        OperationNode.OperationType operationType = null;

	                        switch (operatorToken.get().getType()) {
	                            
	                        case ARROWEQUAL:
	                        
	                        	operationType = OperationNode.OperationType.EXPONENT;
	                            
	                        	break;
	                            
	                        case PERCENTEQUAL:
	                        
	                        	operationType = OperationNode.OperationType.MODULO;
	                            
	                        	break;
	                            
	                        case TIMESEQUAL:
	                        
	                        	operationType = OperationNode.OperationType.MULTIPLY;
	                            
	                        	break;
	                            
	                        case DIVIDEEQUAL:
	                        
	                        	operationType = OperationNode.OperationType.DIVIDE;
	                            
	                        	break;
	                            
	                        case ADDEQUAL:
	                        
	                        	operationType = OperationNode.OperationType.ADD;
	                            
	                        	break;
	                            
	                        case SUBEQUAL:
	                        
	                        	operationType = OperationNode.OperationType.SUBTRACT;
	                            
	                        	break;
	                            
	                        case EQUAL:
	                        
	                        	operationType = OperationNode.OperationType.EQS;
	                            
	                        	break;
	                        	
	                        case DOUBLEEQUAL:
	                        	
	                        	operationType = OperationNode.OperationType.EQTWO;
	                        	
	                        	break;
	                        	
	                        default:
	                            throw new IllegalArgumentException("Unexpected operator: " + opType);
	                    
	                        	
	                        	
	                        }
	                        

	                        return new AssignmentNode(0, 0,new OperationNode(0, 0, rValue,operationType, lValue));
	                    }
 	            }

	            
	            break;
	        }

	        return rValue;
	        
	        
	        
	    }
	 
	 public BlockNode ParseBlock() {
		 
		    
		    BlockNode block = new BlockNode(0, 0, java.util.Optional.empty(), null);

		    if (tokenManager.matchAndRemove(Token.TokenType.LEFTCURLYBRA).isPresent()) {
		        // Multi-line block
		    	AcceptSeperators();
		        StatementNode statement;
		        
		        while (tokenManager.moreTokens()) 
		        {
		            statement = ParseStatement();
		           
		            
		            
		          
		           
		            block.addStatement(statement);
		            AcceptSeperators();
		            if(tokenManager.matchAndRemove(Token.TokenType.RIGHTCURLYBRA).isPresent())
		            break;
		        }
		        	
		    } else {
		        // Single-line block
		        StatementNode statement = ParseStatement();
		        
		        if (statement != null) {
		            block.addStatement(statement);
		        }
		    }


		    AcceptSeperators();

		    return block;
		}


	 public StatementNode ParseStatement() 
	 {
		 Optional<Token> token = tokenManager.peek(0);
		 
		 if(token.isPresent()) {
			 
			 if(tokenManager.matchAndRemove(Token.TokenType.CONTINUE).isPresent()) 
			 {
				 return ParseContinue();
			 }
			 
			 
			 if(tokenManager.matchAndRemove(Token.TokenType.BREAK).isPresent()) 
			 {
				 return ParseBreak();
			 }
			 
			 
			 if(tokenManager.matchAndRemove(Token.TokenType.RETURN).isPresent()) 
			 {
				 return ParseReturn();
			 }
			 
			 if(tokenManager.matchAndRemove(Token.TokenType.FOR).isPresent()) 
			 {
				 return ParseFor();
			 }
			 
			 
			 if(tokenManager.matchAndRemove(Token.TokenType.IF).isPresent()) 
			 {
				 
				 return ParseIf();
				 
			 }
			 
			 if(tokenManager.matchAndRemove(Token.TokenType.WHILE).isPresent()) 
			 {
			 
				 return ParseWhile();
			 }
		 
			 
			 if(tokenManager.matchAndRemove(Token.TokenType.DO).isPresent()) 
			 {
			 
				 return ParseDoWhile();
			 }
		 
		 
			 if(tokenManager.matchAndRemove(Token.TokenType.FOR).isPresent()) 
			 {
			 
				 return ParseFor();
			 }
			 
			 
			 if(tokenManager.matchAndRemove(Token.TokenType.DEL).isPresent())
			 {
			 
				 return ParseDelete();
			 }
			 
			
			 if(tokenManager.peek(1).get().getType() == Token.TokenType.LEFTPAR) 
			 {
			
				 return ParseFunctionCall();
			 
			 }
		
			 
		 
		 
		 else {
			 
			 Node other = ParseOperation();
			
			if(other instanceof AssignmentNode) {
				
				return  (StatementNode) other;
				
			}
			
			if(other instanceof FunctionCallNode) {
			
				return (StatementNode) other;
			}
			
		 }
		
		 
	 }
		return null;
	 }
	 
	

	 /**
	  * ParseFunctionCall peeks to see for a word and left par 
	  * to see if its not concatenation 
	  * @return null if its not a function call. 
	  */
	 
	 public StatementNode ParseFunctionCall() {

		    Token.TokenType currentType = tokenManager.peek(0).get().getType();

		    // Check if the next token is one of the special functions or a regular WORD
		    	boolean isSpecialFunction = 
		        currentType == Token.TokenType.PRINT ||
		        currentType == Token.TokenType.PRINTF ||
		        currentType == Token.TokenType.EXIT ||
		        currentType == Token.TokenType.NEXTFILE ||
		        currentType == Token.TokenType.NEXT ||
		        currentType == Token.TokenType.LENGTH ||
		        currentType == Token.TokenType.GETLINE;

		    if ((isSpecialFunction || currentType == Token.TokenType.WORD)
		            && tokenManager.peek(1).get().getType() == Token.TokenType.LEFTPAR) {

		        Optional<Token> functionNameToken = tokenManager.matchAndRemove(currentType);
		        tokenManager.matchAndRemove(Token.TokenType.LEFTPAR);

		        LinkedList<Node> parameters = new LinkedList<>();

		        while (true) {
		            Optional<Node> parameterOptional = Optional.ofNullable(ParseOperation());

		            if (parameterOptional.isPresent()) {
		                parameters.add(parameterOptional.get());

		                if (!tokenManager.matchAndRemove(Token.TokenType.COMMA).isPresent()) {
		                    break;
		                }
		            } else {
		                break;
		            }
		        }

		        if (tokenManager.matchAndRemove(Token.TokenType.RIGHTPAR).isPresent()) {
		            return new FunctionCallNode(functionNameToken, parameters, 0, 0);
		        } else {
		            throw new IllegalArgumentException("Missing closing parenthesis in function call");
		        }
		    }

		    return null; 
		}


	 
	 public StatementNode ParseContinue() 
	 
	 {

		return new ContinueNode(0,0);
		 
	 }

		public BreakNode ParseBreak() {
			
		return new BreakNode(0,0);
		 }
		
		
		public StatementNode ParseIf() {
		   
			int charPosition = tokenManager.peek(0).get().getCharPos();
		    
			int lineNumber = tokenManager.peek(0).get().getLineNumber();
		    
		   
		tokenManager.matchAndRemove(Token.TokenType.IF);
		    // Parse the condition inside parentheses
		   
			if(tokenManager.matchAndRemove(Token.TokenType.LEFTPAR).isPresent());
		  
		    

		    Node condition = ParseOperation();
		    
		   
		    tokenManager.matchAndRemove(Token.TokenType.RIGHTPAR);

		    // Parse the block of statements for the true condition
		    AcceptSeperators();
		    BlockNode trueBlock = ParseBlock();
		    
		    // Check for 'else' or 'else if'
		    IfNode nextIf = null;
		    BlockNode elseBlock = null;
		    if (tokenManager.matchAndRemove(Token.TokenType.ELSE).isPresent()) {
		        if (tokenManager.matchAndRemove(Token.TokenType.IF).isPresent()) {
		            nextIf = (IfNode) ParseIf();  // Recursive call to handle "else if"
		        } else {
		            elseBlock = ParseBlock();
		        }
		    }

		    return new IfNode(lineNumber, charPosition, condition, trueBlock, Optional.ofNullable(nextIf), Optional.ofNullable(elseBlock));
		

		
		}





		public StatementNode ParseFor() 
		
		{
		    // Match and remove the 'for' keyword
		    if(tokenManager.matchAndRemove(Token.TokenType.FOR).isEmpty()) {
		    	 
		    	//Match and remove the opening parenthesis
		    	tokenManager.matchAndRemove(Token.TokenType.LEFTPAR);
		    
		    	if (tokenManager.peek(1).get().getType() == Token.TokenType.IN) {
			        
			        
		

			        Node arrayExpression = ParseOperation();

			        // Match and remove the closing parenthesis
			        tokenManager.matchAndRemove(Token.TokenType.RIGHTPAR);
			        AcceptSeperators();
			        BlockNode block = ParseBlock();

			        return new ForEachNode(arrayExpression, block);

			    }
			    
		   
		    

		    // Check for traditional for loop pattern
		   
		        
		        Node initialization = ParseOperation();
		        
		        AcceptSeperators();
		        
		        Node terminationCondition = ParseOperation();
		       
		        AcceptSeperators();
		        
		        Node incrementDecrement = ParseOperation();
		        
		        AcceptSeperators();


		        // Match and remove the closing parenthesis
		        tokenManager.matchAndRemove(Token.TokenType.RIGHTPAR);
		        tokenManager.matchAndRemove(Token.TokenType.RIGHTCURLYBRA);

		        BlockNode block = ParseBlock();

		        return new ForNode(
		            initialization.getLineNumber(),
		            initialization.getCharPosition(),
		            initialization,
		            terminationCondition,
		            incrementDecrement,
		            block
		        );
		    }
		    else 

		    throw new IllegalArgumentException("Invalid for loop syntax");
		}

		public DeleteNode ParseDelete() {
			
		    if(tokenManager.matchAndRemove(Token.TokenType.DEL).isEmpty()){ // Check for DEL token

		    Optional<Node> lValueOptional = ParseLValue();

		    if (lValueOptional.isPresent()) {
		        Node lValue = lValueOptional.get();
		        AcceptSeperators(); 

		        return new DeleteNode(lValue);
		    }
		    }
		    

		    throw new IllegalArgumentException("Invalid syntax for DELETE statement");
		}

		public WhileNode ParseWhile() {
			
		    if(tokenManager.matchAndRemove(Token.TokenType.WHILE).isEmpty()) { 

		    tokenManager.matchAndRemove(Token.TokenType.LEFTPAR);

		    Node condition = ParseOperation(); 

		    tokenManager.matchAndRemove(Token.TokenType.RIGHTPAR); 
		    AcceptSeperators();
		    BlockNode block = ParseBlock(); // Parse the block of statements

		    return new WhileNode(condition.getLineNumber(), condition.getCharPosition(), condition, block);
		}
		   
		    throw new IllegalArgumentException("Invalid syntax for WHILE statement");
		}
	    
		public DoWhileNode ParseDoWhile() {
			
		    if(tokenManager.matchAndRemove(Token.TokenType.DO).isEmpty()) {; 

	        AcceptSeperators(); 

		    BlockNode block = ParseBlock(); 
		    
		    tokenManager.matchAndRemove(Token.TokenType.RIGHTCURLYBRA); 

		    tokenManager.matchAndRemove(Token.TokenType.WHILE); 

		    tokenManager.matchAndRemove(Token.TokenType.LEFTPAR); 
		    AcceptSeperators();
		    Node condition = ParseOperation(); 
		    AcceptSeperators();
		    tokenManager.matchAndRemove(Token.TokenType.RIGHTPAR); 

		    return new DoWhileNode(condition.getLineNumber(), condition.getCharPosition(),condition, block);
		    
		    }
		    
		    throw new IllegalArgumentException("Invalid syntax for Do-WHILE statement");

		    }
		    
		public ReturnNode ParseReturn() {
			

		
		    if(tokenManager.matchAndRemove(Token.TokenType.RETURN).isEmpty()) {  
		      Node expression = ParseOperation(); 

		    AcceptSeperators(); 

		    return new ReturnNode(expression.getCharPosition(), expression.getLineNumber(), expression);
		}
			return null;




	}
}












