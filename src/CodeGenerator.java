import utils.codegeneration.AST;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible to generate LLVM code.
 */
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

    /**
     * Generates code
     * @param tree: AST
     * @return String
     */
    public String llvm(AST tree) {
        this.instructions.add("define i32 @main() {");
        this.instructions.add("entry:");
        this.program(tree);
        this.instructions.add("ret i32 0");
        this.instructions.add("}");
        return getInstructions();
    }

    /**
     * Process program tree.
     * @param tree: AST
     */
    private void program(AST tree) {
        AST variablesTree = tree.getLeft();
        AST codeTree = tree.getRight();

        if (variablesTree != null) {
            variables(variablesTree);
        }

        if (codeTree != null) {
            code(codeTree);
        }
    }

    /**
     * Process variable tree.
     * @param tree: AST
     */
    private void variables(AST tree) {
        AST variable = tree.getRight();

        if (variable != null){
            initializeVariable(variable);
            variables(variable);
        }
    }

    /**
     * Process code tree.
     * @param tree: AST
     */
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

    /**
     * Process instruction tree.
     * @param tree: AST
     */
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

    /**
     * Process read instruction tree.
     * @param tree: AST
     */
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

    /**
     * Process print instruction tree.
     * @param tree: AST
     */
    private void print(AST tree) {
        AST left = tree.getLeft();
        AST right = tree.getRight();

        String exprLeft = processArithOperation(left);
        instructions.add(String.format("call void @println(i32 %s)", exprLeft));

        if (right != null) {
            print(right);
        }
    }

    /**
     * Process assign instruction tree.
     * @param tree: AST
     * @return String containing the last variable used in llvm code.
     */
    private String assign(AST tree) {
        AST variable = tree.getLeft();
        AST result = tree.getRight();


        String varname = "%" + variable.getValue();
        String value = processArithOperation(result);

        String instruction = String.format("store i32 %s, i32* %s", value, varname);
        instructions.add(instruction);

        return variable.getValue();
    }

    /**
     * Process for instruction tree.
     * @param tree: AST
     */
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

    /**
     * Process while instruction tree.
     * @param tree: AST
     */
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

    /**
     * Process if-else instruction tree.
     * @param tree: AST
     */
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

    /**
     * Process IF instruction tree.
     * @param tree: AST
     */
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

    /**
     * Process AND instruction tree.
     * @param tree: AST
     */
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

    /**
     * Process OR instruction tree.
     * @param tree: AST
     * @return String containing the last variable used in llvm code.
     */
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

    /**
     * Process NOT instruction tree.
     * @param tree: AST
     * @return String containing the last variable used in llvm code.
     */
    private String notInst(AST tree) {
        AST right = tree.getRight();
        return processBooleanOperationNOT(right);
    }

    /**
     * Process a boolean instruction tree.
     * @param tree: AST
     * @return String containing the last variable used in llvm code.
     */
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

    /**
     * Process comparison operation instruction tree for the NOT instruction.
     * @param tree: AST
     * @return String containing the last variable used in llvm code.
     */
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

    /**
     * Process comparison operation instruction tree.
     * @param tree: AST
     * @return String containing the last variable used in llvm code.
     */
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

    /**
     * Process EQ instruction tree.
     *
     * @param val1 String
     * @param val2 String
     * @return String containing the last varname used in LLVM.
     */
    private String eq(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp eq i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    /**
     * Process NEQ instruction tree.
     *
     * @param val1 String
     * @param val2 String
     * @return String containing the last varname used in LLVM.
     */
    private String ne(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp ne i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    /**
     * Process SGT greater instruction tree.
     *
     * @param val1 String
     * @param val2 String
     * @return String containing the last varname used in LLVM.
     */
    private String sgt(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp sgt i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    /**
     * Process SGE greater or equals instruction tree.
     *
     * @param val1 String
     * @param val2 String
     * @return String containing the last varname used in LLVM.
     */
    private String sge(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp sge i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    /**
     * Process SLT less than instruction tree.
     *
     * @param val1 String
     * @param val2 String
     * @return String containing the last varname used in LLVM.
     */
    private String slt(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp slt i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    /**
     * Process SLE less or equals than instruction tree.
     *
     * @param val1 String
     * @param val2 String
     * @return String containing the last varname used in LLVM.
     */
    private String sle(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = icmp sle i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    /**
     * Process arithmetic instruction tree.
     *
     * @param tree AST
     * @return String containing the last varname used in LLVM.
     */
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

    /**
     * Process PLUS instruction tree.
     *
     * @param val1 String
     * @param val2 String
     * @return String containing the last varname used in LLVM.
     */
    private String plus(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = add i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    /**
     * Process MINUS instruction tree.
     *
     * @param val1 String
     * @param val2 String
     * @return String containing the last varname used in LLVM.
     */
    private String minus(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = sub i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    /**
     * Process TIMES instruction tree.
     *
     * @param val1 String
     * @param val2 String
     * @return String containing the last varname used in LLVM.
     */
    private String times(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = mul i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    /**
     * Process DIVIDE instruction tree.
     *
     * @param val1 String
     * @param val2 String
     * @return String containing the last varname used in LLVM.
     */
    private String divide(String val1, String val2) {
        String varname = getLlvmVariable();
        String instruction = String.format("%s = sdiv i32 %s, %s", varname, val1, val2);
        this.instructions.add(instruction);
        return varname;
    }

    /**
     * Responsible to process variables (number or variable).
     *
     * @param tree AST
     * @return String
     */
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

    /**
     * Initialize variable in llvm.
     *
     * @param tree AST
     */
    private void initializeVariable(AST tree) {
        String varname = "%" + tree.getValue();
        this.instructions.add(String.format("%s = alloca i32", varname));
    }

    /**
     * Returns a llvm variable.
     *
     * @param tree AST
     * @return String
     */
    private String getLlvmVariable() {
        String varname = "%" + this.lastInstructionNumber;
        this.lastInstructionNumber++;
        return varname;
    }

    /**
     * Returns a llvm variable for ifs.
     *
     * @param tree AST
     * @return String
     */
    private int ifVariableNumber() {
        int var = this.ifNumber;
        this.ifNumber++;
        return var;
    }

    /**
     * Returns a llvm variable for whiles.
     *
     * @param tree AST
     * @return String
     */
    private int whileVariableNumber() {
        int var = this.whileNumber;
        this.whileNumber++;
        return var;
    }

    /**
     * Returns a llvm variable for fors.
     *
     * @param tree AST
     * @return String
     */
    private int forVariableNumber() {
        int var = this.forNumber;
        this.forNumber++;
        return var;
    }

    /**
     * Returns llvm instructions.
     *
     * @param tree AST
     * @return String
     */
    private String getInstructions() {
        StringBuilder insts = new StringBuilder();
        for (String instruction :
                this.instructions) {
            insts.append(instruction);
            insts.append("\n");
        }
        return insts.toString();
    }

    /**
     * Initialize main methods needed in Llvm.
     *
     * @param tree AST
     */
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
