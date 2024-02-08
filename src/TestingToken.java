

	import static org.junit.Assert.assertEquals;
	import static org.junit.Assert.assertFalse;
	import static org.junit.Assert.assertNotNull;
	import static org.junit.Assert.assertTrue;

	import java.util.ArrayList;
	import java.util.LinkedList;
	import java.util.List;
	import java.util.Optional;

import org.junit.Before;
import org.junit.Test;


	public class TestingToken {
		private TokenManager tokenManager;

	    @Before
	    public void setUp() {
	       
	    	LinkedList<Token> tokens = new LinkedList<>();
	        
	    	tokens.add(new Token(Token.TokenType.WORD, "hello", 1, 0));
	        
	    	tokens.add(new Token(Token.TokenType.NUMBER, "4", 1, 0));
	        
	    	tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 1, 2));
	        
	    	tokens.add(new Token(Token.TokenType.WORD, "world", 1, 3));
	       	       
	    	tokenManager = new TokenManager(tokens);
	    }

	    @Test
	    public void testPeek() {
	        
	    	Optional<Token> tokenOptional = tokenManager.peek(2);

	        assertTrue(tokenOptional.isPresent());

	        Token token = tokenOptional.get();
	        
	        assertEquals(Token.TokenType.SEPARATOR, token.getType());
	        
	        assertEquals(";", token.getValue());
	    }

	    @Test
	    public void testPeekOutOfRange() {
	        
	    	Optional<Token> tokenOptional = tokenManager.peek(5);

	        assertFalse(tokenOptional.isPresent());
	    }

	    @Test
	    public void testMoreTokens() {
	        
	    	assertTrue(tokenManager.moreTokens());
	        
	        tokenManager.matchAndRemove(Token.TokenType.WORD);
	        
	        assertTrue(tokenManager.moreTokens());
	        
	        tokenManager.matchAndRemove(Token.TokenType.NUMBER);
	        
	        assertTrue(tokenManager.moreTokens());
	        
	        tokenManager.matchAndRemove(Token.TokenType.SEPARATOR);
	        
	        assertTrue(tokenManager.moreTokens());
	        
	        tokenManager.matchAndRemove(Token.TokenType.WORD);
	        
	        assertFalse(tokenManager.moreTokens());
	    }

	    @Test
	    public void testMatchAndRemove() {
	        
	    	assertTrue(tokenManager.moreTokens());
	        
	        Optional<Token> wordTokenOptional = tokenManager.matchAndRemove(Token.TokenType.WORD);
	        
	        assertTrue(wordTokenOptional.isPresent());
	        
	        Token wordToken = wordTokenOptional.get();
	        
	        assertEquals(Token.TokenType.WORD, wordToken.getType());

	        assertTrue(tokenManager.moreTokens());

	       
	        Optional<Token> numberTokenOptional = tokenManager.matchAndRemove(Token.TokenType.NUMBER);

	        
	        assertTrue(numberTokenOptional.isPresent());
	        
	        Token numberToken = numberTokenOptional.get();
	        
	        assertEquals(Token.TokenType.NUMBER, numberToken.getType());

	        assertTrue(tokenManager.moreTokens());

	        Optional<Token> separatorTokenOptional = tokenManager.matchAndRemove(Token.TokenType.SEPARATOR);

	        assertTrue(separatorTokenOptional.isPresent());

	        Token separatorToken = separatorTokenOptional.get();
	        
	        assertEquals(Token.TokenType.SEPARATOR, separatorToken.getType());

	        assertTrue(tokenManager.moreTokens());
	        
	        Optional<Token> wordToken2Optional = tokenManager.matchAndRemove(Token.TokenType.WORD);
	        
	        assertTrue(wordToken2Optional.isPresent());
	        
	        Token wordToken2 = wordToken2Optional.get();
	        
	        assertEquals(Token.TokenType.WORD, wordToken2.getType());
	    }


	    @Test
	    public void testMatchAndRemoveWrongType() {
	    	
	        Optional<Token> tokenOptional = tokenManager.matchAndRemove(Token.TokenType.SEPARATOR);

	        assertFalse(tokenOptional.isPresent());
	    }
	}
		  

