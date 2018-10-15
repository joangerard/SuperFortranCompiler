import java.util.List;

public class TokenPrinter implements PrinterInterface
{
    TokenizerInterface tokenizer;

    public TokenPrinter(TokenizerInterface tokenizer)
    {
        this.tokenizer = tokenizer;
    }

    public void print() {
        List tokens = this.tokenizer.getTokens();

        for (int i = 0; i < tokens.size(); i++) 
        {
            Symbol symbol = (Symbol)tokens.get(i);
            System.out.println(symbol.toString());
        }
    }
}
