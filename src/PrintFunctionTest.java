
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class PrintFunctionTest {
	@Test
    public void testPrintFunction() throws IOException {

		ProgramNode programNode = new ProgramNode();
		
		Interperter interpreter = new Interperter(programNode, null);

  

        // Define some input arguments for the printf function
        HashMap<String, InterpreterDataType> arguments = new HashMap<>();
        arguments.put("0", new InterpreterDataType("%s%s%s"));
        arguments.put("1", new InterpreterDataType("Hello"));
        arguments.put("2", new InterpreterDataType(","));
        arguments.put("3", new InterpreterDataType(" World!"));

        // Call the execute method of the printf function
        String result = interpreter.printf.Execute.apply(arguments);

        // Assert the result
        assertEquals("Hello, World!", result);
    }
	    
	   
	       
    
}

