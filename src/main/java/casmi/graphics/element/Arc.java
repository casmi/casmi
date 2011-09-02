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

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.matrix.Vertex;

/**
 * Arc class. Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Arc extends Element implements Renderable {

	private double x;
	private double y;
	private double w;
	private double h;
	private double x1;
	private double y1;
	private double x2;
	private double y2;
	private double precision = 5.0;

	private double th1;
	private double th2;
	private double th1_rad;
	private double th2_rad;

	private double radStart;
	private double radEnd;

	/**
     * Creates a new Arc object using width, height, start and end points of degree properties.
     * 
     * @param w 
     *            The width of the Arc.
     * @param h
     *            The height of the Arc.
     * @param radStart
     *            The start degree of the Arc.
     * @param radEnd
     *            The end degree of the Arc.
     */
	public Arc(double w, double h, double radStart, double radEnd,
			double precision) {
		this.x = 0;
		this.y = 0;
		this.w = w;
		this.h = h;
		this.radStart = radStart;
		this.radEnd = radEnd;
		this.precision = precision;
	}

	/**
     * Creates a new Arc object using width, height, start and end points of degree and precision properties.
     * 
     * @param w
     *            The width of the Arc.
     * @param h
     *            The height of the Arc.
     * @param radStart
     *            The start degree of the Arc.
     * @param radEnd
     *            The end degree of the Arc.
     * @param precision
     *            The precision of the Arc.
     */
	public Arc(double x, double y, double w, double h, double radStart,
			double radEnd, double precision) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.radStart = radStart;
		this.radEnd = radEnd;
		this.precision = precision;
	}

	/**
     * Creates a new Arc object using x and y-coordinate of the Arc, width, height, start and end points of degree and precision properties.
     * 
     * @param x
     *             The x-coordinate of the Arc.
     * @param y
     *             The y-coordinate of the Arc. 
     * @param r
     *             The radius of the Arc.  
     * @param radStart
     *            The start degree of the Arc.
     * @param radEnd
     *            The end degree of the Arc.
     * @param precision
     *            The precision of the Arc.
     */
	public Arc(double x, double y, double r, double radStart, double radEnd,
			double precision) {
		this.x = x;
		this.y = y;
		this.w = r * 2;
		this.h = r * 2;
		this.radStart = radStart;
		this.radEnd = radEnd;
		this.precision = precision;
	}

	/**
     * Creates a new Arc object using position of the Arc, width, height, start and end points of degree and precision properties.
     * 
     * @param v
     *            The position of the Arc 
     * @param r
     *            The radius of the Arc.  
     * @param radStart
     *            The start degree of the Arc.
     * @param radEnd
     *            The end degree of the Arc.
     * @param precision
     *            The precision of the Arc.
     */
	public Arc(Vertex v, double r, double radStart, double radEnd,
			double precision) {
		this.x = v.x;
		this.y = v.y;
		this.w = r * 2;
		this.h = r * 2;
		this.radStart = radStart;
		this.radEnd = radEnd;
		this.precision = precision;
	}
	
	/**
     * Sets a Arc object using x and y-coordinate of the Arc, width, height, start and end points of degree and precision properties.
     * 
     * @param x
     *             The x-coordinate of the Arc.
     * @param y
     *             The y-coordinate of the Arc. 
     * @param r
     *             The radius of the Arc.  
     * @param radStart
     *            The start degree of the Arc.
     * @param radEnd
     *            The end degree of the Arc.
     * @param precision
     *            The precision of the Arc.
     */
    public void set(double x, double y, double r, double radStart, double radEnd,
            double precision) {
        this.x = x;
        this.y = y;
        this.w = r * 2;
        this.h = r * 2;
        this.radStart = radStart;
        this.radEnd = radEnd;
        this.precision = precision;
    }

	@Override
	public void render(GL gl, GLU glu, int width, int height) {

		if (precision <= 0.0) {
			precision = 5.0;
		}

		if (this.fillColor.getA() != 1 || this.strokeColor.getA() != 1)
			gl.glDisable(GL.GL_DEPTH_TEST);

		if (this.fill) {
			this.fillColor.setup(gl);
			gl.glBegin(GL.GL_TRIANGLE_FAN);
			gl.glVertex2d(x, y);

			for (th1 = radStart; th1 <= radEnd; th1 = th1 + precision) {
				th1_rad = th1 / 180.0 * Math.PI;

				x1 = (w / 2.0) * Math.cos((float) th1_rad) + x;
				y1 = (h / 2.0) * Math.sin((float) th1_rad) + y;
				gl.glVertex2d(x1, y1);
			}
			gl.glEnd();
		}

		if (this.stroke) {
			this.strokeColor.setup(gl);
			gl.glLineWidth(this.strokeWidth);
			for (th1 = radStart; th1 <= radEnd - 10.0; th1 = th1 + precision) {
				th2 = th1 + 10.0;
				th1_rad = th1 / 180.0 * Math.PI;
				th2_rad = th2 / 180.0 * Math.PI;

				x1 = (w / 2.0) * Math.cos((float) th1_rad) + x;
				y1 = (h / 2.0) * Math.sin((float) th1_rad) + y;
				x2 = (w / 2.0) * Math.cos((float) th2_rad) + x;
				y2 = (h / 2.0) * Math.sin((float) th2_rad) + y;

				gl.glBegin(GL.GL_LINES);
				gl.glVertex2d(x1, y1);
				gl.glVertex2d(x2, y2);
				gl.glEnd();
			}
		}

		if (this.fillColor.getA() != 1 || this.strokeColor.getA() != 1)
			gl.glEnable(GL.GL_DEPTH_TEST);
	}

	/**
     * Returns the precision  of this Arc.
     */
	public double getDetail() {
		return precision;
	}

	/**
     * Set the precision  of this Arc.
     * 
     * @param precision
     *             The precision of the Arc.
     */
	public void setDetail(double detail) {
		this.precision = detail;
	}
	
	/**
     * Set the width of this Arc.
     * 
     * @param width
     *             The width of the Arc.
     */
	public void setWidth(double width) {
		this.w = width;
	}

	/**
     * Set the height of this Arc.
     * 
     * @param height
     *             The height of the Arc.
     */
	public void setHeight(double height) {
		this.h = height;
	}
	
	/**
     * Set the radius of this Arc.
     * 
     * @param radius
     *             The radius of the Arc.
     */
	public void setRadius(double radius) {
		this.h = radius*2;
		this.w = radius*2;
	}
	
	/**
     * Set the start degree of this Arc.
     * 
     * @param start
     *             The start degree of the Arc.
     */
	public void setStart(double start) {
		this.radStart = start;
	}
	
	/**
     * Set the end degree of this Arc.
     * 
     * @param end
     *             The end degree of the Arc.
     */
	public void setEnd(double end) {
		this.radEnd = end;
	}


}
