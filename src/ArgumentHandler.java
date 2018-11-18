import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentHandler {
    private List<String> arguments;

    public ArgumentHandler(String[] args){
        this.arguments = new ArrayList<String>();
        arguments.addAll(Arrays.asList(args));
    }

    public Boolean showTokens() {
        return this.arguments.contains("-t");
    }

    public Boolean showIdentifiers() {
        return this.arguments.contains("-i");
    }

    public Boolean showVerbose() {
        return this.arguments.contains("-v")|| this.arguments.contains("--verbose");
    }

    public Boolean shouldWriteParseTreeText() {
        return this.arguments.contains("-wt");
    }

    public Boolean shouldShowHelp() {
        return this.arguments.contains("-h") || this.arguments.contains("--help");
    }

    public String giveTexFile() {
        for (String argument :
                this.arguments) {
            if (argument.contains(".tex")) {
                return argument;
            }
        }
        return "";
    }

    public String giveSuperFortranFile() {
        for (String argument :
                this.arguments) {
            if (argument.contains(".sf")) {
                return argument;
            }
        }
        return "";
    }
}
