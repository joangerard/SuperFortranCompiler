import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Responsible to handler argument options.
 */
public class ArgumentHandler {
    private List<String> arguments;

    /**
     * Constructor.
     * @param args String[]
     */
    public ArgumentHandler(String[] args){
        this.arguments = new ArrayList<String>();
        arguments.addAll(Arrays.asList(args));
    }

    /**
     * Responsible to verify if the -t option was introduced.
     *
     * @return Boolean
     */
    public Boolean showTokens() {
        return this.arguments.contains("-t");
    }

    /**
     * Responsible to verify if the -i option was introduced.
     *
     * @return Boolean
     */
    public Boolean showIdentifiers() {
        return this.arguments.contains("-i");
    }

    /**
     * Responsible to verify if the -v or --verbose option was introduced.
     *
     * @return Boolean
     */
    public Boolean showVerbose() {
        return this.arguments.contains("-v")|| this.arguments.contains("--verbose");
    }

    /**
     * Responsible to verify if the -wt option was introduced.
     *
     * @return Boolean
     */
    public Boolean shouldWriteParseTreeText() {
        return this.arguments.contains("-wt");
    }

    /**
     * Responsible to verify if the -ast option was introduced.
     *
     * @return Boolean
     */
    public Boolean shouldWriteASTText() {
        return this.arguments.contains("-ast");
    }

    /**
     * Responsible to verify if the -h option was introduced.
     *
     * @return Boolean
     */
    public Boolean shouldShowHelp() {
        return this.arguments.contains("-h") || this.arguments.contains("--help");
    }

    /**
     * Responsible to verify if a .tex file was introduced.
     *
     * @return String filename.
     */
    public String giveTexFile() {
        for (String argument :
                this.arguments) {
            if (argument.contains(".tex")) {
                return argument;
            }
        }
        return "";
    }

    /**
     * Responsible to verify if a .sf file was introduced.
     *
     * @return String filename.
     */
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
