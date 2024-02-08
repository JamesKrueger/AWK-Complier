import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

public class TestSplitFunction {
	 @Test
	    public void testSplitFunction() {
	        // Given
	        BuiltInFunctionDefinitionNode splitFunction = new BuiltInFunctionDefinitionNode(
	            (args) -> {
	                String str = args.get("string").getValue();
	                String delimiter = args.get("delimiter").getValue();
	                String[] parts = str.split(Pattern.quote(delimiter));

	                HashMap<String, InterpreterDataType> result = new HashMap<>();
	                for (int i = 0; i < parts.length; i++) {
	                    result.put(String.valueOf(i + 1), new InterpreterDataType(parts[i]));
	                }
	                
	                return result.toString();
	            },
	            false,
	            0, 0
	        );

	        HashMap<String, InterpreterDataType> arguments = new HashMap<>();
	        arguments.put("string", new InterpreterDataType("apple,banana,cherry"));
	        arguments.put("delimiter", new InterpreterDataType(","));

	        Map<String, InterpreterDataType> expectedOutput = new HashMap<>();
	        expectedOutput.put("1", new InterpreterDataType("apple"));
	        expectedOutput.put("2", new InterpreterDataType("banana"));
	        expectedOutput.put("3", new InterpreterDataType("cherry"));

	        String result = splitFunction.Execute.apply(arguments);

	    System.out.println(arguments.toString());
	    System.out.println(expectedOutput.toString());
	    }
}
