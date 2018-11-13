import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible to handle a token list.
 */
public class Tokenizer implements TokenizerInterface
{
    private List<Symbol> tokens;

    /**
     * {@inheritDoc}
     */
    public Tokenizer()
    {
        this.tokens = new ArrayList<Symbol>();
    }

    /**
     * {@inheritDoc}
     */
    public void addToken(LexicalUnit unit,int line,int column,Object value)
    {
        Symbol symbol = new Symbol(unit, line, column, value);
        this.tokens.add(symbol);
    }

    /**
     * {@inheritDoc}
     */
    public List<Symbol> getTokens()
    {
        return this.tokens;
    }
}
