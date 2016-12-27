import java.io.*;
import java.util.StringTokenizer;


public class Fichier {

    public Fichier() {
        try {
            File f = new File("config.txt");
            if (f.exists())
                f.delete();
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void ajoutChaine(String infos) {
        try {
            PrintStream printer = new PrintStream(new FileOutputStream("config.txt", true));
            printer.println(infos);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

