public class SymbolMapper implements SymbolMapperInterface{
    public SymbolMapper(){

    }

    public String mapSymbolToString(LexicalUnit symbol) {
        switch (symbol) {
            case RPAREN:
                return ")";
            case VARNAME:
                return "some value or variable";
            case LPAREN:
                return "(";
            case COMMA:
                return ",";
            case ASSIGN:
                return ":=";
            case MINUS:
                return "-";
            case PLUS:
                return "+";
            case TIMES:
                return "*";
            case DIVIDE:
                return "/";
            case EQ:
                return "comparison symbol";
            case GEQ:
                return ">=";
            case GT:
                return ">";
            case LEQ:
                return "<=";
            case LT:
                return "<";
            case NEQ:
                return "<>";
            case WHILE:
                return "begin or end of instruction";
            case ENDIF:
                return "end of instruction IF or ELSE";
            default:
                return symbol.toString();
        }
    }
}
