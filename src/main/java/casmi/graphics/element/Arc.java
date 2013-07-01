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
 * Arc class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Arc extends Element {

    private double w;
    private double h;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double precision = 5.0;
    private double precisionangle = 5.0;

    private double th1;
    private double th2;
    private double th1_rad;
    private double th2_rad;

    private double radStart;
    private double radEnd;

    private Color centerColor;
    private Color edgeColor;

    /**
     * Creates a new Arc object using width, height, start and end points of degree properties.
     *
     * @param w The width of the Arc.
     * @param h The height of the Arc.
     * @param radStart The start degree of the Arc.
     * @param radEnd The end degree of the Arc.
     */
    public Arc(double w, double h, double radStart, double radEnd, double precision) {
        this.x = 0;
        this.y = 0;
        this.w = w;
        this.h = h;
        this.radStart = radStart;
        this.radEnd = radEnd;
        this.precision = precision;
        this.precisionangle = (this.radEnd - this.radStart) / this.precision;
    }

    /**
     * Creates a new Arc object using width, height, start and end points of degree properties.
     *
     * @param w The width of the Arc.
     * @param h The height of the Arc.
     * @param radStart The start degree of the Arc.
     * @param radEnd The end degree of the Arc.
     */
    public Arc(double w, double h, double radStart, double radEnd) {
        this.x = 0;
        this.y = 0;
        this.w = w;
        this.h = h;
        this.radStart = radStart;
        this.radEnd = radEnd;
    }

    /**
     * Creates a new Arc object using width, height, start and end points of degree and precision
     * properties.
     *
     * @param w The width of the Arc.
     * @param h The height of the Arc.
     * @param radStart The start degree of the Arc.
     * @param radEnd The end degree of the Arc.
     * @param precision The precision of the Arc.
     */
    public Arc(double x, double y, double w, double h, double radStart, double radEnd,
        double precision) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.radStart = radStart;
        this.radEnd = radEnd;
        this.precision = precision;
        this.precisionangle = (this.radEnd - this.radStart) / this.precision;
    }

    /**
     * Creates a new Arc object using width, height, start and end points of degree and precision
     * properties.
     *
     * @param r The radius of the Arc.
     * @param radStart The start degree of the Arc.
     * @param radEnd The end degree of the Arc.
     */
    public Arc(double r, double radStart, double radEnd) {
        this.w = r * 2;
        this.h = r * 2;
        this.radStart = radStart;
        this.radEnd = radEnd;
    }

    /**
     * Creates a new Arc object using x and y-coordinate of the Arc, width, height, start and end
     * points of degree and precision properties.
     *
     * @param x The x-coordinate of the Arc.
     * @param y The y-coordinate of the Arc.
     * @param r The radius of the Arc.
     * @param radStart The start degree of the Arc.
     * @param radEnd The end degree of the Arc.
     * @param precision The precision of the Arc.
     */
    public Arc(double x, double y, double r, double radStart, double radEnd, double precision) {
        this.x = x;
        this.y = y;
        this.w = r * 2;
        this.h = r * 2;
        this.radStart = radStart;
        this.radEnd = radEnd;
        this.precision = precision;
        this.precisionangle = (this.radEnd - this.radStart) / this.precision;
    }

    /**
     * Creates a new Arc object using position of the Arc, width, height, start and end points of
     * degree and precision properties.
     *
     * @param v The position of the Arc
     * @param r The radius of the Arc.
     * @param radStart The start degree of the Arc.
     * @param radEnd The end degree of the Arc.
     * @param precision The precision of the Arc.
     */
    public Arc(Vector3D v, double r, double radStart, double radEnd, double precision) {
        this.x = v.getX();
        this.y = v.getY();
        this.w = r * 2;
        this.h = r * 2;
        this.radStart = radStart;
        this.radEnd = radEnd;
        this.precision = precision;
        this.precisionangle = (this.radEnd - this.radStart) / this.precision;
    }

    /**
     * Creates a new Arc object using position of the Arc, width, height, start and end points of
     * degree properties.
     *
     * @param v The position of the Arc
     * @param r The radius of the Arc.
     * @param radStart The start degree of the Arc.
     * @param radEnd The end degree of the Arc.
     */
    public Arc(Vector3D v, double r, double radStart, double radEnd) {
        this.x = v.getX();
        this.y = v.getY();
        this.w = r * 2;
        this.h = r * 2;
        this.radStart = radStart;
        this.radEnd = radEnd;
    }

    /**
     * Sets a Arc object using x and y-coordinate of the Arc, width, height, start and end points of
     * degree and precision properties.
     *
     * @param x The x-coordinate of the Arc.
     * @param y The y-coordinate of the Arc.
     * @param r The radius of the Arc.
     * @param radStart The start degree of the Arc.
     * @param radEnd The end degree of the Arc.
     * @param precision The precision of the Arc.
     */
    public void set(double x, double y, double r, double radStart, double radEnd, double precision) {
        this.x = x;
        this.y = y;
        this.w = r * 2;
        this.h = r * 2;
        this.radStart = radStart;
        this.radEnd = radEnd;
        this.precision = precision;
        this.precisionangle = (this.radEnd - this.radStart) / this.precision;
    }

    /**
     * Sets a Arc object using x and y-coordinate of the Arc, width, height, start and end points of
     * degree properties.
     *
     * @param x The x-coordinate of the Arc.
     * @param y The y-coordinate of the Arc.
     * @param r The radius of the Arc.
     * @param radStart The start degree of the Arc.
     * @param radEnd The end degree of the Arc.
     */
    public void set(double x, double y, double r, double radStart, double radEnd) {
        this.x = x;
        this.y = y;
        this.w = r * 2;
        this.h = r * 2;
        this.radStart = radStart;
        this.radEnd = radEnd;
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        if (precision <= 0.0) {
            precision = 5.0;
        }

        if (this.fillColor.getAlpha() < 1.0 || this.strokeColor.getAlpha() < 1.0
            || this.isDepthTest() == false) {
            gl.glDisable(GL2.GL_DEPTH_TEST);
        }

        gl.glPushMatrix();
        this.move(gl);

        if (this.fill) {
            // this.fillColor.setup(gl);
            getSceneFillColor().setup(gl);
            gl.glBegin(GL2.GL_TRIANGLE_FAN);
            if (isGradation() == true && centerColor != null)
                getSceneColor(this.centerColor).setup(gl);
            gl.glVertex2d(0, 0);

            if (isGradation() == true && centerColor != null)
                getSceneColor(this.edgeColor).setup(gl);

            for (th1 = radStart; th1 <= radEnd; th1 = th1 + precisionangle) {
                th1_rad = th1 / 180.0 * Math.PI;

                x1 = (w / 2.0) * Math.cos((float)th1_rad);
                y1 = (h / 2.0) * Math.sin((float)th1_rad);
                gl.glVertex2d(x1, y1);
            }
            gl.glEnd();
        }

        if (this.stroke) {
            getSceneStrokeColor().setup(gl);
            gl.glLineWidth(this.strokeWidth);
            for (th1 = radStart; th1 <= radEnd - precisionangle; th1 = th1 + precisionangle) {
                th2 = th1 + precisionangle;
                th1_rad = th1 / 180.0 * Math.PI;
                th2_rad = th2 / 180.0 * Math.PI;

                x1 = (w / 2.0) * Math.cos((float)th1_rad);
                y1 = (h / 2.0) * Math.sin((float)th1_rad);
                x2 = (w / 2.0) * Math.cos((float)th2_rad);
                y2 = (h / 2.0) * Math.sin((float)th2_rad);

                gl.glBegin(GL2.GL_LINES);
                gl.glVertex2d(x1, y1);
                gl.glVertex2d(x2, y2);
                gl.glEnd();
            }
        }

        gl.glPopMatrix();

        if (this.fillColor.getAlpha() < 1.0 || this.strokeColor.getAlpha() < 1.0
            || this.isDepthTest() == false) {
            gl.glEnable(GL2.GL_DEPTH_TEST);
        }
    }

    /**
     * Gets the precision of this Arc.
     *
     * @return The precision of the Arc.
     */
    public double getDetail() {
        return precision;
    }

    /**
     * Sets the precision of this Arc.
     *
     * @param detail The precision of the Arc.
     */
    public void setDetail(double detail) {
        this.precision = detail;
        this.precisionangle = (this.radEnd - this.radStart) / detail;
    }

    /**
     * Sets the width of this Arc.
     *
     * @param width The width of the Arc.
     */
    public void setWidth(double width) {
        this.w = width;
    }

    /**
     * Sets the height of this Arc.
     *
     * @param height The height of the Arc.
     */
    public void setHeight(double height) {
        this.h = height;
    }

    /**
     * Sets the radius of this Arc.
     *
     * @param radius The radius of the Arc.
     */
    public void setRadius(double radius) {
        this.h = radius * 2;
        this.w = radius * 2;
    }

    /**
     * Sets the start degree of this Arc.
     *
     * @param start The start degree of the Arc.
     */
    public void setStart(double start) {
        this.radStart = start;
    }

    /**
     * Sets the end degree of this Arc.
     *
     * @param end The end degree of the Arc.
     */
    public void setEnd(double end) {
        this.radEnd = end;
    }

    /**
     * Gets the start degree of this Arc.
     *
     * @return The start degree of the Arc.
     */
    public double getStart() {
        return this.radStart;
    }

    /**
     * Gets the end degree of this Arc.
     *
     * @return The end degree of the Arc.
     */
    public double getEnd() {
        return this.radEnd;
    }

    /**
     * Gets the width of this Arc.
     *
     * @return The width of the Arc.
     */
    public double getWidth() {
        return this.w;
    }

    /**
     * Gets the height of this Arc.
     *
     * @return The height of the Arc.
     */
    public double getHeight() {
        return this.h;
    }

    /**
     * Gets the radius of this Arc.
     *
     * @return The radius of the Arc.
     */
    public double getRadius() {
        return this.w / 2;
    }

    /**
     * Sets the color of the center of this Arc.
     *
     * @param color The color of the center of the Arc.
     */
    public void setCenterColor(Color color) {
        if (centerColor == null) {
            centerColor = new RGBColor(0.0, 0.0, 0.0);
        }
        setGradation(true);
        this.centerColor = color;
    }

    /**
     * Sets the colorSet of the center of this Arc.
     *
     * @param colorSet The colorSet of the center of the Arc.
     */
    public void setCenterColor(ColorSet colorSet) {
        if (this.centerColor == null) {
            this.centerColor = new RGBColor(0.0, 0.0, 0.0);
        }
        setGradation(true);
        this.centerColor = RGBColor.color(colorSet);
    }

    /**
     * Sets the color of the edge of this Arc.
     *
     * @param color The color of the edge of the Arc.
     */
    public void setEdgeColor(Color color) {
        if (edgeColor == null) edgeColor = new RGBColor(0.0, 0.0, 0.0);
        setGradation(true);
        this.edgeColor = color;
    }

    /**
     * Sets the colorSet of the edge of this Arc.
     *
     * @param colorSet The colorSet of the edge of the Arc.
     */
    public void setEdgeColor(ColorSet colorSet) {
        if (edgeColor == null) edgeColor = new RGBColor(0.0, 0.0, 0.0);
        setGradation(true);
        this.edgeColor = RGBColor.color(colorSet);
    }

    @Override
    public void reset(GL2 gl) {}
}
