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
 * Gray color class.
 *
 * @author T. Takeuchi
 */
public class GrayColor implements Color {

    protected double gray  = 1.0;
    protected double alpha = 1.0;

    /**
     * Creates a new GrayColor object using a gray-scale value.
     *
     * @param gray
     *              The gray-scale value, from 0.0 to 1.0.
     */
    public GrayColor(double gray) {
        this(gray, 1.0);
    }

    /**
     * Creates a new GrayColor object using gray-scale and alpha values.
     *
     * @param gray
     *              The gray-scale value, from 0.0 to 1.0.
     * @param alpha
     *              The alpha value, from 0.0 to 1.0.
     */
    public GrayColor(double gray, double alpha) {
        this.gray  = gray;
        this.alpha = alpha;
    }

    /**
     * Copy constructor for {@link #clone()} method.
     *
     * @param color
     *            GrayColor object.
     */
    private GrayColor(GrayColor color) {
        this(color.gray, color.alpha);
    }

    public static GrayColor lerpColor(GrayColor color1, GrayColor color2, double amt) {
        double gray  = color2.gray  * amt + color1.gray  * (1.0 - amt);
        double alpha = color2.alpha * amt + color1.alpha * (1.0 - amt);
        return new GrayColor(gray, alpha);
    }

    /**
     * Returns a Color object that shows a complementary color.
     *
     * @return a complementary Color object.
     */
    public GrayColor getComplementaryColor() {
        return new GrayColor(1.0 - this.gray, this.alpha);
    }

    public double getGray() {
        return gray;
    }

    public void setGray(double gray) {
        this.gray = gray;
    }

    @Override
    public double getRed() {
        return gray;
    }

    @Override
    public void setRed(double red) {
        gray = red;
    }

    @Override
    public double getGreen() {
        return gray;
    }

    @Override
    public void setGreen(double green) {
        gray = green;
    }

    @Override
    public double getBlue() {
        return gray;
    }

    @Override
    public void setBlue(double blue) {
        gray = blue;
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
        gl.glColor4d(gray, gray, gray, alpha);
    }

    @Override
    public GrayColor clone() {
        return new GrayColor(this);
    }
}
