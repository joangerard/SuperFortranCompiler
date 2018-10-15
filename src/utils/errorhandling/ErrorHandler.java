package utils.errorhandling;

import java.util.List;
import java.util.ArrayList;

public class ErrorHandler implements ErrorHandlerInterface
{
    List errors;

    public ErrorHandler()
    {
        this.errors = new ArrayList();
    }

    public void addError(ErrorList errorType, int line, int column, Object value)
    {
        CompilerError error = new CompilerError(errorType, line, column, value);
        this.errors.add(error);
    }

    public List getErrors()
    {
        return this.errors;
    }
}
