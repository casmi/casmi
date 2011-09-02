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
 * Ellipse class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Ellipse extends Element implements Renderable {

    private double x;
    private double y;
    private double w;
    private double h;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double detail = 10;

    private double th1;
    private double th2;
    private double th1_rad;
    private double th2_rad;

    
    /**
     * Creates a new Ellipse object using x,y-coordinate, width and height.
     * 
     * @param x
     *            The x-coordinate of the center of the Ellipse.
     * @param y
     *            The y-coordinate of the center of the Ellipse.
     * @param w 
     *            The width of the Ellipse.
     * @param h
     *            The height of the Ellipse.
     */
    public Ellipse(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    /**
     * Creates a new Ellipse object using x,y-coordinate, width and height.
     * 
     * @param v
     *            The x,y-coordinate of the center of the Ellipse.
     * @param w 
     *            The width of the Ellipse.
     * @param h
     *            The height of the Ellipse.
     */
    public Ellipse(Vertex v, double w, double h) {
        this.x = v.x;
        this.y = v.x;
        this.w = w;
        this.h = h;
    }
    
    /**
     * Creates a new Ellipse object using width and height.
     * 

     * @param w 
     *            The width of the Ellipse.
     * @param h
     *            The height of the Ellipse.
     */
    public Ellipse(double w, double h) {
        this.x = 0;
        this.y = 0;
        this.w = w;
        this.h = h;
    }

    /**
     * Creates a new Ellipse object using x,y-coordinate and radius.
     * 
     * @param x
     *            The x-coordinate of the center of the Ellipse.
     * @param y
     *            The y-coordinate of the center of the Ellipse.
     * @param r 
     *            The radius of the Ellipse.
     */
    public Ellipse(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.w = r*2;
        this.h = r*2;
    }
    
    /**
     * Creates a new Ellipse object using x,y-coordinate and radius.
     * 
     * @param v
     *            The x,y-coordinate of the center of the Ellipse.
     * @param r 
     *            The radius of the Ellipse.
     */
    public Ellipse(Vertex v, double r) {
        this.x = v.x;
        this.y = v.y;
        this.w = r*2;
        this.h = r*2;
    }
    
    /**
     * Creates a new Ellipse object using radius.
     * 
     * @param r 
     *            The radius of the Ellipse.
     */
    public Ellipse(double r) {
        this.x = 0;
        this.y = 0;
        this.w = r*2;
        this.h = r*2;
    }
    
    /**
     * Sets x,y-coordinate, width and height.
     * 
     * @param x
     *            The x-coordinate of the center of the Ellipse.
     * @param y
     *            The y-coordinate of the center of the Ellipse.
     * @param w 
     *            The width of the Ellipse.
     * @param h
     *            The height of the Ellipse.
     */
    public void set(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    /**
     * Sets x,y-coordinate, width and height.
     * 
     * @param v
     *            The x,y-coordinate of the center of the Ellipse.
     * @param w 
     *            The width of the Ellipse.
     * @param h
     *            The height of the Ellipse.
     */
    public void set(Vertex v, double w, double h) {
        this.x = v.x;
        this.y = v.x;
        this.w = w;
        this.h = h;
    }

    /**
     * Sets x,y-coordinate and radius.
     * 
     * @param x
     *            The x-coordinate of the center of the Ellipse.
     * @param y
     *            The y-coordinate of the center of the Ellipse.
     * @param r 
     *            The radius of the Ellipse.
     */
    public void set(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.w = r*2;
        this.h = r*2;
    }
    
    /**
     * Sets x,y-coordinate and radius.
     * 
     * @param v
     *            The x,y-coordinate of the center of the Ellipse.
     * @param r 
     *            The radius of the Ellipse.
     */
    public void set(Vertex v, double r) {
        this.x = v.x;
        this.y = v.y;
        this.w = r*2;
        this.h = r*2;
    }
    
    /**
     * Sets x-coordinate.
     * 
     * @param x
     *            The x-coordinate of the center of the Ellipse.
     */
    public void setX(double x){
    	this.x = x;
    }
    
    /**
     * Sets y-coordinate.
     * 
     * @param y
     *            The y-coordinate of the center of the Ellipse.
     */
    public void setY(double y){
    	this.y = y;
    }
    
    /**
     * Sets radius.
     * 
     * @param r
     *            The radius of the Ellipse.
     */
    public void setRadius(double r){
    	this.w = r*2;
    	this.h = r*2;
    }
    
    /**
     * Sets width.
     * 
     * @param w
     *            The width of the Ellipse.
     */
    public void setWidth(double w){
    	this.w = w;
    }
    
    /**
     * Sets height.
     * 
     * @param h
     *            The height of the Ellipse.
     */
    public void setHeight(double h){
    	this.h = h;
    }

    @Override
    public void render(GL gl, GLU glu, int width, int height) {

        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glDisable(GL.GL_DEPTH_TEST);
        
        if (this.fill) {
            this.fillColor.setup(gl);
            gl.glBegin(GL.GL_TRIANGLE_FAN);
            gl.glVertex2d(x, y);

            for (th1 = 0.0; th1 <= 360.0; th1 = th1 + detail)
            {
                th1_rad = th1 / 180.0 * Math.PI;

                x1 = (w / 2.0) * Math.cos(th1_rad) + x;
                y1 = (h / 2.0) * Math.sin(th1_rad) + y;
                gl.glVertex2d(x1, y1);
            }
            gl.glEnd();
        }

        if (this.stroke) {
            this.strokeColor.setup(gl);
            gl.glLineWidth(this.strokeWidth);
            for (th1 = 0.0; th1 <= 360.0; th1 = th1 + detail)
            {
                th2 = th1 + 10.0;
                th1_rad = th1 / 180.0 * Math.PI;
                th2_rad = th2 / 180.0 * Math.PI;

                x1 = (w / 2.0) * Math.cos(th1_rad) + x;
                y1 = (h / 2.0) * Math.sin(th1_rad) + y;
                x2 = (w / 2.0) * Math.cos(th2_rad) + x;
                y2 = (h / 2.0) * Math.sin(th2_rad) + y;

                gl.glBegin(GL.GL_LINES);
                gl.glVertex2d(x1, y1);
                gl.glVertex2d(x2, y2);
                gl.glEnd();
            }
        }
        
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glEnable(GL.GL_DEPTH_TEST);
    }

    /**
     * Returns the detail of this Ellipse.
     */
    public double getDetail() {
        return detail;
    }

    /**
     * Sets the detail  of this Ellipse.
     * 
     * @param detail
     *             The precision of the Ellipse.
     */
    public void setDetail(double detail) {
        this.detail = detail;
    }

}
