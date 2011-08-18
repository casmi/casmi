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
 * <p>
 * This class uses <a href="http://devkix.com/nanoxml.php">nanoxml</a> library
 * (lite-2.2.3) licensed by zlib/libpng license.
 * </p>
 * 
 * @see casmi.exception.ParserException
 * 
 * @author T. Takeuchi
 * 
 */
public class XMLElement {

    nanoxml.XMLElement nanoElement;

    // -------------------------------------------------------------------------
    // Constructors.
    // -------------------------------------------------------------------------

    /**
     * Creates a new XMLElement object.
     */
    XMLElement() {

        nanoElement = new nanoxml.XMLElement();
    }

    /**
     * Create a new XMLElement object from the specified name.
     * 
     * @param name The element name.
     */
    public XMLElement(String name) {

        this();
        setName(name);
    }

    /**
     * Create a new XMLElement object from the specified name and content.
     * 
     * @param name The element name.
     * 
     * @param content The content.
     */
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

    /**
     * Add a new attribute with the specified key and value.
     * 
     * @param key The attribute key.
     * 
     * @param value The attribute value.
     */
    public void addAttribute(String key, String value) {

        nanoElement.setAttribute(key, value);
    }

    /**
     * Add a new child.
     * The child is a XMLElement object.
     * 
     * @param child The XMLElement object.
     */
    public void addChild(XMLElement child) {

        nanoElement.addChild(child.nanoElement);
    }

    /**
     * Removes an attribute from the specified key.
     * 
     * @param key The key name of the removed attribute.
     */
    public void removeAttribute(String key) {

        nanoElement.removeAttribute(key);
    }

    /**
     * Removes a child.
     * 
     * @param child The removed XMLElement object.
     */
    public void removeChild(XMLElement child) {

        nanoElement.removeChild(child.nanoElement);
    }

    /**
     * Set a new name to the XMLElement object.
     * 
     * @param name The name string.
     */
    public void setName(String name) {

        nanoElement.setName(name);
    }

    /**
     * Set a new content to the XMLElement object.
     * 
     * @param content The content string.
     */
    public void setContent(String content) {

        nanoElement.setContent(content);
    }

    // -------------------------------------------------------------------------
    // Reading methods.
    // -------------------------------------------------------------------------

    /**
     * Returns an amount of attributes.
     * 
     * @return An amount of attributes as int.
     */
    public int countAttributes() {

        return getAttributeNames().length;
    }

    /**
     * Returns an amount of children.
     * 
     * @return An amount of children as int.
     */
    public int countChildren() {

        return nanoElement.countChildren();
    }

    /**
     * Returns attribute names as a string array.
     * 
     * @return The attribute names.
     */
    public String[] getAttributeNames() {

        return enumerationToStringArray(nanoElement.enumerateAttributeNames());
    }

    /**
     * Returns children as a XMLElement array.
     * 
     * @return The children.
     */
    public XMLElement[] getChildren() {

        return enumerationToXMLElementArray(nanoElement.enumerateChildren());
    }

    /**
     * Returns children that have the specified name as a XMLElement array.
     * 
     * @param name The name to search.
     * 
     * @return The children.
     */
    public XMLElement[] getChildren(String name) {

        java.util.List<XMLElement> list = new java.util.ArrayList<XMLElement>();

        for (XMLElement child : getChildren()) {
            if (child.getName().equals(name)) {
                list.add(child);
            }
        }

        return (0 < list.size()) ? list.toArray(new XMLElement[0]) : null;
    }

    /**
     * Returns the attribute value that have the specified key.
     * 
     * @param key The attribute key to search.
     * 
     * @return The attribute value.
     */
    public String getAttribute(String key) {

        return (String)nanoElement.getAttribute(key);
    }

    /**
     * Returns the content of the XMLElement object as a string.
     * 
     * @return The content.
     */
    public String getContent() {

        String content = nanoElement.getContent();
        return content.trim().replaceAll("\n", "");
    }

    /**
     * Returns a name of this XMLElement object.
     * 
     * @return A name string.
     */
    public String getName() {

        return nanoElement.getName();
    }

    /**
     * Returns the line number in the source data on which the element is found.
     * This method returns 0 there is no associated source data.
     * 
     * @return The line number; 0 if unknown.
     */
    public int getLine() {

        return nanoElement.getLineNr();
    }

    /**
     * Returns true if this object has attributes.
     * 
     * @return <code>true</code> if this object has attributes, or
     *         <code>false</code> if not.
     */
    public boolean hasAttribute() {

        return (0 < getAttributeNames().length) ? true : false;
    }

    /**
     * Returns true if this object has children.
     * 
     * @return <code>true</code> if this object has children, or
     *         <code>false</code> if not.
     */
    public boolean hasChildren() {

        return (0 < countChildren()) ? true : false;
    }

    /**
     * Returns true if this object has a content.
     * 
     * @return <code>true</code> if this object has a content, or
     *         <code>false</code> if not.
     */
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
