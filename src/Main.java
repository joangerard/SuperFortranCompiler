import utils.codegeneration.AST;

import java.util.List;

public class Main{
    static ArgumentHandler argumentHandler;
    public static void main(String args[]){
        argumentHandler = new ArgumentHandler(args);
        if (args.length == 0) {
            System.out.println("No Super Fortran file specified. Please type --help for more info about how to use the command line.");
            System.out.println("java -jar part2.jar [OPTION(S)] [FILE]");
        }
        else {
            String filename = argumentHandler.giveSuperFortranFile();

            if (argumentHandler.shouldShowHelp()) {
                System.out.println("Command line:");
                System.out.println("");
                System.out.println("");
                System.out.println("");
                System.out.println("-v or --verbose\t\t Show the left most derivation tree detailed list.");
                System.out.println("-t\t\t\t\t\t Show the tokens list");
                System.out.println("-i\t\t\t\t\t Show the identifiers list.");
                System.out.println("-wt [FILENAME]\t\t Create a .tex file to see the Parse Tree. Default file: parse_tree.tex.");

            }

            if (filename.equals("")) {
                System.out.println("No Super Fortran file specified. Please type --help for more info about how to use the command line.");
                return;
            }
            String encodingName = "UTF-8";

            LexicalAnalyzer scanner = null;
            try {

                java.io.FileInputStream stream = new java.io.FileInputStream(filename);
                java.io.Reader reader = new java.io.InputStreamReader(stream, encodingName);
                scanner = new LexicalAnalyzer(reader);
                scanner.execute();
                //If the scanner did not recognize any error
                if (!scanner.gotAnyError()) {

                    List<String> lines = FileHandler.readFile(filename);
                    List<Symbol> tokens = scanner.getTokens();
                    SymbolMapperInterface symbolMapper = new SymbolMapper();
                    Parser parser = new Parser(tokens, lines, symbolMapper);
                    ParseTree parseTree = parser.getParseTree();
                    ASTGenerator astGenerator = new ASTGenerator();
                    AST astGenerated = astGenerator.create(parseTree);
                    CodeGenerator codeGenerator = new CodeGenerator();
                    String instructions = codeGenerator.llvm(astGenerated);
                    if (!parser.isSyntaxCorrect()) {
                        System.out.println("Syntax errors. Fix them in order to continue.");
                        System.out.println("");
                        System.out.println(parser.getError().getErrorMessage());
                    }
                    else {
                        if (argumentHandler.showIdentifiers()) {
                            scanner.printIdentifiers();
                        }

                        if (argumentHandler.showTokens()) {
                            scanner.printTokens();
                        }

                        if(argumentHandler.showVerbose()) {

                            parser.showDerivationRules();
                        }

                        if(argumentHandler.shouldWriteParseTreeText()) {
                            String latexFile = argumentHandler.giveTexFile();
                            if (latexFile.equals("")) {
                                latexFile = "parse_tree.tex";
                            }
                            FileHandler.writeInFile(parseTree.toLaTeX(),latexFile);
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println("Parse tree was created. Please check " + latexFile + " file.");
                        }

                        if(argumentHandler.shouldWriteASTText()) {
                            String latexFile =  "ast.tex";

                            FileHandler.writeInFile(astGenerated.toLaTeX(),latexFile);
                            System.out.println("Abstract syntax tree was created. Please check " + latexFile + " file.");
                        }

                        if (argumentHandler.shouldGenerateCodeAndSaveit()) {
                            String llFile = argumentHandler.giveLlvmFile();
                            if (llFile.equals("")) {
                                llFile = "outputFile.ll";
                            }

                            FileHandler.writeInFile(instructions, llFile);
                            System.out.println("LLVM file created. Please check. Please check " + llFile + " file.");
                        }
                        System.out.println();
                        System.out.println();
                        System.out.println(String.format("##### Llvm autogenerated code for file %s #######", filename));
                        System.out.println();
                        System.out.print(instructions);
                        //parser.showDerivationRulesShort();
                    }
                }
            }
            catch (java.io.FileNotFoundException e) {
                System.out.println("File not found : \""+filename+"\"");
            }
            catch (java.io.IOException e) {
                System.out.println("IO error scanning file \""+filename+"\"");
                System.out.println(e);
            }
            catch (Exception e) {
                System.out.println("Unexpected exception:");
                e.printStackTrace();
            }
        }
    }


}