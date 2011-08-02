/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 *  casmi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package casmi.net;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import casmi.io.Reader;
import casmi.parser.XML;
import casmi.parser.XMLElement;

public class HTTPTest {

    @Test
    public void simpleGetTest() {
        HTTP http = null;
        Reader reader = null;

        try {
            http = new HTTP("http://www.xcoo.jp/");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create instance.");
        }

        try {
            reader = http.requestGet();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to request.");
        }

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to read.");
        }

        http.disconnect();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simplePostTest() {
        HTTP http = null;
        Reader reader = null;

        try {
            http = new HTTP("http://www.xcoo.jp/");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create instance.");
        }

        try {
            reader = http.requestPost("");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to request.");
        }

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to read.");
        }

        http.disconnect();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void httpAndXmlTest() {
        HTTP http = null;
        Reader reader = null;

        try {
            http = new HTTP("http://api.twitter.com/1/statuses/public_timeline.xml");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create instance.");
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
                System.out.println(status.getChild("text").getContent());
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to read.");
        }

        http.disconnect();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
