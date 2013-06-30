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

package casmi.graphics.object;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;

/**
 * Mask class.
 *
 * @author Y. Ban
 */
public class Mask {

    private List<Element> elements;
    private BufferedImage maskBuff;
    private boolean maskFlip;

    private double alpha;

    /**
     * Creates a new Mask.
     */
    public Mask() {
        elements = new ArrayList<Element>();
        maskFlip = false;
    }

    /**
     * Adds a Graphics Element for stencil mask.
     *
     * @param element
     *            Graphics Element for mask.
     */
    public void add(Element element) {
        elements.add(element);
    }

    /**
     * Adds a Graphics Element for stencil mask.
     *
     * @param element
     *            Graphics Element for mask.
     * @param index
     *               The index of the Mask.
     */
    public void add(Element element, int index) {
        elements.add(index, element);
    }

    /**
     * Removes the Element for Mask using index.
     *
     * @param index
     *               The index of the Element.
     */
    public void remove(int index) {
        elements.remove(index);
    }

    /**
     * Removes the Element for Mask using index.
     *
     * @param element
     *               The element for Mask
     */
    public void remove(Element element) {
        elements.remove(element);
    }

    /**
     * Removes all Elements for Mask.
     */
    public void clear() {
        elements.clear();
    }

    private void drawingMask(Graphics g) {
        for (Element e : elements)
            render(e, g);
    }

    private void render(Element e, Graphics g) {
        if (e.isVisible()) {
            g.pushMatrix();
            {
                if (e.isTween()) {
                    e.setFillAlpha((int)((int)e.getFillAlpha() * this.alpha));
                    e.setStrokeAlpha((int)((int)e.getStrokeAlpha() * this.alpha));
                    g.render(e, false);
                    e.setFillAlpha((int)e.getFillAlpha());
                    e.setStrokeAlpha((int)e.getStrokeAlpha());
                } else {
                    e.getFillColor().setAlpha(e.getFillColor().getAlpha() * this.alpha);
                    e.getStrokeColor().setAlpha(e.getStrokeColor().getAlpha() * this.alpha);
                    g.render(e, false);
                    e.getFillColor().setAlpha(e.getFillColor().getAlpha());
                    e.getStrokeColor().setAlpha(e.getStrokeColor().getAlpha());
                }
            }
            g.popMatrix();
        }
    }

    public void render(Graphics g) {
        GL2 gl = g.getGL();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_STENCIL_TEST);
        gl.glStencilFunc(GL2.GL_ALWAYS, 1, ~0);
        gl.glStencilOp(GL2.GL_KEEP, GL2.GL_REPLACE, GL2.GL_REPLACE);
        gl.glColorMask(false, false, false, false);
        gl.glDepthMask(false);
        drawingMask(g);
        gl.glColorMask(true, true, true, true);
        gl.glDepthMask(true);
        gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);
        if(maskFlip)
            gl.glStencilFunc(GL2.GL_NOTEQUAL, 1, ~0);
        else
            gl.glStencilFunc(GL2.GL_EQUAL, 1, ~0);
    }

    /**
     * Returns the Mask Image.
     *
     * @return
     *                 The Image for Mask
     */
    public BufferedImage getMaskBuff() {
        return maskBuff;
    }

    /**
     * Sets the Mask Image.
     *
     * @param maskBuff
     *                 The Image for Mask
     */
    public void setMaskBuff(BufferedImage maskBuff) {
        this.maskBuff = maskBuff;
    }

    /**
     * Sets Mask mode; masking the area where Elements draw.
     */
    public void setNormalMask(){
        this.maskFlip = false;
    }

    /**
     * Sets Mask mode; masking the ex-area where Elements draw.
     */
    public void setInverseMask(){
        this.maskFlip = true;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}
