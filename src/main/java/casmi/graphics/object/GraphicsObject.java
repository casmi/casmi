package casmi.graphics.object;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

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
	private int tmpAs, tmpAf;
	private ArrayList<Object> objectList;
	private ArrayList<Light> lightList;
	private ArrayList<Camera> cameraList;
	private ArrayList<Perse> perseList;
	private ArrayList<TweenManager> tmList;
	private ArrayList<Integer> selectionList;
	private BackGround bg;

	private enum matrixMode {
		APPLY, LOAD, NONE
	};

	private matrixMode mode = matrixMode.NONE;
	private DoubleBuffer matrix;
	private boolean selectionbuff = false;
	private int selectionbufsize = 512;
	private int selectedIndex = -1;
	private int s = 0;

	public GraphicsObject() {
		objectList = new ArrayList<Object>();
		lightList = new ArrayList<Light>();
		cameraList = new ArrayList<Camera>();
		perseList = new ArrayList<Perse>();
		tmList = new ArrayList<TweenManager>();
		selectionList = new ArrayList<Integer>();
	}

	public int add(Object r) {
//		if (getSize() == 0) {
//			objectList.add(r);
//		} else {
//			for(Object object : objectList){
//				if(r.equals(object) && (r instanceof Element)){
//					Element e = (Element) r;
//					Element ec = e.clone();
//					objectList.add(ec);
//					return 0;
//				}
//			}
//			objectList.add(r);
//		}
		objectList.add(r);
		return 0;

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
		this.mode = matrixMode.APPLY;
	}

	public void applyMatrix(DoubleBuffer matrix) {
		this.matrix = matrix;
		this.mode = matrixMode.APPLY;
	}

	public void loadMatrix(double matrix[]) {

		this.matrix = java.nio.DoubleBuffer.wrap(matrix);
		this.mode = matrixMode.LOAD;
	}

	public void loadMatrix(DoubleBuffer matrix) {
		this.matrix = matrix;
		this.mode = matrixMode.LOAD;
	}

	public void setBackGroundColor(BackGround bg) {
		this.bg = bg;
	}

	public void selectionbufRender(Graphics g, double mouseX, double mouseY,
			int index) {
		if (selectionbuff == true || this.isSelectionbuffer() == true) {
			int selectBuff[] = new int[selectionbufsize];
			IntBuffer selectBuffer = BufferUtil.newIntBuffer(selectionbufsize);
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

	public void render(Graphics g) {
		if (this.isvisible() == true) {
			this.g = g;
			drawTweenManager(g);
			drawPerse(g, false);
			drawCamera(g);
			if (bg != null)
				bg.render(g);
			drawLight(g);
			g.pushMatrix();
			setMatrix(g);
			this.setTweenParameter(g.getGL());
			g.popMatrix();
			if (this instanceof Group) {
				update(g);
			}
		}
	}

	public void bufRender(Graphics g, double mouseX, double mouseY,
			boolean bool, int index) {
		if (this.isvisible() == true) {
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
			drawObject(g, bool, mouseX, mouseY, index, -1);
			g.popMatrix();
			if (this instanceof Group) {
				update(g);
			}
		}
	}

	private int bufRender(Graphics g, double mouseX, double mouseY,
			boolean bool, int index, int selectedIndex) {
		int sIndex = -1;
		if (this.isvisible() == true) {
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
		if (el.isvisible() == true) {
			if (el.isMasked() == true)
				el.getMask().render(g);
			g.pushMatrix();
			if (el.isTween() == true) {
				tmpAs = (int) el.gettAS();
				tmpAf = (int) el.gettAF();
				el.settAF((int) (tmpAf * this.getSceneFillColor().getA() / 255.0));
				el.settAS((int) (tmpAs * this.getSceneStrokeColor().getA() / 255.0));
				g.render(el);
				el.settAF(tmpAf);
				el.settAS(tmpAs);
			} else {
				tmpAf = el.getFillColor().getA();
				tmpAs = el.getStrokeColor().getA();
				el.getFillColor()
						.setA((int) (tmpAf * this.getSceneFillColor().getA() / 255.0));
				el.getStrokeColor()
						.setA((int) (tmpAs * this.getSceneStrokeColor().getA() / 255.0));
				g.render(el);
				el.getFillColor().setA(tmpAf);
				el.getStrokeColor().setA(tmpAs);
			}

			if (el.isMasked() == true)
				g.getGL().glDisable(GL.GL_STENCIL_TEST);
			g.popMatrix();
		}
	}

	private void drawTweenManager(Graphics g) {
		for (TweenManager tm : tmList)
			g.render(tm);
	}

	private int drawObject(Graphics g, boolean selection, double mouseX,
			double mouseY, int selectionIndex, int selectedIndex) {
		int sIndex = -1;
		for (Object obj : objectList) {
			if (obj instanceof GraphicsObject) {
				GraphicsObject o = (GraphicsObject) obj;
				if (selection == false) {
					if (((Element) o).getMask() != null) {
						((Element) o).getMask().render(g);
					}
					o.bufRender(g, mouseX, mouseY, false, selectionIndex);
					if (((Element) o).getMask() != null)
						g.getGL().glDisable(GL.GL_STENCIL_TEST);
				} else {
					sIndex = selectionIndex;
					selectionIndex = o.bufRender(g, mouseX, mouseY, true,
							selectionIndex, selectedIndex);
					for (int j = sIndex; j < selectionIndex; j++) {
						o.getSelectionList().add(j);

					}
					if (o.getMouseOverCallback() != null) {

						s = 0;
						for (int j = 0; j < o.getSelectionList().size(); j++) {
							if (o.getSelectionList().get(j) == selectedIndex) {
								s += 1;
							}
						}

						if (s > 0) {
							o.callMouseOverCallback(true);
						} else {
							o.setMouseover(false);
						}
						if (o.isMouseover() == false
								&& o.isPreMouseover() == true)
							o.callMouseOverCallback(false);
					}

				}
				o.setPreMouseover(o.isMouseover());
			} else if (obj instanceof TimelineRender) {
				TimelineRender tr = (TimelineRender) obj;
				if (selection == false)
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

	public void mouseObject(casmi.MouseEvent e) {
		for (Object obj : objectList) {
			if (obj instanceof Element) {
				Element el = (Element) obj;
				if (el.isMouseover() == true)
					el.callMouseClickCallback(e);
			}
		}
	}

	private void drawCamera(Graphics g) {
		for (Camera camera : cameraList) {
			if (camera instanceof Camera) {
				Camera c = (Camera) camera;
				c.render(g);
			}
		}
	}

	private void drawPerse(Graphics g, boolean selection) {
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

	private void drawLight(Graphics g) {
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

	public ArrayList<Integer> getSelectionList() {
		return selectionList;
	}

	public void setSelectionList(ArrayList<Integer> selectionList) {
		this.selectionList = selectionList;
	}

}
