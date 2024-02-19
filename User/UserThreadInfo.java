package User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UserThreadInfo {
    private MulticastSocket socket;
    private InetAddress group;
    private int port;

    public UserThreadInfo(String group, int port) throws IOException {
        this.group = InetAddress.getByName(group);
        this.socket = new MulticastSocket(port);
        this.port = port;
    }

    public MulticastSocket getSocket() {
        return socket;
    }

    public void setSocket(MulticastSocket socket) {
        this.socket = socket;
    }

    public InetAddress getGroup() {
        return group;
    }

    public void setGroup(InetAddress group) {
        this.group = group;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
