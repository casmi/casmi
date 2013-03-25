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

package casmi.graphics.element;

/**
 * Circle class. Wrap JOGL and make it easy to use.
 *
 * @see casmi.graphics.element.Ellipse
 *
 * @author Y. Ban
 */
public class Circle extends Ellipse {

    /**
     * Creates a new Circle using radius.
     *
     * @param radius The radius of the Circle.
     */
    public Circle(double radius) {
        super(radius);
    }

    /**
     * Creates a new Circle using radius.
     *
     * @param x The x-coordinate of a position of the Circle.
     * @param y The y-coordinate of a position of the Circle.
     * @param radius The radius of the Circle.
     */
    public Circle(double x, double y, double radius) {
        super(x, y, radius);
    }
}
