import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType {
	
    private HashMap<String, InterpreterDataType> elements;

    public InterpreterArrayDataType() {
        
    	super(); 
    
        this.elements = new HashMap<>();
    }

    public HashMap<String, InterpreterDataType> getElements() {
    	
        return elements;
    }

    public void setElements(HashMap<String, InterpreterDataType> elements) {
    	
        this.elements = elements;
    }

    public void addElement(String key, InterpreterDataType value) {
    	
        elements.put(key, value);
    }

    public InterpreterDataType getElement(String key) {
    	
        return elements.get(key);
    }
    

    public boolean containsElement(String key) {
    	
        return elements.containsKey(key);
    }
    

    public void removeElement(String key) {
    	
        elements.remove(key);
    }
    public int size() {
        return elements.size();
    }
}