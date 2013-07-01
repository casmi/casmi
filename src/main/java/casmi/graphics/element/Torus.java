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

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Torus class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Torus extends Element {

    private double x;
    private double y;
    private double z;
    private double in;
    private double out;

    private int nside = 32;
    private int rings = 16;

    /**
     * Creates a new Torus object using inner radius and outer radius.
     *
     * @param innerRad The inner radius of the torus.
     * @param outerRad The outer radius of the torus.
     */
    public Torus(double innerRad, double outerRad) {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.in = innerRad;
        this.out = outerRad;
    }

    /**
     * Creates a new Torus object using inner radius, outer radius, nsides and rings.
     *
     * @param innerRad The inner radius of the torus.
     * @param outerRad The outer radius of the torus.
     * @param nside The number of side of each radial section.
     * @param rings The number of radial divisions for torus.
     */
    public Torus(double innerRad, double outerRad, int nside, int rings) {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.in = innerRad;
        this.out = outerRad;
        this.nside = nside;
        this.rings = rings;
    }

    /**
     * Creates a new Torus object using inner radius, outer radius and x,y,z-coordinate.
     *
     * @param innerRad The inner radius of the torus.
     * @param outerRad The outer radius of the torus.
     * @param x The x-coordinate of the torus.
     * @param y The y-coordinate of the torus.
     * @param z The z-coordinate of the torus.
     */
    public Torus(double innerRad, double outerRad, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.in = innerRad;
        this.out = outerRad;
    }

    /**
     * Creates a new Torus object using inner radius, outer radius, nsides, rings and
     * x,y,z-coordinate.
     *
     * @param innerRad The inner radius of the torus.
     * @param outerRad The outer radius of the torus.
     * @param x The x-coordinate of the torus.
     * @param y The y-coordinate of the torus.
     * @param z The z-coordinate of the torus.
     * @param nside The number of side of each radial section.
     * @param rings The number of radial divisions for torus.
     */
    public Torus(double innerRad, double outerRad, double x, double y, double z, int nside, int rings) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.in = innerRad;
        this.out = outerRad;
        this.nside = nside;
        this.rings = rings;
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest()) {
            gl.glDisable(GL2.GL_DEPTH_TEST);
        }

        gl.glPushMatrix();
        {
            gl.glTranslated(x, y, z);
            this.move(gl);

            if (this.fill) {
                getSceneFillColor().setup(gl);
                drawSolidTorus(gl, glu, in, out, nside, rings);
            }

            if (this.stroke) {
                getSceneStrokeColor().setup(gl);
                this.strokeColor.setup(gl);
                drawWireTorus(gl, glu, in, out, nside, rings);
            }
        }
        gl.glPopMatrix();

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest()) {
            gl.glEnable(GL2.GL_DEPTH_TEST);
        }
    }

    private void drawWireTorus(GL2 gl, GLU glu, double innerRadius, double outerRadius, int nsides, int rings) {
        gl.glPushAttrib(GL2.GL_POLYGON_BIT);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        doughnut(gl, innerRadius, outerRadius, nsides, rings);
        gl.glPopAttrib();
    }

    private void drawSolidTorus(GL2 gl, GLU glu, double innerRadius, double outerRadius, int nsides, int rings) {
        doughnut(gl, innerRadius, outerRadius, nsides, rings);
    }

    private static void doughnut(GL2 gl, double r, double R, int nsides, int rings) {
        int i, j;
        float theta, phi, theta1;
        float cosTheta, sinTheta;
        float cosTheta1, sinTheta1;
        float ringDelta, sideDelta;

        ringDelta = (float)(2.0 * Math.PI / rings);
        sideDelta = (float)(2.0 * Math.PI / nsides);

        theta = 0.0f;
        cosTheta = 1.0f;
        sinTheta = 0.0f;
        for (i = rings - 1; i >= 0; i--) {
            theta1 = theta + ringDelta;
            cosTheta1 = (float)Math.cos(theta1);
            sinTheta1 = (float)Math.sin(theta1);
            gl.glBegin(GL2.GL_QUAD_STRIP);
            phi = 0.0f;
            for (j = nsides; j >= 0; j--) {
                float cosPhi, sinPhi, dist;

                phi += sideDelta;
                cosPhi = (float)Math.cos(phi);
                sinPhi = (float)Math.sin(phi);
                dist = (float)(R + r * cosPhi);

                gl.glNormal3f(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi);
                gl.glVertex3f(cosTheta1 * dist, -sinTheta1 * dist, (float)r * sinPhi);
                gl.glNormal3f(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
                gl.glVertex3f(cosTheta * dist, -sinTheta * dist, (float)r * sinPhi);
            }
            gl.glEnd();
            theta = theta1;
            cosTheta = cosTheta1;
            sinTheta = sinTheta1;
        }
    }

    @Override
    public void reset(GL2 gl) {
        // TODO Auto-generated method stub

    }
}
