package utils.errorhandling;
/**
 * Responsible to handle compile error information.
 */
public class CompilerError
{
    ErrorType errorType;
    int line;
    int column;
    Object value;

    /**
     * Constructor.
     * 
     * @param errorType What kind of error is it? Program name, Variable Name, etc.
     * @param line      Error line in the code.
     * @param column    Error column in the code.
     * @param value     Value of the input which triggers the error.
     */
    public CompilerError(ErrorType errorType, int line, int column, Object value)
    {
        this.errorType = errorType;
        this.line = line+1;
        this.column = column;
        this.value = value;
    }

    /**
     * @return Error type
     */
    public ErrorType getErrorType()
    {
        return this.errorType;
    }

    /**
     * @return Error line
     */
    public int getLine()
    {
        return this.line;
    }

    /**
     * @return Error column
     */
    public int getColumn()
    {
        return this.column;
    }

    /**
     * @return Value of the input which triggers error.
     */
    public Object getValue()
    {
        return this.value;
    }

    /**
     * @return Error message based on the error type.
     */
    public String getErrorMessage()
    {
        switch(this.getErrorType()) {
            case SYNTAX_ERROR_VARNAME:
                return "SYNTAX ERROR VARNAME: "+this.value.toString()+" is not well defined. Please check line: "+this.line+" column: "+ this.column;
             case SYNTAX_ERROR_NUMBER:
                return "SYNTAX ERROR NUMBER: "+this.value.toString()+" is not well defined. Please check line: "+this.line+" column: "+ this.column;
            case SYNTAX_ERROR_PROGNAME:
                return "SYNTAX ERROR PROGNAME: "+this.value.toString()+" is not well defined. Please check line: "+this.line+" column: "+ this.column;
            default:
                return "";
        }
    }
}
