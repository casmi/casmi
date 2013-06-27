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
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * Sphere class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Sphere extends Element implements Renderable, Reset {

    private double r;

    private int slices = 30;
    private int stacks = 30;

    /**
     * Creates a new Sphere object using radius.
     *
     * @param radius The radius of the Sphere.
     */
    public Sphere(double radius) {
        this.r = radius;
//        this.setThreeD(true);
    }

    /**
     * Creates a new Sphere object using radius, slices and stacks.
     *
     * @param radius The radius of the Sphere.
     * @param slices The sliced division number.
     * @param stacks The stacks division number.
     */
    public Sphere(double radius, int slices, int stacks) {
        this.r = radius;
        this.slices = slices;
        this.stacks = stacks;
//        this.setThreeD(true);
    }

    /**
     * Sets radius of this Sphere.
     *
     * @param radius The radius of the Sphere.
     */
    public final void setRadius(double radius) {
        this.r = radius;
    }

    /**
     * Sets the division number.
     *
     * @param slices The sliced division number.
     * @param stacks The stacks division number.
     */
    public final void setDetail(int slices, int stacks) {
        this.slices = slices;
        this.stacks = stacks;
    }

    /**
     * Sets the sliced division number.
     *
     * @param slices The sliced division number.
     */
    public final void setSlices(int slices) {
        this.slices = slices;
    }

    /**
     * Sets the stacked division number.
     *
     * @param stacks The stacks division number.
     */
    public final void setStacks(int stacks) {
        this.stacks = stacks;
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height) {
//        if ((this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest())
//            && !this.isThreeD()) {
//            gl.glDisable(GL2.GL_DEPTH_TEST);
//        }

//        if (this.enableBump && this.normalMap!=null){
//            gl.glEnable(GL2.GL_NORMALIZE);
//            gl.glActiveTexture(GL2.GL_TEXTURE0);
//            this.shader.enableShader(gl);
//            gl.glEnable(GL2.GL_DEPTH_TEST);
//            this.shader.setUniform("normalMap", 0);
//            normalMap.enableTexture(gl);
//            gl.glActiveTexture(GL2.GL_TEXTURE1);
//            this.shader.setUniform("colorMap", 1);
//        }else if (this.enableShader){
//            gl.glActiveTexture(GL2.GL_TEXTURE0);
//            this.shader.enableShader(gl);
//            gl.glEnable(GL2.GL_DEPTH_TEST);
//            this.shader.setUniform("sampler", 0);
//        }

        if (this.enableTexture) {
            texture.enableTexture(gl);
        }

        gl.glPushMatrix();
        {
            gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
            gl.glPolygonOffset(1f, 1f);
            this.move(gl);
            if (this.ismaterial) material.setup(gl);

            if (this.fill) {
                getSceneFillColor().setup(gl);
//                if(this.enableBump)
//                    drawBumpSphere(gl, (float)r, slices, stacks);
//                else
                drawSolidSphere(glu, (float)r, slices, stacks);
            }

            if (this.stroke) {
                getSceneStrokeColor().setup(gl);
                drawWireSphere(glu, (float)r, slices, stacks);
            }
        }
        gl.glPopMatrix();
        gl.glDisable(GL2.GL_CULL_FACE);
        gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);

        if (this.enableTexture) {
            texture.disableTexture(gl);
        }

//        if ((this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest())
//            && !this.isThreeD()) {
//            gl.glEnable(GL2.GL_DEPTH_TEST);
//        }
//
//        if (this.enableShader)
//            this.shader.disableShader(gl);
    }

//    private final void drawBumpSphere(GL2 gl, float radius, int slices, int stacks) {
//        int i, j;
//        double s, t0, t1, r0, r1, th0, th1, phi;
//        double[] tnt = new double[3];
//        double[] p = new double[3];
//        double[] pp = new double[3];
//        this.shader.enableShader();
//        this.shader.setAttribLocation("tangent");
//        this.shader.setUniform("inv", -1.0f);
//        for(j = 0; j < stacks; j++) {
//            t0 = (double)j / (double)stacks;
//            t1 = (double)(j+1) / (double)stacks;
//            th0 = Math.PI * t0;
//            th1 = Math.PI * t1;
//            r0 = radius * Math.sin(th0);
//            r1 = radius * Math.sin(th1);
//            p[2] = radius * Math.cos(th0);
//            pp[2] = radius * Math.cos(th1);
//            tnt[2] = 0.0;
//
//            //t0 = 1.0 - t0;
//            //t1 = 1.0 - t1;
//
//            gl.glBegin(GL2.GL_QUAD_STRIP);
//            for(i = 0; i <= slices; i++) {
//                s = (double)i / (double)slices;
//                phi = -Math.PI + 2.0 * Math.PI * s;
//                p[0] = r0 * Math.cos(phi);
//                p[1] = r0 * Math.sin(phi);
//                pp[0] = r1 * Math.cos(phi);
//                pp[1] = r1 * Math.sin(phi);
//
//                tnt[0] = -Math.sin(phi);
//                tnt[1] = Math.cos(phi);
//                this.shader.setVertexAttrib3("tangent", tnt);
//                gl.glTexCoord2d(s, t0);
//                gl.glNormal3dv(p, 0);
//                gl.glVertex3dv(p, 0);
//
//                gl.glTexCoord2d(s, t1);
//                gl.glNormal3dv(pp, 0);
//                gl.glVertex3dv(pp, 0);
//            }
//
//            gl.glEnd();
//        }
//
//    }

    private GLUquadric quadObj;

    private final void quadObjInit(GLU glu) {
        if (quadObj == null) {
            quadObj = glu.gluNewQuadric();
        }
        if (quadObj == null) {
            throw new GLException("Out of memory");
        }
    }


    private final void drawSolidSphere(GLU glu, float radius, int slices, int stacks) {
        quadObjInit(glu);
        glu.gluQuadricDrawStyle(quadObj, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadObj, GLU.GLU_SMOOTH);
        glu.gluQuadricTexture(quadObj, true);
        glu.gluSphere(quadObj, radius, slices, stacks);
    }


    private final void drawWireSphere(GLU glu, float radius, int slices, int stacks) {
        quadObjInit(glu);
        glu.gluQuadricDrawStyle(quadObj, GLU.GLU_LINE);
        glu.gluQuadricNormals(quadObj, GLU.GLU_SMOOTH);
        glu.gluSphere(quadObj, radius, slices, stacks);
    }

	@Override
	public void reset(GL2 gl) {
	    if(this.enableTexture) {
	        if(texture.isInit()){
	            texture.loadImage();
	            texture.setInit(false);
	        }else{
	            texture.reloadImage(gl);
	        }
	    }
//		if(this.enableBump)
//		    if(normalMap.isInit()){
//		        normalMap.loadImage();
//		        normalMap.setInit(false);
//		    }else{
//		        normalMap.reloadImage(gl);
//		    }
//		if(this.enableShader){
//		    shader.initShaders(gl);
//		}
	}
}
