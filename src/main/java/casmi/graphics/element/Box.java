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

import casmi.graphics.Graphics;

/**
 * Box class. Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Box extends Element implements Renderable {

	private static final double STROKE_BIAS_RATIO = 1.01;

	private double width;
	private double height;

	private Texture[] textures = new Texture[6];

	public Box(double size) {
		this.width  = size;
		this.height = size;
	}

	public Box(double width, double height) {
		this.width  = width;
		this.height = height;
	}

	@Override
	public void render(GL gl, GLU glu, int width, int height) {
		if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001) {
			gl.glDisable(GL.GL_DEPTH_TEST);
		}

        gl.glPushMatrix();
        {
            this.setTweenParameter(gl);

            if (!this.enableTexture) {
                gl.glEnable(GL.GL_POLYGON_OFFSET_FILL);
                gl.glPolygonOffset(1f, 1f);
            }

            if (this.fill) {
                getSceneFillColor().setup(gl);
                gl.glLineWidth(this.strokeWidth);

                gl.glPushMatrix();
                {
                    gl.glScaled(1.0, this.height / this.width, 1.0);
                    drawBox(gl, (float)(this.width * 1.0), GL.GL_QUADS);
                }
                gl.glPopMatrix();
            }

            if (this.stroke) {
                getSceneStrokeColor().setup(gl);
                gl.glPushMatrix();
                {
                    gl.glScaled(1.0, this.height / this.width, 1.0);
                    if (!this.enableTexture) {
                        drawBox(gl, (float)(this.width * STROKE_BIAS_RATIO),
                            GL.GL_LINE_STRIP);
                    } else {
                        drawBox(gl, (float)(this.width), GL.GL_LINE_STRIP);
                    }
                }
                gl.glPopMatrix();
            }

            if (!this.enableTexture) {
                gl.glDisable(GL.GL_POLYGON_OFFSET_FILL);
            }
        }
        gl.glPopMatrix();

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001) {
            gl.glEnable(GL.GL_DEPTH_TEST);
        }
    }

    private static float[][] boxVertices;

    private static final float[][] boxNormals = {
        {-1.0f, 0.0f, 0.0f},
        {0.0f, 1.0f, 0.0f}, {1.0f, 0.0f, 0.0f}, {0.0f, -1.0f, 0.0f},
        {0.0f, 0.0f, 1.0f}, {0.0f, 0.0f, -1.0f}
    };

    private static final int[][] boxFaces = { 
        {0, 1, 2, 3}, {3, 2, 6, 7},
        {7, 6, 5, 4}, {4, 5, 1, 0}, {5, 6, 2, 1}, {7, 4, 0, 3}
    };

    private final void drawBox(GL gl, float size, int type) {
		if (boxVertices == null) {
			float[][] v = new float[8][];

			for (int i = 0; i < 8; i++) {
				v[i] = new float[3];
			}

			v[0][0] = v[1][0] = v[2][0] = v[3][0] = -0.5f;
			v[4][0] = v[5][0] = v[6][0] = v[7][0] =  0.5f;
			v[0][1] = v[1][1] = v[4][1] = v[5][1] = -0.5f;
			v[2][1] = v[3][1] = v[6][1] = v[7][1] =  0.5f;
			v[0][2] = v[3][2] = v[4][2] = v[7][2] = -0.5f;
			v[1][2] = v[2][2] = v[5][2] = v[6][2] =  0.5f;

			boxVertices = v;
		}

		float[][] v = boxVertices;
		float[][] n = boxNormals;
		int[][] faces = boxFaces;

		for (int i = 5; i >= 0; i--) {
			if (type == GL.GL_QUADS) {
				if (this.enableTexture) {
					if (textures[i].reloadFlag) {
						Graphics.reloadTextures();
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
			gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);

			vt = v[faces[i][1]];
			if (this.enableTexture) {
				gl.glTexCoord2f(textures[i].getTextureCorner(1, 0),
						textures[i].getTextureCorner(1, 1));
			}
			gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);

			vt = v[faces[i][2]];
			if (this.enableTexture) {
				gl.glTexCoord2f(textures[i].getTextureCorner(2, 0),
						textures[i].getTextureCorner(2, 1));
			}
			gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);

			vt = v[faces[i][3]];
			if (this.enableTexture) {
				gl.glTexCoord2f(textures[i].getTextureCorner(3, 0),
						textures[i].getTextureCorner(3, 1));
			}
			gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);

			if (type == GL.GL_LINE_STRIP) {
				vt = v[faces[i][0]];
				gl.glVertex3f(vt[0] * size, vt[1] * size, vt[2] * size);
			}

			if (this.enableTexture && type == GL.GL_QUADS) {
				textures[i].disableTexture();
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

	public void setSize(double size) {
		this.height = size;
		this.width  = size;
	}

	public void setTexture(int index, Texture texture) {
		this.enableTexture = true;
		textures[index] = texture;
	}
}
