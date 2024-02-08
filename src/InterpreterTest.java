import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Optional;

public class InterpreterTest {

	 @Test
	    public void testAssignmentNode() throws Exception {
	        
		 ProgramNode programNode = new ProgramNode();
	    	
			Interperter interpreter = new Interperter(programNode, null);


	        
	        AssignmentNode assignmentNode = new AssignmentNode(
	            1, 1,  
	            new OperationNode(
	                1, 1,  
	                new VariableReferenceNode(0, 0, "a", java.util.Optional.empty()),
	                OperationNode.OperationType.EQS,
	                new ConstantNode(0, 0, "2")
	            )
	        );

	     
	        HashMap<String, InterpreterDataType> locals = new HashMap<>();

	        
	        InterpreterDataType result = interpreter.GetIDT(assignmentNode, locals);
	        
	        assertEquals("2", result.getValue());  
	        	        
	        assertEquals("2", interpreter.globalVariables.get("a").getValue());
	    }
	 

	    @Test
	    public void testConstantNode() throws Exception {
	        
	   	 ProgramNode programNode = new ProgramNode();
	    	
			Interperter interpreter = new Interperter(programNode, null);
	        
	        ConstantNode constantNode = new ConstantNode(1, 1, "5");

	        
	        InterpreterDataType result = interpreter.GetIDT(constantNode, null);

	        
	        assertNotNull(result);

	        
	        assertEquals("5", result.getValue());
	        
	        
	    }
	    
	    
	    @Test
	    public void testArrayVariableWithIndex() throws Exception {
	        
	    	ProgramNode programNode = new ProgramNode();
	  		
	    	Interperter interpreter = new Interperter(programNode, null);
	        
	    	String arrayName = "myArray";
	        
	    	InterpreterArrayDataType array = new InterpreterArrayDataType();
	        
	    	array.addElement("0", new InterpreterDataType("element0"));
	        
	    	array.addElement("5", new InterpreterDataType("element1"));
	        
	    	interpreter.globalVariables.put(arrayName, array);

	        ConstantNode indexExpression = new ConstantNode(0, 0, "5");
	        
	        VariableReferenceNode varRefNode = new VariableReferenceNode(0, 0, arrayName, Optional.of(indexExpression));
	        
	        InterpreterDataType result = interpreter.GetIDT(varRefNode, interpreter.globalVariables);

	        assertEquals("element1", result.getValue());
	    }
	    @Test
	    public void testVariableReference() throws Exception {
	        ProgramNode programNode = new ProgramNode();
			Interperter interpreter = new Interperter(programNode, null);
	        
	        
	        VariableReferenceNode varRefNode = new VariableReferenceNode(0, 0, "myVariable", null);
	        
	        InterpreterDataType result = interpreter.GetIDT(varRefNode, new HashMap<>());
	        
	        assertNotNull("The result should not be null", result);
	        assertEquals("myVariable", result.getValue());
	    }
	    
	    @Test
	    public void testAdditionOperation() throws Exception {
	        
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);

	        
	        HashMap<String, InterpreterDataType> locals = new HashMap<>();

	        
	        ConstantNode leftOperand = new ConstantNode(0,0,"3");
	        ConstantNode rightOperand = new ConstantNode(0,0,"4");

	        
	        OperationNode operationNode = new OperationNode(0,0, leftOperand, OperationNode.OperationType.ADD,  rightOperand);

	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, locals);

	        
	        assertNotNull(result);

	        
	        assertTrue(result instanceof InterpreterDataType);

	        
	        assertEquals("7.0", result.getValue()); 

	        
	    }
	    @Test
	    public void testMultiplyOperation() throws Exception {
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);
				HashMap<String, InterpreterDataType> locals = new HashMap<>();

	        ConstantNode leftOperand = new ConstantNode(0, 0, "2.5");
	        ConstantNode rightOperand = new ConstantNode(0, 0, "4.0");

	        OperationNode operationNode = new OperationNode(0, 0, leftOperand, OperationNode.OperationType.MULTIPLY, rightOperand);

	        InterpreterDataType result = interpreter.GetIDT(operationNode, locals);

	        assertNotNull(result);
	        assertTrue(result instanceof InterpreterDataType);
	        assertEquals("10.0", result.getValue());
	    }

	    @Test
	    public void testDivideOperation() throws Exception {
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);
				HashMap<String, InterpreterDataType> locals = new HashMap<>();

	        ConstantNode leftOperand = new ConstantNode(0, 0, "10.0");
	        ConstantNode rightOperand = new ConstantNode(0, 0, "2.0");

	        OperationNode operationNode = new OperationNode(0, 0, leftOperand, OperationNode.OperationType.DIVIDE, rightOperand);

	        InterpreterDataType result = interpreter.GetIDT(operationNode, locals);

	        assertNotNull(result);
	        assertTrue(result instanceof InterpreterDataType);
	        assertEquals("5.0", result.getValue());
	    }

	    @Test
	    public void testExponentOperation() throws Exception {
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);
				HashMap<String, InterpreterDataType> locals = new HashMap<>();

	        ConstantNode leftOperand = new ConstantNode(0, 0, "2.0");
	        ConstantNode rightOperand = new ConstantNode(0, 0, "3.0");

	        OperationNode operationNode = new OperationNode(0, 0, leftOperand, OperationNode.OperationType.EXPONENT, rightOperand);

	        InterpreterDataType result = interpreter.GetIDT(operationNode, locals);

	        assertNotNull(result);
	        assertTrue(result instanceof InterpreterDataType);
	        assertEquals("8.0", result.getValue());
	    }

	    @Test
	    public void testModuloOperation() throws Exception {
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);
				HashMap<String, InterpreterDataType> locals = new HashMap<>();

	        ConstantNode leftOperand = new ConstantNode(0, 0, "10");
	        ConstantNode rightOperand = new ConstantNode(0, 0, "3");

	        OperationNode operationNode = new OperationNode(0, 0, leftOperand, OperationNode.OperationType.MODULO, rightOperand);

	        InterpreterDataType result = interpreter.GetIDT(operationNode, locals);

	        assertNotNull(result);
	        assertTrue(result instanceof InterpreterDataType);
	        assertEquals("1", result.getValue());
	    }
	    @Test
	    public void testEqualOperation() throws Exception {
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);
	        
	        
				  ConstantNode leftOperand = new ConstantNode(0,0,"3");
			        ConstantNode rightOperand = new ConstantNode(0,0,"3");
	        
	        
	        OperationNode operationNode = new OperationNode(
	        		0, 0, leftOperand,
	        		OperationNode.OperationType.EQS,
	            
	            rightOperand
	        );

	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, new HashMap<>());

	        
	        assertEquals("1", result.getValue());
	    }
	    @Test
	    public void testNotEqualOperation() throws Exception {
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);
	        
	        
				  ConstantNode leftOperand = new ConstantNode(0,0,"4");
			        ConstantNode rightOperand = new ConstantNode(0,0,"3");
	        
	        
	        OperationNode operationNode = new OperationNode(
	        		0, 0, leftOperand,
	        		OperationNode.OperationType.NE,
	            
	            rightOperand
	        );

	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, null);

	      
	        assertEquals("1", result.getValue());
	    }
	    
	    @Test
	    public void testGreaterThanOperation() throws Exception {
	    	 ProgramNode programNode = new ProgramNode();
		    	
					Interperter interpreter = new Interperter(programNode, null);
		        
		        
					  ConstantNode leftOperand = new ConstantNode(0,0,"yoo");
				        ConstantNode rightOperand = new ConstantNode(0,0,"yo");
	        
	        
	        OperationNode operationNode = new OperationNode(
	        		  0, 0, leftOperand,
	            OperationNode.OperationType.GT,
	          
	            rightOperand
	        );

	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, new HashMap<>());

	        
	        assertEquals("1", result.getValue());
	    }
	    @Test
	    public void testLessThanOperation() throws Exception {
	    	 ProgramNode programNode = new ProgramNode();
		    	
					Interperter interpreter = new Interperter(programNode, null);
		        
		        
					  ConstantNode leftOperand = new ConstantNode(0,0,"yoo");
				        ConstantNode rightOperand = new ConstantNode(0,0,"yo");
	        
	        
	        OperationNode operationNode = 
	        		new OperationNode(
	        		  0, 0, leftOperand,
	            OperationNode.OperationType.LT,
	            rightOperand
	        );

	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, new HashMap<>());

	        
	        assertEquals("1", result.getValue());
	    }
	    @Test
	    public void testAndOperation() throws Exception {
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);
	        
				
				  ConstantNode leftOperand = new ConstantNode(0,0,"1");
			        ConstantNode rightOperand = new ConstantNode(0,0,"3");
      
			        OperationNode operationNode = new OperationNode(
			        		0, 0, leftOperand,
			        		OperationNode.OperationType.AND,
			        		rightOperand
      );
	        
	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, new HashMap<>());
	        
	        
	        assertEquals("1", result.getValue());
	    }
	    @Test
	    public void testOrOperation() throws Exception {
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);
	        
				
				  ConstantNode leftOperand = new ConstantNode(0,0,"2");
			        ConstantNode rightOperand = new ConstantNode(0,0,"1");
      
      OperationNode operationNode = new OperationNode(
      		  0, 0, leftOperand,
          OperationNode.OperationType.OR,
          rightOperand
      );
	        
	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, new HashMap<>());
	        
	    
	        assertEquals("1", result.getValue());
	    }

	    @Test
	    public void testDollarOperation() throws Exception {
	        
	    	ProgramNode programNode = new ProgramNode();
	    	
			Interperter interpreter = new Interperter(programNode, null);

	        
	        HashMap<String, InterpreterDataType> locals = new HashMap<>();
	        locals.put("x", new InterpreterDataType("42"));

	        
	        VariableReferenceNode variableNode = new VariableReferenceNode(0, 0, "x", java.util.Optional.empty());

	        
	        OperationNode dollarOperation = new OperationNode(
	            0, 0,
	            variableNode,
	            OperationNode.OperationType.DOLLAR,
	            variableNode
	        );

	        try {
	            
	            InterpreterDataType result = interpreter.GetIDT(dollarOperation, locals);

	            
	            assertEquals("x", result.getValue());

	        } catch (Exception e) {
	            
	            fail("Exception should not be thrown: " + e.getMessage());
	        }
	    }
	    @Test
	    public void testNotOperation() throws Exception {
	    	
	    	ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);
	        
				
				
			        ConstantNode rightOperand = new ConstantNode(0,0,"0");
      
      OperationNode operationNode = new OperationNode(
      		  0, 0, null,
          OperationNode.OperationType.NOT,
          rightOperand
      );
	        
	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, new HashMap<>());
	        
	        
	        assertEquals("0", result.getValue());
	    }
	    @Test
	    public void testMatchOperation() throws Exception {
	        
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);

	        
	        Node leftOperand = new ConstantNode(0, 0, "Hello, World!");

	        
	        PatternNode patternNode = new PatternNode(0, 0, "Hello");


	        
	        OperationNode operationNode = new OperationNode(
	            0, 0, leftOperand,
	            OperationNode.OperationType.MATCH,
	            patternNode
	        );

	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, null);

	        
	        assertEquals("1", result.getValue());
	    }

	    @Test
	    public void testNotMatchOperation() throws Exception {
	        
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);

	        
	        Node leftOperand = new ConstantNode(0, 0, "Hello, World!");

	        
	        PatternNode patternNode = new PatternNode(0, 0, "Goodbye");

	        
	        OperationNode operationNode = new OperationNode(
	            0, 0, leftOperand,
	            OperationNode.OperationType.NOTMATCH,
	            patternNode
	        );

	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, null);

	        
	        assertEquals("1", result.getValue());
	    }
	    @Test
	    public void testTernaryNode() throws Exception {
	        
	    	 ProgramNode programNode = new ProgramNode();
		    	
				Interperter interpreter = new Interperter(programNode, null);

	        
	        HashMap<String, InterpreterDataType> locals = new HashMap<>();

	        
	  	  ConstantNode leftOperand = new ConstantNode(0,0,"4");
	        
	  	  ConstantNode rightOperand = new ConstantNode(0,0,"3");	
	      
	  	  OperationNode operationNode = new OperationNode(
	        		  0, 0, leftOperand,
	            OperationNode.OperationType.LT,
	            rightOperand
	        );
	  	        
	  	       
	        ConstantNode trueBranch = new ConstantNode(0, 0, "Yesc");
	        
	        ConstantNode falseBranch = new ConstantNode(0, 0, "No");

	        
	        TernaryNode ternaryNode = new TernaryNode(0, 0, operationNode, trueBranch, falseBranch);

	        try {
	            
	            InterpreterDataType result = interpreter.GetIDT(ternaryNode, locals);

	            
	            assertEquals("No", result.getValue());

	        } catch (Exception e) {
	            
	            fail("Exception should not be thrown: " + e.getMessage());
	        }
	    }
	    
	    @Test
	    public void testPreIncrementOperationWithRightOperand() throws Exception {
	        
	    	ProgramNode programNode = new ProgramNode();
	    	
			Interperter interpreter = new Interperter(programNode, null);
	        HashMap<String, InterpreterDataType> locals = new HashMap<>();

	        
	        locals.put("x", new InterpreterDataType("2"));
	        
	        ConstantNode rightOperand = new ConstantNode(0, 0, "2");
	        OperationNode operationNode = new OperationNode(
	            0, 0, null, OperationNode.OperationType.PREINC, rightOperand
	        );

	        

	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, locals);

	        
	        assertEquals("3.0", result.getValue());

	        
	        assertEquals("2", locals.get("x").getValue());
	    }
	

	    @Test
	    public void testPostIncrementOperationWithLeftOperand() throws Exception {
	        
	        HashMap<String, InterpreterDataType> locals = new HashMap<>();
	        ProgramNode programNode = new ProgramNode();
	    	
			Interperter interpreter = new Interperter(programNode, null);
	        
	        locals.put("x", new InterpreterDataType("2"));

	        
	        ConstantNode leftOperand = new ConstantNode(0, 0, "2");
	        OperationNode operationNode = new OperationNode(
	            0, 0, leftOperand, OperationNode.OperationType.POSTINC, null
	        );

	       

	        
	        InterpreterDataType result = interpreter.GetIDT(operationNode, locals);

	        assertEquals("3.0", result.getValue());

	        assertEquals("2", locals.get("x").getValue());//built ast wrong 
	    }
	    @Test
	    public void testUnaryPositiveOperation() throws Exception {
	        ProgramNode programNode = new ProgramNode();
	    	Interperter interpreter = new Interperter(programNode, null);
	    	HashMap<String, InterpreterDataType> locals = new HashMap<>();

	        ConstantNode rightOperand = new ConstantNode(0, 0, "-2");
	        OperationNode operationNode = new OperationNode(
	            0, 0, null, OperationNode.OperationType.UNARYPOS, rightOperand
	        );

	        InterpreterDataType result = interpreter.GetIDT(operationNode, locals);

	        assertEquals("2.0", result.getValue());
	    }
	    @Test
	    public void testUnaryNegationOperation() throws Exception {
	        ProgramNode programNode = new ProgramNode();
	        Interperter interpreter = new Interperter(programNode, null);
	        HashMap<String, InterpreterDataType> locals = new HashMap<>();

	        ConstantNode rightOperand = new ConstantNode(0, 0, "-2");
	        OperationNode operationNode = new OperationNode(
	            0, 0, null, OperationNode.OperationType.UNARYNEG, rightOperand
	        );

	        InterpreterDataType result = interpreter.GetIDT(operationNode, locals);

	        assertEquals("2.0", result.getValue());
	    }
	    @Test
	    public void testConcatenationOperation() throws Exception {
	       
	        ProgramNode programNode = new ProgramNode();
	        Interperter interpreter = new Interperter(programNode, null);
	        HashMap<String, InterpreterDataType> locals = new HashMap<>();

	        ConstantNode leftOperand = new ConstantNode(0, 0, "Hello, ");
	        ConstantNode rightOperand = new ConstantNode(0, 0, "world!");

	       
	        OperationNode operationNode = new OperationNode(
	            0, 0, leftOperand,
	            OperationNode.OperationType.CONCATENATION,
	            rightOperand
	        );

	        InterpreterDataType result = interpreter.GetIDT(operationNode, locals);

	        assertEquals("Hello, world!", result.getValue());
	    }


}
