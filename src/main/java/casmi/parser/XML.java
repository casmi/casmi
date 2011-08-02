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
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import casmi.exception.ParserException;

/**
 * XML class.
 * Extends XMLElement.class and expresses top of XML structure.
 * Use this class when you want to read or write a XML file.
 * 
 * @author T. Takeuchi
 * 
 */
public class XML extends XMLElement {

    private static final String DEFAULT_INDENT = "\t";

    private static final String XML_DOCTYPE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private String indent = DEFAULT_INDENT;

    public XML() {
        super();
    }
    
    public XML(String indent) {
        this();
        setIndent(indent);
    }

    public void parseFile(File file) throws ParserException {
        Reader reader = null;

        try {
            reader = new FileReader(file);
            nanoElement.parseFromReader(reader);
        } catch (IOException e) {
            throw new ParserException(e.getMessage());
        }
    }
    
    public void parseString(String xml) {
        nanoElement.parseString(xml);
    }

    public void save(File file) throws ParserException {
        PrintWriter pw = null;

        try {
            pw = new PrintWriter(file);
            pw.println(toPrettyString(indent));
        } catch (FileNotFoundException e) {
            throw new ParserException(e.getMessage());
        } finally {
            if (pw != null) pw.close();
        }
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }

    public String toPrettyString(String indent) {
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
