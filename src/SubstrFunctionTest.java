
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class SubstrFunctionTest {

    @Test
    public void testSubstrFunction() throws IOException {

 
ProgramNode programNode = new ProgramNode();
    	
		Interperter interpreter = new Interperter(programNode, null);
        HashMap<String, InterpreterDataType> arguments1 = new HashMap<>();
        arguments1.put("string", new InterpreterDataType("Hello, World!"));
       
        arguments1.put("start", new InterpreterDataType("8"));
        
        String result1 = interpreter.substr.Execute.apply(arguments1);
        
        assertEquals("World!", result1);

        HashMap<String, InterpreterDataType> arguments2 = new HashMap<>();
        
        arguments2.put("string", new InterpreterDataType("Hello, World!"));
        
        arguments2.put("start", new InterpreterDataType("8"));
        
        arguments2.put("length", new InterpreterDataType("5"));
        
        String result2 = interpreter.substr.Execute.apply(arguments2);
        
        assertEquals("World", result2);
        
        System.out.println(result1);
        
        System.out.println(result2);

    }
    @Test
    public void testSubstrFunction2() throws IOException {
        // Create an instance of your Interpreter and set up function definitions
    	ProgramNode programNode = new ProgramNode();
    	
		Interperter interpreter = new Interperter(programNode, null);
        // Define some input arguments for the substr function
        HashMap<String, InterpreterDataType> arguments = new HashMap<>();
        
        arguments.put("string", new InterpreterDataType("Hello, World!"));
        
        arguments.put("start", new InterpreterDataType("8"));
        
        arguments.put("length", new InterpreterDataType("5"));

    

        // Call the execute method of the substr function
        String result = interpreter.substr.Execute.apply(arguments);

        // Assert the result
        assertEquals("World", result);
        System.out.println(result+" new");
    }
}

