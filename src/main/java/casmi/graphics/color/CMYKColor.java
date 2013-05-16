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
 * CMYK color class.
 *
 * @author T. Takeuchi
 */
public class CMYKColor implements Color {

    protected double cyan    = 1.0;
    protected double magenta = 1.0;
    protected double yellow  = 1.0;
    protected double black   = 1.0;
    protected double alpha   = 1.0;

    /**
     * Creates a new CMYKColor object using CMYK values.
     *
     * @param cyan
     *            The C (cyan) value. 0.0 - 1.0.
     * @param magenta
     *            The M (magenta) value. 0.0 - 1.0.
     * @param yellow
     *            The Y (yellow) value. 0.0 - 1.0.
     * @param black
     *            The K (black) value. 0.0 - 1.0.
     */
    public CMYKColor(double cyan, double magenta, double yellow, double black) {
        this(cyan, magenta, yellow, black, 1.0);
    }

    /**
     * Creates a new CMYKColor object using CMYK and alpha values.
     *
     * @param cyan
     *            The C (cyan) value. 0.0 - 1.0.
     * @param magenta
     *            The M (magenta) value. 0.0 - 1.0.
     * @param yellow
     *            The Y (yellow) value. 0.0 - 1.0.
     * @param black
     *            The K (black) value. 0.0 - 1.0.
     * @param alpha
     *            The alpha value. 0.0 - 1.0.
     */
    public CMYKColor(double cyan, double magenta, double yellow, double black, double alpha) {
        this.cyan    = cyan;
        this.magenta = magenta;
        this.yellow  = yellow;
        this.black   = black;
        this.alpha   = alpha;
    }

    /**
     * Creates a new CMYKColor object from ColorSet.
     *
     * @param colorSet
     *            ColorSet.
     *
     * @see casmi.graphics.color.ColorSet
     */
    public CMYKColor(ColorSet colorSet) {
        this(colorSet, 1.0);
    }

    /**
     * Creates a new CMYKColor object from ColorSet and an alpha value.
     *
     * @param colorSet
     *            ColorSet.
     * @param alpha
     *            alpha value. 0.0 - 1.0.
     *
     * @see casmi.graphics.color.ColorSet
     */
    public CMYKColor(ColorSet colorSet, double alpha) {
        setColor(colorSet);
        this.alpha = alpha;
    }

    private CMYKColor(CMYKColor color) {
        this(color.getCyan(), color.getMagenta(), color.getYellow(), color.getBlack(), color.getAlpha());
    }

    private final void setColor(ColorSet colorSet) {
        int[] rgb = ColorSet.getRGB(colorSet);
        double[] cmyk = CMYKColor.getCMYK(rgb[0] / 255.0, rgb[1] / 255.0, rgb[2] / 255.0);

        cyan    = cmyk[0];
        magenta = cmyk[1];
        yellow  = cmyk[2];
        black   = cmyk[3];
    }

    public static Color color(ColorSet colorSet) {
        return new CMYKColor(colorSet);
    }

    /**
     * Convert CMYK (Cyan, Magenta, Yellow, Black) to RGB values.
     *
     * @param cyan
     * @param magenta
     * @param yellow
     * @param black
     *
     * @return RGB values.
     */
    private static final double[] getRGB(double cyan, double magenta, double yellow, double black) {
        double[] rgb = {0.0, 0.0, 0.0};

        rgb[0] = 1.0 - Math.min(1.0, cyan    * (1.0 - black) + black);
        rgb[1] = 1.0 - Math.min(1.0, magenta * (1.0 - black) + black);
        rgb[2] = 1.0 - Math.min(1.0, yellow  * (1.0 - black) + black);

        return rgb;
    }

    private static final double[] getCMYK(double red, double blue, double green) {
        double[] cmyk = {0.0, 0.0, 0.0, 0.0};

        cmyk[3] = Math.min(Math.min(1.0 - red, 1.0 - blue), 1.0 - green);
        cmyk[0] = (1.0 - red   - cmyk[3]) / (1.0 - cmyk[3]);
        cmyk[1] = (1.0 - green - cmyk[3]) / (1.0 - cmyk[3]);
        cmyk[2] = (1.0 - blue  - cmyk[3]) / (1.0 - cmyk[3]);

        return cmyk;
    }

    /**
     * Calculates a color or colors between two color at a specific increment.
     *
     * @param colorSet1
     *              interpolate from this color
     * @param colorSet2
     *              interpolate to this color
     * @param amt
     *              between 0.0 and 1.0
     *
     * @return
     *              The calculated color values.
     */
    public static Color lerpColor(ColorSet colorSet1, ColorSet colorSet2, double amt) {
         return lerpColor((CMYKColor)CMYKColor.color(colorSet1), (CMYKColor)CMYKColor.color(colorSet2), amt);
    }

    /**
     * Calculates a color or colors between two color at a specific increment.
     *
     * @param color1
     *              interpolate from this color
     * @param color2
     *              interpolate to this color
     * @param amt
     *              between 0.0 and 1.0
     *
     * @return
     *              The calculated color values.
     */
    public static Color lerpColor(CMYKColor color1, CMYKColor color2, double amt) {
        double cyan, magenta, yellow, black, alpha;
        cyan    = color2.cyan    * amt + color1.cyan    * (1.0 - amt);
        magenta = color2.magenta * amt + color1.magenta * (1.0 - amt);
        yellow  = color2.yellow  * amt + color1.yellow  * (1.0 - amt);
        black   = color2.black   * amt + color1.black   * (1.0 - amt);
        alpha   = color2.alpha   * amt + color1.alpha   * (1.0 - amt);
        return new CMYKColor(cyan, magenta, yellow, black, alpha);
    }

    /**
     * Returns a Color object that shows a complementary color.
     *
     * @return a complementary Color object.
     */
    public CMYKColor getComplementaryColor() {
        double[] rgb = CMYKColor.getRGB(cyan, magenta, yellow, black);
        double[] cmyk = CMYKColor.getCMYK(1.0 - rgb[0], 1.0 - rgb[1], 1.0 - rgb[2]);
        return new CMYKColor(cmyk[0], cmyk[1], cmyk[2], cmyk[3]);
    }

    public double getCyan() {
        return cyan;
    }

    public void setCyan(double cyan) {
        this.cyan = cyan;
    }

    public double getMagenta() {
        return magenta;
    }

    public void setMagenta(double magenta) {
        this.magenta = magenta;
    }

    public double getYellow() {
        return yellow;
    }

    public void setYellow(double yellow) {
        this.yellow = yellow;
    }

    public double getBlack() {
        return black;
    }

    public void setBlack(double black) {
        this.black = black;
    }

    @Override
    public double getRed() {
        double[] rgb = CMYKColor.getRGB(cyan, magenta, yellow, black);
        return rgb[0];
    }

    @Override
    public void setRed(double red) {
        double[] rgb  = CMYKColor.getRGB(cyan, magenta, yellow, black);
        double[] cmyk = CMYKColor.getCMYK(red, rgb[1], rgb[2]);

        cyan    = cmyk[0];
        magenta = cmyk[1];
        yellow  = cmyk[2];
        black   = cmyk[3];
    }

    @Override
    public double getGreen() {
        double[] rgb = CMYKColor.getRGB(cyan, magenta, yellow, black);
        return rgb[1];
    }

    @Override
    public void setGreen(double green) {
        double[] rgb  = CMYKColor.getRGB(cyan, magenta, yellow, black);
        double[] cmyk = CMYKColor.getCMYK(rgb[0], green, rgb[2]);

        cyan    = cmyk[0];
        magenta = cmyk[1];
        yellow  = cmyk[2];
        black   = cmyk[3];
    }

    @Override
    public double getBlue() {
        double[] rgb = CMYKColor.getRGB(cyan, magenta, yellow, black);
        return rgb[2];
    }

    @Override
    public void setBlue(double blue) {
        double[] rgb  = CMYKColor.getRGB(cyan, magenta, yellow, black);
        double[] cmyk = CMYKColor.getCMYK(rgb[0], rgb[1], blue);

        cyan    = cmyk[0];
        magenta = cmyk[1];
        yellow  = cmyk[2];
        black   = cmyk[3];
    }

    @Override
    public double getAlpha() {
        return alpha;
    }

    @Override
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public void setup(GL2 gl) {
        double[] rgb  = CMYKColor.getRGB(cyan, magenta, yellow, black);
        gl.glColor4d(rgb[0], rgb[1], rgb[2], alpha);
    }

    @Override
    public CMYKColor clone() {
        return new CMYKColor(this);
    }
}
