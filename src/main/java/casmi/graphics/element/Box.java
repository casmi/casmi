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

import casmi.graphics.Graphics;

/**
 * Box class. 
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 */
public class Box extends Element implements Renderable {

	private static final double STROKE_BIAS_RATIO = 1.00;

	private double width  = 1.0;
	private double height = 1.0;
	private double depth  = 1.0;

	private Texture[] textures = new Texture[6];

	/**
	 * Creates a new Box whose shape is square using size.
	 * 
	 * @param size
	 * 				The size of the Box.
	 */
	public Box(double size) {
		width  = size;
		depth  = size;
		height = size;
		this.setThreeD(true);
	}
	
	/**
	 * Creates a new Box using width, height and depth.
	 * 
     * @param width
     *              The width of the box.
     * @param height 
     *              The height of the box.
     * @param depth
     * 				The depth of the box. 
	 */
	public Box(double width, double height, double depth) {
	    this.width  = width;
	    this.height = height;
	    this.depth  = depth;
		this.setThreeD(true);
	}

	@Override
	public void render(GL2 gl, GLU glu, int width, int height) {
		if ((this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || this.isDepthTest()==false) && this.isThreeD() == false) {
			gl.glDisable(GL2.GL_DEPTH_TEST);
		}
		
		gl.glEnable(GL2.GL_DEPTH_TEST);

        gl.glPushMatrix();
        {
            this.setTweenParameter(gl);

            if (!this.enableTexture) {
                gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
                gl.glPolygonOffset(1f, 1f);
            }

            if (this.fill) {
                getSceneFillColor().setup(gl);
                gl.glLineWidth(this.strokeWidth);

                gl.glPushMatrix();
                {
                    drawBox(gl, this.width, this.height, this.depth, GL2.GL_QUADS);
                }
                gl.glPopMatrix();
            }

            if (this.stroke) {
                getSceneStrokeColor().setup(gl);
                gl.glLineWidth(this.strokeWidth);
                gl.glPushMatrix();
                {
                    if (!this.enableTexture) {
                        drawBox(gl, 
                                this.width  * STROKE_BIAS_RATIO,
                                this.height * STROKE_BIAS_RATIO,
                                this.depth  * STROKE_BIAS_RATIO,
                                GL2.GL_LINE_STRIP);
                    } else {
                        drawBox(gl, this.width, this.height, this.depth, GL2.GL_LINE_STRIP);
                    }
                }
                gl.glPopMatrix();
            }

            if (!this.enableTexture) {
                gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);
            }
        }
        gl.glPopMatrix();


		gl.glDisable(GL2.GL_DEPTH_TEST);
        
		if ((this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || this.isDepthTest()==false) && this.isThreeD() == false) {
            gl.glEnable(GL2.GL_DEPTH_TEST);
        }
    }

    private static float[][] boxVertices;

    private static final float[][] boxNormals = {
        { -1.0f,  0.0f,  0.0f },
        {  0.0f,  1.0f,  0.0f }, 
        {  1.0f,  0.0f,  0.0f },
        {  0.0f, -1.0f,  0.0f },
        {  0.0f,  0.0f,  1.0f },
        {  0.0f,  0.0f, -1.0f }
    };

    private static final int[][] boxFaces = { 
        { 0, 1, 2, 3 },
        { 3, 2, 6, 7 },
        { 7, 6, 5, 4 },
        { 4, 5, 1, 0 },
        { 5, 6, 2, 1 },
        { 7, 4, 0, 3 }
    };

    private final void drawBox(GL2 gl, double width, double height, double depth, int type) {
		if (boxVertices == null) {
			boxVertices = new float[8][3];
			boxVertices[0][0] = boxVertices[1][0] = boxVertices[2][0] = boxVertices[3][0] = -0.5f;
			boxVertices[4][0] = boxVertices[5][0] = boxVertices[6][0] = boxVertices[7][0] =  0.5f;
			boxVertices[0][1] = boxVertices[1][1] = boxVertices[4][1] = boxVertices[5][1] = -0.5f;
			boxVertices[2][1] = boxVertices[3][1] = boxVertices[6][1] = boxVertices[7][1] =  0.5f;
			boxVertices[0][2] = boxVertices[3][2] = boxVertices[4][2] = boxVertices[7][2] = -0.5f;
			boxVertices[1][2] = boxVertices[2][2] = boxVertices[5][2] = boxVertices[6][2] =  0.5f;
		}

		float[][] v = boxVertices;
		float[][] n = boxNormals;
		int[][] faces = boxFaces;

		for (int i = 5; 0 <= i; i--) {
			if (type == GL2.GL_QUADS) {
				if (this.enableTexture) {
				    if (textures[i].loadFlag) {
				        textures[i].getImage().loadTexture();
				        textures[i].loadFlag = false;
				    }
					if (textures[i].reloadFlag) {
//						Graphics.reloadTextures(gl);
					    textures[i].getImage().reloadTexture(gl);
						textures[i].reloadFlag = false;
					}
					textures[i].enableTexture();
				}
			}
			gl.glBegin(type);
			gl.glNormal3fv(n[i], 0);

			float[] vt = v[faces[i][0]];

			if (this.enableTexture) {
				gl.glTexCoord2f(textures[i].getTextureCorner(0, 0),
						        textures[i].getTextureCorner(0, 1));
			}
			gl.glVertex3d(vt[0] * width, vt[1] * height, vt[2] * depth);

			vt = v[faces[i][1]];
			if (this.enableTexture) {
				gl.glTexCoord2f(textures[i].getTextureCorner(1, 0),
                                textures[i].getTextureCorner(1, 1));
			}
			gl.glVertex3d(vt[0] * width, vt[1] * height, vt[2] * depth);

			vt = v[faces[i][2]];
			if (this.enableTexture) {
				gl.glTexCoord2f(textures[i].getTextureCorner(2, 0),
						textures[i].getTextureCorner(2, 1));
			}
			gl.glVertex3d(vt[0] * width, vt[1] * height, vt[2] * depth);

			vt = v[faces[i][3]];
			if (this.enableTexture) {
				gl.glTexCoord2f(textures[i].getTextureCorner(3, 0),
						        textures[i].getTextureCorner(3, 1));
			}
			gl.glVertex3d(vt[0] * width, vt[1] * height, vt[2] * depth);

			if (type == GL2.GL_LINE_STRIP) {
				vt = v[faces[i][0]];
				gl.glVertex3d(vt[0] * width, vt[1] * height, vt[2] * depth);
			}

			if (this.enableTexture && type == GL2.GL_QUADS) {
				textures[i].disableTexture();
			}
			gl.glEnd();
		}
	}

    /**
     * Gets the width of this Box.
     * 
     * @return
     * 			The width of the Box.
     */
	public double getWidth() {
		return width;
	}

	/**
     * Sets the width of this Box.
     * 
     * @param width
     * 				The width of the Box.
     */
	public void setWidth(double width) {
		this.width = width;
	}
	
	/**
     * Gets the height of this Box.
     * 
     * @return
     * 			The height of the Box.
     */
	public double getHeight() {
		return height;
	}

	/**
     * Sets the height of this Box.
     * 
     * @param height
     * 				The height of the Box.
     */
	public void setHeight(double height) {
		this.height = height;
	}
	
	/**
     * Gets the depth of this Box.
     * 
     * @return
     * 			The depth of the Box.
     */
	public double getDepth() {
	    return depth;
	}
	
	/**
     * Sets the depth of this Box.
     * 
     * @param depth
     * 			The depth of the Box.
     */
	public void setDepth(double depth) {
	    this.depth = depth;
	}

	/**
     * Sets the size of this Box whose shape is square.
     * 
     * @param size
     * 			The size of the Box whose shape is square .
     */
	public void setSize(double size) {
	    width  = size;
		height = size;
		depth  = size;
	}

	/**
     * Sets the texture of this Box.
     * 
     * @param index
     * 			The index of the surface of the Box.
     * @param texture
     * 			The texture of the Box.
     * 
     * @see casmi.graphics.element.Texture
     */
	public void setTexture(int index, Texture texture) {
		this.enableTexture = true;
		textures[index] = texture;
		textures[index].loadFlag = true;
	}
}