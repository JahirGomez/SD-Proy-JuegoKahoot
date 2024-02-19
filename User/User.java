package User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Scanner;

public class User extends Thread {
    private static final String TERMINATE = "EXIT";
    static volatile boolean finished = false;
    private static String userName;
    private UserThreadInfo userThreadInfo;

    public User(String grp, int port, String name) throws IOException {
        userName = name;
        this.userThreadInfo = new UserThreadInfo(grp, port);
        this.userThreadInfo.getSocket().setTimeToLive(0);
        this.userThreadInfo.getSocket().joinGroup(this.userThreadInfo.getGroup());
        Thread t = new Thread(new ReadThread(this.userThreadInfo));
        this.startConversation(t);
    }

    public static String getUserName() {
        return userName;
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
            message = sc.nextLine();
            if (message.equals(User.TERMINATE)) {
                sc.close();
                System.out.println("\nYou have left the chat! Hope to see you soon.\n");
                message = userName + " has left the chat.";
                this.sendMessage(message);
                this.exit();
                break;
            }

            message = userName + ": " + message;
            this.sendMessage(message);
        }
    }

    private void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, this.userThreadInfo.getGroup(),
                this.userThreadInfo.getPort());
        this.userThreadInfo.getSocket().send(datagram);
    }

    private void exit() throws IOException {
        finished = true;
        this.userThreadInfo.getSocket().leaveGroup(this.userThreadInfo.getGroup());
        this.userThreadInfo.getSocket().close();
    }
}
