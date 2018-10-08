import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class IdentifierList implements IdentifierListInterface{
    private Map<String, Integer> identifierList;
    private ProcessInterface process;

    public IdentifierList(ProcessInterface process) {
        this.identifierList = new HashMap<>();
        this.process = process;
    }

    public void add(String varName, int lineNumber) {
        if (this.identifierList.get(varName) == null) {
            this.identifierList.put(varName, lineNumber);
        }
    }

    public Map get() {
        return this.process.execute(this.identifierList);
    }

    @Override
    public String toString() {
        String value = "";
        this.identifierList = get();

        for (Map.Entry<String,Integer> entry : this.identifierList.entrySet()) {
            value += entry.getKey() + "\t" + entry.getValue() + "\n";
        }

        return value;
    }
}
