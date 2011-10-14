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

/**
 * Rect class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Rect extends Element implements Renderable{

    private double w;
    private double h;
    private double x=0;
    private double y=0;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double x3;
    private double y3;
    private double x4;
    private double y4;

    /**
     * Creates a new Rect object using width and height.
     *
     * @param w
     *              The width of the rectangle.
     * @param h 
     *              The height of the rectangle.                          
     */
    public Rect(double w, double h) {
        this.w = w;
        this.h = h;
    }
    
    public Rect(double x, double y, double w, double h) {
        this.w = w;
        this.h = h;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Sets a Rect's position of the upper-left corner, width and height.
     *
     * @param w
     *              The width of the rectangle.
     * @param h 
     *              The height of the rectangle.                          
     */
    public void set(double w, double h){
    	this.w = w;
    	this.h = h;
    }
    
    public void setWidth(double w){
    	this.w = w;
    }
    
    public void setHeight(double h){
    	this.h = h;
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
    
    public double getX(){
    	return this.x;
    }
    
    public double getY(){
    	return this.y;
    }
    

    private void calcRect() {
        this.x1 = 0 - w / 2;
        this.y1 = 0 + h / 2;
        this.x2 = 0 - w / 2;
        this.y2 = 0 - h / 2;
        this.x3 = 0 + w / 2;
        this.y3 = 0 - h / 2;
        this.x4 = 0 + w / 2;
        this.y4 = 0 + h / 2;
    }
    
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
        calcRect();
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glDisable(GL.GL_DEPTH_TEST);

        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glRotated(rotate, 0, 0, 1.0);
        this.setTweenParameter(gl);
        if (this.fill) {
            getSceneFillColor().setup(gl);
            gl.glBegin(GL.GL_QUADS);
            gl.glVertex2d(x1, y1);
            gl.glVertex2d(x2, y2);
            gl.glVertex2d(x3, y3);
            gl.glVertex2d(x4, y4);
            gl.glEnd();
        }

        if (this.stroke) {
            gl.glLineWidth(this.strokeWidth);
            getSceneStrokeColor().setup(gl);
            gl.glBegin(GL.GL_LINE_STRIP);
            gl.glVertex2d(x1, y1);
            gl.glVertex2d(x2, y2);
            gl.glVertex2d(x3, y3);
            gl.glVertex2d(x4, y4);
            gl.glVertex2d(x1, y1);
            gl.glEnd();
        }
        
        gl.glPopMatrix();
        
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glEnable(GL.GL_DEPTH_TEST);
    }
}
