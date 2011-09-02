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
 * Triangle class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Triangle extends Element implements Renderable {

    public static final int TRIANGLE = 0;
    public static final int TRIANGLE_3D = 4;
    
    private double x1;
    private double y1;
    private double z1;
    private double x2;
    private double y2;
    private double z2;
    private double x3;
    private double y3;
    private double z3;
    private int MODE;

    /**
     * Creates a new Triangle object using x,y-coordinate of corners.
     * 
     * @param x1
     *              The x-coordinate of the first corner.
     * @param y1
     *              The y-coordinate of the first corner.
     * @param x2
     *              The x-coordinate of the second corner.
     * @param y2
     *              The y-coordinate of the second corner.
     * @param x3
     *              The x-coordinate of the third corner.
     * @param y3
     *              The y-coordinate of the third corner.
     */
    public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        this.MODE = TRIANGLE;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }
    
    /**
     * Creates a new Triangle object using x,y-coordinate of corners.
     * 
     * @param v1
     *              The coordinates of the first corner.
     * @param v2
     *              The coordinates of the second corner.
     * @param v3
     *              The coordinates of the third corner.
     */
    public Triangle(Vertex v1, Vertex v2, Vertex v3){
        this.MODE = TRIANGLE_3D;
        this.x1 = v1.x;
        this.y1 = v1.y;
        this.z1 = v1.z;
        this.x2 = v2.x;
        this.y2 = v2.y;
        this.z1 = v2.z;
        this.x3 = v3.x;
        this.y3 = v3.y;
        this.z1 = v3.z;
    }

    /**
     * Creates a new Triangle object using x,y-coordinate of corners.
     * 
     * @param x1
     *              The x-coordinate of the first corner.
     * @param y1
     *              The y-coordinate of the first corner.
     * @param z1
     *              The z-coordinate of the first corner.
     * @param x2
     *              The x-coordinate of the second corner.
     * @param y2
     *              The y-coordinate of the second corner.
     * @param z2
     *              The z-coordinate of the second corner.
     * @param x3
     *              The x-coordinate of the third corner.
     * @param y3
     *              The y-coordinate of the third corner.
     * @param z3
     *              The z-coordinate of the third corner.
     */
    public Triangle(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
        this.MODE = TRIANGLE_3D;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.x3 = x3;
        this.y3 = y3;
        this.z3 = z3;
    }
    
    /**
     * Sets x,y-coordinate of corners.
     * 
     * @param x1
     *              The x-coordinate of the first corner.
     * @param y1
     *              The y-coordinate of the first corner.
     * @param x2
     *              The x-coordinate of the second corner.
     * @param y2
     *              The y-coordinate of the second corner.
     * @param x3
     *              The x-coordinate of the third corner.
     * @param y3
     *              The y-coordinate of the third corner.
     */
    public void set(double x1, double y1, double x2, double y2, double x3, double y3) {
        this.MODE = TRIANGLE;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }
    
    /**
     * Sets x,y-coordinate of corners.
     * 
     * @param v1
     *              The coordinates of the first corner.
     * @param v2
     *              The coordinates of the second corner.
     * @param v3
     *              The coordinates of the third corner.
     */
    public void set(Vertex v1, Vertex v2, Vertex v3){
        this.MODE = TRIANGLE_3D;
        this.x1 = v1.x;
        this.y1 = v1.y;
        this.z1 = v1.z;
        this.x2 = v2.x;
        this.y2 = v2.y;
        this.z1 = v2.z;
        this.x3 = v3.x;
        this.y3 = v3.y;
        this.z1 = v3.z;
    }

    /**
     * Sets x,y-coordinate of corners.
     * 
     * @param x1
     *              The x-coordinate of the first corner.
     * @param y1
     *              The y-coordinate of the first corner.
     * @param z1
     *              The z-coordinate of the first corner.
     * @param x2
     *              The x-coordinate of the second corner.
     * @param y2
     *              The y-coordinate of the second corner.
     * @param z2
     *              The z-coordinate of the second corner.
     * @param x3
     *              The x-coordinate of the third corner.
     * @param y3
     *              The y-coordinate of the third corner.
     * @param z3
     *              The z-coordinate of the third corner.
     */
    public void set(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
        this.MODE = TRIANGLE_3D;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.x3 = x3;
        this.y3 = y3;
        this.z3 = z3;
    }
    
    public void setCorner(int number, double x, double y){
    	if(number<=0){
    		this.x1 = x;
    		this.y1 = y;
    	} else if(number==1){
    		this.x2 = x;
    		this.y2 = y;
    	} else if(number>=2){
    		this.x3 = x;
    		this.y3 = y;
    	}
    }
    
    public void setCorner(int number, double x, double y, double z){
    	if(number<=0){
    		this.x1 = x;
    		this.y1 = y;
    		this.z1 = z;
    	} else if(number==1){
    		this.x2 = x;
    		this.y2 = y;
    		this.z2 = z;
    	} else if(number>=2){
    		this.x3 = x;
    		this.y3 = y;
    		this.z3 = z;
    	}
    }
    
    public void setConer(int number, Vertex v){
    	if(number<=0){
    		this.x1 = v.x;
    		this.y1 = v.y;
    		this.z1 = v.z;
    	} else if(number==1){
    		this.x2 = v.x;
    		this.y2 = v.y;
    		this.z2 = v.z;
    	} else if(number>=2){
    		this.x3 = v.x;
    		this.y3 = v.y;
    		this.z3 = v.z;
    	}
    }
    

    @Override
    public void render(GL gl, GLU glu, int width, int height) {
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glDisable(GL.GL_DEPTH_TEST);
        switch (MODE) {
        case TRIANGLE:
            
            if (this.fill) {
                this.fillColor.setup(gl);
                gl.glBegin(GL.GL_TRIANGLE_FAN);
                gl.glVertex2d(x1, y1);
                gl.glVertex2d(x2, y2);
                gl.glVertex2d(x3, y3);
                gl.glEnd();
            }
            
            if (this.stroke) {
                gl.glLineWidth(this.strokeWidth);
                this.strokeColor.setup(gl);
                gl.glBegin(GL.GL_LINES);
                gl.glVertex2d(x1, y1);
                gl.glVertex2d(x2, y2);
                gl.glEnd();
                gl.glBegin(GL.GL_LINES);
                gl.glVertex2d(x2, y2);
                gl.glVertex2d(x3, y3);
                gl.glEnd();
                gl.glBegin(GL.GL_LINES);
                gl.glVertex2d(x1, y1);
                gl.glVertex2d(x3, y3);
                gl.glEnd();
            }
            
            break;
            
        case TRIANGLE_3D:
            
            if (this.fill) {
                this.fillColor.setup(gl);
                gl.glBegin(GL.GL_TRIANGLE_FAN);
                gl.glVertex3d(x1, y1, z1);
                gl.glVertex3d(x2, y2, z2);
                gl.glVertex3d(x3, y3, z3);
                gl.glEnd();
            }
            
            if (this.stroke) {
                gl.glLineWidth(this.strokeWidth);
                this.strokeColor.setup(gl);
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(x1, y1, z1);
                gl.glVertex3d(x2, y2, z2);
                gl.glEnd();
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(x2, y2, z2);
                gl.glVertex3d(x3, y3, z3);
                gl.glEnd();
                gl.glBegin(GL.GL_LINES);
                gl.glVertex3d(x1, y1, z1);
                gl.glVertex3d(x3, y3, z3);
                gl.glEnd();
            }
            
            break;
        default:
            // do nothing
            break;
        }
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glEnable(GL.GL_DEPTH_TEST);
    }
}