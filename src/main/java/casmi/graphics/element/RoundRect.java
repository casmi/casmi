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

/**
 * RoundRect class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class RoundRect extends Element {

    public enum ShapeMode {
        CORNER, CORNERS, RADIUS, CENTER
    };

    private double w;
    private double h;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double x3;
    private double y3;
    private double x4;
    private double y4;
    private double r = 1.0;
    private double precision = 20.0;

    private ShapeMode MODE = ShapeMode.CENTER;

    private Arc arc[] = new Arc[4];

    /**
     * Creates a new RoundRect object using position of the upper-left corner, width and height.
     *
     * @param x The x-coordinate of the upper-left corner.
     * @param y The y-coordinate of the upper-left corner.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public RoundRect(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        setRect();
        initArc();
    }

    /**
     * Creates a new RoundRect object using width and height.
     *
     *
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public RoundRect(double width, double height) {
        this.x = 0;
        this.y = 0;
        this.w = width;
        this.h = height;
        setRect();
        initArc();
    }

    /**
     * Creates a new RoundRect object using radius, width and height.
     *
     * @param radius The radius of the corner.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public RoundRect(double radius, double width, double height) {
        this.x = 0;
        this.y = 0;
        this.w = width;
        this.h = height;
        this.r = radius;
        setRect();
        initArc();
    }

    /**
     * Creates a new RoundRect object using radius, position of the upper-left corner, width and
     * height.
     *
     * @param radius The radius of the corner.
     * @param x The x-coordinate of the upper-left corner.
     * @param y The y-coordinate of the upper-left corner.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public RoundRect(double radius, double x, double y, double width, double height) {
        this.r = radius;
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        setRect();
        initArc();
    }

    private final void setArc() {
        arc[0].set(x1 + r, y1 - r, r, 90, 180, precision);
        arc[1].set(x2 + r, y2 + r, r, 180, 270, precision);
        arc[2].set(x3 - r, y3 + r, r, 270, 360, precision);
        arc[3].set(x4 - r, y4 - r, r, 0, 90, precision);
    }

    private final void initArc() {
        arc[0] = new Arc(x1 + r, y1 - r, r, 90, 180, precision);
        arc[1] = new Arc(x2 + r, y2 + r, r, 180, 270, precision);
        arc[2] = new Arc(x3 - r, y3 + r, r, 270, 360, precision);
        arc[3] = new Arc(x4 - r, y4 - r, r, 0, 90, precision);
    }

    private final void setRect() {
        switch (MODE) {
        case CORNER:
            this.x1 = 0.0;
            this.y1 = 0.0;
            this.x2 = 0.0;
            this.y2 = 0.0 - h;
            this.x3 = 0.0 + w;
            this.y3 = 0.0 - h;
            this.x4 = 0.0 + w;
            this.y4 = 0.0;
            break;
        case CORNERS:
            this.x1 = 0.0;
            this.y1 = 0.0;
            this.x2 = 0.0;
            this.y2 = h;
            this.x3 = w;
            this.y3 = h;
            this.x4 = w;
            this.y4 = 0.0;
            break;
        case CENTER:
            this.x1 = 0.0 - w / 2;
            this.y1 = 0.0 + h / 2;
            this.x2 = 0.0 - w / 2;
            this.y2 = 0.0 - h / 2;
            this.x3 = 0.0 + w / 2;
            this.y3 = 0.0 - h / 2;
            this.x4 = 0.0 + w / 2;
            this.y4 = 0.0 + h / 2;
            break;
        case RADIUS:
            this.x1 = 0.0 - w;
            this.y1 = 0.0 + h;
            this.x2 = 0.0 - w;
            this.y2 = 0.0 - h;
            this.x3 = 0.0 + w;
            this.y3 = 0.0 - h;
            this.x4 = 0.0 + w;
            this.y4 = 0.0 + h;
            break;
        }
    }

    private final void calcRect() {
        setRect();
        setArc();
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        calcRect();

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001
            || !this.isDepthTest()) {
            gl.glDisable(GL2.GL_DEPTH_TEST);
        }

        gl.glPushMatrix();
        this.move(gl);
        if (this.fill) {
            getSceneFillColor().setup(gl);
            // this.fillColor.setup(gl);
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2d(x1 + r, y1);
            gl.glVertex2d(x2 + r, y2);
            gl.glVertex2d(x3 - r, y3);
            gl.glVertex2d(x4 - r, y4);
            gl.glEnd();
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2d(x1, y1 - r);
            gl.glVertex2d(x2, y2 + r);
            gl.glVertex2d(x2 + r, y3 + r);
            gl.glVertex2d(x1 + r, y4 - r);
            gl.glEnd();
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2d(x4 - r, y1 - r);
            gl.glVertex2d(x3 - r, y2 + r);
            gl.glVertex2d(x3, y3 + r);
            gl.glVertex2d(x4, y4 - r);

            gl.glEnd();

            for (int i = 0; i < 4; i++) {
                arc[i].setStroke(false);
                arc[i].setFill(true);
                arc[i].setFillColor(this.fillColor);
                arc[i].render(gl, glu, width, height, selection);
            }
        }

        if (this.stroke) {
            gl.glLineWidth(this.strokeWidth);
            // this.strokeColor.setup(gl);
            getSceneStrokeColor().setup(gl);
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2d(x1, y1 - r);
            gl.glVertex2d(x2, y2 + r);
            gl.glVertex2d(x2 + r, y2);
            gl.glVertex2d(x3 - r, y3);
            gl.glVertex2d(x3, y3 + r);
            gl.glVertex2d(x4, y4 - r);
            gl.glVertex2d(x4 - r, y4);
            gl.glVertex2d(x1 + r, y1);
            gl.glEnd();
            for (int i = 0; i < 4; i++) {
                arc[i].setFill(false);
                arc[i].setStroke(true);
                arc[i].setStrokeWidth(this.strokeWidth);
                arc[i].setStrokeColor(this.strokeColor);
                arc[i].render(gl, glu, width, height, selection);
            }
        }
        gl.glPopMatrix();

        if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001
            || !this.isDepthTest()) {
            gl.glEnable(GL2.GL_DEPTH_TEST);
        }
    }

    /**
     * Gets width of this RoundRect.
     *
     * @return The width of the RoundRect.
     */
    public final double getWidth() {
        return this.w;
    }

    /**
     * Gets height of this RoundRect.
     *
     * @return The height of the RoundRect.
     */
    public final double getHeight() {
        return this.h;
    }

    /**
     * Sets width of this RoundRect.
     *
     * @param width The width of the RoundRect.
     */
    public final void setWidth(double width) {
        this.w = width;
    }

    /**
     * Sets height of this RoundRect
     *
     * @param height The height of the RoundRect.
     */
    public final void setHeight(double height) {
        this.h = height;
    }

    /**
     * Sets the radius of the corner of this RoundRect.
     *
     * @param radius The radius of the corner.
     */
    public final void setRadius(double radius) {
        this.r = radius;
    }

    /**
     * Gets the radius of the corner of this RoundRect.
     *
     * @return The radius of the corner.
     */
    public final double getRadius() {
        return this.r;
    }

    /**
     * Gets the precision of the corner arc.
     *
     * @return The precision of the corner.
     */
    public final double getPrecision() {
        return this.precision;
    }

    /**
     * Sets the precision of the corner arc.
     *
     * @param precision The precision of the corner.
     */
    public final void setPrecision(double precision) {
        this.precision = precision;
    }

    /**
     * Align the position of this RoundRect
     *
     * @param mode The Alignment of the position.
     */
    public final void rectMode(ShapeMode mode) {
        this.MODE = mode;
    }

    @Override
    public void reset(GL2 gl) {}
}
