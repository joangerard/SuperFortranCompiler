public class LlvmCreator {

    public String create(ParseTree tree)
    {
        return this.program(tree);
    }

    private String program(ParseTree tree)
    {
        StringBuilder llvm = new StringBuilder();

        llvm.append(initialize());
        llvm.append("define i32 @main() {\n");

            llvm.append("\tentry:\n");
                llvm.append(this.variables(tree.getChildren().get(0)));
                llvm.append("ret i32 0");
        llvm.append("}\n");
        return llvm.toString();
    }

    private String variables(ParseTree tree)
    {
        StringBuilder llvm = new StringBuilder();
        for (ParseTree variable :
                tree.getChildren()) {
            llvm.append("\t\t%"+variable.getSymbol().getValue()+" = alloca i32\n");
        }
        return llvm.toString();
    }

    private String initialize()
    {
        String s = "@.strP = private unnamed_addr constant [4 x i8] c\"%d\\0A\\00\", align 1\n";
        s += "@.strR = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1\n";
        return s;
    }
}
