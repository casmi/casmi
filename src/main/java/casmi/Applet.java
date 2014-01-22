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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.glu.GLU;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import casmi.graphics.Graphics;
import casmi.graphics.canvas.Canvas;
import casmi.graphics.canvas.RootCanvas;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Element;
import casmi.graphics.object.Background;
import casmi.graphics.object.Camera;
import casmi.graphics.object.Light;
import casmi.graphics.object.Projection;
import casmi.image.ImageType;
import casmi.tween.Tweener;
import casmi.ui.Component;
import casmi.util.FileUtil;

import com.jogamp.opengl.util.awt.Screenshot;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * casmi Applet
 *
 * @author Takashi AOKI <federkasten@me.com>, Y. Ban, T. Takeuchi
 */
abstract public class Applet extends JApplet implements GLCanvasPanelEventListener {

    abstract public void setup();

    @Override
    abstract public void update();

    @Override
    abstract public void exit();

    @Override
    abstract public void mouseEvent(MouseEvent event, MouseButton button, Mouse mouse);

    @Override
    abstract public void keyEvent(KeyEvent event, Keyboard keyboard);

    @Override
    public void refresh() {}

    private MenuBar menuBar = null;

    private JFrame windowFrame = null;
    private GLCanvasPanel panel = null;

    private boolean isFullScreen = false;

    private GraphicsDevice displayDevice;

    private boolean available = false;

    public Applet() {
        panel = new GLCanvasPanel();
        panel.setEventListener(this);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                exit();
            }
        });
    }

    @Override
    public void start() {
        this.setup();

        available = true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    public GLCanvasPanel getPanel() {
        return this.panel;
    }

    @Override
    public void init() {
        super.init();

        add(panel);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);

        panel.setPanelSize(width, height);

        if (windowFrame != null) {
            windowFrame.setSize(width, height);
        }
    }

    public void close() {
        if(windowFrame != null) {
            windowFrame.dispatchEvent(new WindowEvent(windowFrame, WindowEvent.WINDOW_CLOSING));
        } else {
            System.exit(0);
        }
    }

    public JFrame getWindowFrame() {
        return windowFrame;
    }

    public void setWindowFrame(JFrame windowFrame) {
        this.windowFrame = windowFrame;
    }

    public GraphicsDevice getDisplayDevice() {
        return displayDevice;
    }

    public void setDisplayDevice(GraphicsDevice displayDevice) {
        this.displayDevice = displayDevice;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;

        if (windowFrame != null) {
            windowFrame.setJMenuBar(menuBar.getJMenuBar());
        } else {
            this.setJMenuBar(menuBar.getJMenuBar());
        }
    }

    public int getAppletWidth() {
        return panel.getPanelWidth();
    }

    public int getAppletHeight() {
        return panel.getPanelHeight();
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;

        if (windowFrame != null && windowFrame.isDisplayable()) {
            windowFrame.dispose();
        }

        if (isFullScreen) {
            if (windowFrame != null) {
                windowFrame.setUndecorated(true);
                displayDevice.setFullScreenWindow(windowFrame);
            }
        } else {
            if (windowFrame != null) {
                windowFrame.setUndecorated(false);
                windowFrame.setSize(panel.getWidth(), panel.getHeight());
            }
        }
    }

    /**
     * Change mouse cursor image
     *
     * @param cursorMode
     *            A cursor type
     */
    public void setCursor(CursorMode cursorMode) {
        Cursor c = CursorMode.getAWTCursor(cursorMode);

        if (c.getType() == panel.getCursor().getType()) return;
        panel.setCursor(c);
    }

    public void setCursor(String path, int hotspotX, int hotspotY) throws IOException {
        Image image = ImageIO.read(new java.io.File(path));

        Point hotspot = new Point(hotspotX, hotspotY);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Cursor cursor = tk.createCustomCursor(image, hotspot, "Custom Cursor");
        panel.setCursor(cursor);
    }

    public void addCanvas(Canvas c) {
        panel.addCanvas(c);
    }

    public void removeCanvas(Canvas c) {
        panel.removeCanvas(c);
    }

    public void setFPS(double fps) {
        panel.setFPS(fps);
    }

    public double getFPS() {
        return panel.getFPS();
    }

    public double getWorkingFPS() {
        return panel.getWorkingFPS();
    }

    public void setBackgroundColor(Color color) {
        panel.setBackgroundColor(color);
    }

    public void setBackgroundColor(ColorSet colorSet) {
        panel.setBackgroundColor(new RGBColor(colorSet));
    }

    public void capture(String filepath) {
        panel.capture(filepath);
    }

    public Mouse getMouse() {
        return panel.getMouse();  // TODO modify to return immutable object
    }

    public Keyboard getKeyboard() {
        return panel.getKeyboard();  // TODO modify to return immutable object
    }

    // TODO refactor followings

    public void addObject(Element e) {
        panel.addObject(e);
    }

    public void removeObject(Element e) {
        panel.removeObject(e);
    }

    public void addTweener(Tweener tweener) {
        panel.addTweener(tweener);
    }

    public void setInitializing(boolean isInitializing) {
        panel.setInitializing(isInitializing);
    }

    public void clearObject() {
        panel.clearObject();
    }

    public void setProjection(Projection p) {
        panel.setProjection(p);
    }

    public void setCamera(Camera c) {
        panel.setCamera(c);
    }

    public void addLight(Light l) {
        panel.addLight(l);
    }

    public void addControlComponent(Component c) {
        panel.addControlComponent(c);
    }
}

/**
 * @author Takashi AOKI <federkasten@me.com>
 *
 */
interface GLCanvasPanelEventListener {
    void start();
    void update();
    void refresh();
    void exit();
    void mouseEvent(MouseEvent event, MouseButton button, Mouse mouse);
    void keyEvent(KeyEvent event, Keyboard keyboard);
    boolean isAvailable();
}

/**
 * @author Takashi AOKI <federkasten@me.com>
 *
 */
class GLCanvasPanel extends JPanel
implements GraphicsDrawable, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, ComponentListener{
    private GLCanvasPanelEventListener eventListener;

    private int panelWidth  = 100;
    private int panelHeight = 100;

    // FPS
    private double fps = 30.0;
    private double workingFPS = fps;
    private int frame = 0;
    private long baseTime = 0;

    private final Mouse mouse = new Mouse();
    private final Keyboard keyboard = new Keyboard();

    private MouseButton mouseButton;
    private MouseEvent mouseStatus;

	private GLCapabilities caps;
	private GLCanvas canvas = null;
	private AppletGLEventListener listener = null;

	private Timer timer;
	private Timer updateTimer;

//	private boolean initialFullScreen = false;

	private boolean isInitializing = true;  // TODO rename

//	private boolean runAsApplication = false;

//	private boolean timeline = false;
//	private Timeline rootTimeline;

	private boolean rootObjectIsInitialized = false;
	private RootCanvas rootCanvas = null;

	// for capturing a window
	private ImageType imageType = ImageType.JPG;
	private boolean saveImageFlag = false;
	private boolean saveBackground = true;
	private String saveFile;

	private JPanel controlPanel;

	private List<Component> components = new ArrayList<Component>();

	// -------------------------------------------------------------------------

	class UpdateTask extends TimerTask {

	    @Override
	    public void run() {
	        if (canvas != null && eventListener != null && eventListener.isAvailable()) {
	            eventListener.update();
	        }
	    }
	}

	class GLRedisplayTask extends TimerTask {

	    @Override
	    public void run() {
	        if (canvas != null) {
	            canvas.display();

//	            if (initialFullScreen) {
//	                setFullScreen(true);
//	                initialFullScreen = false;
//	            }

	            // TODO refactor followings

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

	            if (rootCanvas != null) {
	                rootCanvas.updateMouseStatus(null);
	            }
	        }
	    }
	}

	public GLCanvasPanel() {
	    // setup layered pane
	    this.controlPanel = new JPanel();
	    this.controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));

		// setup JOGL
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		this.caps = new GLCapabilities(profile);
		this.caps.setStencilBits(8);
		this.caps.setHardwareAccelerated( true );
        this.caps.setDoubleBuffered( true );
		this.canvas = new GLCanvas(this.caps);
		this.listener = new AppletGLEventListener(this, panelWidth, panelHeight);

		canvas.addGLEventListener(listener);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);
		canvas.addKeyListener(this);

		setFocusable(false);
		canvas.setFocusable(true);

		this.setLayout(new BorderLayout());
		this.add(canvas, BorderLayout.CENTER, -1);

		timer = new Timer();
		timer.schedule(new GLRedisplayTask(), 0, (long)(1000.0 / fps));

		updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(new UpdateTask(), 0, (long)(1000.0 / 24));

		this.addComponentListener(this);

		this.validate();
	}

	void setPanelSize(int w, int h) {
	    this.panelWidth = w;
	    this.panelHeight = h;
	}

	public void setFPS(double fps) {
		this.fps = fps;

		if (!isInitializing()) {
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
		eventListener.mouseEvent(MouseEvent.PRESSED, mouseButton, mouse);
		rootCanvas.updateMouseStatus(MouseEvent.PRESSED);

//		if (timeline) {
//			rootTimeline.getScene().mouseEvent(MouseEvent.PRESSED,mouseButton);
//			rootTimeline.getScene().setMouseEvent(MouseEvent.PRESSED);
//		}

		updateMouse();
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
	    eventListener.mouseEvent(MouseEvent.RELEASED, mouseButton, mouse);
		rootCanvas.updateMouseStatus(MouseEvent.RELEASED);

//		if (timeline) {
//			rootTimeline.getScene().mouseEvent(MouseEvent.RELEASED, mouseButton);
//			rootTimeline.getScene().setMouseEvent(MouseEvent.RELEASED);
//		}

		updateMouse();
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
	    mouse.setClicked(true);

		switch (e.getButton()) {
		case java.awt.event.MouseEvent.BUTTON1:
			mouseButton = MouseButton.LEFT;
			eventListener.mouseEvent(MouseEvent.CLICKED, MouseButton.LEFT, mouse);
			mouseStatus = MouseEvent.CLICKED;
			if ((System.currentTimeMillis() - mouse.getMouseClickLeftTime()) < 300) {
				mouse.setDoubleClicked(true);
				eventListener.mouseEvent(MouseEvent.DOUBLE_CLICKED, MouseButton.LEFT, mouse);
				mouseStatus = MouseEvent.DOUBLE_CLICKED;
			}
			mouse.setMouseClickLeftTime(System.currentTimeMillis());
			break;
		case java.awt.event.MouseEvent.BUTTON2:
			mouseButton = MouseButton.MIDDLE;
			eventListener.mouseEvent(MouseEvent.CLICKED, MouseButton.MIDDLE, mouse);
			mouseStatus = MouseEvent.CLICKED;
			if ((System.currentTimeMillis() - mouse.getMouseClickMiddleTime()) < 300) {
				mouse.setDoubleClicked(true);
				eventListener.mouseEvent(MouseEvent.DOUBLE_CLICKED, MouseButton.MIDDLE, mouse);
				mouseStatus = MouseEvent.DOUBLE_CLICKED;
			}
			mouse.setMouseClickLeftTime(System.currentTimeMillis());
			break;
		case java.awt.event.MouseEvent.BUTTON3:
			mouseButton = MouseButton.RIGHT;
			eventListener.mouseEvent(MouseEvent.CLICKED, MouseButton.RIGHT, mouse);
			mouseStatus = MouseEvent.CLICKED;
			if ((System.currentTimeMillis() - mouse.getMouseClickRightTime()) < 300) {
				mouse.setDoubleClicked(true);
				eventListener.mouseEvent(MouseEvent.DOUBLE_CLICKED, MouseButton.RIGHT, mouse);
				mouseStatus = MouseEvent.DOUBLE_CLICKED;
			}
			mouse.setMouseClickLeftTime(System.currentTimeMillis());
			break;
		}

		rootCanvas.updateMouseStatus(mouseStatus);
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
	    eventListener.mouseEvent(MouseEvent.ENTERED, MouseButton.NONE, mouse);

		mouse.setEntered(true);

//		if (timeline) {
//			rootTimeline.getScene().mouseEvent(MouseEvent.ENTERED, MouseButton.LEFT);
//		}

		initMouse();
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
	    eventListener.mouseEvent(MouseEvent.EXITED, MouseButton.NONE, mouse);

		mouse.setEntered(false);

//		if (timeline) {
//			rootTimeline.getScene().mouseEvent(MouseEvent.EXITED, MouseButton.LEFT);
//		}

		initMouse();
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
	    eventListener.mouseEvent(MouseEvent.DRAGGED, mouseButton, mouse);
		rootCanvas.updateMouseStatus(MouseEvent.DRAGGED);

//		if (timeline) {
//			rootTimeline.getScene().mouseEvent(MouseEvent.DRAGGED, mouseButton);
//			rootTimeline.getScene().setMouseEvent(MouseEvent.DRAGGED);
//		}

		updateMouse();
	}

	@Override
	public void mouseMoved(java.awt.event.MouseEvent e) {
		mouse.setMoved(true);

		eventListener.mouseEvent(MouseEvent.MOVED, MouseButton.LEFT, mouse);
		rootCanvas.updateMouseStatus(MouseEvent.MOVED);

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
            eventListener.mouseEvent(MouseEvent.WHEEL_ROTATED, MouseButton.NONE, mouse);
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

	private final void initMouse() {
        Point p = getMousePosition(true);
        if (p != null) {
            mouse.setX(p.x);
            mouse.setY(getHeight() - p.y);

            mouse.setPrevX(mouse.getX());
            mouse.setPrevY(mouse.getY());
        }
    }

	@Override
	public void keyPressed(java.awt.event.KeyEvent e) {
		keyboard.setPressed(true);
		keyboard.setCharacter(e.getKeyChar());
		keyboard.setKeyCode(e.getKeyCode());

		eventListener.keyEvent(KeyEvent.PRESSED, keyboard);

//		if (timeline) {
//			rootTimeline.getScene().keyEvent(KeyEvent.PRESSED);
//		}
	}

	@Override
	public void keyReleased(java.awt.event.KeyEvent e) {
	    keyboard.setReleased(true);

	    eventListener.keyEvent(KeyEvent.RELEASED, keyboard);

//		if (timeline) {
//			rootTimeline.getScene().keyEvent(KeyEvent.RELEASED);
//		}

        keyboard.setCharacter(java.awt.event.KeyEvent.CHAR_UNDEFINED);
        keyboard.setKeyCode(java.awt.event.KeyEvent.VK_UNDEFINED);
	}

	@Override
	public void keyTyped(java.awt.event.KeyEvent e) {
	    keyboard.setTyped(true);
	    keyboard.setCharacter(e.getKeyChar());
	    keyboard.setKeyCode(e.getKeyCode());

	    eventListener.keyEvent(KeyEvent.TYPED, keyboard);

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
	    rootCanvas = new RootCanvas();

	    eventListener.start();

// TODO fix
//	    if (runAsApplication) {
//	        if(windowFrame != null) {
//	            if (!initialFullScreen) {
//	                Insets insets = windowFrame.getInsets();
//	                windowFrame.setSize(panelWidth  + insets.left + insets.right,
//	                                    panelHeight + insets.top  + insets.bottom);
//	            }
//	        }
//	    } else {
//	        this.isInitializing = false;
//	    }

	    setFPS(fps);
	}

	@Override
    public void resetGraphics(Graphics g) {
	    if (rootCanvas != null) {
	        rootCanvas.reset(g);
	    }
	}

	@Override
	public void drawWithGraphics(Graphics g) {
        eventListener.refresh();

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
				    // TODO fix to use com.jogamp.opengl.util.GLReadBufferUtil
					Screenshot.writeToFile(new File(saveFile),
					                       panelWidth, panelHeight, !saveBackground);
					break;
				}
			} catch (GLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//	public boolean isRunAsApplication() {
//		return runAsApplication;
//	}
//
//	public void setRunAsApplication(boolean runAsApplication) {
//		this.runAsApplication = runAsApplication;
//	}

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
		return keyboard.getCharacter();
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
        }
    }

    public void addObject(int index, Element obj) {
        rootCanvas.add(index, obj);
    }

    public void replaceObject(Collection<? extends Element> objects) {
        rootCanvas.replace(objects);
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

    public void replaceCanvases(Collection<? extends Canvas> c) {
        rootCanvas.replaceCanvases(c);
    }

    public void clearCanvases() {
        rootCanvas.clearCanvases();
    }

    public void setProjection(Projection p) {
        rootCanvas.setProjection(p);
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

    public void addControlComponent(Component c) {
        components.add(c);
        updateControl();
    }

    public void removeControlComponent(Component c) {
        components.clear();
        updateControl();
    }

    private void updateControl() {
        if(components.size() == 0) {
            if(controlPanel.isDisplayable()) {
                this.remove(controlPanel);
            }
        } else {
            if(!controlPanel.isDisplayable()) {
                this.add(controlPanel, BorderLayout.NORTH, 1);
            }

            controlPanel.removeAll();
            for(Component c : components) {
                controlPanel.add(c.getInstance());
            }
        }

        this.validate();

        Dimension size = this.getSize();

        if (canvas != null) {
            canvas.setSize(size);
        }
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

    public void setBackgroundColor(Color color) {
        rootCanvas.setBackground(new Background(color));
    }

    public static void showAlert(String title, String message) {
        JFrame frame = new JFrame();
        frame.setTitle(title);
        JOptionPane.showMessageDialog(frame, message);
    }

    public AppletGLEventListener getListener() {
        return listener;
    }

    public int getPanelWidth() {
        return panelWidth;
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    public GLCanvas getGLCanvas() {
        return this.canvas;
    }

    public JPanel getControlPanel() {
        return controlPanel;
    }

    public boolean isInitializing() {
        return isInitializing;
    }

    public void setInitializing(boolean isInitializing) {
        this.isInitializing = isInitializing;
    }

    public GLCanvasPanelEventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(GLCanvasPanelEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Dimension size = this.getSize();

        if (canvas != null) {
            canvas.setSize(size);
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
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
	private boolean initialized = false;

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

		if (isInitialized()) {
			d.resetGraphics(g);
		} else {
			d.initGraphics(g);
		}

		this.initialized = true;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		synchronized (this) {
			gl.glViewport(0, 0, width, height);

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
	    GLCanvasPanel panel = (GLCanvasPanel) d;
	    if (!panel.isInitializing()) {
	        panel.setPanelSize(width, height);
	    }

	    this.setSize(width, height);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		if (this.initialized) {
		    this.g.setWidth(w);
		    this.g.setHeight(h);
		}
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
	}

    public boolean isInitialized() {
        return initialized;
    }
}
