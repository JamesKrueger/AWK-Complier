
public class Token {
	//All my Token types
	public enum TokenType {
		WORD,
		IF,
		ELSE,
		BEGIN,
		DO,
		FOR,
		NUMBER,
		SEPARATOR,
		WHILE,
		BREAK,
		CONTINUE,
		RETURN,
		END,
		PRINT,
		PRINTF,
		DOT,
		NEXT,
		IN,
		DEL,
		GETLINE,
		EXIT,
		NEXTFILE,
		FUNCTION,
		RIGHTPAR,
		QUESTIONMARK,
		LEFTPAR,
		RIGHTBRA,
		LEFTBRA,
		GREATEREQUAL,
		DOUBLEADD,
		DOUBLEOR,
		DOUBLESUB,
		LESSEQUAL,
		DOUBLEEQUAL,
		FACTEQUAL,
		FACTSWINGLY,
		ARROWEQUAL,
		PERCENTEQUAL,
		TIMESEQUAL,
		DIVIDEEQUAL,
		PLUSEQUAL,
		SUBEQUAL,
		DOUBLEAND,
		DOUBLEARROW,
		ORSYMBOL,
		COMMA,
		EQUAL,
		STRINGLIT,
		PATTERN,
		SUB,
		ADD,ADDEQUAL, LEFTCURLYBRA, RIGHTCURLYBRA, LEFTARROW, RIGHTARROW, EXCLIMATIONPOINT, UPARROW, COLON,BACKSLASH, TIMES, PERCENT, BAR, SWINGLY, MONEYSIGN, NEWLINE, BREAK2, LENGTH
		
		
	}
    private TokenType type;
    
    private String value;
    
    private int lineNumber;
    
    private int charPosition;

    public Token(TokenType type, int lineNumber, int charPosition) {
    
    	this.type = type;
        
    	this.lineNumber = lineNumber;
        
    	this.charPosition = charPosition;
    }

    public Token(TokenType type, String value,int lineNumber, int charPosition) {
        
    	this(type, lineNumber, charPosition);
        
    	this.value = value;
    }

    @Override
    public String toString() {
    	
    	if(value==null)
    		
    		return "(" + "Token " + type + ")";
    	
    	
    	
        return "Token(" + type + ", " + value + ")";
        
        
    }

    public TokenType getType() {
        return type;
    }
    public String getValue() {
    	return value;
    }
    public int getLineNumber() {
    	return lineNumber;
    }
    public int getCharPos() {
    	return charPosition;
    }
}

