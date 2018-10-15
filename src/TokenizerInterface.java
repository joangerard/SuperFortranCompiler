import java.util.List;

public interface TokenizerInterface
{
    public void addToken(LexicalUnit unit,int line,int column,Object value);
    public List getTokens();
}
