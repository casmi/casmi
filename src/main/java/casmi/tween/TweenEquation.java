/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011-2012, Xcoo, Inc.
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

package casmi.tween;

/**
 * Base class for every easing equation. You can create your own equations
 * and directly use them in the Tween static methods by deriving from this
 * class.
 *
 * @author Y. Ban
 */
public abstract class TweenEquation {

    /**
     * Computes the next value of the interpolation.
     * @param t Current time, in seconds.
     * @param b Initial value.
     * @param c Offset to the initial value.
     * @param d Total duration, in seconds.
     * @return float
     */
    public abstract float compute(float t, float b, float c, float d);

    /**
     * Returns true if the given string is the name of this equation (the name
     * is returned in the toString() method, don't forget to override it).
     * This method is usually used to save/load a tween to/from a text file.
     */
    public boolean isValueOf(String str) {
        return str.equals(toString());
    }
}
