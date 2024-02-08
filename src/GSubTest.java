
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

public class GSubTest {

    @Test
    public void testGsubFunction() throws IOException {
    	ProgramNode programNode = new ProgramNode();
    	
		Interperter interpreter = new Interperter(programNode, null);

        HashMap<String, InterpreterDataType> arguments = new HashMap<>();
        
        arguments.put("target", new InterpreterDataType("Hello, World World !"));
       
        arguments.put("regexp", new InterpreterDataType("World"));
        
        arguments.put("replacement", new InterpreterDataType("Universe"));

        String result = interpreter.gsub.Execute.apply(arguments); 

        assertEquals("Hello, Universe Universe !", result);
      
        assertEquals("Hello, Universe Universe !", arguments.get("target").getValue());
        
        System.out.print(result);
    }
}
