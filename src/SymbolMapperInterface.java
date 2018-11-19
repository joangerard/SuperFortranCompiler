/**
 * Responsible to map a lexical unit symbol into a message.
 */
public interface SymbolMapperInterface {
    /**
     * Maps a LexicalUnit into a string.
     *
     * @param symbol LexicalUnit
     * @return String
     */
    public String mapSymbolToString(LexicalUnit symbol);
}
