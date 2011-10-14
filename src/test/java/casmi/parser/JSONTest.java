package casmi.parser;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import casmi.exception.ParserException;
import casmi.util.FileUtil;

/**
 * JSON test class.
 * 
 * @author T. Takeuchi
 * 
 */
public class JSONTest {

    /**
     * Inner class for JSON tests.
     * 
     * @author T. Takeuchi
     * 
     */
    public class Sake {
        public String name;
        public int abv;
        public String origin;

        public Sake() {}

        public Sake(String name, int abv, String origin) {
            this.name = name;
            this.abv = abv;
            this.origin = origin;
        }
    }

    @Test
    public void encodeTest1() {
        JSON json = new JSON();

        Sake[] sake = new Sake[2];
        sake[0] = new Sake("Urakasumi", 15, "Miyagi");
        sake[1] = new Sake("Houhai", 16, "Aomori");

        String out = json.encode(sake);
        System.out.println(out);
    }

    @Test
    public void encodeTest2() {
        JSON json = new JSON();
        XML xml = new XML();

        File xmlFile = new File( FileUtil.url2Uri(getClass().getResource("example.xml")) );
        
        try {
            xml.parseFile(xmlFile);
        } catch (ParserException e) {
            e.printStackTrace();
            fail("Failed to parse XML.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String out = json.encode(xml);
            System.out.println(out);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to encode from xml.");
        }
    }

    @Test
    public void decodeTest() {
        JSON json = new JSON();
        Sake sake = null;

        try {
            sake = json.decode("{\"name\":\"Urakasumi\", \"abv\":\"15\", \"origin\":\"Miyagi\"}", Sake.class);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to decode.");
        }

        System.out.println(sake.name + ", " + sake.abv + ", " + sake.origin);
    }

    @Test
    public void decodeTest2() {
        JSON json = new JSON();
        Sake[] sakes = null;

        File f = new File( FileUtil.url2Uri(getClass().getResource("example.json")) );
        
        try {
            sakes = json.decode(f, Sake[].class);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to decode.");
        }

        for (Sake sake : sakes) {
            System.out.println(sake.name + ", " + sake.abv + ", " + sake.origin);
        }
    }

}
