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

/**
 * XML element class.
 * 
 * @author T. Takeuchi
 * 
 */
public class XMLElement {

    nanoxml.XMLElement nanoElement;

    // -------------------------------------------------------------------------
    // Constructors.
    // -------------------------------------------------------------------------

    XMLElement() {
        nanoElement = new nanoxml.XMLElement();
    }

    public XMLElement(String name) {
        this();
        setName(name);
    }

    public XMLElement(String name, String content) {
        this();
        setName(name);
        setContent(content);
    }

    private XMLElement(nanoxml.XMLElement element) {
        this.nanoElement = element;
    }

    // -------------------------------------------------------------------------
    // Writing methods.
    // -------------------------------------------------------------------------

    public void addAttribute(String key, String value) {
        nanoElement.setAttribute(key, value);
    }

    public void addChild(XMLElement child) {
        nanoElement.addChild(child.nanoElement);
    }

    public void removeAttribute(String key) {
        nanoElement.removeAttribute(key);
    }

    public void removeChild(XMLElement child) {
        nanoElement.removeChild(child.nanoElement);
    }

    public void setName(String name) {
        nanoElement.setName(name);
    }

    public void setContent(String content) {
        nanoElement.setContent(content);
    }

    // -------------------------------------------------------------------------
    // Reading methods.
    // -------------------------------------------------------------------------

    public int countAttributes() {
        return getAttributeNames().length;
    }

    public int countChildren() {
        return nanoElement.countChildren();
    }

    public String[] getAttributeNames() {
        return enumerationToStringArray(nanoElement.enumerateAttributeNames());
    }

    public XMLElement[] getChildren() {
        return enumerationToXMLElementArray(nanoElement.enumerateChildren());
    }

    public XMLElement[] getChildren(String name) {
        java.util.ArrayList<XMLElement> list = new java.util.ArrayList<XMLElement>();
 
        for (XMLElement child : getChildren()) {
            if (child.getName().equals(name)) {
                list.add(child);
            }
        }
 
        if (0 < list.size()) {
            return list.toArray(new XMLElement[0]);
        } else {
            return null;
        }
    }
    
    public String getAttribute(String name) {
        return (String)nanoElement.getAttribute(name);
    }
    
    public XMLElement getChild(String name) {
        for (XMLElement child : getChildren()) {
            if (child.getName().equals(name)) return child;
        }
        
        return null;
    }

    public String getContent() {
        String content = nanoElement.getContent();
        return content.trim().replaceAll("\n", "");
    }

    public String getName() {
        return nanoElement.getName();
    }

    public int getLine() {
        return nanoElement.getLineNr();
    }

    public boolean hasAttribute() {
        return (0 < getAttributeNames().length) ? true : false;
    }

    public boolean hasChildren() {
        return (0 < countChildren()) ? true : false;
    }

    public boolean hasContent() {
        return (!getContent().isEmpty()) ? true : false;
    }

    // -------------------------------------------------------------------------
    // Others.
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return nanoElement.toString();
    }

    private XMLElement[] enumerationToXMLElementArray(java.util.Enumeration<?> enumeration) {
        java.util.ArrayList<XMLElement> arrayList = new java.util.ArrayList<XMLElement>();
        while (enumeration.hasMoreElements()) {
            arrayList.add(new XMLElement((nanoxml.XMLElement)enumeration.nextElement()));
        }
        return arrayList.toArray(new XMLElement[0]);
    }

    private String[] enumerationToStringArray(java.util.Enumeration<?> enumeration) {
        java.util.ArrayList<String> arrayList = new java.util.ArrayList<String>();
        while (enumeration.hasMoreElements()) {
            arrayList.add((String)enumeration.nextElement());
        }
        return arrayList.toArray(new String[0]);
    }
}
