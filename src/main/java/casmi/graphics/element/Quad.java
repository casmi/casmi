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
import casmi.image.Texture;
import casmi.matrix.Vector3D;

/**
 * Quad class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Quad extends Element {

    private double x1;
    private double y1;
    private double z1;
    private double x2;
    private double y2;
    private double z2;
    private double x3;
    private double y3;
    private double z3;
    private double x4;
    private double y4;
    private double z4;

    private Color[] cornerColor = new Color[4];

    private Texture texture;

    /**
     * Creates a new Quad object using x,y-coordinate of corners.
     *
     * @param x1 The x-coordinate of the first corner.
     * @param y1 The y-coordinate of the first corner.
     * @param x2 The x-coordinate of the second corner.
     * @param y2 The y-coordinate of the second corner.
     * @param x3 The x-coordinate of the third corner.
     * @param y3 The y-coordinate of the third corner.
     * @param x4 The x-coordinate of the fourth corner.
     * @param y4 The y-coordinate of the fourth corner.
     */
    public Quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4,
        double y4) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
        calcG();
    }

    /**
     * Creates a new Quad object using x,y-coordinate of corners.
     *
     * @param v1 The coordinates of the first corner.
     * @param v2 The coordinates of the second corner.
     * @param v3 The coordinates of the third corner.
     * @param v4 The coordinates of the fourth corner.
     */
    public Quad(Vector3D v1, Vector3D v2, Vector3D v3, Vector3D v4) {
        this.x1 = (float)v1.getX();
        this.y1 = (float)v1.getY();
        this.x2 = (float)v2.getX();
        this.y2 = (float)v2.getY();
        this.x3 = (float)v3.getX();
        this.y3 = (float)v3.getY();
        this.x4 = (float)v4.getX();
        this.y4 = (float)v4.getY();
        calcG();
    }

    /**
     * Sets x,y-coordinate of corners.
     *
     * @param x1 The x-coordinate of the first corner.
     * @param y1 The y-coordinate of the first corner.
     * @param x2 The x-coordinate of the second corner.
     * @param y2 The y-coordinate of the second corner.
     * @param x3 The x-coordinate of the third corner.
     * @param y3 The y-coordinate of the third corner.
     * @param x4 The x-coordinate of the fourth corner.
     * @param y4 The y-coordinate of the fourth corner.
     */
    public void set(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = x3;
        this.x4 = x4;
        this.y4 = y4;
        calcG();
    }

    /**
     * Sets x,y-coordinate of corners.
     *
     * @param v1 The coordinates of the first corner.
     * @param v2 The coordinates of the second corner.
     * @param v3 The coordinates of the third corner.
     * @param v4 The coordinates of the fourth corner.
     */
    public void set(Vector3D v1, Vector3D v2, Vector3D v3, Vector3D v4) {
        this.x1 = (float)v1.getX();
        this.y1 = (float)v1.getY();
        this.x2 = (float)v2.getX();
        this.y2 = (float)v2.getY();
        this.x3 = (float)v3.getX();
        this.y3 = (float)v3.getY();
        this.x4 = (float)v4.getX();
        this.y4 = (float)v4.getY();
        calcG();
    }

    /**
     * Sets x,y-coordinate of a corner.
     *
     * @param index The index of a corner.
     * @param x The x-coordinate of a corner.
     * @param y The y-coordinate of a corner.
     */
    public void setCorner(int index, double x, double y) {
        if (index <= 0) {
            this.x1 = x;
            this.y1 = y;
        } else if (index == 1) {
            this.x2 = x;
            this.y2 = y;
        } else if (index == 2) {
            this.x3 = x;
            this.y3 = y;
        } else if (index >= 3) {
            this.x4 = x;
            this.y4 = y;
        }
        calcG();
    }

    /**
     * Sets x,y,z-coordinate of a corner.
     *
     * @param index The index of a corner.
     * @param x The x-coordinate of a corner.
     * @param y The y-coordinate of a corner.
     * @param z The z-coordinate of a corner.
     */
    public void setCorner(int index, double x, double y, double z) {
        if (index <= 0) {
            this.x1 = x;
            this.y1 = y;
        } else if (index == 1) {
            this.x2 = x;
            this.y2 = y;
        } else if (index == 2) {
            this.x3 = x;
            this.y3 = y;
        } else if (index >= 3) {
            this.x4 = x;
            this.y4 = y;
        }
        calcG();
    }

    /**
     * Sets coordinates of a corner.
     *
     * @param index The index of a corner.
     * @param v The coordinates of a corner.
     */
    public void setConer(int index, Vector3D v) {
        if (index <= 0) {
            this.x1 = v.getX();
            this.y1 = v.getY();
        } else if (index == 1) {
            this.x2 = v.getX();
            this.y2 = v.getY();
        } else if (index == 2) {
            this.x3 = v.getX();
            this.y3 = v.getY();
        } else if (3 <= index) {
            this.x4 = v.getX();
            this.y4 = v.getY();
        }
        calcG();
    }

    /**
     * Gets coordinates of a corner.
     *
     * @param index The index of a corner.
     */
    public Vector3D getConer(int index) {
        Vector3D v = new Vector3D(0, 0, 0);

        if (index <= 0) {
            v.set(this.x1, this.y1);
        } else if (index == 1) {
            v.set(this.x2, this.y2);
        } else if (index == 2) {
            v.set(this.x3, this.y3);
        } else if (3 <= index) {
            v.set(this.x4, this.y4);
        }
        return v;
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest()) {
            gl.glDisable(GL2.GL_DEPTH_TEST);
        }

        if (this.enableTexture && this.texture != null) {
            this.texture.render(gl);
            this.texture.enableTexture(gl);
        }

        gl.glPushMatrix();
        {
            this.move(gl);
            if (this.fill) {
                getSceneFillColor().setup(gl);
                gl.glBegin(GL2.GL_QUADS);
                {
                    if (isGradation()) getSceneColor(cornerColor[0]).setup(gl);
                    if (this.enableTexture)
                        gl.glTexCoord2f(getTexture().getTextureCorner(0, 0), getTexture().getTextureCorner(0, 1));
                    gl.glVertex2d(x1 - x, y1 - y);
                    if (isGradation()) getSceneColor(cornerColor[1]).setup(gl);
                    if (this.enableTexture)
                        gl.glTexCoord2f(getTexture().getTextureCorner(1, 0), getTexture().getTextureCorner(1, 1));
                    gl.glVertex2d(x2 - x, y2 - y);
                    if (isGradation()) getSceneColor(cornerColor[2]).setup(gl);
                    if (this.enableTexture)
                        gl.glTexCoord2f(getTexture().getTextureCorner(2, 0), getTexture().getTextureCorner(2, 1));
                    gl.glVertex2d(x3 - x, y3 - y);
                    if (isGradation()) getSceneColor(cornerColor[3]).setup(gl);
                    if (this.enableTexture)
                        gl.glTexCoord2f(getTexture().getTextureCorner(3, 0), getTexture().getTextureCorner(3, 1));
                    gl.glVertex2d(x4 - x, y4 - y);
                }
                gl.glEnd();
            }

            if (this.stroke) {
                gl.glLineWidth(this.strokeWidth);
                getSceneStrokeColor().setup(gl);
                gl.glBegin(GL2.GL_LINES);
                {
                    if (isGradation()) getSceneColor(cornerColor[0]).setup(gl);
                    gl.glVertex2d(x1 - x, y1 - y);
                    if (isGradation()) getSceneColor(cornerColor[1]).setup(gl);
                    gl.glVertex2d(x2 - x, y2 - y);
                    gl.glEnd();
                    gl.glBegin(GL2.GL_LINES);
                    gl.glVertex2d(x2 - x, y2 - y);
                    if (isGradation()) getSceneColor(cornerColor[2]).setup(gl);
                    gl.glVertex2d(x3 - x, y3 - y);
                    gl.glEnd();
                    gl.glBegin(GL2.GL_LINES);
                    gl.glVertex2d(x3 - x, y3 - y);
                    if (isGradation()) getSceneColor(cornerColor[3]).setup(gl);
                    gl.glVertex2d(x4 - x, y4 - y);
                    gl.glEnd();
                    gl.glBegin(GL2.GL_LINES);
                    gl.glVertex2d(x4 - x, y4 - y);
                    if (isGradation()) getSceneColor(cornerColor[0]).setup(gl);
                    gl.glVertex2d(x1 - x, y1 - y);
                }
                gl.glEnd();
            }
        }
        gl.glPopMatrix();

        if (this.enableTexture && this.texture != null) {
            texture.disableTexture(gl);
        }

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest()) {
            gl.glEnable(GL2.GL_DEPTH_TEST);
        }
    }

    private void calcG() {
        this.x = (x1 + x2 + x3 + x4) / 4.0;
        this.y = (y1 + y2 + y3 + y4) / 4.0;
    }

    @Override
    public void setPosition(double x, double y) {
        setPosition(x, y, this.z);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        x1 = x1 + x - this.x;
        y1 = y1 + y - this.y;
        z1 = z1 + z - this.z;
        x2 = x2 + x - this.x;
        y2 = y2 + y - this.y;
        z2 = z2 + z - this.z;
        x3 = x3 + x - this.x;
        y3 = y3 + y - this.y;
        z3 = z3 + z - this.z;
        x4 = x4 + x - this.x;
        y4 = y4 + y - this.y;
        z4 = z4 + z - this.z;
        calcG();
    }

    /**
     * Sets the color of a corner for gradation.
     *
     * @param index The index of a corner.
     * @param color The color of a corner.
     */
    public void setCornerColor(int index, Color color) {
        if (!isGradation()) {
            for (int i = 0; i < 4; i++) {
                cornerColor[i] = new RGBColor(0.0, 0.0, 0.0);
                cornerColor[i] = this.fillColor;
            }
            setGradation(true);
        }
        cornerColor[index] = color;
    }

    /**
     * Sets the color of a corner for gradation.
     *
     * @param index The index of a corner.
     * @param colorSet The colorSet of a corner.
     */
    public void setCornerColor(int index, ColorSet colorSet) {
        setCornerColor(index, new RGBColor(colorSet));
    }

    @Override
    public void reset(GL2 gl) {
        if (this.enableTexture && this.texture != null) {
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
