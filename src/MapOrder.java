import java.util.Map;
import java.util.TreeMap;

public class MapOrder implements ProcessInterface
{
    public Map<String,Integer> execute(Map<String,Integer> map) {
        return new TreeMap<String, Integer>(map);
    }
}
