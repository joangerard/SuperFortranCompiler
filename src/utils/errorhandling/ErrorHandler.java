package utils.errorhandling;

import java.util.List;
import java.util.ArrayList;

/**
 * Responsible to handle errors.
 */
public class ErrorHandler implements ErrorHandlerInterface
{
    List errors;

    public ErrorHandler()
    {
        this.errors = new ArrayList();
    }

    /**
     * {@inheritDoc}
     */
    public void addError(ErrorType errorType, int line, int column, Object value)
    {
        CompilerError error = new CompilerError(errorType, line, column, value);
        this.errors.add(error);
    }

    /**
     * {@inheritDoc}
     */
    public List getErrors()
    {
        return this.errors;
    }
}
