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

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import casmi.exception.ParserException;
import casmi.util.FileUtil;
import casmi.util.SystemUtil;

public class XMLTest {

    @Test
    public void simpleReadTest() {
        XML xml = new XML();
        
        File f = new File( FileUtil.url2Uri(getClass().getResource("example.xml")) );
        
        try {
            xml.parseFile(f);
        } catch (ParserException e) {
            e.printStackTrace();
            fail("Failed to parse XML.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(xml);
    }

    @Test
    public void readTest() {
        XML xml = new XML();

        File f = new File( FileUtil.url2Uri(getClass().getResource("example.xml")) );
        
        try {
            xml.parseFile(f);
        } catch (ParserException e) {
            e.printStackTrace();
            fail("Failed to parse XML.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        recursivePrint(xml, 0);
    }

    private void recursivePrint(XMLElement element, int indent) {
        String indentStr = "";
        for (int i = 0; i < indent; i++) {
            indentStr += "  ";
        }

        // Print start tag and attributes.
        System.out.print(indentStr);
        System.out.print("<");
        System.out.print(element.getName());

        for (String attributeName : element.getAttributeNames()) {
            String value = element.getAttribute(attributeName);
            System.out.print(" " + attributeName + "=\"" + value + "\"");
        }

        System.out.println(">");

        // Print content.
        if (element.hasContent()) {
            System.out.print(indentStr + "  ");
            System.out.println(element.getContent());
        }

        // If this element does not have children, return method.
        if (!element.hasChildren()) {
            // Print end tag.
            System.out.print(indentStr);
            System.out.println("</" + element.getName() + ">");
            return;
        }

        // Execute this method recursively.
        for (XMLElement child : element.getChildren()) {
            recursivePrint(child, indent + 1);
        }

        // Print end tag.
        System.out.print(indentStr);
        System.out.println("</" + element.getName() + ">");
    }

    @Test
    public void writeTest() {
        XML xml = new XML("  ");

        xml.setName("alcohoric");

        XMLElement sake = new XMLElement("sake");
        
        XMLElement urakasumi = new XMLElement("urakasumi", "UraKasumi");
        urakasumi.addAttribute("origin", "Miyagi");
        urakasumi.addAttribute("abv", "15");
        sake.addChild(urakasumi);
        
        xml.addChild(sake);

        String path = SystemUtil.JAVA_TMP_PATH + "write_example.xml";
        
        try {
            xml.save(new File(path));
            
            System.out.println("write data to " + path);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to generate XML.");
        }
    }

}
