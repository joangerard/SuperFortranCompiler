import utils.errorhandling.CompilerError;
import utils.errorhandling.ErrorHandlerInterface;
import utils.errorhandling.ErrorType;

import java.util.List;

public class SyntaxChecker {

    private List<Symbol> tokens;
    private int tokenIndex;
    private ParseTree parseTree;
    private Boolean shouldContinue;
    private CompilerError error;

    public SyntaxChecker(List<Symbol> tokens) {
        this.tokens = tokens;
        this.tokenIndex = 0;
        this.shouldContinue = true;
        this.error = new CompilerError();
    }

    private Symbol getToken() {
        return this.tokens.get(this.tokenIndex);
    }

    private void match(LexicalUnit expectedType, LexicalUnit currentType) {
        if (this.shouldContinue) {
            if (expectedType != currentType) {
                this.shouldContinue = false;
                Symbol token = this.getToken();
                this.error = new CompilerError(
                        ErrorType.SYNTAX_ERROR,
                        token.getLine(),
                        token.getColumn(),
                        token.getType(),
                        expectedType.toString());
            }
        }
        this.tokenIndex++;
    }

    private void program() {
        if (this.shouldContinue) {
            this.match(LexicalUnit.BEGINPROG, this.getToken().getType());
            this.match(LexicalUnit.PROGNAME, this.getToken().getType());
            this.skipEndLines();
            this.variables();
            this.code();
            this.match(LexicalUnit.BEGINPROG, this.getToken().getType());
        }

    }

    private void id() {

        if (this.shouldContinue) {
            Symbol token = this.getToken();

            switch (token.getType()) {
                case VARNAME:
                    this.match(LexicalUnit.VARNAME, this.getToken().getType());
                    break;
                case NUMBER:
                    this.match(LexicalUnit.NUMBER, this.getToken().getType());
                    break;
                case LPAREN:
                    this.match(LexicalUnit.LPAREN, this.getToken().getType());
                    this.exprArith();
                    this.match(LexicalUnit.RPAREN, this.getToken().getType());
                    break;
                case MINUS:
                    this.match(LexicalUnit.MINUS, this.getToken().getType());
                    id();
                    break;
            }
        }
    }

    private void variables() {
        if (this.shouldContinue) {
            Symbol token = this.getToken();
            switch (token.getType()) {
                case ENDPROG:
                    this.match(LexicalUnit.ENDPROG, this.getToken().getType());
                    return;
                case VARNAME:
                    this.match(LexicalUnit.VARNAME, this.getToken().getType());
                    return;
                case IF:
                    this.match(LexicalUnit.IF, this.getToken().getType());
                    return;
                case WHILE:
                    this.match(LexicalUnit.WHILE, this.getToken().getType());
                    return;
                case FOR:
                    this.match(LexicalUnit.FOR, this.getToken().getType());
                    return;
                case PRINT:
                    this.match(LexicalUnit.PRINT, this.getToken().getType());
                    return;
                case READ:
                    this.match(LexicalUnit.READ, this.getToken().getType());
                    return;
            }
            this.match(LexicalUnit.VARIABLES, this.getToken().getType());
            this.varList();
            this.skipEndLines();
        }

    }

    private void varList() {
        if (this.shouldContinue) {
            this.match(LexicalUnit.VARNAME, this.getToken().getType());
            this.match(LexicalUnit.COMMA, this.getToken().getType());
            this.varListTail();
        }
    }

    private void varListTail() {
        if (this.shouldContinue) {
            Symbol token = this.getToken();
            if (token.getType() == LexicalUnit.ENDLINE) {
                this.skipEndLines();
                return;
            }
            this.varList();
        }
    }

    private void code() {
        if (this.shouldContinue) {
            Symbol token = this.getToken();
            switch (token.getType()) {
                case ENDPROG:
                    this.match(LexicalUnit.ENDPROG, this.getToken().getType());
                    return;
                case ENDWHILE:
                    this.match(LexicalUnit.ENDWHILE, this.getToken().getType());
                    return;
                case ENDIF:
                    this.match(LexicalUnit.ENDIF, this.getToken().getType());
                    return;
                case ELSE:
                    this.match(LexicalUnit.ELSE, this.getToken().getType());
                    return;
                case ENDFOR:
                    this.match(LexicalUnit.ENDFOR, this.getToken().getType());
                    return;
            }

            this.instruction();
            this.skipEndLines();
            this.code();
        }
    }

    private void instruction() {
        if (this.shouldContinue) {
            this.read();
        }
    }

    private void read() {
        if (this.shouldContinue) {
            this.match(LexicalUnit.READ, this.getToken().getType());
            this.match(LexicalUnit.LPAREN, this.getToken().getType());
            this.varList();
            this.match(LexicalUnit.RPAREN, this.getToken().getType());
        }
    }


    //TODO
    private void exprMultA() {
        Symbol token = this.getToken();

    }

    private ParseTree exprArith() {
        return null;

    }

    public void run() {
        this.program();
    }

    public boolean isSyntaxCorrect(){
        return this.shouldContinue;
    }

    public CompilerError getError(){
        return this.error;
    }

    private void skipToken(){
        this.tokenIndex++;
    }

    private void skipEndLines(){

        while (this.getToken().getType() == LexicalUnit.ENDLINE){
            this.skipToken();
        }
    }

}
