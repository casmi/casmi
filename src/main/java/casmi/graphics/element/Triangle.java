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
 * Triangle class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Triangle extends Element {

    public static final int TRIANGLE = 0;
    public static final int TRIANGLE_3D = 4;

    private double x1;
    private double y1;
    private double z1;
    private double x2;
    private double y2;
    private double z2;
    private double x3;
    private double y3;
    private double z3;
    private int MODE;

    private Color cornerColor[] = new Color[3];

    private Texture texture;

    /**
     * Creates a new Triangle object using x,y-coordinate of corners.
     *
     * @param x1 The x-coordinate of the first corner.
     * @param y1 The y-coordinate of the first corner.
     * @param x2 The x-coordinate of the second corner.
     * @param y2 The y-coordinate of the second corner.
     * @param x3 The x-coordinate of the third corner.
     * @param y3 The y-coordinate of the third corner.
     */
    public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        this.MODE = TRIANGLE;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        calcG();
    }

    /**
     * Creates a new Triangle object using x,y-coordinate of corners.
     *
     * @param v1 The coordinates of the first corner.
     * @param v2 The coordinates of the second corner.
     * @param v3 The coordinates of the third corner.
     */
    public Triangle(Vector3D v1, Vector3D v2, Vector3D v3) {
        this.MODE = TRIANGLE_3D;
        this.x1 = v1.getX();
        this.y1 = v1.getY();
        this.z1 = v1.getZ();
        this.x2 = v2.getX();
        this.y2 = v2.getY();
        this.z1 = v2.getZ();
        this.x3 = v3.getX();
        this.y3 = v3.getY();
        this.z1 = v3.getZ();
        calcG();
    }

    /**
     * Creates a new Triangle object using x,y-coordinate of corners.
     *
     * @param x1 The x-coordinate of the first corner.
     * @param y1 The y-coordinate of the first corner.
     * @param z1 The z-coordinate of the first corner.
     * @param x2 The x-coordinate of the second corner.
     * @param y2 The y-coordinate of the second corner.
     * @param z2 The z-coordinate of the second corner.
     * @param x3 The x-coordinate of the third corner.
     * @param y3 The y-coordinate of the third corner.
     * @param z3 The z-coordinate of the third corner.
     */
    public Triangle(double x1, double y1, double z1, double x2, double y2, double z2, double x3,
        double y3, double z3) {
        this.MODE = TRIANGLE_3D;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.x3 = x3;
        this.y3 = y3;
        this.z3 = z3;
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
     */
    public void set(double x1, double y1, double x2, double y2, double x3, double y3) {
        this.MODE = TRIANGLE;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        calcG();
    }

    /**
     * Sets x,y-coordinate of corners.
     *
     * @param v1 The coordinates of the first corner.
     * @param v2 The coordinates of the second corner.
     * @param v3 The coordinates of the third corner.
     */
    public void set(Vector3D v1, Vector3D v2, Vector3D v3) {
        this.MODE = TRIANGLE_3D;
        this.x1 = v1.getX();
        this.y1 = v1.getY();
        this.z1 = v1.getZ();
        this.x2 = v2.getX();
        this.y2 = v2.getY();
        this.z1 = v2.getZ();
        this.x3 = v3.getX();
        this.y3 = v3.getY();
        this.z1 = v3.getZ();
        calcG();
    }

    /**
     * Sets x,y-coordinate of corners.
     *
     * @param x1 The x-coordinate of the first corner.
     * @param y1 The y-coordinate of the first corner.
     * @param z1 The z-coordinate of the first corner.
     * @param x2 The x-coordinate of the second corner.
     * @param y2 The y-coordinate of the second corner.
     * @param z2 The z-coordinate of the second corner.
     * @param x3 The x-coordinate of the third corner.
     * @param y3 The y-coordinate of the third corner.
     * @param z3 The z-coordinate of the third corner.
     */
    public void set(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
        this.MODE = TRIANGLE_3D;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.x3 = x3;
        this.y3 = y3;
        this.z3 = z3;
        calcG();
    }

    public void setCorner(int number, double x, double y) {
        if (number <= 0) {
            this.x1 = x;
            this.y1 = y;
        } else if (number == 1) {
            this.x2 = x;
            this.y2 = y;
        } else if (number >= 2) {
            this.x3 = x;
            this.y3 = y;
        }
        calcG();
    }

    public void setCorner(int number, double x, double y, double z) {
        if (number <= 0) {
            this.x1 = x;
            this.y1 = y;
            this.z1 = z;
        } else if (number == 1) {
            this.x2 = x;
            this.y2 = y;
            this.z2 = z;
        } else if (number >= 2) {
            this.x3 = x;
            this.y3 = y;
            this.z3 = z;
        }
        calcG();
    }

    public void setConer(int number, Vector3D v) {
        if (number <= 0) {
            this.x1 = v.getX();
            this.y1 = v.getY();
            this.z1 = v.getZ();
        } else if (number == 1) {
            this.x2 = v.getX();
            this.y2 = v.getY();
            this.z2 = v.getZ();
        } else if (2 <= number) {
            this.x3 = v.getX();
            this.y3 = v.getY();
            this.z3 = v.getZ();
        }
        calcG();
    }

    public Vector3D getConer(int number) {
        Vector3D v = new Vector3D(0, 0, 0);

        if (number <= 0) {
            v.set(this.x1, this.y1, this.z1);
        } else if (number == 1) {
            v.set(this.x2, this.y2, this.z2);
        } else if (number >= 2) {
            v.set(this.x3, this.y3, this.z3);
        }
        return v;
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || !this.isDepthTest()) {
            gl.glDisable(GL2.GL_DEPTH_TEST);
        }

        if (this.enableTexture && this.texture != null) {
            texture.render(gl);
            texture.enableTexture(gl);
        }

        gl.glPushMatrix();
        {
            this.move(gl);
            switch (MODE) {
            case TRIANGLE:
                if (this.fill) {
                    getSceneFillColor().setup(gl);
                    gl.glBegin(GL2.GL_TRIANGLE_FAN);
                    if (isGradation()) getSceneColor(cornerColor[0]).setup(gl);
                    if (this.enableTexture)
                        gl.glTexCoord2f(texture.getTextureCorner(0, 0), texture.getTextureCorner(0, 1));
                    gl.glVertex2d(x1 - x, y1 - y);
                    if (isGradation()) getSceneColor(cornerColor[1]).setup(gl);
                    if (this.enableTexture)
                        gl.glTexCoord2f(texture.getTextureCorner(1, 0), texture.getTextureCorner(1, 1));
                    gl.glVertex2d(x2 - x, y2 - y);
                    if (isGradation()) getSceneColor(cornerColor[2]).setup(gl);
                    if (this.enableTexture)
                        gl.glTexCoord2f(texture.getTextureCorner(2, 0), texture.getTextureCorner(2, 1));
                    gl.glVertex2d(x3 - x, y3 - y);
                    gl.glEnd();
                }

                if (this.stroke) {
                    gl.glLineWidth(this.strokeWidth);
                    getSceneStrokeColor().setup(gl);
                    gl.glBegin(GL2.GL_LINES);
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
                    gl.glVertex2d(x1 - x, y1 - y);
                    if (isGradation()) getSceneColor(cornerColor[0]).setup(gl);
                    gl.glVertex2d(x3 - x, y3 - y);
                    gl.glEnd();
                }
                break;

            case TRIANGLE_3D:
                if (this.fill) {
                    this.fillColor.setup(gl);
                    gl.glBegin(GL2.GL_TRIANGLE_FAN);
                    if (isGradation()) getSceneColor(cornerColor[0]).setup(gl);
                    gl.glVertex3d(x1 - x, y1 - y, z1);
                    if (isGradation()) getSceneColor(cornerColor[1]).setup(gl);
                    gl.glVertex3d(x2 - x, y2 - y, z2);
                    if (isGradation()) getSceneColor(cornerColor[2]).setup(gl);
                    gl.glVertex3d(x3 - x, y3 - y, z3);
                    gl.glEnd();
                }

                if (this.stroke) {
                    gl.glLineWidth(this.strokeWidth);
                    this.strokeColor.setup(gl);
                    gl.glBegin(GL2.GL_LINES);
                    if (isGradation()) getSceneColor(cornerColor[0]).setup(gl);
                    gl.glVertex3d(x1 - x, y1 - y, z1);
                    if (isGradation()) getSceneColor(cornerColor[1]).setup(gl);
                    gl.glVertex3d(x2 - x, y2 - y, z2);
                    gl.glEnd();
                    gl.glBegin(GL2.GL_LINES);
                    gl.glVertex3d(x2 - x, y2 - y, z2);
                    if (isGradation()) getSceneColor(cornerColor[2]).setup(gl);
                    gl.glVertex3d(x3 - x, y3 - y, z3);
                    gl.glEnd();
                    gl.glBegin(GL2.GL_LINES);
                    gl.glVertex3d(x1 - x, y1 - y, z1);
                    if (isGradation()) getSceneColor(cornerColor[0]).setup(gl);
                    gl.glVertex3d(x3 - x, y3 - y, z3);
                    gl.glEnd();
                }
                break;
            default:
                // do nothing
                break;
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

    private final void calcG() {
        x = (x1 + x2 + x3) / 3.0;
        y = (y1 + y2 + y3) / 3.0;
        z = (z1 + z2 + z3) / 3.0;
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
        calcG();
    }

    /**
     * Sets the color of the corner for gradation.
     *
     * @param index The index of the corner.
     * @param color The color of the corner.
     */
    public void setCornerColor(int index, Color color) {
        if (!isGradation()) {
            for (int i = 0; i < 3; i++) {
                cornerColor[i] = new RGBColor(0.0, 0.0, 0.0);
                cornerColor[i] = this.fillColor;
            }
            setGradation(true);
        }
        cornerColor[index] = color;
    }

    /**
     * Sets the color of the corner for gradation.
     *
     * @param index The index of the corner.
     * @param colorSet The colorSet of the corner.
     */
    public void setCornerColor(int index, ColorSet colorSet) {
        setCornerColor(index, new RGBColor(colorSet));
    }

    @Override
    public void reset(GL2 gl) {
        if (this.enableTexture && this.texture != null) {
            texture.reload();
        }
    }
}
