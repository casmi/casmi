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

import java.util.Random;

/**
 * @author Y. Ban
 */
public class Noise {

    private static final float PRECISION = 0.5f;
    private static final int TABLE_LENGTH = (int) (360f / PRECISION);

    private static final int PERLIN_YWRAPB = 4;
    private static final int PERLIN_YWRAP = 1 << PERLIN_YWRAPB;
    private static final int PERLIN_ZWRAPB = 8;
    private static final int PERLIN_ZWRAP = 1 << PERLIN_ZWRAPB;

    private static final int PERLIN_SIZE = 4095;

    private static int perlin_octaves = 4; // default to medium smooth
    private static float perlin_amp_falloff = 0.5f; // 50% reduction/octave

    private static int perlin_TWOPI, perlin_PI;
    private static float[] perlin_cosTable;
    private static float[] perlin;

    static {
        perlin_cosTable = new float[TABLE_LENGTH];

        for (int i = 0; i < TABLE_LENGTH; i++) {
            perlin_cosTable[i] = (float) Math.cos(i / 180.0f * Math.PI * PRECISION);
        }

        perlin_TWOPI = perlin_PI = TABLE_LENGTH;
        perlin_PI /= 2;
    }

    private static java.util.Random r = null;

    public static float noise(float x) {
        return noise(x, 0f, 0f);
    }

    public static float noise(float x, float y) {
        return noise(x, y, 0f);
    }

    public static float noise(float x, float y, float z) {
        if (perlin == null) {
            if (r == null) {
                r = new Random();
            }
            perlin = new float[PERLIN_SIZE + 1];
            for (int i = 0; i < PERLIN_SIZE + 1; i++) {
                perlin[i] = r.nextFloat(); // (float)Math.random();
            }
        }

        if (x < 0)
            x = -x;
        if (y < 0)
            y = -y;
        if (z < 0)
            z = -z;

        int xi = (int) x, yi = (int) y, zi = (int) z;
        float xf = (float) (x - xi);
        float yf = (float) (y - yi);
        float zf = (float) (z - zi);
        float rxf, ryf;

        float r = 0;
        float ampl = 0.5f;

        float n1, n2, n3;

        for (int i = 0; i < perlin_octaves; i++) {
            int of = xi + (yi << PERLIN_YWRAPB) + (zi << PERLIN_ZWRAPB);

            rxf = noise_fsc(xf);
            ryf = noise_fsc(yf);

            n1 = perlin[of & PERLIN_SIZE];
            n1 += rxf * (perlin[(of + 1) & PERLIN_SIZE] - n1);
            n2 = perlin[(of + PERLIN_YWRAP) & PERLIN_SIZE];
            n2 += rxf * (perlin[(of + PERLIN_YWRAP + 1) & PERLIN_SIZE] - n2);
            n1 += ryf * (n2 - n1);

            of += PERLIN_ZWRAP;
            n2 = perlin[of & PERLIN_SIZE];
            n2 += rxf * (perlin[(of + 1) & PERLIN_SIZE] - n2);
            n3 = perlin[(of + PERLIN_YWRAP) & PERLIN_SIZE];
            n3 += rxf * (perlin[(of + PERLIN_YWRAP + 1) & PERLIN_SIZE] - n3);
            n2 += ryf * (n3 - n2);

            n1 += noise_fsc(zf) * (n2 - n1);

            r += n1 * ampl;
            ampl *= perlin_amp_falloff;
            xi <<= 1;
            xf *= 2;
            yi <<= 1;
            yf *= 2;
            zi <<= 1;
            zf *= 2;

            if (xf >= 1.0f) {
                xi++;
                xf--;
            }
            if (yf >= 1.0f) {
                yi++;
                yf--;
            }
            if (zf >= 1.0f) {
                zi++;
                zf--;
            }
        }
        return r;
    }

    private static float noise_fsc(float i) {
        return 0.5f * (1.0f - perlin_cosTable[(int) (i * perlin_PI)
                % perlin_TWOPI]);
    }

    public static void setSeed(long seed) {
        if (r == null)
            r = new Random();
        r.setSeed(seed);

        perlin = null;
    }
}
