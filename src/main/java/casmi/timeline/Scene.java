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

package casmi.timeline;

import javax.media.opengl.GL2;

import casmi.Keyboard;
import casmi.Mouse;
import casmi.MouseButton;
import casmi.PopupMenu;
import casmi.graphics.Graphics;
import casmi.graphics.element.Reset;
import casmi.graphics.object.GraphicsObject;
import casmi.graphics.object.RootObject;

/**
 * Scene class for time line animation.
 * 
 * @author Y. Ban
 */
abstract public class Scene extends RootObject {

    private String idName;
    private double time;
    private double sceneA = 0.0;
    private Timeline rootTimeline;

    public Scene(String id) {
    	super();
    	setIdName(id);
    	setTime(0);
    }
    
    public Scene(String id, double time) {
    	super();
    	setIdName(id);
    	setTime(time);
    }
    
    public void setRootTimeline(Timeline timeline) {
    	this.rootTimeline = timeline;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String id) {
        this.idName = id;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
    

    public double getSceneA() {
        return sceneA;
    }

    public void setSceneA(double sceneA, Graphics g) {
        this.sceneA = sceneA;
        g.setSceneA(this.sceneA);
    }

    public void drawscene(Graphics g) {
    	this.clearSelectionList();
    //	this.bufRender(g, getMouseX(), getMouseY(), false, 0);
    	this.rootBufRender(g, getMouseX(), getMouseY(), false,0);
    	if(!this.rootTimeline.isNowDissolve())
    		this.rootSelectionbufRender(g, getMouseX(), getMouseY(), 0);
    	update(g);
    }

    public void update(Graphics g) {
        update();
    }

    abstract public void update();
    

    public int addObject(Object r) {
       this.add(r);
        return 0;
    }

    public void removeObject(int index) {
        this.remove(index);
    }

    public int addObject(int index, Object r) {
    	return this.addObject(index, r);
    }

    public Object getObject(int index) {
        return this.get(index);
    }

    public void clearObject() {
        this.clear();
    }
	
	public void reset(GL2 gl){
		for (Object obj : this.getObjectList()) {
			if (obj instanceof Reset) {
				Reset el = (Reset)obj;
				el.reset(gl);
			} else if (obj instanceof GraphicsObject) {
				GraphicsObject go = (GraphicsObject)obj;
				go.resetObjects();
			}
		}
	}
	
	
	

	public int getMouseWheelRotation() {
		return rootTimeline.getMouse().getWheelRotation();
	}
	
	public Mouse getMouse() {
	    return rootTimeline.getMouse();
	}

	public int getPreMouseX() {
		return rootTimeline.getMouse().getPrvX();
	}

	public int getPreMouseY() {
		return rootTimeline.getMouse().getPrvY();
	}

	public int getMouseX() {
		return rootTimeline.getMouse().getX();
	}

	public int getMouseY() {
		return rootTimeline.getMouse().getY();
	}

	public boolean isMousePressed() {
		return rootTimeline.getMouse().isPressed();
	}
	
	public boolean isMousePressed(MouseButton button) {
	    return rootTimeline.getMouse().isButtonPressed(button);
	}

	public boolean isMouseClicked() {
		return rootTimeline.getMouse().isClicked();
	}

	public boolean isMouseEntered() {
		return rootTimeline.getMouse().isEntered();
	}

	public boolean isMouseExited() {
		return rootTimeline.getMouse().isExited();
	}

	public boolean isMouseReleased() {
		return rootTimeline.getMouse().isReleased();
	}
	
	public Keyboard getKeyboard() {
	    return rootTimeline.getKeyboard();
	}
	
	public char getKey() {
		return rootTimeline.getKeyboard().getKey();
	}
	
	public int getKeyCode() {
	    return rootTimeline.getKeyboard().getKeyCode();
	}
	
	public boolean isKeyPressed() {
		return rootTimeline.getKeyboard().isPressed();
	}

	public boolean isKeyReleased() {
		return rootTimeline.getKeyboard().isReleased();
	}

	public boolean isKeyTyped() {
		return rootTimeline.getKeyboard().isTyped();
	}
	
	// PopupMenu
	
	public PopupMenu getPopupMenu() {
	    return rootTimeline.getPopup();
	}
	
    abstract public void keyEvent(casmi.KeyEvent e);

    abstract public void mouseEvent(casmi.MouseEvent e,  MouseButton b);

}
