
import java.util.LinkedList;
import java.util.Optional;

class TokenManager {
	
    private LinkedList<Token> tokens;

    public TokenManager(LinkedList<Token> tokens) {
        
    	this.tokens = tokens;
    }
    
    /**
     * Gives us the token at a certian index
     * @param j the index we want to peek into
     * @return the token that is in the index
     */
    public Optional<Token> peek(int j) {
        
    	
    	if ( j < 0 || j >= tokens.size()) {
        	
            return Optional.empty(); //Out of tokens
        }
        
    	return Optional.of(tokens.get(j));
    }

   /** 
 * Checks if there is more tokens 
 * @return false if there is no more tokens returns true if there is more tokens
 */
    public boolean moreTokens() {
      
    	if(!tokens.isEmpty())
        
    		return true;
        
       
    return false;
    }
    

    /**
     * Checks if the type we get is equal to the type we passed through 
     * @param t the tokenType passed into it
     * @return empty if the token dont match
     */
    public Optional<Token> matchAndRemove(Token.TokenType t) {
       
    	if (tokens.isEmpty()) {
        
    		return Optional.empty(); //No tokens left to match
        }

        Token nextToken = tokens.peek(); //Peek at the next token
        
        if(!tokens.isEmpty()) {
        
        if (nextToken.getType() == t) {
       
        		tokens.remove(); //Remove the matched token from head of the list
            
        		return Optional.of(nextToken);
        }
}
        return Optional.empty(); //Token type does not match
    }

    
    
}

