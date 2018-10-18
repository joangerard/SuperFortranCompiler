import java.util.Map;

/**
 * Responsible for listing identifiers
 */
public interface IdentifierListInterface
{
    /**
     * Adds an identifier to the list
     * @param varName The corresponding variable name to be added to the list
     * @param lineNumber The corresponding line number in which the variable appears for the first time
     */
    void add(String varName, int lineNumber);

    /**
     * Returns identifier list
     */
    Map<String,Integer> get();
}
