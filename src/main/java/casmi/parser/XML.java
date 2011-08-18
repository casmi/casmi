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
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import nanoxml.XMLParseException;
import casmi.exception.ParserException;
import casmi.io.Writer;

/**
 * XML class.
 * 
 * <p>
 * Extends XMLElement.class and expresses top of XML structure. Use this class
 * when you want to read or write a XML file.
 * </p>
 * 
 * @see casmi.exception.ParseException
 * 
 * @author T. Takeuchi
 * 
 */
public class XML extends XMLElement {

    /** The default indent string is "tab." */
    private static final String DEFAULT_INDENT = "\t";

    /** The string added to the head of a XML file. */
    private static final String XML_DOCTYPE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    /** A indent string. */
    private String indent = DEFAULT_INDENT;

    /**
     * Creates the XML object.
     */
    public XML() {

        super();
    }

    /**
     * Creates the XML object with the specified indent string.
     * 
     * @param indent The indent string to output a XML file.
     */
    public XML(String indent) {

        this();
        setIndent(indent);
    }

    /**
     * Parse a XML file.
     * 
     * @param file The XML file.
     * 
     * @throws ParserException
     *             If an error is occurred while parsing the
     *             read data.
     * @throws IOException
     *             If an I/O error is occurred.
     */
    public void parseFile(File file) throws ParserException, IOException {

        Reader reader = null;

        try {
            reader = new FileReader(file);
            nanoElement.parseFromReader(reader);
        } catch (XMLParseException e) {
            throw new ParserException(e.getMessage());
        } finally {
            if (reader != null) reader.close();
        }
    }

    /**
     * Parse a XML string.
     * 
     * @param xml The XML string.
     * 
     * @throws ParserException
     *             If an error is occurred while i/o processing or parsing the
     *             read data.
     */
    public void parseString(String xml) throws ParserException {

        try {
            nanoElement.parseString(xml);
        } catch (XMLParseException e) {
            throw new ParserException(e.getMessage());
        }
    }

    /**
     * Save this XML object as a XML file.
     * 
     * @param file The output XML file.
     * 
     * @throws IOException
     *             If an I/O error is occurred.
     */
    public void save(File file) throws IOException {

        Writer writer = null;

        try {
            writer = new Writer(file);
            writer.println(toPrettyString());
        } finally {
            if (writer != null) writer.close();
        }
    }

    /**
     * Set the indent string.
     * 
     * @param indent The indent string.
     */
    public void setIndent(String indent) {

        this.indent = indent;
    }

    /**
     * Returns this XML object as a formatted string using the indent.
     * 
     * @return The formatted XML string.
     */
    public String toPrettyString() {

        StringBuilder sb = recursiveToString(this, 0, indent, new StringBuilder());
        sb.insert(0, XML_DOCTYPE + "\n");

        return new String(sb);
    }

    private StringBuilder recursiveToString(XMLElement element, int indentCnt,
        String indent, StringBuilder sb) {

        String indentStr = "";
        for (int i = 0; i < indentCnt; i++) {
            indentStr += indent;
        }

        // Start tag and attributes.
        sb.append(indentStr);
        sb.append("<");
        sb.append(element.getName());

        for (String attributeName : element.getAttributeNames()) {
            String value = element.getAttribute(attributeName);
            sb.append(" " + attributeName + "=\"" + value + "\"");
        }

        sb.append(">");
        sb.append("\n");

        // Content.
        if (element.hasContent()) {
            sb.append(indentStr + indent);
            sb.append(element.getContent());
            sb.append("\n");
        }

        // Return if this element does not have children.
        if (!element.hasChildren()) {
            // End tag.
            sb.append(indentStr);
            sb.append("</" + element.getName() + ">");
            return sb;
        }

        // Execute this method recursively.
        for (XMLElement child : element.getChildren()) {
            sb = recursiveToString(child, indentCnt + 1, indent, sb);
            sb.append("\n");
        }

        // End tag.
        sb.append(indentStr);
        sb.append("</" + element.getName() + ">");

        return sb;
    }
}
