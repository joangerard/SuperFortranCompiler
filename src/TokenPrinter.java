import java.util.List;
/**
 * Responsible to show information related to the token.
 *
 */
public class TokenPrinter implements PrinterInterface
{
    TokenizerInterface tokenizer;
	/**
	 * @param tokenizer offers a list of tokens to be printed.
	 */
    public TokenPrinter(TokenizerInterface tokenizer)
    {
        this.tokenizer = tokenizer;
    }

    /**
     * {@inheritDoc}
     */
    public void print() {
        List tokens = this.tokenizer.getTokens();

        // print in standard output all the tokens.
        for (int i = 0; i < tokens.size(); i++) 
        {
            Symbol symbol = (Symbol)tokens.get(i);
            System.out.println(symbol.toString());
        }
    }
}
