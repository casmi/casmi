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

package casmi.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import casmi.exception.ParserException;
import casmi.io.Reader;
import casmi.io.Writer;

/**
 * JSON parser class.
 * 
 * @author T. Takeuchi
 * 
 */
public class JSON {

    public JSON() {}

    public String encode(Object source) {
        
        return net.arnx.jsonic.JSON.encode(source, true);
    }

    public void encode(Object source, File file) throws IOException, ParserException {
        
        Writer writer = new Writer(file);
        try {
            net.arnx.jsonic.JSON.encode(source, writer, true);
        } catch (net.arnx.jsonic.JSONException e) {
            throw new ParserException(e.getMessage());
        } finally {
            writer.close();
        }
    }

    public String encode(XML xml) throws ParserException, SAXException, IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ParserException(e.getMessage());
        }
        InputSource is = new InputSource(new StringReader(xml.toString()));
        Document doc = builder.parse(is);
        return net.arnx.jsonic.JSON.encode(doc, true);
    }

    public <T> T decode(String source, Class<? extends T> cls) {

        return cls.cast(net.arnx.jsonic.JSON.decode(source, cls));
    }

    public <T> T decode(File file, Class<? extends T> cls)
        throws ParserException, FileNotFoundException, IOException {

        try {
            return net.arnx.jsonic.JSON.decode(new Reader(file), cls);
        } catch (net.arnx.jsonic.JSONException e) {
            throw new ParserException(e.getMessage());
        }
    }
}
