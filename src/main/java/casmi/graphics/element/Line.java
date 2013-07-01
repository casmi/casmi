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
 * Line class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Line extends Element {

    public static final int LINES = 1;
    public static final int LINES_3D = 3;

    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double z1;
    private double z2;
    private double[] dx = new double[2];
    private double[] dy = new double[2];
    private double[] dz = new double[2];

    private Color startColor;
    private Color endColor;

    private int MODE;

    private boolean dashed = false;
    private double dashedLineLength;
    private double dashedLineInterval;

    private double[] dashedIntervalD = new double[3];
    private double[] dashedLengthD = new double[3];
    private double[] dashedRestD = new double[3];
    private double dashedRepeatNum;

    /**
     * Creates a new Line object
     */
    public Line() {}

    /**
     * Creates a new Line object using coordinates for the first and second point.
     *
     * @param x1 The x-coordinates for the first point.
     * @param y1 The y-coordinates for the first point.
     * @param x2 The x-coordinates for the second point.
     * @param y2 The y-coordinates for the second point.
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.MODE = LINES_3D;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.z1 = 0;
        this.z2 = 0;
        calcG();
        dx[0] = x1 - x;
        dx[1] = x2 - x;
        dy[0] = y1 - y;
        dy[1] = y2 - y;
        dz[0] = z1 - 0;
        dz[1] = z2 - 0;
    }

    /**
     * Creates a new Line object using coordinates for the first and second point.
     *
     * @param v1 The coordinates for the first point.
     * @param v2 The coordinates for the second point.
     */
    public Line(Vector3D v1, Vector3D v2) {
        this.MODE = LINES_3D;
        this.x1 = v1.getX();
        this.y1 = v1.getY();
        this.z1 = v1.getZ();
        this.x2 = v2.getX();
        this.y2 = v2.getY();
        this.z2 = v2.getZ();
        calcG();
        dx[0] = x1 - x;
        dx[1] = x2 - x;
        dy[0] = y1 - y;
        dy[1] = y2 - y;
        dz[0] = z1 - z;
        dz[1] = z2 - z;
    }

    /**
     * Creates a new Line object using coordinates for the first and second point.
     *
     * @param x1 The x-coordinates for the first point.
     * @param y1 The y-coordinates for the first point.
     * @param z1 The z-coordinates for the first point.
     * @param x2 The x-coordinates for the second point.
     * @param y2 The y-coordinates for the second point.
     * @param z2 The z-coordinates for the second point.
     */
    public Line(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.MODE = LINES_3D;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        calcG();
        dx[0] = x1 - x;
        dx[1] = x2 - x;
        dy[0] = y1 - y;
        dy[1] = y2 - y;
        dz[0] = z1 - z;
        dz[1] = z2 - z;
    }

    /**
     * Sets the coordinates for the first and second point.
     *
     * @param x1 The x-coordinates for the first point.
     * @param y1 The y-coordinates for the first point.
     * @param z1 The z-coordinates for the first point.
     * @param x2 The x-coordinates for the second point.
     * @param y2 The y-coordinates for the second point.
     * @param z2 The z-coordinates for the second point.
     */
    public void set(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.MODE = LINES_3D;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        calcG();
        dx[0] = x1 - x;
        dx[1] = x2 - x;
        dy[0] = y1 - y;
        dy[1] = y2 - y;
        dz[0] = z1 - z;
        dz[1] = z2 - z;
        if (dashed) calcDashedLine();
    }

    /**
     * Sets the coordinates for the first and second point.
     *
     * @param x1 The x-coordinates for the first point.
     * @param y1 The y-coordinates for the first point.
     * @param x2 The x-coordinates for the second point.
     * @param y2 The y-coordinates for the second point.
     */
    public void set(double x1, double y1, double x2, double y2) {
        this.MODE = LINES;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        calcG();
        dx[0] = x1 - x;
        dx[1] = x2 - x;
        dy[0] = y1 - y;
        dy[1] = y2 - y;
        dz[0] = z1 - 0;
        dz[1] = z2 - 0;
        if (dashed) calcDashedLine();
    }

    /**
     * Sets the coordinates for the first or second point.
     *
     * @param index choose the point; if index is true, the point is first.
     * @param x The x-coordinate of the first or second control point.
     * @param y The y-coordinate of the first or second control point.
     */
    public void set(boolean index, double x, double y) {
        this.MODE = LINES;
        if (index == true) {
            this.x1 = x;
            this.y1 = y;
        } else {
            this.x2 = x;
            this.y2 = y;
        }

        calcG();
        dx[0] = x1 - this.x;
        dx[1] = x2 - this.x;
        dy[0] = y1 - this.y;
        dy[1] = y2 - this.y;
        dz[0] = z1 - 0;
        dz[1] = z2 - 0;
        if (dashed) calcDashedLine();
    }

    /**
     * Sets the coordinates for the first or second point.
     *
     * @param index choose the point; if index is true, the point is first.
     * @param x The x-coordinate of the first or second control point.
     * @param y The y-coordinate of the first or second control point.
     * @param z The z-coordinate of the first or second control point.
     */
    public void set(boolean index, double x, double y, double z) {
        this.MODE = LINES_3D;
        if (index == true) {
            this.x1 = x;
            this.y1 = y;
            this.z1 = z;
        } else {
            this.x2 = x;
            this.y2 = y;
            this.z2 = z;

        }
        calcG();
        dx[0] = x1 - this.x;
        dx[1] = x2 - this.x;
        dy[0] = y1 - this.y;
        dy[1] = y2 - this.y;
        dz[0] = z1 - this.z;
        dz[1] = z2 - this.z;
        if (dashed) calcDashedLine();
    }

    /**
     * Sets the coordinates for the first and second point.
     *
     * @param v1 The coordinates for the first anchor point.
     * @param v2 The coordinates for the first control point.
     */
    public void set(Vector3D v1, Vector3D v2) {
        this.MODE = LINES;
        this.x1 = v1.getX();
        this.y1 = v1.getY();
        this.z1 = v1.getZ();
        this.x2 = v2.getX();
        this.y2 = v2.getY();
        this.z2 = v2.getZ();
        calcG();
        dx[0] = x1 - this.x;
        dx[1] = x2 - this.x;
        dy[0] = y1 - this.y;
        dy[1] = y2 - this.y;
        dz[0] = z1 - this.z;
        dz[1] = z2 - this.z;
        if (dashed) calcDashedLine();
    }

    public void setDashedLine(boolean dashed) {
        this.dashed = dashed;
    }

    /**
     * Sets the parameter for the dashed line.
     *
     * @param length The length of the dashed lines.
     * @param interval The interval of the dashed lines.
     */
    public void setDashedLinePram(double length, double interval) {
        this.dashedLineLength = length;
        this.dashedLineInterval = interval;
        this.dashed = true;
        calcDashedLine();
    }

    public double getDashedLinelength() {
        return this.dashedLineLength;
    }

    public double getDashedLineInterval() {
        return this.dashedLineInterval;
    }

    private double getLineLength() {
        return Math.sqrt(Math.pow((this.x2 - this.x1), 2) + Math.pow((this.y2 - this.y1), 2)
            + Math.pow((this.y2 - this.y1), 2));
    }

    private void calcDashedLine() {
        double len = getLineLength();
        dashedRepeatNum =
            (int)(((len - this.dashedLineInterval) / (this.dashedLineInterval + this.dashedLineLength)) / 2);
        double rest =
            ((len - this.dashedLineInterval) - (this.dashedLineInterval + this.dashedLineLength)
                * (dashedRepeatNum * 2)) / 2.0;
        if (rest > dashedLineLength) rest = dashedLineLength;
        switch (MODE) {
        case LINES_3D:
            dashedLengthD[2] = this.dashedLineLength / len * (this.z2 - this.z1);
            dashedIntervalD[2] = this.dashedLineInterval / len * (this.z2 - this.z1);
            dashedRestD[2] = rest / len * (this.z2 - this.z1);
        case LINES:
            dashedLengthD[0] = this.dashedLineLength / len * (this.x2 - this.x1);
            dashedLengthD[1] = this.dashedLineLength / len * (this.y2 - this.y1);
            dashedIntervalD[0] = this.dashedLineInterval / len * (this.x2 - this.x1);
            dashedIntervalD[1] = this.dashedLineInterval / len * (this.y2 - this.y1);
            dashedRestD[0] = rest / len * (this.x2 - this.x1);
            dashedRestD[1] = rest / len * (this.y2 - this.y1);
            break;
        }
    }

    private void drawDashedLine(GL2 gl) {
        int sign = 1;
        switch (MODE) {
        case LINES:
            gl.glLineWidth(this.strokeWidth);
            for (int j = 0; j < 2; j++) {
                if (j == 0)
                    sign = 1;
                else
                    sign = -1;
                for (int i = 0; i < dashedRepeatNum; i++) {
                    gl.glBegin(GL2.GL_LINES);
                    gl.glVertex2d(sign
                        * (this.dashedIntervalD[0] / 2.0 + (this.dashedIntervalD[0] + this.dashedLengthD[0])
                            * i), sign
                        * (this.dashedIntervalD[1] / 2.0 + (this.dashedIntervalD[1] + this.dashedLengthD[1])
                            * i));
                    gl.glVertex2d(sign
                        * (this.dashedIntervalD[0] / 2.0 + this.dashedLengthD[0] + (this.dashedIntervalD[0] + this.dashedLengthD[0])
                            * i), sign
                        * (this.dashedIntervalD[1] / 2.0 + this.dashedLengthD[1] + (this.dashedIntervalD[1] + this.dashedLengthD[1])
                            * i));
                    gl.glEnd();
                }
                gl.glBegin(GL2.GL_LINES);
                gl.glVertex2d(sign
                    * (this.dashedIntervalD[0] / 2.0 + (this.dashedIntervalD[0] + this.dashedLengthD[0])
                        * dashedRepeatNum), sign
                    * (this.dashedIntervalD[1] / 2.0 + (this.dashedIntervalD[1] + this.dashedLengthD[1])
                        * dashedRepeatNum));
                gl.glVertex2d(sign
                    * (this.dashedIntervalD[0] / 2.0 + this.dashedRestD[0] + (this.dashedIntervalD[0] + this.dashedLengthD[0])
                        * dashedRepeatNum), sign
                    * (this.dashedIntervalD[1] / 2.0 + this.dashedRestD[1] + (this.dashedIntervalD[1] + this.dashedLengthD[1])
                        * dashedRepeatNum));
                gl.glEnd();
            }
            break;
        case LINES_3D:
            gl.glLineWidth(this.strokeWidth);
            for (int j = 0; j < 2; j++) {
                if (j == 0)
                    sign = 1;
                else
                    sign = -1;
                for (int i = 0; i < dashedRepeatNum; i++) {
                    gl.glBegin(GL2.GL_LINES);
                    gl.glVertex3d(sign
                        * (this.dashedIntervalD[0] / 2.0 + (this.dashedIntervalD[0] + this.dashedLengthD[0])
                            * i), sign
                        * (this.dashedIntervalD[1] / 2.0 + (this.dashedIntervalD[1] + this.dashedLengthD[1])
                            * i), sign
                        * (this.dashedIntervalD[1] / 2.0 + (this.dashedIntervalD[2] + this.dashedLengthD[2])
                            * i));
                    gl.glVertex3d(sign
                        * (this.dashedIntervalD[0] / 2.0 + this.dashedLengthD[0] + (this.dashedIntervalD[0] + this.dashedLengthD[0])
                            * i), sign
                        * (this.dashedIntervalD[1] / 2.0 + this.dashedLengthD[1] + (this.dashedIntervalD[1] + this.dashedLengthD[1])
                            * i), sign
                        * (this.dashedIntervalD[2] / 2.0 + this.dashedLengthD[2] + (this.dashedIntervalD[2] + this.dashedLengthD[2])
                            * i));
                    gl.glEnd();
                }
                gl.glBegin(GL2.GL_LINES);
                gl.glVertex3d(sign
                    * (this.dashedIntervalD[0] / 2.0 + (this.dashedIntervalD[0] + this.dashedLengthD[0])
                        * dashedRepeatNum), sign
                    * (this.dashedIntervalD[1] / 2.0 + (this.dashedIntervalD[1] + this.dashedLengthD[1])
                        * dashedRepeatNum), sign
                    * (this.dashedIntervalD[2] / 2.0 + (this.dashedIntervalD[2] + this.dashedLengthD[2])
                        * dashedRepeatNum));
                gl.glVertex3d(sign
                    * (this.dashedIntervalD[0] / 2.0 + this.dashedRestD[0] + (this.dashedIntervalD[0] + this.dashedLengthD[0])
                        * dashedRepeatNum), sign
                    * (this.dashedIntervalD[1] / 2.0 + this.dashedRestD[1] + (this.dashedIntervalD[1] + this.dashedLengthD[1])
                        * dashedRepeatNum), sign
                    * (this.dashedIntervalD[2] / 2.0 + this.dashedRestD[2] + (this.dashedIntervalD[2] + this.dashedLengthD[2])
                        * dashedRepeatNum));
                gl.glEnd();
            }
            break;
        default:
            break;
        }
    }

    private void drawLine(GL2 gl) {
        switch (MODE) {
        case LINES:
            gl.glLineWidth(this.strokeWidth);
            gl.glBegin(GL2.GL_LINES);
            {
                if (isGradation() && startColor != null) getSceneColor(startColor).setup(gl);

                gl.glVertex2d(this.dx[0], this.dy[0]);

                if (isGradation() && endColor != null) getSceneColor(endColor).setup(gl);

                gl.glVertex2d(this.dx[1], this.dy[1]);
            }
            gl.glEnd();
            break;
        case LINES_3D:
            gl.glLineWidth(this.strokeWidth);
            gl.glBegin(GL2.GL_LINES);
            {
                if (isGradation() && startColor != null) getSceneColor(startColor).setup(gl);

                gl.glVertex3d(this.dx[0], this.dy[0], this.dz[0]);

                if (isGradation() && endColor != null) getSceneColor(endColor).setup(gl);

                gl.glVertex3d(this.dx[1], this.dy[1], this.dz[1]);
            }
            gl.glEnd();
            break;
        default:
            break;
        }
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001
            || this.isDepthTest() == false) {
            gl.glDisable(GL2.GL_DEPTH_TEST);
        }

        getSceneStrokeColor().setup(gl);

        gl.glPushMatrix();

        {
            this.move(gl);
            if (dashed) {
                drawDashedLine(gl);
            } else {
                drawLine(gl);
            }


        }
        gl.glPopMatrix();

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001
            || this.isDepthTest() == false) {
            gl.glEnable(GL2.GL_DEPTH_TEST);
        }
    }

    private final void calcG() {
        x = (x1 + x2) / 2.0;
        y = (y1 + y2) / 2.0;
        z = (z1 + z2) / 2.0;
    }

    /**
     * Sets the color of the first or second point for gradation.
     *
     * @param index The index of the point; if index is 0, the point is first.
     * @param color The color of the first or second point.
     */
    public void setCornerColor(int index, Color color) {
        if (index <= 0) {
            if (startColor == null) {
                startColor = new RGBColor(0.0, 0.0, 0.0);
            }
            setGradation(true);
            this.startColor = color;
        } else if (index >= 1) {
            if (endColor == null) {
                endColor = new RGBColor(0.0, 0.0, 0.0);
            }
            setGradation(true);
            this.endColor = color;
        }
    }

    /**
     * Sets the color of the first or second point for gradation.
     *
     * @param index The index of the point; if index is 0, the point is first.
     * @param colorSet The colorSt of the first or second point.
     */
    public void setCornerColor(int index, ColorSet colorSet) {
        setCornerColor(index, new RGBColor(colorSet));
    }

    /**
     * Gets coordinates of the point.
     *
     * @param index The index of the point; if the index is 0, the point is first.
     * @return The coordinates of the point.
     */
    public Vector3D getCorner(int index) {
        Vector3D v = new Vector3D();
        if (index <= 0)
            v.set(x1, y1, z1);
        else
            v.set(x2, y2, z2);
        return v;
    }

    @Override
    public void reset(GL2 gl) {}
}
