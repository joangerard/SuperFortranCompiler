import utils.errorhandling.*;
import java.util.List;

/**
 * Responsible to print errors.
 */
public class ErrorPrinter implements PrinterInterface
{
    ErrorHandlerInterface errorHandler;

    /**
     * Constructor.
     * 
     * @param errorHandler - ErrorHandlerInterface instance.
     */
    public ErrorPrinter(ErrorHandlerInterface errorHandler)
    {
        this.errorHandler = errorHandler;
    }

    /**
     * {@inheritDoc}
     */
    public void print()
    {
        List errors = this.errorHandler.getErrors();

        // if there is at least one error print error separator.
        if (errors.size() > 0)
        {
            System.out.println("-------- Errors ---------------");
        }
        
        for(int i = 0; i < errors.size(); i++)
        {
            CompilerError error = (CompilerError)errors.get(i);
            System.out.println(error.getErrorMessage());
        }
    }
}
