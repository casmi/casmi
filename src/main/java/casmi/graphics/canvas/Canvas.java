package casmi.graphics.canvas;

import java.nio.DoubleBuffer;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import casmi.MouseStatus;
import casmi.graphics.Graphics;
import casmi.graphics.element.Element;
import casmi.graphics.object.Camera;
import casmi.graphics.object.Light;
import casmi.graphics.object.Projection;
import casmi.graphics.object.Resettable;

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

    protected enum ObjectMatrixMode {
        APPLY,
        LOAD,
        NONE
    };

    protected ObjectMatrixMode mode = ObjectMatrixMode.NONE;

    protected DoubleBuffer matrix;

	protected MouseStatus mouseStatus = MouseStatus.RELEASED;

	private double x = 0.0, y = 0.0, z = 0.0;

	public Canvas() {
		elementList = new CopyOnWriteArrayList<Element>();
		lights = new CopyOnWriteArrayList<Light>();
		camera = null;
		projection = null;
	}

	public void add(Element e) {
	    this.elementList.add(e);
	}

	public synchronized void addAll(Collection<? extends Element> c) {
	    this.elementList.addAll(c);
	}

	public synchronized void addLight(Light l) {
		l.setIndex(lights.size());
		this.lights.add(l);
	}

	public synchronized void setCamera(Camera c) {
		this.camera = c;
	}

	public synchronized void setProjection(Projection p) {
		this.projection = p;
	}

	public synchronized void remove(Element e) {
		elementList.remove(e);
	}

	public synchronized void removeLight(Light l) {
		lights.remove(l);
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

	public synchronized void add(int index, Element r) {
		elementList.add(index, r);
	}

	public synchronized void addLight(int index, Light r) {
		lights.add(index, r);
	}

	public synchronized void clear() {
		elementList.clear();
	}

	public synchronized void clearLight() {
		lights.clear();
	}

	public synchronized void applyMatrix(double[] matrix) {
		this.matrix = java.nio.DoubleBuffer.wrap(matrix);
		this.mode   = ObjectMatrixMode.APPLY;
	}

	public synchronized void applyMatrix(DoubleBuffer matrix) {
		this.matrix = matrix;
		this.mode   = ObjectMatrixMode.APPLY;
	}

	public synchronized void loadMatrix(double[] matrix) {
	    this.matrix = java.nio.DoubleBuffer.wrap(matrix);
		this.mode   = ObjectMatrixMode.LOAD;
	}

	public synchronized  void loadMatrix(DoubleBuffer matrix) {
	    this.matrix = matrix;
	    this.mode   = ObjectMatrixMode.LOAD;
	}

	protected synchronized void renderAll(Graphics g) {
// TODO
//	    if (removeObject) {
//	        for (Object obj : elementList) {
//	            if(obj instanceof Element && ((Element)obj).isRemove())
//	                elementList.remove(obj);
//	        }
//	        removeObject = false;
//	    }

	    setupProjection(g, false);
	    setupCamera(g);
	    setupLight(g);

	    g.pushMatrix();
	    {
	        loadMatrix(g);
	        g.translate(x, y, z);
	        renderAllElements(g, false, 0, 0, 0);
	    }
	    g.popMatrix();
	}

	protected synchronized int renderAllForSelection(Graphics g, double mouseX, double mouseY, int beginIndex) {
	    int lastIndex;

//		renderTweenManager(g);

		g.resetMatrix();
		setupProjection(g, true);
		setupCamera(g);
		setupLight(g);

		g.pushMatrix();
		{
		    loadMatrix(g);
		    g.translate(x, y, z);
		    lastIndex = renderAllElements(g, true, mouseX, mouseY, beginIndex);
		}
		g.popMatrix();

		return lastIndex;
	}

	private static final void renderElement(Graphics g, Element e, boolean selection) {
		if (e.isVisible()) {
//			if (e.isMasked()) {
//				e.getMask().render(g);
//			}

			g.pushMatrix();
			{
                g.render(e, selection);

//			    if (e.isMasked()) {
//			        g.getGL().glDisable(GL2.GL_STENCIL_TEST);
//			    }
			}
			g.popMatrix();
		}
	}

	private final int renderAllElements(Graphics g, boolean selection, double mouseX, double mouseY, int beginIndex) {
	    int index = beginIndex;
	    for (Element e : elementList) {
	        if (selection) {
	            if (e.getMouseEventCallbacks() != null && e.getMouseEventCallbacks().size() > 0) {
	                g.getGL().glLoadName(index);

	                index++;

	                renderElement(g, e, true);
	            }
	        } else {
// TODO
//	            if (e.isReset()) {
//	                resetObject = true;
//	                e.setReset(false);
//	            }

	            renderElement(g, e, false);
	        }
	    }

		return index;
	}

	public void setMouseStatus(MouseStatus status){
		this.mouseStatus = status;
	}

	private final void setupCamera(Graphics g) {
	    if (this.camera != null) {
	        this.camera.render(g);
	    }
	}

	private final void setupProjection(Graphics g, boolean selection) {
	    if (this.projection == null) {
	        if (selection) {
	            g.setJustOrtho();
	        } else {
	            g.setOrtho();
	        }
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

	private void loadMatrix(Graphics g) {
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

//	public boolean isResetObject() {
//		for (Object obj : elementList) {
//			if (obj instanceof Canvas) {
//				Canvas go = (Canvas)obj;
//				if(go.isResetObject()){
//					resetObject = true;
//					go.setResetObject(false);
//				}
//			}
//		}
//		return resetObject;
//	}
//
//	public void setResetObject(boolean resetObject) {
//		this.resetObject = resetObject;
//	}

	protected synchronized void resetObjects(Graphics g) {
		for (Element e : elementList) {
		    if (e instanceof Resettable) {
		        ((Resettable) e).reset(g.getGL());
		    }
		}
	}

	public synchronized int triggerMouseEvent(int selectedIndex, int beginIndex) {
        int index = beginIndex;
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

        return index;
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


