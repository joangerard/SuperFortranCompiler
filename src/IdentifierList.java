import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for listing identifiers
 */
public class IdentifierList implements IdentifierListInterface{

    private Map<String, Integer> identifierList; // HashMap list to store all identifiers
    private MapOrderInterface mapOrder;

    /**
     *
     * @param mapOrder Map order object that implements ProcessInterface
     */
    public IdentifierList(MapOrderInterface mapOrder) {
        this.identifierList = new HashMap<String, Integer>();
        this.mapOrder = mapOrder;
    }

    /**
     * Adds variable and line number to identifier list Map as long as the variable name does not exists in the list
     * @param varName The corresponding variable name to be added to the list
     * @param lineNumber The corresponding line number in which the variable appears for the first time
     */
    public void add(String varName, int lineNumber) {
        if (this.identifierList.get(varName) == null) {
            this.identifierList.put(varName, lineNumber);
        }
    }

    /**
     * Returns identifier list sorted in ascending order
     * @return list of identifiers
     */
    public Map<String,Integer> get() {
        return this.mapOrder.execute(this.identifierList);
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
