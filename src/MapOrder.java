import java.util.Map;
import java.util.TreeMap;

/**
 * This class is responsible for ordering Map objects
 */
public class MapOrder implements MapOrderInterface
{
    /**
     * {@inheritDoc}
     */
    public Map<String,Integer> execute(Map<String,Integer> map) {
        return new TreeMap<String, Integer>(map);
    }
}
