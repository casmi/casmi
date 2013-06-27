/*
 *   casmi
 *   http://casmi.github.io/
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

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import casmi.MouseEvent;
import casmi.graphics.Graphics;
import casmi.graphics.Graphics.MatrixMode;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Element;
import casmi.graphics.element.Reset;
import casmi.graphics.element.Text;
import casmi.graphics.group.Group;
import casmi.timeline.TimelineRender;
import casmi.tween.TweenManager;

import com.jogamp.common.nio.Buffers;

/**
 * Root GraphicsObject.
 *
 * @author Y. Ban
 * @author Takashi AOKI <federkasten@me.com>
 */
public class RootObject extends GraphicsObject {

    private BackGround bg;

    private int selectedIndex = -1;
    private IntBuffer selectBuffer;
    private int selectBuff[];

    public final int NO_SELECTIONBUFF = 10;

    public RootObject() {
        objectList = new CopyOnWriteArrayList<Object>();
        lights = new CopyOnWriteArrayList<Light>();
        cameras = new CopyOnWriteArrayList<Camera>();
        projections = new CopyOnWriteArrayList<Projection>();
        tmList = new CopyOnWriteArrayList<TweenManager>();
        selectionList = new CopyOnWriteArrayList<Integer>();
        mouseEventList = new CopyOnWriteArrayList<MouseEvent>();
        selectBuffer = Buffers.newDirectIntBuffer(selectionBufSize);
        selectBuff = new int[selectionBufSize];

        this.setDepthTest(false);
    }

    public RootObject(int selectionNum) {
        objectList = new CopyOnWriteArrayList<Object>();
        lights = new CopyOnWriteArrayList<Light>();
        cameras = new CopyOnWriteArrayList<Camera>();
        projections = new CopyOnWriteArrayList<Projection>();
        tmList = new CopyOnWriteArrayList<TweenManager>();
        selectionList = new CopyOnWriteArrayList<Integer>();
        selectBuffer = Buffers.newDirectIntBuffer(NO_SELECTIONBUFF);
        selectBuff = new int[NO_SELECTIONBUFF];

        this.setDepthTest(false);
    }

    @Override
    public void add(Object object) {
        if (object instanceof Element || object instanceof Group
            || object instanceof TimelineRender) objectList.add(object);
    }

    @Override
    public void addAll(Collection<? extends Object> c) {
        objectList.addAll(c);
    }

    @Override
    public void addLight(Light r) {
        r.setIndex(lights.size());
        lights.add(r);
    }

    @Override
    public void addCamera(Camera r) {
        cameras.add(r);
    }

    @Override
    public void addProjection(Projection p) {
        projections.add(p);
    }

    @Override
    public void addTweenManager(TweenManager r) {
        tmList.add(r);
    }

    @Override
    public void clearAllObjects() {
        objectList = null;
        objectList = new CopyOnWriteArrayList<Object>();
    }

    @Override
    public void remove(int index) {
        objectList.remove(index);
    }

    @Override
    public void removeLight(int index) {
        lights.remove(index);
    }

    @Override
    public void removeCamera(int index) {
        cameras.remove(index);
    }

    @Override
    public void removePerse(int index) {
        projections.remove(index);
    }

    @Override
    public void removeTweenManager(int index) {
        tmList.remove(index);
    }

    @Override
    public Object get(int index) {
        return objectList.get(index);
    }

    @Override
    public Object getLight(int index) {
        return lights.get(index);
    }

    @Override
    public Object getCamera(int index) {
        return cameras.get(index);
    }

    @Override
    public Object getProjection(int index) {
        return projections.get(index);
    }

    @Override
    public TweenManager getTweenManager(int index) {
        return tmList.get(index);
    }

    @Override
    public void add(int index, Object r) {
        objectList.add(index, r);
    }

    @Override
    public void addLight(int index, Light r) {
        lights.add(index, r);
    }

    @Override
    public void addCamera(int index, Camera r) {
        cameras.add(index, r);
    }

    @Override
    public void addProjection(int index, Projection r) {
        projections.add(index, r);
    }

    @Override
    public void clear() {
        objectList.clear();
    }

    @Override
    public void clearLight() {
        lights.clear();
    }

    @Override
    public void clearCamera() {
        cameras.clear();
    }

    @Override
    public void clearPerse() {
        projections.clear();
    }

    @Override
    public void clearTweenManager() {
        tmList.clear();
    }

    /**
     * Applies the transformation matrix.
     */
    @Override
    public void applyMatrix(double[] matrix) {
        this.matrix = java.nio.DoubleBuffer.wrap(matrix);
        this.mode = ObjectMatrixMode.APPLY;
    }

    @Override
    public void applyMatrix(DoubleBuffer matrix) {
        this.matrix = matrix;
        this.mode = ObjectMatrixMode.APPLY;
    }

    @Override
    public void loadMatrix(double[] matrix) {
        this.matrix = java.nio.DoubleBuffer.wrap(matrix);
        this.mode = ObjectMatrixMode.LOAD;
    }

    @Override
    public void loadMatrix(DoubleBuffer matrix) {
        this.matrix = matrix;
        this.mode = ObjectMatrixMode.LOAD;
    }

    public void setBackGroundColor(BackGround bg) {
        this.bg = bg;
    }

    public void renderSelectionAll(Graphics g, double mouseX, double mouseY, int index) {
        if (selectionbuff || isSelectionbuffer()) {
            Arrays.fill(selectBuff, 0);
            selectBuffer.position(0);
            int hits;
            int viewport[] = new int[4];

            g.getGL().glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
            g.getGL().glSelectBuffer(selectionBufSize, selectBuffer);
            g.getGL().glRenderMode(GL2.GL_SELECT);

            g.getGL().glInitNames();
            g.getGL().glPushName(-1);

            drawTweenManager(g);

            g.getGL().glMatrixMode(GL2.GL_PROJECTION);
            g.getGL().glLoadIdentity();

            g.getGLU().gluPickMatrix(mouseX, mouseY, 5.0, 5.0, viewport, 0);

            g.getGL().glMatrixMode(GL2.GL_MODELVIEW);
            g.getGL().glLoadIdentity();
            drawProjection(g, true);
            drawCamera(g);
            if (bg != null) bg.render(g);
            drawLight(g);
            g.pushMatrix();
            loadMatrix(g);
            this.move(g.getGL());
            drawObject(g, true, mouseX, mouseY, index, selectedIndex);
            g.popMatrix();

            hits = g.getGL().glRenderMode(GL2.GL_RENDER);
            selectBuffer.get(selectBuff);
            processHits(hits, selectBuff);
            g.getGL().glMatrixMode(GL2.GL_MODELVIEW);
        }

        if (removeObject) {
            Iterator<Object> itr = objectList.iterator();
            while (itr.hasNext()) {
                Object obj = itr.next();
                if (obj instanceof Element)
                    if (((Element)obj).isRemove())
                        objectList.remove(obj);
            }
        }
    }

    private void processHits(int hits, int[] buffer) {
        if (hits > 0) {
            selectedIndex = buffer[4 * hits - 1];
        } else {
            selectedIndex = -1;
        }
    }

    @Override
    public void render(Graphics g) {}

    public void renderAll(Graphics g) {
        if (this.isVisible()) {
            this.g = g;

            if (removeObject) {
                for (Object obj : objectList) {
                    if (obj instanceof Element && ((Element)obj).isRemove())
                        objectList.remove(obj);
                }
                removeObject = false;
            }

            if (bg != null) bg.render(g);

            drawTweenManager(g);
            drawProjection(g, false);
            drawCamera(g);
            drawLight(g);

            g.pushMatrix();
            {
                loadMatrix(g);
                this.move(g.getGL());
                drawObject(g, false, 0, 0, 0, -1);
            }
            g.popMatrix();
        }
    }

    @Override
    public void render(Element e) {
        if (e.isVisible()) {
            if (e.isMasked()) {
                e.getMask().render(g);
            }

            if (e.isUseProjection()) {
                this.setDepthTest(false);
            } else {
                this.setDepthTest(true);
            }

            g.pushMatrix();
            {
                if (e.isTween()) {
                    tmpAs = e.gettAS();
                    tmpAf = e.gettAF();
                    e.settAF(tmpAf * this.getSceneFillColor().getAlpha());
                    e.settAS(tmpAs * this.getSceneStrokeColor().getAlpha());
                    g.render(e);
                    e.settAF(tmpAf);
                    e.settAS(tmpAs);
                } else {
                    tmpAf = e.getFillColor().getAlpha();
                    tmpAs = e.getStrokeColor().getAlpha();
                    e.getFillColor().setAlpha(tmpAf * this.getSceneFillColor().getAlpha());
                    e.getStrokeColor().setAlpha(tmpAs * this.getSceneStrokeColor().getAlpha());

                    if (!e.isUseProjection()) {
                        g.matrixMode(MatrixMode.PROJECTION);
                        g.pushMatrix();
                        g.resetMatrix();
                        g.simpleOrtho();
                        g.matrixMode(MatrixMode.MODELVIEW);
                    }

                    g.render(e);

                    if (!e.isUseProjection()) {
                        g.matrixMode(MatrixMode.PROJECTION);
                        g.popMatrix();
                        g.matrixMode(MatrixMode.MODELVIEW);
                    }

                    e.getFillColor().setAlpha(tmpAf);
                    e.getStrokeColor().setAlpha(tmpAs);
                }

                if (e.isMasked()) {
                    g.getGL().glDisable(GL2.GL_STENCIL_TEST);
                }
            }
            g.popMatrix();
        }
    }

    private final void drawTweenManager(Graphics g) {
        for (TweenManager tm : tmList) {
            g.render(tm);
        }
    }

    private final int drawObject(Graphics g,
        boolean selection, double mouseX, double mouseY,
        int selectionIndex, int selectedIndex) {

        for (Object obj : objectList) {
            if (obj instanceof GraphicsObject) {
                GraphicsObject o = (GraphicsObject)obj;
                if (!selection) {
                    if (o.isRemove()) removeObject = true;
                    if (((Element)o).isMasked()) {
                        ((Element)o).getMask().render(g);
                    }
                    if (o.getMouseOverCallback() != null) {
                        selectionbuff = true;
                    }

                    if(!o.isUseProjection()) {
                        g.matrixMode(MatrixMode.PROJECTION);
                        g.pushMatrix();
                        g.resetMatrix();
                        g.simpleOrtho();
                        g.matrixMode(MatrixMode.MODELVIEW);
                        g.pushMatrix();
                        g.resetMatrix();
                    }

                    o.renderAll(g, mouseX, mouseY, false, selectionIndex);

                    if(!o.isUseProjection()) {
                        g.matrixMode(MatrixMode.MODELVIEW);
                        g.popMatrix();
                        g.matrixMode(MatrixMode.PROJECTION);
                        g.popMatrix();
                        g.matrixMode(MatrixMode.MODELVIEW);
                    }

                    if (o.isSelectionbuff() == true) selectionbuff = true;
                    if (((Element)o).isMasked()) g.getGL().glDisable(GL2.GL_STENCIL_TEST);
                } else {
                    if(!o.isUseProjection()) {
                        g.matrixMode(MatrixMode.PROJECTION);
                        g.pushMatrix();
                        g.resetMatrix();
                        g.simpleOrtho();
                        g.matrixMode(MatrixMode.MODELVIEW);
                        g.pushMatrix();
                        g.resetMatrix();
                    }

                    selectionIndex = o.renderSelectionAll(g, mouseX, mouseY, true, selectionIndex, selectedIndex);

                    if(!o.isUseProjection()) {
                        g.matrixMode(MatrixMode.MODELVIEW);
                        g.popMatrix();
                        g.matrixMode(MatrixMode.PROJECTION);
                        g.popMatrix();
                        g.matrixMode(MatrixMode.MODELVIEW);
                    }
                }
                o.setPrevMouseover(o.isMouseover());
            } else if (obj instanceof TimelineRender) {
                TimelineRender tr = (TimelineRender)obj;
                tr.render(g);
            } else if (obj instanceof TweenManager) {
                TweenManager tm = (TweenManager)obj;
                if (!selection) tm.render(g);
            } else {
                Element e = (Element)obj;
                if (selection) {
                    if (e.getMouseOverCallback() != null) {
                        g.getGL().glLoadName(selectionIndex);
                        if (e instanceof Text) {
                            ((Text)e).setSelection(true);
                        }
                        if (selectionIndex == selectedIndex) {
                            e.callMouseOverCallback(true);
                            e.callMouseClickCallback(mouseEvent);

                        } else {
                            e.setMouseover(false);
                        }
                        if (e.isMouseover() == false && e.isPreMouseover() == true)
                            e.callMouseOverCallback(false);
                        selectionIndex++;
                        this.render((Element)obj);
                        if (e instanceof Text) ((Text)e).setSelection(false);
                    }
                } else {
                    if (e.isRemove()) removeObject = true;
                    if (e.isReset()) {
                        resetObject = true;
                        e.setReset(false);
                    }
                    if (e.getMouseOverCallback() != null) {
                        selectionbuff = true;

                    }
                    this.render((Element)obj);
                }
                e.setPrevMouseover(e.isMouseover());
            }
        }
        return selectionIndex;
    }

    @Override
    public void callMouseClickCallbackOfChildren(casmi.MouseEvent e) {
        for (Object obj : objectList) {
            if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.callMouseClickCallbackOfChildren(e);
            } else if (obj instanceof Element) {
                Element el = (Element)obj;
                if (el.isMouseover()) {
                    el.callMouseClickCallback(e);
                }
            }
        }
    }

    @Override
    public void setMouseEvent(casmi.MouseEvent e) {
        mouseEvent = e;
        for (Object obj : objectList) {
            if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setMouseEvent(mouseEvent);
            }
        }
    }

    private final void drawCamera(Graphics g) {
        for (Camera camera : cameras) {
            if (camera instanceof Camera) {
                Camera c = camera;
                c.render(g);
            }
        }
    }

    private final void drawProjection(Graphics g, boolean selection) {
        for (Projection p : projections) {
            if (p instanceof Perspective) {
                Perspective perspective = (Perspective)p;
                if (!selection)
                    perspective.render(g);
                else
                    perspective.renderForSelection(g);
            } else if (p instanceof Ortho) {
                Ortho ortho = (Ortho)p;
                if (!selection)
                    ortho.render(g);
                else
                    ortho.renderForSelection(g);
            } else if (p instanceof Frustum) {
                Frustum frustum = (Frustum)p;
                if (!selection)
                    frustum.render(g);
                else
                    frustum.renderForSelection(g);
            }
        }
        if (selection && projections.size() == 0) {
            g.simpleOrtho();
        }
    }

    private final void drawLight(Graphics g) {
        for (Light light : lights)
            light.render(g);
    }

    @Override
    public void loadMatrix(Graphics g) {
        switch (mode) {
        case APPLY:
            g.applyMatrix(matrix);
            break;
        case LOAD:
            g.loadMatrix(matrix);
            break;
        case NONE:
            break;
        }
    }

    @Override
    public int getSize() {
        return objectList.size();
    }

    @Override
    public void update(Graphics g) {
        update();
    }

    @Override
    public void update() {

    }

    @Override
    public List<Object> getObjectList() {
        return objectList;
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height) {}

    @Override
    public boolean isSelectionbuff() {
        return selectionbuff;
    }

    @Override
    public void setSelectionbuff(boolean selectionbuff) {
        this.selectionbuff = selectionbuff;
    }

    public int getSelectionbuffsize() {
        return selectionBufSize;
    }

    public void setSelectionbuffsize(int selectionbuffsize) {
        this.selectionBufSize = selectionbuffsize;

        selectBuffer = Buffers.newDirectIntBuffer(selectionBufSize);
        selectBuff = new int[selectionBufSize];
    }

    @Override
    public List<Integer> getSelectionList() {
        return selectionList;
    }

    @Override
    public void clearSelections() {
        selectionList.clear();
    }

    @Override
    public void setSelectionList(ArrayList<Integer> selectionList) {
        this.selectionList = selectionList;
    }

    @Override
    public boolean isResetObject() {
        for (Object obj : objectList) {
            if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                if (go.isResetObject()) {
                    resetObject = true;
                    go.setResetObject(false);
                }
            }
        }
        return resetObject;
    }

    @Override
    public void setResetObject(boolean resetObject) {
        this.resetObject = resetObject;
    }

    @Override
    public void setStroke(boolean setStroke) {
        for (Object obj : objectList) {
            if (obj instanceof Element) {
                Element el = (Element)obj;
                el.setStroke(setStroke);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setStroke(setStroke);
            }
        }
    }

    @Override
    public void setFill(boolean setFill) {
        for (Object obj : objectList) {
            if (obj instanceof Element) {
                Element el = (Element)obj;
                el.setStroke(setFill);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setStroke(setFill);
            }
        }
    }


    /**
     * Sets the width of this Element's stroke.
     *
     * @param strokeWidth The width of the Element's stroke.
     */
    @Override
    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = (float)strokeWidth;
        for (Object obj : objectList) {
            if (obj instanceof Element) {
                Element el = (Element)obj;
                el.setStrokeWidth(strokeWidth);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setStrokeWidth(strokeWidth);
            }
        }
    }

    /**
     * Sets the color of this Element's stroke.
     *
     * @param color The color of the Element's stroke.
     */
    @Override
    public void setStrokeColor(Color color) {
        this.strokeColor = color;
        this.tAS = color.getAlpha();
        for (Object obj : objectList) {
            if (obj instanceof Element) {
                Element el = (Element)obj;
                el.setStrokeColor(color);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setStrokeColor(color);
            }
        }
    }

    @Override
    public void setStrokeColorAlpha(double alpha) {
        this.strokeColor.setAlpha(alpha);
        this.tAS = alpha;
        for (Object obj : objectList) {
            if (obj instanceof Element) {
                Element el = (Element)obj;
                el.setStrokeColorAlpha(alpha);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setStrokeColorAlpha(alpha);
            }
        }
    }

    /**
     * Sets the color of this Element's stroke.
     *
     * @param colorSet The colorSet of the Element's stroke.
     */
    @Override
    public void setStrokeColor(ColorSet colorSet) {
        strokeColor = new RGBColor(colorSet);
        for (Object obj : objectList) {
            if (obj instanceof Element) {
                Element el = (Element)obj;
                el.setStrokeColor(colorSet);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setStrokeColor(colorSet);
            }
        }
    }

    @Override
    public void setStrokeColor(ColorSet colorSet, double alpha) {
        strokeColor = new RGBColor(colorSet);
        this.tAS = alpha;
        for (Object obj : objectList) {
            if (obj instanceof Element) {
                Element el = (Element)obj;
                el.setStrokeColor(colorSet, alpha);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setStrokeColor(colorSet, alpha);
            }
        }
    }



    /**
     * Sets the color of this Element's fill.
     *
     * @param color The color of the Element's fill.
     */
    @Override
    public void setFillColor(Color color) {
        this.fillColor = color;
        this.tAF = color.getAlpha();
        for (Object obj : objectList) {
            if (obj instanceof Element) {
                Element el = (Element)obj;
                el.setFillColor(color);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setFillColor(color);
            }
        }
    }

    @Override
    public void setFillColorAlpha(double alpha) {
        this.fillColor.setAlpha(alpha);
        this.tAF = alpha;
        for (Object obj : objectList) {
            if (obj instanceof Element) {
                Element el = (Element)obj;
                el.setFillColorAlpha(alpha);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setFillColorAlpha(alpha);
            }
        }
    }

    /**
     * Sets the color of this Element's fill.
     *
     * @param colorSet The color of the Element's fill.
     */
    @Override
    public void setFillColor(ColorSet colorSet) {
        this.fillColor = new RGBColor(colorSet);
        for (Object obj : objectList) {
            if (obj instanceof Element) {
                Element el = (Element)obj;
                el.setFillColor(colorSet);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setFillColor(colorSet);
            }
        }
    }

    @Override
    public void setFillColor(ColorSet colorSet, double alpha) {
        this.fillColor = new RGBColor(colorSet, alpha);
        this.tAF = alpha;
        for (Object obj : objectList) {
            if (obj instanceof Element) {
                Element el = (Element)obj;
                el.setFillColor(colorSet, alpha);
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.setFillColor(colorSet, alpha);
            }
        }
    }

    @Override
    public void resetObjects() {
        for (Object obj : objectList) {
            if (obj instanceof Reset) {
                Reset el = (Reset)obj;
                el.reset(g.getGL());
            } else if (obj instanceof GraphicsObject) {
                GraphicsObject go = (GraphicsObject)obj;
                go.resetObjects();
            }
        }
    }
}
