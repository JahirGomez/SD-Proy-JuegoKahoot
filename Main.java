
import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

import User.User;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String group = "239.0.0.0";
        int port = 1234;
        System.out.print("Please, enter a nickname: ");
        String name = sc.nextLine();
        try {
            new User(group, port, name);
        } catch (SocketException se) {
            System.out.println("Error creating socket");
            se.printStackTrace();
        } catch (IOException ie) {
            System.out.println("Error reading/writing from/to socket");
            ie.printStackTrace();
        }

    }
}
