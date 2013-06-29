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

package casmi.util;

import casmi.matrix.Vector3D;

/**
 * @author Y. Ban
 */
public class Random {

    private static java.util.Random r = null;

    public static final int random(int max) {

        if (max == 0)
            return 0;

        if (r == null)
            r = new java.util.Random();


        return r.nextInt(max);
    }

    public static final int random(int min, int max) {
        if (min >= max)
            return min;

        int diff = max - min;

        return random(diff) + min;
    }

    public static final float random(float max) {

        if (max == 0)
            return 0;

        if (r == null)
            r = new java.util.Random();

        return r.nextFloat() * max;
    }

    public static final float random(float min, float max) {
        if (min >= max)
            return min;

        float diff = max - min;

        return random(diff) + min;
    }

    public static final void setSeed(long seed) {
        if (r == null)
            r = new java.util.Random();

        r.setSeed(seed);
    }

    /**
     * returns a random Vertex that represents a point on the unit circle
     *
     * @return vertex
     */
    public static final Vector3D randVertex2d() {
        float theta = random((float)(Math.PI*2.0));
        return new Vector3D(Math.cos(theta),Math.sin(theta));
    }
}
