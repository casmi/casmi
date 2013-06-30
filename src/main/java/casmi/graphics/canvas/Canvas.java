package casmi.graphics.canvas;

import java.nio.DoubleBuffer;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.media.opengl.GL2;

import casmi.MouseStatus;
import casmi.graphics.Graphics;
import casmi.graphics.element.Element;
import casmi.graphics.object.Camera;
import casmi.graphics.object.Light;
import casmi.graphics.object.Projection;
import casmi.graphics.object.Resettable;
import casmi.tween.TweenerManager;

/**
 * Graphics Object
 *
 * @author Y. Ban
 * @author Takashi AOKI <federkasten@me.com>
 */
public class Canvas {

    protected List<Element> elementList;

    protected List<Light> lights;
    protected Camera camera;
    protected Projection projection;

    protected TweenerManager tweenerManager;

    protected enum ObjectMatrixMode {
        APPLY,
        LOAD,
        NONE
    };

    protected ObjectMatrixMode mode = ObjectMatrixMode.NONE;

    protected DoubleBuffer matrix;

	protected boolean removeObject = false;
	protected boolean resetObject = false;

	protected MouseStatus mouseStatus;

	private double x = 0.0, y = 0.0, z = 0.0;

	public Canvas() {
		elementList = new CopyOnWriteArrayList<Element>();
		lights = new CopyOnWriteArrayList<Light>();
		camera = null;
		projection = null;
		tweenerManager = null;
	}

	public void add(Element e) {
	    this.elementList.add(e);
	}

	public void addAll(Collection<? extends Element> c) {
	    this.elementList.addAll(c);
	}

	public void addLight(Light l) {
		l.setIndex(lights.size());
		this.lights.add(l);
	}

	public void setCamera(Camera c) {
		this.camera = c;
	}

	public void addProjection(Projection p) {
		this.projection = p;
	}

	public void setTweenManager(TweenerManager tweenerManager) {
	    this.tweenerManager = tweenerManager;
	}

	public void clearAllObjects() {
		elementList = null;
	    elementList = new CopyOnWriteArrayList<Element>();
	}

	public void remove(int index) {
		elementList.remove(index);
	}

	public void removeLight(int index) {
		lights.remove(index);
	}

	public Element get(int index) {
		return elementList.get(index);
	}

	public Camera getCamera(int index) {
		return this.camera;
	}

	public Projection getProjection(int index) {
		return this.projection;
	}

	public TweenerManager getTweenManager() {
		return tweenerManager;
	}

	public void add(int index, Element r) {
		elementList.add(index, r);
	}

	public void addLight(int index, Light r) {
		lights.add(index, r);
	}

	public void clear() {
		elementList.clear();
	}

	public void clearLight() {
		lights.clear();
	}

	public void clearCamera() {
		this.camera = null;
	}

	public void clearPerse() {
		this.projection = null;
	}

	public void clearTweenManager() {
	    tweenerManager = null;
	}

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

	protected void renderAll(Graphics g) {
	    if (removeObject) {
	        for (Object obj : elementList) {
	            if(obj instanceof Element && ((Element)obj).isRemove())
	                elementList.remove(obj);
	        }
	        removeObject = false;
	    }

	    renderTweenManager(g);

	    setupProjection(g, false);

	    setupCamera(g);

	    setupLight(g);

// TODO
//			if (!this.isUseProjection()) {
//			    g.matrixMode(MatrixMode.PROJECTION);
//                g.pushMatrix();
//                g.resetMatrix();
//                g.setJustOrtho();
//                g.matrixMode(MatrixMode.MODELVIEW);
//			}

	    g.pushMatrix();
	    {
	        loadMatrix(g);
	        g.translate(x, y, z);
	        renderAllElements(g, false, 0, 0, 0);
	    }
	    g.popMatrix();

// TODO
//			if (!this.isUseProjection()) {
//              g.matrixMode(MatrixMode.PROJECTION);
//              g.popMatrix();
//              g.matrixMode(MatrixMode.MODELVIEW);
//          }
	}

	protected int renderAllForSelection(Graphics g, double mouseX, double mouseY, int beginIndex) {
	    int lastIndex = beginIndex;

		renderTweenManager(g);

		setupProjection(g, true);
		setupCamera(g);
		setupLight(g);

// TODO
//      if (!this.isUseProjection()) {
//          g.matrixMode(MatrixMode.PROJECTION);
//            g.pushMatrix();
//            g.resetMatrix();
//            g.setJustOrtho();
//            g.matrixMode(MatrixMode.MODELVIEW);
//      }

		g.pushMatrix();
		{
		    loadMatrix(g);
		    g.translate(x, y, z);
		    lastIndex = renderAllElements(g, true, mouseX, mouseY, beginIndex);
		}
		g.popMatrix();

// TODO
//      if (!this.isUseProjection()) {
//          g.matrixMode(MatrixMode.PROJECTION);
//          g.popMatrix();
//          g.matrixMode(MatrixMode.MODELVIEW);
//      }

		return lastIndex;
	}

	protected final void renderTweenManager(Graphics g) {
	    g.render(tweenerManager);
	}

	private static final void renderElement(Graphics g, Element e, boolean selection) {
		if (e.isVisible()) {
			if (e.isMasked()) {
				e.getMask().render(g);
			}

			g.pushMatrix();
			{
                g.render(e, selection);

			    if (e.isMasked()) {
			        g.getGL().glDisable(GL2.GL_STENCIL_TEST);
			    }
			}
			g.popMatrix();
		}
	}

	private final int renderAllElements(Graphics g, boolean selection, double mouseX, double mouseY, int beginIndex) {
	    int selectionIndex = beginIndex;
	    for (Element e : elementList) {
	        if (selection) {
	            if (e.getMouseEventCallbacks() != null) {
	                g.getGL().glLoadName(selectionIndex);

	                selectionIndex++;

	                renderElement(g, e, true);
	            }
	        } else {
	            if (e.isRemove()) {
	                removeObject = true;
	            }

	            if (e.isReset()) {
	                resetObject = true;
	                e.setReset(false);
	            }

	            renderElement(g, e, false);
	        }
	    }

		return selectionIndex;
	}

	public void setMouseEvent(casmi.MouseStatus e){
		mouseStatus = e;
		for (Object obj : elementList) {
			if (obj instanceof Canvas) {
				Canvas go = (Canvas) obj;
				go.setMouseEvent(mouseStatus);
			}
		}
	}

	private final void setupCamera(Graphics g) {
	    if (this.camera != null) {
	        this.camera.render(g);
	    }
	}

	private final void setupProjection(Graphics g, boolean selection) {
	    if (this.projection == null) {
	        if (selection) g.setJustOrtho();
	    } else {
	        if (selection) {
	            this.projection.projectForSelection(g);
	        } else {
	            this.projection.project(g);
	        }
	    }
	}

	private final void setupLight(Graphics g) {
		for (Light light : lights) {
			light.render(g);
		}
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
		return elementList.size();
	}

	public boolean isResetObject() {
		for (Object obj : elementList) {
			if (obj instanceof Canvas) {
				Canvas go = (Canvas)obj;
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

	public void resetObjects(Graphics g) {
		for (Element e : elementList) {
		    if (e instanceof Resettable) {
		        ((Resettable) e).reset(g.getGL());
		    }
		}
	}

	public void triggerMouseEvent(int selectedIndex) {
        int index = 0;
        for (Element e : elementList) {
            if (e.getMouseEventCallbacks() != null && e.getMouseEventCallbacks().size() > 0) {
                if (index == selectedIndex) {
                    e.triggerMouseEvent(mouseStatus, true);
                } else {
                    e.triggerMouseEvent(mouseStatus, false);
                }

                index ++;
            }
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    /**Sets the position of the Element in 2D.
    *
    * @param x
    *              x-coordinate
    * @param y
    *              y-coordinate
    */
   public void setPosition(double x, double y) {
       this.x = x;
       this.y = y;
   }

   /**Sets the position of the Element in 3D.
    *
    * @param x
    *              x-coordinate
    * @param y
    *              y-coordinate
    * @param z
    *              z-coordinate
    */
   public void setPosition(double x, double y, double z) {
       this.x = x;
       this.y = y;
       this.z = z;
   }
}


