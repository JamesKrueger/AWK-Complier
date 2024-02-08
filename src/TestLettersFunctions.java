import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

public class TestLettersFunctions {
	  @Test
	    public void testToLowerFunction() throws IOException {
		  	ProgramNode programNode = new ProgramNode();
	    	
					Interperter interpreter = new Interperter(programNode, null);
		  HashMap<String, InterpreterDataType> args = new HashMap<>();
	      
		  args.put("string", new InterpreterDataType("HELLO WORLD"));
	  

	        String result = interpreter.tolower.Execute.apply(args);

	        assertEquals("hello world", result);
	    }

	    @Test
	    public void testToUpperFunction() throws IOException {
	    	ProgramNode programNode = new ProgramNode();
	    	
			Interperter interpreter = new Interperter(programNode, null);
	    	HashMap<String, InterpreterDataType> args = new HashMap<>();
	        
	    	args.put("string", new InterpreterDataType("hello world"));
	

	        String result = interpreter.toupper.Execute.apply(args);

	        assertEquals("HELLO WORLD", result);
	    }
}
