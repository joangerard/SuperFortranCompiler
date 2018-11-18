package utils.errorhandling;

/**
 * Responsible to handle compile error information.
 */
public class CompilerError {
    private ErrorType errorType;
    private int line;
    private int column;
    private Object value;
    private String expectedValue;

    public CompilerError() {

    }

    /**
     * Constructor.
     *
     * @param errorType What kind of error is it? Program name, Variable Name, etc.
     * @param line      Error line in the code.
     * @param column    Error column in the code.
     * @param value     Value of the input which triggers the error.
     */
    public CompilerError(ErrorType errorType, int line, int column, Object value) {
        this.errorType = errorType;
        this.line = line + 1;
        this.column = column;
        this.value = value;
        this.expectedValue = "";
    }

    /**
     * Constructor.
     *
     * @param errorType What kind of error is it? Program name, Variable Name, etc.
     * @param line      Error line in the code.
     * @param column    Error column in the code.
     * @param value     Value of the input which triggers the error.
     */
    public CompilerError(ErrorType errorType, int line, int column, Object value, String expectedValue) {
        this.errorType = errorType;
        this.line = line + 1;
        this.column = column;
        this.value = value;
        this.expectedValue = expectedValue;
    }

    /**
     * @return Error type
     */
    public ErrorType getErrorType() {
        return this.errorType;
    }

    /**
     * @return Error line
     */
    public int getLine() {
        return this.line;
    }

    /**
     * @return Error column
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * @return Value of the input which triggers error.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * @return Error message based on the error type.
     */
    public String getErrorMessage() {
        switch (this.getErrorType()) {
            case SYNTAX_ERROR_VARNAME:
                return "SYNTAX ERROR VARNAME: " + this.value.toString() + " is not well defined. Please check line: " + this.line + " column: " + this.column;
            case SYNTAX_ERROR_NUMBER:
                return "SYNTAX ERROR NUMBER: " + this.value.toString() + " is not well defined. Please check line: " + this.line + " column: " + this.column;
            case SYNTAX_ERROR_PROGNAME:
                return "SYNTAX ERROR PROGNAME: " + this.value.toString() + " is not well defined. Please check line: " + this.line + " column: " + this.column;
            case SYNTAX_ERROR_NOT_RECOGNIZED_CHARACTER:
                return "SYNTAX ERROR NOT RECOGNIZED CHAR: " + this.value.toString() + " not recognized. Please check line: " + this.line + " column: " + this.column;
            case SYNTAX_ERROR:
                if (this.value.toString() != "\n") {
                    return "SYNTAX ERROR near " + "line " + (this.line - 1) + ", column " + this.column + ". Got an end of line instead of " + this.expectedValue + ". Please remove end of line.";
                }
                else if (this.value.toString() != "EOS") {
                    return "SYNTAX ERROR near " + "line " + (this.line - 1) + ", column " + this.column + ". Got { " +this.value.toString() + " } instead of " + this.expectedValue + ".";
                } else {
                    return "SYNTAX ERROR near " + "line " + (this.line - 1) + ", column " + this.column + " expected " + this.expectedValue + ".";
                }
            case SYNTAX_ERROR_END_LINE:
                return "SYNTAX ERROR near " + "line " + (this.line - 1) + ", column " + this.column + ". Please add an end of line before {"+ this.value.toString() +"}";
            case SYNTAX_ERROR_EXTRA_PAR:
                return "SYNTAX ERROR near line " + (this.line - 1) + ", column " + this.column + ". Extra parenthesis.";
            case SYNTAX_ERROR_MULT:
                return "SYNTAX ERROR near line " + (this.line - 1) + ", column " + this.column + ". Extra multiplication symbol.";
            case SYNTAX_ERROR_DIVIDE:
                return "SYNTAX ERROR near line " + (this.line - 1) + ", column " + this.column + ". Extra divide symbol.";
            case SYNTAX_ERROR_MINUS:
                return "SYNTAX ERROR near line " + (this.line - 1) + ", column " + this.column + ". Extra minus symbol.";
            case SYNTAX_ERROR_SUM:
                return "SYNTAX ERROR near line " + (this.line - 1) + ", column " + this.column + ". Extra plus symbol.";
            case SYNTAX_ERROR_UNEXPECTED_CHAR:
                return "SYNTAX ERROR near line " + (this.line - 1) + ", column " + this.column + ". Unexpected value {" + this.value +"}.";
            default:
                return "";
        }
    }
}
