import utils.errorhandling.CompilerError;
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


    private CompilerError initializeErrorMessage(Symbol token, LexicalUnit expectedType, ErrorType errorType) {
        if (expectedType == null) {
            return new CompilerError(
                    errorType,
                    token.getLine(),
                    token.getColumn(),
                    token.getType());
        }
        return new CompilerError(
                errorType,
                token.getLine(),
                token.getColumn(),
                token.getType(),
                expectedType.toString());
    }

    private CompilerError createErrorMessage(Symbol token, LexicalUnit expectedType) {
        if (token.getType() == LexicalUnit.LPAREN || token.getType() == LexicalUnit.RPAREN) {
            return this.initializeErrorMessage(token, expectedType, ErrorType.SYNTAX_ERROR_EXTRA_PAR);
        }

        if (token.getType() == LexicalUnit.MINUS) {
            return this.initializeErrorMessage(token, expectedType, ErrorType.SYNTAX_ERROR_MINUS);
        }

        if (token.getType() == LexicalUnit.PLUS) {
            return this.initializeErrorMessage(token, expectedType, ErrorType.SYNTAX_ERROR_SUM);
        }
        if (token.getType() == LexicalUnit.DIVIDE) {
            return this.initializeErrorMessage(token, expectedType, ErrorType.SYNTAX_ERROR_DIVIDE);
        }

        if (token.getType() == LexicalUnit.TIMES) {
            return this.initializeErrorMessage(token, expectedType, ErrorType.SYNTAX_ERROR_MULT);
        }

        return new CompilerError(
                ErrorType.SYNTAX_ERROR_UNEXPECTED_CHAR,
                token.getLine(),
                token.getColumn(),
                token.getValue());

    }

    private void stopExecutionAndNotifyUser(Symbol token, LexicalUnit expectedType) {
        this.shouldContinue = false;
        this.error = createErrorMessage(token, expectedType);
    }

    private void match(LexicalUnit expectedType, LexicalUnit currentType) {
        if (this.shouldContinue) {
            if (expectedType != currentType) {
                this.shouldContinue = false;
                Symbol token = this.getToken();
                this.error = createErrorMessage(token, expectedType);
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
            this.match(LexicalUnit.ENDPROG, this.getToken().getType());
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
                    this.idTail();
                    break;
                default:
                    stopExecutionAndNotifyUser(token, null);
            }
        }
    }

    private void idTail() {
        if (this.shouldContinue) {
            Symbol token = this.getToken();
            switch (token.getType()) {
                case VARNAME:
                    this.match(LexicalUnit.VARNAME, this.getToken().getType());
                    return;
                case NUMBER:
                    this.match(LexicalUnit.NUMBER, this.getToken().getType());
                    return;
                default:
                    stopExecutionAndNotifyUser(token, null);
            }
        }
    }

    private void variables() {
        if (this.shouldContinue) {
            Symbol token = this.getToken();
            switch (token.getType()) {
                case ENDPROG:
                case VARNAME:
                case IF:
                case WHILE:
                case FOR:
                case PRINT:
                case READ:
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
            this.varListTail();
        }
    }

    private void varListTail() {
        if (this.shouldContinue) {
            Symbol token = this.getToken();
            switch (token.getType()) {
                case ENDLINE:
                case RPAREN:
                    return;
            }
            this.match(LexicalUnit.COMMA, this.getToken().getType());
            this.varList();
        }
    }

    private void code() {
        if (this.shouldContinue) {
            Symbol token = this.getToken();
            switch (token.getType()) {
                case ENDPROG:
                case ENDWHILE:
                case ENDIF:
                case ELSE:
                case ENDFOR:
                    return;
            }

            this.instruction();
            this.skipEndLines();
            this.code();
        }
    }

    private void instruction() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case READ:
                    this.read();
                    return;
                case VARNAME:
                    this.assign();
                    return;
                case IF:
                    this.ifVariable();
                    return;
                case WHILE:
                    this.whileVariable();
                    return;
                case FOR:
                    this.forVariable();
                    return;
                case PRINT:
                    this.print();
                    return;
                default:
                    this.stopExecutionAndNotifyUser(this.getToken(), null);
            }

        }
    }

    private void whileVariable() {
        if (this.shouldContinue) {
            this.match(LexicalUnit.WHILE, this.getToken().getType());
            this.match(LexicalUnit.LPAREN, this.getToken().getType());
            this.cond();
            this.match(LexicalUnit.RPAREN, this.getToken().getType());
            this.match(LexicalUnit.DO, this.getToken().getType());
            this.skipEndLines();
            this.code();
            this.match(LexicalUnit.ENDWHILE, this.getToken().getType());
        }
    }

    private void forVariable() {
        if (this.shouldContinue) {
            this.match(LexicalUnit.FOR, this.getToken().getType());
            this.match(LexicalUnit.VARNAME, this.getToken().getType());
            this.match(LexicalUnit.ASSIGN, this.getToken().getType());
            this.exprArith();
            this.match(LexicalUnit.TO, this.getToken().getType());
            this.exprArith();
            this.match(LexicalUnit.DO, this.getToken().getType());
            this.skipEndLines();
            this.code();
            this.match(LexicalUnit.FOR, this.getToken().getType());
        }
    }

    private void print() {
        if (this.shouldContinue) {
            this.match(LexicalUnit.PRINT, this.getToken().getType());
            this.match(LexicalUnit.LPAREN, this.getToken().getType());
            this.expList();
            this.match(LexicalUnit.RPAREN, this.getToken().getType());
        }
    }

    private void expList() {
        if (this.shouldContinue) {
            this.exprArith();
            this.expListTail();
        }
    }

    private void expListTail() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case RPAREN:
                    return;
            }
            this.match(LexicalUnit.COMMA, this.getToken().getType());
            this.expList();
        }
    }

    private void ifVariable() {
        if (this.shouldContinue) {
            this.match(LexicalUnit.IF, this.getToken().getType());
            this.match(LexicalUnit.LPAREN, this.getToken().getType());
            this.cond();
            this.match(LexicalUnit.RPAREN, this.getToken().getType());
            this.match(LexicalUnit.THEN, this.getToken().getType());
            this.skipEndLines();
            this.code();
            this.ifTail();
        }
    }

    private void ifTail() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case ENDIF:
                    this.match(LexicalUnit.ENDIF, this.getToken().getType());
                    return;
                case ELSE:
                    this.match(LexicalUnit.ELSE, this.getToken().getType());
                    this.skipEndLines();
                    this.code();
                    this.match(LexicalUnit.ENDIF, this.getToken().getType());
                    return;
                default:
                    this.stopExecutionAndNotifyUser(this.getToken(), null);
            }
        }
    }

    private void cond() {
        if (this.shouldContinue) {
            this.condAnd();
            this.condA();
        }
    }

    private void condA() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case RPAREN:
                    return;
            }
            this.match(LexicalUnit.OR, this.getToken().getType());
            this.condAnd();
            this.condA();
        }
    }

    private void condAnd() {
        if (this.shouldContinue) {
            this.condFinal();
            this.condAndA();
        }
    }

    private void condFinal() {
        if (this.shouldContinue) {
            if (this.getToken().getType() == LexicalUnit.NOT) {
                this.match(LexicalUnit.NOT, this.getToken().getType());
                this.simpleCond();
            } else {
                this.simpleCond();
            }

        }
    }

    private void simpleCond() {
        if (this.shouldContinue) {
            this.exprArith();
            this.comp();
            this.exprArith();
        }
    }

    private void comp() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case EQ:
                    this.match(LexicalUnit.EQ, this.getToken().getType());
                    return;
                case GEQ:
                    this.match(LexicalUnit.GEQ, this.getToken().getType());
                    return;
                case GT:
                    this.match(LexicalUnit.GT, this.getToken().getType());
                    return;
                case LEQ:
                    this.match(LexicalUnit.LEQ, this.getToken().getType());
                    return;
                case LT:
                    this.match(LexicalUnit.LT, this.getToken().getType());
                    return;
                case NEQ:
                    this.match(LexicalUnit.NEQ, this.getToken().getType());
                    return;
                default:
                    this.stopExecutionAndNotifyUser(this.getToken(), null);
            }
        }
    }

    private void condAndA() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case RPAREN:
                case OR:
                    return;
            }
            this.match(LexicalUnit.AND, this.getToken().getType());
            this.condFinal();
            this.condAndA();
        }
    }

    private void assign() {
        if (this.shouldContinue) {
            this.match(LexicalUnit.VARNAME, this.getToken().getType());
            this.match(LexicalUnit.ASSIGN, this.getToken().getType());
            this.exprArith();
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

    private void exprArith() {
        if (this.shouldContinue) {
            this.exprMult();
            this.exprArithA();
        }
    }

    private void exprArithA() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case ENDLINE:
                case RPAREN:
                case EQ:
                case GEQ:
                case GT:
                case LEQ:
                case LT:
                case NEQ:
                case DO:
                case TO:
                case COMMA:
                    return;
                case PLUS:
                    this.match(LexicalUnit.PLUS, this.getToken().getType());
                    this.exprMult();
                    this.exprArithA();
                    return;
                case MINUS:
                    this.match(LexicalUnit.MINUS, this.getToken().getType());
                    this.exprMult();
                    this.exprArithA();
                    return;
            }
        }
    }

    private void exprMult() {
        if (this.shouldContinue) {
            this.id();
            this.exprMultA();
        }
    }

    private void exprMultA() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case ENDLINE:
                case PLUS:
                case MINUS:
                case RPAREN:
                case EQ:
                case GEQ:
                case GT:
                case LEQ:
                case LT:
                case NEQ:
                case DO:
                case TO:
                case COMMA:
                    return;
                case TIMES:
                    this.match(LexicalUnit.TIMES, this.getToken().getType());
                    this.id();
                    this.exprMultA();
                    return;
                case DIVIDE:
                    this.match(LexicalUnit.DIVIDE, this.getToken().getType());
                    this.id();
                    this.exprMultA();
                    return;

            }

        }
    }

    public void run() {
        this.program();
    }

    public boolean isSyntaxCorrect() {
        return this.shouldContinue;
    }

    public CompilerError getError() {
        return this.error;
    }

    private void skipToken() {
        this.tokenIndex++;
    }

    private void skipEndLines() {

        while (this.getToken().getType() == LexicalUnit.ENDLINE){
            this.skipToken();
        }
    }

}
