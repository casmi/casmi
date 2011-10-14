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
	private double precisionangle = 5.0;

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
		this.precisionangle = (this.radEnd-this.radStart)/this.precision;
	}
	
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
	public Arc(double w, double h, double radStart, double radEnd) {
		this.x = 0;
		this.y = 0;
		this.w = w;
		this.h = h;
		this.radStart = radStart;
		this.radEnd = radEnd;
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
		this.precisionangle = (this.radEnd-this.radStart)/this.precision;
	}
	
	/**
     * Creates a new Arc object using width, height, start and end points of degree and precision properties.
     * 
     * @param r
     *             The radius of the Arc.  
     * @param radStart
     *            The start degree of the Arc.
     * @param radEnd
     *            The end degree of the Arc.
     */
	public Arc(double r, double radStart, double radEnd) {
		this.w = r * 2;
		this.h = r * 2;
		this.radStart = radStart;
		this.radEnd = radEnd;
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
		this.precisionangle = (this.radEnd-this.radStart)/this.precision;
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
		this.precisionangle = (this.radEnd-this.radStart)/this.precision;
	}
	
	/**
     * Creates a new Arc object using position of the Arc, width, height, start and end points of degree properties.
     * 
     * @param v
     *            The position of the Arc 
     * @param r
     *            The radius of the Arc.  
     * @param radStart
     *            The start degree of the Arc.
     * @param radEnd
     *            The end degree of the Arc.
     */
	public Arc(Vertex v, double r, double radStart, double radEnd) {
		this.x = v.x;
		this.y = v.y;
		this.w = r * 2;
		this.h = r * 2;
		this.radStart = radStart;
		this.radEnd = radEnd;
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
		this.precisionangle = (this.radEnd-this.radStart)/this.precision;
    }
    
	/**
     * Sets a Arc object using x and y-coordinate of the Arc, width, height, start and end points of degree properties.
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
	public void render(GL gl, GLU glu, int width, int height) {

		if (precision <= 0.0) {
			precision = 5.0;
		}

		if (this.fillColor.getA() != 1 || this.strokeColor.getA() != 1)
			gl.glDisable(GL.GL_DEPTH_TEST);


        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glRotated(this.rotate, 0, 0, 1.0);
        this.setTweenParameter(gl);
		
		if (this.fill) {
			this.fillColor.setup(gl);
			gl.glBegin(GL.GL_TRIANGLE_FAN);
			gl.glVertex2d(0, 0);

			for (th1 = radStart; th1 <= radEnd; th1 = th1 + precisionangle) {
				th1_rad = th1 / 180.0 * Math.PI;

				x1 = (w / 2.0) * Math.cos((float) th1_rad);
				y1 = (h / 2.0) * Math.sin((float) th1_rad);
				gl.glVertex2d(x1, y1);
			}
			gl.glEnd();
		}

		if (this.stroke) {
			getSceneStrokeColor().setup(gl);
			//this.strokeColor.setup(gl);
			gl.glLineWidth(this.strokeWidth);
			for (th1 = radStart; th1 <= radEnd-precisionangle; th1 = th1 + precisionangle) {
				th2 = th1 + precisionangle;
				th1_rad = th1 / 180.0 * Math.PI;
				th2_rad = th2 / 180.0 * Math.PI;

				x1 = (w / 2.0) * Math.cos((float) th1_rad);
				y1 = (h / 2.0) * Math.sin((float) th1_rad);
				x2 = (w / 2.0) * Math.cos((float) th2_rad);
				y2 = (h / 2.0) * Math.sin((float) th2_rad);

				gl.glBegin(GL.GL_LINES);
				gl.glVertex2d(x1, y1);
				gl.glVertex2d(x2, y2);
				gl.glEnd();
			}
		}
		
		gl.glPopMatrix();

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
		this.precisionangle = (this.radEnd-this.radStart)/detail;
		System.out.println(this.precisionangle);
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
	
	public double getStart(){
		return this.radStart;
	}
	
	public double getEnd(){
		return this.radEnd;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setXY(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public void setRotate(double angle){
		this.rotate = angle;
	}
	
	public double getRotate(){
		return this.rotate;
	}
	
	public double getWidth(){
		return this.w;
	}
	
	public double getHeight(){
		return this.h;
	}
	
	public double getRadius(){
		return this.w/2;
	}
	


}
