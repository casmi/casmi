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

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;
import casmi.graphics.element.Text;
import casmi.graphics.group.Group;
import casmi.timeline.TimelineRender;
import casmi.tween.TweenManager;

import com.sun.opengl.util.BufferUtil;

/**
 * GrapicsObject.
 * 
 * @author Y. Ban
 */
public class GraphicsObject extends Element implements ObjectRender {

	private Graphics g;
	
	private double tmpAs, tmpAf;
	
	private List<Object> objectList;
	private List<Light> lightList;
	private List<Camera> cameraList;
	private List<Perse> perseList;
	private List<TweenManager> tmList;
	private List<Integer> selectionList;
	
	private BackGround bg;

	private enum MatrixMode {
		APPLY, LOAD, NONE
	};

	private MatrixMode mode = MatrixMode.NONE;
	private DoubleBuffer matrix;
	private boolean selectionbuff = false;
	private int selectionbufsize = 1024*1024;
	private int selectedIndex = -1;
	private int s = 0;
	private IntBuffer selectBuffer;
	private int selectBuff[];

	public GraphicsObject() {
	    objectList    = new CopyOnWriteArrayList<Object>();
		lightList     = new CopyOnWriteArrayList<Light>();
		cameraList    = new CopyOnWriteArrayList<Camera>();
		perseList     = new CopyOnWriteArrayList<Perse>();
		tmList        = new CopyOnWriteArrayList<TweenManager>();
		selectionList = new CopyOnWriteArrayList<Integer>();
		selectBuffer  = BufferUtil.newIntBuffer(selectionbufsize);
		selectBuff    =  new int[selectionbufsize];
	}

	public void add(Object object) {
		objectList.add(object);
	}
	
	public void addAll(Collection<? extends Object> c) {
	    objectList.addAll(c);
	}

	public void addLight(Light r) {
		r.setIndex(lightList.size());
		lightList.add(r);
	}

	public void addCamera(Camera r) {
		cameraList.add(r);
	}

	public void addPerse(Perse r) {
		perseList.add(r);
	}

	public void addTweenManager(TweenManager r) {
		tmList.add(r);
	}

	public void remove(int index) {
		objectList.remove(index);
	}

	public void removeLight(int index) {
		lightList.remove(index);
	}

	public void removeCamera(int index) {
		cameraList.remove(index);
	}

	public void removePerse(int index) {
		perseList.remove(index);
	}

	public void removeTweenManager(int index) {
		tmList.remove(index);
	}

	public Object get(int index) {
		return (Object) objectList.get(index);
	}

	public Object getLight(int index) {
		return (Object) lightList.get(index);
	}

	public Object getCamera(int index) {
		return (Object) cameraList.get(index);
	}

	public Object getPerse(int index) {
		return (Object) perseList.get(index);
	}

	public TweenManager getTweenManager(int index) {
		return (TweenManager) tmList.get(index);
	}

	public void add(int index, Object r) {
		objectList.add(index, r);
	}

	public void addLight(int index, Light r) {
		lightList.add(index, r);
	}

	public void addCamera(int index, Camera r) {
		cameraList.add(index, r);
	}

	public void addPerse(int index, Perse r) {
		perseList.add(index, r);
	}

	public void clear() {
		objectList.clear();
	}

	public void clearLight() {
		lightList.clear();
	}

	public void clearCamera() {
		cameraList.clear();
	}

	public void clearPerse() {
		perseList.clear();
	}

	public void clearTweenManager() {
		tmList.clear();
	}

	/**
	 * Applies the transformation matrix.
	 */
	public void applyMatrix(double matrix[]) {
		this.matrix = java.nio.DoubleBuffer.wrap(matrix);
		this.mode = MatrixMode.APPLY;
	}

	public void applyMatrix(DoubleBuffer matrix) {
		this.matrix = matrix;
		this.mode = MatrixMode.APPLY;
	}

	public void loadMatrix(double matrix[]) {

		this.matrix = java.nio.DoubleBuffer.wrap(matrix);
		this.mode = MatrixMode.LOAD;
	}

	public void loadMatrix(DoubleBuffer matrix) {
		this.matrix = matrix;
		this.mode = MatrixMode.LOAD;
	}

	public void setBackGroundColor(BackGround bg) {
		this.bg = bg;
	}

	public void selectionbufRender(Graphics g, double mouseX, double mouseY,
			int index) {
		if (selectionbuff == true || this.isSelectionbuffer() == true) {
			Arrays.fill(selectBuff,0);
			selectBuffer.position(0);
			int hits;
			int viewport[] = new int[4];

			g.getGL().glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
			g.getGL().glSelectBuffer(selectionbufsize, selectBuffer);
			g.getGL().glRenderMode(GL.GL_SELECT);

			g.getGL().glInitNames();
			g.getGL().glPushName(-1);

			drawTweenManager(g);

			g.getGL().glMatrixMode(GL.GL_PROJECTION);
			g.getGL().glLoadIdentity();

			g.getGLU().gluPickMatrix(mouseX, mouseY, 5.0, 5.0, viewport, 0);

			g.getGL().glMatrixMode(GL.GL_MODELVIEW);
			g.getGL().glLoadIdentity();
			drawPerse(g, true);
			drawCamera(g);
			if (bg != null)
				bg.render(g);
			drawLight(g);
			g.pushMatrix();
			setMatrix(g);
			this.setTweenParameter(g.getGL());
			drawObject(g, true, mouseX, mouseY, index, selectedIndex);
			g.popMatrix();

			hits = g.getGL().glRenderMode(GL.GL_RENDER);
			selectBuffer.get(selectBuff);
			processHits(hits, selectBuff);
			g.getGL().glMatrixMode(GL.GL_MODELVIEW);

		}
	}

	private void processHits(int hits, int buffer[]) {
		if (hits > 0) {
			selectedIndex = buffer[4 * hits - 1];
		} else {
			selectedIndex = -1;
		}
	}

	@Override
	public void render(Graphics g) {
		if (this.isVisible()) {
			this.g = g;
			
			drawTweenManager(g);
			
			drawPerse(g, false);
			
			drawCamera(g);
			
			if (bg != null)
				bg.render(g);
			
			drawLight(g);
			
			g.pushMatrix();
			{
			    setMatrix(g);
			    this.setTweenParameter(g.getGL());
			}
			g.popMatrix();

			if (this instanceof Group) {
				update(g);
			}
		}
	}

	public void bufRender(Graphics g, double mouseX, double mouseY, boolean bool, int index) {
		if (this.isVisible()) {
			this.g = g;
			
			drawTweenManager(g);
			
			if (!bool)
			    drawPerse(g, bool);

			drawCamera(g);
			
			if (bg != null)
				bg.render(g);
			
			drawLight(g);
			
			g.pushMatrix();
			{
			    setMatrix(g);
			    this.setTweenParameter(g.getGL());
			    drawObject(g, bool, mouseX, mouseY, index, -1);
			}
			g.popMatrix();
			
			if (this instanceof Group) {
				update(g);
			}
		}
	}

	private int bufRender(Graphics g, double mouseX, double mouseY,
			boolean bool, int index, int selectedIndex) {
		int sIndex = -1;
		if (this.isVisible() == true) {
			this.g = g;
			drawTweenManager(g);
			if (bool == false)
				drawPerse(g, bool);

			drawCamera(g);
			if (bg != null)
				bg.render(g);
			drawLight(g);
			g.pushMatrix();
			setMatrix(g);
			this.setTweenParameter(g.getGL());
			sIndex = drawObject(g, bool, mouseX, mouseY, index, selectedIndex);
			g.popMatrix();
			if (this instanceof Group) {
				update(g);
			}
		}

		return sIndex;
	}

	public void render(Element el) {
		if (el.isVisible()) {
			if (el.isMasked()) {
				el.getMask().render(g);
			}
			
			g.pushMatrix();
			{
			    if (el.isTween()) {
			        tmpAs = el.gettAS();
			        tmpAf = el.gettAF();
			        el.settAF(tmpAf * this.getSceneFillColor().getAlpha());
			        el.settAS(tmpAs * this.getSceneStrokeColor().getAlpha());
			        g.render(el);
			        el.settAF(tmpAf);
			        el.settAS(tmpAs);
			    } else {
			        tmpAf = el.getFillColor().getAlpha();
			        tmpAs = el.getStrokeColor().getAlpha();
			        el.getFillColor().setAlpha(tmpAf * this.getSceneFillColor().getAlpha());
			        el.getStrokeColor().setAlpha(tmpAs * this.getSceneStrokeColor().getAlpha());
			        g.render(el);
			        el.getFillColor().setAlpha(tmpAf);
			        el.getStrokeColor().setAlpha(tmpAs);
			    }

			    if (el.isMasked()) {
			        g.getGL().glDisable(GL.GL_STENCIL_TEST);
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
		int sIndex = -1;
		for (Object obj : objectList) {
			if (obj instanceof GraphicsObject) {
				GraphicsObject o = (GraphicsObject)obj;
				if (!selection) {
					if (((Element) o).getMask() != null) {
						((Element) o).getMask().render(g);
					}
					if (o.getMouseOverCallback() != null) {
						selectionbuff = true;
						System.out.println("pickup");
					}
					o.bufRender(g, mouseX, mouseY, false, selectionIndex);
					if (((Element) o).getMask() != null)
						g.getGL().glDisable(GL.GL_STENCIL_TEST);
				} else {
					sIndex = selectionIndex;
					//System.out.println("test"+sIndex);
					selectionIndex = o.bufRender(g, mouseX, mouseY, true,
							selectionIndex, selectedIndex);
//stuck selectionID of elements in a group
//					for (int j = sIndex; j < selectionIndex; j++) {
//						o.getSelectionList().add(j);
//
//					}
					if (o.getMouseOverCallback() != null) {

//						s = 0;
//						for (int j = 0; j < o.getSelectionList().size(); j++) {
//							if (o.getSelectionList().get(j) == selectedIndex) {
//								s += 1;
//							}
//						}

//						if (s > 0) {
//							o.callMouseOverCallback(true);
//						} else {
//							o.setMouseover(false);
//						}
//						if (o.isMouseover() == false
//								&& o.isPreMouseover() == true)
//							o.callMouseOverCallback(false);
					}

				}
				o.setPreMouseover(o.isMouseover());
			} else if (obj instanceof TimelineRender) {
				TimelineRender tr = (TimelineRender) obj;
				tr.render(g);
			} else if (obj instanceof TweenManager) {
				TweenManager tm = (TweenManager) obj;
				if (selection == false)
					tm.render(g);
			} else {
				Element e = (Element) obj;
				if (selection == false) {
					if (e.getMouseOverCallback() != null) {
						selectionbuff = true;
					}
					this.render((Element) obj);
				} else {
					if (e.getMouseOverCallback() != null) {
						g.getGL().glLoadName(selectionIndex);
						if (e instanceof Text) {
							((Text) e).setSelection(true);
						}
						if (selectionIndex == selectedIndex) {
							e.callMouseOverCallback(true);
						} else {
							e.setMouseover(false);
						}
						if (e.isMouseover() == false
								&& e.isPreMouseover() == true)
							e.callMouseOverCallback(false);
						selectionIndex++;
						this.render((Element) obj);
						if (e instanceof Text)
							((Text) e).setSelection(false);
					}

				}
				e.setPreMouseover(e.isMouseover());
			}
		}
		sIndex = selectionIndex;
		return selectionIndex;
	}

	public void callMouseClickCallbackOfChildren(casmi.MouseEvent e) {
		for (Object obj : objectList) {
			if (obj instanceof Element) {
				Element el = (Element)obj;
				if (el.isMouseover()) {
					el.callMouseClickCallback(e);
				}
			}
		}
	}

	private final void drawCamera(Graphics g) {
		for (Camera camera : cameraList) {
			if (camera instanceof Camera) {
				Camera c = (Camera) camera;
				c.render(g);
			}
		}
	}

	private final void drawPerse(Graphics g, boolean selection) {
		for (Perse perse : perseList) {
			if (perse instanceof Perspective) {
				Perspective perspective = (Perspective) perse;
				if (selection == false)
					perspective.render(g);
				else
					perspective.simplerender(g);
			} else if (perse instanceof Ortho) {
				Ortho ortho = (Ortho) perse;
				if (selection == false)
					ortho.render(g);
				else
					ortho.simplerender(g);
			} else if (perse instanceof Frustum) {
				Frustum frustum = (Frustum) perse;
				if (selection == false)
					frustum.render(g);
				else
					frustum.simplerender(g);
			}
		}
		if (selection == true && perseList.size() == 0) {
			g.simpleortho();
		}
	}

	private final void drawLight(Graphics g) {
		for (Light light : lightList)
			light.render(g);
	}

	public void setMatrix(Graphics g) {
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

	public int getSize() {
		return objectList.size();
	}

	public void update(Graphics g) {
		update();
	}

	public void update() {

	}
	
	public List<Object> getObjectList() {
		return objectList;
	}

	@Override
	public void render(GL gl, GLU glu, int width, int height) {
	}

	public boolean isSelectionbuff() {
		return selectionbuff;
	}

	public void setSelectionbuff(boolean selectionbuff) {
		this.selectionbuff = selectionbuff;
	}

	public int getSelectionbuffsize() {
		return selectionbufsize;
	}

	public void setSelectionbuffsize(int selectionbuffsize) {
		this.selectionbufsize = selectionbuffsize;
	}

	public List<Integer> getSelectionList() {
		return selectionList;
	}
	
	public void clearSelectionList() {
		selectionList.clear();
	}

	public void setSelectionList(ArrayList<Integer> selectionList) {
		this.selectionList = selectionList;
	}
}
