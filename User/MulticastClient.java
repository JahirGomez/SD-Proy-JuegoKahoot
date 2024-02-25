package User;

import java.awt.EventQueue;
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
    private MainGUI mainGUI;
    private int score;

    public MulticastClient(String group, int port, String username) throws IOException {
        this.group = InetAddress.getByName(group);
        this.port = port;
        this.username = username;
        socket = new MulticastSocket(port);
        socket.joinGroup(this.group);
        this.score = 0;
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

                if (question.get(0).equals("END")){
                    if (mainGUI != null) {
                        score = score + mainGUI.getScore();
                        System.out.println("FINAL SCORE: " + score);
                        ResultGUI resultGUI = new ResultGUI(username, score); // Crear ventana ResultGUI con el puntaje
                        resultGUI.setVisible(true);
                        mainGUI.dispose(); // Cerrar la instancia existente de MainGUI si existe
                    }
                }else{
                    EventQueue.invokeLater(() -> {
                        if (mainGUI != null) {
                            score = score + mainGUI.getScore();
                            System.out.println("SCORE:"+ score );
                            mainGUI.dispose(); // Cerrar la instancia existente de MainGUI si existe
                        }
                        mainGUI = new MainGUI(username, question); // Crear una nueva instancia de MainGUI
                    });
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }



}
