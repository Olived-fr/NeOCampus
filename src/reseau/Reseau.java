package reseau;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Reseau {

    private final static int NUMERO_PORT = 7888;
    private Socket socket = null;

    public Reseau() {

        try {
            socket = new Socket(InetAddress.getLocalHost(), NUMERO_PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public Socket getSocket() {
        return socket;
    }
}
