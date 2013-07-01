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

import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.matrix.Vector3D;

/**
 * Ellipse class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Ellipse extends Element {

    private double width;
    private double height;
    private double detailAngle = 10.0;

    private double th1;
    private double th2;

    private Color centerColor;
    private Color edgeColor;

    /**
     * Creates a new Ellipse object using x,y-coordinate, width and height.
     *
     * @param x The x-coordinate of the center of the Ellipse.
     * @param y The y-coordinate of the center of the Ellipse.
     * @param width The width of the Ellipse.
     * @param height The height of the Ellipse.
     */
    public Ellipse(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a new Ellipse object using x,y-coordinate, width and height.
     *
     * @param v The x,y-coordinate of the center of the Ellipse.
     * @param width The width of the Ellipse.
     * @param height The height of the Ellipse.
     */
    public Ellipse(Vector3D v, double width, double height) {
        this.x = v.getX();
        this.y = v.getY();
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a new Ellipse object using width and height.
     *
     *
     * @param width The width of the Ellipse.
     * @param height The height of the Ellipse.
     */
    public Ellipse(double width, double height) {
        this.x = 0.0;
        this.y = 0.0;
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a new Ellipse object using x,y-coordinate and radius.
     *
     * @param x The x-coordinate of the center of the Ellipse.
     * @param y The y-coordinate of the center of the Ellipse.
     * @param radius The radius of the Ellipse.
     */
    public Ellipse(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.width = radius * 2.0;
        this.height = radius * 2.0;
    }

    /**
     * Creates a new Ellipse object using x,y-coordinate and radius.
     *
     * @param v The x,y-coordinate of the center of the Ellipse.
     * @param radius The radius of the Ellipse.
     */
    public Ellipse(Vector3D v, double radius) {
        this.x = v.getX();
        this.y = v.getY();
        this.width = radius * 2.0;
        this.height = radius * 2.0;
    }

    /**
     * Creates a new Ellipse object using radius.
     *
     * @param radius The radius of the Ellipse.
     */
    public Ellipse(double radius) {
        this.x = 0;
        this.y = 0;
        this.width = radius * 2.0;
        this.height = radius * 2.0;
    }

    /**
     * Sets x,y-coordinate, width and height.
     *
     * @param x The x-coordinate of the center of the Ellipse.
     * @param y The y-coordinate of the center of the Ellipse.
     * @param width The width of the Ellipse.
     * @param height The height of the Ellipse.
     */
    public void set(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets x,y-coordinate, width and height.
     *
     * @param v The x,y-coordinate of the center of the Ellipse.
     * @param width The width of the Ellipse.
     * @param height The height of the Ellipse.
     */
    public void set(Vector3D v, double width, double height) {
        this.x = v.getX();
        this.y = v.getY();
        this.width = width;
        this.height = height;
    }

    /**
     * Sets x,y-coordinate and radius.
     *
     * @param x The x-coordinate of the center of the Ellipse.
     * @param y The y-coordinate of the center of the Ellipse.
     * @param radius The radius of the Ellipse.
     */
    public void set(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.width = radius * 2.0;
        this.height = radius * 2.0;
    }

    /**
     * Sets x,y-coordinate and radius.
     *
     * @param v The x,y-coordinate of the center of the Ellipse.
     * @param radius The radius of the Ellipse.
     */
    public void set(Vector3D v, double radius) {
        this.x = v.getX();
        this.y = v.getY();
        this.width = radius * 2.0;
        this.height = radius * 2.0;
    }

    /**
     * Sets radius.
     *
     * @param radius The radius of the Ellipse.
     */
    public void setRadius(double radius) {
        this.width = radius * 2.0;
        this.height = radius * 2.0;
    }

    /**
     * Sets width.
     *
     * @param width The width of the Ellipse.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Sets height.
     *
     * @param height The height of the Ellipse.
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Gets radius.
     *
     * @return The radius of the Ellipse.
     */
    public double getRadius() {
        return this.width / 2.0;
    }

    /**
     * Gets width.
     *
     * @return The width of the Ellipse.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Gets height.
     *
     * @return The height of the Ellipse.
     */
    public double getHeight() {
        return this.height;
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest())
            gl.glDisable(GL2.GL_DEPTH_TEST);

        gl.glPushMatrix();
        {
            this.move(gl);

            if (this.fill) {
                getSceneFillColor().setup(gl);
                gl.glBegin(GL2.GL_TRIANGLE_FAN);
                {
                    if (isGradation() && centerColor != null)
                        getSceneColor(this.centerColor).setup(gl);

                    gl.glVertex2d(0, 0);

                    if (isGradation() && centerColor != null)
                        getSceneColor(this.edgeColor).setup(gl);

                    for (th1 = 0.0; th1 <= 360.0; th1 += detailAngle) {
                        double th1Rad = th1 / 180.0 * Math.PI;
                        double x1 = (this.width / 2.0) * Math.cos(th1Rad);
                        double y1 = (this.height / 2.0) * Math.sin(th1Rad);
                        gl.glVertex2d(x1, y1);
                    }
                }
                gl.glEnd();
            }

            if (this.stroke) {
                getSceneStrokeColor().setup(gl);
                gl.glLineWidth(this.strokeWidth);
                for (th1 = 0.0; th1 < 360.0; th1 += detailAngle) {
                    th2 = th1 + detailAngle;
                    double th1Rad = th1 / 180.0 * Math.PI;
                    double th2Rad = th2 / 180.0 * Math.PI;

                    double x1 = (this.width / 2.0) * Math.cos(th1Rad);
                    double y1 = (this.height / 2.0) * Math.sin(th1Rad);
                    double x2 = (this.width / 2.0) * Math.cos(th2Rad);
                    double y2 = (this.height / 2.0) * Math.sin(th2Rad);

                    gl.glBegin(GL2.GL_LINES);
                    {
                        gl.glVertex2d(x1, y1);
                        gl.glVertex2d(x2, y2);
                    }
                    gl.glEnd();
                }
            }
        }
        gl.glPopMatrix();

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest())
            gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    /**
     * Sets the detail of this Ellipse.
     *
     * @param detail The precision of the Ellipse.
     */
    public void setDetail(double detail) {
        this.detailAngle = 360.0 / detail;
    }

    /**
     * Sets the color of this Ellipse's center.
     *
     * @param color The color of the Ellipse's center.
     */
    public void setCenterColor(Color color) {
        if (centerColor == null) {
            centerColor = new RGBColor(0.0, 0.0, 0.0);
        }
        setGradation(true);
        this.centerColor = color;
    }

    /**
     * Sets the color of this Ellipse's center.
     *
     * @param colorSet The colorSet of the Ellipse's center.
     */
    public void setCenterColor(ColorSet colorSet) {
        setCenterColor(new RGBColor(colorSet));
    }

    /**
     * Sets the color of this Ellipse's edge.
     *
     * @param color The color of the Ellipse's edge.
     */
    public void setEdgeColor(Color color) {
        if (edgeColor == null) {
            edgeColor = new RGBColor(0.0, 0.0, 0.0);
        }
        setGradation(true);
        this.edgeColor = color;
    }

    /**
     * Sets the color of this Ellipse's edge.
     *
     * @param colorSet The colorSet of the Ellipse's edge.
     */
    public void setEdgeColor(ColorSet colorSet) {
        setEdgeColor(new RGBColor(colorSet));
    }

    @Override
    public void reset(GL2 gl) {}
}
