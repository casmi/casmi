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
 * Point class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Point extends Element {

    public static final int POINT = 0;
    public static final int POINTS = 1;
    public static final int POINT_3D = 2;
    public static final int POINTS_3D = 3;

    private double x;
    private double y;
    private double z;
    private int MODE;

    /**
     * Creates a new Point object using x,y-coordinate.
     *
     * @param x The x-coordinate of the Point.
     * @param y The y-coordinate of the Point.
     */
    public Point(double x, double y) {
        this.MODE = POINTS;
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new Point object using x,y,z-coordinate.
     *
     * @param x The x-coordinate of the Point.
     * @param y The y-coordinate of the Point.
     * @param z The z-coordinate of the Point.
     */
    public Point(double x, double y, double z) {
        this.MODE = POINTS_3D;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Sets x,y-coordinate.
     *
     * @param x The x-coordinate of the Point.
     * @param y The y-coordinate of the Point.
     */
    public void set(double x, double y) {
        this.MODE = POINTS;
        this.x = x;
        this.y = y;
    }

    /**
     * Sets x,y,z-coordinate.
     *
     * @param x The x-coordinate of the Point.
     * @param y The y-coordinate of the Point.
     * @param z The z-coordinate of the Point.
     */
    public void set(double x, double y, double z) {
        this.MODE = POINTS_3D;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001
            || !this.isDepthTest())
            gl.glDisable(GL2.GL_DEPTH_TEST);

        getSceneStrokeColor().setup(gl);

        gl.glPushMatrix();
        {
            this.move(gl);

            switch (MODE) {
            case POINTS:
                gl.glBegin(GL2.GL_POINTS);
                gl.glVertex2d(this.x, this.y);
                gl.glEnd();
                break;
            case POINTS_3D:
                gl.glBegin(GL2.GL_POINTS);
                gl.glVertex3d(this.x, this.y, this.z);
                gl.glEnd();
                break;
            default:
                break;
            }
        }
        gl.glPopMatrix();

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001
            || !this.isDepthTest())
            gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    @Override
    public void reset(GL2 gl) {}
}
