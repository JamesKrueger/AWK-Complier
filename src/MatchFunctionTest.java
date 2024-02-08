
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

public class MatchFunctionTest {
    

    @Test
    public void testMatchFunction() throws IOException {
    	ProgramNode programNode = new ProgramNode();
    	
		Interperter interpreter = new Interperter(programNode, null);

        
		HashMap<String, InterpreterDataType> arguments = new HashMap<>();
        
        arguments.put("string", new InterpreterDataType("Hello, World!"));
        
        arguments.put("regexp", new InterpreterDataType("World"));

   

        String result = interpreter.match.Execute.apply(arguments);

        assertEquals("7", result);

        arguments.put("regexp", new InterpreterDataType("Universe"));
        result = interpreter.match.Execute.apply(arguments);

        assertEquals("-1", result);
    }
}

