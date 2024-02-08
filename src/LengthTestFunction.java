import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

public class LengthTestFunction {
	@Test
    public void testLengthFunction() throws IOException {
		ProgramNode programNode = new ProgramNode();
    	
		Interperter interpreter = new Interperter(programNode, null);

        HashMap<String, InterpreterDataType> arguments = new HashMap<>();
        arguments.put("string", new InterpreterDataType("Hello, World!"));

     

        String result = interpreter.length.Execute.apply(arguments);

        assertEquals("13", result);
    }
}
