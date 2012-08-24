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

/**
 * Rect class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Rect extends Element implements Renderable {

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
    private Color gradationColor = new RGBColor(0.0, 0.0, 0.0);
    private GradationMode mode = GradationMode.HORIZONTAL;

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
    public void set(double w, double h) {
    	this.w = w;
    	this.h = h;
    }
    
    public void setWidth(double w) {
    	this.w = w;
    }
    
    public void setHeight(double h) {
    	this.h = h;
    }
    
    public double getWidth() {
    	return this.w;
    }
    
    public double getHeight() {
    	return this.h;
    }
    

    private final void calcRect() {
        this.x1 = - w / 2;
        this.y1 =   h / 2;
        this.x2 = - w / 2;
        this.y2 = - h / 2;
        this.x3 =   w / 2;
        this.y3 = - h / 2;
        this.x4 =   w / 2;
        this.y4 =   h / 2;
    }
    
    @Override
    public void render(GL2 gl, GLU glu, int width, int height) {
        calcRect();
        if (getSceneStrokeColor().getAlpha() < 1.000 || getSceneFillColor().getAlpha() < 1.00 || this.isDepthTest()==false)
          	gl.glDisable(GL2.GL_DEPTH_TEST);
        gl.glPushMatrix();
        {
            this.setTweenParameter(gl);
            
            if (this.fill) {
                getSceneFillColor().setup(gl);
                gl.glBegin(GL2.GL_QUADS);
                if (!isGradation()) {
                    gl.glVertex2d(x1, y1);
                    gl.glVertex2d(x2, y2);
                    gl.glVertex2d(x3, y3);
                    gl.glVertex2d(x4, y4);
                } else {
                    this.gradationColor.setRed((this.startColor.getRed()     + this.endColor.getRed())   / 2);
                    this.gradationColor.setGreen((this.startColor.getGreen() + this.endColor.getGreen()) / 2);
                    this.gradationColor.setBlue((this.startColor.getBlue()   + this.endColor.getBlue())  / 2);
                    this.gradationColor.setAlpha((this.startColor.getAlpha() + this.endColor.getAlpha()) / 2);
                    switch (mode) {
                    case HORIZONTAL:
                        getSceneColor(this.startColor).setup(gl);
                        gl.glVertex2d(x1, y1);
                        gl.glVertex2d(x2, y2);
                        getSceneColor(this.endColor).setup(gl);
                        gl.glVertex2d(x3, y3);
                        gl.glVertex2d(x4, y4);
                        break;
                    case VERTICAL:
                        getSceneColor(this.startColor).setup(gl);
                        gl.glVertex2d(x1, y1);
                        getSceneColor(this.endColor).setup(gl);
                        gl.glVertex2d(x2, y2);
                        gl.glVertex2d(x3, y3);
                        getSceneColor(this.startColor).setup(gl);
                        gl.glVertex2d(x4, y4);
                        break;
                    case LEFT_SIDEWAYS:
                        getSceneColor(this.startColor).setup(gl);
                        gl.glVertex2d(x1, y1);
                        getSceneColor(this.gradationColor).setup(gl);
                        gl.glVertex2d(x2, y2);
                        getSceneColor(this.endColor).setup(gl);
                        gl.glVertex2d(x3, y3);
                        getSceneColor(this.gradationColor).setup(gl);
                        gl.glVertex2d(x4, y4);
                        break;
                    case RIGHT_SIDEWAYS:
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
                gl.glBegin(GL2.GL_LINE_STRIP);
                gl.glVertex2d(x1, y1);
                gl.glVertex2d(x2, y2);
                gl.glVertex2d(x3, y3);
                gl.glVertex2d(x4, y4);
                gl.glVertex2d(x1, y1);
                gl.glEnd();
            }
        }
        gl.glPopMatrix();
        if (getSceneStrokeColor().getAlpha() < 1.00 || getSceneFillColor().getAlpha() < 1.00 || this.isDepthTest()==false)
        	gl.glEnable(GL2.GL_DEPTH_TEST);
    }
    
    public void setGradationColor(GradationMode mode, Color color1, Color color2) {
        setGradation(true);
        if (startColor == null || endColor == null) {
            startColor = new RGBColor(0.0, 0.0, 0.0);
            endColor   = new RGBColor(0.0, 0.0, 0.0);
        }
        startColor = color1;
        endColor   = color2;
        this.mode  = mode;
	}
	
	public void setGradationColor(GradationMode mode, ColorSet colorSet1, ColorSet colorSet2){
	    setGradationColor(mode, new RGBColor(colorSet1), new RGBColor(colorSet2));
	}
	
	public void setGradationMode(GradationMode mode) {
		this.mode = mode;
	}
}
