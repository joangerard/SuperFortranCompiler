import java.util.Map;

/**
 * Responsible for ordering Map objects
 */
public interface MapOrderInterface
{
    /**
     * Returns Map object sorted in ascending order
     * @param value Map object to be sorted
     */
    public Map<String,Integer> execute(Map<String,Integer> value);
}
