package casmi.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPSocketClient {

    private final String host;
    private final int port;

    private DatagramSocket socket;
    InetSocketAddress address;

    public UDPSocketClient(String host, int port) throws SocketException {
        this.host = host;
        this.port = port;

        socket = new DatagramSocket();
        address = new InetSocketAddress(host, port);
    }

    public void send(byte[] data) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, 0, data.length, address);
        socket.send(packet);
    }

    public void send(char data) throws IOException {
        byte[] bytes = { (byte)data };
        send(bytes);
    }

    public void send(int data) throws IOException {
        BigInteger intValue = BigInteger.valueOf(data);
        byte[] bytes = intValue.toByteArray();
        send(bytes);
    }

    public void send(String data) throws IOException {
        try {
            send(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
