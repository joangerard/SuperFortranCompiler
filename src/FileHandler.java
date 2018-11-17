import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static List<String> readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new java.io.FileReader(fileName));
        List<String> lines = new ArrayList<String>();
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();


            while (line != null) {
                sb.append(line);
                line = br.readLine();
                lines.add(line);
            }
            String everything = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return lines;
    }

    public static void writeInFile(String content, String filename) {

        try (OutputStreamWriter writer =
                     new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8)) {
            writer.write(content);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
