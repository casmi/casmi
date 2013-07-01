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

package casmi.graphics.canvas;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.media.opengl.GL2;

import casmi.MouseStatus;
import casmi.graphics.Graphics;
import casmi.graphics.object.Background;
import casmi.tween.Tweener;

import com.jogamp.common.nio.Buffers;

/**
 * Root GraphicsObject.
 *
 * @author Y. Ban
 * @author Takashi AOKI <federkasten@me.com>
 */
public class RootCanvas extends Canvas {

    private Background background;

    private IntBuffer selectionBuffer;
    private int selections[];

    protected int SELECTION_BUFFER_SIZE = 1024*1024;

    private List<Canvas> canvases = new CopyOnWriteArrayList<Canvas>();

    protected List<Tweener> tweeners = new CopyOnWriteArrayList<Tweener>();

    public RootCanvas() {
        super();

        selectionBuffer = Buffers.newDirectIntBuffer(SELECTION_BUFFER_SIZE);
        selections = new int[SELECTION_BUFFER_SIZE];
    }

    public void setBackGroundColor(Background bg) {
        this.background = bg;
    }

    private void animate() {
        for (Tweener t: tweeners) {
            t.render();
        }
    }

    public synchronized void render(Graphics g, double mouseX, double mouseY) {
        animate();

        // render

//        if (removeObject) {
//            for (Object obj : objectList) {
//                if (obj instanceof Element && ((Element)obj).isRemove())
//                    objectList.remove(obj);
//            }
//            removeObject = false;
//        }

        if (background != null) background.render(g);
        g.clear();

        renderAll(g);

        for (Canvas c : canvases) {
            c.renderAll(g);
        }

        // render for selection

        Arrays.fill(selections, 0);
        selectionBuffer.position(0);

        int viewport[] = new int[4];

        g.getGL().glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
        g.getGL().glSelectBuffer(SELECTION_BUFFER_SIZE, selectionBuffer);
        g.getGL().glRenderMode(GL2.GL_SELECT);

        g.getGL().glInitNames();
        g.getGL().glPushName(-1);

        g.getGL().glMatrixMode(GL2.GL_PROJECTION);
        g.getGL().glLoadIdentity();

        g.getGLU().gluPickMatrix(mouseX, mouseY, 5.0, 5.0, viewport, 0);

        g.getGL().glMatrixMode(GL2.GL_MODELVIEW);
        g.getGL().glLoadIdentity();

        int index = 0;
        index = renderAllForSelection(g, mouseX, mouseY, index);

        for (Canvas c : canvases) {
            index = c.renderAllForSelection(g, mouseX, mouseY, index);
        }

        int hits = g.getGL().glRenderMode(GL2.GL_RENDER);

        selectionBuffer.get(selections);

        g.getGL().glMatrixMode(GL2.GL_MODELVIEW);

        int selectedIndex = processHits(hits, selections);

        int lastIndex = triggerMouseEvent(selectedIndex, 0);

        for (Canvas c: canvases) {
            lastIndex = c.triggerMouseEvent(selectedIndex, lastIndex);
        }
    }

    public synchronized void reset(Graphics g) {
        resetObjects(g);

        for (Canvas c: canvases) {
            c.resetObjects(g);
        }
    }

    private static final int processHits(int hits, int[] buffer) {
        if (hits > 0) {
            return buffer[4 * hits - 1];
        } else {
            return -1;
        }
    }

    public synchronized void addCanvas(Canvas c) {
        canvases.add(c);
    }

    public synchronized void removeCanvas(Canvas c) {
        canvases.remove(c);
    }

    public synchronized void removeAllCanvases() {
        canvases.clear();
    }

    public void updateMouseStatus(MouseStatus status) {
        setMouseStatus(status);

        for (Canvas c : canvases) {
            c.setMouseStatus(status);
        }
    }

    public synchronized void addTweener(Tweener t) {
        tweeners.add(t);
    }

    public synchronized void removeTweener(Tweener t) {
        tweeners.remove(t);
    }

    public synchronized void removeAllTweeners() {
        tweeners.clear();
    }
}
