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
import casmi.matrix.Vertex;

/**
 * Bezier class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Bezier extends Element implements Renderable {

    private float points[] = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    private int detail = 30;
    
    private Color startColor;
    private Color endColor;
    private Color gradationColor = new Color(0,0,0);

    /**
     * Creates a new Bezier object using coordinates for the anchor and control points.
     * 
     * @param x1,y1
     *            The coordinates for the first anchor point.
     * @param x2,y2
     *            The coordinates for the first control point.
     * @param x3,y3
     *            The coordinates for the second control point.
     * @param x4,y4
     *            The coordinates for the second ancor point.
     */
    public Bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        this.points[0] = x1;
        this.points[1] = y1;
        this.points[2] = 0;
        this.points[3] = x2;
        this.points[4] = y2;
        this.points[5] = 0;
        this.points[6] = x3;
        this.points[7] = y3;
        this.points[8] = 0;
        this.points[9] = x4;
        this.points[10] = y4;
        this.points[11] = 0;
        set();
    }

    /**
     * Creates a new Bezier object using coordinates for the anchor and control points.
     * 
     * @param x1,y1
     *            The coordinates for the first anchor point.
     * @param x2,y2
     *            The coordinates for the first control point.
     * @param x3,y3
     *            The coordinates for the second control point.
     * @param x4,y4
     *            The coordinates for the second ancor point.
     */
    public Bezier(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        this.points[0] = (float)x1;
        this.points[1] = (float)y1;
        this.points[2] = 0;
        this.points[3] = (float)x2;
        this.points[4] = (float)y2;
        this.points[5] = 0;
        this.points[6] = (float)x3;
        this.points[7] = (float)y3;
        this.points[8] = 0;
        this.points[9] = (float)x4;
        this.points[10] = (float)y4;
        this.points[11] = 0;
        set();
    }

    /**
     * Creates a new Bezier object using coordinates for the anchor and control points.
     * 
     * @param x1,y1,z1
     *            The coordinates for the first anchor point.
     * @param x2,y2,z2
     *            The coordinates for the first control point.
     * @param x3,y3,z3
     *            The coordinates for the second control point.
     * @param x4,y4,z4
     *            The coordinates for the second ancor point.
     */
    public Bezier(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        this.points[0] = x1;
        this.points[1] = y1;
        this.points[2] = z1;
        this.points[3] = x2;
        this.points[4] = y2;
        this.points[5] = z2;
        this.points[6] = x3;
        this.points[7] = y3;
        this.points[8] = z3;
        this.points[9] = x4;
        this.points[10] = y4;
        this.points[11] = z4;
        set();
    }

    /**
     * Creates a new Bezier object using coordinates for the anchor and control points.
     * 
     * @param x1,y1,z1
     *            The coordinates for the first anchor point.
     * @param x2,y2,z2
     *            The coordinates for the first control point.
     * @param x3,y3,z3
     *            The coordinates for the second control point.
     * @param x4,y4,z4
     *            The coordinates for the second ancor point.
     */
    public Bezier(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
        this.points[0] = (float)x1;
        this.points[1] = (float)y1;
        this.points[2] = (float)z1;
        this.points[3] = (float)x2;
        this.points[4] = (float)y2;
        this.points[5] = (float)z2;
        this.points[6] = (float)x3;
        this.points[7] = (float)y3;
        this.points[8] = (float)z3;
        this.points[9] = (float)x4;
        this.points[10] = (float)y4;
        this.points[11] = (float)z4;
        set();
    }
    
    /**
     * Creates a new Bezier object using coordinates for the anchor and control points.
     * 
     * @param v1
     *            The coordinates for the first anchor point.
     * @param v2
     *            The coordinates for the first control point.
     * @param v3
     *            The coordinates for the second control point.
     * @param v4
     *            The coordinates for the second ancor point.
     */
    public Bezier(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        this.points[0] = (float)v1.x;
        this.points[1] = (float)v1.y;
        this.points[2] = (float)v1.z;
        this.points[3] = (float)v2.x;
        this.points[4] = (float)v2.y;
        this.points[5] = (float)v2.z;
        this.points[6] = (float)v3.x;
        this.points[7] = (float)v3.y;
        this.points[8] = (float)v3.z;
        this.points[9] = (float)v4.x;
        this.points[10] = (float)v4.y;
        this.points[11] = (float)v4.z;
        set();
    }
    
    public void setNode(int number, double x, double y){
    	if(number<=0)
    		number = 0;
    	if(number>=3)
    		number = 3;
    	this.points[number*3] = (float)x;
    	this.points[number*3+1] = (float)y;
    	this.points[number*3+2] = 0;
    	set();
    }
    
    public void setNode(int number, double x, double y, double z){
    	if(number<=0)
    		number = 0;
    	if(number>=3)
    		number = 3;
    	this.points[number*3] = (float)x;
    	this.points[number*3+1] = (float)y;
    	this.points[number*3+2] = (float)z;
    	set();
    }
    
    public void setNode(int number, Vertex v){
    	if(number<=0)
    		number = 0;
    	if(number>=3)
    		number = 3;
    	this.points[number*3] = (float)v.x;
    	this.points[number*3+1] = (float)v.y;
    	this.points[number*3+2] = (float)v.z;
    	set();
    }

    @Override
    public void render(GL gl, GLU glu, int width, int height) {
        
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glDisable(GL.GL_DEPTH_TEST);

        gl.glPushMatrix();
        this.setTweenParameter(gl);
        gl.glTranslated(-this.points[0],-this.points[1], -this.points[2]);
        if (this.fill) {

            getSceneFillColor().setup(gl);

            gl.glMap1f(GL.GL_MAP1_VERTEX_3, 0.0f, 1.0f, 3, 4, java.nio.FloatBuffer.wrap(points));
            gl.glEnable(GL.GL_MAP1_VERTEX_3);
            gl.glBegin(GL.GL_TRIANGLE_FAN);

            for (int i = 0; i <= detail; i++){
                if (i == 0 && isGradation() == true && startColor != null)
                    getSceneColor(this.startColor).setup(gl);
                if (i == detail && isGradation() == true && endColor != null)
                    getSceneColor(this.endColor).setup(gl);
                if (i != 0 && i != detail && isGradation() && endColor != null && startColor != null) {
                    gradationColor = Color.lerpColor(this.startColor, this.endColor, (i / (double)detail));
                    getSceneColor(this.gradationColor).setup(gl);
                }
                gl.glEvalCoord1f((float)(i / (float)detail));
            }

            gl.glEnd();
            gl.glDisable(GL.GL_MAP1_VERTEX_3);
        }

        if (this.stroke) {

            getSceneStrokeColor().setup(gl);

            gl.glMap1f(GL.GL_MAP1_VERTEX_3, 0.0f, 1.0f, 3, 4, java.nio.FloatBuffer.wrap(points));
            gl.glEnable(GL.GL_MAP1_VERTEX_3);
            gl.glBegin(GL.GL_LINE_STRIP);

            for (int i = 0; i <= detail; i++) {
                if (i == 0 && isGradation()&& startColor != null)
                    getSceneColor(this.startColor).setup(gl);
                if (i == detail && isGradation() && endColor != null)
                    getSceneColor(this.endColor).setup(gl);
                if (i != 0 && i != detail && isGradation() && endColor != null && startColor != null) {
                    gradationColor = Color.lerpColor(this.startColor, this.endColor, (i / (double)detail));
                    getSceneColor(this.gradationColor).setup(gl);
                }
                gl.glEvalCoord1f((float)(i / (float)detail));
            }
            gl.glEnd();
            gl.glDisable(GL.GL_MAP1_VERTEX_3);
        }

        gl.glTranslated(this.points[0],this.points[1], this.points[2]);
        gl.glPopMatrix();
        
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glEnable(GL.GL_DEPTH_TEST);

    }

//    public void bezierPoint(float t) {
//        float tmp = 0;
//        gl.glMap1f(GL.GL_MAP1_VERTEX_3, 0.0f, 1.0f, 3, 4, java.nio.FloatBuffer.wrap(points));
//        gl.glEnable(GL.GL_MAP1_VERTEX_3);
//        gl.glEvalCoord1f(t);
//        gl.glDisable(GL.GL_MAP1_VERTEX_3);
//    }

    
    /**
     * Returns the detail of this Bezier.
     */
    public int getDetail() {
    	return detail;
    }
    
    /**
     * Set the detail of this Bezier.
     * 
     * @param detail
     *             The detail of the Bezier.
     */
    public void setDetail(int d) {
        detail = d;
    }
    
    private void set(){
    	x = this.points[0];
    	y = this.points[1];
    	z = this.points[2];
    }
    
    public void setAnchorColor(int index,ColorSet colorset){
    	if(index==0){
    	if(startColor == null)
			startColor = new Color(0,0,0);
    	setGradation(true);
    	this.startColor = Color.color(colorset);
    	}
    	if(index==1){
        	if(endColor == null)
    			endColor = new Color(0,0,0);
        	setGradation(true);
        	this.endColor = Color.color(colorset);
        	}
    }
    
    public void setAnchorColor(int index,Color color){
    	if(index==0){
    	if(startColor == null)
			startColor = new Color(0,0,0);
    	setGradation(true);
    	this.startColor = color;
    	}
    	if(index==1){
        	if(endColor == null)
    			endColor = new Color(0,0,0);
        	setGradation(true);
        	this.endColor = color;
		}
	}

}
