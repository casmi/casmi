package casmi.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPSocketServer {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private final int port;

    private DatagramSocket socket;

    public UDPSocketServer(int port) throws IOException {
        this.port = port;
        socket = new DatagramSocket(port);
    }

    public byte[] receive() throws IOException {
        return receive(DEFAULT_BUFFER_SIZE);
    }

    public byte[] receive(int size) throws IOException {
        byte[] array = new byte[size];
        DatagramPacket packet = new DatagramPacket(array, array.length);
        socket.receive(packet);
        return packet.getData();
    }

    public String receiveStr() throws IOException {
        return receiveStr(DEFAULT_BUFFER_SIZE);
    }

    public String receiveStr(int size) throws IOException {
        byte[] array = new byte[size];
        DatagramPacket packet = new DatagramPacket(array, array.length);
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

    public int getPort() {
        return port;
    }
}
