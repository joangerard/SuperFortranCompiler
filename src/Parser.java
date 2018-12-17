import utils.errorhandling.CompilerError;
import utils.errorhandling.ErrorType;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible to check for syntax and generating the parser tree.
 */
public class Parser {

    /**
     * List of tokens to check.
     */
    private List<Symbol> tokens;
    /**
     * It has the current token to analyze.
     */
    private int tokenIndex;

    /**
     * It is false if the program found any error.
     */
    private Boolean shouldContinue;

    /**
     * It has the error message if there was any.
     */
    private CompilerError error;

    /**
     * List of lines of code.
     */
    private List<String> lines;

    /**
     * It contains the derivation rules that were applied.
     */
    private List<Rule> derivationRules;

    /**
     * It containes the symbol mapper used for error messages.
     */
    private SymbolMapperInterface symbolMapper;

    /**
     * This constants are used for Parse tree generation.
     */
    private final String EPSILON = "EPSILON";
    private final String PROGRAM = "PROGRAM";
    private final String VARIABLES = "VARIABLES";
    private final String VAR_LIST = "VAR-LIST";
    private final String VAR_LIST_TAIL = "VAR-LIST-TAIL";
    private final String CODE = "CODE";
    private final String INSTRUCTION = "INSTRUCTION";
    private final String ASSIGN = "ASSIGN";
    private final String EXPR_ARITH = "EXPR-ARITH";
    private final String EXPR_ARITH_A = "EXPR-ARITH-A";
    private final String EXPR_MULT = "EXPR-MULT";
    private final String EXPR_MULT_A = "EXPR-MULT-A";
    private final String ID = "ID";
    private final String ID_TAIL = "ID-TAIL";
    private final String IF = "IF";
    private final String IF_TAIL = "IF-TAIL";
    private final String COND = "COND";
    private final String COND_A = "COND-A";
    private final String COND_AND = "COND-AND";
    private final String COND_AND_A = "COND-AND-A";
    private final String COND_FINAL = "COND-FINAL";
    private final String SIMPLE_COND = "SIMPLE-COND";
    private final String COMP = "COMP";
    private final String WHILE = "WHILE";
    private final String FOR = "FOR";
    private final String PRINT = "PRINT";
    private final String READ = "READ";
    private final String EXP_LIST = "EXP-LIST";
    private final String EXP_LIST_TAIL = "EXP-LIST-TAIL";

    /**
     * Constructor.
     * @param tokens        List of tokens
     * @param lines         List of lines of code
     * @param symbolMapper  SymbolMapperInterface
     */
    public Parser(List<Symbol> tokens, List<String> lines, SymbolMapperInterface symbolMapper) {
        this.tokens = tokens;
        this.tokenIndex = 0;
        this.shouldContinue = true;
        this.error = new CompilerError();
        this.lines = lines;
        this.derivationRules = new ArrayList<Rule>();
        this.symbolMapper = symbolMapper;
    }

    /**
     * Get the current token.
     * @return Symbol
     */
    private Symbol getToken() {
        return this.tokens.get(this.tokenIndex);
    }

    /**
     * It creates an error message.
     *
     * @param token         LexicalUnit
     * @param expectedType  LexicalUnit
     * @return CompilerError
     */
    private CompilerError createErrorMessage(Symbol token, LexicalUnit expectedType) {
        if (expectedType == LexicalUnit.ENDLINE){
            return new CompilerError(
                    ErrorType.SYNTAX_ERROR_END_LINE,
                    token.getLine(),
                    token.getColumn(),
                    token.getValue(),
                    "",
                    this.lines.get(token.getLine()-1));
        }
        else if (expectedType == null) {
            return new CompilerError(
                    ErrorType.SYNTAX_ERROR,
                    token.getLine(),
                    token.getColumn(),
                    token.getValue(),
                    "",
                    this.lines.get(token.getLine()-1));
        }
        return new CompilerError(
                ErrorType.SYNTAX_ERROR,
                token.getLine(),
                token.getColumn(),
                token.getValue(),
                this.symbolMapper.mapSymbolToString(expectedType),
                this.lines.get(token.getLine()-1));

    }

    /**
     * Stops Syntax Analyzer execution and returns a compilation error.
     *
     * @param token         Symbol
     * @param expectedType  LexicalUnit
     */
    private void stopExecutionAndNotifyUser(Symbol token, LexicalUnit expectedType) {
        this.shouldContinue = false;
        this.error = createErrorMessage(token, expectedType);
    }

    /**
     * It match a lexycal unit with another. If it does not match it creates a compilation error.
     *
     * @param expectedType  LexicalUnit What it is expected to receive.
     * @param currentType   LexicalUnit What it received.
     *
     * @return ParseTree
     */
    private ParseTree matchTree(LexicalUnit expectedType, LexicalUnit currentType) {
        ParseTree matchTree = null;
        if (this.shouldContinue) {
            if (expectedType != currentType) {
                this.shouldContinue = false;
                Symbol token = this.getToken();
                this.error = createErrorMessage(token, expectedType);
            } else {
                matchTree = new ParseTree(this.getToken());
                this.tokenIndex++;
            }
        }
        return matchTree;
    }

    /**
     * Production rule: program.
     *
     * @return ParseTree
     */
    private ParseTree program() {
        ParseTree programTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            derivationRules.add(new Rule(1, "BEGINPROG [ProgName][EndLine] <Variables> <Code> ENDPROG [EOS]", this.getToken()));
            ParseTree beginProgTree = this.matchTree(LexicalUnit.BEGINPROG, this.getToken().getType());
            ParseTree progNameTree = this.matchTree(LexicalUnit.PROGNAME, this.getToken().getType());
            ParseTree endLineTree = this.skipEndLines();
            ParseTree variableTree = this.variables();
            ParseTree codeTree = this.code();
            ParseTree endProgTree = this.matchTree(LexicalUnit.ENDPROG, this.getToken().getType());
            children.add(beginProgTree);
            children.add(progNameTree);
            children.add(endLineTree);
            children.add(variableTree);
            children.add(codeTree);
            children.add(endProgTree);
            programTree = new ParseTree(new Symbol(null, PROGRAM), children);
        }
        return programTree;
    }

    /**
     * Production rule: id.
     *
     * @return ParseTree
     */
    private ParseTree id() {
        ParseTree idTree = null;

        if (this.shouldContinue) {
            Symbol token = this.getToken();
            List<ParseTree> children = new ArrayList<ParseTree>();

            switch (token.getType()) {
                case VARNAME:
                    this.derivationRules.add(new Rule(24, "Id ->  [VarName]",  this.getToken()));
                    ParseTree varNameTree = this.matchTree(LexicalUnit.VARNAME, this.getToken().getType());
                    children.add(varNameTree);
                    break;
                case NUMBER:
                    this.derivationRules.add(new Rule(25, "Id -> [Number]",  this.getToken()));
                    ParseTree numberTree = this.matchTree(LexicalUnit.NUMBER, this.getToken().getType());
                    children.add(numberTree);
                    break;
                case LPAREN:
                    this.derivationRules.add(new Rule(26, "Id -> ( <ExprArith> )",  this.getToken()));
                    ParseTree lParenTree = this.matchTree(LexicalUnit.LPAREN, this.getToken().getType());
                    ParseTree exprArithTree = this.exprArith();
                    ParseTree rParenTree = this.matchTree(LexicalUnit.RPAREN, this.getToken().getType());
                    children.add(lParenTree);
                    children.add(exprArithTree);
                    children.add(rParenTree);
                    break;
                case MINUS:
                    this.derivationRules.add(new Rule(27, "Id -> − <IdTail>",  this.getToken()));
                    ParseTree minusTree = this.matchTree(LexicalUnit.MINUS, this.getToken().getType());
                    ParseTree idTailTree = this.idTail();
                    children.add(minusTree);
                    children.add(idTailTree);
                    break;
                default:
                    this.stopExecutionAndNotifyUser(token, LexicalUnit.VARNAME);
            }
            idTree = new ParseTree(new Symbol(null, ID), children);

        }
        return idTree;
    }

    /**
     * Production rule: IdTail
     *
     * @return ParseTree
     */
    private ParseTree idTail() {
        if (this.shouldContinue) {
            Symbol token = this.getToken();
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (token.getType()) {
                case VARNAME:
                    this.derivationRules.add(new Rule(28, "IdTail -> [VarName]",  this.getToken()));
                    ParseTree varNameTree = this.matchTree(LexicalUnit.VARNAME, this.getToken().getType());
                    children.add(varNameTree);
                    return new ParseTree(new Symbol(null, ID_TAIL), children);
                case NUMBER:
                    this.derivationRules.add(new Rule(29, "IdTail -> [VarNumber]",  this.getToken()));
                    ParseTree numberTree = this.matchTree(LexicalUnit.NUMBER, this.getToken().getType());
                    children.add(numberTree);
                    return new ParseTree(new Symbol(null, ID_TAIL), children);
                default:
                    this.stopExecutionAndNotifyUser(token, LexicalUnit.VARNAME);
            }

        }
        return null;
    }

    /**
     * Production rule: Variable
     *
     * @return ParseTree
     */
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
                    this.derivationRules.add(new Rule(3, "<Variables> -> EPSILON",  this.getToken()));
                    ParseTree epsilonTree = new ParseTree(new Symbol(null, EPSILON));
                    children.add(epsilonTree);
                    ParseTree parseTree = new ParseTree(new Symbol(null, VARIABLES), children);

                    return parseTree;
            }
            this.derivationRules.add(new Rule(2, "<Variables> -> VARIABLES <VarList> [EndLine]",  this.getToken()));
            ParseTree variablesTree1 = this.matchTree(LexicalUnit.VARIABLES, this.getToken().getType());
            ParseTree varListTree1 = this.varList();
            ParseTree endLineTree = this.skipEndLines();
            children.add(variablesTree1);
            children.add(varListTree1);
            children.add(endLineTree);
            variablesTree = new ParseTree(new Symbol(null, VARIABLES), children);
        }
        return variablesTree;
    }

    /**
     * Production rule: VarList
     *
     * @return ParseTree
     */
    private ParseTree varList() {
        ParseTree varListTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(4, "<VarList> -> [VarName] <VarListTail>",  this.getToken()));
            ParseTree varNameTree = this.matchTree(LexicalUnit.VARNAME, this.getToken().getType());
            ParseTree varListTailTree = this.varListTail();
            children.add(varNameTree);
            children.add(varListTailTree);
            varListTree = new ParseTree(new Symbol(null, VAR_LIST), children);
        }
        return varListTree;
    }

    /**
     * Production rule: VarListTail
     *
     * @return ParseTree
     */
    private ParseTree varListTail() {
        ParseTree varListTailTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            Symbol token = this.getToken();
            switch (token.getType()) {
                case ENDLINE:
                case RPAREN:
                    this.derivationRules.add(new Rule(6, "<VarListTail> -> EPSILON",  this.getToken()));
                    return new ParseTree(new Symbol(null, EPSILON));
            }
            this.derivationRules.add(new Rule(5, "<VarListTail> -> , <VarList>",  this.getToken()));
            ParseTree comaTree = this.matchTree(LexicalUnit.COMMA, this.getToken().getType());
            ParseTree varListTree = this.varList();
            children.add(comaTree);
            children.add(varListTree);
            varListTailTree = new ParseTree(new Symbol(null, VAR_LIST_TAIL), children);
        }
        return varListTailTree;
    }

    /**
     * Production rule: Code
     *
     * @return ParseTree
     */
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
                    this.derivationRules.add(new Rule(8, "<Code> -> EPSILON",  this.getToken()));
                    ParseTree epsilonTree = new ParseTree(new Symbol(null, EPSILON));
                    children.add(epsilonTree);
                    return new ParseTree(new Symbol(null, CODE), children);
            }

            this.derivationRules.add(new Rule(7, "<Code> -> <Instruction> [EndLine] <Code>",  this.getToken()));
            ParseTree instruction = this.instruction();
            ParseTree endLineTree = this.skipEndLines();
            ParseTree codeTree1 = this.code();
            children.add(instruction);
            children.add(endLineTree);
            children.add(codeTree1);
            codeTree = new ParseTree(new Symbol(null, CODE), children);
        }
        return codeTree;
    }

    /**
     * Production rule: Instruction
     *
     * @return ParseTree
     */
    private ParseTree instruction() {
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (this.getToken().getType()) {
                case READ:
                    this.derivationRules.add(new Rule(14, "<Instruction> -> <Read>",  this.getToken()));
                    ParseTree readTree = this.read();
                    children.add(readTree);
                    return new ParseTree(new Symbol(null, "INSTRUCTION"), children);
                case VARNAME:
                    this.derivationRules.add(new Rule(9, "<Instruction> -> <Varname>",  this.getToken()));
                    ParseTree assign = this.assign();
                    children.add(assign);
                    return new ParseTree(new Symbol(null, "INSTRUCTION"), children);
                case IF:
                    this.derivationRules.add(new Rule(10, "<Instruction> -> <If>",  this.getToken()));
                    ParseTree ifTree = this.ifVariable();
                    children.add(ifTree);
                    return new ParseTree(new Symbol(null, "INSTRUCTION"), children);
                case WHILE:
                    this.derivationRules.add(new Rule(11, "<Instruction> -> <While>",  this.getToken()));
                    ParseTree whileTree = this.whileVariable();
                    children.add(whileTree);
                    return new ParseTree(new Symbol(null, "INSTRUCTION"), children);
                case FOR:
                    this.derivationRules.add(new Rule(12, "<Instruction> -> <FOR>",  this.getToken()));
                    ParseTree forTree = this.forVariable();
                    children.add(forTree);
                    return new ParseTree(new Symbol(null, "INSTRUCTION"), children);
                case PRINT:
                    this.derivationRules.add(new Rule(13, "<Instruction> -> PRINT",  this.getToken()));
                    ParseTree printTree = this.print();
                    children.add(printTree);
                    return new ParseTree(new Symbol(null, "INSTRUCTION"), children);
                default:
                    this.stopExecutionAndNotifyUser(this.getToken(), LexicalUnit.WHILE);
            }

        }
        return null;
    }

    /**
     * Production rule: While
     *
     * @return ParseTree
     */
    private ParseTree whileVariable() {
        ParseTree whileVariableTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(48, "<While> -> WHILE (<Cond>) DO [EndLine]<Code> ENDWHILE",  this.getToken()));
            ParseTree whileTree = this.matchTree(LexicalUnit.WHILE, this.getToken().getType());
            ParseTree lparenTree = this.matchTree(LexicalUnit.LPAREN, this.getToken().getType());
            ParseTree condTree = this.cond();
            ParseTree rparenTree = this.matchTree(LexicalUnit.RPAREN, this.getToken().getType());
            ParseTree doTree = this.matchTree(LexicalUnit.DO, this.getToken().getType());
            ParseTree endLineTree = this.skipEndLines();
            ParseTree codeTree = this.code();
            ParseTree endWhileTree = this.matchTree(LexicalUnit.ENDWHILE, this.getToken().getType());
            children.add(whileTree);
            children.add(lparenTree);
            children.add(condTree);
            children.add(rparenTree);
            children.add(doTree);
            children.add(endLineTree);
            children.add(codeTree);
            children.add(endWhileTree);
            whileVariableTree = new ParseTree(new Symbol(null, WHILE), children);
        }
        return whileVariableTree;
    }

    /**
     * Production rule: For
     *
     * @return ParseTree
     */
    private ParseTree forVariable() {
        ParseTree forTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(49, "<For> -> FOR [VarName] := <ExprArith> TO <ExprArith> DO [EndLine] <Code> ENDFOR",  this.getToken()));
            ParseTree forTree1 = this.matchTree(LexicalUnit.FOR, this.getToken().getType());
            ParseTree varNameTree = this.matchTree(LexicalUnit.VARNAME, this.getToken().getType());
            ParseTree assignTree = this.matchTree(LexicalUnit.ASSIGN, this.getToken().getType());
            ParseTree exprArithTree = this.exprArith();
            ParseTree toTree = this.matchTree(LexicalUnit.TO, this.getToken().getType());
            ParseTree exprArithTree1 = this.exprArith();
            ParseTree doTree = this.matchTree(LexicalUnit.DO, this.getToken().getType());
            ParseTree endLineTree = this.skipEndLines();
            ParseTree codeTree = this.code();
            ParseTree endForTree = this.matchTree(LexicalUnit.ENDFOR, this.getToken().getType());
            children.add(forTree1);
            children.add(varNameTree);
            children.add(assignTree);
            children.add(exprArithTree);
            children.add(toTree);
            children.add(exprArithTree1);
            children.add(doTree);
            children.add(endLineTree);
            children.add(codeTree);
            children.add(endForTree);
            forTree = new ParseTree(new Symbol(null, FOR), children);
        }
        return forTree;
    }

    /**
     * Production rule: Print
     *
     * @return ParseTree
     */
    private ParseTree print() {
        ParseTree printTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(50, "<Print> -> PRINT(<ExpList>)",  this.getToken()));
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

    /**
     * Production rule: ExpList
     *
     * @return ParseTree
     */
    private ParseTree expList() {
        ParseTree expListTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(52, "<ExpList> -> <ExprArith> <ExpListTail>",  this.getToken()));
            ParseTree exprArithTree = this.exprArith();
            ParseTree expListTailTree = this.expListTail();
            children.add(exprArithTree);
            children.add(expListTailTree);
            expListTree = new ParseTree(new Symbol(null, EXP_LIST), children);
        }
        return expListTree;
    }

    /**
     * Production rule: ExpListTail
     *
     * @return ParseTree
     */
    private ParseTree expListTail() {
        ParseTree expListTailTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (this.getToken().getType()) {
                case RPAREN:
                    this.derivationRules.add(new Rule(54, "<ExpListTail> -> EPSILON",  this.getToken()));
                    ParseTree epsilonTree = new ParseTree(new Symbol(null, EPSILON));
                    children.add(epsilonTree);
                    return new ParseTree(new Symbol(null, EXP_LIST_TAIL), children);

            }
            this.derivationRules.add(new Rule(53, "<ExpListTail> -> , <ExpList>",  this.getToken()));
            ParseTree commaTree = this.matchTree(LexicalUnit.COMMA, this.getToken().getType());
            ParseTree expListTree = this.expList();
            children.add(commaTree);
            children.add(expListTree);
            expListTailTree = new ParseTree(new Symbol(null, EXP_LIST_TAIL), children);
        }
        return expListTailTree;
    }

    /**
     * Production rule: If
     *
     * @return ParseTree
     */
    private ParseTree ifVariable() {
        ParseTree ifTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(30, "<If> -> IF ( <Cond> ) THEN [EndLine] <Code><IfTail>",  this.getToken()));
            ParseTree ifTree1 = this.matchTree(LexicalUnit.IF, this.getToken().getType());
            ParseTree lParenTree = this.matchTree(LexicalUnit.LPAREN, this.getToken().getType());
            ParseTree condTree = this.cond();
            ParseTree rParenTree = this.matchTree(LexicalUnit.RPAREN, this.getToken().getType());
            ParseTree thenTree = this.matchTree(LexicalUnit.THEN, this.getToken().getType());
            ParseTree endLineTree = this.skipEndLines();
            ParseTree codeTree = this.code();
            ParseTree ifTailTree = this.ifTail();
            children.add(ifTree1);
            children.add(lParenTree);
            children.add(condTree);
            children.add(rParenTree);
            children.add(thenTree);
            children.add(endLineTree);
            children.add(codeTree);
            children.add(ifTailTree);
            ifTree = new ParseTree(new Symbol(null, IF), children);
        }
        return ifTree;
    }

    /**
     * Production rule: IfTail
     *
     * @return ParseTree
     */
    private ParseTree ifTail() {
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (this.getToken().getType()) {
                case ENDIF:
                    this.derivationRules.add(new Rule(31, "<IfTail> -> ENDIF",  this.getToken()));
                    ParseTree endIfTree = this.matchTree(LexicalUnit.ENDIF, this.getToken().getType());
                    children.add(endIfTree);
                    return new ParseTree(new Symbol(null, IF_TAIL), children);
                case ELSE:
                    this.derivationRules.add(new Rule(32, "<IfTail> -> ELSE [EndLine] <Code> ENDIF",  this.getToken()));
                    ParseTree elseTree = this.matchTree(LexicalUnit.ELSE, this.getToken().getType());
                    ParseTree endLineTree = this.skipEndLines();
                    ParseTree codeTree = this.code();
                    ParseTree endIfTree1 = this.matchTree(LexicalUnit.ENDIF, this.getToken().getType());
                    children.add(elseTree);
                    children.add(endLineTree);
                    children.add(codeTree);
                    children.add(endIfTree1);
                    return new ParseTree(new Symbol(null, IF_TAIL), children);
                default:
                    this.stopExecutionAndNotifyUser(this.getToken(), LexicalUnit.ENDIF);
            }
        }
        return null;
    }

    /**
     * Production rule: Cond
     *
     * @return ParseTree
     */
    private ParseTree cond() {
        ParseTree condTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(33, "<Cond> -> <CondAnd><CondA>",  this.getToken()));
            ParseTree condAndTree = this.condAnd();
            ParseTree condA = this.condA();
            children.add(condAndTree);
            children.add(condA);
            condTree = new ParseTree(new Symbol(null, COND), children);
        }
        return condTree;
    }

    /**
     * Production rule: CondA
     *
     * @return ParseTree
     */
    private ParseTree condA() {
        ParseTree condATree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();

            switch (this.getToken().getType()) {
                case RPAREN:
                    this.derivationRules.add(new Rule(35, "<CondA> -> EPSILON",  this.getToken()));
                    ParseTree epsilonTree = new ParseTree(new Symbol(null, EPSILON));
                    children.add(epsilonTree);
                    return new ParseTree(new Symbol(null, COND_A), children);
            }
            this.derivationRules.add(new Rule(34, "<CondA> -> OR <CondAnd><CondA>",  this.getToken()));
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

    /**
     * Production rule: CondAnd
     *
     * @return ParseTree
     */
    private ParseTree condAnd() {
        ParseTree condAndTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(36, "<CondAnd> -> <CondFinal> <CondAndA>",  this.getToken()));
            ParseTree condFinalTree = this.condFinal();
            ParseTree condAndATree = this.condAndA();
            children.add(condFinalTree);
            children.add(condAndATree);
            condAndTree = new ParseTree(new Symbol(null, COND_AND), children);
        }
        return condAndTree;
    }

    /**
     * Production rule: CondFinal
     *
     * @return ParseTree
     */
    private ParseTree condFinal() {
        ParseTree condFinalTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            if (this.getToken().getType() == LexicalUnit.NOT) {
                this.derivationRules.add(new Rule(39, "<CondFinal> -> EPSILON",  this.getToken()));
                ParseTree notTree = this.matchTree(LexicalUnit.NOT, this.getToken().getType());
                ParseTree simpleCondTree = this.simpleCond();
                children.add(notTree);
                children.add(simpleCondTree);
            } else {
                this.derivationRules.add(new Rule(40, "<CondFinal> -> NOT <SimpleCond>",  this.getToken()));
                ParseTree simpleCondTree1 = this.simpleCond();
                children.add(simpleCondTree1);
            }
            condFinalTree = new ParseTree(new Symbol(null, COND_FINAL), children);
        }
        return condFinalTree;
    }

    /**
     * Production rule: SimpleCond
     *
     * @return ParseTree
     */
    private ParseTree simpleCond() {
        ParseTree simpleCondTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(41, "<SimpleCond> -> <ExprArith><Comp><ExprArith>",  this.getToken()));
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

    /**
     * Production rule: Comp
     *
     * @return ParseTree
     */
    private ParseTree comp() {
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (this.getToken().getType()) {
                case EQ:
                    this.derivationRules.add(new Rule(42, "<Comp> -> =",  this.getToken()));
                    ParseTree eqTree = this.matchTree(LexicalUnit.EQ, this.getToken().getType());
                    children.add(eqTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                case GEQ:
                    this.derivationRules.add(new Rule(43, "<Comp> -> >=",  this.getToken()));
                    ParseTree geqTree = this.matchTree(LexicalUnit.GEQ, this.getToken().getType());
                    children.add(geqTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                case GT:
                    this.derivationRules.add(new Rule(44, "<Comp> -> >",  this.getToken()));
                    ParseTree gtTree = this.matchTree(LexicalUnit.GT, this.getToken().getType());
                    children.add(gtTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                case LEQ:
                    this.derivationRules.add(new Rule(45, "<Comp> -> <=",  this.getToken()));
                    ParseTree leqTree = this.matchTree(LexicalUnit.LEQ, this.getToken().getType());
                    children.add(leqTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                case LT:
                    this.derivationRules.add(new Rule(46, "<Comp> -> <",  this.getToken()));
                    ParseTree ltTree = this.matchTree(LexicalUnit.LT, this.getToken().getType());
                    children.add(ltTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                case NEQ:
                    this.derivationRules.add(new Rule(47, "<Comp> -> <>",  this.getToken()));
                    ParseTree neqTree = this.matchTree(LexicalUnit.NEQ, this.getToken().getType());
                    children.add(neqTree);
                    return new ParseTree(new Symbol(null, COMP), children);
                default:
                    this.stopExecutionAndNotifyUser(this.getToken(), LexicalUnit.EQ);
            }
        }
        return null;
    }

    /**
     * Production rule: CondAndA
     *
     * @return ParseTree
     */
    private ParseTree condAndA() {
        ParseTree condAndA = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            switch (this.getToken().getType()) {
                case RPAREN:
                case OR:
                    this.derivationRules.add(new Rule(38, "<CondAndA> -> EPSILON",  this.getToken()));
                    ParseTree epsilonTree = new ParseTree(new Symbol(null, EPSILON));
                    children.add(epsilonTree);
                    return new ParseTree(new Symbol(null, COND_AND_A), children);
            }
            this.derivationRules.add(new Rule(37, "<CondAndA> -> AND <CondFinal> <CondAndA>",  this.getToken()));
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

    /**
     * Production rule: Assign
     *
     * @return ParseTree
     */
    private ParseTree assign() {
        ParseTree assignTree = null;
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(15, "<Assign> -> [ VarName] := <ExprArith>",  this.getToken()));
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

    /**
     * Production rule: Read
     *
     * @return ParseTree
     */
    private ParseTree read() {
        ParseTree readTree = null;
        if (this.shouldContinue) {
            List<ParseTree> children = new ArrayList<ParseTree>();
            this.derivationRules.add(new Rule(51, "<Read> -> READ(<VarList>)",  this.getToken()));
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

    /**
     * Production rule: exprArith
     *
     * @return ParseTree
     */
    private ParseTree exprArith() {
        ParseTree exprArithTree = null;
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(16, "<ExprArith> -> <ExprMult><ExprArithA>",  this.getToken()));
            ParseTree exprMultTree = this.exprMult();
            ParseTree exprArithATree = this.exprArithA();
            List<ParseTree> children = new ArrayList<ParseTree>();
            children.add(exprMultTree);
            children.add(exprArithATree);
            exprArithTree = new ParseTree(new Symbol(null, EXPR_ARITH), children);
        }
        return exprArithTree;
    }

    /**
     * Production rule: exprArithA
     *
     * @return ParseTree
     */
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
                case AND:
                case OR:
                case COMMA:
                    this.derivationRules.add(new Rule(19, "<ExprArithA> -> EPSILON",  this.getToken()));
                    ParseTree epsilonTree = new ParseTree(new Symbol(null, EPSILON));
                    children.add(epsilonTree);
                    return new ParseTree(new Symbol(null, EXPR_ARITH_A), children);
                case PLUS:
                    this.derivationRules.add(new Rule(17, "<ExprArithA> -> + <ExprMult> <ExprArithA>",  this.getToken()));
                    ParseTree plusTree = this.matchTree(LexicalUnit.PLUS, this.getToken().getType());
                    ParseTree exprMultTree = this.exprMult();
                    ParseTree exprArithA = this.exprArithA();
                    children.add(plusTree);
                    children.add(exprMultTree);
                    children.add(exprArithA);
                    return new ParseTree(new Symbol(null, EXPR_ARITH_A), children);
                case MINUS:
                    this.derivationRules.add(new Rule(18, "<ExprArithA> -> - <ExprMult> <ExprArithA>",  this.getToken()));
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

    /**
     * Production rule: ExprMult
     *
     * @return ParseTree
     */
    private ParseTree exprMult() {
        ParseTree exprMultTree = null;
        if (this.shouldContinue) {
            this.derivationRules.add(new Rule(20, "<ExprMult> -> <Id><ExprMultA>",  this.getToken()));
            ParseTree idTree = this.id();
            ParseTree exprMultATree = this.exprMultA();
            List<ParseTree> children = new ArrayList<ParseTree>();
            children.add(idTree);
            children.add(exprMultATree);
            exprMultTree = new ParseTree(new Symbol(null, EXPR_MULT), children);
        }
        return exprMultTree;
    }

    /**
     * Production rule: ExprMultA
     *
     * @return ParseTree
     */
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
                case AND:
                case OR:
                case COMMA:
                    this.derivationRules.add(new Rule(23, "<ExprMultA> -> EPSILON",  this.getToken()));
                    ParseTree epsilonTree = new ParseTree(new Symbol(null, EPSILON));
                    children.add(epsilonTree);
                    return new ParseTree(new Symbol(null, EXPR_MULT_A), children);
                case TIMES:
                    this.derivationRules.add(new Rule(21, "<ExprMultA> -> ∗ <Id><ExprMultA>",  this.getToken()));
                    ParseTree timesTree = this.matchTree(LexicalUnit.TIMES, this.getToken().getType());
                    ParseTree idTree = this.id();
                    ParseTree exprMultATree1 = this.exprMultA();
                    children.add(timesTree);
                    children.add(idTree);
                    children.add(exprMultATree1);
                    return new ParseTree(new Symbol(null, EXPR_MULT_A), children);
                case DIVIDE:
                    this.derivationRules.add(new Rule(22, "<ExprMultA> -> / <Id><ExprMultA>",  this.getToken()));
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

    /**
     * It shows the derivation rules list with detail.
     */
    public void showDerivationRules() {
        System.out.println();
        System.out.println();
        System.out.println("Detailed left most derivation rules:");
        for (Rule rule: derivationRules) {
            System.out.println(rule.toString());
        }
    }

    /**
     * It shows the derivation rule numbers only.
     */
    public void showDerivationRulesShort() {
        System.out.println();
        System.out.println();
        System.out.println("Left most derivation rules:");
        for (Rule rule: derivationRules) {
            System.out.print(rule.getNumber()+" ");
        }
    }

    /**
     * It creates and returns the parseTree
     * @return ParseTree
     */
    public ParseTree getParseTree() {
        return this.program();
    }

    /**
     * It verifies that no error was threw.
     *
     * @return boolean
     */
    public boolean isSyntaxCorrect() {
        return this.shouldContinue;
    }

    /**
     * It returns the compiling error if there was one during syntax checker execution.
     *
     * @return CompilerError
     */
    public CompilerError getError() {
        return this.error;
    }

    /**
     * It skip a token.
     */
    private void skipToken() {
        this.tokenIndex++;
    }

    /**
     * It matches an end line and skip the other ones if there are more than one.
     *
     * @return ParseTree
     */
    private ParseTree skipEndLines() {
        ParseTree endLineTree = this.matchTree(LexicalUnit.ENDLINE, this.getToken().getType());
        while (this.shouldSkipTokens()) {
            this.skipToken();
        }
        return endLineTree;
    }

    /**
     * It verifies if the endline token should be skipped.
     *
     * @return boolean
     */
    private boolean shouldSkipTokens() {
        return this.shouldContinue && this.tokenIndex < this.tokens.size() && this.getToken().getType() == LexicalUnit.ENDLINE;
    }

}
