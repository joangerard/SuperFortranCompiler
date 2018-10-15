package utils.errorhandling;

public class CompilerError
{
    ErrorList errorType;
    int line;
    int column;
    Object value;

    public CompilerError(ErrorList errorType, int line, int column, Object value)
    {
        this.errorType = errorType;
        this.line = line+1;
        this.column = column;
        this.value = value;
    }

    public ErrorList getErrorType()
    {
        return this.errorType;
    }

    public int getLine()
    {
        return this.line;
    }

    public int getColumn()
    {
        return this.column;
    }

    public Object getValue()
    {
        return this.value;
    }

    public String getErrorMessage()
    {
        switch(this.getErrorType()) {
            case SYNTAX_ERROR_VARNAME:
                return "SYNTAX ERROR VARNAME: "+this.value.toString()+" is not well defined. Please check line: "+this.line+" column: "+ this.column;
             case SYNTAX_ERROR_NUMBER:
                return "SYNTAX ERROR NUMBER: "+this.value.toString()+" is not well defined. Please check line: "+this.line+" column: "+ this.column;
            case SYNTAX_ERROR_PROGNAME:
                return "SYNTAX ERROR PROGNAME: "+this.value.toString()+" is not well defined. Please check line: "+this.line+" column: "+ this.column;
        }
        return "";
    }
}
