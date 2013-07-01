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
 * Cone class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Cone extends Element {

    private double base;
    private double height;

    private int slices = 30;
    private int stacks = 30;

    /**
     * Creates a new Cone object using base size and height.
     *
     * @param base The base size of the Cone.
     * @param height The height of the Cone.
     */
    public Cone(double base, double height) {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.base = base;
        this.height = height;
//        this.setThreeD(true);
    }

    /**
     * Creates a new Cone object using base size, height, slices and stacks.
     *
     * @param base The base size of the Cone.
     * @param height The height of the Cone.
     * @param slices The slices of the Cone.
     * @param stacks The stacks of the Cone.
     */
    public Cone(double base, double height, int slices, int stacks) {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.base = base;
        this.height = height;
        this.slices = slices;
        this.stacks = stacks;
//        this.setThreeD(true);
    }

    /**
     * Creates a new Cone object using base size, height and x,y,z-coordinate.
     *
     * @param base The base size of the Cone.
     * @param height The height of the Cone.
     * @param x The x-coordinate of the Cone.
     * @param y The y-coordinate of the Cone.
     * @param z The z-coordinate of the Cone.
     */
    public Cone(double base, double height, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.base = base;
        this.height = height;
//        this.setThreeD(true);
    }

    /**
     * Creates a new Cone object using base size, height, slices, stacks, and x,y,z-coordinate.
     *
     * @param base The base size of the Cone.
     * @param height The height of the Cone.
     * @param x The x-coordinate of the Cone.
     * @param y The y-coordinate of the Cone.
     * @param z The z-coordinate of the Cone.
     * @param slices The slices of the Cone.
     * @param stacks The stacks of the Cone.
     */
    public Cone(double base, double height, double x, double y, double z, int slices, int stacks) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.slices = slices;
        this.stacks = stacks;
        this.base = base;
        this.height = height;
//        this.setThreeD(true);
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
//        if ((this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest()) &&
//            !this.isThreeD()) {
//            gl.glDisable(GL2.GL_DEPTH_TEST);
//        }

        gl.glPushMatrix();
        {
            this.move(gl);

            gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
            gl.glPolygonOffset(1f, 1f);

            gl.glPushMatrix();
            {
                gl.glRotated(90.0, -1.0, 0.0, 0.0);
                if (this.fill) {
                    getSceneFillColor().setup(gl);
                    drawSolidCone(glu, base, getHeight(), slices, stacks);
                } else if (this.stroke) {
                    getSceneStrokeColor().setup(gl);
                    drawWireCone(glu, base, getHeight(), slices, stacks);
                }
            }
            gl.glPopMatrix();

            gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);
        }
        gl.glPopMatrix();

//        if ((this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest()) &&
//            !this.isThreeD()) {
//            gl.glEnable(GL2.GL_DEPTH_TEST);
//        }
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

    private void drawWireCone(GLU glu, double base, double height, int slices, int stacks) {
        quadObjInit(glu);
        glu.gluQuadricDrawStyle(quadObj, GLU.GLU_LINE);
        glu.gluQuadricNormals(quadObj, GLU.GLU_SMOOTH);
        glu.gluCylinder(quadObj, base, 0.0, height, slices, stacks);
    }

    private void drawSolidCone(GLU glu, double base, double height, int slices, int stacks) {
        quadObjInit(glu);
        glu.gluQuadricDrawStyle(quadObj, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadObj, GLU.GLU_SMOOTH);
        glu.gluCylinder(quadObj, base, 0.0, height, slices, stacks);
    }

    /**
     * Gets the base size of this Cone.
     *
     * @return The base size of the Cone.
     */
    public double getBase() {
        return base;
    }

    /**
     * Sets the base size of this Cone.
     *
     * @param base The base size of the Cone.
     */
    public void setBase(double base) {
        this.base = base;
    }

    /**
     * Gets the height of this Cone.
     *
     * @return The height of the Cone.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height of this Cone.
     *
     * @param height The height of the Cone.
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Sets the slices of this Cone.
     *
     * @param slices The slices of the Cone.
     */
    public void setSlices(int slices) {
        this.slices = slices;
    }

    /**
     * Set the stacks of this Cone.
     *
     * @param stacks The stacks of the Cone.
     */
    public void setStacks(int stacks) {
        this.stacks = stacks;
    }

    /**
     * Gets the slices of this Cone.
     *
     * @return The slices of the Cone.
     */
    public int getSlices() {
        return this.slices;
    }

    /**
     * Gets the stacks of this Cone.
     *
     * @return The stacks of the Cone.
     */
    public int getStacks() {
        return this.stacks;
    }

    @Override
    public void reset(GL2 gl) {
        // TODO Auto-generated method stub

    }
}
