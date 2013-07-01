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

import casmi.graphics.object.Renderable;
import casmi.graphics.object.Resettable;
import casmi.image.Texture;

/**
 * Sphere class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Sphere extends Element implements Renderable, Resettable {

    private double r;

    private int slices = 30;
    private int stacks = 30;

    private Texture texture;

    /**
     * Creates a new Sphere object using radius.
     *
     * @param radius The radius of the Sphere.
     */
    public Sphere(double radius) {
        this.r = radius;
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
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        if (this.enableTexture && this.texture != null) {
            this.texture.render(gl);
            this.texture.enableTexture(gl);
        }

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest()) {
            gl.glDisable(GL2.GL_DEPTH_TEST);
        }

        gl.glPushMatrix();
        {
            gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
            gl.glPolygonOffset(1f, 1f);
            this.move(gl);
            if (this.ismaterial) material.setup(gl);

            if (this.fill) {
                getSceneFillColor().setup(gl);
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

        if (this.enableTexture && this.texture != null) {
            this.texture.disableTexture(gl);
        }

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest()) {
            gl.glEnable(GL2.GL_DEPTH_TEST);
        }
    }

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
	    if(this.enableTexture && this.texture != null) {
	        this.texture.reload();
	    }
	}

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        enableTexture();
    }
}
