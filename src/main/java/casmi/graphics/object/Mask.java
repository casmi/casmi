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

import javax.media.opengl.GL;
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

    public Mask() {
        elements = new ArrayList<Element>();
    }

    /**
     * Add a Graphics Element for stencil mask.
     * 
     * @param element
     *            Graphics Element for mask.
     */
    public void add(Element element) {
        elements.add(element);
    }

    public void add(Element element, int index) {
        elements.add(index, element);
    }

    public void remove(int index) {
        elements.remove(index);
    }

    public void remove(Element element) {
        elements.remove(element);
    }

    public void clear() {
        elements.clear();
    }

    public void drawingMask(Graphics g) {
        for (Element e : elements)
            render(e, g);
    }

    public void render(Element el, Graphics g) {
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
    public void render(GL gl, GLU glu, int width, int height) {
    }

    @Override
    public void render(Graphics g) {
        GL gl = g.getGL();
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_STENCIL_TEST);
        gl.glStencilFunc(GL.GL_ALWAYS, 1, ~0);
        gl.glStencilOp(GL.GL_KEEP, GL.GL_REPLACE, GL.GL_REPLACE);
        gl.glColorMask(false, false, false, false);
        gl.glDepthMask(false);
        drawingMask(g);
        gl.glColorMask(true, true, true, true);
        gl.glDepthMask(true);
        gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);
        gl.glStencilFunc(GL.GL_EQUAL, 1, ~0);
    }

    public BufferedImage getMaskBuff() {
        return maskBuff;
    }

    public void setMaskBuff(BufferedImage maskBuff) {
        this.maskBuff = maskBuff;
    }
}
