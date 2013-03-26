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
 * <p>
 * This class uses JSONIC library
 * (jsonic-1.2.5) licensed by Apache 2.0 license.
 * </p>
 *
 * @see casmi.exception.ParserException
 *
 * @author T. Takeuchi
 *
 */
public class JSON {

    /**
     * Creates a JSON object.
     */
    public JSON() {}

    /**
     * Encodes a object into a JSON string.
     *
     * @param source A object to encode.
     * @return A JSON string.
     */
    public String encode(Object source) {

        return net.arnx.jsonic.JSON.encode(source, true);
    }

    /**
     * Encodes a object into a JSON string and outputs a file.
     *
     * @param source A object to encode.
     * @param file An output file.
     *
     * @throws IOException
     *             If an I/O error is occurred.
     * @throws ParserException
     *             If an error is occurred while parsing the read data.
     */
    public void encode(Object source, File file) throws IOException, ParserException {

        Writer writer = null;

        try {
            writer = new Writer(file);
            net.arnx.jsonic.JSON.encode(source, writer, true);
        } catch (net.arnx.jsonic.JSONException e) {
            throw new ParserException(e.getMessage());
        } finally {
            if (writer != null) writer.close();
        }
    }

    /**
     * Convert a XML object to a JSON string.
     *
     * @param xml
     *
     * @return
     *     JSON string
     *
     * @throws ParserException
     * @throws SAXException
     * @throws IOException
     */
    public String encode(XML xml) throws ParserException, SAXException, IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        InputSource is = new InputSource(new StringReader(xml.toString()));

        try {
            builder = dbFactory.newDocumentBuilder();
            Document doc = builder.parse(is);
            return net.arnx.jsonic.JSON.encode(doc, true);
        } catch (SAXException e) {
            throw new ParserException(e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new ParserException(e.getMessage());
        }
    }

    /**
     * Decodes a JSON string into a typed object.
     *
     * @param source A Json string to decode.
     * @param cls Class for converting.
     *
     * @return A decoded object.
     */
    public <T> T decode(String source, Class<? extends T> cls) {

        return net.arnx.jsonic.JSON.decode(source, cls);
    }

    /**
     * Decodes a JSON file into a typed object.
     *
     * @param file A JSON file to decode.
     * @param cls Class for converting.
     *
     * @return A decoded object.
     *
     * @throws ParserException
     *             If an error is occurred while parsing the read data.
     * @throws IOException
     *             If an I/O error is occurred.
     */
    public <T> T decode(File file, Class<? extends T> cls)
        throws ParserException, IOException {

        return decode(new Reader(file), cls);
    }

    /**
     * Decodes a JSON reader into a typed object.
     *
     * @param reader A JSON reader to decode.
     * @param cls Class for converting.
     *
     * @return A decoded object.
     *
     * @throws ParserException
     *             If an error is occurred while parsing the read data.
     * @throws IOException
     *             If an I/O error is occurred.
     */
    public <T> T decode(Reader reader, Class<? extends T> cls)
        throws ParserException, IOException {

        try {
            return net.arnx.jsonic.JSON.decode(reader, cls);
        } catch (net.arnx.jsonic.JSONException e) {
            throw new ParserException(e.getMessage());
        }
    }
}
