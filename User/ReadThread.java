package User;

import java.io.IOException;
import java.net.DatagramPacket;

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
                message = new String(buffer, 0, datagram.getLength(), "UTF-8");
                if (!message.startsWith(User.getUserName()))
                    System.out.println(message);
            } catch (IOException e) {
                System.out.println("Socket closed!");
            }
        }
    }
}
