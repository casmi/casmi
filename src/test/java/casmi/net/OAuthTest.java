package casmi.net;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import casmi.exception.NetException;
import casmi.extension.net.OAuth;
import casmi.io.Reader;
import casmi.parser.XML;
import casmi.parser.XMLElement;

public class OAuthTest {

    @Test
    public void test() {
        OAuth oauth = new OAuth();
        HTTP http = null;
        Reader reader = null;

        oauth.setConsumer("ybqI6NgKiRv8cvo86zE1xw", "uOzNUoFHuaoqr6nL2xJkOIT3WmKOpRq1oheY6rRfI");

        oauth.setTokenWithSecret("102987848-gGurDKBC7DGCjJXcX2BcS1LVkL8OylBXiOsJSxNs",
            "Tl5DF7cTeGwOlnTb3VwvfBgKZSNYLLzQQwQ2zL9iE");

        try {
            http = new HTTP("http://api.twitter.com/1/statuses/home_timeline.xml");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create instance.");
        }

        try {
            oauth.sign(http);
        } catch (NetException e1) {
            e1.printStackTrace();
        }

        try {
            reader = http.requestGet();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to request.");
        }

        try {
            XML xml = new XML();
            xml.parseString(reader.readAll());

            for (XMLElement status : xml.getChildren()) {
                String user = status.getChild("user").getChild("screen_name").getContent();
                String text = status.getChild("text").getContent();

                System.out.println("@" + user + ": " + text);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to read xml.");
        }

        http.disconnect();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }    }

}
