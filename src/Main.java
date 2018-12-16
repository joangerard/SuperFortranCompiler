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
                    SemanticHelper semanticHelper = new SemanticHelper();
                    ParseTree abstractSemanticTree = semanticHelper.getAbstractTree(parseTree);
                    LlvmCreator llvmCreator = new LlvmCreator();
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

                        if(argumentHandler.shouldWriteAbstractSyntaxTreeText()) {
                            String latexFile = argumentHandler.giveTexFile();
                            if (latexFile.equals("")) {
                                latexFile = "abstract_syntax_tree.tex";
                            }
                            FileHandler.writeInFile(abstractSemanticTree.toLaTeX(),latexFile);
                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println("Abstract syntax tree was created. Please check " + latexFile + " file.");
                        }

                        if(argumentHandler.shouldGenerateLlvmFile()){
                            String content = llvmCreator.create(abstractSemanticTree);
                            String llFile = argumentHandler.giveLlFile();
                            if (llFile.equals("")) {
                                llFile = "auto-generated.ll";
                            }
                            FileHandler.writeInFile(content, llFile);

                            System.out.println();
                            System.out.println();
                            System.out.println();
                            System.out.println("Llvm file was created. Please check " + llFile + " file.");
                        }

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