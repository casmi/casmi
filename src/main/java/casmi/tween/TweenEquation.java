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
 * @author Takashi AOKI <federkasten@me.com>
 */
public interface TweenEquation {

    /**
     * Computes the next value of the interpolation.
     * @param t Current time, in seconds.
     * @param b Initial value.
     * @param c Offset to the initial value.
     * @param d Total duration, in seconds.
     * @return float
     */
    double compute(double t, double b, double c, double d);
}
