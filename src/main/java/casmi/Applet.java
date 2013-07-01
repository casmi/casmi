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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.glu.GLU;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import casmi.graphics.Graphics;
import casmi.graphics.canvas.Canvas;
import casmi.graphics.canvas.RootCanvas;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.element.Element;
import casmi.graphics.object.Background;
import casmi.graphics.object.Camera;
import casmi.graphics.object.Frustum;
import casmi.graphics.object.Light;
import casmi.graphics.object.Ortho;
import casmi.graphics.object.Perspective;
import casmi.image.ImageType;
import casmi.tween.Tweener;
import casmi.ui.PopupMenu;
import casmi.util.FileUtil;

import com.jogamp.opengl.util.awt.Screenshot;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * casmi Applet.
 *
 * @author Takashi AOKI <federkasten@me.com>, Y. Ban, T. Takeuchi
 */
abstract public class Applet extends JApplet
implements GraphicsDrawable, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private int width  = 100;
    private int height = 100;

    // FPS.
    private double fps        = 30.0;
    private double workingFPS = fps;
    private int    frame      = 0;
    private long   baseTime   = 0;

    private final Mouse     mouse     = new Mouse();
    private final Keyboard  keyboard  = new Keyboard();
    private final MenuBar   menuBar   = new MenuBar();
    private final PopupMenu popupMenu = new PopupMenu(this);

    private MouseButton mouseButton;
    private MouseStatus mouseStatus;

	private GLCapabilities caps;
	private GLJPanel panel = null;
	private AppletGLEventListener listener = null;

	private Timer timer;

	private boolean isFullScreen = false;
	private boolean initialFullScreen = false;
	private int normalWidth, normalHeight;

	private boolean isInitializing = true;

	private boolean runAsApplication = false;

//	private boolean timeline = false;
//	private Timeline rootTimeline;

	private boolean rootObjectIsInitialized = false;
	private RootCanvas rootCanvas;

	// for capturing a window
	private ImageType imageType = ImageType.JPG;
	private boolean saveImageFlag = false;
	private boolean saveBackground = true;
	private String saveFile;

	// Abstract methods.
	// -------------------------------------------------------------------------
	abstract public void setup();

	abstract public void update();

	abstract public void exit();

	abstract public void mouseEvent(MouseStatus status, MouseButton button);

	abstract public void keyEvent(KeyEvent event);
	// -------------------------------------------------------------------------

	class GLRedisplayTask extends TimerTask {

	    @Override
	    public void run() {
	        if (panel != null) { // TODO if no update, do not re-render
	            if (initialFullScreen) {
	                setFullScreen(true);
	                initialFullScreen = false;
	            }

	            panel.display();

	            mouse.setPressed(false);
	            mouse.setClicked(false);
	            mouse.setDoubleClicked(false);
	            mouse.setEntered(false);
	            mouse.setExited(false);
	            mouse.setReleased(false);
	            mouse.setDragged(false);
	            mouse.setMoved(false);

	            keyboard.setPressed(false);
	            keyboard.setReleased(false);
	            keyboard.setTyped(false);

	            rootCanvas.updateMouseStatus(null);
	        }
	    }
	}

	private void initCanvas() {
		rootCanvas = new RootCanvas();
	}

	@Override
	public void init() {
		initCanvas();

		setSize(0, 0);

		// JOGL setup
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		this.caps = new GLCapabilities(profile);
		this.caps.setStencilBits(8);
		this.panel = new GLJPanel(this.caps);
		this.listener = new AppletGLEventListener(this, getWidth(), getHeight());

		panel.addGLEventListener(listener);
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		panel.addMouseWheelListener(this);
		panel.addKeyListener(this);
		add(panel);

		setFocusable(false);
		panel.setFocusable(true);

		if (runAsApplication) {
		    AppletRunner.frame.setJMenuBar(menuBar.getJMenuBar());
		} else {
		    setJMenuBar(menuBar.getJMenuBar());
		}

		timer = new Timer();
		timer.schedule(new GLRedisplayTask(), 0, (long)(1000.0 / fps));

		Runtime.getRuntime().addShutdownHook(new Thread() {
		    @Override
		    public void run() {
		        exit();
            }
        });
	}

	@Override
	public void setSize(int width, int height) {
	    normalWidth  = width;
	    normalHeight = height;
	    innerSetSize(width, height);
	}

	private void innerSetSize(int width, int height) {
		this.width  = width;
		this.height = height;
		super.setSize(new Dimension(width, height));

		if (panel != null) {
			panel.setSize(new Dimension(width, height));
		}
	}

	void setAppletSize(int w, int h) {
	    this.width = w;
	    this.height = h;
	}

	/**
	 * Changes a cursor image.
	 *
	 * @param cursorMode
	 *            A cursor type.
	 */
	public void setCursor(CursorMode cursorMode) {
	    Cursor c = CursorMode.getAWTCursor(cursorMode);
	    if (c.getType() == getCursor().getType()) return;
		setCursor(c);
	}

	public void setCursor(String path, int hotspotX, int hotspotY) throws IOException {
		Image image = ImageIO.read(new java.io.File(path));

		Point hotspot = new Point(hotspotX, hotspotY);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Cursor cursor = tk.createCustomCursor(image, hotspot, "Custom Cursor");
		setCursor(cursor);
	}

	public void setFPS(double fps) {
		this.fps = fps;

		if (!isInitializing) {
		    timer.cancel();
		    timer = new Timer();
		    timer.schedule(new GLRedisplayTask(), 0, (long) (1000.0 / fps));
		}
	}

	public double getFPS() {
		return fps;
	}

	public double getWorkingFPS() {
	    return workingFPS;
	}

	public boolean isFullScreen() {
		return isFullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		if (isInitializing) {
		    initialFullScreen = fullScreen;
		    if (fullScreen) {
		        AppletRunner.displayDevice.setFullScreenWindow(AppletRunner.frame);
		        innerSetSize(AppletRunner.displayDevice.getFullScreenWindow().getWidth(),
		                     AppletRunner.displayDevice.getFullScreenWindow().getHeight());
		    }
		    return;
		}

		if (this.isFullScreen == fullScreen) {
			return;
		}

		this.isFullScreen = fullScreen;

		if (AppletRunner.frame.isDisplayable()) {
		    AppletRunner.frame.dispose();
		}

		if (fullScreen) {
		    AppletRunner.frame.setUndecorated(true);
			AppletRunner.displayDevice.setFullScreenWindow(AppletRunner.frame);

			innerSetSize(AppletRunner.displayDevice.getFullScreenWindow().getWidth(),
			             AppletRunner.displayDevice.getFullScreenWindow().getHeight());
			AppletRunner.frame.setSize(width, height);
		} else {
		    innerSetSize(normalWidth, normalHeight);
		    AppletRunner.frame.setUndecorated(false);
			AppletRunner.displayDevice.setFullScreenWindow(null);
			Insets insets = AppletRunner.frame.getInsets();
			AppletRunner.frame.setSize(width  + insets.left + insets.right,
	                                   height + insets.top  + insets.bottom);
		}
		AppletRunner.frame.setVisible(true);
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		mouse.setPressed(true);

	    switch (e.getButton()) {
		case java.awt.event.MouseEvent.BUTTON1:
			mouseButton = MouseButton.LEFT;
			break;
		case java.awt.event.MouseEvent.BUTTON2:
			mouseButton = MouseButton.MIDDLE;
			break;
		case java.awt.event.MouseEvent.BUTTON3:
			mouseButton = MouseButton.RIGHT;
			break;
		}

	    mouse.setButtonPressed(mouseButton, true);
		mouseEvent(MouseStatus.PRESSED, mouseButton);

		rootCanvas.updateMouseStatus(MouseStatus.PRESSED);

//		if (timeline) {
//			rootTimeline.getScene().mouseEvent(MouseEvent.PRESSED,mouseButton);
//			rootTimeline.getScene().setMouseEvent(MouseEvent.PRESSED);
//		}
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		mouse.setReleased(true);

	    switch (e.getButton()) {
		case java.awt.event.MouseEvent.BUTTON1:
			mouseButton = MouseButton.LEFT;
			break;
		case java.awt.event.MouseEvent.BUTTON2:
			mouseButton = MouseButton.MIDDLE;
			break;
		case java.awt.event.MouseEvent.BUTTON3:
			mouseButton = MouseButton.RIGHT;
			break;
		}

	    mouse.setButtonPressed(mouseButton, false);
	    mouseEvent(MouseStatus.RELEASED, mouseButton);
		rootCanvas.updateMouseStatus(MouseStatus.RELEASED);

//		if (timeline) {
//			rootTimeline.getScene().mouseEvent(MouseEvent.RELEASED, mouseButton);
//			rootTimeline.getScene().setMouseEvent(MouseEvent.RELEASED);
//		}
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
	    mouse.setClicked(true);

		switch (e.getButton()) {
		case java.awt.event.MouseEvent.BUTTON1:
			mouseButton = MouseButton.LEFT;
			mouseEvent(MouseStatus.CLICKED, MouseButton.LEFT);
			mouseStatus = MouseStatus.CLICKED;
			if ((System.currentTimeMillis() - mouse.getMouseClickLeftTime()) < 300) {
				mouse.setDoubleClicked(true);
				mouseEvent(MouseStatus.DOUBLE_CLICKED, MouseButton.LEFT);
				mouseStatus = MouseStatus.DOUBLE_CLICKED;
			}
			mouse.setMouseClickLeftTime(System.currentTimeMillis());
			break;
		case java.awt.event.MouseEvent.BUTTON2:
			mouseButton = MouseButton.MIDDLE;
			mouseEvent(MouseStatus.CLICKED, MouseButton.MIDDLE);
			mouseStatus = MouseStatus.CLICKED;
			if ((System.currentTimeMillis() - mouse.getMouseClickMiddleTime()) < 300) {
				mouse.setDoubleClicked(true);
				mouseEvent(MouseStatus.DOUBLE_CLICKED, MouseButton.MIDDLE);
				mouseStatus = MouseStatus.DOUBLE_CLICKED;
			}
			mouse.setMouseClickLeftTime(System.currentTimeMillis());
			break;
		case java.awt.event.MouseEvent.BUTTON3:
			mouseButton = MouseButton.RIGHT;
			mouseEvent(MouseStatus.CLICKED, MouseButton.RIGHT);
			mouseStatus = MouseStatus.CLICKED;
			if ((System.currentTimeMillis() - mouse.getMouseClickRightTime()) < 300) {
				mouse.setDoubleClicked(true);
				mouseEvent(MouseStatus.DOUBLE_CLICKED, MouseButton.RIGHT);
				mouseStatus = MouseStatus.DOUBLE_CLICKED;
			}
			mouse.setMouseClickLeftTime(System.currentTimeMillis());
			break;
		}

		rootCanvas.updateMouseStatus(mouseStatus);
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
		mouseEvent(MouseStatus.ENTERED, MouseButton.LEFT);

		mouse.setEntered(true);

//		if (timeline) {
//			rootTimeline.getScene().mouseEvent(MouseEvent.ENTERED, MouseButton.LEFT);
//		}
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
		mouseEvent(MouseStatus.EXITED, MouseButton.LEFT);

		mouse.setEntered(false);

//		if (timeline) {
//			rootTimeline.getScene().mouseEvent(MouseEvent.EXITED, MouseButton.LEFT);
//		}
	}

	@Override
	public void mouseDragged(java.awt.event.MouseEvent e) {
		mouse.setDragged(true);

		switch (e.getButton()) {
		case java.awt.event.MouseEvent.BUTTON1:
			mouseButton = MouseButton.LEFT;
			break;
		case java.awt.event.MouseEvent.BUTTON2:
			mouseButton = MouseButton.MIDDLE;
			break;
		case java.awt.event.MouseEvent.BUTTON3:
			mouseButton = MouseButton.RIGHT;
			break;
		}

	    mouse.setButtonPressed(mouseButton, true);
		mouseEvent(MouseStatus.DRAGGED, mouseButton);
		rootCanvas.updateMouseStatus(MouseStatus.DRAGGED);

//		if (timeline) {
//			rootTimeline.getScene().mouseEvent(MouseEvent.DRAGGED, mouseButton);
//			rootTimeline.getScene().setMouseEvent(MouseEvent.DRAGGED);
//		}

		updateMouse();
	}

	@Override
	public void mouseMoved(java.awt.event.MouseEvent e) {
		mouse.setMoved(true);

	    mouseEvent(MouseStatus.MOVED, MouseButton.LEFT);
		rootCanvas.updateMouseStatus(MouseStatus.MOVED);

//		if (timeline) {
//			rootTimeline.getScene().mouseEvent(MouseEvent.MOVED,  MouseButton.LEFT);
//			rootTimeline.getScene().setMouseEvent(MouseEvent.MOVED);
//		}

		updateMouse();
	}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int wheelRotation = e.getWheelRotation();

        mouse.setWheelRotation(wheelRotation);

        if (wheelRotation != 0) {
            mouseEvent(MouseStatus.WHEEL_ROTATED, MouseButton.NONE);
        }
    }

	private final void updateMouse() {
		mouse.setPrevX(mouse.getX());
		mouse.setPrevY(mouse.getY());

		Point p = getMousePosition(true);
		if (p != null) {
			mouse.setX(p.x);
			mouse.setY(getHeight() - p.y);
		}
	}

	@Override
	public void keyPressed(java.awt.event.KeyEvent e) {
		keyboard.setPressed(true);
		keyboard.setKey(e.getKeyChar());
		keyboard.setKeyCode(e.getKeyCode());

		keyEvent(KeyEvent.PRESSED);

//		if (timeline) {
//			rootTimeline.getScene().keyEvent(KeyEvent.PRESSED);
//		}
	}

	@Override
	public void keyReleased(java.awt.event.KeyEvent e) {
	    keyboard.setReleased(true);

		keyEvent(KeyEvent.RELEASED);

//		if (timeline) {
//			rootTimeline.getScene().keyEvent(KeyEvent.RELEASED);
//		}

        keyboard.setKey(java.awt.event.KeyEvent.CHAR_UNDEFINED);
        keyboard.setKeyCode(java.awt.event.KeyEvent.VK_UNDEFINED);
	}

	@Override
	public void keyTyped(java.awt.event.KeyEvent e) {
	    keyboard.setTyped(true);
	    keyboard.setKey(e.getKeyChar());
	    keyboard.setKeyCode(e.getKeyCode());

		keyEvent(KeyEvent.TYPED);

//		if (timeline) {
//			rootTimeline.getScene().keyEvent(KeyEvent.TYPED);
//		}
	}

	// -------------------------------------------------------------------------
	// capture image
	// -------------------------------------------------------------------------

	public void capture(String file) {
		capture(file, true);
	}

	public void capture(String file, boolean background) {
		String suffix = FileUtil.getSuffix(new File(file));
		if (suffix.matches("jpg|JPG|jpeg|JPEG")) {
			capture(file, background, ImageType.JPG);
		} else if (suffix.matches("png|PNG")) {
			capture(file, background, ImageType.PNG);
		} else if (suffix.matches("bmp|BMP")) {
			capture(file, background, ImageType.BMP);
		} else if (suffix.matches("gif|GIF")) {
			capture(file, background, ImageType.GIF);
		} else {
			throw new IllegalArgumentException(
					"The file type is not supporeted.");
		}
	}

	public void capture(String file, boolean background, ImageType type) {
		saveImageFlag = true;
		imageType = type;
		saveBackground = background;
		saveFile = file;
	}

	// -------------------------------------------------------------------------

	@Override
	public void initGraphics(Graphics g) {
	    rootObjectIsInitialized = true;
	    rootCanvas = null;

	    initCanvas();

	    this.setup();

	    isInitializing = false;

	    if (AppletRunner.frame != null && runAsApplication) {
	        JFrame frame = AppletRunner.frame;
	        if (!initialFullScreen) {
	            Insets insets = frame.getInsets();
	            frame.setSize(getWidth()  + insets.left + insets.right,
	                getHeight() + insets.top  + insets.bottom);
	        }
	        frame.setLocationRelativeTo(null);
	    }

	    setFPS(getFPS());
	}

	@Override
    public void resetGraphics(Graphics g) {
		rootCanvas.reset(g);
	}

	@Override
	public void drawWithGraphics(Graphics g) {
        update();

	    drawObjects(g);

		// Calculate real fps.
		{
		    frame++;
		    long now = System.currentTimeMillis();
		    long elapse = now - baseTime;
		    if (1000 < elapse) {
		        workingFPS = frame * 1000.0 / elapse;
		        baseTime = now;
		        frame = 0;
		    }
		}

		// capture image
		if (saveImageFlag) {
			saveImageFlag = false;

			try {
				switch (imageType) {
				case JPG:
				case PNG:
				case BMP:
				case GIF:
				default:
					Screenshot.writeToFile(new File(saveFile), width, height,
							!saveBackground);
					break;
				}
			} catch (GLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isRunAsApplication() {
		return runAsApplication;
	}

	public void setRunAsApplication(boolean runAsApplication) {
		this.runAsApplication = runAsApplication;
	}

	public int getMouseWheelRotation() {
		return mouse.getWheelRotation();
	}

	public Mouse getMouse() {
	    return mouse;
	}

	public int getPrevMouseX() {
		return mouse.getPrevX();
	}

	public int getPrevMouseY() {
		return mouse.getPrevY();
	}

	public int getMouseX() {
		return mouse.getX();
	}

	public int getMouseY() {
		return mouse.getY();
	}

	public boolean isMousePressed() {
		return mouse.isPressed();
	}

	public boolean isMousePressed(MouseButton button) {
	    return mouse.isButtonPressed(button);
	}

	public boolean isMouseClicked() {
		return mouse.isClicked();
	}

	public boolean isMouseEntered() {
		return mouse.isEntered();
	}

	public boolean isMouseExited() {
		return mouse.isExited();
	}

	public boolean isMouseReleased() {
		return mouse.isReleased();
	}

	public boolean isMouseDragged() {
		return mouse.isDragged();
	}

	public boolean isMouseMoved() {
		return mouse.isMoved();
	}

	public Keyboard getKeyboard() {
	    return keyboard;
	}

	public char getKey() {
		return keyboard.getKey();
	}

	public int getKeyCode() {
	    return keyboard.getKeyCode();
	}

	public boolean isKeyPressed() {
		return keyboard.isPressed();
	}

	public boolean isKeyReleased() {
		return keyboard.isReleased();
	}

	public boolean isKeyTyped() {
		return keyboard.isTyped();
	}

	// MenuBar -----

	public MenuBar getMenuBar() {
	    return menuBar;
	}

	// PopupMenu -----

	public PopupMenu getPopupMenu() {
	    return popupMenu;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

    private final void drawObjects(Graphics g) {
        rootCanvas.render(g, getMouseX(), getMouseY());
    }

    public void addTweener(Tweener t) {
        rootCanvas.addTweener(t);
    }

    public void removeTweener(Tweener t) {
        rootCanvas.removeTweener(t);
    }

    public void removeAllTweeners() {
        rootCanvas.removeAllTweeners();
    }

    public void addObject(Element obj) {
        if(rootObjectIsInitialized){
            rootCanvas.add(obj);
            //           if(obj instanceof Timeline){
            //               rootTimeline = (Timeline) obj;
            //               timeline = true;
            //               rootTimeline.setKeyboard(keyboard);
            //               rootTimeline.setMouse(mouse);
            //               rootTimeline.setPopup(popupMenu);
            //               rootTimeline.setApplet(this);
            //           }
        }
    }

    public void addObject(int index, Element obj) {
        rootCanvas.add(index, obj);
        //       if(obj instanceof Timeline){
        //           rootTimeline = (Timeline)obj;
        //           timeline = true;
        //           rootTimeline.setKeyboard(keyboard);
        //           rootTimeline.setMouse(mouse);
        //           rootTimeline.setPopup(popupMenu);
        //           rootTimeline.setApplet(this);
        //       }
    }

    public void addObject(Collection<? extends Element> objects) {
        rootCanvas.addAll(objects);
    }

    public void removeObject(Element e) {
        rootCanvas.remove(e);
    }


    public Object getObject(int index) {
        return rootCanvas.get(index);
    }

    public void clearObject() {
        rootCanvas.clear();
    }

    public void addCanvas(Canvas c) {
        rootCanvas.addCanvas(c);
    }

    public void removeCanvas(Canvas c) {
        rootCanvas.removeCanvas(c);
    }

    public void removeAllCanvases() {
        rootCanvas.removeAllCanvases();
    }

    public void setPerspective() {
        rootCanvas.setProjection(new Perspective());
    }

    public void setPerspective(double fov, double aspect, double zNear,    double zFar) {
        rootCanvas.setProjection(new Perspective(fov, aspect, zNear, zFar));
    }

    public void setPerspective(Perspective perspective) {
        rootCanvas.setProjection(perspective);
    }

    public void setOrtho() {
        rootCanvas.setProjection(new Ortho());
    }

    public void setOrtho(double left, double right, double bottom, double top, double near, double far) {
        rootCanvas.setProjection(new Ortho(left, right, bottom, top, near, far));
    }

    public void setOrtho(Ortho ortho) {
        rootCanvas.setProjection(ortho);
    }

    public void setFrustum() {
        rootCanvas.setProjection(new Frustum());
    }

    public void setFrustum(double left, double right, double bottom, double top, double near, double far) {
        rootCanvas.setProjection(new Frustum(left, right, bottom, top, near, far));
    }

    public void setFrustum(Frustum frustum) {
        rootCanvas.setProjection(frustum);
    }

    public void setCamera() {
        rootCanvas.setCamera(new Camera());
    }

    public void setCamera(double eyeX, double eyeY, double eyeZ, double centerX, double centerY, double centerZ, double upX, double upY, double upZ) {
        rootCanvas.setCamera(new Camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ));
    }

    public void setCamera(Camera camera) {
        rootCanvas.setCamera(camera);
    }

    public void getCamera(int index) {
        rootCanvas.getCamera(index);
    }

    public void addLight(Light light) {
        rootCanvas.addLight(light);
    }

    public void addLight(int index, Light light) {
        rootCanvas.addLight(index, light);
    }

    public void removeLight(Light light) {
        rootCanvas.removeLight(light);
    }

    public void applyMatrix(DoubleBuffer matrix) {
        rootCanvas.applyMatrix(matrix);
    }

    public void applyMatix(double matrix[]) {
        rootCanvas.applyMatrix(matrix);
    }

    public void loadMatrix(DoubleBuffer matrix) {
        rootCanvas.loadMatrix(matrix);
    }

    public void loadMatix(double matrix[]) {
        rootCanvas.loadMatrix(matrix);
    }

    public void setBackGroundColor(double gray) {
        rootCanvas.setBackGroundColor(new Background(gray));
    }

    public void setBackGroundColor(double r, double g, double b) {
        rootCanvas.setBackGroundColor(new Background(r, g, b));
    }

    public void setBackGroundColor(Color color) {
        rootCanvas.setBackGroundColor(new Background(color));
    }

    public void setBackGroundColor(ColorSet colorset) {
        rootCanvas.setBackGroundColor(new Background(colorset));
    }

    public static void showAlert(String title, String message) {
        JFrame frame = new JFrame();
        frame.setTitle(title);
        JOptionPane.showMessageDialog(frame, message);
    }

    public AppletGLEventListener getListener() {
        return listener;
    }
}

/**
 * implement draw function using casmi Graphics
 *
 * @author Takashi AOKI <federkasten@me.com>
 *
 */
interface GraphicsDrawable {
    public void drawWithGraphics(Graphics g);

    public void resetGraphics(Graphics g);
    public void initGraphics(Graphics g);
}

/**
 * JOGL Events wrap class
 *
 * @author Takashi AOKI <federkasten@me.com>
 *
 */
class AppletGLEventListener implements GLEventListener {

	public int width;
	public int height;

	private GL2 gl;
	GLU glu;
	GLUT glut;

	private Graphics g = null;
	private GraphicsDrawable d = null;
	boolean initialized = false;

	public AppletGLEventListener(GraphicsDrawable drawable, int w, int h) {
		this.width = w;
		this.height = h;
		this.d = drawable;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
		glu = new GLU();
		glut = new GLUT();

		g = new Graphics(gl, glu, glut, width, height);

		if (initialized) {
			d.resetGraphics(g);
		} else {
			d.initGraphics(g);
		}

		initialized = true;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		synchronized (this) {
			gl.glViewport(0, 0, this.width, this.height);

	        gl.glClearStencil(0);

			g.setOrtho();

			gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL2.GL_BLEND);

	        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
			gl.glEnable(GL2.GL_LINE_SMOOTH);

			if (d != null) {
				d.drawWithGraphics(g);
			}

			gl.glFlush();
		}
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	    ((Applet)d).setAppletSize(width, height);
	    this.setSize(width, height);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		this.g.setWidth(w);
		this.g.setHeight(h);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
	}
}
