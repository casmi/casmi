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
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;

/**
 * Maxk class.
 * 
 * @author Y. Ban
 */
public class Mask extends Element implements ObjectRender {

    private List<Element> elements;
    private BufferedImage maskBuff;
    private boolean maskFlip;

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
     * 			  The index of the Mask.
     */
    public void add(Element element, int index) {
        elements.add(index, element);
    }

    /**
     * Removes the Element for Mask using index.
     * 
     * @param index
     * 			  The index of the Element.
     */
    public void remove(int index) {
        elements.remove(index);
    }

    /**
     * Removes the Element for Mask using index.
     * 
     * @param element
     * 			  The element for Mask
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

    private void render(Element el, Graphics g) {
        if (el.isVisible()) {
            g.pushMatrix();
            {
                if (el.isTween()) {
                    el.settAF((int)((int)el.gettAF() * this.getSceneFillColor().getAlpha()));
                    el.settAS((int)((int)el.gettAS() * this.getSceneStrokeColor().getAlpha()));
                    g.render(el);
                    el.settAF((int)el.gettAF());
                    el.settAS((int)el.gettAS());
                } else {
                    el.getFillColor().setAlpha(el.getFillColor().getAlpha() * getSceneFillColor().getAlpha());
                    el.getStrokeColor().setAlpha(el.getStrokeColor().getAlpha() * getSceneStrokeColor().getAlpha());
                    g.render(el);
                    el.getFillColor().setAlpha(el.getFillColor().getAlpha());
                    el.getStrokeColor().setAlpha(el.getStrokeColor().getAlpha());
                }
            }
            g.popMatrix();
        }
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height) {
    }

    @Override
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
     * 				The Image for Mask
     */
    public BufferedImage getMaskBuff() {
        return maskBuff;
    }

    /**
     * Sets the Mask Image.
     * 
     * @param maskBuff
     * 				The Image for Mask
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
}
