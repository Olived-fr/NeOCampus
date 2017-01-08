package reseau;

import fenetreVisualisation.FenVisu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;


public class Reseau {

    private final static int NUMERO_PORT= 7888;

    private Socket socket = null;

    public Reseau() {

        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"),NUMERO_PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connexionVisu() {
        PrintStream printer = null;
        try {
            if (this.getSocket().isClosed()) {
                socket = new Socket(InetAddress.getByName("127.0.0.1"),NUMERO_PORT);
            }
            printer = new PrintStream(this.getSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Random rand = new Random();
        String chaineCapteur = null;
        chaineCapteur = "ConnexionVisu;"+rand.nextInt(Integer.MAX_VALUE)+1;
        printer.println(chaineCapteur);
    }

    public void deconnexionVisu() {
        PrintStream printer = null;
        try {
            printer = new PrintStream(this.getSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        printer.println("DeconnexionVisu");
        deconnexion();
    }

    public void deconnexion() {

        try {
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receptionMessage() {
        BufferedReader reader = null;
        String chaineCapteur = null;
        if (!FenVisu.reseau.getSocket().isClosed()) {
            try {
                reader = new BufferedReader(new InputStreamReader(this.getSocket().getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                chaineCapteur = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return chaineCapteur;
    }

    public void inscriptionCapteur(String chaineCapteur) {
        PrintStream printer = null;
        try {
            printer = new PrintStream(this.getSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        printer.println("InscriptionCapteur;"+chaineCapteur);
    }

    public void desinscriptionCapteur(String chaineCapteur) {
        PrintStream printer = null;
        try {
            printer = new PrintStream(this.getSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        printer.println("DesinscriptionCapteur;"+chaineCapteur);
    }

    public Socket getSocket() {
        return socket;
    }
}
