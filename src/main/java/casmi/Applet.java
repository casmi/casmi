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

package casmi;

import java.awt.AWTEvent;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JApplet;

import casmi.graphics.Graphics;
import casmi.util.OS;
import casmi.util.SystemUtil;

import com.sun.opengl.util.GLUT;

/**
 * casmi Applet
 * 
 * @author takashi
 * 
 */

abstract public class Applet extends JApplet implements GraphicsDrawable, MouseListener, MouseMotionListener, KeyListener {

	private static final int PRESSED = 0;
	private static final int CLICKED = 1;
	private static final int ENTERED = 2;
	private static final int EXITED = 3;
	private static final int RELEASED = 4;
	private static final int DRAGGED = 5;
	private static final int MOVED = 6;

	private int mouseX, mouseY;
	private int preMouseX, preMouseY;
	private char key;
	private double fps = 30.0;
	
	public int width = 100, height = 100;
	
	private Cursor cursormode[] = new Cursor[5];

	public enum CursorMode {
        DEFAULT, CROSS, HAND, MOVE, TEXT, WAIT
    }
	
	private GLJPanel panel = null;
	private AppletGLEventListener listener = null;

	private Timer timer;



	private boolean mousePressed = false;
	private boolean mouseClicked = false;
	private boolean mouseEntered = false;
	private boolean mouseExited = false;
	private boolean mouseReleased = false;
	private boolean mouseDragged = false;
	private boolean mouseMoved = false;
	private boolean keypressed = false;

	private boolean runAsApplication = false;
	
    // TODO need to refactoring
	/*
	 * Set system property "java.library.path" programmatically. This static
	 * scope is required to load native libraries of JOGL. This is called
	 * initially once.
	 */
	static {
		String defaultPath = System.getProperty("java.library.path");
		String newPath = "../../casmi/lib/native/";
		newPath = newPath.replaceAll("/", java.io.File.separator
				+ java.io.File.separator);

		OS os = SystemUtil.getOS();
		switch (os) {
		case MAC:
			newPath += "mac";
			break;
		case MAC_64:
			newPath += "mac";
			break;
		case WIN:
			newPath += "win";
			break;
		case WIN_64:
			newPath += "win_64";
			break;
		case LINUX:
			newPath += "linux";
			break;
		case LINUX_64:
			newPath += "linux_64";
			break;
		default:
			break;
		}

		System.setProperty("java.library.path", defaultPath
				+ java.io.File.pathSeparatorChar + newPath);

		try {
			Field fieldSysPath = ClassLoader.class
					.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println(System.getProperty("java.library.path"));
	}

	class GLRedisplayTask extends TimerTask {
		@Override
		public void run() {
			if (panel != null) { // TODO if no update, do not re-render
				panel.display();
				mousePressed = false;
				mouseClicked = false;
				mouseEntered = false;
				mouseExited = false;
				mouseReleased = false;
				mouseDragged = false;
				mouseMoved = false;
				keypressed = false;
			}
		}
	}

	@Override
	public void init() {
		this.setup();

		// JOGL setup
		this.panel = new GLJPanel();
		listener = new AppletGLEventListener(this, width, height);
		panel.addGLEventListener(listener);
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		panel.addKeyListener(this);
	//	this.addComponentListener();
		setFocusable(true);
		initCursor();

		this.add(panel);

		timer = new Timer();
		timer.schedule(new GLRedisplayTask(), 0, (long)(1000.0/fps));
	}

	public void setSize(int w, int h) {
		width = w;
		height = h;
		super.setSize(new Dimension(w, h));

		if (panel != null) {
			panel.setSize(new Dimension(w, h));
		}
	}
	
	private void initCursor() {
		cursormode[0] = new Cursor(Cursor.DEFAULT_CURSOR);
		cursormode[1] = new Cursor(Cursor.CROSSHAIR_CURSOR);
		cursormode[2] = new Cursor(Cursor.HAND_CURSOR);
		cursormode[3] = new Cursor(Cursor.TEXT_CURSOR);
		cursormode[4] = new Cursor(Cursor.WAIT_CURSOR);
	}

	public void cursor(CursorMode mode) {
		Cursor c;
		switch (mode) {
		case DEFAULT:
			c = cursormode[0];
			break;
		case CROSS:
			c = cursormode[1];
			break;
		case HAND:
			c = cursormode[2];
			break;
		case TEXT:
			c = cursormode[3];
			break;
		case WAIT:
			c = cursormode[4];
			break;
		default:
			c = cursormode[0];
			break;

		}
		setCursor(c);
	}

	public void noCursor() {
		Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(
				new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
				new Point(), "");
		setCursor(c);
	}

	public void cursor(String string, int hotspotX, int hotspotY) {
		Image jimage;
		try {
			jimage = ImageIO.read(new java.io.File(string));

			Point hotspot = new Point(hotspotX, hotspotY);
			Toolkit tk = Toolkit.getDefaultToolkit();
			Cursor cursor = tk.createCustomCursor(jimage, hotspot,
					"Custom Cursor");
			setCursor(cursor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setFPS(double fps){
	    this.fps = fps;
	}
	
	public double getFPS(){
        return fps;
    }

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;		
		//System.out.println(e.getPoint().x+" "+e.getPoint().y);
		updateMouse(PRESSED);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseClicked = true;
		updateMouse(CLICKED);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseEntered = true;
		updateMouse(ENTERED);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseEntered = false;
		updateMouse(EXITED);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//mousePressed = false;
		//mouseDragged = false;
		mouseReleased = true;
		updateMouse(RELEASED);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDragged = true;
		updateMouse(DRAGGED);
		//panel.display();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseMoved = true;	
		updateMouse(MOVED);
		
		//panel.display();
	}

	private void updateMouse(int MODE) {
		preMouseX = mouseX;
		preMouseY = mouseY;

		Point p = getMousePosition(true);
		if (p != null) {
			mouseX = p.x;
			mouseY = height - p.y;
		}

		//panel.display();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keypressed = true;
		this.key = e.getKeyChar();
		panel.display();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keypressed = false;
		panel.display();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		keypressed = true;
		this.key = e.getKeyChar();
		this.keyPressed();
		this.key = e.getKeyChar();

		panel.display();
	}

	public void keyPressed() {
	};

	public void keyReleased() {
	};

	@Override
	public void drawWithGraphics(Graphics g) {
		this.draw(g);
	}

	public boolean isRunAsApplication() {
		return runAsApplication;
	}

	public void setRunAsApplication(boolean runAsApplication) {
		this.runAsApplication = runAsApplication;
	}

    public char getKey() {
        return key;
    }

    public int getPreMouseX() {
        return preMouseX;
    }

    public int getPreMouseY() {
        return preMouseY;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
    
	public boolean isMousePressed() {
		return mousePressed;
	}

	public boolean isMouseClicked() {
		return mouseClicked;
	}

	public boolean isMouseEntered() {
		return mouseEntered;
	}

	public boolean isMouseExited() {
		return mouseExited;
	}

	public boolean isMouseReleased() {
		return mouseReleased;
	}

	public boolean isMouseDragged() {
		return mouseDragged;
	}

	public boolean isMouseMoved() {
		return mouseMoved;
	}
	
    abstract public void setup();

    abstract public void draw(Graphics g);
    
}

/**
 * implement draw function using casmi Graphics
 * 
 * @author takashi
 * 
 */
interface GraphicsDrawable {
	public void drawWithGraphics(Graphics g);
}

/**
 * JOGL Events wrap class
 * 
 * @author takashi
 * 
 */

class AppletGLEventListener implements GLEventListener {

	public int width;
	public int height;
	private GL gl;
	GLU glu;
	GLUT glut;
	private Graphics g = null;
	private GraphicsDrawable d = null;

	public AppletGLEventListener(GraphicsDrawable drawable, int w, int h) {
		this.width = w;
		this.height = h;
		this.d = drawable;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();

		g = new Graphics(gl, glu, glut, width, height);

		g.init();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glViewport(0, 0, this.width, this.height);

		g.ortho();

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL.GL_BLEND);
		
		gl.glEnable(GL.GL_LINE_SMOOTH);

		if (d != null) {
			d.drawWithGraphics(g);
		}

		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		this.setSize(width, height);
	}

	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		this.g.setWidth(w);
		this.g.setHeight(h);
	}
	
	
	
}
