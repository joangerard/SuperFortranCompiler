import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * It is in charge of reading/writing text from/to a file.
 */
public class FileHandler {

    /**
     * Read a file and returns a list of strings.
     *
     * @param fileName String containing the filename to read.
     *
     * @return List<String>
     *
     * @throws IOException In case no file is found.
     */
    public static List<String> readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new java.io.FileReader(fileName));
        List<String> lines = new ArrayList<String>();
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();


            while (line != null) {
                lines.add(line);
                sb.append(line);
                line = br.readLine();
            }
            String everything = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return lines;
    }

    /**
     * It writes some content into a file.
     * @param content   String
     * @param filename  String
     *
     * @throws IOException In case no file is found or some other IO error occurs.
     */
    public static void writeInFile(String content, String filename) throws IOException {

        try (OutputStreamWriter writer =
                     new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8)) {
            writer.write(content);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
