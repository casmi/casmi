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
 * Quad class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Quad extends Element implements Renderable {

    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double x3;
    private double y3;
    private double x4;
    private double y4;

    /**
     * Creates a new Quad object using x,y-coordinate of corners.
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
     * @param x4
     *              The x-coordinate of the fourth corner.
     * @param y4
     *              The y-coordinate of the fourth corner.
     */
    public Quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = x3;
        this.x4 = x4;
        this.y4 = y4;
    }
    
    /**
     * Creates a new Quad object using x,y-coordinate of corners.
     * 
     * @param v1
     *              The coordinates of the first corner.
     * @param v2
     *              The coordinates of the second corner.
     * @param v3
     *              The coordinates of the third corner.
     * @param v4
     *              The coordinates of the fourth corner.
     */
    public Quad(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        this.x1 = (float)v1.x;
        this.y1 = (float)v1.y;
        this.x2 = (float)v2.x;
        this.y2 = (float)v2.y;
        this.x3 = (float)v3.x;
        this.y3 = (float)v3.y;
        this.x4 = (float)v4.x;
        this.y4 = (float)v4.y;

    }

    @Override
    public void render(GL gl, GLU glu, int width, int height) {

        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glDisable(GL.GL_DEPTH_TEST);
        
        if (this.fill) {
            this.fillColor.setup(gl);
            gl.glBegin(GL.GL_QUADS);
            gl.glVertex2d(x1, y1);
            gl.glVertex2d(x2, y2);
            gl.glVertex2d(x3, y3);
            gl.glVertex2d(x4, y4);
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
            gl.glVertex2d(x3, y3);
            gl.glVertex2d(x4, y4);
            gl.glEnd();
            gl.glBegin(GL.GL_LINES);
            gl.glVertex2d(x4, y4);
            gl.glVertex2d(x1, y1);
            gl.glEnd();
        }
        
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glEnable(GL.GL_DEPTH_TEST);
    }
}
