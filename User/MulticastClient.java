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
    private boolean isOver;

    public MulticastClient(String group, int port, String username) throws IOException {
        this.group = InetAddress.getByName(group);
        this.port = port;
        this.username = username;
        socket = new MulticastSocket(port);
        socket.joinGroup(this.group);
        this.score = 0;
        this.isOver = false;
    }

    private Vector<String> deserializeQuestion(byte[] data) {
        String receivedData = new String(data); // Convertir los bytes a un string
        String[] elements = receivedData.split(","); // Dividir el string en elementos individuales
        return new Vector<>(Arrays.asList(elements)); // Convertir el array de strings a un vector
    }

    public void sendUserData(String username, int score) {
        try {
            // Crear un mensaje que contenga la información del cliente
            System.out.println("user " + username);
            System.out.println("score " + score);
            String message = "Usuario: ," + username + ",Score" + score;
            System.out.println("Mensaje " + message);

            // Convertir el mensaje en bytes
            byte[] data = message.getBytes();

            // Crear un paquete DatagramPacket con los datos y la dirección del servidor
            DatagramPacket packet = new DatagramPacket(data, data.length, group, port);

            // Enviar el paquete al servidor
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                if (new String(packet.getData()).trim().equals(new String("END").trim())){
                    isOver = true;
                    if (mainGUI != null) {
                        score = score + mainGUI.getScore();
                        System.out.println("FINAL SCORE: " + score);
                        ResultGUI resultGUI = new ResultGUI(username, score); // Crear ventana ResultGUI con el puntaje
                        sendUserData(username, score);
                        resultGUI.setVisible(true);
                        mainGUI.dispose(); // Cerrar la instancia existente de MainGUI
                    }
                }else{
                    if (isOver == false) {
                        Vector<String> question = deserializeQuestion(packet.getData());
                        EventQueue.invokeLater(() -> {
                            if (mainGUI != null) {
                                score = score + mainGUI.getScore();
                                System.out.println("SCORE:"+ score );
                                mainGUI.dispose(); // Cerrar la instancia existente de MainGUI si existe
                            }
                            mainGUI = new MainGUI(username, question); // Crear una nueva instancia de MainGUI
                        });
                    }else{
                        System.out.println(new String(packet.getData()));
                    }
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Finalizo el hilo");
            //socket.close();
        }
    }



}
