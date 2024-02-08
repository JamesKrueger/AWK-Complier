import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

public class TestSubFunction {
	 @Test
	 
	    public void testSubFunction() throws IOException {

		 	ProgramNode programNode = new ProgramNode();
	    	
			Interperter interpreter = new Interperter(programNode, null);

	        HashMap<String, InterpreterDataType> arguments = new HashMap<>();
	        
	        arguments.put("target", new InterpreterDataType("Hello, World!"));
	        
	        arguments.put("regexp", new InterpreterDataType("World"));
	        
	        arguments.put("replacement", new InterpreterDataType("Friend"));

	        
	        String result = interpreter.sub.Execute.apply(arguments);
	        System.out.println(result);
	        
	        assertEquals("Hello, Friend!", result);
	        
	        assertEquals("Hello, Friend!", arguments.get("target").getValue());
	    }
}
