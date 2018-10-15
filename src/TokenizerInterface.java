import java.util.List;

/**
 * Responsible to handle tokens.
 * */
public interface TokenizerInterface
{
	/**
     * Add a token into the list.
     * 
     * @param unit 		The Corresponding lexical unit to be added
     * @param line		The line in which the lexical unit was found.
     * @param column	The column in which the lexical unit was found.
     * @param value		The value that it has. Ex. PRINT, (, ), -, 5, etc.
     * */
    public void addToken(LexicalUnit unit,int line,int column,Object value);
    
    /**
     * @return			List of tokens.
     */
    public List getTokens();
}
