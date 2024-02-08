
public class ReturnType {
enum returnType{
	NORMAL,
	BREAK,
	CONTINUE,
	RETURN
}
public returnType type;
public String value;
public ReturnType(returnType type) {
	
	this.type = type;
}
public ReturnType(returnType type, String value) {
	
	this.type=type;
	
	this.value=value;	
}
public returnType getReturn() {
	return type;
}
public String toString() 
{
	return "Return value: " + value + " The return type: " + type;
	
}
public String getValue() {
	return value;
}
}
