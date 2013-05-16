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
 * Color class. Wrap JOGL and make it easy to use.
 * <p>
 * Creates colors for storing in variables of the color datatype. The parameters are interpreted as
 * RGB or HSB values depending on the current colorMode(). The default mode is RGB values from 0 to
 * 255.
 *
 * @see casmi.graphics.color.ColorSet
 *
 * @author Y. Ban
 */
public interface Color {

    double getRed();

    void setRed(double red);

    double getGreen();

    void setGreen(double green);

    double getBlue();

    void setBlue(double blue);

    double getAlpha();

    void setAlpha(double alpha);

    void setup(GL2 gl);

    Color clone();
}
