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
    private double[] dx = new double[2];
    private double[] dy = new double[2];
    private double[] dz = new double[2];
    
    private Color startColor;
    private Color endColor;

    private int MODE;

    /**
     * Creates a new Line object
     */
    public Line() {}
    
    /**
     * Creates a new Line object using coordinates for the first and second point.
     * 
     * @param x1,y1
     *            The coordinates for the first anchor point.
     * @param x2,y2
     *            The coordinates for the first control point.
     */            
    public Line(double x1, double y1, double x2, double y2) {
        this.MODE = LINES_3D;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.z1 = 0;
        this.z2 = 0;
        calcG();
        dx[0] = x1 - x;
        dx[1] = x2 - x;
        dy[0] = y1 - y;
        dy[1] = y2 - y;
        dz[0] = z1 - 0;
        dz[1] = z2 - 0;
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
        this.MODE = LINES_3D;
        this.x1 = v1.getX();
        this.y1 = v1.getY();
        this.z1 = v1.getZ();
        this.x2 = v2.getX();
        this.y2 = v2.getY();
        this.z2 = v2.getZ();
        calcG();
        dx[0] = x1 - x;
        dx[1] = x2 - x;
        dy[0] = y1 - y;
        dy[1] = y2 - y;
        dz[0] = z1 - z;
        dz[1] = z2 - z;
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
        calcG();
        dx[0] = x1 - x;
        dx[1] = x2 - x;
        dy[0] = y1 - y;
        dy[1] = y2 - y;
        dz[0] = z1 - z;
        dz[1] = z2 - z;
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
        calcG();
        dx[0] = x1 - x;
        dx[1] = x2 - x;
        dy[0] = y1 - y;
        dy[1] = y2 - y;
        dz[0] = z1 - z;
        dz[1] = z2 - z;
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

        calcG();
        dx[0] = x1 - x;
        dx[1] = x2 - x;
        dy[0] = y1 - y;
        dy[1] = y2 - y;
        dz[0] = z1 - 0;
        dz[1] = z2 - 0;
    }
    
    public void set(boolean index, double x, double y) {
    	this.MODE = LINES;
    	if(index == true){
    		this.x1 = x;
            this.y1 = y;
    	} else {
    		this.x2 = x;
            this.y2 = y;
    	}

        calcG();
        dx[0] = x1 - this.x;
        dx[1] = x2 - this.x;
        dy[0] = y1 - this.y;
        dy[1] = y2 - this.y;
        dz[0] = z1 - 0;
        dz[1] = z2 - 0;
    }
    
    public void set(boolean index, double x, double y, double z) {
        this.MODE = LINES_3D;
        if(index==true){
        	this.x1 = x;
            this.y1 = y;
            this.z1 = z;
        } else {
            this.x2 = x;
            this.y2 = y;
            this.z2 = z;
        	
        }
        calcG();
        dx[0] = x1 - this.x;
        dx[1] = x2 - this.x;
        dy[0] = y1 - this.y;
        dy[1] = y2 - this.y;
        dz[0] = z1 - this.z;
        dz[1] = z2 - this.z;
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
        this.x1 = v1.getX();
        this.y1 = v1.getY();
        this.z1 = v1.getZ();
        this.x2 = v2.getX();
        this.y2 = v2.getY();
        this.z2 = v2.getZ();
        calcG();
        dx[0] = x1 - this.x;
        dx[1] = x2 - this.x;
        dy[0] = y1 - this.y;
        dy[1] = y2 - this.y;
        dz[0] = z1 - this.z;
        dz[1] = z2 - this.z;
    }
   

    @Override
    public void render(GL2 gl, GLU glu, int width, int height) {
    	if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || this.isDepthTest()==false) {
            gl.glDisable(GL2.GL_DEPTH_TEST);
        }

        getSceneStrokeColor().setup(gl);

        gl.glPushMatrix();

        {
            this.setTweenParameter(gl);

//            gl.glTranslated(x, y, z);

            switch (MODE) {
            case LINES:
                gl.glLineWidth(this.strokeWidth);
                gl.glBegin(GL2.GL_LINES);
                {
                    if (isGradation() && startColor != null)
                        getSceneColor(startColor).setup(gl);
                    
                    gl.glVertex2d(this.dx[0], this.dy[0]);
                    
                    if (isGradation() && endColor != null)
                        getSceneColor(endColor).setup(gl);
                    
                    gl.glVertex2d(this.dx[1], this.dy[1]);
                }
                gl.glEnd();
                break;
            case LINES_3D:
                gl.glLineWidth(this.strokeWidth);
                gl.glBegin(GL2.GL_LINES);
                {
                    if (isGradation() && startColor != null)
                        getSceneColor(startColor).setup(gl);
                    
                    gl.glVertex3d(this.dx[0], this.dy[0], this.dz[0]);
                    
                    if (isGradation() && endColor != null)
                        getSceneColor(endColor).setup(gl);
                    
                    gl.glVertex3d(this.dx[1], this.dy[1], this.dz[1]);
                }
                gl.glEnd();
                break;
            default:
                break;
            }
        }
        gl.glPopMatrix();
        
        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || this.isDepthTest()==false) {
            gl.glEnable(GL2.GL_DEPTH_TEST);
        }
    }
    
    private final void calcG() {
        x = (x1 + x2) / 2.0;
        y = (y1 + y2) / 2.0;
        z = (z1 + z2) / 2.0;
    }
    
//    private void setPoint() {
//    	this.x1 = x + dx[0];
//        this.x2 = x + dx[1];
//        this.y1 = y + dy[0];
//        this.y2 = y + dy[1];
//        this.z1 = z + dz[0];
//        this.z2 = z + dz[1];
//    }
    
    public void setCornerColor(int index, Color color) {
        if (index == 0) {
            if (startColor == null) {
                startColor = new RGBColor(0.0, 0.0, 0.0);
            }
            setGradation(true);
            this.startColor = color;
        } else if (index == 1) {
            if (endColor == null) {
                endColor = new RGBColor(0.0, 0.0, 0.0);
            }
            setGradation(true);
            this.endColor = color;
        }
    }

    public void setCornerColor(int index, ColorSet colorSet) {
        setCornerColor(index, new RGBColor(colorSet));
    }
    
    public Vertex getCorner(int index){
    	Vertex v = new Vertex();
    	if(index==0)
    		v.set(x1, y1, z1);
    	else
    		v.set(x2, y2, z2);
    	return v;
    }
}
