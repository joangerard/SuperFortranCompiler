import java.util.Map;

public interface IdentifierListInterface
{
    public void add(String varName, int lineNumber);
    public Map<String,Integer> get();
}
