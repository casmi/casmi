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
import casmi.Updatable;
import casmi.graphics.Graphics;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Element;
import casmi.graphics.element.Reset;
import casmi.graphics.element.Text;
import casmi.graphics.element.TextBox;
import casmi.graphics.group.Group;
import casmi.timeline.TimelineRender;
import casmi.tween.TweenManager;

public class GraphicsObjects  extends Element implements Updatable, ObjectRender {

protected Graphics g;
	
protected double tmpAs, tmpAf;
	
protected List<Object>       objectList;
protected List<Light>        lightList;
protected List<Camera>       cameraList;
protected List<Perse>        perseList;
protected List<TweenManager> tmList;
protected List<Integer> selectionList;
protected List<MouseEvent> mouseEventList;
	
	//private BackGround bg;

	protected enum MatrixMode {
		APPLY, LOAD, NONE
	};

	protected MatrixMode mode = MatrixMode.NONE;
	protected DoubleBuffer matrix;
	protected boolean selectionbuff = false;
	
	public final int NO_SELECTIONBUFF = 10;
	
	protected boolean removeObject;
	protected boolean resetObject = false;
	
	protected MouseEvent mouseEvent;
	
	protected int selectionBufSize = 1024*1024;
	
	public GraphicsObjects() {
		objectList    = new CopyOnWriteArrayList<Object>();
		lightList     = new CopyOnWriteArrayList<Light>();
		cameraList    = new CopyOnWriteArrayList<Camera>();
		perseList     = new CopyOnWriteArrayList<Perse>();
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
	
	public void clearAllObjects() {
		objectList = null;
	    objectList = new CopyOnWriteArrayList<Object>();
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
	public void applyMatrix(double[] matrix) {
		this.matrix = java.nio.DoubleBuffer.wrap(matrix);
		this.mode   = MatrixMode.APPLY;
	}

	public void applyMatrix(DoubleBuffer matrix) {
		this.matrix = matrix;
		this.mode   = MatrixMode.APPLY;
	}

	public void loadMatrix(double[] matrix) {
	    this.matrix = java.nio.DoubleBuffer.wrap(matrix);
		this.mode   = MatrixMode.LOAD;
	}

	public void loadMatrix(DoubleBuffer matrix) {
	    this.matrix = matrix;
	    this.mode   = MatrixMode.LOAD;
	}

	
	public double selectionbufRender(Graphics g, double mouseX, double mouseY, int index, int[] selectBuff, IntBuffer selectBuffer, int selectedIndex) {
		
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
			drawPerse(g, true);
			drawCamera(g);
			drawLight(g);
			g.pushMatrix();
			setMatrix(g);
			this.setTweenParameter(g.getGL());
			drawObject(g, true, mouseX, mouseY, index, selectedIndex);
			g.popMatrix();

			hits = g.getGL().glRenderMode(GL2.GL_RENDER);
			selectBuffer.get(selectBuff);
			selectedIndex = processHits(hits, selectBuff, selectedIndex);
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
		
		return selectedIndex;
	}



	private int processHits(int hits, int[] buffer, int selectedIndex) {
		if (hits > 0) {
			selectedIndex = buffer[4 * hits - 1];
		} else {
			selectedIndex = -1;
		}
		return selectedIndex;
	}

	@Override
	public void render(Graphics g) {
		if (this.isVisible()) {
			this.g = g;
			
			drawTweenManager(g);
			
			drawPerse(g, false);
			
			drawCamera(g);
			
			
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
			
			if(removeObject){
				for(Object obj : objectList){
					if(obj instanceof Element && ((Element) obj).isRemove())
						objectList.remove(obj);
				}
				removeObject = false;
			}
			
			drawTweenManager(g);
			
			if (!bool)
			    drawPerse(g, bool);

			drawCamera(g);
			
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

	protected int bufRender(Graphics g, double mouseX, double mouseY,
			boolean bool, int index, int selectedIndex) {
		int sIndex = -1;
		if (this.isVisible() == true) {
			this.g = g;
			drawTweenManager(g);
			if (bool == false)
				drawPerse(g, bool);

			drawCamera(g);
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
			
			if (el.getPosition().getZ()==0){
				el.setDepthTest(false);
			} else {
				this.setDepthTest(true);
			}
			
			//if(this.isDepthTest())
			//el.setDepthTest(false);
			
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
			if (obj instanceof GraphicsObjects) {
				GraphicsObjects o = (GraphicsObjects)obj;
				if (!selection) {
					if(o.isRemove())
						removeObject = true;
					if (((Element) o).getMask() != null) {
						((Element) o).getMask().render(g);
					}
					if (o.getMouseOverCallback() != null) {
						selectionbuff = true;
					}
					o.bufRender(g, mouseX, mouseY, false, selectionIndex);
					if(o.isSelectionbuff()==true)
						selectionbuff = true;
					if (((Element) o).getMask() != null)
						g.getGL().glDisable(GL2.GL_STENCIL_TEST);
				} else {
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
				if (!selection)
					tm.render(g);
			} else {
				Element e = (Element) obj;
				if (!selection) {
					if (e.isRemove())
						removeObject = true;
					if (e.isReset()){
						resetObject = true;
						e.setReset(false);
					}
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
						//	if(mouseEventList.size()!=0){
								e.callMouseClickCallback(mouseEvent);
						//		e.callMouseClickCallback(mouseEventList.get(0));
						//		mouseEventList.remove(0);
						//	}
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
		return selectionIndex;
	}

	public void callMouseClickCallbackOfChildren(casmi.MouseEvent e) {
		for (Object obj : objectList) {
			if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
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
	//	if(mouseEventList==null)
	//		mouseEventList = new CopyOnWriteArrayList<MouseEvent>();
	//	if(e!=null&&(e!=mouseEvent||mouseEventList.size()==0))
	//		mouseEventList.add(e);
		mouseEvent = e;
		for (Object obj : objectList) {
			if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects) obj;
				go.setMouseEvent(mouseEvent);
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

	@Override
	public void update() {

	}
	
	public List<Object> getObjectList() {
		return objectList;
	}

	@Override
	public void render(GL2 gl, GLU glu, int width, int height) {
	}

	public boolean isSelectionbuff() {
		return selectionbuff;
	}

	public void setSelectionbuff(boolean selectionbuff) {
		this.selectionbuff = selectionbuff;
	}

	public int getSelectionbuffsize() {
		return selectionBufSize;
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

	public boolean isResetObject() {
		for (Object obj : objectList) {
			if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
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
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
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
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
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
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
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
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
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
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
				go.setStrokeColorAlpha(alpha);
			}
		}
	}

	/**
	 * Sets the color of this Element's stroke.
	 * 
	 * @param strokeColor
	 *            The color of the Element's stroke.
	 */
	@Override
	public void setStrokeColor(ColorSet colorSet) {
		strokeColor = new RGBColor(colorSet);
		for (Object obj : objectList) {
			if (obj instanceof Element) {
				Element el = (Element)obj;
				el.setStrokeColor(colorSet);
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
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
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
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
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
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
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
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
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
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
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
				go.setFillColor(colorSet, alpha);
			}
		}
	}
	
	public void resetObjects() {
		System.out.println("fff");
		for (Object obj : objectList) {
			if (obj instanceof Reset) {
				Reset el = (Reset)obj;
				el.reset();
			} else if (obj instanceof GraphicsObjects) {
				GraphicsObjects go = (GraphicsObjects)obj;
				go.resetObjects();
			}
		}
	}
}


