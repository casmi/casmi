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

import casmi.GradationMode2D;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.image.Texture;

/**
 * Rect class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Rect extends Element {

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

    private Color startColor;
    private Color endColor;
    private Color gradationColor = new RGBColor(0.0, 0.0, 0.0);
    private GradationMode2D mode = GradationMode2D.HORIZONTAL;

    private Texture texture;

    /**
     * Creates a new Rect object using width and height.
     *
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public Rect(double width, double height) {
        this.w = width;
        this.h = height;
    }

    public Rect(double x, double y, double width, double height) {
        this.w = width;
        this.h = height;
        this.x = x;
        this.y = y;
    }

    /**
     * Sets width and height of this Rect.
     *
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public void set(double width, double height) {
        this.w = width;
        this.h = height;
    }

    /**
     * Sets width of this Rect.
     *
     * @param width The width of the rectangle.
     */
    public void setWidth(double width) {
        this.w = width;
    }

    /**
     * Sets height of this Rect.
     *
     * @param height The height of the rectangle.
     */
    public void setHeight(double height) {
        this.h = height;
    }

    /**
     * Gets width of this Rect.
     *
     * @return The width of the Rect.
     */
    public double getWidth() {
        return this.w;
    }

    /**
     * Gets height of this Rect.
     *
     * @return The height of the Rect.
     */
    public double getHeight() {
        return this.h;
    }

    private final void calcRect() {
        this.x1 = -w / 2;
        this.y1 = h / 2;
        this.x2 = -w / 2;
        this.y2 = -h / 2;
        this.x3 = w / 2;
        this.y3 = -h / 2;
        this.x4 = w / 2;
        this.y4 = h / 2;
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height, boolean selection) {
        calcRect();

        if (getSceneStrokeColor().getAlpha() < 1.0 || getSceneFillColor().getAlpha() < 1.0 || !isDepthTest())
            gl.glDisable(GL2.GL_DEPTH_TEST);

        if (this.enableTexture && this.texture != null) {
            this.texture.render(gl);
            this.texture.enableTexture(gl);
        }

        gl.glPushMatrix();
        {
            move(gl);

            if (fill) {
                getSceneFillColor().setup(gl);
                gl.glBegin(GL2.GL_QUADS);
                if (!isGradation()) {
                    if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(1, 0), getTexture().getTextureCorner(1, 1));
                    gl.glVertex2d(x1, y1);
                    if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(0, 0), getTexture().getTextureCorner(0, 1));
                    gl.glVertex2d(x2, y2);
                    if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(3, 0), getTexture().getTextureCorner(3, 1));
                    gl.glVertex2d(x3, y3);
                    if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(2, 0), getTexture().getTextureCorner(2, 1));
                    gl.glVertex2d(x4, y4);
                } else {
                    this.gradationColor.setRed((this.startColor.getRed() + this.endColor.getRed()) / 2);
                    this.gradationColor.setGreen((this.startColor.getGreen() + this.endColor.getGreen()) / 2);
                    this.gradationColor.setBlue((this.startColor.getBlue() + this.endColor.getBlue()) / 2);
                    this.gradationColor.setAlpha((this.startColor.getAlpha() + this.endColor.getAlpha()) / 2);
                    switch (mode) {
                    case HORIZONTAL:
                        getSceneColor(this.startColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(1, 0), getTexture().getTextureCorner(0, 1));
                        gl.glVertex2d(x1, y1);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(0, 0), getTexture().getTextureCorner(1, 1));
                        gl.glVertex2d(x2, y2);
                        getSceneColor(this.endColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(3, 0), getTexture().getTextureCorner(2, 1));
                        gl.glVertex2d(x3, y3);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(2, 0), getTexture().getTextureCorner(3, 1));
                        gl.glVertex2d(x4, y4);
                        break;
                    case VERTICAL:
                        getSceneColor(this.startColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(1, 0), getTexture().getTextureCorner(1, 1));
                        gl.glVertex2d(x1, y1);
                        getSceneColor(this.endColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(0, 0), getTexture().getTextureCorner(0, 1));
                        gl.glVertex2d(x2, y2);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(3, 0), getTexture().getTextureCorner(3, 1));
                        gl.glVertex2d(x3, y3);
                        getSceneColor(this.startColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(2, 0), getTexture().getTextureCorner(2, 1));
                        gl.glVertex2d(x4, y4);
                        break;
                    case LEFT_SIDEWAYS:
                        getSceneColor(this.startColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(1, 0), getTexture().getTextureCorner(1, 1));
                        gl.glVertex2d(x1, y1);
                        getSceneColor(this.gradationColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(0, 0), getTexture().getTextureCorner(0, 1));
                        gl.glVertex2d(x2, y2);
                        getSceneColor(this.endColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(3, 0), getTexture().getTextureCorner(3, 1));
                        gl.glVertex2d(x3, y3);
                        getSceneColor(this.gradationColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(2, 0), getTexture().getTextureCorner(2, 1));
                        gl.glVertex2d(x4, y4);
                        break;
                    case RIGHT_SIDEWAYS:
                        getSceneColor(this.gradationColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(1, 0), getTexture().getTextureCorner(1, 1));
                        gl.glVertex2d(x1, y1);
                        getSceneColor(this.endColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(0, 0), getTexture().getTextureCorner(0, 1));
                        gl.glVertex2d(x2, y2);
                        getSceneColor(this.gradationColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(3, 0), getTexture().getTextureCorner(3, 1));
                        gl.glVertex2d(x3, y3);
                        getSceneColor(this.startColor).setup(gl);
                        if (this.enableTexture) gl.glTexCoord2f(getTexture().getTextureCorner(2, 0), getTexture().getTextureCorner(2, 1));
                        gl.glVertex2d(x4, y4);
                        break;
                    }
                }
                gl.glEnd();
            }

            if (stroke) {
                gl.glLineWidth(this.strokeWidth);
                getSceneStrokeColor().setup(gl);
                gl.glBegin(GL2.GL_LINE_STRIP);
                {
                    gl.glVertex2d(x1, y1);
                    gl.glVertex2d(x2, y2);
                    gl.glVertex2d(x3, y3);
                    gl.glVertex2d(x4, y4);
                    gl.glVertex2d(x1, y1);
                }
                gl.glEnd();
            }
        }
        gl.glPopMatrix();

        if (this.enableTexture && this.texture != null) {
            this.texture.disableTexture(gl);
        }

        if (getSceneStrokeColor().getAlpha() < 1.0 || getSceneFillColor().getAlpha() < 1.0 || !isDepthTest())
            gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    /**
     * Sets the gradation mode and colors.
     *
     * @param mode The mode of gradation.
     * @param color1 The color for gradation.
     * @param color2 The color for gradation.
     *
     * @see casmi.GradationMode2D
     */
    public void setGradationColor(GradationMode2D mode, Color color1, Color color2) {
        setGradation(true);
        if (startColor == null || endColor == null) {
            startColor = new RGBColor(0.0, 0.0, 0.0);
            endColor = new RGBColor(0.0, 0.0, 0.0);
        }
        startColor = color1;
        endColor = color2;
        this.mode = mode;
    }

    /**
     * Sets the gradation mode and colors.
     *
     * @param mode The mode of gradation.
     * @param colorSet1 The colorSet for gradation.
     * @param colorSet2 The colorSet for gradation.
     *
     * @see casmi.GradationMode2D
     */
    public void setGradationColor(GradationMode2D mode, ColorSet colorSet1, ColorSet colorSet2) {
        setGradationColor(mode, new RGBColor(colorSet1), new RGBColor(colorSet2));
    }

    /**
     * Sets the gradation mode and colors.
     *
     * @param mode The mode of gradation.
     */
    public void setGradationMode(GradationMode2D mode) {
        this.mode = mode;
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
