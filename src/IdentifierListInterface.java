import java.util.Map;

/**
 * Responsible for listing identifiers
 */
public interface IdentifierListInterface
{
    /**
     * Adds variable and line number to identifier list Map as long as the variable name does not exists in the list
     * @param varName The corresponding variable name to be added to the list
     * @param lineNumber The corresponding line number in which the variable appears for the first time
     */
    void add(String varName, int lineNumber);

    /**
     * Returns identifier list
     */
    Map<String,Integer> get();
}
