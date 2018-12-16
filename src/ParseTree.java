import java.util.List;
import java.util.ArrayList;

/**
 * A skeleton class to represent parse trees.
 * The arity is not fixed: a node can have 0, 1 or more children.
 * Trees are represented in the following way:
 * In other words, trees are defined recursively:
 * A tree is a root (with a label of type Symbol) and a list of trees children.
 * Thus, a leave is simply a tree with no children (its list of children is empty).
 * This class can also be seen as representing the Node of a tree,
 * in which case a tree is simply represented as its root.
 *
 * @author Léo Exibard
 */

public class ParseTree {
    private Symbol label;             // The label of the root of the tree
    private List<ParseTree> children; // Its children, which are trees themselves

    /**
     * Creates a singleton tree with only a root labeled by lbl.
     *
     * @param lbl The label of the root
     */
    public ParseTree(Symbol lbl) {
        this.label = lbl;
        this.children = new ArrayList<ParseTree>(); // This tree has no children
    }

    /**
     * Creates a tree with root labeled by lbl and children chdn.
     *
     * @param lbl  The label of the root
     * @param chdn Its children
     */
    public ParseTree(Symbol lbl, List<ParseTree> chdn) {
        this.label = lbl;
        this.children = chdn;
    }

    public boolean hasChildren()
    {
        return this.children.size() != 0;
    }

    public List<ParseTree> getChildren() {
        return this.children;
    }

    public void addChild(ParseTree child){
        this.children.add(child);
    }

    public Symbol getSymbol()
    {
        return label;
    }


    /**
     * Writes the tree as LaTeX code.
     *
     * @return String
     */
    public String toTeX() {
        StringBuilder treeTeX = new StringBuilder();
        treeTeX.append("[.");
        if (label.isEpsilon()) {
            treeTeX.append("$\\varepsilon$");
        } else {
            treeTeX.append(label);
        }
        treeTeX.append(" ");
        for (ParseTree child : children) {
            treeTeX.append(child.toTeX());
        }
        treeTeX.append("]");
        return treeTeX.toString();
    }

    /**
     * Writes the tree as TikZ code.
     * TikZ is a language to specify drawings in LaTeX files.
     *
     * @return String
     */
    public String toTikZ() {
        StringBuilder treeTikZ = new StringBuilder();
        treeTikZ.append("node {");
        if (label.isTerminal()) {
            Singleton.getInstance().counter++;
            treeTikZ.append("\\colorbox{blue!20}{\\textbf{"+Singleton.getInstance().counter+"} "+label.toTeX()+"}");
        } else {
            treeTikZ.append(label.toTeX());
        }

        treeTikZ.append("}\n");
        if ( children != null) {
            for (ParseTree child : children) {
                treeTikZ.append("child { ");
                if(child == null) {
                    String a = "";
                }
                treeTikZ.append(child.toTikZ());
                treeTikZ.append(" }\n");
            }
        }
        return treeTikZ.toString();
    }

    /**
     * Writes the tree as a TikZ picture.
     * A TikZ picture embeds TikZ code so that LaTeX understands it.
     *
     * @return String
     */
    public String toTikZPicture() {
        return "\\begin{tikzpicture}[tree layout]\n\\" + toTikZ() + ";\n\\end{tikzpicture}";
    }

    /**
     * Writes the tree as a LaTeX document which can be compiled (using the LuaLaTeX engine).
     * Be careful that such code will not compile with PDFLaTeX,
     * since the tree drawing algorithm is written in Lua.
     * The code is not very readable as such, but you can have a look at the outputted file
     * if you want to understand better.
     *
     * @return String
     */
    public String toLaTeX() {
        return "\\RequirePackage{luatex85}\n\\documentclass{standalone}\n\n\\usepackage{tikz}\n\n\\usetikzlibrary{graphdrawing, graphdrawing.trees}\n\n\\begin{document}\n\n" + toTikZPicture() + "\n\n\\end{document}\n%% Local Variables:\n%% TeX-engine: luatex\n%% End:";
    }
}
