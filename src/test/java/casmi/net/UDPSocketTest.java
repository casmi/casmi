package casmi.net;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.SocketException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class UDPSocketTest {

    private static final String HOST = "127.0.0.1";
    private static final int    PORT = 8012;
    
    private static UDPSocketServer server;
    private static UDPSocketClient client;
    
    @BeforeClass
    public static void beforeClass() {
        try {
            server = new UDPSocketServer(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            fail("fail to construct");
        }
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                while(true) {
                    try {
                        String str = server.receiveStr();
                        System.out.println(str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                }
            }
        }).start();
        
        try {
            client = new UDPSocketClient(HOST, PORT);
        } catch (SocketException e) {
            e.printStackTrace();
            fail("fail to construct");
        }
    }
    
    @AfterClass
    public static void afterClass() {
    }
    
    @Test
    public void test() {
        try {
            for (int i = 0; i < 10; i++) {
                client.send("This is a UDPSocketText");
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("fail to send");
        }
    }

}
