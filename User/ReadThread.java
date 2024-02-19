package User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.Vector;

class ReadThread implements Runnable {
    private UserThreadInfo userThreadInfo;
    private static final int MAX_LEN = 1000;

    ReadThread(UserThreadInfo userThreadInfo) {
        this.userThreadInfo = userThreadInfo;
    }

    @Override
    public void run() {
        while (!User.finished) {
            byte[] buffer = new byte[ReadThread.MAX_LEN];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, userThreadInfo.getGroup(),
                    userThreadInfo.getPort());
            String message;
            try {
                userThreadInfo.getSocket().receive(datagram);
                message = new String(buffer, 0, datagram.getLength(), "UTF-8"); // tipo de dato recibido
                if (!message.startsWith(User.getUserName())) {

                    if (message.startsWith("GAME")) {
                        String[] parts = message.split(" ");
                        User.currentQuestion = new Vector<>(
                                Arrays.asList(parts[0], parts[1], parts[2], parts[3], parts[4]));
                    } /*
                       * else if (message.startsWith("FINISH")) {
                       * System.out.println(message);
                       * }
                       */else {
                        System.out.println(message);
                    }
                }

            } catch (IOException e) {
                System.out.println("Socket closed!");
            }
        }
    }
}
