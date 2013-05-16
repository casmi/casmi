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

package casmi.graphics.font;

import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import casmi.util.FileUtil;

/**
 * Font class. Wraps java.awt.Font and make it easy to use.
 *
 * @author T. Takeuchi
 */
public class Font {

    // -------------------------------------------------------------------------
    // Constants.
    // -------------------------------------------------------------------------

    /** Default font name. */
    private static final String DEFAULT_NAME = "Default";

    /** Default font style. */
    private static final FontStyle DEFAULT_STYLE = FontStyle.PLAIN;

    /** Default font size. */
    private static final double DEFAULT_SIZE = 18.0;

    // -------------------------------------------------------------------------
    // Valuables.
    // -------------------------------------------------------------------------

    /** java.awt.Font object. */
    private java.awt.Font awtFont = null;

    // -------------------------------------------------------------------------
    // Constructors.
    // -------------------------------------------------------------------------

    /**
     * Creates a new Font object using default properties.
     */
    public Font() {
        this(DEFAULT_NAME, DEFAULT_STYLE, DEFAULT_SIZE);
    }

    /**
     * Creates a new Font object from the specified name.
     *
     * @param name
     *            The font name.
     */
    public Font(String name) {
        this(name, DEFAULT_STYLE, DEFAULT_SIZE);
    }

    /**
     * Creates a new Font object from the specified name, style and point size.
     *
     * @param name
     *            The font name.
     * @param style
     *            The font style enum by FontStyle.
     * @param size
     *            The point size of the Font.
     */
    public Font(String name, FontStyle style, double size) {
        awtFont = new java.awt.Font(name, fontStyleToInt(style),
            (int)DEFAULT_SIZE);
        setSize(size);
    }

    /**
     * Creates a new Font object from the specified name, style and point size.
     *
     * @param name
     *            The font name.
     * @param style
     *            The font style.
     * @param size
     *            The point size of the Font.
     */
    public Font(String name, String style, double size) {
        this(name, stringToFontStyle(style), size);
    }

    /**
     * Creates a new Font object from the specified font file.
     *
     * @param file
     *            The font file. Open Type Font(.otf) or True Type Font(.ttf)
     *            file can be used.
     *
     * @throws FontFormatException
     *             If fontFormat is not TRUETYPE_FONT or TYPE1_FONT.
     * @throws IOException
     *             If the fontFile cannot be read.
     */
    public Font(File file) throws FontFormatException, IOException {
        this(file, DEFAULT_STYLE, DEFAULT_SIZE);
    }

    /**
     * Creates a new Font object from the specified font file, style and point
     * size.
     *
     * @param file
     *            The font file. Open Type Font(.otf) or True Type Font(.ttf)
     *            file can be used.
     * @param style
     *            The font style enum by FontStyle.
     * @param size
     *            The point size of the Font.
     *
     * @throws FontFormatException
     *             If fontFormat is not TRUETYPE_FONT or TYPE1_FONT.
     * @throws IOException
     *             If the fontFile cannot be read.
     */
    public Font(File file, FontStyle style, double size)
        throws FontFormatException, IOException {
        String suffix = FileUtil.getSuffix(file);
        if (suffix.matches("otf|ttf")) {
            awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, file).deriveFont(fontStyleToInt(style), (float)size);
        } else {
            throw new IOException("The file is invalid. An OTF or TTF file can be used.");
        }
    }

    /**
     * Creates a new Font object from the specified font file, style and point
     * size.
     *
     * @param file
     *            The font file. Open Type Font(.otf) or True Type Font(.ttf)
     *            file can be used.
     * @param style
     *            The font style.
     * @param size
     *            The point size of the Font.
     *
     * @throws FontFormatException
     *             If fontFormat is not TRUETYPE_FONT or TYPE1_FONT.
     * @throws IOException
     *             If the fontFile cannot be read.
     */
    public Font(File file, String style, double size)
        throws FontFormatException, IOException {
        this(file, stringToFontStyle(style), size);
    }

    /**
     * Creates a new Font object from java.awt.Font object.
     *
     * @param font
     *            java.awt.Font object.
     */
    public Font(java.awt.Font font) {
        awtFont = font;
    }

    // -------------------------------------------------------------------------
    // Methods.
    // -------------------------------------------------------------------------

    /**
     * Get java.awt.Font object from this Font object.
     *
     * @return java.awt.Font object.
     */
    public java.awt.Font getAWTFont() {
        return awtFont;
    }

    /**
     * Returns the point size of this Font in double value.
     *
     * @return The point size of this Font as a double value.
     */
    public double getSize() {
        return awtFont.getSize2D();
    }

    /**
     * Set the new point size.
     *
     * @param size
     *            The new point size of the Font.
     */
    public void setSize(double size) {
        awtFont = awtFont.deriveFont((float)size);
    }

    /**
     * Returns the style of this Font. The style can be PLAIN, BOLD, ITALIC, or
     * BOLD+ITALIC.
     *
     * @return The style of this Font.
     */
    public FontStyle getStyle() {
        return intToFontStyle(awtFont.getStyle());
    }

    /**
     * Set the new style.
     *
     * @param style
     *            The new style of this Font.
     */
    public void setStyle(FontStyle style) {
        awtFont = awtFont.deriveFont(fontStyleToInt(style));
    }

    /**
     * Set the new style from string. This method ignores case.
     * <p>
     *
     * Example: font.setStyle("plain");
     *
     * @param style
     *            The new style of this Font.
     */
    public void setStyle(String style) {
        setStyle(stringToFontStyle(style));
    }

    /**
     * Returns the family name of this Font.
     *
     * @return A String that is the family name of this Font.
     */
    public String getFamily() {
        return awtFont.getFamily();
    }

    /**
     * Returns the logical name of this Font. Use getFamily to get the family
     * name of the font. Use getFontName to get the font face name of the font.
     *
     * @return A String representing the logical name of this Font.
     */
    public String getName() {
        return awtFont.getName();
    }

    /**
     * Returns the font face name of this Font. For example, Helvetica Bold
     * could be returned as a font face name. Use getFamily to get the family
     * name of the font. Use getName to get the logical name of the font.
     *
     * @return A String representing the font face name of this Font.
     */
    public String getFontName() {
        return awtFont.getFontName();
    }

    /**
     * Returns the postscript name of this Font. Use getFamily to get the family
     * name of the font. Use getFontName to get the font face name of the font.
     *
     * @return A String representing the postscript name of this Font.
     */
    public String getPSName() {
        return awtFont.getPSName();
    }

    /**
     * Return a java.awt.Font style constant from FontStyle.
     *
     * @param style
     *            FontStyle value.
     * @return A java.awt.Font style constant.
     */
    private static int fontStyleToInt(FontStyle style) {
        switch (style) {
        case PLAIN:
            return java.awt.Font.PLAIN;
        case BOLD:
            return java.awt.Font.BOLD;
        case ITALIC:
            return java.awt.Font.ITALIC;
        default:
            return java.awt.Font.PLAIN;
        }
    }

    /**
     * Return FontStyle enum value from java.awt.Font style constant.
     *
     * @param style
     *            java.awt.Font style constant value.
     * @return FontStyle value.
     */
    private static FontStyle intToFontStyle(int style) {
        switch (style) {
        case java.awt.Font.PLAIN:
            return FontStyle.PLAIN;
        case java.awt.Font.BOLD:
            return FontStyle.BOLD;
        case java.awt.Font.ITALIC:
            return FontStyle.ITALIC;
        case java.awt.Font.BOLD + java.awt.Font.ITALIC:
            return FontStyle.BOLD_ITALIC;
        default:
            return FontStyle.PLAIN;
        }
    }

    /**
     * Return FontStyle enum value from a font style name.
     *
     * @param style
     *            A style name like "italic".
     * @return FontStyle enum value.
     */
    private static FontStyle stringToFontStyle(String style) {
        if (style.compareToIgnoreCase("plain") == 0) {
            return FontStyle.PLAIN;
        } else if (style.compareToIgnoreCase("bold") == 0) {
            return FontStyle.BOLD;
        } else if (style.compareToIgnoreCase("italic") == 0) {
            return FontStyle.ITALIC;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Converts this Font object to a String representation.
     */
    @Override
    public String toString() {
        return awtFont.toString();
    }

    /**
     * Return available fonts' name of this computer.
     *
     * @return An array of String containing font family names localized for the
     *         default locale, or a suitable alternative name if no name exists
     *         for this locale.
     */
    public static String[] getAvailableFontFamilyNames() {
        GraphicsEnvironment ge = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        return ge.getAvailableFontFamilyNames();
    }
}
