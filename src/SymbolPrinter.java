public class SymbolPrinter implements PrinterInterface
{
    public void print(LexicalUnit lexicalUnit, int line, int column, String value) {
        Symbol symbol = new Symbol(lexicalUnit, line, column, value);
        System.out.println(symbol.toString());
    }
}
