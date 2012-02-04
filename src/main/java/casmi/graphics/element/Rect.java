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

import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;

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
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double x3;
    private double y3;
    private double x4;
    private double y4;
    

    private Color startColor;
    private Color endColor;
    private Color gradationColor = new Color(0,0,0);
    private GradationMode mode = GradationMode.Horizontal;

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
        this.setTweenParameter(gl);
        if (this.fill) {
            getSceneFillColor().setup(gl);
            gl.glBegin(GL.GL_QUADS);
            if(isGradation()==false){
            gl.glVertex2d(x1, y1);
            gl.glVertex2d(x2, y2);
            gl.glVertex2d(x3, y3);
            gl.glVertex2d(x4, y4);
            } else {
            	this.gradationColor.setR((this.startColor.getR()+this.endColor.getR())/2);
            	this.gradationColor.setG((this.startColor.getG()+this.endColor.getG())/2);
            	this.gradationColor.setB((this.startColor.getB()+this.endColor.getB())/2);
            	this.gradationColor.setA((this.startColor.getA()+this.endColor.getA())/2);
            	switch(mode){
            	case Horizontal:
            		getSceneColor(this.startColor).setup(gl);
                    gl.glVertex2d(x1, y1);
                    gl.glVertex2d(x2, y2);
            		getSceneColor(this.endColor).setup(gl);
                    gl.glVertex2d(x3, y3);
                    gl.glVertex2d(x4, y4);
                    break;
            	case Vertical:
            		getSceneColor(this.startColor).setup(gl);
                    gl.glVertex2d(x1, y1);
            		getSceneColor(this.endColor).setup(gl);
                    gl.glVertex2d(x2, y2);
                    gl.glVertex2d(x3, y3);
            		getSceneColor(this.startColor).setup(gl);
                    gl.glVertex2d(x4, y4);
                    break;
            	case LeftSideways:
            		getSceneColor(this.startColor).setup(gl);
                    gl.glVertex2d(x1, y1);
            		getSceneColor(this.gradationColor).setup(gl);
                    gl.glVertex2d(x2, y2);
            		getSceneColor(this.endColor).setup(gl);
                    gl.glVertex2d(x3, y3);
            		getSceneColor(this.gradationColor).setup(gl);
                    gl.glVertex2d(x4, y4);
            		break;
            	case RightSideways:
            		getSceneColor(this.gradationColor).setup(gl);
                    gl.glVertex2d(x1, y1);
            		getSceneColor(this.endColor).setup(gl);
                    gl.glVertex2d(x2, y2);
            		getSceneColor(this.gradationColor).setup(gl);
                    gl.glVertex2d(x3, y3);
            		getSceneColor(this.startColor).setup(gl);
                    gl.glVertex2d(x4, y4);
            		break;
            		
            	}
            }
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
    
    public void setGradationColor(GradationMode mode, Color color1, Color color2){
		if(startColor==null||endColor==null){
			startColor = new Color(0,0,0);
			endColor = new Color(0,0,0);
		}
		startColor = color1;
		endColor = color2;
		this.mode = mode;
	}
	
	public void setGradationColor(GradationMode mode, ColorSet colorset1, ColorSet colorset2){
		setGradation(true);
		if(startColor==null||endColor==null){
			startColor = new Color(0,0,0);
			endColor = new Color(0,0,0);
		}
		startColor = Color.color(colorset1);
		endColor = Color.color(colorset2);
		this.mode = mode;
	}
	
	public void setGradationMode(GradationMode mode){
		this.mode = mode;
	}
}
