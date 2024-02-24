package User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.Vector;

public class MulticastClient implements Runnable{
    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private String username;

    public MulticastClient(String group, int port, String username) throws IOException {
        this.group = InetAddress.getByName(group);
        this.port = port;
        this.username = username;
        socket = new MulticastSocket(port);
        socket.joinGroup(this.group);
    }

    public void start() {
        try {
            while (true) {
                byte[] buffer = new byte[1024]; // Tamaño del buffer para recibir datos
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                Vector<String> question = deserializeQuestion(packet.getData()); // Método a implementar para deserializar la pregunta
                // Mostrar la pregunta en la interfaz gráfica, etc.
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private Vector<String> deserializeQuestion(byte[] data) {
        String receivedData = new String(data); // Convertir los bytes a un string
        String[] elements = receivedData.split(","); // Dividir el string en elementos individuales
        return new Vector<>(Arrays.asList(elements)); // Convertir el array de strings a un vector
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                Vector<String> question = deserializeQuestion(packet.getData());
                new MainGUI(username, question);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }



}
