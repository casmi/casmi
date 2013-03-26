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
 * HSB color class.
 *
 * @author T. Takeuchi
 */
public class HSBColor implements Color {

    protected double hue        = 1.0;
    protected double saturation = 1.0;
    protected double brightness = 1.0;
    protected double alpha      = 1.0;

    /**
     * Creates a new Color object using HSB values.
     *
     * @param hue
     *              The H value. 0.0 - 1.0.
     * @param saturation
     *              The S value. 0.0 - 1.0.
     * @param brightness
     *              The B value. 0.0 - 1.0.
     */
    public HSBColor(double hue, double saturation, double brightness) {
        this(hue, saturation, brightness, 1.0);
    }

    /**
     * Creates a new Color object using HSB and alpha values.
     *
     * @param hue
     *              The H value. 0.0 - 1.0.
     * @param saturation
     *              The S value. 0.0 - 1.0.
     * @param brightness
     *              The B value. 0.0 - 1.0.
     * @param alpha
     *              The Alpha value. 0.0 - 1.0.
     */
    public HSBColor(double hue, double saturation, double brightness, double alpha) {
        this.hue        = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        this.alpha      = alpha;
    }

    /**
     * Creates a new Color object from ColorSet.
     *
     * @param colorSet
     *            ColorSet.
     *
     * @see casmi.graphics.color.ColorSet
     */
    public HSBColor(ColorSet colorSet) {
        this(colorSet, 1.0);
    }

    /**
     * Creates a new Color object from ColorSet and an alpha value.
     *
     * @param colorSet
     *            ColorSet.
     * @param alpha
     *              alpha value. 0.0 - 1.0.
     *
     * @see casmi.graphics.color.ColorSet
     */
    public HSBColor(ColorSet colorSet, double alpha) {
        setColor(colorSet);
        this.alpha = alpha;
    }

    /**
     * Copy constructor for {@link #clone()} method.
     *
     * @param color
     *            HSBColor object.
     */
    private HSBColor(HSBColor color) {
        this(color.getHue(), color.getSaturation(), color.getBrightness(), color.getAlpha());
    }

    private final void setColor(ColorSet colorSet) {
        int[] rgb = ColorSet.getRGB(colorSet);
        double[] hsb = HSBColor.getHSB(rgb[0] / 255.0, rgb[1] / 255.0, rgb[2] / 255.0);
        this.hue        = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    public static Color color(ColorSet colorSet) {
        return new HSBColor(colorSet);
    }

    /**
     * Convert HSB (Hue, Saturation, Brightness) to RGB values.
     *
     * @param hue
     * @param saturation
     * @param brightness
     *
     * @return
     *     RGB values.
     */
    private static final double[] getRGB(double hue, double saturation, double brightness) {
        double[] rgb = {0.0, 0.0, 0.0};

        double tmpHue = hue * 360.0;
        int hi;
        double f;
        double p, q, t;

        hi = (int)Math.floor(tmpHue / 60.0) % 6;
        f  = tmpHue / 60.0 - hi;
        p  = (brightness * (1.0 - saturation));
        q  = (brightness * (1.0 - saturation * f));
        t  = (brightness * (1.0 - saturation * (1.0 - f)));

        switch (hi) {
        case 0:
            rgb[0] = brightness;
            rgb[1] = t;
            rgb[2] = p;
            break;
        case 1:
            rgb[0] = q;
            rgb[1] = brightness;
            rgb[2] = p;
            break;
        case 2:
            rgb[0] = p;
            rgb[1] = brightness;
            rgb[2] = t;
            break;
        case 3:
            rgb[0] = p;
            rgb[1] = q;
            rgb[2] = brightness;
            break;
        case 4:
            rgb[0] = t;
            rgb[1] = p;
            rgb[2] = brightness;
            break;
        case 5:
            rgb[0] = brightness;
            rgb[1] = p;
            rgb[2] = q;
            break;
        default:
            break;
        }

        return rgb;
    }

    private static final double[] getHSB(double red, double green, double blue) {
        double[] hsb = {0.0, 0.0, 0.0};

        int flag = 0; // red is max: 0, green: 1, blue: 2
        double max = 0.0;
        double min = 0.0;
        if (green <= red) {
            if (blue <= red) {
                flag = 0;
                max = red;
                if (green <= blue) { // green < blue < red
                    min = green;
                } else {             // blue < green < red
                    min = blue;
                }
            } else {                 // green < red < blue
                flag = 2;
                max = blue;
                min = green;
            }
        } else {
            if (blue <= green) {
                flag = 1;
                max = green;
                if (red <= blue) {   // red < blue < green
                    min = red;
                } else {             // blue < red < green
                    min = blue;
                }
            } else {                 // red < green < blue
                flag = 2;
                max = blue;
                min = red;
            }
        }

        // calculate Hue
        switch (flag) {
        case 0: // max: red
            hsb[0] = 60.0 * (green - blue) / (max - min) / 360.0;
            break;
        case 1: // max: green
            hsb[0] = (60.0 * (green - blue) / (max - min) + 120.0) / 360.0;
            break;
        case 2: // max: blue
            hsb[0] = (60.0 * (green - blue) / (max - min) + 240.0) / 360.0;
            break;
        default:
            break;
        }
        if (hsb[0] < 0.0) {
            hsb[0] += 1.0;
        }

        // calculate Saturation
        hsb[1] = (max - min) / max;

        // calculate Brightness
        hsb[2] = max;

        return hsb;
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
         return lerpColor((HSBColor)HSBColor.color(colorSet1), (HSBColor)HSBColor.color(colorSet2), amt);
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
    public static Color lerpColor(HSBColor color1, HSBColor color2, double amt) {
        double hue, saturation, brightness, alpha;
        hue        = color2.hue        * amt + color1.hue        * (1 - amt);
        saturation = color2.saturation * amt + color1.saturation * (1 - amt);
        brightness = color2.brightness * amt + color1.brightness * (1 - amt);
        alpha      = color2.alpha      * amt + color1.alpha      * (1 - amt);
        return new HSBColor(hue, saturation, brightness, alpha);
    }

    /**
     * Returns a Color object that shows a complementary color.
     *
     * @return a complementary Color object.
     */
    public HSBColor getComplementaryColor() {
        double[] rgb = HSBColor.getRGB(hue, saturation, brightness);
        double[] hsb = HSBColor.getHSB(1.0 - rgb[0], 1.0 - rgb[1], 1.0 - rgb[2]);
        return new HSBColor(hsb[0], hsb[1], hsb[2]);
    }

    public double getHue() {
        return hue;
    }

    public void setHue(double hue) {
        this.hue = hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    @Override
    public double getRed() {
        double[] rgb = HSBColor.getRGB(this.hue, this.saturation, this.brightness);
        return rgb[0];
    }

    @Override
    public void setRed(double red) {
        double[] rgb = HSBColor.getRGB(hue, saturation, brightness);
        double[] hsb = HSBColor.getHSB(red, rgb[1], rgb[2]);

        hue        = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
    }

    @Override
    public double getGreen() {
        double[] rgb = HSBColor.getRGB(this.hue, this.saturation, this.brightness);
        return rgb[1];
    }

    @Override
    public void setGreen(double green) {
        double[] rgb = HSBColor.getRGB(hue, saturation, brightness);
        double[] hsb = HSBColor.getHSB(rgb[0], green, rgb[2]);

        hue        = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
    }

    @Override
    public double getBlue() {
        double[] rgb = HSBColor.getRGB(this.hue, this.saturation, this.brightness);
        return rgb[2];
    }

    @Override
    public void setBlue(double blue) {
        double[] rgb = HSBColor.getRGB(hue, saturation, brightness);
        double[] hsb = HSBColor.getHSB(rgb[0], rgb[1], blue);

        hue        = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
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
        double[] rgb = HSBColor.getRGB(this.hue, this.saturation, this.brightness);
        gl.glColor4d(rgb[0], rgb[1], rgb[2], alpha);
    }

    @Override
    public HSBColor clone() {
        return new HSBColor(this);
    }
}
