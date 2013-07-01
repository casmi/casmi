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
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * Cylinder class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Cylinder extends Element {

    private double x;
    private double y;
    private double z;
    private double radius;
    private double height;

    private int slices = 30;
    private int stacks = 30;

    /**
     * Creates a new Cylinder object using radius and height.
     *
     * @param radius The radius of the Cylinder.
     * @param height The height of the Cylinder.
     */
    public Cylinder(double radius, double height) {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.radius = radius;
        this.setHeight(height);
    }

    /**
     * Creates a new Cylinder object using radius, height, slices and stacks.
     *
     * @param radius The radius of the Cylinder.
     * @param height The height of the Cylinder.
     * @param slices The slices of the Cylinder.
     * @param stacks The stacks of the Cylinder.
     */
    public Cylinder(double radius, double height, int slices, int stacks) {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.radius = radius;
        this.setHeight(height);
        this.slices = slices;
        this.stacks = stacks;
    }

    /**
     * Creates a new Cylinder object using radius, height and x,y,z-coordinate.
     *
     * @param radius The radius of the Cylinder.
     * @param height The height of the Cylinder.
     * @param x The x-coordinate of the Cylinder.
     * @param y The y-coordinate of the Cylinder.
     * @param z The z-coordinate of the Cylinder.
     */
    public Cylinder(double radius, double height, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.setHeight(height);
    }

    /**
     * Creates a new Cylinder object using radius, height, slices, stacks, and x,y,z-coordinate.
     *
     * @param radius The radius of the Cylinder.
     * @param height The height of the Cylinder.
     * @param x The x-coordinate of the Cylinder.
     * @param y The y-coordinate of the Cylinder.
     * @param z The z-coordinate of the Cylinder.
     * @param slices The slices of the Cylinder.
     * @param stacks The stacks of the Cylinder.
     */
    public Cylinder(double radius, double height, double x, double y, double z, int slices, int stacks) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.slices = slices;
        this.stacks = stacks;
        this.radius = radius;
        this.setHeight(height);
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest()) {
            gl.glDisable(GL2.GL_DEPTH_TEST);
        }

        gl.glPushMatrix();
        this.move(gl);

        gl.glTranslated(x, y, z);

        if (this.fill) {
            getSceneFillColor().setup(gl);
            drawSolidCylinder(gl, glu, radius, getHeight(), slices, stacks);
        }

        if (this.stroke) {
            getSceneStrokeColor().setup(gl);
            drawWireCylinder(glu, radius, getHeight(), slices, stacks);
        }

        gl.glPopMatrix();

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest()) {
            gl.glEnable(GL2.GL_DEPTH_TEST);
        }
    }

    private GLUquadric quadObj;

    private void quadObjInit(GLU glu) {
        if (quadObj == null) {
            quadObj = glu.gluNewQuadric();
        }
        if (quadObj == null) {
            throw new GLException("Out of memory");
        }
    }

    private void drawWireCylinder(GLU glu, double radius, double height, int slices, int stacks) {
        quadObjInit(glu);
        glu.gluQuadricDrawStyle(quadObj, GLU.GLU_LINE);
        glu.gluQuadricNormals(quadObj, GLU.GLU_SMOOTH);
        glu.gluCylinder(quadObj, radius, radius, height, slices, stacks);
    }

    private void drawSolidCylinder(GL2 gl, GLU glu, double radius, double height, int slices, int stacks) {

        double[] x = new double[slices];
        double[] y = new double[slices];
        double angleDelta = Math.PI * 2 / slices;
        double angle = 0;
        for (int i = 0; i < slices; i++) {
            angle = i * angleDelta;
            x[i] = Math.cos(angle) * radius;
            y[i] = Math.sin(angle) * radius;
        }

        // Draw bottom cap
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glNormal3d(0, 0, -1);
        gl.glVertex3d(0, 0, 0);
        for (int i = 0; i < slices; i++) {
            gl.glVertex3d(x[i], y[i], 0);
        }
        gl.glVertex3d(x[0], y[0], 0);
        gl.glEnd();

        // Draw top cap
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glNormal3d(0, 0, 1);
        gl.glVertex3d(0, 0, height);
        for (int i = 0; i < slices; i++) {
            gl.glVertex3d(x[i], y[i], height);
        }
        gl.glVertex3d(x[0], y[0], height);
        gl.glEnd();

        // Draw walls
        quadObjInit(glu);
        glu.gluQuadricDrawStyle(quadObj, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadObj, GLU.GLU_SMOOTH);
        glu.gluCylinder(quadObj, radius, radius, height, slices, stacks);
    }

    /**
     * Gets the radius of this Cylinder.
     *
     * @return The radius of the Cylinder.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of this Cylinder.
     *
     * @param radius The radius of the Cylinder.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Gets the height of this Cylinder.
     *
     * @return The height of the Cylinder.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height of this Cylinder.
     *
     * @param height The height of the Cylinder.
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Sets the slices of this Cylinder.
     *
     * @param slices The slices of the Cylinder.
     */
    public void setSlices(int slices) {
        this.slices = slices;
    }

    /**
     * Sets the stacks of this Cylinder.
     *
     * @param stacks The stacks of the Cylinder.
     */
    public void setStacks(int stacks) {
        this.stacks = stacks;
    }

    @Override
    public void reset(GL2 gl) {}
}
