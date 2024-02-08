

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;



public class LexerTest {
	
	private void assertTokenEquals(Token expected, Token actual) {
		
	    if (expected.getType() != actual.getType() )
	         {
	    	
	        System.out.println("Expected: " + expected);
	        
	        System.out.println("Actual: " + actual);
	        
	        fail("Expected token: " + expected + ", but got: " + actual);
	    }
	    
	}

    @Test
    public void LexerTesting() 
    {
    	String input = "if (x >= 0) { print \"Hello, World!\"; } `This a pattern` 5<=5 4++4";
    	Lexer lexer = new Lexer(input);
        List <Token> tokens = lexer.lex();


        assertTokenEquals(new Token(Token.TokenType.IF, "if", 1, 1), tokens.get(0));
        assertTokenEquals(new Token(Token.TokenType.LEFTPAR, "(", 1, 4), tokens.get(1));
        assertTokenEquals(new Token(Token.TokenType.WORD, "x", 1, 6), tokens.get(2));
        assertTokenEquals(new Token(Token.TokenType.GREATEREQUAL, ">=", 1, 8), tokens.get(3));
        assertTokenEquals(new Token(Token.TokenType.NUMBER, "0", 1, 11), tokens.get(4));
        assertTokenEquals(new Token(Token.TokenType.RIGHTPAR, ")", 1, 13), tokens.get(5));
        assertTokenEquals(new Token(Token.TokenType.LEFTCURLYBRA, "{", 1, 15), tokens.get(6));
        assertTokenEquals(new Token(Token.TokenType.PRINT, "print", 1, 17), tokens.get(7));
        assertTokenEquals(new Token(Token.TokenType.STRINGLIT, "\"Hello, World!\"", 1, 23), tokens.get(8));
        assertTokenEquals(new Token(Token.TokenType.SEPARATOR, ";", 1, 38), tokens.get(9));
        assertTokenEquals(new Token(Token.TokenType.RIGHTCURLYBRA, "}", 1, 40), tokens.get(10));
        assertTokenEquals(new Token(Token.TokenType.PATTERN, "This is a pattern", 1, 40), tokens.get(11));
        assertTokenEquals(new Token(Token.TokenType.NUMBER, "5" , 1,41),tokens.get(12));
        
        assertTokenEquals(new Token(Token.TokenType.LESSEQUAL, "<=" , 1,42),tokens.get(13));
        assertTokenEquals(new Token(Token.TokenType.NUMBER, "5" , 1,43),tokens.get(14));
        assertTokenEquals(new Token(Token.TokenType.NUMBER, "4" , 1,44),tokens.get(15));
        assertTokenEquals(new Token(Token.TokenType.DOUBLEADD, "++" , 1,45),tokens.get(16));
        assertTokenEquals(new Token(Token.TokenType.NUMBER, "4" , 1,46),tokens.get(17));



        
     
    }
    


}

