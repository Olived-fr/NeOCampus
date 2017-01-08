package fenetreVisualisation;

import java.io.*;
import java.util.StringTokenizer;


public class Fichier {

    public void nouveauFichier(String chaineCapteur) {
        try {
            StringTokenizer Tok = new StringTokenizer(chaineCapteur,";");
            Tok.nextElement();
            String id = (String)Tok.nextElement();
            File f = new File(id+".txt");
            if (f.exists())
                f.delete();
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void ajoutChaine(String name, String infos) {
        try {
            PrintStream printer = new PrintStream(new FileOutputStream(name+".txt", true));
            printer.println(infos);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

