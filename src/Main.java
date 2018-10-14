public class Main{

    public static void main(String args[]){
        
        System.out.println("Main class");

        if (args.length == 0) {
            System.out.println("Usage : java LexicalAnalizer [ --encoding <name> ] <inputfile(s)>");
        }
        else {
            int firstFilePos = 0;
            String encodingName = "UTF-8";
            if (args[0].equals("--encoding")) {
                firstFilePos = 2;
                encodingName = args[1];
                try {
                    java.nio.charset.Charset.forName(encodingName); // Side-effect: is encodingName valid? 
                } catch (Exception e) {
                    System.out.println("Invalid encoding '" + encodingName + "'");
                    return;
                }
            }
            for (int i = firstFilePos; i < args.length; i++) {
                LexicalAnalizer scanner = null;
                try {
                    java.io.FileInputStream stream = new java.io.FileInputStream(args[i]);
                    java.io.Reader reader = new java.io.InputStreamReader(stream, encodingName);
                    scanner = new LexicalAnalyzer(reader);
                    scanner.yylex();
                }
                catch (java.io.FileNotFoundException e) {
                    System.out.println("File not found : \""+args[i]+"\"");
                }
                catch (java.io.IOException e) {
                    System.out.println("IO error scanning file \""+args[i]+"\"");
                    System.out.println(e);
                }
                catch (Exception e) {
                    System.out.println("Unexpected exception:");
                    e.printStackTrace();
                }
            }
        }
    }
}