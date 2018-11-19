package utils.errorhandling;

/**
 * Responsible to handle compiling warnings.
 */
public class CompilerWarning {
    private WarningType type;
    private int line;
    private int column;
    private Object value;

    /**
     * Constructor.
     * @param type      WarningType
     * @param line      int
     * @param column    int
     * @param value     Object
     */
    public CompilerWarning(WarningType type, int line, int column, Object value){
        this.type = type;
        this.line = line;
        this.column = column;
        this.value = value;
    }

    /**
     * Responsible to get type.
     *
     * @return WarningType
     */
    public WarningType getType() {
        return type;
    }

    /**
     * Set type.
     * @param type WarningType
     */
    public void setType(WarningType type) {
        this.type = type;
    }

    /**
     * Get line
     * @return int
     */
    public int getLine() {
        return line;
    }

    /**
     * Set line
     * @param line int
     */
    public void setLine(int line) {
        this.line = line;
    }

    /**
     * Get Column.
     * @return int
     */
    public int getColumn() {
        return column;
    }

    /**
     * Set column
     * @param column int
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Get Value.
     * @return Object
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set Value
     * @param value Object
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
