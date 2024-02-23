package User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Scanner;
import java.util.Vector;

public class User extends Thread {
    private static final String TERMINATE = "EXIT";
    static volatile boolean finished = false;
    private static String userName;
    private UserThreadInfo userThreadInfo;
    private int indexAnwer;
    

    public static KahootGame game = null;
    public static Vector<String> currentQuestion = new Vector<>();
    

    public User(String grp, int port, String name)  throws IOException, InterruptedException {
        userName = name;
        this.userThreadInfo = new UserThreadInfo(grp, port);
        this.userThreadInfo.getSocket().setTimeToLive(0);
        this.userThreadInfo.getSocket().joinGroup(this.userThreadInfo.getGroup());
        Thread t = new Thread(new ReadThread(this.userThreadInfo));
        this.directedUSer(t);
    }

    public static String getUserName() {
        return userName;
    }

    public String showUserQuestion(Vector<String> questionInfo) {
        currentQuestion = questionInfo;
        return questionInfo.toString();
    }

    public void directedUSer(Thread t) throws IOException, InterruptedException {
        if (userName.equals("GAME")) {
            this.beginGame(t);
        } else {
            startConversation(t);
        }
    }

    public void beginGame(Thread t) throws IOException, InterruptedException {
        System.out.println("This will send the questions");
        KahootGame game = new KahootGame("preguntas.txt");
        String message = "";
        while (true) {

            if (game.isOver()) {
                break;
            }/*  */

            message = this.showUserQuestion(game.getRandomQuestion(indexAnwer));
            new MainGUI(getUserName(), currentQuestion);
            
            
       
            System.out.println("Sending question: " + currentQuestion.get(0));
            message = "QUESTION: " + message;
            this.sendMessage(message);
            int seconds = 20;
            Thread.sleep(1000 * seconds);
        }

        // userName = "FINISH";
        System.out.println("Out of questions, finishing game...");
        String winner = giveResults(); // todo Set a way to keep tab of the scores and a way to see the winner
        message = "GAME OVER! The winner is " + winner;
        this.sendMessage(message);
        this.exit();

    }

    public String giveResults() {
        return "Winner!";
    }

    public boolean verifyAnswer(String letter) {
        Vector<String> answers = new KahootGame("preguntas.txt").getAnswers();
        boolean correct = false;
        System.err.println("Correct Answer "+ answers.get(indexAnwer));
        if(letter == answers.get(indexAnwer)){
            correct = true;
        }
        return correct;

    }

    public boolean isAnswer(String message) {
        return message.equals("A") || message.equals("B") || message.equals("C") || message.equals("D");
    }

    public void startConversation(Thread t) throws IOException {
        Scanner sc = new Scanner(System.in);
        t.start();

        System.out.println("Hello! Welcome " + this.getUserName() + ", you have joined the chat!\n");
        System.out.println("To exit the chat, type 'EXIT'");
        System.out.println("Type a message...\n");
        String message = userName + " has joined the chat!";
        this.sendMessage(message);

        while (true) {
            byte[] buffer = new byte[1024];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
            this.userThreadInfo.getSocket().receive(datagram);

            System.out.println("tengo  " + new String(datagram.getData(), 0, datagram.getLength())+"algo");

            message = sc.nextLine();

            if (message.equals(User.TERMINATE)) {
                sc.close();
                System.out.println("\nYou have left the chat! Hope to see you soon.\n");
                message = userName + " has left the chat.";
                this.sendMessage(message);
                this.exit();
                break;
            }

            String answer = "";
            if (isAnswer(message)) {
                boolean correct = verifyAnswer(message);
                answer = correct ? "correct" : "incorrect";
                message = userName + " choose the " + answer + " answer!";
            } else {
                message = userName + ": " + message;
            }

            this.sendMessage(message);

        }
    }

    private void sendMessage(String message) throws IOException { // * NO CAMBIAR */
        byte[] buffer = message.getBytes();
        DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, this.userThreadInfo.getGroup(),
                this.userThreadInfo.getPort());
        this.userThreadInfo.getSocket().send(datagram);
    }

    private void exit() throws IOException { // * NO CAMBIAR */
        finished = true;
        this.userThreadInfo.getSocket().leaveGroup(this.userThreadInfo.getGroup());
        this.userThreadInfo.getSocket().close();
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024]; // Define un buffer para almacenar los datos recibidos

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // Crea un DatagramPacket para recibir datos
                userThreadInfo.getSocket().receive(packet); // Espera a recibir un paquete de datos
                String message = new String(packet.getData(), 0, packet.getLength()); // Convierte los datos recibidos a String
                System.out.println("Imprimir mensaje " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
