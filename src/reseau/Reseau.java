package reseau;

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

    private final static int NUMERO_PORT_SIMU = 7888;
    private final static int NUMERO_PORT_VISU = 7889;
    private Socket socket = null;

    public Reseau(int num) {

        try {
            if (num ==1)
            socket = new Socket(InetAddress.getByName("127.0.0.1"),NUMERO_PORT_SIMU);
            else if (num == 2)
                socket = new Socket(InetAddress.getByName("127.0.0.1"),NUMERO_PORT_VISU);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connexionVisu() {
        PrintStream printer = null;
        try {
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
