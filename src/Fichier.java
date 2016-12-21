import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by Olivier DUFOUR on 21/12/2016.
 */
public class Fichier {

    public Fichier (String chaineCapteur) {
        try {
            StringTokenizer Tok = new StringTokenizer(chaineCapteur,";");
            Tok.nextElement();
            String id = (String)Tok.nextElement();
            File f = new File(id+".txt");
            if(f.exists())
                f.delete();
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
