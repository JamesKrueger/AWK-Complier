import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;


public class ParserTest {
	 
	LinkedList<Token> tokens = new LinkedList<>();

   @Test
   public void testParseAction() {
   	
	   ProgramNode programNode = new ProgramNode();

       tokens.add(new Token(Token.TokenType.BEGIN, "BEGIN", 0, 0));

       
       
       TokenManager tokenManager = new TokenManager(tokens);

       boolean result = new Parser(programNode, tokenManager).ParseAction(programNode);

       assertTrue(result);

      System.out.println(result);
      
       assertEquals(1, programNode.getBeginBlocks().size());
   }
   
   
   @Test
   public void testEndParseAction() {
   
	   ProgramNode programNode = new ProgramNode();

       tokens.add(new Token(Token.TokenType.END, "END", 0, 0));

       
       
       TokenManager tokenManager = new TokenManager(tokens);

       boolean result = new Parser(programNode, tokenManager).ParseAction(programNode);

       assertTrue(result);

      
      
       assertEquals(1, programNode.getEndBlocks().size());
   }
   


   @Test
	  public void testParseFunction() {
	     
	   ProgramNode programNode = new ProgramNode();

	      tokens.add(new Token(Token.TokenType.FUNCTION, "function", 0, 0));

	      tokens.add(new Token(Token.TokenType.WORD, "NAME", 0, 0));
	      
	      tokens.add(new Token(Token.TokenType.LEFTPAR, "(", 0, 0));
	      
	      tokens.add(new Token(Token.TokenType.WORD, "parameter1", 0, 0));
	      
	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));
	      
	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));
	      
	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));
	      
	      tokens.add(new Token(Token.TokenType.COMMA, ",", 0, 0));
	      
	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));
	      
	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));

	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));

	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));

	      tokens.add(new Token(Token.TokenType.WORD, "parameter2", 0, 0));

	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));

	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));
	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));

	      tokens.add(new Token(Token.TokenType.COMMA, ",", 0, 0));
	      
	      
	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));
	     
	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));
	      
	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));

	      tokens.add(new Token(Token.TokenType.WORD, "parameter3", 0, 0));
	      
	      tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));

	
	      tokens.add(new Token(Token.TokenType.RIGHTPAR, ")", 0, 0));
	      
	  

	      TokenManager tokenManager = new TokenManager(tokens);

	      boolean result = new Parser(programNode, tokenManager).ParseFunction(programNode);

	      assertTrue(result);

	      assertEquals(1, programNode.getFunctionNodes().size());

	      FunctionDefinitionNode functionNode = programNode.getFunctionNodes().get(0);
	      
	      assertEquals("NAME", functionNode.getFunctionName());

	      LinkedList<String> parameters = functionNode.getParameters();
	      
	      assertEquals(3, parameters.size());
	      
	      assertEquals("parameter1", parameters.get(0));
	      
	      assertEquals("parameter2", parameters.get(1));
	      
	      assertEquals("parameter3", parameters.get(2));

	  }

   @Test
   public void testAcceptSeparators() {
      
	   tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 1, 2));
       
       tokens.add(new Token(Token.TokenType.WORD, "variable", 1, 3)); 

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

      
       assertTrue(parser.AcceptSeperators()); 

      
       assertFalse(parser.AcceptSeperators());

      
       assertTrue(tokenManager.moreTokens());
   }


   @Test
   public void testParseLValue() {
       tokens.add(new Token(Token.TokenType.WORD, "TestingArrayVariables", 1, 1));
     
       tokens.add(new Token(Token.TokenType.LEFTBRA, "[", 1, 10));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "5", 1, 11));
       
       tokens.add(new Token(Token.TokenType.RIGHTBRA, "]", 1, 13));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       Optional<Node> result = parser.ParseLValue();

       assertTrue(result.isPresent());

       assertTrue(result.get() instanceof VariableReferenceNode);

       VariableReferenceNode varRefNode = (VariableReferenceNode) result.get();
       
       assertEquals("TestingArrayVariables", varRefNode.getName());

       assertTrue(varRefNode.getIndexExpression().isPresent());
       
       Node indexNode = varRefNode.getIndexExpression().get();
       
       assertTrue(indexNode instanceof ConstantNode);

       ConstantNode constantNode = (ConstantNode) indexNode;
       
       assertEquals("5", constantNode.getValue());
   }

   
   
   
   @Test
   public void testParseLValueVariable() {
      
	   tokens.add(new Token(Token.TokenType.WORD, "variable", 1, 0));

       TokenManager tokenManager = new TokenManager(tokens);

       Parser parser = new Parser(null, tokenManager);

       Optional<Node> result = parser.ParseLValue();
   
       assertTrue(result.isPresent());

       assertTrue(result.get() instanceof VariableReferenceNode);

       VariableReferenceNode variableNode = (VariableReferenceNode) result.get();
       
       assertEquals("variable", variableNode.getName());
   }
   @Test
   public void testParseBottomLevelParen() {
       
	   tokens.add(new Token(Token.TokenType.LEFTPAR, "(", 1, 0));
       
	   tokens.add(new Token(Token.TokenType.DOUBLEADD, "++", 1, 1));
       
	   tokens.add(new Token(Token.TokenType.WORD, "d", 1, 3));
       
       tokens.add(new Token(Token.TokenType.RIGHTPAR, ")", 1, 4));

       
       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       Node result = parser.ParseBottomLevel();

       assertNotNull(result);
       
       assertTrue(result instanceof OperationNode);

       OperationNode operationNode = (OperationNode) result;

       assertEquals(OperationNode.OperationType.PREINC, operationNode.getOperation());

       assertTrue(operationNode.getRight().isPresent());
       
       assertTrue(operationNode.getRight().get() instanceof VariableReferenceNode);

       VariableReferenceNode variableNode = (VariableReferenceNode) operationNode.getRight().get();
       
       assertEquals("d", variableNode.getName());

       assertTrue(variableNode.getIndexExpression().isEmpty());
   }
   @Test
   public void testParseLValueE() {

	   tokens.add(new Token(Token.TokenType.MONEYSIGN, "$", 1, 1));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "7", 1, 2));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       Optional<Node> result = parser.ParseLValue();

       assertTrue(result.isPresent());

       assertTrue(result.get() instanceof OperationNode);

       OperationNode operationNode = (OperationNode) result.get();

       assertEquals(OperationNode.OperationType.DOLLAR, operationNode.getOperation());

       assertTrue(operationNode.getRight().isPresent());
       
       assertTrue(operationNode.getRight().get() instanceof ConstantNode);

       ConstantNode constantNode = (ConstantNode) operationNode.getRight().get();

       assertEquals("7", constantNode.getValue());

       assertTrue(operationNode.getRight().isPresent());
   }
   
   
   
   @Test
   public void testParseBottomLevel() 
   
   {
       TokenManager tokenManager = new TokenManager(tokens);

       Parser parser = new Parser(null, tokenManager);

       tokens.add(new Token(Token.TokenType.STRINGLIT, "Hello", 1, 1));
       
       Node result = parser.ParseBottomLevel();
       
       assertNotNull(result);
       
       assertTrue(result instanceof ConstantNode);
       
       assertEquals("Hello", ((ConstantNode) result).getValue());

       
       tokens.clear();
       
       tokens.add(new Token(Token.TokenType.NUMBER, "42", 1, 1));
       
       result = parser.ParseBottomLevel();
       
       assertNotNull(result);
       
       assertTrue(result instanceof ConstantNode);
       
       assertEquals("42", ((ConstantNode) result).getValue());

       
       tokens.clear();
       
       tokens.add(new Token(Token.TokenType.PATTERN, "[a-z]--", 1, 1));
       
       result = parser.ParseBottomLevel();
       
       assertNotNull(result);
       
       assertTrue(result instanceof PatternNode);
       
       assertEquals("[a-z]--", ((PatternNode) result).getValue());

       
       
       tokens.clear();
       
       tokens.add(new Token(Token.TokenType.LEFTPAR, "(", 1, 1));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "42", 1, 2));
       
       tokens.add(new Token(Token.TokenType.RIGHTPAR, ")", 1, 4));
       
       result = parser.ParseBottomLevel();
       
       assertNotNull(result);
       
       assertTrue(result instanceof ConstantNode);
       
       assertEquals("42", ((ConstantNode) result).getValue());
       
       
      

       
}  
   
	 @Test
	 public void testParseBottomLevelUnaryOperators() {

		 tokens.add(new Token(Token.TokenType.SUB, "-", 1, 0));
	     
		 tokens.add(new Token(Token.TokenType.NUMBER, "5", 1, 1));

	        
	     TokenManager tokenManager = new TokenManager(tokens);
	     
	  
	    Parser parser = new Parser(null, tokenManager);
	    
	    Node result = parser.ParseBottomLevel();

	        
	    assertNotNull(result);

	        
	    assertTrue(result instanceof OperationNode);

	       
	    OperationNode operationNode = (OperationNode) result;
	    
	    assertEquals(OperationNode.OperationType.UNARYNEG, operationNode.getOperation());
	    }
	 @Test
	 public void testParsePostIncrement() {
	     
		 tokens.add(new Token(Token.TokenType.WORD, "a", 1, 0));
	     
		 tokens.add(new Token(Token.TokenType.DOUBLEADD, "++", 1, 1));

	     TokenManager tokenManager = new TokenManager(tokens);
	     
	     Parser parser = new Parser(null, tokenManager);

	     Node result = parser.ParsePostIncDec();

	     assertNotNull(result);
	     

	       assertTrue(result instanceof AssignmentNode);

	       AssignmentNode assignmentNode = (AssignmentNode) result;

	       assertTrue(assignmentNode.getExpression() instanceof OperationNode);
	       
	       OperationNode operationNode = (OperationNode) assignmentNode.getExpression();
	       
	       assertEquals(OperationNode.OperationType.POSTINC, operationNode.getOperation());

	       assertEquals("a", operationNode.getLeft().get().toString());
	       
	
	 }
	 @Test
	 public void testParsePostDecerment() {
	     
		 tokens.add(new Token(Token.TokenType.WORD, "hello", 1, 0));
	     
		 tokens.add(new Token(Token.TokenType.DOUBLESUB, "--", 1, 1));

	     TokenManager tokenManager = new TokenManager(tokens);
	     
	     Parser parser = new Parser(null, tokenManager);

	     Node result = parser.ParsePostIncDec();

	     assertNotNull(result);
	     

	       assertTrue(result instanceof AssignmentNode);

	       AssignmentNode assignmentNode = (AssignmentNode) result;

	       assertTrue(assignmentNode.getExpression() instanceof OperationNode);
	       
	       OperationNode operationNode = (OperationNode) assignmentNode.getExpression();
	       
	       assertEquals(OperationNode.OperationType.POSTDEC, operationNode.getOperation());

	       assertEquals("hello", operationNode.getLeft().get().toString());
	 }
	 @Test
	 public void testParseTerm() {
		 
	     tokens.add(new Token(Token.TokenType.NUMBER, "2", 1, 0));
	     
	     tokens.add(new Token(Token.TokenType.BACKSLASH, "/", 1, 1));
	     
	     tokens.add(new Token(Token.TokenType.NUMBER, "3", 1, 2));

	     TokenManager tokenManager = new TokenManager(tokens);
	     
	     Parser parser = new Parser(null, tokenManager);

	     Node result = parser.ParseTerm();

	     assertNotNull(result);
	     
	     assertTrue(result instanceof OperationNode);

	     OperationNode operationNode = (OperationNode) result;

	     assertEquals(OperationNode.OperationType.DIVIDE, operationNode.getOperation());

	     assertTrue(operationNode.getLeft().isPresent());
	     
	     assertTrue(operationNode.getRight().isPresent());

	     ConstantNode leftNode = (ConstantNode) operationNode.getLeft().get();

	     ConstantNode rightNode = (ConstantNode) operationNode.getRight().get();

	     assertEquals("2", leftNode.getValue());
	     
	     assertEquals("3", rightNode.getValue());
	 }

	 @Test
	 public void testParseExpression() {
	     
		 tokens.add(new Token(Token.TokenType.WORD, "Yo", 1, 0));
	     
		 tokens.add(new Token(Token.TokenType.ADD, "+", 1, 1));
	     
		 tokens.add(new Token(Token.TokenType.WORD, "great", 1, 2));

	     TokenManager tokenManager = new TokenManager(tokens);
	     
	     Parser parser = new Parser(null, tokenManager);

	     Node result = parser.ParseExpression();

	     assertNotNull(result);

	     assertTrue(result instanceof OperationNode);

	     OperationNode operationNode = (OperationNode) result;

	     assertEquals(OperationNode.OperationType.ADD, operationNode.getOperation());

	     assertTrue(operationNode.getLeft().isPresent());
	     
	     assertTrue(operationNode.getRight().isPresent());

	     VariableReferenceNode leftNode = (VariableReferenceNode) operationNode.getLeft().get();
	     
	     VariableReferenceNode rightNode = (VariableReferenceNode) operationNode.getRight().get();

	     assertEquals("Yo", leftNode.getName());
	     
	     assertEquals("great", rightNode.getName());
	 }


	 
	 @Test
	 public void testParseStringConcatenation() {
	     
		 tokens.add(new Token(Token.TokenType.WORD, "Yo", 1, 0));
	     
	     tokens.add(new Token(Token.TokenType.WORD, "There", 1, 2));

	     TokenManager tokenManager = new TokenManager(tokens);
	     
	     Parser parser = new Parser(null, tokenManager);

	     Node result = parser.ParseConcatenation();

	     assertNotNull(result);
	     
	     assertTrue(result instanceof OperationNode);

	     OperationNode operationNode = (OperationNode) result;

	     assertEquals(OperationNode.OperationType.CONCATENATION, operationNode.getOperation());

	     assertTrue(operationNode.getLeft().isPresent());
	     
	     assertTrue(operationNode.getRight().isPresent());
	      
	     assertTrue(operationNode.getLeft().get() instanceof VariableReferenceNode);

	    VariableReferenceNode leftNode = (VariableReferenceNode) operationNode.getLeft().get();
	       
	    assertTrue(operationNode.getRight().get() instanceof VariableReferenceNode);

	   VariableReferenceNode rightNode = (VariableReferenceNode) operationNode.getRight().get();
	       	  
	   assertEquals("Yo", leftNode.getName());
	     
	  assertEquals("There", rightNode.getName());

	
	 }

	 
   @Test
   public void testParseNotMatch() {

       tokens.add(new Token(Token.TokenType.NUMBER, "3", 1, 0));
       
       tokens.add(new Token(Token.TokenType.FACTSWINGLY, "!~", 1, 1));
       
       tokens.add(new Token(Token.TokenType.WORD, "hey", 1, 2));


       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       Node result = parser.ParseMatch();

       assertNotNull(result);
       
       assertTrue(result instanceof OperationNode);

       OperationNode operationNode = (OperationNode) result;
       
       assertEquals(OperationNode.OperationType.NOTMATCH, operationNode.getOperation());

       assertTrue(operationNode.getLeft().isPresent());
       
       assertTrue(operationNode.getLeft().get() instanceof ConstantNode);

       ConstantNode leftNode = (ConstantNode) operationNode.getLeft().get();
       
       assertEquals("3", leftNode.getValue());

       assertTrue(operationNode.getRight().isPresent());
       
       assertTrue(operationNode.getRight().get() instanceof VariableReferenceNode);

       VariableReferenceNode rightNode = (VariableReferenceNode) operationNode.getRight().get();
       
       assertEquals("hey", rightNode.getName());
   }

   @Test
   public void testParseMatch() {
	   
       tokens.add(new Token(Token.TokenType.WORD, "to", 1, 0));
       
       tokens.add(new Token(Token.TokenType.SWINGLY, "~", 1, 1));
       
       tokens.add(new Token(Token.TokenType.WORD, "yo", 1, 2));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       Node result = parser.ParseOperation();

       assertNotNull(result);
       
       assertTrue(result instanceof OperationNode);

       OperationNode operationNode = (OperationNode) result;
       
       assertEquals(OperationNode.OperationType.MATCH, operationNode.getOperation());

       assertTrue(operationNode.getLeft().isPresent());
       
       assertTrue(operationNode.getLeft().get() instanceof VariableReferenceNode);

       VariableReferenceNode leftNode = (VariableReferenceNode) operationNode.getLeft().get();
       
       assertEquals("to", leftNode.getName());

       assertTrue(operationNode.getRight().isPresent());
       
       assertTrue(operationNode.getRight().get() instanceof VariableReferenceNode);

       VariableReferenceNode rightNode = (VariableReferenceNode) operationNode.getRight().get();
       
       assertEquals("yo", rightNode.getName());
   }

   @Test
   public void testExpressionLess() {
	   
       tokens.add(new Token(Token.TokenType.WORD, "i", 1, 0));
       
       tokens.add(new Token(Token.TokenType.LEFTARROW, "<", 1, 1));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "3", 1, 2));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       Node result = parser.ParseComparison();

       assertNotNull(result);
       
       assertTrue(result instanceof OperationNode);

       OperationNode operationNode = (OperationNode) result;
       
       assertEquals(OperationNode.OperationType.LT, operationNode.getOperation());

       assertTrue(operationNode.getLeft().isPresent());
       
       assertTrue(operationNode.getLeft().get() instanceof VariableReferenceNode);

       VariableReferenceNode leftNode = (VariableReferenceNode) operationNode.getLeft().get();
       
       assertEquals("i", leftNode.getName());

       assertTrue(operationNode.getRight().isPresent());
       
       assertTrue(operationNode.getRight().get() instanceof ConstantNode);

       ConstantNode rightNode = (ConstantNode) operationNode.getRight().get();
       
       assertEquals("3", rightNode.getValue());
   }

   @Test
   public void testExpressionNotEqual() {
       tokens.add(new Token(Token.TokenType.NUMBER, "4", 1, 0));
      
       tokens.add(new Token(Token.TokenType.FACTEQUAL, "!=", 1, 1));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "5", 1, 3));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       Node result = parser.ParseComparison();

       assertNotNull(result);
       
       assertTrue(result instanceof OperationNode);

       OperationNode operationNode = (OperationNode) result;
       
       assertEquals(OperationNode.OperationType.NE, operationNode.getOperation());

       assertTrue(operationNode.getLeft().isPresent());
       
       assertTrue(operationNode.getLeft().get() instanceof ConstantNode);

       ConstantNode leftNode = (ConstantNode) operationNode.getLeft().get();
       
       assertEquals("4", leftNode.getValue());
       
       assertTrue(operationNode.getRight().isPresent());
       
       assertTrue(operationNode.getRight().get() instanceof ConstantNode);

       
       ConstantNode rightNode = (ConstantNode) operationNode.getRight().get();
       assertEquals("5", rightNode.getValue());
   }

   @Test
   public void testExpressionLessEqual() {
       tokens.add(new Token(Token.TokenType.NUMBER, "6", 1, 0));
       
       tokens.add(new Token(Token.TokenType.LEFTARROW, "==", 1, 1));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "7", 1, 3));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       Node result = parser.ParseComparison();

       assertNotNull(result);
       
       assertTrue(result instanceof OperationNode);
       
       OperationNode operationNode = (OperationNode) result;
       
       assertEquals(OperationNode.OperationType.LT, operationNode.getOperation());

       assertTrue(operationNode.getLeft().isPresent());
       
       assertTrue(operationNode.getLeft().get() instanceof ConstantNode);

       ConstantNode leftNode = (ConstantNode) operationNode.getLeft().get();
       
       assertEquals("6", leftNode.getValue());

       assertTrue(operationNode.getRight().isPresent());
       
       assertTrue(operationNode.getRight().get() instanceof ConstantNode);

       ConstantNode rightNode = (ConstantNode) operationNode.getRight().get();
       
       assertEquals("7", rightNode.getValue());
   }



   @Test
   public void testParseExponents() {
       
	   tokens.add(new Token(Token.TokenType.NUMBER, "2", 1, 1));
       
	   tokens.add(new Token(Token.TokenType.UPARROW, "^", 1, 2));
       
	   tokens.add(new Token(Token.TokenType.NUMBER, "3", 1, 3));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       Node result = parser.ParseExponents();

       assertNotNull(result);
       
       assertTrue(result instanceof OperationNode);

       OperationNode operationNode = (OperationNode) result;

       assertEquals(OperationNode.OperationType.EXPONENT, operationNode.getOperation());

       assertTrue(operationNode.getLeft().isPresent());
       
       assertTrue(operationNode.getRight().isPresent());

       ConstantNode rightNode = (ConstantNode) operationNode.getRight().get();
     
       ConstantNode leftNode = (ConstantNode) operationNode.getLeft().get();
       
       assertEquals("2", leftNode.getValue());  
       
       assertEquals("3", rightNode.getValue()); 
       System.out.println(result);
       
       }


   
   @Test
   public void testParseLogicalAnd() {
       
	   tokens.add(new Token(Token.TokenType.WORD, "Hey", 1, 0));
       
	   tokens.add(new Token(Token.TokenType.DOUBLEAND, "&&", 1, 1));
       
	   tokens.add(new Token(Token.TokenType.WORD, "Yo", 1, 3));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       Node result = parser.ParseLogicalAnd();

       assertNotNull(result);
       
       assertTrue(result instanceof OperationNode);

       OperationNode operationNode = (OperationNode) result;
       
       assertEquals(OperationNode.OperationType.AND, operationNode.getOperation());

       assertTrue(operationNode.getLeft().isPresent());
       
       assertTrue(operationNode.getLeft().get() instanceof VariableReferenceNode);

       VariableReferenceNode leftNode = (VariableReferenceNode) operationNode.getLeft().get();
       
       assertEquals("Hey", leftNode.getName());

       assertTrue(operationNode.getRight().isPresent());
       
       assertTrue(operationNode.getRight().get() instanceof VariableReferenceNode);

       VariableReferenceNode rightNode = (VariableReferenceNode) operationNode.getRight().get();
       
       assertEquals("Yo", rightNode.getName());
   }


   @Test
   public void testParseLogicalOr() {
       
	   tokens.add(new Token(Token.TokenType.WORD, "Yoo", 1, 0));
       
	   tokens.add(new Token(Token.TokenType.DOUBLEOR, "||", 1, 1));
       
	   tokens.add(new Token(Token.TokenType.WORD, "Boom", 1, 3));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       Node result = parser.ParseLogicalOr();
       
       assertNotNull(result);
       
       assertTrue(result instanceof OperationNode);

       OperationNode operationNode = (OperationNode) result;
       
       assertEquals(OperationNode.OperationType.OR, operationNode.getOperation());

       assertTrue(operationNode.getLeft().isPresent());
       
       assertTrue(operationNode.getLeft().get() instanceof VariableReferenceNode);

       VariableReferenceNode leftNode = (VariableReferenceNode) operationNode.getLeft().get();
       
       assertEquals("Yoo", leftNode.getName());

       assertTrue(operationNode.getRight().isPresent());
       
       assertTrue(operationNode.getRight().get() instanceof VariableReferenceNode);

       VariableReferenceNode rightNode = (VariableReferenceNode) operationNode.getRight().get();
       
       assertEquals("Boom", rightNode.getName());
   }
   @Test
   public void testParseTenary() {
     
       tokens.add(new Token(Token.TokenType.WORD, "Yurr", 1, 0));
     
       tokens.add(new Token(Token.TokenType.QUESTIONMARK, "?", 1, 1));
       
       tokens.add(new Token(Token.TokenType.WORD, "condition", 1, 2));
       
       tokens.add(new Token(Token.TokenType.COLON, ":", 1, 3));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "3", 1, 4));

       TokenManager tokenManager = new TokenManager(tokens);
       Parser parser = new Parser(null, tokenManager);

       Node result = parser.ParseTenary();

       assertNotNull(result);
       
       assertTrue(result instanceof TernaryNode);

       TernaryNode ternaryNode = (TernaryNode) result;

       
       assertNotNull(ternaryNode.getCondition());
       
       assertNotNull(ternaryNode.getFalseBranch());
       
       assertNotNull(ternaryNode.getTrueBranch());

       VariableReferenceNode conditionNode = (VariableReferenceNode) ternaryNode.getCondition();
       
       VariableReferenceNode trueBranchNode = (VariableReferenceNode) ternaryNode.getFalseBranch();
       
       ConstantNode falseBranchNode = (ConstantNode) ternaryNode.getTrueBranch();

       assertEquals("Yurr", conditionNode.getName());
       
       assertEquals("condition", trueBranchNode.getName());
       
       assertEquals("3", falseBranchNode.getValue());
       
       System.out.print(result);
   }

   @Test
   public void testParseAssignment() {
	  
	   tokens.add(new Token(Token.TokenType.LEFTPAR, "a", 1, 0));
	  
	   tokens.add(new Token(Token.TokenType.WORD,  "a", 1, 0));
       
	   tokens.add(new Token(Token.TokenType.ARROWEQUAL, "=", 1, 1));
       
	   tokens.add(new Token(Token.TokenType.NUMBER, "2", 1, 3));
       
	   tokens.add(new Token(Token.TokenType.RIGHTPAR, "a", 1, 0));

	   TokenManager tokenManager = new TokenManager(tokens);

       Parser parser = new Parser(null,tokenManager);

       Node result = parser.parseAssignment();

       assertTrue(result instanceof AssignmentNode);

       AssignmentNode assignmentNode = (AssignmentNode) result;

       assertTrue(assignmentNode.getExpression() instanceof OperationNode);
       
       OperationNode operationNode = (OperationNode) assignmentNode.getExpression();
       
       assertEquals(OperationNode.OperationType.EXPONENT, operationNode.getOperation());

       assertEquals("a", operationNode.getLeft().get().toString());
       
       assertEquals("2", operationNode.getRight().get().toString());
       
       System.out.println(result);
     
   }




   @Test
   public void testParseArrayMembership() {

	   tokens.add(new Token(Token.TokenType.NUMBER, "42", 0, 0));
       
	   tokens.add(new Token(Token.TokenType.IN, "in", 0, 0));
       
	   tokens.add(new Token(Token.TokenType.WORD, "arrayName", 0, 0));

       TokenManager tokenManager = new TokenManager(tokens);

       Parser parser = new Parser(null, tokenManager);

   
       Node result = parser.ParseArrayMembership();

    
       assertTrue(result instanceof OperationNode);

       OperationNode operationNode = (OperationNode) result;

       assertEquals(OperationNode.OperationType.IN, operationNode.getOperation());


       assertTrue(operationNode.getLeft().get() instanceof ConstantNode);

       assertTrue(operationNode.getRight().get() instanceof VariableReferenceNode);

       ConstantNode constantNode = (ConstantNode) operationNode.getLeft().get();
       
       VariableReferenceNode variableNode = (VariableReferenceNode) operationNode.getRight().get();

       assertEquals("42", constantNode.getValue());
       
       assertEquals("arrayName", variableNode.getName());
   }
   
   @Test
   public void testParseContinue() {

	   tokens.add(new Token(Token.TokenType.CONTINUE, "continue", 0, 0));

       TokenManager tokenManager = new TokenManager(tokens);

       Parser parser = new Parser(null, tokenManager);

       StatementNode result = parser.ParseStatement();

       assertTrue(result instanceof StatementNode);
   }

   @Test
   public void testParseBreak() {
       tokens.add(new Token(Token.TokenType.BREAK, "break", 0, 0));

       TokenManager tokenManager = new TokenManager(tokens);

       Parser parser = new Parser(null, tokenManager);

       BreakNode result = (BreakNode) parser.ParseBreak();

       assertTrue(result instanceof BreakNode);
   }
   
  
   

   @Test
   public void testParseFor() {
       
	   tokens.add(new Token(Token.TokenType.FOR, "for", 1, 0));
       
	   tokens.add(new Token(Token.TokenType.LEFTPAR, "(", 1, 4));
       
	   tokens.add(new Token(Token.TokenType.WORD, "i", 1, 5));
       
	   tokens.add(new Token(Token.TokenType.EQUAL, "=", 1, 7));
       
	   tokens.add(new Token(Token.TokenType.NUMBER, "0", 1, 9));
       
	   tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 1, 10));
       
	   tokens.add(new Token(Token.TokenType.WORD, "i", 1, 12));
       
	   tokens.add(new Token(Token.TokenType.LEFTARROW, "<", 1, 14));
       
	   tokens.add(new Token(Token.TokenType.NUMBER, "5", 1, 16));
       
	   tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 1, 17));
       
	   tokens.add(new Token(Token.TokenType.WORD, "i", 1, 19));
       
	   tokens.add(new Token(Token.TokenType.DOUBLEADD, "++", 1, 19));
       
	   tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 1, 23));
       
	   tokens.add(new Token(Token.TokenType.RIGHTPAR, ")", 1, 24));
	   
	   tokens.add(new Token(Token.TokenType.LEFTCURLYBRA, "{", 1, 24));

	   tokens.add(new Token(Token.TokenType.WORD, "i", 1, 12));
       
	   tokens.add(new Token(Token.TokenType.EQUAL, "=", 1, 14));
       
	   tokens.add(new Token(Token.TokenType.NUMBER, "5", 1, 16));
	   
	   tokens.add(new Token(Token.TokenType.RIGHTCURLYBRA, "}", 1, 24));

       
       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       ForNode result = (ForNode) parser.ParseStatement();
       
       assertNotNull(result);

       Node initialization = result.getInitialization();
       
       Node terminationCondition = result.getCondition();
       
       Node incrementDecrement = result.getincDec();

       
       BlockNode block = result.getBlock();
       
       assertNotNull(initialization);
       
       assertNotNull(terminationCondition);
       
       assertNotNull(incrementDecrement);
       
       assertNotNull(block);
       
      // System.out.println(initialization);
      // System.out.println(terminationCondition);

       
       assertTrue(initialization instanceof AssignmentNode);
       
       AssignmentNode assignmentNode = (AssignmentNode) initialization;
       
       assertTrue(assignmentNode.getExpression() instanceof OperationNode);

       OperationNode initializationOperation = (OperationNode) assignmentNode.getExpression();
       
       assertEquals("i", ((VariableReferenceNode) initializationOperation.getLeft().get()).getName());
       
       assertEquals("0", ((ConstantNode) initializationOperation.getRight().get()).getValue());

       assertTrue(terminationCondition instanceof OperationNode);

       OperationNode terminationOperation = (OperationNode) terminationCondition;
       
       assertEquals(OperationNode.OperationType.LT, terminationOperation.getOperation());

      
       assertTrue(incrementDecrement instanceof AssignmentNode);

       AssignmentNode incrementDecrementOperation = (AssignmentNode) incrementDecrement;

       assertTrue(assignmentNode.getExpression() instanceof OperationNode);
       
       OperationNode operationNode = (OperationNode) incrementDecrementOperation.getExpression();
       
       assertEquals(OperationNode.OperationType.POSTINC, operationNode.getOperation());

       assertEquals("i", operationNode.getLeft().get().toString());
       
       assertTrue(block instanceof BlockNode);
       
       //System.out.println(block.getStatements());

       
   }


   @Test
   public void testParseReturnExp() {
       
       tokens.add(new Token(Token.TokenType.RETURN, "return", 0, 0));
       
       tokens.add(new Token(Token.TokenType.WORD, "blah", 0, 0));
      
       tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);
       
       ReturnNode returnNode = (ReturnNode) parser.ParseStatement();

       
       assertNotNull(returnNode);
       
       assertTrue(returnNode.getExpression() instanceof VariableReferenceNode);
       
       VariableReferenceNode constNode = (VariableReferenceNode) returnNode.getExpression();
       
       assertEquals("blah", constNode.getName());

       
   }
   
   @Test
   public void testParseIf() {
       
	   tokens.add(new Token(Token.TokenType.IF, "if", 1, 0));
       
       tokens.add(new Token(Token.TokenType.LEFTPAR, "(", 1, 3));
       
       tokens.add(new Token(Token.TokenType.WORD, "x", 1, 4));
       
       tokens.add(new Token(Token.TokenType.EQUAL, "=", 1, 6));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "0", 1, 8));
       
       tokens.add(new Token(Token.TokenType.RIGHTPAR, ")", 1, 9));
       
       tokens.add(new Token(Token.TokenType.WORD, "b", 1, 4));
       
       tokens.add(new Token(Token.TokenType.EQUAL, "=", 1, 6));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "4", 1, 4));



       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       IfNode result = (IfNode) parser.ParseStatement();
       
       assertNotNull(result);
    
       assertTrue(result.getCondition() instanceof AssignmentNode);
     
       //System.out.println(result.getCondition().toString());
       
       assertTrue(result.getTrueBlock() instanceof BlockNode);
       
       //System.out.println(result.getTrueBlock().toString());
       
  

       
       
   }
   @Test
   public void testParseDelete() {
	   
	   tokens.add(new Token(Token.TokenType.DEL, "del", 1, 0));
       
	   tokens.add(new Token(Token.TokenType.WORD, "myArray", 1, 4));
       
	   tokens.add(new Token(Token.TokenType.LEFTBRA, "[", 1, 11));
       
	   tokens.add(new Token(Token.TokenType.WORD, "index", 1, 12));
       
	   tokens.add(new Token(Token.TokenType.RIGHTBRA, "]", 1, 17));
       
       
	   TokenManager tokenManager = new TokenManager(tokens);
       
	   Parser parser = new Parser(null, tokenManager);

       DeleteNode result = (DeleteNode) parser.ParseStatement();
       
       assertNotNull(result);
       
       //System.out.print(result);
       
       
       assertTrue(result.getTarget() instanceof VariableReferenceNode);
      
       
   }

   
   @Test
   public void testParseWhile() {
       
       tokens.add(new Token(Token.TokenType.WHILE, "while", 1, 0));
      
       tokens.add(new Token(Token.TokenType.LEFTPAR, "(", 1, 6));
       
       tokens.add(new Token(Token.TokenType.WORD, "i", 1, 7));
       
       tokens.add(new Token(Token.TokenType.LEFTARROW, "<", 1, 9));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "10", 1, 11));
       
       tokens.add(new Token(Token.TokenType.RIGHTPAR, ")", 1, 13));
       
       tokens.add(new Token(Token.TokenType.LEFTCURLYBRA, "{", 1, 15));
       
       tokens.add(new Token(Token.TokenType.WORD, "x", 1, 17));
       
       tokens.add(new Token(Token.TokenType.EQUAL, "=", 1, 19));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "42", 1, 21));
  
       tokens.add(new Token(Token.TokenType.RIGHTCURLYBRA, "}", 1, 25));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       WhileNode result = (WhileNode) parser.ParseStatement();
       
       assertNotNull(result);
       
       System.out.println(result);
       
       
   }

   @Test
   public void testParseDoWhile() {
	   
	   tokens.add(new Token(Token.TokenType.DO, "do", 1, 0));
      
       tokens.add(new Token(Token.TokenType.LEFTCURLYBRA, "{", 1, 3));
       
       tokens.add(new Token(Token.TokenType.WORD, "x", 1, 5));
       
       tokens.add(new Token(Token.TokenType.DOUBLEADD, "=", 1, 7));
    
       tokens.add(new Token(Token.TokenType.RIGHTCURLYBRA, "}", 1, 13));
       
       tokens.add(new Token(Token.TokenType.WHILE, "while", 1, 15));
       
       tokens.add(new Token(Token.TokenType.LEFTPAR, "(", 1, 21));
       
       tokens.add(new Token(Token.TokenType.WORD, "x", 1, 22));
       
       tokens.add(new Token(Token.TokenType.RIGHTARROW, ">", 1, 24));
       
       tokens.add(new Token(Token.TokenType.NUMBER, "0", 1, 26));
       
       tokens.add(new Token(Token.TokenType.RIGHTPAR, ")", 1, 27));
       
       tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 1, 28));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       DoWhileNode result = (DoWhileNode) parser.ParseStatement();
       
       assertNotNull(result);

      
       System.out.println(result);
   }

   @Test
   public void testParseFunctionCall() {
              
	   tokens.add(new Token(Token.TokenType.WORD, "doSomeFunction", 1, 0));
       
	   tokens.add(new Token(Token.TokenType.LEFTPAR, "(", 1, 13));
       
	   tokens.add(new Token(Token.TokenType.WORD, "Parameter1", 1, 14));
       
	   tokens.add(new Token(Token.TokenType.COMMA, ",", 1, 15));
       
	   tokens.add(new Token(Token.TokenType.WORD, "Parameter2", 1, 14));
       
	   tokens.add(new Token(Token.TokenType.COMMA, ",", 1, 18));
       
	   tokens.add(new Token(Token.TokenType.WORD, "x", 1, 20));
       
	   tokens.add(new Token(Token.TokenType.RIGHTPAR, ")", 1, 21));
       
	   
       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);
       
       StatementNode functionCallOptional = parser.ParseStatement();
       
       System.out.println(functionCallOptional);
       
    
       
       assertTrue(functionCallOptional instanceof FunctionCallNode);
       
       FunctionCallNode functionCallNode = (FunctionCallNode) functionCallOptional;

       assertEquals("doSomeFunction", functionCallNode.getFunctionName().get().getValue());

       LinkedList<Node> parameters = functionCallNode.getParamaters();
      
       assertEquals(3, parameters.size());
       
       assertTrue(parameters.get(0) instanceof VariableReferenceNode);
       
       assertTrue(parameters.get(1) instanceof VariableReferenceNode);
       
       assertTrue(parameters.get(2) instanceof VariableReferenceNode);

      
   }
   @Test
   public void testParseProgramFunction() {
       tokens.add(new Token(Token.TokenType.WORD, "getline", 1, 0));
       tokens.add(new Token(Token.TokenType.LEFTPAR, "getline", 1, 0));
       tokens.add(new Token(Token.TokenType.RIGHTPAR, "getline", 1, 0));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       StatementNode result = parser.ParseStatement();

       System.out.println(result);
       
       assertNotNull(result);
       
       assertTrue(result instanceof FunctionCallNode);

       FunctionCallNode functionCallNode = (FunctionCallNode) result;
       
       Optional<Token> functionName = functionCallNode.getFunctionName();

       assertTrue(functionName.isPresent());
       
       assertEquals("getline", functionName.get().getValue());

       LinkedList<Node> parameters = functionCallNode.getParamaters();
       
       assertEquals(0, parameters.size());
   }
   
   @Test
   public void testParseProgramFunctionWithParams() {
   
	   tokens.add(new Token(Token.TokenType.GETLINE, "getline", 1, 0));
       
	   tokens.add(new Token(Token.TokenType.LEFTPAR, "(", 1, 5));
       
	   tokens.add(new Token(Token.TokenType.NUMBER, "11", 1, 6));  // Use STRINGLIT for the format string
       
	   tokens.add(new Token(Token.TokenType.COMMA, ",", 1, 10));
       
	   tokens.add(new Token(Token.TokenType.NUMBER, "12", 1, 12));
       
	   tokens.add(new Token(Token.TokenType.RIGHTPAR, ")", 1, 18));
       
	   tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 1, 19));

       TokenManager tokenManager = new TokenManager(tokens);
       
       Parser parser = new Parser(null, tokenManager);

       StatementNode result = parser.ParseStatement();
       
       assertNotNull(result);
       
       System.out.println(result);
       
       assertTrue(result instanceof FunctionCallNode);
       
       FunctionCallNode functionCallNode = (FunctionCallNode) result;
       
       assertEquals(2, functionCallNode.getParamaters().size());

       LinkedList<Node> parameters = functionCallNode.getParamaters();

       
       assertTrue(parameters.get(0) instanceof ConstantNode);
       
       ConstantNode formatString = (ConstantNode) parameters.get(0);
       
       assertEquals("11", formatString.getValue());  

       
       assertTrue(parameters.get(1) instanceof ConstantNode);
       
       ConstantNode variableReference = (ConstantNode) parameters.get(1);
       
       assertEquals("12", variableReference.getValue());
   }



}

   

