package utils.errorhandling;

import java.util.List;

public class ErrorPrinter
{
    ErrorHandlerInterface errorHandler;

    public ErrorPrinter(ErrorHandlerInterface errorHandler)
    {
        this.errorHandler = errorHandler;
    }

    public void print()
    {
        List errors = this.errorHandler.getErrors();
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
