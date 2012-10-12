package casmi.net;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import casmi.exception.NetException;
import casmi.exception.ParserException;
import casmi.io.Reader;
import casmi.parser.XML;
import casmi.parser.XMLElement;
import casmi.util.SystemUtil;

public class OAuthTest {

    private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    private static final String AUTHORIZE_URL     = "https://api.twitter.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL  = "https://api.twitter.com/oauth/access_token";
    
    private static final String CONSUMER_KEY      = "Your Consumer Key";
    private static final String CONSUMER_SECRET   = "Your Consumer Secret";
    
    private static final String HOME_TIMELINE_URL = "http://api.twitter.com/1/statuses/home_timeline.xml";
    
    private OAuth oauth = new OAuth();
    
    @Before
    public void twitterAuthorizeTest() {
        
        oauth.setConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        oauth.setProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZE_URL);
        String url = null;
        try {
            url = oauth.retrieveRequestToken();
        } catch (NetException e) {
            e.printStackTrace();
            fail();
        }
        
        // Browse an authorization page.
        try {
            SystemUtil.browse(new URL(url));
        } catch (NetException e) {
            e.printStackTrace();
            fail();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }

        // Input a PIN code.
        System.out.println("Input the PIN code to authorize.");
        System.out.print("PIN: ");
        Reader reader = new Reader(System.in);
        String pin = null;
        try {
            pin = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } finally {
            reader.close();
        }
        
        // Retrieve an access token and a secret.
        try {
            oauth.retrieveAccessToken(pin);
        } catch (NetException e) {
            e.printStackTrace();
            fail();
        }
        oauth.setTokenWithSecret(oauth.getToken(), oauth.getTokenSecret());
    }
    
    @Test
    public void twitterReadTest() {
        HTTP http = null;
        Reader reader = null;

        try {
            http = new HTTP(HOME_TIMELINE_URL);
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
                String user = status.getChildren("user")[0].getChildren("screen_name")[0].getContent();
                String text = status.getChildren("text")[0].getContent();

                System.out.println("@" + user + ": " + text);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to read xml.");
        } catch (ParserException e) {
            e.printStackTrace();
            fail("Failed to parse.");
        }

        http.disconnect();
        reader.close();
    }

}
