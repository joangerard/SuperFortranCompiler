import java.util.Map;
import java.util.TreeMap;

public class MapOrder implements ProcessInterface
{
    public Map execute(Map map) {
        return new TreeMap<String, Integer>(map);
    }
}
