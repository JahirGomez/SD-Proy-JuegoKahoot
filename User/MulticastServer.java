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
        run();
    }
    
    private byte[] serializeQuestion(Vector<String> question) {
        String serialized = String.join(",", question); // Unir elementos del vector con comas
        return serialized.getBytes(); // Obtener los bytes del string
    }

    public void receiveUserData() {
        try {
            // Recibir el paquete del cliente
            byte[] buffer = new byte[1024]; // Tamaño del buffer para recibir datos
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            // Obtener los datos del paquete
            byte[] data = packet.getData();
            String message = new String(data);
            System.out.println(message);

            // Procesar el mensaje recibido (por ejemplo, guardar los datos en una base de datos)
            String[] userData = message.split(",");
            String username = userData[0];
            //int score = Integer.parseInt(userData[1]);
            /* String score = userData[1];
            String userAnswers = userData[2];

            System.out.println("USER: " + username + " SCORE: " + score + " ANSWERS: " + userAnswers); */

            // Realizar las operaciones necesarias con los datos recibidos
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            if(kahootGame.isOver()){
                byte[] data = new String("END").getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
                socket.send(packet);
                Thread.sleep(15000);
            }

            while (true) {
                receiveUserData();
            }

            

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Finalizo");
            socket.close();
        }
    }

}

