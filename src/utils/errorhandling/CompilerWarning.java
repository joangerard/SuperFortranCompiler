package utils.errorhandling;

public class CompilerWarning {
    private WarningType type;
    private int line;
    private int column;
    private Object value;

    public CompilerWarning(WarningType type, int line, int column, Object value){
        this.type = type;
        this.line = line;
        this.column = column;
        this.value = value;
    }

    public WarningType getType() {
        return type;
    }

    public void setType(WarningType type) {
        this.type = type;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
