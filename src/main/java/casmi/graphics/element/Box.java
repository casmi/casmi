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
 * Box class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Box extends Element implements Renderable {

    private static double STROKE_BIAS_RATIO = 1.01;
    
    private double width;
    private double height;

    public Box(double size) {
        this.width = this.height = size;
    }

    public Box(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(GL gl, GLU glu, int width, int height) {

        if (this.fillColor.getA() < 1.0 || this.strokeColor.getA() < 1.0) {
            gl.glDisable(GL.GL_DEPTH_TEST);
        }
        
        gl.glPushMatrix();
        
        if (this.fill) {
            this.fillColor.setup(gl);
            gl.glLineWidth(this.strokeWidth);
            
            gl.glPushMatrix();
            gl.glScaled(1.0, this.height / this.width, 1.0);
            drawBox(gl, (float)this.width, GL.GL_QUADS);
            gl.glPopMatrix();
        }
        //gl.glDisable(GL.GL_DEPTH_TEST);
        if (this.stroke) {
            this.strokeColor.setup(gl);

            gl.glPushMatrix();
            gl.glScaled(1.0, this.height / this.width, 1.0);
            drawBox(gl, (float)(this.width * STROKE_BIAS_RATIO), GL.GL_LINE_STRIP);
            gl.glPopMatrix();
        }
        
        gl.glPopMatrix();
        
        if (this.fillColor.getA() < 1.0 || this.strokeColor.getA() < 1.0) {
            gl.glEnable(GL.GL_DEPTH_TEST);
        }
    }

    private static float[][] boxVertices;

    private static final float[][] boxNormals = {
        {-1.0f, 0.0f, 0.0f},
        {0.0f, 1.0f, 0.0f},
        {1.0f, 0.0f, 0.0f},
        {0.0f, -1.0f, 0.0f},
        {0.0f, 0.0f, 1.0f},
        {0.0f, 0.0f, -1.0f}
    };

    private static final int[][] boxFaces = {
        {0, 1, 2, 3},
        {3, 2, 6, 7},
        {7, 6, 5, 4},
        {4, 5, 1, 0},
        {5, 6, 2, 1},
        {7, 4, 0, 3}
    };

    private void drawBox(GL gl, float size, int type) {

        if (boxVertices == null) {

            float[][] v = new float[8][];

            for (int i = 0; i < 8; i++) {
                v[i] = new float[3];
            }

            v[0][0] = v[1][0] = v[2][0] = v[3][0] = -0.5f;
            v[4][0] = v[5][0] = v[6][0] = v[7][0] = 0.5f;
            v[0][1] = v[1][1] = v[4][1] = v[5][1] = -0.5f;
            v[2][1] = v[3][1] = v[6][1] = v[7][1] = 0.5f;
            v[0][2] = v[3][2] = v[4][2] = v[7][2] = -0.5f;
            v[1][2] = v[2][2] = v[5][2] = v[6][2] = 0.5f;

            boxVertices = v;
        }

        float[][] v = boxVertices;
        float[][] n = boxNormals;
        int[][] faces = boxFaces;

        for (int i = 5; i >= 0; i--) {
            gl.glBegin(type);
            gl.glNormal3fv(n[i], 0);
            
            float[] vt = v[faces[i][0]];
            gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);
            vt = v[faces[i][1]];
            gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);
            vt = v[faces[i][2]];
            gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);
            vt = v[faces[i][3]];
            gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);
            
            if( type == GL.GL_LINE_STRIP ) {
                vt = v[faces[i][0]];
                gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);
            }

            gl.glEnd();
        }
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
