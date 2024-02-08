import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Interperter {
  
	HashMap<String, InterpreterDataType> globalVariables;
    
	HashMap<String, FunctionDefinitionNode> functionDefinitions;
    
	public LineManager lineManager;
	
	public Interperter(ProgramNode programNode, String filePath) 
			throws IOException 
	{
        globalVariables = new HashMap<>();
       
        functionDefinitions = new HashMap<>();

        globalVariables.put("FS", new InterpreterDataType(" ")); 
        
        globalVariables.put("OFS", new InterpreterDataType(" ")); 
        
        globalVariables.put("ORS", new InterpreterDataType("\n")); 
        
        globalVariables.put("OFMT", new InterpreterDataType("%.6g")); 
        
        globalVariables.put("FILENAME", new InterpreterDataType(filePath)); 

        for (FunctionDefinitionNode function : programNode.getFunctionNodes()) {        
        	
        	functionDefinitions.put(function.getFunctionName(), function);
        }

        if (filePath!=null) {
            
        	List<String> inputLines = Files.readAllLines(Paths.get(filePath));
            
        	lineManager = new LineManager(inputLines);
        } 
        else {
        
        	lineManager = new LineManager(new ArrayList<>());
        }
        functionDefinitions.put("print", print);
        functionDefinitions.put("printf", printf);
        functionDefinitions.put("nextLine", nextLine);
        functionDefinitions.put("gsub", gsub);
        functionDefinitions.put("match", match);
        functionDefinitions.put("sub", sub);
        functionDefinitions.put("index", index);
        functionDefinitions.put("length", length);
        functionDefinitions.put("substr", substr);
        functionDefinitions.put("split", split);
        functionDefinitions.put("tolower", tolower);
        functionDefinitions.put("toupper", toupper);
    }
	  public InterpreterDataType getGlobalVariable(String variableName) {
	        if (globalVariables.containsKey(variableName)) {
	            return globalVariables.get(variableName);
	        } else {
	            throw new IllegalArgumentException("Global variable '" + variableName + "' not found.");
	        }
	    }
    public class LineManager {

    	private List<String> lines;
        

        public LineManager(List<String> lines) {
            this.lines = lines;
        
        }

        
        public boolean splitAndAssign() 
        
        {
            if (lines.isEmpty()) 
        
            {
                return false; //No more lines to split
            }

            String nextLine = lines.remove(0); //remove line from list

            String[] fields = nextLine.split(globalVariables.get("FS").toString());

            
            
            for (int i = 0; i < fields.length; i++) 
            {
                String varName = "Variable: " + i;
            
                globalVariables.put(varName, new InterpreterDataType(fields[i]));
            }

            
            globalVariables.put("NF", new InterpreterDataType());

            globalVariables.put("NR", new InterpreterDataType());
           
            globalVariables.put("FNR", new InterpreterDataType());

            return true;
        }

    }
    
    
    BuiltInFunctionDefinitionNode print = new BuiltInFunctionDefinitionNode(
            (args) -> 
            
            {
            	//Create a stringBuilder
            	StringBuilder result = new StringBuilder();
                
            	List<String> sortedKeys = new ArrayList<>(args.keySet());
                //Make a list of sorted keys that are set to the arguments keys
            	
            	Collections.sort(sortedKeys);//Sort the keys so we can access them in the order we want.
                
            	//Iterate over each key in the sortedKeys list to process them in order.
            	for (String argKey : sortedKeys) {
                //Set value to argumentsKey value to string. 
            		String argValue = args.get(argKey).getValue().toString();
                    
            		result.append(argValue);//Append the value
                }
            	System.out.println(result);
            	return result.toString();//return that value. 
            },
            
            true, //if it's variadic
            0, 0 
        );

    BuiltInFunctionDefinitionNode printf = new BuiltInFunctionDefinitionNode(
    	    (args) -> {
    	        
    	    	if (args.isEmpty()) return ""; 

    	        String format = args.get("0").getValue().toString();

    	        Object[] otherArgs = new Object[args.size() - 1];
    	        for (int i = 1; i < args.size(); i++) {
    	            otherArgs[i - 1] = args.get(String.valueOf(i)).getValue();
    	        }

    	        String result = String.format(format, otherArgs);

    	        System.out.print(result);
    	        return result; 
    	    },
    	    true, // variadic
    	    0, 0 
    	);

    
   
    BuiltInFunctionDefinitionNode nextLine = new BuiltInFunctionDefinitionNode(
    
    		(args) -> {
    	    
    			lineManager.splitAndAssign(); 
    	        
    			return ""; 
    	    },
    	  
    		false, 
    	   
    		0, 0  
    	);
    
    
    BuiltInFunctionDefinitionNode gsub = new BuiltInFunctionDefinitionNode(
        (args) -> {
            
            //Get the target string from the arguments.
            String target = args.get("target").getValue();
            
            //Get the regular expression pattern to be replaced.
            String regexp = args.get("regexp").getValue();
            
            //Get the replacement string.
            String replacement = args.get("replacement").getValue();

            //Do the global substitution on the target string.
            String result = target.replaceAll(regexp, replacement);
            
            args.get("target").setValue(result); 

            //Return the modified string.
            return result;
        },
       
        // if vardaic.
        false, 
       
        0, 0  
    );

    BuiltInFunctionDefinitionNode match = new BuiltInFunctionDefinitionNode(
        (args) -> {
            //Get the input string from arguments.
            String inputString = args.get("string").getValue();
            
            //Get the regular expression pattern from arguments.
            String regexp = args.get("regexp").getValue();

            //Compile the regular expression pattern.
            Pattern pattern = Pattern.compile(regexp);
            
            //Create a matcher to find matches of the pattern in the input string.
            Matcher matcher = pattern.matcher(inputString);

            //Return the start position of the first match, or -1 if no matches are found.
            return Integer.toString(matcher.find() ? matcher.start() : -1); 
        },
        false,  //If vardaic or not.
        0, 0
    );

    BuiltInFunctionDefinitionNode sub = new BuiltInFunctionDefinitionNode(
        (args) -> {

        	//Get the target string, regular expression pattern, and replacement string from arguments.
            
        	String target = args.get("target").getValue();
            
        	String regexp = args.get("regexp").getValue();
            
        	String replacement = args.get("replacement").getValue();

            //Replace the first match of the pattern with the replacement string.
            String result = target.replaceFirst(regexp, replacement);
           
            //Update the target value in the arguments.
            args.get("target").setValue(result); 

            return result;
        },
        false,  //If vardaic or not.
        0, 0
    );

    BuiltInFunctionDefinitionNode index = new BuiltInFunctionDefinitionNode(
        (args) -> {
            //Get the main string and the substring to search from arguments.
            
        	String mainString = args.get("mainString").getValue();
            
        	String searchString = args.get("searchString").getValue();
            
            //Return the index position of the substring in the main string (adding 1 for 1-based index).
           
        	return Integer.toString(mainString.indexOf(searchString) + 1); 
        },
        false,  //If vardaic or not.
        0, 0
    );

    BuiltInFunctionDefinitionNode length = new BuiltInFunctionDefinitionNode(
        (args) -> {
            //Get the string from arguments.
            String str = args.get("string").getValue();
            
            // Return the length of the string.
            return Integer.toString(str.length());
        },
        false,  // Indicates this function does not have a variable number of arguments.
        0, 0
    );

    BuiltInFunctionDefinitionNode substr = new BuiltInFunctionDefinitionNode(
        (args) -> {

        	//Get the string, start position, and optional length from arguments.
            
        	String str = args.get("string").getValue();
            
        	int start = Integer.parseInt(args.get("start").getValue()) - 1;  
            
            //Check if a length is provided and retrieve the substring accordingly.
            
        	if (args.containsKey("length")) {
            
        		int length = Integer.parseInt(args.get("length").getValue());
                
        		return str.substring(start, start + length);
            }
            //If no length is provided, return the substring from start position to the end.
            return str.substring(start);
        },
        false,  //If vardaic or not.
        0, 0
    );

    BuiltInFunctionDefinitionNode split = new BuiltInFunctionDefinitionNode(
        (args) -> {

        	//Get the string and delimiter from arguments.
            
        	String str = args.get("string").getValue();
            
        	String delimiter = args.get("delimiter").getValue();
            
            //Split the string by the delimiter.
            String[] parts = str.split(Pattern.quote(delimiter));

            
            HashMap<String, InterpreterDataType> result = new HashMap<>();
            
            for (int i = 0; i < parts.length; i++) {
            
            	result.put(String.valueOf(i + 1), new InterpreterDataType(parts[i]));
            }
            
            return result.toString();
        },
        false,  //If vardaic or not.
        0, 0
    );

    BuiltInFunctionDefinitionNode tolower = new BuiltInFunctionDefinitionNode(
        (args) -> {
        	
            //Get the string from arguments.
            
        	String str = args.get("string").getValue();
            
            // Return the string in lowercase.
            
        	return str.toLowerCase();
        },
        
        false,  //If vardaic or not.
        
        0, 0
    );
    	BuiltInFunctionDefinitionNode toupper = new BuiltInFunctionDefinitionNode(
    		    (args) -> {
    		        
    		    	//Get string from arguments
    		    	
    		    	String str = args.get("string").getValue();
    		        
    		    	//return the string in uppercase 
    		    	
    		    	return str.toUpperCase();
    		    },
    		    
    		    false,//If vardaic or not
    		    
    		    0, 0
    		);
    	
    	
    	
    	
    	//INT 4 
  
    	public String RunFunctionCall(FunctionCallNode functionCallNode, HashMap<String, InterpreterDataType> locals) throws Exception {
    	   //Get the name
    		String name = functionCallNode.getFunctionName().get().getValue();
    		//See if our hashmap contains the name
    		if (functionDefinitions.containsKey(name)) {
    		  //See if this is a builtinfunctionDef node and is vardaic 
    			if (functionDefinitions.get(name) instanceof BuiltInFunctionDefinitionNode && ((BuiltInFunctionDefinitionNode) functionDefinitions.get(name)).isVariadic()) {
    				
    				BuiltInFunctionDefinitionNode func = ((BuiltInFunctionDefinitionNode) functionDefinitions.get(name));
    				//Make a new hashmap for parameters
    				HashMap<String, InterpreterDataType> paramMap = new HashMap<>();
    		        
    				int i = 0;
    		        //For every parameter in the node
    				for (Node parameter : functionCallNode.getParamaters()) {
    					//We get the parameter and put it into a variable
    					String paramName = parameter.toString();
    		            //Call GetIDT on each parameter
    					InterpreterDataType paramValue = GetIDT(functionCallNode.getParamaters().get(i), locals);
    		           //put them into the map
    					paramMap.put(paramName, paramValue);
    		           //increment our counter of parameters
    					i++;
    		        }
    				//Execute the function on the parameters
    		        return func.Execute.apply(paramMap);
    		        
    		    }
    			//If its only a built in and not vardaic same logic as above
    			if (functionDefinitions.get(name) instanceof BuiltInFunctionDefinitionNode) {
    				BuiltInFunctionDefinitionNode func = ((BuiltInFunctionDefinitionNode) functionDefinitions.get(name));

    			HashMap<String, InterpreterDataType> paramMap = new HashMap<>();
		        
				int i = 0;
		        
				for (String parameter : func.getParameters()) {
		        
					String paramName = parameter.toString();
		            
					InterpreterDataType paramValue = GetIDT(functionCallNode.getParamaters().get(i), locals);
		           
					paramMap.put(paramName, paramValue);
		           
					i++;
		        }
			
				return func.Execute.apply(paramMap);
    			}
    			
    		//If not built in has to be a user defined function
    				HashMap<String, InterpreterDataType> paramMap = new HashMap<>();

    				for(var i=0; i < functionCallNode.getParamaters().size(); i++) 
    				{
        			//For each parameter we call getIDT on the parameters, plus getting the parameters 
        			paramMap.put(String.valueOf(functionDefinitions.get(i)), GetIDT(functionCallNode.getParamaters().get(i),locals));
        			
        			
    				}
    				//Call ILOS on the statements so we can deal with them. 
        		return InterpretListOfStatements(functionDefinitions.get(name).getStatements(), paramMap).getValue();
    			
    			
    			

    			
    		}
			
    		else {
    			throw new Exception("Function is not found");
    		}

			
   
			
    	}
    	
    	public ReturnType InterpretBlock(BlockNode block,HashMap<String, InterpreterDataType> locals ) throws Exception {
			
    		
			Optional<Node> condition = block.getCondition();
	    	
	    	LinkedList<StatementNode> statements = (LinkedList<StatementNode>) block.getStatements();
	    	
	    	locals = new HashMap<>();
	    	//If the condition is empty we look for statements and call ProcessStatement to deal with them
	    	if(condition.isEmpty()) {
	    		for(StatementNode currState : statements) {
	    			ReturnType res = ProcessStatement(locals,currState);
	    			
	    		}
	    		
	    	}
	    	//Should be a condition and see if its true do same logic above
	    	else if(GetIDT(condition.get(),locals).getValue().equals("1")) {
	    		for(StatementNode currState: statements) {
	    			ReturnType res = ProcessStatement(locals,currState);
	    			
	    		}
	    	}
			return null;
    		
    	}
    	public void InterpretProgram(ProgramNode node , HashMap<String, InterpreterDataType> locals) throws Exception
    	{
		//For each begin block call interpretBlock
    		for(BlockNode beginNode : node.getBeginBlocks()) 
    		{
    			InterpretBlock(beginNode, locals);
    		}
    		//Split and assign return true or false, so this is how we would know
    		while(lineManager.splitAndAssign()) 
    		
    		{		//For each other block call interpretBlock

    			for(BlockNode otherNode: node.getOtherBlocks()) 
    			{
    				InterpretBlock(otherNode, locals);
    				
    			}
    		}
    		//For each end block call interpretBlock

    		for(BlockNode endNode : node.getEndBlocks()) 
    		{
    			InterpretBlock(endNode,locals);
    		}
    	}
    	
    	
    	//INT3
    	public ReturnType ProcessStatement(HashMap<String, InterpreterDataType> locals, StatementNode stmt) throws Exception {
			
  
    		if(stmt instanceof BreakNode) 
    		{
    		return new ReturnType(ReturnType.returnType.BREAK);
    		}
    		
    		else if(stmt instanceof ContinueNode) 
    		{
    			return new ReturnType(ReturnType.returnType.CONTINUE);
    		}
    		if (stmt instanceof DeleteNode) {

                DeleteNode node = (DeleteNode) stmt;
                //Get target
                VariableReferenceNode varRefNode = (VariableReferenceNode) node.getTarget();
                String name = varRefNode.getName();
                //Check if locals contains the name
                if(locals.containsKey(name)) 
                {
                	//if it does make sure its a IADT
                    if(locals.get(name) instanceof InterpreterArrayDataType) 
                    {
                    	//Get the index 
                        Optional<Node> array = varRefNode.getIndexExpression();
                        
                        if(array != null) 
                        {
                        	//Call GETIDT on the array index
                        	InterpreterArrayDataType index =(InterpreterArrayDataType) GetIDT(array.get(), locals);
                            //Remove that index//really not sure what it should be here 
                        	index.getElements().remove(GetIDT(varRefNode.getIndexExpression().get(),locals).getValue());
                        }
                    
                        else 
                        {	//If index is null remove whole array from locals
                            locals.remove(name);
                        }
                    }

                }
                //If globals has the key
                else if(globalVariables.containsKey(name)) {
                	//Check if key is IADT
                    if(globalVariables.get(name) instanceof InterpreterArrayDataType) {
                        //Get the index
                    	Optional<Node> array = varRefNode.getIndexExpression();

                        if(array != null) {
                        	//if index is not null call GetIDT on the index
                           InterpreterDataType index =(InterpreterArrayDataType) GetIDT(array.get(), locals);
                            //Remove index
                           index.getElements().remove(GetIDT(varRefNode.getIndexExpression().get(),locals).getValue());                        }
                    
                        else {
                        	//If index is null remove whole array
                        	globalVariables.remove(name);
                        }
                    }
                    
                }
                return new ReturnType(ReturnType.returnType.NORMAL);
    		}
    		
    		else if (stmt instanceof DoWhileNode) 
    		{ 
            
    			DoWhileNode doWhileNode = (DoWhileNode) stmt;
                
    			InterpreterDataType conditionValue;
                
    			ReturnType result;

                do {
                    //Process the list of statements inside the do-while loop
                    result = InterpretListOfStatements((LinkedList<StatementNode>) doWhileNode.getBlock().getStatements(),locals);

                    //Check if we need to break out of the loop
                  
                    if(result.getReturn() == ReturnType.returnType.RETURN) 
                    {
                	  
                	  return result;
                  
                    }
                  
                    if(result.getReturn() == ReturnType.returnType.BREAK) 
                    {
                	  
                	  return new ReturnType(ReturnType.returnType.BREAK);
                  }

                    //Re-evaluate the condition at the end of the do-while loop
                    conditionValue = GetIDT(doWhileNode.getCondition(), locals);

                    
                } while (conditionValue.getValue().equals("1"));

                //If the result was Break, we return from ProcessStatement
                
                
                return new ReturnType(ReturnType.returnType.NORMAL);
            }
    		
    		else if(stmt instanceof ForNode) {
    			 ForNode forNode = (ForNode) stmt;
    			InterpreterDataType inc;
    			//Calling GetIdt on i=0;
    			inc = GetIDT((StatementNode) forNode.getInitialization(), locals);
                InterpreterDataType conditionValue;
                //Calling getidt on i<5;
                conditionValue = GetIDT(forNode.getCondition(),locals);
                //put condition into locals
                while(conditionValue.getValue().equals("1")) 
                {
                	//Call ILOS with our block of statements
                	ReturnType statements = InterpretListOfStatements((LinkedList<StatementNode>) forNode.getBlock().getStatements(), locals);
                	//Check the return types
                	if( statements.getReturn() == ReturnType.returnType.RETURN ) 
                	{
                  	  
                		return statements;
                    
                	}
                    
                	if(statements.getReturn() == ReturnType.returnType.BREAK) 
                	{
                  	  
                		return new ReturnType(ReturnType.returnType.BREAK);
                    
                	}
                	//Calling GetIDT on the i++;
                	InterpreterDataType increment = GetIDT((StatementNode) forNode.getincDec(),locals);
                   //Calling getidt on condition again to see if condition is true.
                	conditionValue = GetIDT(forNode.getCondition(),locals);

                	
                }

    			
    		}
    		else if(stmt instanceof ForEachNode) {
    			
    			ForEachNode node = (ForEachNode) stmt;
    			
    			if(node.getCondition() instanceof OperationNode) 
    			{
    				//Cast my condition as a OperationNode
    				OperationNode forEach = (OperationNode) node.getCondition();
    				
    				if(forEach.getLeft().isPresent()) 
    				{
    					//Check if left is a VariableReferenceNode and right is present 
    					if(forEach.getLeft().get() instanceof VariableReferenceNode && forEach.getRight().isPresent()) 
    					{
    						//Check if right is a variableReferenceNode
    						if(forEach.getRight().get() instanceof VariableReferenceNode) 
    						{
    							//Cast to VRN
    							VariableReferenceNode varRef = (VariableReferenceNode) forEach.getRight().get();
    							//Make a IADT and get the name of the array. 
    							InterpreterArrayDataType array = (InterpreterArrayDataType) locals.get(varRef.getName());
    							//Iterate through the elements of the 'array' using a for-each loop
    							for(Map.Entry<String, InterpreterDataType> currentKey : array.getElements().entrySet()) {
    								//Get the key 
    								String currentValue = currentKey.getKey();
    								//Put the variable name to the key
    								locals.put(varRef.getName(), new InterpreterDataType(currentValue));
    								//Call ILOS on the block of statements 
    								ReturnType statements = InterpretListOfStatements((LinkedList<StatementNode>) node.getBlock().getStatements(), locals);
    			                	//Check the return types
    			                	if( statements.getReturn() == ReturnType.returnType.RETURN ) 
    			                	{
    			                  	  
    			                		return statements;
    			                    
    			                	}
    			                    
    			                	if(statements.getReturn() == ReturnType.returnType.BREAK) 
    			                	{
    			                  	  
    			                		return new ReturnType(ReturnType.returnType.BREAK);
    			                    
    			                	}
    								
    							}
    							
    						}
    					}
    				}
    			}
        		
    		}
    		
    		else if (stmt instanceof IfNode) 
    		{
    	        IfNode currentIfNode = (IfNode) stmt;

    	        while (currentIfNode != null) {
    	            Node condition = currentIfNode.getCondition();
    	           
    	            boolean shouldExecuteBlock = false;

    	            if (condition == null) {
    	                //if no condition should be else block
    	                shouldExecuteBlock = true;
    	            } else {
    	            	//Call GetIdt on the condition 
    	            	InterpreterDataType conditionValue = GetIDT(condition, locals);
    	            //Check if its true
    	                shouldExecuteBlock = conditionValue.getValue().equals("1"); 
    	            }
    	            //If the condition is true 
    	            if (shouldExecuteBlock) {
    	                
    	            	//Call ILOS on the block of statements 
    	            	ReturnType result = InterpretListOfStatements((LinkedList<StatementNode>) currentIfNode.getTrueBlock().getStatements(),locals);

    	                if (result.getReturn() != ReturnType.returnType.NORMAL) {
    	                	//Return result if not normal
    	                		return result;
    	                }

    	                break; //Exit the loop once the correct block is executed
    	            }

    	            //Check if the else if is present or the else block is there
    	            if (currentIfNode.getNextIf().isPresent()) {
    	            	//If the else if is there the code will repeat ( Recursive) 
    	                currentIfNode = currentIfNode.getNextIf().get();
    	            } else if (currentIfNode.getElseBlock().isPresent()) {
    	                //if else is there then call ILOS with our block of statements
    	                ReturnType result = InterpretListOfStatements( (LinkedList<StatementNode>) currentIfNode.getElseBlock().get().getStatements(),locals);

    	                if (result.getReturn() != ReturnType.returnType.NORMAL) {
    	                    return result;
    	                }

    	                break; //Exit the loop 
    	            } else {
    	                break; //No more else/else if blocks, exit the loop
    	            }
    	        }

    	        return new ReturnType(ReturnType.returnType.NORMAL);
    	    }
    		
    		else if (stmt instanceof ReturnNode) {
    	        ReturnNode returnNode = (ReturnNode) stmt;

    	        //Get the expression for return
    	        Node returnValueNode = returnNode.getExpression(); 

    	        if (returnValueNode != null) {
    	        	//Call GetIDT on the expression
    	        	InterpreterDataType evaluatedValue = GetIDT(returnValueNode, locals);
    	            return new ReturnType(ReturnType.returnType.RETURN, evaluatedValue.getValue());
    	        } else {
    	            //If no return value is present
    	            return new ReturnType(ReturnType.returnType.RETURN);
    	        }
    	    }
    		else if (stmt instanceof WhileNode) {
    		    WhileNode whileNode = (WhileNode) stmt;
    		    InterpreterDataType conditionValue;
    		    ReturnType result;

    		    //Call getIdt on the condition
    		    conditionValue = GetIDT(whileNode.getCondition(), locals);

    		    while (conditionValue.getValue().equals("1")) {
    		    	//While condition is true call ILOS on our Statements
    		    	result = InterpretListOfStatements((LinkedList<StatementNode>) whileNode.getBlock().getStatements(), locals);

    		    	//Check return type
    		    	if (result.getReturn() == ReturnType.returnType.RETURN) {
    		            return result;
    		        }
    		        if (result.getReturn() == ReturnType.returnType.BREAK) {
    		            break;
    		        }

    		        //Make sure condition is still true  
    		        conditionValue = GetIDT(whileNode.getCondition(), locals);
    		    }

    		    return new ReturnType(ReturnType.returnType.NORMAL);
    		}
    		else {
    			InterpreterDataType result = GetIDT(stmt,locals);
    			if(result == null) {
    				throw new Exception("Invalid statement");
    			}
    			return new ReturnType(ReturnType.returnType.NORMAL);
    		}
			return null;


    		
    		
			
    	}
    	
    	public ReturnType InterpretListOfStatements(LinkedList<StatementNode> statements, HashMap<String, InterpreterDataType> locals) throws Exception {
    	    
    		for (StatementNode statement : statements) 
    	    {//For each Statement in linked list call process statement on it. 
    	        ReturnType result = ProcessStatement(locals, statement);

    	        
    	        if (result.getReturn() != ReturnType.returnType.NORMAL) {
    	            return result;
    	        }
    	        
    	        
    	    }
    	    return new ReturnType(ReturnType.returnType.NORMAL); // If all statements processed normally
    	}

		public InterpreterDataType GetIDT(Node node, HashMap<String, InterpreterDataType> locals) throws Exception {
    		
    		locals = new HashMap<>();
    		
    		if (node instanceof AssignmentNode) 
    		{
    		    AssignmentNode assignmentNode = (AssignmentNode) node;
    		    if (assignmentNode.getExpression() instanceof OperationNode) 
    		    {
    		        OperationNode operationNode = (OperationNode) assignmentNode.getExpression();
    		        
    		        //Obtain the left side
    		        Node leftSide = operationNode.getLeft().get();
    		        
    		            if (leftSide instanceof VariableReferenceNode) 
    		            {
    		                String targetVariable = ((VariableReferenceNode) leftSide).getName();
    		                if(operationNode.getRight().isPresent()) {
    		                	
    		                	
    		                //Evaluate the right-hand side expression
    		                InterpreterDataType rightValue = GetIDT(operationNode.getRight().get(), locals);

    		                //Set the target's value to the result
    		                globalVariables.put(targetVariable, rightValue);

    		                //Return the result of the assignment
    		                return rightValue;
    		                }  
    		                else {
    		                	//Deals with Postinc/dec  because no right is present but is a assignmentnode. 
    		                	InterpreterDataType inc = GetIDT(assignmentNode.getExpression(),locals);
    		                	  return globalVariables.get(targetVariable); 
    		                }
    		               
    		            } else if (leftSide instanceof OperationNode && OperationNode.OperationType.DOLLAR.equals(((OperationNode) leftSide).getOperation())) 
    		            {
    		            	String targetVariable = "";
    		            	if (leftSide instanceof OperationNode) {
    		            	  Object left = ((OperationNode) leftSide).getLeft();
    		            	  if (left != null) {
    		            	    targetVariable = left.toString();
    		            	  }
    		            	}
    		                //Evaluate the right-hand side expression
    		                InterpreterDataType rightValue = GetIDT(operationNode.getRight().get(), locals);

    		                //Set the target's value to the result
    		                globalVariables.put(targetVariable, rightValue);

    		                //Return the result of the assignment
    		                return rightValue;
    		            }
    		        
    		    }
    		}

    	    else if (node instanceof ConstantNode) 
    	    {
    	        ConstantNode constantNode = (ConstantNode) node;
    	        //return IDT with value
    	        return new InterpreterDataType(constantNode.getValue());
    	    }
    	    else if (node instanceof PatternNode) 
    	    {
    		    throw new IllegalArgumentException("PatternNode cannot be used in this context.");
    		}
    	    else if (node instanceof FunctionCallNode) 
    	    {
    	        FunctionCallNode functionCallNode = (FunctionCallNode) node;
    	        String result = RunFunctionCall(functionCallNode, locals);
    	        return new InterpreterDataType(result);
    	    }
    	    else if (node instanceof TernaryNode) {
    	    	
    	        TernaryNode ternaryNode = (TernaryNode) node;
    	        
    	        //Obtain the condition
    	        InterpreterDataType conditionResult = GetIDT(ternaryNode.getCondition(), locals);

    	        //Check if the conditionResult is equal to "1" based off the condition
    	        
    	        if (conditionResult.getValue().equals("1")) {
    	        
    	        	return GetIDT(ternaryNode.getTrueBranch(), locals);
    	        } 
    	        else {
    	        
    	        	return GetIDT(ternaryNode.getFalseBranch(), locals);
    	        }
    	    }
    	    else if (node instanceof VariableReferenceNode) {
                
                VariableReferenceNode varRefNode = (VariableReferenceNode) node;
                
                String varName = varRefNode.getName();

            
                if(varRefNode.getIndexExpression().isEmpty()) {
                //Check if the variable exists in locals or globals
                if (locals.containsKey(varName)) {
                    return locals.get(varName);
                } else if (globalVariables.containsKey(varName)) {
                    return globalVariables.get(varName);
                } 
                else if(!globalVariables.containsKey(varName)&& !locals.containsKey(varName)){
                    //Add the variable to globalVariables with a null value
                    globalVariables.put(varName, new InterpreterDataType());
                    //Create a new InterpreterDataType with the variable name
                    return  new InterpreterDataType(varName);
                }
                
                }
                else {
                    //Get the expression
                    InterpreterDataType index = GetIDT(varRefNode.getIndexExpression().get(), locals);
                    //Get name
                    InterpreterDataType returnType = globalVariables.get(varRefNode.getName());
                    //if a IDAT 
                    if(returnType instanceof InterpreterArrayDataType) {
                    //Get the elements at the index we want 
                        return ((InterpreterArrayDataType) returnType).getElements().get(index.getValue());
                    }
                    
                }

                
               
            }


    	    else if (node instanceof OperationNode) 
    	    {
    	       
    	    	OperationNode operationNode = (OperationNode) node;
    	      
    	    	//Get the left 
    	        InterpreterDataType leftOperand = GetIDT(operationNode.getLeft().get(), locals);
    	       
    	        //Perform the operation based on OperationType
    	        String resultValue = "";
    	        //See what operation it is. 
    	        switch (operationNode.getOperation()) 
    	        {
    	        case ADD:
    	        	 
    	            try {
    	            	
    	            	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);

    	                //Try to parse the left and right operands as floats
    	                float leftFloat = Float.parseFloat(leftOperand.getValue());
    	               
    	                float rightFloat = Float.parseFloat(rightOperand.getValue());

    	                //Perform the addition and store the result as a string
    	                resultValue = String.valueOf(leftFloat + rightFloat);
    	                
    	            } catch (NumberFormatException e) 
    	            {
    	            
    	            	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
    	
    	                resultValue = leftOperand.getValue() + rightOperand.getValue();
    	            }
    	            break;
    	            
    	            case SUBTRACT:
    	            	
    	               
    	                try {
    	                	//Get the right operand
    	                	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
        	                
    	                	//Try to parse the left and right operands as floats
    	                   	float leftFloatSub = Float.parseFloat(leftOperand.getValue());
        	                
        	            	float rightFloatSub = Float.parseFloat(rightOperand.getValue());
        	            	
        	            	resultValue = String.valueOf(leftFloatSub - rightFloatSub);
        	            
        	            } catch (NumberFormatException e) {
        	               
        	                resultValue = null;
        	            }
        	            break;
        	            
    	            case MULTIPLY:
    	            	
    	                try {
    	                	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
        	               
    	                	//Try to parse the left and right operands as floats
    	                   	float leftFloatMulti = Float.parseFloat(leftOperand.getValue());
        	                
        	            	float rightFloatMulti = Float.parseFloat(rightOperand.getValue());
        	            	
        	            	resultValue = String.valueOf(leftFloatMulti * rightFloatMulti);
        	            
        	            } catch (NumberFormatException e) {
    	                    throw new IllegalArgumentException("Fuck you");

        	            }
        	            break;
    	            case DIVIDE:
    	                try {
    	                	//Get the right operand
    	                    InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
    	                    //Try to parse the left and right operands as floats
    	                    float leftFloatDiv = Float.parseFloat(leftOperand.getValue());
    	                    float rightFloatDiv = Float.parseFloat(rightOperand.getValue());
    	                    
    	                    // Ensure we are not dividing by zero
    	                    if (rightFloatDiv == 0) {
    	                        throw new ArithmeticException("Division by zero.");
    	                    }
    	                    
    	                    resultValue = String.valueOf(leftFloatDiv / rightFloatDiv);
    	                
    	                } catch (NumberFormatException e) {
    	                    throw new IllegalArgumentException("Non-numeric value used in division.");
    	                }
    	                break;

    	            case EXPONENT:
    	                try {
    	                	//Get the right operand
    	                    InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
    	                    // Try to parse the left and right operands as floats
    	                    float leftFloatExp = Float.parseFloat(leftOperand.getValue());
    	                   
    	                    float rightFloatExp = Float.parseFloat(rightOperand.getValue());
    	                    
    	                    resultValue = String.valueOf(Math.pow(leftFloatExp, rightFloatExp));
    	                
    	                } catch (NumberFormatException e) {
    	                    throw new IllegalArgumentException("Non-numeric value used in exponentiation.");
    	                }
    	                break;

    	            case MODULO:
    	                try {
    	                	//Get the rightOperand
    	                    InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
    	                    //Try to parse the left and right operands as integers
    	                    int leftIntMod = Integer.parseInt(leftOperand.getValue());
    	                    int rightIntMod = Integer.parseInt(rightOperand.getValue());
    	                    
    	                    //Ensure we are not performing modulo by zero
    	                    if (rightIntMod == 0) {
    	                        throw new ArithmeticException("Modulo by zero.");
    	                    }
    	                    
    	                    resultValue = String.valueOf(leftIntMod % rightIntMod);
    	                
    	                } catch (NumberFormatException e) {
    	                    throw new IllegalArgumentException("Non-numeric value used in modulo operation.");
    	                }
    	                break;
    	           
    	            case EQS:
    	            	
    	                try 
    	                {
    	                	//Get the right operand 
    	                	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
    	                   	//Convert to float
    	                	float leftFloatE = Float.parseFloat(leftOperand.getValue());
        	                
        	            	float rightFloatE = Float.parseFloat(rightOperand.getValue());
        	            	
        	            	
        	            	if(leftFloatE == rightFloatE)
        	            	
        	            		resultValue = "1";
        	            	
        	            	else
        	            	
        	            		resultValue = "0";
        	            
        	            } catch (NumberFormatException e) {
        	            	
        	            	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
        	                
        	                resultValue = String.valueOf(leftOperand.getValue() == rightOperand.getValue());
        	            }
        	            break;
    	            
    	            case NE:
    	                try 
    	                {
    	                	//Get the right operand 
    	                	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
    	                   	//Convert to float 
    	                	float leftFloatE = Float.parseFloat(leftOperand.getValue());
        	                
        	            	float rightFloatE = Float.parseFloat(rightOperand.getValue());
        	            	//Do operation
        	            	if(leftFloatE != rightFloatE)
        	            		resultValue = "1";
        	            	else
        	            		resultValue = "0";
        	            
        	            } catch (NumberFormatException e) 
    	                {
        	            	
        	            	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
        	            
        	            	 resultValue = String.valueOf(leftOperand.getValue() != rightOperand.getValue());
        	            }
        	            break;
    	          
    	            case GT:
    	            	
    	                try {
    	                	//Get the right operand
    	                	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
    	                   	
    	                	float leftFloatG = Float.parseFloat(leftOperand.getValue());
        	                
        	            	float rightFloatG = Float.parseFloat(rightOperand.getValue());
        	            	
        	            	
        	            if(leftFloatG > rightFloatG)
        	            	
        	            	resultValue = "1";
        	            
        	            else
        	            
        	            	resultValue = "0";
        	            } 
    	                catch (NumberFormatException e) 
    	                {
        	            	
    	                	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
        	                
        	            	 resultValue = String.valueOf(leftOperand.getValue().compareTo(rightOperand.getValue()));
        	            }
        	            break;
    	            	
    	            
    	            case LT:
    	            	
    	            	 try {
    	            		 //Get the right operand
    	            		 InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
     	                   	
    	            		 float leftFloatL = Float.parseFloat(leftOperand.getValue());
         	                
         	            	 float rightFloatL = Float.parseFloat(rightOperand.getValue());
         	            	
         	            	
         	           
         	            	 if(leftFloatL < rightFloatL)
         	            	
         	            		 resultValue = "1";
         	            
         	            	 else
         	            	
         	            		 resultValue = "0";
         	            
         	            } 
    	            	 
    	            	 catch (NumberFormatException e) {
         	            
    	            		 InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
         	            	 
    	            		 resultValue = String.valueOf(leftOperand.getValue().compareTo(rightOperand.getValue()));
         	            }
         	            break;
    	            	
    	            case LE:
    	            	
   	            	 try {
   	            		 //Get the right operand
   	            		 InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
    	                   	
   	            		 float leftFloatL = Float.parseFloat(leftOperand.getValue());
        	                
        	            	 float rightFloatL = Float.parseFloat(rightOperand.getValue());
        	            	
        	            	
        	           
        	            	 if(leftFloatL <= rightFloatL)
        	            	
        	            		 resultValue = "1";
        	            
        	            	 else
        	            	
        	            		 resultValue = "0";
        	            
        	            } 
   	            	 
   	            	 catch (NumberFormatException e) {
        	            
   	            		 InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
        	            	 
   	            		 resultValue = String.valueOf(leftOperand.getValue().compareTo(rightOperand.getValue()));
        	            }
        	            break;
        	            
    	            case GE:
    	            	
      	            	 try {
      	            		 //Get the right operand
      	            		 InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
       	                   	
      	            		 float leftFloatL = Float.parseFloat(leftOperand.getValue());
           	                
           	            	 float rightFloatL = Float.parseFloat(rightOperand.getValue());
           	            	
           	            	
           	           
           	            	 if(leftFloatL >= rightFloatL)
           	            	
           	            		 resultValue = "1";
           	            
           	            	 else
           	            	
           	            		 resultValue = "0";
           	            
           	            } 
      	            	 
      	            	 catch (NumberFormatException e) {
           	            
      	            		 InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
           	            	 
      	            		 resultValue = String.valueOf(leftOperand.getValue().compareTo(rightOperand.getValue()));
           	            }
           	            break;
    	            case AND:
    	            	
    	              
    	                try {
    	                	//Get the right operand
    	                	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
    	                   	
    	                	float leftFloatAnd = Float.parseFloat(leftOperand.getValue());
        	                
        	            	float rightFloatAnd = Float.parseFloat(rightOperand.getValue());
        	            	
        	            	//resultValue = leftFloatG > rightFloatG;
        	            
        	            	if(leftFloatAnd !=0 && rightFloatAnd!=0)
        	            	
        	            	resultValue = "1";
        	            
        	            
        	            	else
        	            	
        	            	resultValue = "0";
        	            } 
    	                catch (NumberFormatException e) {
        	            	
    	                	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
        	            	
    	                	resultValue = String.valueOf(leftOperand.getValue().compareTo(rightOperand.getValue()));
        	            }
        	            break;

    	            case OR:
    	               
    	            	  try {
    	            		  //Get the rightOperand
    	            		  InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
      	                   
    	            		  //Convert to float
    	            		  float leftFloatAnd = Float.parseFloat(leftOperand.getValue());
          	                
          	            	
    	            		  float rightFloatAnd = Float.parseFloat(rightOperand.getValue());
          	            	
          	            
    	            		  if(leftFloatAnd !=0 && rightFloatAnd!=0)
    	            			  
    	            			  resultValue = "1";
          	            
          	            	
    	            		  else
          	            
          	            		resultValue = "0";
          	            
          	            } 
    	            	  catch (NumberFormatException e) {
          	            	
    	            		  InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);
          	         
          	            	 resultValue = String.valueOf(leftOperand.getValue().compareTo(rightOperand.getValue()));
          	            }
          	            break;

    	            case NOT:
    	               
    	            	//Get the right
    	            	InterpreterDataType rightOp = GetIDT(operationNode.getRight().orElse(null), locals);
    	                
    	            	float boolNotFloat = Float.parseFloat(rightOp.getValue());
    	                
    	                resultValue = (boolNotFloat != 0) ? "1" : "0";
    	                
    	                break;
    	         
    	            case MATCH:
    	                
	                	//Get the right operand
    	            	PatternNode patternNode = (PatternNode) operationNode.getRight().orElse(null);
    	                
    	            	if (!(patternNode instanceof PatternNode)) {
    	                    throw new IllegalArgumentException("Right operand of !~ (not match) must be a PatternNode.");
    	                }

    	                //Get the regex pattern from the PatternNode
    	                String pattern = patternNode.getValue();

    	                String leftValue = leftOperand.getValue();

    	                //Use Pattern.compile() to compile the pattern
    	                Pattern regexPattern = Pattern.compile(pattern);

    	                //Use Matcher.find() to check if there is a match in the input string
    	                Matcher matcher = regexPattern.matcher(leftValue);
    	               
    	                boolean matches = matcher.find();

    	                //Convert the boolean result to "1" or "0"
    	                resultValue = matches ? "1" : "0";

    	                break;


    	            case NOTMATCH:
	                	//Get the right operand
    	            	PatternNode notPatternNode = (PatternNode) operationNode.getRight().orElse(null);
    	                if (!(notPatternNode instanceof PatternNode)) {
    	                	
    	                    throw new IllegalArgumentException("Right operand of !~ (not match) must be a PatternNode.");
    	                }
    	                
    	                //Get the regex pattern from the PatternNode
    	                String notPattern = notPatternNode.getValue();

    	                String leftNotValue = leftOperand.getValue();

    	                //Use Pattern.matches() to check if the leftNotValue does not match the pattern
    	                boolean notMatches = !Pattern.matches(notPattern, leftNotValue);

    	                //Convert the boolean result to "1" or "0"
    	                resultValue = notMatches ? "1" : "0";
    	                
    	                break;


    	            case DOLLAR:
    	               
    	            	try {
    	            	        	                
    	                    //Ensure that the left is not null
    	                    if (leftOperand == null) 
    	                    {
    	                        throw new IllegalArgumentException("Right operand of '$' is missing.");
    	                    }

    	                    //Add a "$" to the left operand's string representation
    	                    String dollarValue = "$" + leftOperand.getValue();

    	                    //Get the value of the resulting string as a variable
    	                    if (locals.containsKey(dollarValue)) 
    	                    {
    	                        
    	                    	return locals.get(dollarValue);
    	                    }
    	                    if (locals.containsKey(dollarValue)) 
    	                    {
    	        	            
    	                    	return locals.get(dollarValue);
    	        	        } 
    	                    else if (globalVariables.containsKey(dollarValue)) 
    	                    {
    	        	            
    	        	        	return globalVariables.get(dollarValue);
    	        	        } 
    	        	        else {
    	        	            //Add the variable to globalVariables with a null value
    	        	            globalVariables.put(dollarValue, new InterpreterDataType());
    	        	            //Create a new InterpreterDataType with the variable name
    	        	            return new InterpreterDataType(dollarValue); // Assuming InterpreterDataType can store a String
    	        	        }
    	                } catch (NullPointerException e) {

    	                	throw new IllegalArgumentException("Right operand of '$' is missing.");
    	                }
    	            	
    	            case PREINC:
    	                
    	            	//Evaluate the operand
      	            	InterpreterDataType rightOperand = GetIDT(operationNode.getRight().orElse(null), locals);

    	                //Perform pre-increment
    	                float preIncrementFloat = Float.parseFloat(rightOperand.getValue());
    	               
    	                ++preIncrementFloat;
    	                
    	                String preIncrementResult = String.valueOf(preIncrementFloat);
    	                
    	                //Update the variable in locals with the new value
    	                locals.put(rightOperand.getValue(), new InterpreterDataType(preIncrementResult));
    	                
    	                //Return the updated value
    	                return new InterpreterDataType(preIncrementResult);

    	            case POSTINC:
    	            	//Perform post-increment
    	            	float postIncrementFloat = Float.parseFloat(leftOperand.getValue());
    	                
    	            	postIncrementFloat++;
    	                
    	            	String postIncrementResult = String.valueOf(postIncrementFloat);

    	                //Update the variable in locals with the new value
    	                globalVariables.put(((OperationNode) node).getLeft().get().toString(), new InterpreterDataType(postIncrementResult));

    	                //Return the original value (before increment)
    	                return globalVariables.get(((OperationNode) node).getLeft().get().toString());
    	          
    	                
    	            case POSTDEC:
    	            	//Perform post-increment
    	            	float postIncrementFloat1 = Float.parseFloat(leftOperand.getValue());
    	                
    	            	postIncrementFloat1--;
    	                
    	            	String postIncrementResult1 = String.valueOf(postIncrementFloat1);

    	                //Update the variable in locals with the new value
    	                globalVariables.put(((OperationNode) node).getLeft().get().toString(), new InterpreterDataType(postIncrementResult1));

    	                //Return the original value (before increment)
    	                return globalVariables.get(((OperationNode) node).getLeft().get().toString());
    	            
    	            case PREDEC:
    	            	//Perform post-increment
    	            	float postIncrementFloat2 = Float.parseFloat(leftOperand.getValue());
    	                
    	            	--postIncrementFloat2;
    	                
    	            	String postIncrementResult2 = String.valueOf(postIncrementFloat2);

    	                //Update the variable in locals with the new value
    	                globalVariables.put(((OperationNode) node).getLeft().get().toString(), new InterpreterDataType(postIncrementResult2));

    	                //Return the original value (before increment)
    	                return globalVariables.get(((OperationNode) node).getLeft().get().toString());

    	            case UNARYPOS:
    	                
    	            	//Get the operand
    	                InterpreterDataType rightOperandd = GetIDT(operationNode.getRight().orElse(null), locals);

    	                //Convert to float 
    	                float operandFloat = Float.parseFloat(rightOperandd.getValue());

    	                //Convert the operand to its positive counterpart
    	                float unaryPlusFloat = Math.abs(operandFloat) * 1;

    	                //Return the positive value as a string
    	                return new InterpreterDataType(String.valueOf(unaryPlusFloat));

    	            case UNARYNEG:
    	            	
    	                //Get the operand
    	                InterpreterDataType rightOperanddd = GetIDT(operationNode.getRight().orElse(null), locals);

    	                //Convert to float 
    	                float negFloat = Float.parseFloat(rightOperanddd.getValue());

    	                //Convert the operand to its positive counterpart
    	                float unaryNegFloat = negFloat *(-1);

    	                //Return the positive value as a string
    	                return new InterpreterDataType(String.valueOf(unaryNegFloat));
    	             
    	            case CONCATENATION:
      	            	InterpreterDataType rightOperanddddd = GetIDT(operationNode.getRight().orElse(null), locals);

    	                //Get both operands
    	                InterpreterDataType leftConcat = leftOperand;
    	                
    	                InterpreterDataType rightConcat = rightOperanddddd;
    	                
    	                //Concatenate the two strings
    	                String concatenatedValue = leftConcat.getValue() + rightConcat.getValue();
    	                
    	                //Return the concatenated string
    	                return new InterpreterDataType(concatenatedValue);
    	            
    	            case IN:
    	            	
    	                //Get the left operand (value to be searched)
      	            	InterpreterDataType rightOperandddddd = GetIDT(operationNode.getRight().orElse(null), locals);

    	                InterpreterDataType leftIn = leftOperand;

    	                //Get the right operand (array)
    	                InterpreterDataType rightIn = rightOperandddddd;

    	                //Check if the right operand is an array
    	                if (!(rightIn instanceof InterpreterArrayDataType)) {
    	                    throw new IllegalArgumentException("Right operand of 'IN' must be an array.");
    	                }

    	                //Check if the left operand exists in the array
    	                
    	                InterpreterArrayDataType array = (InterpreterArrayDataType) rightIn;
    	                
    	                boolean isInArray = array.containsElement(leftIn.toString());

    	                //Return '1' if the value is in the array, '0' otherwise
    	                return new InterpreterDataType(isInArray ? "1" : "0");

    	            default:
    	                
    	                throw new IllegalArgumentException("Unsupported operation: " + operationNode.getOperation());
    	        }

    	        return new InterpreterDataType(resultValue);
    	    }


    	    return null;
    	}

    	

}