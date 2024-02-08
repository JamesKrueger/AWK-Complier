import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

public class IndexTestFunction {
	   @Test
	    public void testIndexFunction() throws IOException {
		   ProgramNode programNode = new ProgramNode();
	    	
			Interperter interpreter = new Interperter(programNode, null);

	        HashMap<String, InterpreterDataType> arguments = new HashMap<>();
	        
	        arguments.put("mainString", new InterpreterDataType("Hello, World!"));
	        
	        arguments.put("searchString", new InterpreterDataType("World"));

	        String result = interpreter.index.Execute.apply(arguments);

	        assertEquals("8", result);

	        System.out.print(result);
	    }

	
}
