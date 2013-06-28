package casmi.graphics.object;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import casmi.MouseEvent;
import casmi.Updatable;
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

/**
 * Graphics Object
 *
 * @author Y. Ban
 * @author Takashi AOKI <federkasten@me.com>
 */
public class GraphicsObject extends Element implements Updatable, ObjectRender {

    protected Graphics g;

    protected double tmpAs, tmpAf;

    protected List<Object> objectList;

    protected List<Light> lights;
    protected List<Camera> cameras;
    protected List<Projection> projections;

    protected List<TweenManager> tmList;
    protected List<Integer> selectionList;
    protected List<MouseEvent> mouseEventList;

    protected enum ObjectMatrixMode {
        APPLY, LOAD, NONE
    };

    protected ObjectMatrixMode mode = ObjectMatrixMode.NONE;
    protected DoubleBuffer matrix;
    protected boolean selectionbuff = false;

	public final int NO_SELECTIONBUFF = 10;

	protected boolean removeObject;
	protected boolean resetObject = false;

	protected MouseEvent mouseEvent;
	protected int selectionBufSize = 1024*1024;

	public GraphicsObject() {
		objectList    = new CopyOnWriteArrayList<Object>();
		lights     = new CopyOnWriteArrayList<Light>();
		cameras    = new CopyOnWriteArrayList<Camera>();
		projections     = new CopyOnWriteArrayList<Projection>();
		tmList        = new CopyOnWriteArrayList<TweenManager>();
		selectionList = new CopyOnWriteArrayList<Integer>();

		this.setDepthTest(false);
	}

	public void add(Object object) {
	    if (object instanceof Element || object instanceof Group || object instanceof TimelineRender)
	        objectList.add(object);
	}

	public void addAll(Collection<? extends Object> c) {
	    objectList.addAll(c);
	}

	public void addLight(Light r) {
		r.setIndex(lights.size());
		lights.add(r);
	}

	public void addCamera(Camera r) {
		cameras.add(r);
	}

	public void addProjection(Projection r) {
		projections.add(r);
	}

	public void addTweenManager(TweenManager r) {
		tmList.add(r);
	}

	public void clearAllObjects() {
		objectList = null;
	    objectList = new CopyOnWriteArrayList<Object>();
	}

	public void remove(int index) {
		objectList.remove(index);
	}

	public void removeLight(int index) {
		lights.remove(index);
	}

	public void removeCamera(int index) {
		cameras.remove(index);
	}

	public void removePerse(int index) {
		projections.remove(index);
	}

	public void removeTweenManager(int index) {
		tmList.remove(index);
	}

	public Object get(int index) {
		return objectList.get(index);
	}

	public Object getLight(int index) {
		return lights.get(index);
	}

	public Object getCamera(int index) {
		return cameras.get(index);
	}

	public Object getProjection(int index) {
		return projections.get(index);
	}

	public TweenManager getTweenManager(int index) {
		return tmList.get(index);
	}

	public void add(int index, Object r) {
		objectList.add(index, r);
	}

	public void addLight(int index, Light r) {
		lights.add(index, r);
	}

	public void addCamera(int index, Camera r) {
		cameras.add(index, r);
	}

	public void addProjection(int index, Projection r) {
		projections.add(index, r);
	}

	public void clear() {
		objectList.clear();
	}

	public void clearLight() {
		lights.clear();
	}

	public void clearCamera() {
		cameras.clear();
	}

	public void clearPerse() {
		projections.clear();
	}

	public void clearTweenManager() {
		tmList.clear();
	}

	/**
	 * Applies the transformation matrix.
	 */
	public void applyMatrix(double[] matrix) {
		this.matrix = java.nio.DoubleBuffer.wrap(matrix);
		this.mode   = ObjectMatrixMode.APPLY;
	}

	public void applyMatrix(DoubleBuffer matrix) {
		this.matrix = matrix;
		this.mode   = ObjectMatrixMode.APPLY;
	}

	public void loadMatrix(double[] matrix) {
	    this.matrix = java.nio.DoubleBuffer.wrap(matrix);
		this.mode   = ObjectMatrixMode.LOAD;
	}

	public void loadMatrix(DoubleBuffer matrix) {
	    this.matrix = matrix;
	    this.mode   = ObjectMatrixMode.LOAD;
	}

	@Override
	public void render(Graphics g) {}
//	public void render(Graphics g) {
//		if (this.isVisible()) {
//			this.g = g;
//
//			drawTweenManager(g);
//
//			drawPerse(g, false);
//
//			drawCamera(g);
//
//			drawLight(g);
//
//			g.pushMatrix();
//			{
//			    setMatrix(g);
//			    this.move(g.getGL());
//			}
//			g.popMatrix();
//
//			if (this instanceof Group) {
//				update();
//			}
//		}
//	}

	public void renderAll(Graphics g, double mouseX, double mouseY, boolean bool, int index) {
		if (this.isVisible()) {
			this.g = g;

			if (removeObject) {
				for (Object obj : objectList) {
					if(obj instanceof Element && ((Element)obj).isRemove())
						objectList.remove(obj);
				}
				removeObject = false;
			}

			drawTweenManager(g);

			if (!bool)
			    drawProjection(g, bool);

			drawCamera(g);

			drawLight(g);

			if (!this.isUseProjection()) {
			    g.matrixMode(MatrixMode.PROJECTION);
                g.pushMatrix();
                g.resetMatrix();
                g.simpleOrtho();
                g.matrixMode(MatrixMode.MODELVIEW);
			}

			g.pushMatrix();
			{
			    loadMatrix(g);
			    this.move(g.getGL());
			    drawObject(g, bool, mouseX, mouseY, index, -1);
			}
			g.popMatrix();

			if (!this.isUseProjection()) {
//              g.matrixMode(MatrixMode.MODELVIEW);
//              g.popMatrix();
              g.matrixMode(MatrixMode.PROJECTION);
              g.popMatrix();
              g.matrixMode(MatrixMode.MODELVIEW);
          }

			if (this instanceof Group) {
				update(g);
			}


		}
	}

	protected int renderSelectionAll(Graphics g, double mouseX, double mouseY, boolean bool, int index, int selectedIndex) {
		int sIndex = -1;
		if (this.isVisible() == true) {
			this.g = g;
			drawTweenManager(g);
			if (bool == false)
				drawProjection(g, bool);
			drawCamera(g);
			drawLight(g);
			g.pushMatrix();
			loadMatrix(g);
			this.move(g.getGL());
			sIndex = drawObject(g, bool, mouseX, mouseY, index, selectedIndex);
			g.popMatrix();
			if (this instanceof Group) {
				update(g);
			}
		}

		return sIndex;
	}

	public void render(Element e) {
		if (e.isVisible()) {
			if (e.isMasked()) {
				e.getMask().render(g);
			}

			if (e.getPosition().getZ() == 0) {
				e.setDepthTest(false);
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
					if (o.isRemove())
						removeObject = true;
					if (((Element) o).isMasked()) {
						((Element) o).getMask().render(g);
					}
					if (o.getMouseOverCallback() != null) {
						selectionbuff = true;
					}
					o.renderAll(g, mouseX, mouseY, false, selectionIndex);
					if (o.isSelectionbuff())
						selectionbuff = true;
					if (((Element)o).isMasked())
						g.getGL().glDisable(GL2.GL_STENCIL_TEST);
				} else {
					selectionIndex = o.renderSelectionAll(g, mouseX, mouseY, true, selectionIndex, selectedIndex);
				}
				o.setPrevMouseover(o.isMouseover());
			} else if (obj instanceof TimelineRender) {
				TimelineRender tr = (TimelineRender) obj;
				tr.render(g);
			} else if (obj instanceof TweenManager) {
				TweenManager tm = (TweenManager) obj;
				if (!selection)
					tm.render(g);
			} else {
				Element e = (Element)obj;
				if (!selection) {
					if (e.isRemove())
						removeObject = true;
					if (e.isReset()) {
						resetObject = true;
						e.setReset(false);
					}
					if (e.getMouseOverCallback() != null) {
						selectionbuff = true;
					}
					this.render((Element)obj);
				} else {
				    if (e.getMouseOverCallback() != null) {
				        g.getGL().glLoadName(selectionIndex);
				        if (e instanceof Text) {
				            ((Text) e).setSelection(true);
				        }
				        if (selectionIndex == selectedIndex) {
				            e.callMouseOverCallback(true);
				            e.callMouseClickCallback(mouseEvent);
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
				e.setPrevMouseover(e.isMouseover());
			}
		}
		return selectionIndex;
	}

	public void callMouseClickCallbackOfChildren(casmi.MouseEvent e) {
		for (Object obj : objectList) {
			if (obj instanceof GraphicsObject) {
				GraphicsObject go = (GraphicsObject)obj;
				go.callMouseClickCallbackOfChildren(e);
			}
			else if (obj instanceof Element) {
				Element el = (Element)obj;
				if (el.isMouseover()) {
					el.callMouseClickCallback(e);
				}
			}
		}
	}

	public void setMouseEvent(casmi.MouseEvent e){
		mouseEvent = e;
		for (Object obj : objectList) {
			if (obj instanceof GraphicsObject) {
				GraphicsObject go = (GraphicsObject) obj;
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
		for (Projection perse : projections) {
			if (perse instanceof Perspective) {
				Perspective perspective = (Perspective) perse;
				if (selection == false)
					perspective.render(g);
				else
					perspective.renderForSelection(g);
			} else if (perse instanceof Ortho) {
				Ortho ortho = (Ortho) perse;
				if (selection == false)
					ortho.render(g);
				else
					ortho.renderForSelection(g);
			} else if (perse instanceof Frustum) {
				Frustum frustum = (Frustum) perse;
				if (selection == false)
					frustum.render(g);
				else
					frustum.renderForSelection(g);
			}
		}
		if (selection == true && projections.size() == 0) {
			g.simpleOrtho();
		}
	}

	private final void drawLight(Graphics g) {
		for (Light light : lights)
			light.render(g);
	}

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

	public int getSize() {
		return objectList.size();
	}

	public void update(Graphics g) {
		update();
	}

	@Override
	public void update() {}

	public List<Object> getObjectList() {
		return objectList;
	}

	@Override
	public void render(GL2 gl, GLU glu, int width, int height) {}

	public boolean isSelectionbuff() {
		return selectionbuff;
	}

	public void setSelectionbuff(boolean selectionbuff) {
		this.selectionbuff = selectionbuff;
	}

	public List<Integer> getSelectionList() {
		return selectionList;
	}

	public void clearSelections() {
		selectionList.clear();
	}

	public void setSelectionList(ArrayList<Integer> selectionList) {
		this.selectionList = selectionList;
	}

	public boolean isResetObject() {
		for (Object obj : objectList) {
			if (obj instanceof GraphicsObject) {
				GraphicsObject go = (GraphicsObject)obj;
				if(go.isResetObject()){
					resetObject = true;
					go.setResetObject(false);
				}
			}
		}
		return resetObject;
	}

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
	 * @param strokeWidth
	 *            The width of the Element's stroke.
	 */
	@Override
	public void setStrokeWidth(double strokeWidth) {
		this.strokeWidth = (float) strokeWidth;
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
	 * @param color
	 *            The color of the Element's stroke.
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
	 * @param colorSet
	 *            The color of the Element's stroke.
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
	 * @param color
	 *            The color of the Element's fill.
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
	 * @param colorSet
	 *            The color of the Element's fill.
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

	public void resetObjects() {
		for (Object obj : objectList) {
			if (obj instanceof Reset) {
				Reset el = (Reset)obj;
				if (g != null)
				    el.reset(g.getGL());
			} else if (obj instanceof GraphicsObject) {
				GraphicsObject go = (GraphicsObject)obj;
				go.resetObjects();
			}
		}
	}

	@Override
    public void setMask(Mask mask) {
		for (Object obj : objectList) {
			if (obj instanceof Element) {
				Element el = (Element)obj;
				el.setMask(mask);
			} else if (obj instanceof GraphicsObject) {
				GraphicsObject go = (GraphicsObject)obj;
				go.setMask(mask);
			}
		}
	}

	@Override
    public void enableMask() {
		for (Object obj : objectList) {
			if (obj instanceof Element) {
				Element el = (Element)obj;
				el.enableMask();
			} else if (obj instanceof GraphicsObject) {
				GraphicsObject go = (GraphicsObject)obj;
				go.enableMask();
			}
		}
	}

	@Override
    public void disableMask() {
		for (Object obj : objectList) {
			if (obj instanceof Element) {
				Element el = (Element)obj;
				el.disableMask();
			} else if (obj instanceof GraphicsObject) {
				GraphicsObject go = (GraphicsObject)obj;
				go.disableMask();
			}
		}
	}
}


