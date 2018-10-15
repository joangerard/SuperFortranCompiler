package utils.errorhandling;

import java.util.List;

public interface ErrorHandlerInterface
{
    public void addError(ErrorList error, int line, int column, Object value);
    public List getErrors();
}
