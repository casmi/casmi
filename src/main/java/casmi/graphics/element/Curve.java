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
import casmi.matrix.Vector3D;

/**
 * Curve class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Curve extends Element {

    private float[] points = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private int detail = 30;

    private Color startColor;
    private Color endColor;
    private Color gradationColor = new RGBColor(0.0, 0.0, 0.0);

    public enum XYZ {
        X, Y, Z
    };

    /**
     * Creates a new Curve object using coordinates for the anchor and control points.
     *
     * @param x1 The coordinate x for the first anchor point.
     * @param y1 The coordinate y for the first anchor point.
     * @param x2 The coordinate x for the first control point.
     * @param y2 The coordinate y for the first control point.
     * @param x3 The coordinate x for the second control point.
     * @param y3 The coordinate y for the second control point.
     * @param x4 The coordinate x for the second anchor point.
     * @param y4 The coordinate y for the second anchor point.
     */
    public Curve(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
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
     * Creates a new Curve object using coordinates for the anchor and control points.
     *
     * @param x1 The coordinate x for the first anchor point.
     * @param y1 The coordinate y for the first anchor point.
     * @param x2 The coordinate x for the first control point.
     * @param y2 The coordinate y for the first control point.
     * @param x3 The coordinate x for the second control point.
     * @param y3 The coordinate y for the second control point.
     * @param x4 The coordinate x for the second anchor point.
     * @param y4 The coordinate y for the second anchor point.
     */
    public Curve(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
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
     * Creates a new Curve object using coordinates for the anchor and control points.
     *
     * @param x1 The coordinate x for the first anchor point.
     * @param y1 The coordinate y for the first anchor point.
     * @param z1 The coordinate z for the first anchor point.
     * @param x2 The coordinate x for the first control point.
     * @param y2 The coordinate y for the first control point.
     * @param z2 The coordinate z for the first control point.
     * @param x3 The coordinate x for the second control point.
     * @param y3 The coordinate y for the second control point.
     * @param z3 The coordinate z for the second control point.
     * @param x4 The coordinate x for the second anchor point.
     * @param y4 The coordinate y for the second anchor point.
     * @param z4 The coordinate z for the second anchor point.
     */
    public Curve(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3,
        float z3, float x4, float y4, float z4) {
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
     * Creates a new Curve object using coordinates for the anchor and control points.
     *
     * @param x1 The coordinate x for the first anchor point.
     * @param y1 The coordinate y for the first anchor point.
     * @param z1 The coordinate z for the first anchor point.
     * @param x2 The coordinate x for the first control point.
     * @param y2 The coordinate y for the first control point.
     * @param z2 The coordinate z for the first control point.
     * @param x3 The coordinate x for the second control point.
     * @param y3 The coordinate y for the second control point.
     * @param z3 The coordinate z for the second control point.
     * @param x4 The coordinate x for the second anchor point.
     * @param y4 The coordinate y for the second anchor point.
     * @param z4 The coordinate z for the second anchor point.
     */
    public Curve(double x1, double y1, double z1, double x2, double y2, double z2, double x3,
        double y3, double z3, double x4, double y4, double z4) {
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
     * Creates a new Curve object using coordinates for the anchor and control points.
     *
     * @param v1 The coordinates for the first anchor point.
     * @param v2 The coordinates for the first control point.
     * @param v3 The coordinates for the second control point.
     * @param v4 The coordinates for the second ancor point.
     */
    public Curve(Vector3D v1, Vector3D v2, Vector3D v3, Vector3D v4) {
        this.points[0] = (float)v1.getX();
        this.points[1] = (float)v1.getY();
        this.points[2] = (float)v1.getZ();
        this.points[3] = (float)v2.getX();
        this.points[4] = (float)v2.getY();
        this.points[5] = (float)v2.getZ();
        this.points[6] = (float)v3.getX();
        this.points[7] = (float)v3.getY();
        this.points[8] = (float)v3.getZ();
        this.points[9] = (float)v4.getX();
        this.points[10] = (float)v4.getY();
        this.points[11] = (float)v4.getZ();
        set();
    }

    /**
     * Sets coordinates for the anchor and control points.
     *
     * @param x1 The coordinate x for the first anchor point.
     * @param y1 The coordinate y for the first anchor point.
     * @param x2 The coordinate x for the first control point.
     * @param y2 The coordinate y for the first control point.
     * @param x3 The coordinate x for the second control point.
     * @param y3 The coordinate y for the second control point.
     * @param x4 The coordinate x for the second anchor point.
     * @param y4 The coordinate y for the second anchor point.
     */
    public void set(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
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
     * Sets coordinates for the anchor and control points.
     *
     * @param x1 The coordinate x for the first anchor point.
     * @param y1 The coordinate y for the first anchor point.
     * @param x2 The coordinate x for the first control point.
     * @param y2 The coordinate y for the first control point.
     * @param x3 The coordinate x for the second control point.
     * @param y3 The coordinate y for the second control point.
     * @param x4 The coordinate x for the second anchor point.
     * @param y4 The coordinate y for the second anchor point.
     */
    public void set(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
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
     * Sets coordinates for the anchor and control points.
     *
     * @param x1 The coordinate x for the first anchor point.
     * @param y1 The coordinate y for the first anchor point.
     * @param z1 The coordinate z for the first anchor point.
     * @param x2 The coordinate x for the first control point.
     * @param y2 The coordinate y for the first control point.
     * @param z2 The coordinate z for the first control point.
     * @param x3 The coordinate x for the second control point.
     * @param y3 The coordinate y for the second control point.
     * @param z3 The coordinate z for the second control point.
     * @param x4 The coordinate x for the second anchor point.
     * @param y4 The coordinate y for the second anchor point.
     * @param z4 The coordinate z for the second anchor point.
     */
    public void set(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
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
     * Sets coordinates for the anchor and control points.
     *
     * @param x1 The coordinate x for the first anchor point.
     * @param y1 The coordinate y for the first anchor point.
     * @param z1 The coordinate z for the first anchor point.
     * @param x2 The coordinate x for the first control point.
     * @param y2 The coordinate y for the first control point.
     * @param z2 The coordinate z for the first control point.
     * @param x3 The coordinate x for the second control point.
     * @param y3 The coordinate y for the second control point.
     * @param z3 The coordinate z for the second control point.
     * @param x4 The coordinate x for the second anchor point.
     * @param y4 The coordinate y for the second anchor point.
     * @param z4 The coordinate z for the second anchor point.
     */
    public void set(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
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
     * Sets coordinates for the anchor and control points.
     *
     * @param v1 The coordinates for the first anchor point.
     * @param v2 The coordinates for the first control point.
     * @param v3 The coordinates for the second control point.
     * @param v4 The coordinates for the second ancor point.
     */
    public void set(Vector3D v1, Vector3D v2, Vector3D v3, Vector3D v4) {
        this.points[0] = (float)v1.getX();
        this.points[1] = (float)v1.getY();
        this.points[2] = (float)v1.getZ();
        this.points[3] = (float)v2.getX();
        this.points[4] = (float)v2.getY();
        this.points[5] = (float)v2.getZ();
        this.points[6] = (float)v3.getX();
        this.points[7] = (float)v3.getY();
        this.points[8] = (float)v3.getZ();
        this.points[9] = (float)v4.getX();
        this.points[10] = (float)v4.getY();
        this.points[11] = (float)v4.getZ();
        set();
    }

    /**
     * Sets x,y,z-coordinate of nodes of this Curve.
     *
     * @param number The number of a node. The node whose number is 0 or 3 is a anchor point, and
     *        the node whose number is 1 or 2 is a control point.
     *
     * @param x The x-coordinate of this node.
     * @param y The y-coordinate of this node.
     */
    public void setNode(int number, double x, double y) {
        if (number <= 0) number = 0;
        if (number >= 3) number = 3;
        this.points[number * 3] = (float)x;
        this.points[number * 3 + 1] = (float)y;
        this.points[number * 3 + 2] = 0;
        set();
    }

    /**
     * Sets x,y,z-coordinate of nodes of this Curve.
     *
     * @param number The number of a node. The node whose number is 0 or 3 is a anchor point, and
     *        the node whose number is 1 or 2 is a control point.
     *
     * @param x The x-coordinate of this node.
     * @param y The y-coordinate of this node.
     * @param z The y-coordinate of this node.
     */
    public void setNode(int number, double x, double y, double z) {
        if (number <= 0) number = 0;
        if (number >= 3) number = 3;
        this.points[number * 3] = (float)x;
        this.points[number * 3 + 1] = (float)y;
        this.points[number * 3 + 2] = (float)z;
        set();
    }

    /**
     * Sets coordinate of nodes of this Curve.
     *
     * @param number The number of a node. The node whose number is 0 or 3 is a anchor point, and
     *        the node whose number is 1 or 2 is a control point.
     *
     * @param v The coordinates of this node.
     */
    public void setNode(int number, Vector3D v) {
        if (number <= 0) {
            number = 0;
        } else if (3 <= number) {
            number = 3;
        }
        this.points[number * 3] = (float)v.getX();
        this.points[number * 3 + 1] = (float)v.getY();
        this.points[number * 3 + 2] = (float)v.getZ();
        set();
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001
            || this.isDepthTest() == false) {
            gl.glDisable(GL2.GL_DEPTH_TEST);
        }

        gl.glPushMatrix();
        this.move(gl);
        gl.glTranslated(-this.points[0], -this.points[1], -this.points[2]);


        if (this.fill) {
            getSceneFillColor().setup(gl);
            gl.glBegin(GL2.GL_TRIANGLE_STRIP);
            for (int i = 0; i < detail; i++) {
                if (i == 0 && isGradation() && startColor != null)
                    getSceneColor(this.startColor).setup(gl);
                if (i == detail - 1 && isGradation() && endColor != null)
                    getSceneColor(this.endColor).setup(gl);
                if (i != 0 && i != (detail - 1) && isGradation() && endColor != null
                    && startColor != null) {
                    gradationColor =
                        RGBColor.lerpColor(this.startColor, this.endColor, (i / (double)(detail - 1)));
                    getSceneColor(this.gradationColor).setup(gl);
                }
                gl.glVertex2d(catmullRom(points[0], points[3], points[6], points[9], ((i + 1) / (float)detail)), catmullRom(points[1], points[4], points[7], points[10], ((i + 1) / (float)detail)));
            }
            gl.glEnd();
        }

        if (this.stroke) {
            getSceneStrokeColor().setup(gl);
            gl.glLineWidth(this.strokeWidth);
            gl.glBegin(GL2.GL_LINE_STRIP);
            for (int i = 0; i < detail; i++) {
                if (i == 0 && isGradation() && startColor != null)
                    getSceneColor(this.startColor).setup(gl);
                if (i == detail - 1 && isGradation() && endColor != null)
                    getSceneColor(this.endColor).setup(gl);
                if (i != 0 && i != (detail - 1) && isGradation() && endColor != null
                    && startColor != null) {
                    gradationColor =
                        RGBColor.lerpColor(this.startColor, this.endColor, (i / (double)(detail - 1)));
                    getSceneColor(this.gradationColor).setup(gl);
                }
                gl.glVertex2d(catmullRom(points[0], points[3], points[6], points[9], ((i + 1) / (float)detail)), catmullRom(points[1], points[4], points[7], points[10], ((i + 1) / (float)detail)));
            }
            gl.glEnd();
        }

        gl.glPopMatrix();

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001
            || this.isDepthTest() == false) gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    private final double catmullRom(float p0, float p1, float p2, float p3, float t) {
        double v0 = (p2 - p0) * 0.5;
        double v1 = (p2 - p0) * 0.5;
        return (2 * p1 - 2 * p2 + v0 + v1) * t * t * t + (-3 * p1 + 3 * p2 - 2 * v0 - v1) * t * t
            + v0 * t + p1;
    }

    /**
     * Evaluates the curve at point t.
     *
     * @param vec The coordinate to get the location of a curve at t.
     * @param t value between 0 and 1
     */
    public double curvePoint(XYZ vec, float t) {
        double tmp = 0;
        switch (vec) {
        case X:
            tmp = catmullRom(points[0], points[3], points[6], points[9], t);
            break;
        case Y:
            tmp = catmullRom(points[1], points[4], points[7], points[10], t);
            break;
        default:
            break;
        }
        return tmp;
    }

    /**
     * Gets the detail of this Curve.
     *
     * @return The detail of the Curve.
     */
    public int getDetail() {
        return detail;
    }

    /**
     * Sets the detail of this Curve.
     *
     * @param detail The detail of the Curve.
     */
    public void setDetail(int detail) {
        this.detail = detail;
    }

    private final void set() {
        x = this.points[0];
        y = this.points[1];
        z = this.points[2];
    }

    /**
     * Sets the color of the anchor point for gradation.
     *
     * @param index The index of anchors. The index of the start anchor point is 0, the index of the
     *        end anchor point is 1.
     *
     * @param color The color of the anchor point.
     * */
    public void setAnchorColor(int index, Color color) {
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

    /**
     * Sets the colorSet of the anchor point for gradation.
     *
     * @param index The index of anchors. The index of the start anchor point is 0, the index of the
     *        end anchor point is 1.
     *
     * @param colorSet The colorSet of the anchor point.
     * */
    public void setAnchorColor(int index, ColorSet colorSet) {
        setAnchorColor(index, new RGBColor(colorSet));
    }

    @Override
    public void reset(GL2 gl) {}
}
