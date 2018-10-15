import java.util.HashMap;
import java.util.Map;

public class IdentifierList implements IdentifierListInterface{

    private Map<String, Integer> identifierList; // HashMap list to store all identifiers
    private ProcessInterface process;

    /**
     * Constructor
     * @param process Map order class that implements ProcessInterface
     */
    public IdentifierList(ProcessInterface process) {
        this.identifierList = new HashMap<>();
        this.process = process;
    }

    /**
     * Adds variable and line number to identifier list Map as long as the variable name does not exists in the list
     * @param varName Variable name
     * @param lineNumber Line number in which the variable appears for the first time
     */
    public void add(String varName, int lineNumber) {
        if (this.identifierList.get(varName) == null) {
            this.identifierList.put(varName, lineNumber);
        }
    }

    /**
     * Returns identifier list sorted in ascending order
     */
    public Map<String,Integer> get() {
        return this.process.execute(this.identifierList);
    }

    /**
     *
     * Returns a string with all identifiers form the list
     */
    @Override
    public String toString() {
        String value = "";
        this.identifierList = get();

        for (Map.Entry<String,Integer> entry : this.identifierList.entrySet()) {
            value += entry.getKey() + "\t" + entry.getValue() + "\n";
        }

        return value;
    }
}
