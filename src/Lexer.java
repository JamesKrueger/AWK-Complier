

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Lexer {
	
	List<Token> tokens = new LinkedList<>();//List of my tokens
	
    private StringHandler stringHandler;
    
    private int lineNumber;
    
    private int charPosition;
    
    private static final Map<String, Token.TokenType> KEYWORDS;
    
    private static final Map<String, Token.TokenType>DoubleSymbol;
    
    private static final Map<String, Token.TokenType>OneSymbol;
    

    static //All my symbols and KeyWords below 
    {
        KEYWORDS = new HashMap<>();
        KEYWORDS.put("if", Token.TokenType.IF);
        KEYWORDS.put("else", Token.TokenType.ELSE);
        KEYWORDS.put("do", Token.TokenType.DO);
        KEYWORDS.put("for", Token.TokenType.FOR);
        KEYWORDS.put("while", Token.TokenType.WHILE);
        KEYWORDS.put("END", Token.TokenType.END);
        KEYWORDS.put("return", Token.TokenType.RETURN);
        KEYWORDS.put("break", Token.TokenType.BREAK);
        KEYWORDS.put("continue", Token.TokenType.CONTINUE);
        KEYWORDS.put("print", Token.TokenType.PRINT);
        KEYWORDS.put("printf", Token.TokenType.PRINTF);
        KEYWORDS.put("next", Token.TokenType.NEXT);
        KEYWORDS.put("length", Token.TokenType.LENGTH);
        KEYWORDS.put("function", Token.TokenType.FUNCTION);
        KEYWORDS.put("delete", Token.TokenType.DEL);
        KEYWORDS.put("in", Token.TokenType.IN);
        KEYWORDS.put("getline", Token.TokenType.GETLINE);
        KEYWORDS.put("exit", Token.TokenType.EXIT);
        KEYWORDS.put("nextfile", Token.TokenType.NEXTFILE);
        KEYWORDS.put("BEGIN", Token.TokenType.BEGIN);
        KEYWORDS.put("break2", Token.TokenType.BREAK2);
               
        
        DoubleSymbol = new HashMap<>();
    	DoubleSymbol.put(">=", Token.TokenType.GREATEREQUAL);
    	DoubleSymbol.put("++", Token.TokenType.DOUBLEADD);
    	DoubleSymbol.put("--", Token.TokenType.DOUBLESUB);
    	DoubleSymbol.put("<=", Token.TokenType.LESSEQUAL);
    	DoubleSymbol.put("!=", Token.TokenType.FACTEQUAL);
    	DoubleSymbol.put("^=", Token.TokenType.ARROWEQUAL);
    	DoubleSymbol.put("%=", Token.TokenType.PERCENTEQUAL);
    	DoubleSymbol.put("/=", Token.TokenType.DIVIDEEQUAL);
    	DoubleSymbol.put("*=", Token.TokenType.TIMESEQUAL);
    	DoubleSymbol.put("-=", Token.TokenType.SUBEQUAL);
    	DoubleSymbol.put(">>", Token.TokenType.DOUBLEARROW);
    	DoubleSymbol.put("!~", Token.TokenType.FACTSWINGLY);
    	DoubleSymbol.put("&&", Token.TokenType.DOUBLEAND);
    	DoubleSymbol.put("||", Token.TokenType.DOUBLEOR);
    	DoubleSymbol.put("==", Token.TokenType.DOUBLEEQUAL);
    	DoubleSymbol.put("+=", Token.TokenType.ADDEQUAL);

    	
    	OneSymbol = new HashMap<>();
    	OneSymbol.put("{", Token.TokenType.LEFTCURLYBRA);
    	OneSymbol.put("}", Token.TokenType.RIGHTCURLYBRA);
    	OneSymbol.put("(", Token.TokenType.LEFTPAR);
    	OneSymbol.put(")", Token.TokenType.RIGHTPAR);
    	OneSymbol.put(",", Token.TokenType.COMMA);
    	OneSymbol.put("+", Token.TokenType.ADD);
    	OneSymbol.put("=", Token.TokenType.EQUAL);
    	OneSymbol.put("-", Token.TokenType.SUB);
    	OneSymbol.put("?", Token.TokenType.QUESTIONMARK);
    	OneSymbol.put("]", Token.TokenType.RIGHTBRA);
    	OneSymbol.put("[", Token.TokenType.LEFTBRA);
    	OneSymbol.put("<", Token.TokenType.LEFTARROW);
    	OneSymbol.put(">", Token.TokenType.RIGHTARROW);
    	OneSymbol.put("!", Token.TokenType.EXCLIMATIONPOINT);
    	OneSymbol.put("^", Token.TokenType.UPARROW);
    	OneSymbol.put(":", Token.TokenType.COLON);
    	OneSymbol.put("*", Token.TokenType.TIMES);
    	OneSymbol.put("/", Token.TokenType.BACKSLASH);
    	OneSymbol.put("%", Token.TokenType.PERCENT);
    	OneSymbol.put("|", Token.TokenType.BAR);
    	OneSymbol.put("$", Token.TokenType.MONEYSIGN);
    	OneSymbol.put("~", Token.TokenType.SWINGLY);
    	OneSymbol.put(";", Token.TokenType.SEPARATOR);

    	
    	
    	
        
    }
    
    public Lexer(String input) 
    {
    	
        this.stringHandler = new StringHandler(input);
      
        this.lineNumber = 1;
        
        this.charPosition = 0;
    }
    
//Lex method
    public List<Token> lex() {
        
        while (!stringHandler.IsDone()) //While my string is not done
        
        { 
            char currentChar = stringHandler.Peek(0);//CurrentChar is where my finger is pointing
           
            if (currentChar == ' ' || currentChar == '\t') 
            {
            	//Consume tabs and white space but track our charc pos
            	
                
            	stringHandler.Swallow(1);//Take it in. 
         
                charPosition++;
            } 
            else if (currentChar == '\n' || currentChar == ';') {
                //New line or ";" create separator token increase line number but our char pos =0.
            	//New line so we restart our char pos
            	
               tokens.add(new Token(Token.TokenType.SEPARATOR, lineNumber, charPosition));
                
                stringHandler.Swallow(1);
            
                lineNumber++;
                
                charPosition = 0;
      
            } 
            else if (currentChar == '\r')
            {
                stringHandler.Swallow(1);
                //Ignore
            } 
            else if (Character.isLetter(currentChar)) {
            	//If letter go to word method and add token to list
                Token word = ProcessWord();
                tokens.add(word);
            } 
            else if (Character.isDigit(currentChar))
            	
            {
            	//If there is a digit we go to process number
                Token number = ProcessNumber();
                tokens.add(number);
            }
            else if (currentChar == '"') {
            	//If String Literal go to my string literal method. Add token to list.
                Token StringLit = StringLit();
                tokens.add(StringLit);
            } else if (currentChar == '`') {
            	//If we encounter a ` go to pattern method and add to list of tokens
                Token patternToken = HandlePattern();
                tokens.add(patternToken);
            } 
            else if (currentChar == '#') {
                //Skip over comment lines by looping to the end of the line
                while (!stringHandler.IsDone() && stringHandler.Peek(0) != '\n') {
                    stringHandler.Swallow(1);
                    lineNumber++;
            }
            } 
             else {//Should be a symbol so got to Symbol method
                Token symbol = ProcessSymbol();
                if (symbol != null) {
                    tokens.add(symbol);
                } else {//We dont know what it is. 
                    throw new IllegalArgumentException("Unrecognized character: " + currentChar);
                }
            }
        }

        

        return tokens;
    }


    //My method for words.
    private Token ProcessWord() {
    	
        StringBuilder value = new StringBuilder();


        while (!stringHandler.IsDone() && 
        		
        (Character.isLetterOrDigit(stringHandler.Peek(0)) 
        		|| stringHandler.Peek(0) == '_')) //Peeks in for a letter or digit or _ for it to be a word
        	
        	
        {
        
        	value.append(stringHandler.GetChar());//Gets the character
            
        	charPosition++;
        	
        }

        String word = value.toString();
        //Check if the word is a keyword
        Token.TokenType tokenType = KEYWORDS.getOrDefault(word, Token.TokenType.WORD);
        return new Token(tokenType, value.toString(), charPosition, lineNumber);
    }


	private Token ProcessNumber() 
	{
	    StringBuilder value = new StringBuilder();


	    // Count the number of decimal points
	    int decimalCount = 0;

	    while (!stringHandler.IsDone() &&
	            (Character.isDigit(stringHandler.Peek(0)) || stringHandler.Peek(0) == '.')) 
	    {
	    	//While string is not done, look into to see if charc is digit or point. 
	        
	    	char currentChar = stringHandler.Peek(0);//assign currentChar to my where finger is pointing

	        if (currentChar == '.') //Check for deci point. Increase counter. 
	        	{
	            decimalCount++;
	            
	            if (decimalCount > 1) {
	            	
	            	throw new IllegalArgumentException("Invalid: No more than 1: "  + currentChar); 
	            	
	            	//Stop processing after the second decimal
	            }
	        }

	        value.append(currentChar);
	        stringHandler.Swallow(1);
	        charPosition++;
	    }

	    //Handle the case of only a single dot
	    
	    return new Token(Token.TokenType.NUMBER, value.toString(), charPosition, lineNumber);
	}

    //My symbol method.
    private Token ProcessSymbol() {
    	
        int remainingChars = stringHandler.Remainder().length();//Gets substring stores length 
        
        if (remainingChars >= 2) {//If substring length >2
            String twoCharSymbol = stringHandler.PeekString(2);//Peek into the next 2 charc
            if (DoubleSymbol.containsKey(twoCharSymbol)) {//If contains keys from hashmap
                stringHandler.Swallow(2);//Swallow
                charPosition += 2;//Move 2 charac pos
                return new Token(DoubleSymbol.get(twoCharSymbol), twoCharSymbol, charPosition-2, lineNumber);
                //Make the token. Charpos-2 due to the fact we making the token based off of two pos ago.
                
            }
        }

        if (remainingChars >= 1) {
        	//If substring length is >1
            String oneCharSymbol = stringHandler.PeekString(1);//Peek into the next character 
            if (OneSymbol.containsKey(oneCharSymbol)) {//If contains keys from hashmap
                stringHandler.Swallow(1);
                charPosition++;//Move once
                return new Token(OneSymbol.get(oneCharSymbol), oneCharSymbol, charPosition-1, lineNumber);
            }                //Make the token. Charpos-1 due to the fact we making the token based off of one pos ago.

        }

        return null; // No recognized symbol found
    }
    private Token HandlePattern() {
    	
        StringBuilder value = new StringBuilder();

        stringHandler.Swallow(1); //Take first character in

        char currentChar = 0;

        while (!stringHandler.IsDone()) {
        
        	currentChar = stringHandler.GetChar();//Gets chara
            
        	charPosition++;

            if (currentChar == '`') {
                // End of the pattern
                  break;
          } else {
                value.append(currentChar);//Append the string inside
            }
        }

        if (currentChar != '`') {//Check for closing backtick
            throw new IllegalArgumentException("Unclosed backtick in pattern");
        }

        return new Token(Token.TokenType.PATTERN, value.toString(), charPosition, lineNumber);
    }


    private Token StringLit() {
        StringBuilder value = new StringBuilder();

        stringHandler.Swallow(1); //Take first opening quotes
        
        char currentChar = 0 ;

        while (!stringHandler.IsDone()) {
            currentChar = stringHandler.GetChar();//Get Chara
            charPosition++;

            if (currentChar == '\\') {
                //Handle escape sequences
                if (stringHandler.IsDone()) {
                    throw new IllegalArgumentException("Unexpected end of input after escape character");
                }
                char escapedChar = stringHandler.GetChar();
                charPosition++;
                switch (escapedChar) {
                    case 'n':
                        value.append('\n');
                        break;
                    case 't':
                        value.append('\t');
                        break;
                    case '\\':
                        value.append('\\');
                        break;
                    case '"':
                        value.append('"');
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid escape sequence: \\" + escapedChar);
                }
            } else if (currentChar == '"') {
                // End of the string literal
                break;
            } else {
                value.append(currentChar);
            }
        }

        if (currentChar != '"') {//Check for closing quotes
            throw new IllegalArgumentException("Unclosed double quote in string literal");
        }

        // Create a STRINGLITERAL token
        return new Token(Token.TokenType.STRINGLIT, value.toString(), charPosition, lineNumber);
    }





    
    
    	
    
}