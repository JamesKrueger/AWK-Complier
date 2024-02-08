

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Check for command line argument of the file
        if (args.length != 1) {
            System.out.println("Usage: java Main <filename>");
            return;
        }

        String filename = args[0]; 
        

        try {
            String input = readFromFile(filename);

            Lexer lexer = new Lexer(input); 
            List<Token> tokens = lexer.lex(); 

            // Printing the tokens
            for (Token token : tokens) {
                //System.out.println(token);
            }

            // Parsing the tokens
            TokenManager tokenManager = new TokenManager(new LinkedList<>(tokens));
            Parser parser = new Parser(null, tokenManager);
            ProgramNode parsedProgram = parser.Parse();

      
          //System.out.println(parsedProgram);

           
           Interperter interpreter = new Interperter(parsedProgram, null);
           System.out.println("Please put my text file into the same folder as where the SRC file is for the code. The file is how I am testing. ");
           System.out.println("Output for the code in text file is as follows P.S I do not test \ninput processing only loops math and logic: ");
            interpreter.InterpretProgram(parsedProgram, null);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readFromFile(String filename) throws Exception {
        Path path = Paths.get(filename);
        return new String(Files.readAllBytes(path));
    }
}


