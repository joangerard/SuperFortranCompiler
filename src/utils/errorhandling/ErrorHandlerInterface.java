package utils.errorhandling;

import java.util.List;
/**
 * Responsible to handling errors.
 */
public interface ErrorHandlerInterface
{
    /**
     * Responsible to add an error.
     * 
     * @param error     Type of error
     * @param line      Line of the error.
     * @param column    Column of the error.
     * @param value     Value that triggered the value.
     */
    public void addError(ErrorType error, int line, int column, Object value);

    /**
     * Responsible to return a list of errors.
     *
     * @return          List of errors.
     */
    public List getErrors();
}
