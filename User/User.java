package User;

import java.io.IOException;
import java.net.SocketException;

public class User {
    private String username;
    private boolean isServer;
    private static final String HOST_USERNAME = "HOST";
    private static final String GROUP = "239.0.0.0";
    private static final int PORT = 1234;

    public User(String username) {
        this.username = username;
        this.isServer = username.equals(HOST_USERNAME);
    }

    public void start() {
        if (isServer) {
            startServer();
        } else {
            startClient();
        }
    }

    private void startServer() {
        // Código para iniciar el servidor
        try {
            KahootGame kahootGame = new KahootGame("preguntas.txt"); // Suponiendo que tienes un archivo preguntas.txt
            MulticastServer multicastServer = new MulticastServer(GROUP, PORT, kahootGame);
            Thread serverThread = new Thread(multicastServer);
            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startClient() {
        // Código para iniciar el cliente
        try {
            MulticastClient multicastClient = new MulticastClient(GROUP, PORT, username);
            Thread clientThread = new Thread(multicastClient);
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
