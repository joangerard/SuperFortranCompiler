import utils.errorhandling.CompilerError;
import utils.errorhandling.ErrorType;

import java.util.ArrayList;
import java.util.List;

public class SyntaxChecker {

    private List<Symbol> tokens;
    private int tokenIndex;
    private ParseTree parseTree;
    private Boolean shouldContinue;
    private CompilerError error;
    private List<String> lines;
    private List<Rule> derivationRules;

    public SyntaxChecker(List<Symbol> tokens, List<String> lines) {
        this.tokens = tokens;
        this.tokenIndex = 0;
        this.shouldContinue = true;
        this.error = new CompilerError();
        this.lines = lines;
        this.derivationRules = new ArrayList<Rule>();
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
            this.tokenIndex++;
        }
    }

    private void program() {
        if (this.shouldContinue) {
            derivationRules.add(new Rule(1, "Program", this.getToken()));
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
                    this.derivationRules.add(new Rule(24, "Id",  this.getToken()));
                    this.match(LexicalUnit.VARNAME, this.getToken().getType());
                    break;
                case NUMBER:
                    this.derivationRules.add(new Rule(25, "Id",  this.getToken()));
                    this.match(LexicalUnit.NUMBER, this.getToken().getType());
                    break;
                case LPAREN:
                    this.derivationRules.add(new Rule(26, "Id",  this.getToken()));
                    this.match(LexicalUnit.LPAREN, this.getToken().getType());
                    this.exprArith();
                    this.match(LexicalUnit.RPAREN, this.getToken().getType());
                    break;
                case MINUS:
                    this.derivationRules.add(new Rule(27, "Id",  this.getToken()));
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
                    this.derivationRules.add(new Rule(28, "IdTail",  this.getToken()));
                    this.match(LexicalUnit.VARNAME, this.getToken().getType());
                    return;
                case NUMBER:
                    this.derivationRules.add(new Rule(29, "IdTail",  this.getToken()));
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
                    this.derivationRules.add(new Rule(3, "Variables",  this.getToken()));
                    return;
            }
            this.derivationRules.add(new Rule(2, "Variables",  this.getToken()));
            this.match(LexicalUnit.VARIABLES, this.getToken().getType());
            this.varList();
            this.skipEndLines();
        }

    }

    private void varList() {
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(4, "VarList",  this.getToken()));
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
                    this.derivationRules.add(new Rule(6, "Variables",  this.getToken()));
                    return;
            }
            this.derivationRules.add(new Rule(5, "VarListTail",  this.getToken()));
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
                    this.derivationRules.add(new Rule(8, "Code",  this.getToken()));
                    return;
            }

            this.derivationRules.add(new Rule(7, "Code",  this.getToken()));
            this.instruction();
            this.skipEndLines();
            this.code();
        }
    }

    private void instruction() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case READ:
                    this.derivationRules.add(new Rule(14, "Instruction",  this.getToken()));
                    this.read();
                    return;
                case VARNAME:
                    this.derivationRules.add(new Rule(9, "Instruction",  this.getToken()));
                    this.assign();
                    return;
                case IF:
                    this.derivationRules.add(new Rule(10, "Instruction",  this.getToken()));
                    this.ifVariable();
                    return;
                case WHILE:
                    this.derivationRules.add(new Rule(11, "Instruction",  this.getToken()));
                    this.whileVariable();
                    return;
                case FOR:
                    this.derivationRules.add(new Rule(12, "Instruction",  this.getToken()));
                    this.forVariable();
                    return;
                case PRINT:
                    this.derivationRules.add(new Rule(13, "Instruction",  this.getToken()));
                    this.print();
                    return;
                default:
                    this.stopExecutionAndNotifyUser(this.getToken(), null);
            }

        }
    }

    private void whileVariable() {
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(48, "While",  this.getToken()));
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
            this.derivationRules.add(new Rule(49, "For",  this.getToken()));
            this.match(LexicalUnit.FOR, this.getToken().getType());
            this.match(LexicalUnit.VARNAME, this.getToken().getType());
            this.match(LexicalUnit.ASSIGN, this.getToken().getType());
            this.exprArith();
            this.match(LexicalUnit.TO, this.getToken().getType());
            this.exprArith();
            this.match(LexicalUnit.DO, this.getToken().getType());
            this.skipEndLines();
            this.code();
            this.match(LexicalUnit.ENDFOR, this.getToken().getType());
        }
    }

    private void print() {
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(50, "Print",  this.getToken()));
            this.match(LexicalUnit.PRINT, this.getToken().getType());
            this.match(LexicalUnit.LPAREN, this.getToken().getType());
            this.expList();
            this.match(LexicalUnit.RPAREN, this.getToken().getType());
        }
    }

    private void expList() {
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(52, "ExpList",  this.getToken()));
            this.exprArith();
            this.expListTail();
        }
    }

    private void expListTail() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case RPAREN:
                    this.derivationRules.add(new Rule(54, "ExpListTail",  this.getToken()));
                    return;
            }
            this.derivationRules.add(new Rule(53, "ExpListTail",  this.getToken()));
            this.match(LexicalUnit.COMMA, this.getToken().getType());
            this.expList();
        }
    }

    private void ifVariable() {
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(30, "If",  this.getToken()));
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
                    this.derivationRules.add(new Rule(31, "IfTail",  this.getToken()));
                    this.match(LexicalUnit.ENDIF, this.getToken().getType());
                    return;
                case ELSE:
                    this.derivationRules.add(new Rule(32, "IfTail",  this.getToken()));
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
            this.derivationRules.add(new Rule(33, "Cond",  this.getToken()));
            this.condAnd();
            this.condA();
        }
    }

    private void condA() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case RPAREN:
                    this.derivationRules.add(new Rule(35, "CondA",  this.getToken()));
                    return;
            }
            this.derivationRules.add(new Rule(34, "Or",  this.getToken()));
            this.match(LexicalUnit.OR, this.getToken().getType());
            this.condAnd();
            this.condA();
        }
    }

    private void condAnd() {
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(36, "CondAnd",  this.getToken()));
            this.condFinal();
            this.condAndA();
        }
    }

    private void condFinal() {
        if (this.shouldContinue) {
            if (this.getToken().getType() == LexicalUnit.NOT) {
                this.derivationRules.add(new Rule(39, "CondFinal",  this.getToken()));
                this.match(LexicalUnit.NOT, this.getToken().getType());
                this.simpleCond();
            } else {
                this.derivationRules.add(new Rule(40, "CondFinal",  this.getToken()));
                this.simpleCond();
            }

        }
    }

    private void simpleCond() {
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(41, "SimpleCond",  this.getToken()));
            this.exprArith();
            this.comp();
            this.exprArith();
        }
    }

    private void comp() {
        if (this.shouldContinue) {
            switch (this.getToken().getType()) {
                case EQ:
                    this.derivationRules.add(new Rule(42, "Comp",  this.getToken()));
                    this.match(LexicalUnit.EQ, this.getToken().getType());
                    return;
                case GEQ:
                    this.derivationRules.add(new Rule(43, "Comp",  this.getToken()));
                    this.match(LexicalUnit.GEQ, this.getToken().getType());
                    return;
                case GT:
                    this.derivationRules.add(new Rule(44, "Comp",  this.getToken()));
                    this.match(LexicalUnit.GT, this.getToken().getType());
                    return;
                case LEQ:
                    this.derivationRules.add(new Rule(45, "Comp",  this.getToken()));
                    this.match(LexicalUnit.LEQ, this.getToken().getType());
                    return;
                case LT:
                    this.derivationRules.add(new Rule(46, "Comp",  this.getToken()));
                    this.match(LexicalUnit.LT, this.getToken().getType());
                    return;
                case NEQ:
                    this.derivationRules.add(new Rule(47, "Comp",  this.getToken()));
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
                    this.derivationRules.add(new Rule(38, "CondAndA",  this.getToken()));
                    return;
            }
            this.derivationRules.add(new Rule(37, "CondAndA",  this.getToken()));
            this.match(LexicalUnit.AND, this.getToken().getType());
            this.condFinal();
            this.condAndA();
        }
    }

    private void assign() {
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(15, "Assign",  this.getToken()));
            this.match(LexicalUnit.VARNAME, this.getToken().getType());
            this.match(LexicalUnit.ASSIGN, this.getToken().getType());
            this.exprArith();
        }
    }

    private void read() {
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(51, "Read",  this.getToken()));
            this.match(LexicalUnit.READ, this.getToken().getType());
            this.match(LexicalUnit.LPAREN, this.getToken().getType());
            this.varList();
            this.match(LexicalUnit.RPAREN, this.getToken().getType());
        }
    }

    private void exprArith() {
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(16, "ExprArith",  this.getToken()));
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
                    this.derivationRules.add(new Rule(19, "ExprArithA",  this.getToken()));
                    return;
                case PLUS:
                    this.derivationRules.add(new Rule(17, "ExprArithA",  this.getToken()));
                    this.match(LexicalUnit.PLUS, this.getToken().getType());
                    this.exprMult();
                    this.exprArithA();
                    return;
                case MINUS:
                    this.derivationRules.add(new Rule(18, "ExprArithA",  this.getToken()));
                    this.match(LexicalUnit.MINUS, this.getToken().getType());
                    this.exprMult();
                    this.exprArithA();
                    return;
            }
        }
    }

    private void exprMult() {
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(20, "ExprMult",  this.getToken()));
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
                    this.derivationRules.add(new Rule(23, "ExprMultA",  this.getToken()));
                    return;
                case TIMES:
                    this.derivationRules.add(new Rule(21, "ExprMultA",  this.getToken()));
                    this.match(LexicalUnit.TIMES, this.getToken().getType());
                    this.id();
                    this.exprMultA();
                    return;
                case DIVIDE:
                    this.derivationRules.add(new Rule(22, "ExprMultA",  this.getToken()));
                    this.match(LexicalUnit.DIVIDE, this.getToken().getType());
                    this.id();
                    this.exprMultA();
                    return;

            }

        }
    }

    public void showDerivationRules() {
        for (Rule rule: derivationRules) {
            System.out.println(rule.toString());
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

        while (this.shouldSkipTokens()) {
            this.skipToken();
        }
    }

    private boolean shouldSkipTokens() {
        return this.shouldContinue && this.tokenIndex < this.tokens.size() && this.getToken().getType() == LexicalUnit.ENDLINE;
    }

}
