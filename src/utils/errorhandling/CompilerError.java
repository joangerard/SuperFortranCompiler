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
    private String errorLine;

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
        this.errorLine = "";
    }

    /**
     * Constructor.
     *
     * @param errorType What kind of error is it? Program name, Variable Name, etc.
     * @param line      Error line in the code.
     * @param column    Error column in the code.
     * @param value     Value of the input which triggers the error.
     */
    public CompilerError(ErrorType errorType, int line, int column, Object value, String expectedValue, String errorLine) {
        this.errorType = errorType;
        this.line = line + 1;
        this.column = column;
        this.value = value;
        this.expectedValue = expectedValue;
        this.errorLine = errorLine;
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
                return getSyntaxErrorMessage();
            case SYNTAX_ERROR_END_LINE:
                return getLineErrorDescription(getLineColumnInfo() + ". Please add an end of line before {"+ this.value.toString() +"}");
            default:
                return "";
        }
    }

    /**
     * Get syntax error message.
     *
     * @return String
     */
    private String getSyntaxErrorMessage() {
        String errorMessage = "";
        if (this.value.toString().contains("\\n")) {
            errorMessage = getLineColumnInfo() + ". Got an end of line instead of { " + this.expectedValue + " }. Please remove end of line.";
        }
        else if (this.value.toString() != "EOS") {
            errorMessage = getLineColumnInfo() + ". Got { " +this.value.toString() + " } instead of { " + this.expectedValue + " }.";
        } else {
            errorMessage = getLineColumnInfo() + " expected { " + this.expectedValue + " }.";
        }

        errorMessage = getLineErrorDescription(errorMessage);

        return errorMessage;
    }

    private String getLineErrorDescription(String errorMessage) {
        if (!this.errorLine.equals("")) {
            errorMessage += "\n\r";
            errorMessage += "\n\r" + this.errorLine;
            errorMessage += "\n\r" + new String(new char[this.column]).replace("\0", " ") + "^";
        }
        return errorMessage;
    }

    /**
     * Get line column error information.
     *
     * @return String
     */
    private String getLineColumnInfo() {
        return "SYNTAX ERROR near " + "line " + (this.line - 1) + ", column " + this.column;
    }
}
