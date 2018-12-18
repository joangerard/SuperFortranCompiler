import utils.codegeneration.AST;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {
    List<String> instructions;
    int lastInstructionNumber;
    int ifNumber;
    int whileNumber;
    int forNumber;

    CodeGenerator(){
        this.instructions = new ArrayList<>();
        this.lastInstructionNumber = 0;
        this.ifNumber = 0;
        this.whileNumber = 0;
        this.forNumber = 0;
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
        if (tree != null) {
            AST instructionTree = tree.getLeft();
            AST codeTree = tree.getRight();

            if (instructionTree != null) {
                instruction(instructionTree);
            }

            if (codeTree != null){
                code(codeTree);
            }
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
            case READ:
                read(instructionTree);
                break;
            case IF:
                ifProcess(tree);
                break;
            case ELSE:
                ifElseProcess(tree);
                break;
            case WHILE:
                whileExpr(tree);
                break;
            case FOR:
                forExpr(tree);
                break;
        }
    }

    private void read(AST tree) {
        AST right = tree.getRight();
        String varname = "%" + tree.getValue();
        String tempVar = this.getLlvmVariable();

        instructions.add(String.format("%s = call i32 @readInt()", tempVar ));
        instructions.add(String.format("store i32 %s, i32* %s", tempVar, varname));

        if(right != null) {
            read(right);
        }
    }

    private void print(AST tree) {
        AST left = tree.getLeft();
        AST right = tree.getRight();

        String exprLeft = processArithOperation(left);
        instructions.add(String.format("call void @println(i32 %s)", exprLeft));

        if (right != null) {
            print(right);
        }
    }

    private String assign(AST tree) {
        AST variable = tree.getLeft();
        AST result = tree.getRight();


        String varname = "%" + variable.getValue();
        String value = processArithOperation(result);

        String instruction = String.format("store i32 %s, i32* %s", value, varname);
        instructions.add(instruction);

        return variable.getValue();
    }

    private void forExpr(AST tree) {
        AST condTree = tree.getLeft();
        AST codeTree = tree.getRight();
        AST assignTree = condTree.getLeft();
        AST toTree = condTree.getRight();

        String assignmentVar = assign(assignTree.getRight());
        String to = processArithOperation(toTree);

        int forNumber = this.forVariableNumber();

        String tempVar = this.getLlvmVariable();

        String forIns = "for" + forNumber;
        String doInst = "do" + forNumber;
        String endForIns = "endfor" + forNumber;
        String condIns = "%cond"+forNumber;

        instructions.add(String.format("br label %s", "%"+forIns));
        instructions.add(String.format("%s: ", forIns));
        instructions.add(String.format("%s = load i32, i32* %s", tempVar, "%"+assignmentVar));
        instructions.add(String.format("%s = icmp sle i32 %s, %s", condIns, tempVar, to));
        instructions.add(String.format("br i1 %s, label %s, label %s", condIns, "%" + doInst, "%" + endForIns));
        instructions.add(String.format(String.format("%s: ", doInst)));
        code(codeTree);
        String tempVar2 = this.getLlvmVariable();
        instructions.add(String.format("%s = add i32 %s, 1", tempVar2, tempVar));
        instructions.add(String.format("store i32 %s, i32* %s", tempVar2, "%"+assignmentVar));
        instructions.add(String.format("br label %s", "%" + forIns));
        instructions.add(String.format("%s: ", endForIns));
    }

    private void whileExpr(AST tree) {
        AST condTree = tree.getLeft();
        AST codeTree = tree.getRight();

        int whileNumber = this.whileVariableNumber();
        String whileIns = "while" + whileNumber;
        String doInst = "do"+ whileNumber;
        String endWhileInst = "endwhile"+ whileNumber;
        instructions.add(String.format("br label %s", "%"+whileIns));
        instructions.add(String.format("%s: ", whileIns));
        String cond = processBooleanInstruction(condTree);
        instructions.add(String.format("br i1 %s, label %s, label %s", cond, "%"+ doInst, "%" + endWhileInst));
        instructions.add(String.format("%s: ", doInst));
        code(codeTree);
        instructions.add(String.format("br label %s", "%" + whileIns));
        instructions.add(String.format("%s: ", endWhileInst));
    }

    private void ifElseProcess(AST tree) {
        AST ifTree = tree.getLeft();
        AST elseCode = tree.getRight();
        AST ifCond = ifTree.getLeft();
        AST ifCode = ifTree. getRight();

        String cond = processBooleanInstruction(ifCond);
        int condNumber = this.ifVariableNumber();

        String condYes = "yes"+condNumber;
        String condNo = "no"+condNumber;
        String endif = "endif"+condNumber;
        this.instructions.add(String.format("br i1 %s, label %s, label %s", cond, "%"+condYes, "%"+condNo));
        //IF
        this.instructions.add(condYes+":");
        code(ifCode);
        this.instructions.add("br label %"+endif);
        //ELSE
        this.instructions.add(condNo + ":");
        code(elseCode);
        this.instructions.add("br label %"+endif);
        //ENDIF
        this.instructions.add(endif+":");
    }

    private void ifProcess(AST tree) {
        AST left = tree.getLeft();
        AST right = tree.getRight();

        String cond = processBooleanInstruction(left);
        int condNumber = this.ifVariableNumber();
        String condYes = "yes"+condNumber;
        String endif = "endif"+condNumber;
        this.instructions.add(String.format("br i1 %s, label %s, label %s", cond, "%"+condYes, "%"+endif));
        //IF
        this.instructions.add(condYes+":");
        code(right);
        this.instructions.add("br label %"+endif);
        //ENDIF
        this.instructions.add(endif+":");
    }

    private  String andInst(AST tree) {
        AST left = tree.getLeft();
        AST right = tree.getRight();

        String leftTree = processBooleanInstruction(left);
        String rightTree = processBooleanInstruction(right);

        String varname = getLlvmVariable();
        String instruction = String.format("%s = mul i1 %s, %s", varname, leftTree, rightTree);
        this.instructions.add(instruction);

        return varname;
    }

    private String orInst(AST tree) {
        AST left = tree.getLeft();
        AST right = tree.getRight();

        String leftTree = processBooleanInstruction(left);
        String rightTree = processBooleanInstruction(right);

        String varname = getLlvmVariable();
        String instruction = String.format("%s = add i1 %s, %s", varname, leftTree, rightTree);
        this.instructions.add(instruction);

        return varname;
    }

    private String notInst(AST tree) {
        AST right = tree.getRight();
        return processBooleanOperationNOT(right);
    }

    private String processBooleanInstruction(AST tree) {
        String varname = "";
        switch (tree.getType()) {
            case AND:
                varname = andInst(tree);
                break;
            case OR:
                varname = orInst(tree);
                break;
            case NOT:
                varname = notInst(tree);
                break;
            case EQ:
            case NEQ:
            case LEQ:
            case LT:
            case GEQ:
            case GT:
                varname = processBooleanOperation(tree);
        }
        return varname;
    }


    private String processBooleanOperationNOT(AST tree) {
        AST left = tree.getLeft();
        AST right = tree.getRight();

        String leftResult = processArithOperation(left);
        String rightResult = processArithOperation(right);

        String varname = "";

        switch (tree.getType()) {
            case EQ:
                varname = ne(leftResult, rightResult);
                break;
            case NEQ:
                varname = eq(leftResult, rightResult);
                break;
            case LEQ:
                varname = sgt(leftResult, rightResult);
                break;
            case LT:
                varname = sge(leftResult, rightResult);
                break;
            case GEQ:
                varname = slt(leftResult, rightResult);
                break;
            case GT:
                varname = sle(leftResult, rightResult);
                break;
        }
        return varname;
    }

    private String processBooleanOperation(AST tree) {
        AST left = tree.getLeft();
        AST right = tree.getRight();

        String leftResult = processArithOperation(left);
        String rightResult = processArithOperation(right);

        String varname = "";

        switch (tree.getType()) {
            case EQ:
                varname = eq(leftResult, rightResult);
                break;
            case NEQ:
                varname = ne(leftResult, rightResult);
                break;
            case LEQ:
                varname = sle(leftResult, rightResult);
                break;
            case LT:
                varname = slt(leftResult, rightResult);
                break;
            case GEQ:
                varname = sge(leftResult, rightResult);
                break;
            case GT:
                varname = sgt(leftResult, rightResult);
                break;
        }
        return varname;
    }

    private String eq(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp eq i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    private String ne(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp ne i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    private String sgt(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp sgt i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    private String sge(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp sge i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    private String slt(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp slt i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    private String sle(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp sle i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
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
                String instruction = "";
                // if -variable
                if (tree.getValue().contains("-")) {
                    instruction = variable + " = load i32, i32* %"+tree.getValue().split("-")[1]+"\n";
                    String tempVariable = this.getLlvmVariable();
                    instruction += tempVariable + " = mul i32 -1, "+variable;
                    variable = tempVariable;
                }
                else {
                    instruction = variable + " = load i32, i32* %"+tree.getValue();
                }
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
        String varname = "%" + this.lastInstructionNumber;
        this.lastInstructionNumber++;
        return varname;
    }

    private int ifVariableNumber() {
        int var = this.ifNumber;
        this.ifNumber++;
        return var;
    }

    private int whileVariableNumber() {
        int var = this.whileNumber;
        this.whileNumber++;
        return var;
    }

    private int forVariableNumber() {
        int var = this.forNumber;
        this.forNumber++;
        return var;
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
