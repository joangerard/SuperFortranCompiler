package utils.codegeneration;

/**
 * Class responsible for generating AST structure.
 */
public class AST {

    private String value;
    private Type type;
    private AST left;
    private AST right;

    public AST()
    {
        this.left = null;
        this.right = null;
    }

    public AST(Type type, String value)
    {
        this.type = type;
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AST getLeft() {
        return left;
    }

    public void setLeft(AST left) {
        this.left = left;
    }

    public AST getRight() {
        return right;
    }

    public void setRight(AST right) {
        this.right = right;
    }

    public Type getType() {
        return type;
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
        treeTikZ.append(this.value);
        treeTikZ.append("}\n");

        if (this.left != null) {
            treeTikZ.append("child { ");
            treeTikZ.append(this.left.toTikZ());
            treeTikZ.append(" }\n");
        }

        if (this.right != null) {
            treeTikZ.append("child { ");
            treeTikZ.append(this.right.toTikZ());
            treeTikZ.append(" }\n");
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
