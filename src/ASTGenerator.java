import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import utils.codegeneration.AST;
import utils.codegeneration.Symbols;
import utils.codegeneration.Type;

import java.util.List;

public class ASTGenerator {

    public AST create(ParseTree tree) {
        return this.program(tree);
    }

    private AST program(ParseTree tree) {
        List<ParseTree> children = tree.getChildren();
        ParseTree variablesChild = children.get(3);
        ParseTree codeChild = children.get(4);

        AST programTree = new AST(Type.PROGRAM, Symbols.PROGRAM);
        AST variablesTree = variables(variablesChild);
        AST codeTree = code(codeChild);

        programTree.setLeft(variablesTree);
        programTree.setRight(codeTree);

        return programTree;
    }

    private AST code(ParseTree tree) {
        List<ParseTree> children = tree.getChildren();

        if (children.size() == 1) {
            return null;
        }

        ParseTree firstChild = children.get(0);
        ParseTree thirdChild = children.get(2);

        AST instructionTree = instruction(firstChild);
        AST codeTree = code(thirdChild);

        if (codeTree == null) {
            AST newCodeTree = new AST(Type.CODE, Symbols.CODE);
            newCodeTree.setLeft(instructionTree);
            return newCodeTree;
        }

        AST newCodeTree = new AST(Type.CODE, Symbols.CODE);
        newCodeTree.setLeft(instructionTree);
        newCodeTree.setRight(codeTree);

        return newCodeTree;
    }

    private AST instruction(ParseTree tree) {
        List<ParseTree> children = tree.getChildren();
        ParseTree firstChild = children.get(0);
        AST result = null;
        switch (getValue(firstChild))
        {
            case Symbols.ASSIGN:
                result = assign(firstChild);
                break;
        }
        return result;
    }

    private AST assign(ParseTree tree) {
        List<ParseTree> children = tree.getChildren();

        AST varnameTree = varName(children.get(0));
        AST assign = assignSym(children.get(1));
        AST exprArithTree = exprArith(children.get(2));

        AST assignTree = new AST(Type.ASSIGN_INS, "ASSIGN");

        assign.setRight(exprArithTree);
        assign.setLeft(varnameTree);
        assignTree.setRight(assign);

        return assignTree;
    }

    private AST variables(ParseTree tree) {
        List<ParseTree> children = tree.getChildren();
        ParseTree secondChild = children.get(1);

        AST varlistTree = varList(secondChild);
        AST variables = new AST(Type.VARIABLES, Symbols.VARIABLES);
        variables.setRight(varlistTree);

        return variables;
    }

    private AST varList(ParseTree tree) {
        List<ParseTree> children = tree.getChildren();
        ParseTree firstChild = children.get(0);
        ParseTree secondChild = children.get(1);

        if (getValue(secondChild).equals(Symbols.EPSILON)) {
            return varName(firstChild);
        }

        AST varListTailTree = varListTail(secondChild);
        AST varnameTree = varName(firstChild);

        varnameTree.setRight(varListTailTree);

        return varnameTree;
    }

    private AST varListTail(ParseTree tree) {
        List<ParseTree> children = tree.getChildren();
        ParseTree secondChild = children.get(1);

        return varList(secondChild);
    }

    private AST exprArith(ParseTree tree) {
        List<ParseTree> children = tree.getChildren();

        AST exprMultTree = exprMult(children.get(0));
        AST exprArithATree = exprArithA(children.get(1));

        if (exprArithATree == null) {
            return exprMultTree;
        }

        return addLeafToTheBottomLeft(exprMultTree, exprArithATree);
    }

    private AST exprArithA(ParseTree tree) {
        List<ParseTree> children = tree.getChildren();

        if (children.size() == 1) {
            return null;
        }

        AST arithTree = arithSymbol(children.get(0));
        AST exprMultTree = exprMult(children.get(1));
        AST exprArithATree = exprArithA(children.get(2));

        if (exprArithATree == null) {
            arithTree.setRight(exprMultTree);
            return arithTree;
        }

        arithTree.setRight(exprMultTree);
        return addLeafToTheBottomLeft(arithTree, exprArithATree);
    }

    private AST exprMult(ParseTree tree) {
        List <ParseTree> children = tree.getChildren();

        AST idTree = id(children.get(0));
        AST exprMultA = exprMultA(children.get(1));

        if (exprMultA == null) {
            return idTree;
        }

        return addLeafToTheBottomLeft(idTree, exprMultA);
    }

    private AST exprMultA(ParseTree tree) {
        List<ParseTree> children = tree.getChildren();

        if (children.size() == 1) {
            return null;
        }

        AST arithTree = arithSymbol(children.get(0));
        AST idTree = id(children.get(1));
        AST exprMultATree = exprMultA(children.get(2));

        if (exprMultATree == null) {
            arithTree.setRight(idTree);
            return arithTree;
        }

        arithTree.setRight(idTree);
        return addLeafToTheBottomLeft(arithTree, exprMultATree);
    }

    private AST addLeafToTheBottomLeft(AST leaf, AST tree)
    {
        if (tree.getLeft() == null) {
            tree.setLeft(leaf);
            return tree;
        }
        else {
            AST newLeft = addLeafToTheBottomLeft(leaf, tree.getLeft());
            tree.setLeft(newLeft);
            return tree;
        }
    }

    private AST id(ParseTree tree) {
        AST result = null;
        List<ParseTree> children = tree.getChildren();
        int numberOfChildren = children.size();

        if (numberOfChildren == 1) {
            ParseTree child = children.get(0);
            return getVariable(child);
        }

        if (numberOfChildren == 2) {
            AST idTailTree = idTail(children.get(1));
            String newValue = Symbols.MINUS + idTailTree.getValue();
            idTailTree.setValue(newValue);
            return idTailTree;
        }

        if (numberOfChildren == 3) {
            return exprArith(children.get(1));
        }
        return result;
    }

    private AST idTail(ParseTree tree) {
        AST result = null;
        ParseTree child = tree.getChildren().get(0);
        result = getVariable(child);
        return result;
    }

    private AST getVariable(ParseTree child) {
        AST result = null;
        switch (getType(child)) {
            case NUMBER:
                result = new AST(Type.NUMBER, child.getSymbol().getValue().toString());
                break;
            case VARNAME:
                result = new AST(Type.VARNAME, child.getSymbol().getValue().toString());
                break;
        }
        return result;
    }

    private AST varName(ParseTree tree) {
        String value = tree.getSymbol().getValue().toString();
        return new AST(Type.VARNAME, value);
    }

    private AST number(ParseTree tree) {
        String value = tree.getSymbol().getValue().toString();
        return new AST(Type.NUMBER, value);
    }

    private AST assignSym(ParseTree tree) {
        String value = tree.getSymbol().getValue().toString();
        return new AST(Type.ASSIGN, value);
    }

    private AST arithSymbol(ParseTree tree) {
        AST result = null;
        switch (getType(tree)){
            case DIVIDE:
                result = new AST(Type.DIVIDE, Symbols.DIVISION);
                break;
            case PLUS:
                result = new AST(Type.PLUS, Symbols.PLUS);
                break;
            case TIMES:
                result = new AST(Type.TIMES, Symbols.TIMES);
                break;
            case MINUS:
                result = new AST(Type.MINUS, Symbols.MINUS);
                break;
        }
        return result;
    }

    private LexicalUnit getType(ParseTree tree) {
        return tree.getSymbol().getType();
    }

    private String getValue(ParseTree tree) {
        return tree.getSymbol().getValue().toString();
    }
}
