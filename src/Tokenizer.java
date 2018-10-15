import java.util.ArrayList;
import java.util.List;

public class Tokenizer implements TokenizerInterface
{
    List tokens;

    public Tokenizer()
    {
        this.tokens = new ArrayList();
    }

    public void addToken(LexicalUnit unit,int line,int column,Object value)
    {
        Symbol symbol = new Symbol(unit, line, column, value);
        this.tokens.add(symbol);
    }

    public List getTokens()
    {
        return this.tokens;
    }
}
