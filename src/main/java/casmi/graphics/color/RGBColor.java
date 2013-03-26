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

package casmi.graphics.color;

import javax.media.opengl.GL2;

/**
 * RGB color class.
 *
 * @author T. Takeuchi
 *
 */
public class RGBColor implements Color {

    protected double red   = 1.0;
    protected double green = 1.0;
    protected double blue  = 1.0;
    protected double alpha = 1.0;

    /**
     * Creates a new Color object using Grayscale value.
     *
     * @param gray
     *              The grayscale value. 0.0 - 1.0.
     */
    public RGBColor(double gray) {
        this(gray, 1.0);
    }

    /**
     * Creates a new Color object using Grayscale and alpha values.
     *
     * @param gray
     *              The grayscale value. 0.0 - 1.0.
     * @param alpha
     *              The alpha value. 0.0 - 1.0.
     */
    public RGBColor(double gray, double alpha) {
        this(gray, gray, gray, alpha);
    }

    /**
     * Creates a new Color object using RGB values.
     *
     * @param red
     *              The R value. 0.0 - 1.0.
     * @param green
     *              The G value. 0.0 - 1.0.
     * @param blue
     *              The B value. 0.0 - 1.0.
     */
    public RGBColor(double red, double green, double blue) {
        this(red, green, blue, 1.0);
    }

    /**
     * Creates a new Color object using RGB and alpha values.
     *
     * @param red
     *              The R value. 0.0 - 1.0.
     * @param green
     *              The G value. 0.0 - 1.0.
     * @param blue
     *              The B value. 0.0 - 1.0.
     * @param alpha
     *              The Alpha value. 0.0 - 1.0.
     */
    public RGBColor(double red, double green, double blue, double alpha) {
        this.red   = red;
        this.green = green;
        this.blue  = blue;
        this.alpha = alpha;
    }

    /**
     * Creates a new Color object using RGB value in hexadecimal notation (i.e. "#FFCC44" or 0xFFFFCC00)
     *
     * @param colorStr
     */
    public RGBColor(String colorStr) {
        int tmpR = 255;
        int tmpG = 255;
        int tmpB = 255;
        int tmpA = 255;

        if (colorStr.indexOf("#") == 0) {
            if (colorStr.length() == 7) {
                tmpR = Integer.decode("0X" + colorStr.substring(1, 3));
                tmpG = Integer.decode("0X" + colorStr.substring(3, 5));
                tmpB = Integer.decode("0X" + colorStr.substring(5, 7));
            } else if (colorStr.length() == 9) {
                tmpR = Integer.decode("0X" + colorStr.substring(1, 3));
                tmpG = Integer.decode("0X" + colorStr.substring(3, 5));
                tmpB = Integer.decode("0X" + colorStr.substring(5, 7));
                tmpA = Integer.decode("0X" + colorStr.substring(7, 9));
            }
        } else if (colorStr.indexOf("0x") == 0 || colorStr.indexOf("0X") == 0) {
            if (colorStr.length() == 8) {
                tmpR = Integer.decode("0X" + colorStr.substring(2, 4));
                tmpG = Integer.decode("0X" + colorStr.substring(4, 6));
                tmpB = Integer.decode("0X" + colorStr.substring(6, 8));
            } else if (colorStr.length() == 10) {
                tmpR = Integer.decode("0X" + colorStr.substring(2, 4));
                tmpG = Integer.decode("0X" + colorStr.substring(4, 6));
                tmpB = Integer.decode("0X" + colorStr.substring(6, 8));
                tmpA = Integer.decode("0X" + colorStr.substring(8, 10));
            }
        }

        this.red   = tmpR / 255.0;
        this.green = tmpG / 255.0;
        this.blue  = tmpB / 255.0;
        this.alpha = tmpA / 255.0;
    }

    /**
     * Creates a new Color object from ColorSet.
     *
     * @param colorSet
     *            ColorSet.
     *
     * @see casmi.graphics.color.ColorSet
     */
    public RGBColor(ColorSet colorSet) {
        setColor(colorSet);
    }

    /**
     * Creates a new Color object from ColorSet and an alpha value.
     *
     * @param colorSet
     *            ColorSet.
     * @param alpha
     *            alpha value. 0.0 - 1.0.
     *
     * @see casmi.graphics.color.ColorSet
     */
    public RGBColor(ColorSet colorSet, double alpha) {
        setColor(colorSet);
        this.alpha = alpha;
    }

    /**
     * Copy constructor for {@link #clone()} method.
     *
     * @param color
     *            RGBColor object.
     */
    private RGBColor(RGBColor color) {
        this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Returns the colorset's RGB values.
     *
     * @param colorSet
     *              The ColorSet.
     * @return
     *              The ColorSet's RGB values.
     */
    private final void setColor(ColorSet colorSet) {
        int[] rgb = ColorSet.getRGB(colorSet);
        this.red   = rgb[0] / 255.0;
        this.green = rgb[1] / 255.0;
        this.blue  = rgb[2] / 255.0;
    }

    /**
     * Returns the colorset's RGB values.
     * @param colorSet
     *              The ColorSet.
     * @return
     *              The ColorSet's RGB values.
     */
    public static Color color(ColorSet colorSet) {
        return new RGBColor(colorSet);
    }

    /**
     * Calculates a color or colors between two color at a specific increment.
     * @param colorSet1
     *              interpolate from this color
     * @param colorSet2
     *              interpolate to this color
     * @param amt
     *              between 0.0 and 1.0
     * @return
     *              The calculated color values.
     */
    public static Color lerpColor(ColorSet colorSet1, ColorSet colorSet2, double amt) {
         return lerpColor(RGBColor.color(colorSet1), RGBColor.color(colorSet2), amt);
    }

    public static RGBColor lerpColor(Color color1, Color color2, double amt) {
        double red   = color2.getRed()   * amt + color1.getRed()   * (1.0 - amt);
        double green = color2.getGreen() * amt + color1.getGreen() * (1.0 - amt);
        double blue  = color2.getBlue()  * amt + color1.getBlue()  * (1.0 - amt);
        double alpha = color2.getAlpha() * amt + color1.getAlpha() * (1.0 - amt);
        return new RGBColor(red, green, blue, alpha);
    }

    /**
     * Returns a Color object that shows a complementary color.
     *
     * @return a complementary Color object.
     */
    public Color getComplementaryColor() {
        return new RGBColor(1.0 - red, 1.0 - green, 1.0 - blue, alpha);
    }

    @Override
    public double getRed() {
        return red;
    }

    @Override
    public void setRed(double red) {
        this.red = red;
    }

    @Override
    public double getGreen() {
        return green;
    }

    @Override
    public void setGreen(double green) {
        this.green = green;
    }

    @Override
    public double getBlue() {
        return blue;
    }

    @Override
    public void setBlue(double blue) {
        this.blue = blue;
    }

    @Override
    public double getAlpha() {
        return alpha;
    }

    @Override
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setGray(double gray) {
        this.red   = gray;
        this.green = gray;
        this.blue  = gray;
    }

    @Override
    public void setup(GL2 gl) {
        gl.glColor4d(this.red, this.green, this.blue, this.alpha);
    }

    @Override
    public RGBColor clone() {
        return new RGBColor(this);
    }
}
