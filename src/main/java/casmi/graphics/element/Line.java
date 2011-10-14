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
 * Line class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Line extends Element implements Renderable {

    public static final int LINES         =1;
    public static final int LINES_3D      =3;
    
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double z1;
    private double z2;
    private double x=0;
    private double y=0;

    private int MODE;

    /**
     * Creates a new Line object
     */
    public Line() {

    }
    
    /**
     * Creates a new Line object using coordinates for the first and second point.
     * 
     * @param x1,y1
     *            The coordinates for the first anchor point.
     * @param x2,y2
     *            The coordinates for the first control point.
     */            
    public Line(double x1, double y1, double x2, double y2) {
        this.MODE = LINES;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    /**
     * Creates a new Line object using coordinates for the first and second point.
     * 
     * @param v1
     *            The coordinates for the first anchor point.
     * @param V2
     *            The coordinates for the first control point.
     */            
    public Line(Vertex v1, Vertex v2) {
        this.MODE = LINES;
        this.x1 = v1.x;
        this.y1 = v1.y;
        this.z1 = v1.z;
        this.x2 = v2.x;
        this.y2 = v2.y;
        this.z2 = v2.z;
    }

    /**
     * Creates a new Line object using coordinates for the first and second point.
     * 
     * @param x1,y1,z1
     *            The coordinates for the first anchor point.
     * @param x2,y2,z2
     *            The coordinates for the first control point.
     */            
    public Line(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.MODE = LINES_3D;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }
    
    /**
     * Sets the coordinates for the first and second point.
     * 
     * @param x1,y1
     *            The coordinates for the first anchor point.
     * @param x2,y2
     *            The coordinates for the first control point.
     */                        
    public void set(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.MODE = LINES_3D;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }
    
    /**
     * Sets the coordinates for the first and second point.
     * 
     * @param x1,y1
     *            The coordinates for the first anchor point.
     * @param x2,y2
     *            The coordinates for the first control point.
     */            
    public void set(double x1, double y1, double x2, double y2) {
        this.MODE = LINES;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    /**
     * Sets the coordinates for the first and second point.
     * 
     * @param v1
     *            The coordinates for the first anchor point.
     * @param v2
     *            The coordinates for the first control point.
     */            
    public void set(Vertex v1, Vertex v2) {
        this.MODE = LINES;
        this.x1 = v1.x;
        this.y1 = v1.y;
        this.z1 = v1.z;
        this.x2 = v2.x;
        this.y2 = v2.y;
        this.z2 = v2.z;
    }
   

    @Override
    public void render(GL gl, GLU glu, int width, int height) {
        calcG();
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glDisable(GL.GL_DEPTH_TEST);

        getSceneStrokeColor().setup(gl);

        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glRotated(rotate, 0, 0, 1.0);
        this.setTweenParameter(gl);

        switch (MODE) {
        case LINES:
            gl.glLineWidth(this.strokeWidth);
            gl.glBegin(GL.GL_LINES);
            gl.glVertex2d(this.x1-x, this.y1-y);
            gl.glVertex2d(this.x2-x, this.y2-y);
            gl.glEnd();
            break;
        case LINES_3D:
            gl.glLineWidth(this.strokeWidth);
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(this.x1-x, this.y1-y, this.z1);
            gl.glVertex3d(this.x2-x, this.y2-y, this.z2);
            gl.glEnd();
            break;
        default:
            break;
        }
        
        gl.glPopMatrix();
        
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glEnable(GL.GL_DEPTH_TEST);
    }
    
    private void calcG(){
    	x = (x1+x2)/2.0;
    	y = (y1+y2)/2.0;
    }
    
    public double getX(){
    	return this.x;
    }
    
    public double getY(){
    	return this.y;
    }
    
    public void setX(double x){
    	this.x = x;
    }
    
    public void setY(double y){
    	this.y = y;
    }
    
    
    public void setXY(double x, double y){
    	this.x = x;
    	this.y = y;
    }

}
