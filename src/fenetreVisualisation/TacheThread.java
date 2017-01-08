package fenetreVisualisation;


import static fenetreVisualisation.FenVisu.tache;

public class TacheThread implements Runnable {

    public void run() {

        String chaineCapteur = "";
        while(!FenVisu.reseau.getSocket().isClosed()) {
            chaineCapteur = FenVisu.reseau.receptionMessage();
            if (chaineCapteur != null)
                FenVisu.traitement(chaineCapteur);
        }
    }
}
