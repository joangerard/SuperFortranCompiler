import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for listing identifiers
 */
public class IdentifierList implements IdentifierListInterface{

    private Map<String, Integer> identifierList; // HashMap list to store all identifiers
    private ProcessInterface process;

    /**
     *
     * @param process Map order object that implements ProcessInterface
     */
    public IdentifierList(ProcessInterface process) {
        this.identifierList = new HashMap<String, Integer>();
        this.process = process;
    }

    /**
     * {@inheritDoc}
     */
    public void add(String varName, int lineNumber) {
        if (this.identifierList.get(varName) == null) {
            this.identifierList.put(varName, lineNumber);
        }
    }

    /**
     * {@inheritDoc}
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
        StringBuilder value = new StringBuilder();
        this.identifierList = get();

        for (Map.Entry<String,Integer> entry : this.identifierList.entrySet()) {
            value.append(entry.getKey()).append("\t").append(entry.getValue()).append("\n");
        }

        return value.toString();
    }
}
