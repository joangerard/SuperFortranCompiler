import java.util.Map;

/**
 * Responsible for ordering Map objects
 */
public interface MapOrderInterface
{
    /**
     * Executes a process
     * @param value Map object to be sorted
     * @return Map Object
     */
    public Map<String,Integer> execute(Map<String,Integer> value);
}
