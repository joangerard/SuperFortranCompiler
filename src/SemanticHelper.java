import java.util.ArrayList;
import java.util.List;

public class SemanticHelper {

    private final String PROGRAM = "PROGRAM";
    private final String VARLIST = "VAR-LIST";
    private final String CODE = "CODE";
    private final String INSTRUCTION = "INSTRUCTION";
    private final String ASSIGN = "ASSIGN";
    private final String ENDLINE = "\\n";
    private final String ID = "ID";
    private final String EXPRMULTA = "EXPR-MULT-A";
    private final String EXPRMULT = "EXPR-MULT";
    private final String EXPRARITH = "EXPR-ARITH";
    private final String EXPRARITHA = "EXPR-ARITH-A";
    private final String EPSILON = "EPSILON";
    private final String VARNAME= "VARNAME";
    private final String NUMBER = "NUMBER";
    private final String MINUS = "MINUS";
    private final String IDTAIL = "ID-TAIL";


    public ParseTree getAbstractTree(ParseTree tree)
    {
        return this.program(tree);
    }

    public ParseTree program(ParseTree tree)
    {
        List<ParseTree> children = new ArrayList<>();

        ParseTree variablesTree = getChild(tree, LexicalUnit.VARIABLES.toString());
        ParseTree codeTree = getChild(tree, CODE);

        variablesTree = processVariables(variablesTree);
        codeTree = processCode(codeTree);

        children.add(variablesTree);
        children.add(codeTree);

        return new ParseTree(new Symbol(null, PROGRAM), children);
    }

    private ParseTree processCode(ParseTree codeTree) {

        List<ParseTree> newChildren = new ArrayList<>();
        for (ParseTree child :
                codeTree.getChildren()) {
            switch ((String)child.getSymbol().getValue()) {
                case ASSIGN:
                    newChildren.add(processAssign(child));
                    break;
                case CODE:
                    ParseTree code = processCode(child);
                    if (code != null) {
                        newChildren.add(code);
                    }
                    break;
                case ENDLINE:
                    break;
                default:
                    return null;
            }
        }

        return new ParseTree(new Symbol(null, CODE), newChildren);
    }

    private ParseTree processAssign(ParseTree assignTree) {
        if (isEquals(assignTree.getChildren().get(0),ASSIGN)) {
            return  processAssign(assignTree.getChildren().get(0));
        }
        List<ParseTree> children = new ArrayList<>();
        children.add(assignTree.getChildren().get(0)); // number

        ParseTree exprArith = getChild(assignTree, EXPRARITH);
        exprArith = processExprArith(exprArith);

        children.add(exprArith);

        return new ParseTree(assignTree.getChildren().get(1).getSymbol(), children);
    }

    private ParseTree processExprArith(ParseTree exprArith) {
        List<ParseTree> children = new ArrayList<>();

        ParseTree exprMult = getChild(exprArith, EXPRMULT);
        exprMult = processExprMult(exprMult);

        ParseTree exprArithA = getChild(exprArith, EXPRARITHA);
        exprArithA = processExprArithA(exprArithA);

        ParseTree newHeader = exprArithA.getChildren().get(0);
        exprArithA.getChildren().remove(0);
        newHeader.addChild(exprMult);
        newHeader.addChild(exprArithA);

        children.add(newHeader);

        return new ParseTree(new Symbol(null, EXPRARITH), children);
    }

    private ParseTree processExprArithA(ParseTree exprArithA) {

        List<ParseTree> children = new ArrayList<>();

        if (isChildEpsilon(exprArithA)){
            return null;
        }

        ParseTree exprMult = getChild(exprArithA, EXPRMULT);
        exprMult = processExprMult(exprMult);

        ParseTree exprArithASub = getChild(exprArithA, EXPRARITHA);
        exprArithASub = processExprArithA(exprArithASub);

        children.add(exprArithA.getChildren().get(0)); // number

        if (exprMult != null) {
            children.add(exprMult); // exprMult
        }

        // In case expr-arith-a is null then we dont put the sign symbol in the header
        if (exprArithASub == null) {
            return new ParseTree(new Symbol(null, EXPRARITHA), children);
        }

        // In case expr-arith-a is the last of the chain
        if (isEquals(exprArithASub, EXPRARITHA)) {
            children.add(exprArithASub.getChildren().get(1));
        }
        else {
            children.add(exprArithASub);
        }


        ParseTree result = new ParseTree(exprArithASub.getChildren().get(0).getSymbol(), children);
        exprArithASub.getChildren().remove(0);
        return result;
    }

    private boolean isChildEpsilon(ParseTree tree)
    {
        if (tree.getChildren().size() == 1 && tree.getChildren().get(0).getSymbol().getValue() == EPSILON)
        {
            return true;
        }
        return false;
    }

    private ParseTree processExprMult(ParseTree exprMult) {
        List<ParseTree> children = new ArrayList<>();

        for (ParseTree child :
                exprMult.getChildren()) {
            switch ((String) child.getSymbol().getValue()) {
                case ID:
                    children.add(processId(child));
                    break;
                case EXPRMULTA:
                    ParseTree tree = processExprMultA(child);
                    if (tree != null) {
                        children.add(tree);
                    }
                    break;
                default:
                    return null;
            }
        }

        if (children.size() == 1) {
            return new ParseTree(children.get(0).getSymbol(), children.get(0).getChildren());
        }
        return new ParseTree(new Symbol(null, EXPRMULT), children);
    }

    private ParseTree processId(ParseTree id) {
        List<ParseTree> newChildren = new ArrayList<>();
        for (ParseTree child :
                id.getChildren()) {
            if (child.getSymbol().isTerminal()) {
                switch (child.getSymbol().getType()) {
                    case VARNAME:
                    case NUMBER:
                        newChildren.add(child);
                        break;
                    case MINUS:
                        newChildren.add(child);
                        break;
                    default:
                        break;
                }
            }
            else {
                switch (child.getSymbol().getValue().toString()) {
                    case EXPRARITH:
                        return processExprArith(child);
                    case IDTAIL:
                        newChildren.add(processIdTail(child));
                        break;
                }
            }

        }
        return new ParseTree(new Symbol(null, ID), newChildren);
    }

    private ParseTree processIdTail(ParseTree idTail)
    {
        ParseTree newChildren = null;

        for (ParseTree child :
                idTail.getChildren()) {
            switch (child.getSymbol().getType().toString()) {
                case VARNAME:
                case NUMBER:
                    newChildren = child;
                    break;
                default:
                    break;
            }
        }
        return newChildren;
    }

    private ParseTree processExprMultA(ParseTree exprMultA) {
        return null;
    }

    private ParseTree processVariables(ParseTree tree) {
        ParseTree varName = getChild(tree, VARLIST);
        List<ParseTree> newChildren = new ArrayList<>();
        processVarname(varName, newChildren);
        return new ParseTree(new Symbol(null, LexicalUnit.VARIABLES.toString()), newChildren);
    }

    private void processVarname(ParseTree varName, List<ParseTree> variables) {
        if(!varName.hasChildren())
        {
            if (varName.getSymbol().getType() == LexicalUnit.VARNAME){
                variables.add(varName);
            }
        }
        else {
            List<ParseTree> children = varName.getChildren();
            for (ParseTree child:
                 children) {
                processVarname(child, variables);
            }

        }
    }

    public ParseTree getChild(ParseTree tree, String type) {
        for (ParseTree child :
                tree.getChildren()) {
            if (isEquals(child, type))
                return child;
        }
        return null;
    }

    public ParseTree getFirstChild(ParseTree tree) {
        return tree.getChildren().get(0);
    }

    private boolean isEquals(ParseTree child, String type) {
        return child.getSymbol().getValue().equals(type);
    }
}
