

public class StringHandler {
    
	private String input;

    private int index;

    public StringHandler(String input) {
        
    	this.input = input;
    
        this.index = 0;
    }

    public char Peek(int i) {
        
    	if (index + i < input.length()) 
    	{
        
    		return input.charAt(index + i);
        } 
    	else {
          
        	throw new IndexOutOfBoundsException("Peek index out of range.");
        }
    }

    public String PeekString(int i) {
        
    	if (index + i <= input.length()) {
        
        	return input.substring(index, index + i);
        } 
        else {
        
        	throw new IndexOutOfBoundsException("PeekString index out of range.");
        }
    }
    
    
public boolean IsDone() {
        
    	return index >= input.length();
    }


    public char GetChar() {
        
    	if (!IsDone()) {
        
    		char currentChar = input.charAt(index);
            
    		index++;
            
    		return currentChar;
        } else {
            
        	throw new IndexOutOfBoundsException("GetChar reached end of document.");
        }
    }

    
    public void Swallow(int i) {
        
    	if (index + i <= input.length()) {
        
    		index += i;
        } else {
            
        	throw new IndexOutOfBoundsException("Swallow index out of range.");
        }
    }

    

    public String Remainder() {
        
    	return input.substring(index);
    }
}
