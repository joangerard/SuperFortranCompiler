import utils.codegeneration.AST;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {
    List<String> instructions;
    int lastInstructionName;

    CodeGenerator(){
        this.instructions = new ArrayList<>();
        this.lastInstructionName = 0;
        initializeInstructions();
    }

    public String llvm(AST tree) {
        this.instructions.add("define i32 @main() {");
        this.instructions.add("entry:");
        this.program(tree);
        this.instructions.add("ret i32 0");
        this.instructions.add("}");
        return getInstructions();
    }

    private void program(AST tree) {
        AST variablesTree = tree.getLeft();
        AST codeTree = tree.getRight();

        variables(variablesTree);
        code(codeTree);
    }

    private void variables(AST tree) {
        AST variable = tree.getRight();

        if (variable != null){
            initializeVariable(variable);
            variables(variable);
        }
    }

    private void code(AST tree) {
        AST instructionTree = tree.getLeft();
        AST codeTree = tree.getRight();

        if (instructionTree != null) {
            instruction(instructionTree);
        }

        if (codeTree != null){
            code(codeTree);
        }
    }

    private void instruction(AST tree) {
        AST instructionTree = tree.getRight();

        switch (tree.getType()) {
            case ASSIGN_INS:
                assign(instructionTree);
                break;
            case PRINT:
                print(tree);
                break;
        }
    }

    private void print(AST tree) {
        AST left = tree.getLeft();
        AST right = tree.getRight();

        String exprLeft = processArithOperation(left);
        instructions.add(String.format("call void @println(i32 %s)", exprLeft));

        switch (right.getType()) {
            case PRINT:
                print(right);
                break;
            default:
                String operationRes = processArithOperation(right);
                instructions.add(String.format("call void @println(i32 %s)", operationRes));
                break;
        }
    }

    private String assign(AST tree) {
        AST variable = tree.getLeft();
        AST result = tree.getRight();


        String varname = "%" + variable.getValue();
        String value = processArithOperation(result);

        String instruction = String.format("store i32 %s, i32* %s", value, varname);
        instructions.add(instruction);

        return varname;
    }

    private String processArithOperation(AST tree) {
        AST left = tree.getLeft();
        AST right = tree.getRight();

        if (left == null) {
            return variable(tree);
        }

        String leftResult = processArithOperation(left);
        String rightResult = processArithOperation(right);

        String varname = "";
        switch (tree.getType()) {
            case PLUS:
                varname = plus(leftResult, rightResult);
                break;
            case MINUS:
                varname = minus(leftResult,rightResult);
                break;
            case TIMES:
                varname = times(leftResult, rightResult);
                break;
            case DIVIDE:
                varname = divide(leftResult, rightResult);
                break;
        }
        return varname;
    }

    private String plus(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = add i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    private String minus(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = sub i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    private String times(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = mul i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    private String divide(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = sdiv i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    private String variable(AST tree) {
        String variable = "";
        switch (tree.getType()) {
            case NUMBER:
                variable = tree.getValue();
                break;
            case VARNAME:
                variable = getLlvmVariable();
                String instruction = variable + " = load i32, i32* %"+tree.getValue();
                this.instructions.add(instruction);
                break;
        }
        return variable;
    }

    private void initializeVariable(AST tree) {
        String varname = "%" + tree.getValue();
        this.instructions.add(String.format("%s = alloca i32", varname));
    }

    private String getLlvmVariable() {
        String varname = "%" + lastInstructionName;
        lastInstructionName++;
        return varname;
    }

    private String getInstructions() {
        StringBuilder insts = new StringBuilder();
        for (String instruction :
                this.instructions) {
            insts.append(instruction);
            insts.append("\n");
        }
        return insts.toString();
    }

    private void initializeInstructions(){
        String header = "@.strP = private unnamed_addr constant [4 x i8] c\"%d\\0A\\00\", align 1\n" +
                "@.strR = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1\n" +
                "\n" +
                "define void @println(i32 %x) {\n" +
                "  %1 = alloca i32, align 4\n" +
                "  store i32 %x, i32* %1, align 4\n" +
                "  %2 = load i32, i32* %1, align 4\n" +
                "  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.strP, i32 0, i32 0), i32 %2)\n" +
                "  ret void\n" +
                "}\n" +
                "\n" +
                "define i32 @readInt() {\n" +
                "  %x = alloca i32, align 4\n" +
                "  %1 = call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.strR, i32 0, i32 0), i32* %x)\n" +
                "  %2 = load i32, i32* %x, align 4\n" +
                "  ret i32 %2\n" +
                "}\n" +
                "\n" +
                "declare i32 @scanf(i8*, ...)\n" +
                "declare i32 @printf(i8*, ...)\n";

        this.instructions.add(header);
    }
}
