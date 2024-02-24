package User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Vector;

public class MulticastServer implements Runnable{
    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private KahootGame kahootGame;

    public MulticastServer(String group, int port, KahootGame kahootGame) throws IOException {
        this.group = InetAddress.getByName(group);
        this.port = port;
        this.kahootGame = kahootGame;
        socket = new MulticastSocket(port);
        socket.joinGroup(this.group);
    }

    public void start() {
        try {
            while (!kahootGame.isOver()) {
                Vector<String> question = kahootGame.getRandomQuestion();
                byte[] data = serializeQuestion(question); // Método a implementar para serializar la pregunta
                DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
                socket.send(packet);
                Thread.sleep(15000); // Esperar 15 segundos antes de enviar la siguiente pregunta
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private byte[] serializeQuestion(Vector<String> question) {
    String serialized = String.join(",", question); // Unir elementos del vector con comas
    return serialized.getBytes(); // Obtener los bytes del string
}

    @Override
    public void run() {
        try {
            while (!kahootGame.isOver()) {
                Vector<String> question = kahootGame.getRandomQuestion();
                byte[] data = serializeQuestion(question);
                DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
                socket.send(packet);
                Thread.sleep(15000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

}

