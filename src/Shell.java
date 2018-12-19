import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Shell {

    boolean errorState;

    Shell(){
        this.errorState = false;
    }

    public boolean getErrorState(){
        return this.errorState;
    }

    public String executeCommand(String command) {
        this.errorState = false;

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader readerError =
                    new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

            while ((line = readerError.readLine())!= null) {
                this.errorState = true;
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }
}
