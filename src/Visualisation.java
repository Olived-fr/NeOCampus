import reseau.Reseau;

import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;

public class Visualisation {

    public static Date date = new Date();
    //Reseau reseau = new Reseau();

    /* Main Interface de Visualisation */
    public static void main(String[] args) {

        String chaineCapteur = null;

        //do {
           // chaineCapteur = reseau.receptionMessage();
        chaineCapteur = "CapteurPresent;Identifiant1;TypeDuCapteur;Bâtiment;Etage;Salle;PositionRelative";
        traitement(chaineCapteur);
        chaineCapteur = "ValeurCapteur;Identifiant1;12";
        traitement(chaineCapteur);

       // } while(true);


    }

    public static void traitement(String chaineCapteur) {
        StringTokenizer Tok = new StringTokenizer(chaineCapteur,";");
        String id = (String)Tok.nextElement();

        switch(id) {

            case "CapteurPresent":
                Fichier fichier = new Fichier(chaineCapteur);
                break;

            case "InscriptionCapteurKO":
            case "DesinscriptionCapteurKO":
            case "CapteurDeco":
                break;

            case "ValeurCapteur":
                id = (String) Tok.nextElement();
                try {
                    String chaine = (String)Tok.nextElement() + " " + date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"\n";
                    System.out.println(chaine);
                    FileWriter fw = new FileWriter(id+".txt", true);
                    fw.write(chaine);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;

            default:
                break;
        }
    }


}
