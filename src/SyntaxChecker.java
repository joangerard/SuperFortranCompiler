import utils.errorhandling.CompilerError;
import utils.errorhandling.ErrorType;

import java.util.ArrayList;
import java.util.List;

public class SyntaxChecker {

    private List<Symbol> tokens;
    private int tokenIndex;
    private Boolean shouldContinue;
    private CompilerError error;
    private List<String> lines;
    private List<Rule> derivationRules;
    private final String EPSILON = "EPSILON";
    private final String PROGRAM = "PROGRAM";
    private final String VARIABLES = "VARIABLES";
    private final String VAR_LIST = "VAR_LIST";
    private final String VAR_LIST_TAIL = "VAR_LIST_TAIL";
    private final String CODE = "CODE";
    private final String INSTRUCTION = "INSTRUCTION";
    private final String ASSIGN = "ASSIGN";
    private final String EXPR_ARITH = "EXPR_ARITH";
    private final String EXPR_ARITH_A = "EXPR_ARITH_A";
    private final String EXPR_MULT = "EXPR_MULT";
    private final String EXPR_MULT_A = "EXPR_MULT_A";
    private final String ID = "ID";
    private final String ID_TAIL = "ID_TAIL";
    private final String IF = "IF";
    private final String IF_TAIL = "IF_TAIL";
    private final String COND = "COND";
    private final String COND_A = "COND_A";
    private final String COND_AND = "COND_AND";
    private final String COND_AND_A = "COND_AND_A";
    private final String COND_FINAL = "COND_FINAL";
    private final String SIMPLE_COND = "SIMPLE_COND";
    private final String COMP = "COMP";
    private final String WHILE = "WHILE";
    private final String FOR = "FOR";
    private final String PRINT = "PRINT";
    private final String READ = "READ";
    private final String EXP_LIST = "EXP_LIST";
    private final String EXP_LIST_TAIL = "EXP_LIST_TAIL";

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

    private ParseTree matchTree(LexicalUnit expectedType, LexicalUnit currentType) {
        ParseTree matchTree = null;
        if (this.shouldContinue) {
            if (expectedType != currentType) {
                this.shouldContinue = false;
                Symbol token = this.getToken();
                this.error = createErrorMessage(token, expectedType);
            }
            matchTree = new ParseTree(this.getToken());
            this.tokenIndex++;
        }
        return matchTree;
    }

    private ParseTree program() {
        ParseTree programTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            derivationRules.add(new Rule(1, "Program", this.getToken()));
            ParseTree beginProgTree = this.matchTree(LexicalUnit.BEGINPROG, this.getToken().getType());
            ParseTree progNameTree = this.matchTree(LexicalUnit.PROGNAME, this.getToken().getType());
            this.skipEndLines();
            ParseTree variableTree = this.variables();
            ParseTree codeTree = this.code();
            ParseTree endProgTree = this.matchTree(LexicalUnit.ENDPROG, this.getToken().getType());
            children.add(beginProgTree);
            children.add(progNameTree);
            children.add(variableTree);
            children.add(codeTree);
            children.add(endProgTree);
            programTree = new ParseTree(new Symbol(null, PROGRAM), children);
        }
        return programTree;
    }

    private ParseTree id() {
        ParseTree idTree = null;

        if (this.shouldContinue) {
            Symbol token = this.getToken();
            List<ParseTree> children = new ArrayList<ParseTree>();

            switch (token.getType()) {
                case VARNAME:
                    this.derivationRules.add(new Rule(24, "Id",  this.getToken()));
                    ParseTree varNameTree = this.matchTree(LexicalUnit.VARNAME, this.getToken().getType());
                    children.add(varNameTree);
                    break;
                case NUMBER:
                    this.derivationRules.add(new Rule(25, "Id",  this.getToken()));
                    ParseTree numberTree = this.matchTree(LexicalUnit.NUMBER, this.getToken().getType());
                    children.add(numberTree);
                    break;
                case LPAREN:
                    this.derivationRules.add(new Rule(26, "Id",  this.getToken()));
                    ParseTree lParenTree = this.matchTree(LexicalUnit.LPAREN, this.getToken().getType());
                    ParseTree exprArithTree = this.exprArith();
                    ParseTree rParenTree = this.matchTree(LexicalUnit.RPAREN, this.getToken().getType());
                    children.add(lParenTree);
                    children.add(exprArithTree);
                    children.add(rParenTree);
                    break;
                case MINUS:
                    this.derivationRules.add(new Rule(27, "Id",  this.getToken()));
                    ParseTree minusTree = this.matchTree(LexicalUnit.MINUS, this.getToken().getType());
                    ParseTree idTailTree = this.idTail();
                    children.add(minusTree);
                    children.add(idTailTree);
                    break;
                default:
                    stopExecutionAndNotifyUser(token, null);
            }
            idTree = new ParseTree(new Symbol(null, ID), children);

        }
        return idTree;
    }

    private ParseTree idTail() {
        if (this.shouldContinue) {
            Symbol token = this.getToken();
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (token.getType()) {
                case VARNAME:
                    this.derivationRules.add(new Rule(28, "IdTail",  this.getToken()));
                    ParseTree varNameTree = this.matchTree(LexicalUnit.VARNAME, this.getToken().getType());
                    children.add(varNameTree);
                    return new ParseTree(new Symbol(null, ID_TAIL), children);
                case NUMBER:
                    this.derivationRules.add(new Rule(29, "IdTail",  this.getToken()));
                    ParseTree numberTree = this.matchTree(LexicalUnit.NUMBER, this.getToken().getType());
                    children.add(numberTree);
                    return new ParseTree(new Symbol(null, ID_TAIL), children);
                default:
                    stopExecutionAndNotifyUser(token, null);
            }

        }
        return null;
    }

    private ParseTree variables() {
        ParseTree variablesTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
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
                    return new ParseTree(new Symbol(null, EPSILON));
            }
            this.derivationRules.add(new Rule(2, "Variables",  this.getToken()));
            ParseTree variablesTree1 = this.matchTree(LexicalUnit.VARIABLES, this.getToken().getType());
            ParseTree varListTree1 = this.varList();
            children.add(variablesTree1);
            children.add(varListTree1);
            this.skipEndLines();
            variablesTree = new ParseTree(new Symbol(null, VARIABLES), children);
        }
        return variablesTree;
    }

    private ParseTree varList() {
        ParseTree varListTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(4, "VarList",  this.getToken()));
            ParseTree varNameTree = this.matchTree(LexicalUnit.VARNAME, this.getToken().getType());
            ParseTree varListTailTree = this.varListTail();
            children.add(varNameTree);
            children.add(varListTailTree);
            varListTree = new ParseTree(new Symbol(null, VAR_LIST), children);
        }
        return varListTree;
    }

    private ParseTree varListTail() {
        ParseTree varListTailTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            Symbol token = this.getToken();
            switch (token.getType()) {
                case ENDLINE:
                case RPAREN:
                    this.derivationRules.add(new Rule(6, "Variables",  this.getToken()));
                    return new ParseTree(new Symbol(null, EPSILON));
            }
            this.derivationRules.add(new Rule(5, "VarListTail",  this.getToken()));
            ParseTree comaTree = this.matchTree(LexicalUnit.COMMA, this.getToken().getType());
            ParseTree varListTree = this.varList();
            children.add(comaTree);
            children.add(varListTree);
            varListTailTree = new ParseTree(new Symbol(null, VAR_LIST_TAIL), children);
        }
        return varListTailTree;
    }

    private ParseTree code() {
        ParseTree codeTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            Symbol token = this.getToken();
            switch (token.getType()) {
                case ENDPROG:
                case ENDWHILE:
                case ENDIF:
                case ELSE:
                case ENDFOR:
                    this.derivationRules.add(new Rule(8, "Code",  this.getToken()));
                    return new ParseTree(new Symbol(null, EPSILON));
            }

            this.derivationRules.add(new Rule(7, "Code",  this.getToken()));
            ParseTree instruction = this.instruction();
            this.skipEndLines();
            ParseTree codeTree1 = this.code();
            children.add(instruction);
            children.add(codeTree1);
            codeTree = new ParseTree(new Symbol(null, CODE), children);
        }
        return codeTree;
    }

    private ParseTree instruction() {
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (this.getToken().getType()) {
                case READ:
                    this.derivationRules.add(new Rule(14, "Instruction",  this.getToken()));
                    ParseTree readTree = this.read();
                    children.add(readTree);
                    return new ParseTree(new Symbol(null, READ), children);
                case VARNAME:
                    this.derivationRules.add(new Rule(9, "Instruction",  this.getToken()));
                    ParseTree assign = this.assign();
                    children.add(assign);
                    return new ParseTree(new Symbol(null, ASSIGN), children);
                case IF:
                    this.derivationRules.add(new Rule(10, "Instruction",  this.getToken()));
                    ParseTree ifTree = this.ifVariable();
                    children.add(ifTree);
                    return new ParseTree(new Symbol(null, IF), children);
                case WHILE:
                    this.derivationRules.add(new Rule(11, "Instruction",  this.getToken()));
                    ParseTree whileTree = this.whileVariable();
                    children.add(whileTree);
                    return new ParseTree(new Symbol(null, WHILE), children);
                case FOR:
                    this.derivationRules.add(new Rule(12, "Instruction",  this.getToken()));
                    ParseTree forTree = this.forVariable();
                    children.add(forTree);
                    return new ParseTree(new Symbol(null, FOR), children);
                case PRINT:
                    this.derivationRules.add(new Rule(13, "Instruction",  this.getToken()));
                    ParseTree printTree = this.print();
                    children.add(printTree);
                    return new ParseTree(new Symbol(null, PRINT), children);
                default:
                    this.stopExecutionAndNotifyUser(this.getToken(), null);
            }

        }
        return null;
    }

    private ParseTree whileVariable() {
        ParseTree whileVariableTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(48, "While",  this.getToken()));
            ParseTree whileTree = this.matchTree(LexicalUnit.WHILE, this.getToken().getType());
            ParseTree lparenTree = this.matchTree(LexicalUnit.LPAREN, this.getToken().getType());
            ParseTree condTree = this.cond();
            ParseTree rparenTree = this.matchTree(LexicalUnit.RPAREN, this.getToken().getType());
            ParseTree doTree = this.matchTree(LexicalUnit.DO, this.getToken().getType());
            this.skipEndLines();
            ParseTree codeTree = this.code();
            ParseTree endWhileTree = this.matchTree(LexicalUnit.ENDWHILE, this.getToken().getType());
            children.add(whileTree);
            children.add(lparenTree);
            children.add(condTree);
            children.add(rparenTree);
            children.add(doTree);
            children.add(codeTree);
            children.add(endWhileTree);
            whileVariableTree = new ParseTree(new Symbol(null, WHILE), children);
        }
        return whileVariableTree;
    }

    private ParseTree forVariable() {
        ParseTree forTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(49, "For",  this.getToken()));
            ParseTree forTree1 = this.matchTree(LexicalUnit.FOR, this.getToken().getType());
            ParseTree varNameTree = this.matchTree(LexicalUnit.VARNAME, this.getToken().getType());
            ParseTree assignTree = this.matchTree(LexicalUnit.ASSIGN, this.getToken().getType());
            ParseTree exprArithTree = this.exprArith();
            ParseTree toTree = this.matchTree(LexicalUnit.TO, this.getToken().getType());
            ParseTree exprArithTree1 = this.exprArith();
            ParseTree doTree = this.matchTree(LexicalUnit.DO, this.getToken().getType());
            this.skipEndLines();
            ParseTree codeTree = this.code();
            ParseTree endForTree = this.matchTree(LexicalUnit.ENDFOR, this.getToken().getType());
            children.add(forTree1);
            children.add(varNameTree);
            children.add(assignTree);
            children.add(exprArithTree);
            children.add(toTree);
            children.add(exprArithTree1);
            children.add(doTree);
            children.add(codeTree);
            children.add(endForTree);
            forTree = new ParseTree(new Symbol(null, FOR), children);
        }
        return forTree;
    }

    private ParseTree print() {
        ParseTree printTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(50, "Print",  this.getToken()));
            ParseTree printTree1 = this.matchTree(LexicalUnit.PRINT, this.getToken().getType());
            ParseTree lParenTree = this.matchTree(LexicalUnit.LPAREN, this.getToken().getType());
            ParseTree expListTree = this.expList();
            ParseTree rParenTree = this.matchTree(LexicalUnit.RPAREN, this.getToken().getType());
            children.add(printTree1);
            children.add(lParenTree);
            children.add(expListTree);
            children.add(rParenTree);
            printTree = new ParseTree(new Symbol(null, PRINT), children);
        }
        return printTree;
    }

    private ParseTree expList() {
        ParseTree expListTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(52, "ExpList",  this.getToken()));
            ParseTree exprArithTree = this.exprArith();
            ParseTree expListTailTree = this.expListTail();
            children.add(exprArithTree);
            children.add(expListTailTree);
            expListTree = new ParseTree(new Symbol(null, EXP_LIST), children);
        }
        return expListTree;
    }

    private ParseTree expListTail() {
        ParseTree expListTailTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (this.getToken().getType()) {
                case RPAREN:
                    this.derivationRules.add(new Rule(54, "ExpListTail",  this.getToken()));
                    return new ParseTree(new Symbol(null, EPSILON));

            }
            this.derivationRules.add(new Rule(53, "ExpListTail",  this.getToken()));
            ParseTree commaTree = this.matchTree(LexicalUnit.COMMA, this.getToken().getType());
            ParseTree expListTree = this.expList();
            children.add(commaTree);
            children.add(expListTree);
            expListTailTree = new ParseTree(new Symbol(null, EXP_LIST_TAIL), children);
        }
        return expListTailTree;
    }

    private ParseTree ifVariable() {
        ParseTree ifTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(30, "If",  this.getToken()));
            ParseTree ifTree1 = this.matchTree(LexicalUnit.IF, this.getToken().getType());
            ParseTree lParenTree = this.matchTree(LexicalUnit.LPAREN, this.getToken().getType());
            ParseTree condTree = this.cond();
            ParseTree rParenTree = this.matchTree(LexicalUnit.RPAREN, this.getToken().getType());
            ParseTree thenTree = this.matchTree(LexicalUnit.THEN, this.getToken().getType());
            this.skipEndLines();
            ParseTree codeTree = this.code();
            ParseTree ifTailTree = this.ifTail();
            children.add(ifTree1);
            children.add(lParenTree);
            children.add(condTree);
            children.add(rParenTree);
            children.add(thenTree);
            children.add(codeTree);
            children.add(ifTailTree);
            ifTree = new ParseTree(new Symbol(null, IF), children);
        }
        return ifTree;
    }

    private ParseTree ifTail() {
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (this.getToken().getType()) {
                case ENDIF:
                    this.derivationRules.add(new Rule(31, "IfTail",  this.getToken()));
                    ParseTree endIfTree = this.matchTree(LexicalUnit.ENDIF, this.getToken().getType());
                    children.add(endIfTree);
                    return new ParseTree(new Symbol(null, IF_TAIL), children);
                case ELSE:
                    this.derivationRules.add(new Rule(32, "IfTail",  this.getToken()));
                    ParseTree elseTree = this.matchTree(LexicalUnit.ELSE, this.getToken().getType());
                    this.skipEndLines();
                    ParseTree codetree = this.code();
                    ParseTree endIfTree1 = this.matchTree(LexicalUnit.ENDIF, this.getToken().getType());
                    children.add(elseTree);
                    children.add(codetree);
                    children.add(endIfTree1);
                    return new ParseTree(new Symbol(null, IF_TAIL), children);
                default:
                    this.stopExecutionAndNotifyUser(this.getToken(), null);
            }
        }
        return null;
    }

    private ParseTree cond() {
        ParseTree condTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(33, "Cond",  this.getToken()));
            ParseTree condAndTree = this.condAnd();
            ParseTree condA = this.condA();
            children.add(condAndTree);
            children.add(condA);
            condTree = new ParseTree(new Symbol(null, COND), children);
        }
        return condTree;
    }

    private ParseTree condA() {
        ParseTree condATree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();

            switch (this.getToken().getType()) {
                case RPAREN:
                    this.derivationRules.add(new Rule(35, "CondA",  this.getToken()));
                    return new ParseTree(new Symbol(null, EPSILON));
            }
            this.derivationRules.add(new Rule(34, "Or",  this.getToken()));
            ParseTree orTree = this.matchTree(LexicalUnit.OR, this.getToken().getType());
            ParseTree condAndTree = this.condAnd();
            ParseTree condATree1 = this.condA();
            children.add(orTree);
            children.add(condAndTree);
            children.add(condATree1);
            condATree = new ParseTree(new Symbol(null, COND_A), children);
        }
        return condATree;
    }

    private ParseTree condAnd() {
        ParseTree condAndTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(36, "CondAnd",  this.getToken()));
            ParseTree condFinalTree = this.condFinal();
            ParseTree condAndATree = this.condAndA();
            children.add(condFinalTree);
            children.add(condAndATree);
            condAndTree = new ParseTree(new Symbol(null, COND_AND), children);
        }
        return condAndTree;
    }

    private ParseTree condFinal() {
        ParseTree condFinalTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            if (this.getToken().getType() == LexicalUnit.NOT) {
                this.derivationRules.add(new Rule(39, "CondFinal",  this.getToken()));
                ParseTree notTree = this.matchTree(LexicalUnit.NOT, this.getToken().getType());
                ParseTree simpleCondTree = this.simpleCond();
                children.add(notTree);
                children.add(simpleCondTree);
            } else {
                this.derivationRules.add(new Rule(40, "CondFinal",  this.getToken()));
                ParseTree simpleCondTree1 = this.simpleCond();
                children.add(simpleCondTree1);
            }
            condFinalTree = new ParseTree(new Symbol(null, COND_FINAL), children);
        }
        return condFinalTree;
    }

    private ParseTree simpleCond() {
        ParseTree simpleCondTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(41, "SimpleCond",  this.getToken()));
            ParseTree exprArithTree = this.exprArith();
            ParseTree compTree = this.comp();
            ParseTree exprArithTree1 = this.exprArith();
            children.add(exprArithTree);
            children.add(compTree);
            children.add(exprArithTree1);
            simpleCondTree = new ParseTree(new Symbol(null, SIMPLE_COND), children);
        }
        return simpleCondTree;
    }

    private ParseTree comp() {
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (this.getToken().getType()) {
                case EQ:
                    this.derivationRules.add(new Rule(42, "Comp",  this.getToken()));
                    ParseTree eqTree = this.matchTree(LexicalUnit.EQ, this.getToken().getType());
                    children.add(eqTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                case GEQ:
                    this.derivationRules.add(new Rule(43, "Comp",  this.getToken()));
                    ParseTree geqTree = this.matchTree(LexicalUnit.GEQ, this.getToken().getType());
                    children.add(geqTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                case GT:
                    this.derivationRules.add(new Rule(44, "Comp",  this.getToken()));
                    ParseTree gtTree = this.matchTree(LexicalUnit.GT, this.getToken().getType());
                    children.add(gtTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                case LEQ:
                    this.derivationRules.add(new Rule(45, "Comp",  this.getToken()));
                    ParseTree leqTree = this.matchTree(LexicalUnit.LEQ, this.getToken().getType());
                    children.add(leqTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                case LT:
                    this.derivationRules.add(new Rule(46, "Comp",  this.getToken()));
                    ParseTree ltTree = this.matchTree(LexicalUnit.LT, this.getToken().getType());
                    children.add(ltTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                case NEQ:
                    this.derivationRules.add(new Rule(47, "Comp",  this.getToken()));
                    ParseTree neqTree = this.matchTree(LexicalUnit.NEQ, this.getToken().getType());
                    children.add(neqTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                default:
                    this.stopExecutionAndNotifyUser(this.getToken(), null);
            }
        }
        return null;
    }

    private ParseTree condAndA() {
        ParseTree condAndA = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (this.getToken().getType()) {
                case RPAREN:
                case OR:
                    this.derivationRules.add(new Rule(38, "CondAndA",  this.getToken()));
                    return new ParseTree(new Symbol(null, EPSILON));
            }
            this.derivationRules.add(new Rule(37, "CondAndA",  this.getToken()));
            ParseTree andTree = this.matchTree(LexicalUnit.AND, this.getToken().getType());
            ParseTree condFinalTree = this.condFinal();
            ParseTree condAndATree = this.condAndA();
            children.add(andTree);
            children.add(condFinalTree);
            children.add(condAndATree);
            condAndA = new ParseTree(new Symbol(null, COND_AND_A), children);
        }
        return condAndA;
    }

    private ParseTree assign() {
        ParseTree assignTree = null;
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(15, "Assign",  this.getToken()));
            ParseTree varnameLeaf = this.matchTree(LexicalUnit.VARNAME, this.getToken().getType());
            ParseTree assignLeaf = this.matchTree(LexicalUnit.ASSIGN, this.getToken().getType());
            ParseTree exprArithTree = this.exprArith();
            List<ParseTree> children = new ArrayList<ParseTree>();
            children.add(varnameLeaf);
            children.add(assignLeaf);
            children.add(exprArithTree);
            assignTree = new ParseTree(new Symbol(null, ASSIGN), children);
        }
        return assignTree;
    }

    private ParseTree read() {
        ParseTree readTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(51, "Read",  this.getToken()));
            ParseTree readTree1 = this.matchTree(LexicalUnit.READ, this.getToken().getType());
            ParseTree lParenTree = this.matchTree(LexicalUnit.LPAREN, this.getToken().getType());
            ParseTree varListTree = this.varList();
            ParseTree rparenTree = this.matchTree(LexicalUnit.RPAREN, this.getToken().getType());
            children.add(readTree1);
            children.add(lParenTree);
            children.add(varListTree);
            children.add(rparenTree);
            readTree = new ParseTree(new Symbol(null, READ), children);
        }
        return readTree;
    }

    private ParseTree exprArith() {
        ParseTree exprArithTree = null;
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(16, "ExprArith",  this.getToken()));
            ParseTree exprMultTree = this.exprMult();
            ParseTree exprArithATree = this.exprArithA();
            List<ParseTree> children = new ArrayList<ParseTree>();
            children.add(exprMultTree);
            children.add(exprArithATree);
            exprArithTree = new ParseTree(new Symbol(null, EXPR_ARITH), children);
        }
        return exprArithTree;
    }

    private ParseTree exprArithA() {
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
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
                    return new ParseTree(new Symbol(null, EPSILON));
                case PLUS:
                    this.derivationRules.add(new Rule(17, "ExprArithA",  this.getToken()));
                    ParseTree plusTree = this.matchTree(LexicalUnit.PLUS, this.getToken().getType());
                    ParseTree exprMultTree = this.exprMult();
                    ParseTree exprArithA = this.exprArithA();
                    children.add(plusTree);
                    children.add(exprMultTree);
                    children.add(exprArithA);
                    return new ParseTree(new Symbol(null, EXPR_ARITH_A), children);
                case MINUS:
                    this.derivationRules.add(new Rule(18, "ExprArithA",  this.getToken()));
                    ParseTree minusTree = this.matchTree(LexicalUnit.MINUS, this.getToken().getType());
                    ParseTree exprMultTree1 = this.exprMult();
                    ParseTree exprArithATree = this.exprArithA();
                    children.add(minusTree);
                    children.add(exprMultTree1);
                    children.add(exprArithATree);
                    return new ParseTree(new Symbol(null, EXPR_ARITH_A), children);
            }
        }
        return null;
    }

    private ParseTree exprMult() {
        ParseTree exprMultTree = null;
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(20, "ExprMult",  this.getToken()));
            ParseTree idTree = this.id();
            ParseTree exprMultATree = this.exprMultA();
            List<ParseTree> children = new ArrayList<ParseTree>();
            children.add(idTree);
            children.add(exprMultATree);
            exprMultTree = new ParseTree(new Symbol(null, EXPR_MULT), children);
        }
        return exprMultTree;
    }

    private ParseTree exprMultA() {
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
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
                    return new ParseTree(new Symbol(null, EPSILON));
                case TIMES:
                    this.derivationRules.add(new Rule(21, "ExprMultA",  this.getToken()));
                    ParseTree timesTree = this.matchTree(LexicalUnit.TIMES, this.getToken().getType());
                    ParseTree idTree = this.id();
                    ParseTree exprMultATree1 = this.exprMultA();
                    children.add(timesTree);
                    children.add(idTree);
                    children.add(exprMultATree1);
                    return new ParseTree(new Symbol(null, EXPR_MULT_A), children);
                case DIVIDE:
                    this.derivationRules.add(new Rule(22, "ExprMultA",  this.getToken()));
                    ParseTree divideTree = this.matchTree(LexicalUnit.DIVIDE, this.getToken().getType());
                    ParseTree idTree1 = this.id();
                    ParseTree exprMultATree2 = this.exprMultA();
                    children.add(divideTree);
                    children.add(idTree1);
                    children.add(exprMultATree2);
                    return new ParseTree(new Symbol(null, EXPR_MULT_A), children);
            }

        }
        return null;
    }

    public void showDerivationRules() {
        for (Rule rule: derivationRules) {
            System.out.println(rule.toString());
        }
    }

    public ParseTree getParseTree() {
        return this.program();
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
