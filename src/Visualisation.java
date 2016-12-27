import reseau.Reseau;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class Visualisation {

    public static Date date = new Date();
    //Reseau reseau = new Reseau();
    public static Fichier fichier = new Fichier();

    /* Main Interface de Visualisation */
    public static void main(String[] args) {

        String chaineCapteur = null;


        //******* Test ********/
        //do {
           // chaineCapteur = reseau.receptionMessage();
        chaineCapteur = "CapteurPresent;Identifiant1;TypeDuCapteur;Bâtiment;Etage;Salle;PositionRelative";
        traitement(chaineCapteur);
        chaineCapteur = "ValeurCapteur;Identifiant1;12";
        traitement(chaineCapteur);
        chaineCapteur = "CapteurPresent;Identifiant2;TypeDuCapteur;Bâtiment;Etage;Salle;PositionRelative";
        traitement(chaineCapteur);
        chaineCapteur = "ValeurCapteur;Identifiant2;122";
        traitement(chaineCapteur);

       // } while(true);
        /*************************/

    }

    public static void traitement(String chaineCapteur) {
        StringTokenizer Tok = new StringTokenizer(chaineCapteur,";");
        String type = (String)Tok.nextElement();

        switch(type) {

            case "CapteurPresent":
                String infos ="//";
                while (Tok.hasMoreElements()) {
                    infos = infos + (String)Tok.nextElement() + ";";
                }
                fichier.ajoutChaine(infos);
                break;

            case "InscriptionCapteurKO":
            case "DesinscriptionCapteurKO":
            case "CapteurDeco":
                break;

            case "ValeurCapteur":
                String val ="--";
                while (Tok.hasMoreElements()) {
                    val = val + (String)Tok.nextElement() + ";";
                }
                val =  val + date;
                fichier.ajoutChaine(val);
                break;

            default:
                break;
        }
    }


}
