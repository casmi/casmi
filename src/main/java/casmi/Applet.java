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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
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
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import casmi.exception.CasmiRuntimeException;
import casmi.graphics.Graphics;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.element.Element;
import casmi.graphics.group.Group;
import casmi.graphics.object.BackGround;
import casmi.graphics.object.Camera;
import casmi.graphics.object.Frustum;
import casmi.graphics.object.Light;
import casmi.graphics.object.Mask;
import casmi.graphics.object.Ortho;
import casmi.graphics.object.Perspective;
import casmi.graphics.object.RootObject;
import casmi.io.ImageType;
import casmi.timeline.Timeline;
import casmi.timeline.TimelineRender;
import casmi.tween.Tween;
import casmi.tween.TweenManager;
import casmi.tween.TweenParallelGroup;
import casmi.tween.TweenSerialGroup;
import casmi.util.FileUtil;

import com.jogamp.opengl.util.awt.Screenshot;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * casmi Applet.
 *
 * @author takashi, Y. Ban, T. Takeuchi
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
    private MouseEvent mouseEvent;

    private GLCapabilities caps;
    private GLJPanel panel = null;
    private AppletGLEventListener listener = null;

    private Timer timer;

    private boolean isFullScreen = false;
    private boolean initialFullScreen = false;
    private int normalWidth, normalHeight;

    private boolean isInitializing = true;

    private boolean runAsApplication = false;

    // private boolean selectionBuffer = false;

    private boolean timeline = false;
    private Timeline rootTimeline;
    //private TimelineRender rootTimelineRender;

    private boolean rootObjectInit = false;
    private RootObject rootObject;
    private List<Updatable> updateObjectList = new ArrayList<Updatable>();

    //private static final int SELECTION_BUFSIZE = 1024 * 1024;
    // private int selectedIndex = 0;

    // for capturing a window
    private ImageType imageType = ImageType.JPG;
    private boolean saveImageFlag = false;
    private boolean saveBackground = true;
    private String saveFile;

    // Abstract methods.
    // -------------------------------------------------------------------------
    abstract public void setup();

    abstract public void update();

    public void exit() {}
    // TODO: will use abstract method from next version
    // abstract public void exit();

    abstract public void mouseEvent(MouseEvent e, MouseButton b);

    abstract public void keyEvent(KeyEvent e);
    // -------------------------------------------------------------------------

    public void setGLParam(GL2 gl) {

    }

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

                rootObject.setMouseEvent(null);
            }
        }
    }

    public void initRootOject() {
        rootObject = new RootObject();
        rootObject.setSelectionbuffsize(rootObject.getSelectionbuffsize());
        rootObject.setDepthTest(false);
    }

    @Override
    public void init() {
        initRootOject();

        setSize(100, 100);

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

    public void setDepthTest(boolean depthTest) {
        rootObject.setDepthTest(depthTest);
    }

    public boolean isDepthTest() {
        return rootObject.isDepthTest();
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
        mouseEvent(MouseEvent.PRESSED, mouseButton);

        rootObject.setMouseEvent(MouseEvent.PRESSED);

        if (timeline) {
            rootTimeline.getScene().mouseEvent(MouseEvent.PRESSED,mouseButton);
            rootTimeline.getScene().setMouseEvent(MouseEvent.PRESSED);
        }
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
        mouseEvent(MouseEvent.RELEASED, mouseButton);
        rootObject.setMouseEvent(MouseEvent.RELEASED);

        if (timeline) {
            rootTimeline.getScene().mouseEvent(MouseEvent.RELEASED, mouseButton);
            rootTimeline.getScene().setMouseEvent(MouseEvent.RELEASED);
        }
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        mouse.setClicked(true);

        switch (e.getButton()) {
        case java.awt.event.MouseEvent.BUTTON1:
            mouseButton = MouseButton.LEFT;
            mouseEvent(MouseEvent.CLICKED, MouseButton.LEFT);
            mouseEvent = MouseEvent.CLICKED;
            if ((System.currentTimeMillis() - mouse.getMouseClickLeftTime()) < 300) {
                mouse.setDoubleClicked(true);
                mouseEvent(MouseEvent.DOUBLE_CLICKED, MouseButton.LEFT);
                mouseEvent = MouseEvent.DOUBLE_CLICKED;
            }
            mouse.setMouseClickLeftTime(System.currentTimeMillis());
            break;
        case java.awt.event.MouseEvent.BUTTON2:
            mouseButton = MouseButton.MIDDLE;
            mouseEvent(MouseEvent.CLICKED, MouseButton.MIDDLE);
            mouseEvent = MouseEvent.CLICKED;
            if ((System.currentTimeMillis() - mouse.getMouseClickMiddleTime()) < 300) {
                mouse.setDoubleClicked(true);
                mouseEvent(MouseEvent.DOUBLE_CLICKED, MouseButton.MIDDLE);
                mouseEvent = MouseEvent.DOUBLE_CLICKED;
            }
            mouse.setMouseClickLeftTime(System.currentTimeMillis());
            break;
        case java.awt.event.MouseEvent.BUTTON3:
            mouseButton = MouseButton.RIGHT;
            mouseEvent(MouseEvent.CLICKED, MouseButton.RIGHT);
            mouseEvent = MouseEvent.CLICKED;
            if ((System.currentTimeMillis() - mouse.getMouseClickRightTime()) < 300) {
                mouse.setDoubleClicked(true);
                mouseEvent(MouseEvent.DOUBLE_CLICKED, MouseButton.RIGHT);
                mouseEvent = MouseEvent.DOUBLE_CLICKED;
            }
            mouse.setMouseClickLeftTime(System.currentTimeMillis());
            break;
        }

        rootObject.setMouseEvent(mouseEvent);

        if (timeline) {
            rootTimeline.getScene().mouseEvent(mouseEvent, mouseButton);
            rootTimeline.getScene().setMouseEvent(mouseEvent);
        }
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        mouseEvent(MouseEvent.ENTERED, MouseButton.LEFT);

        mouse.setEntered(true);

        if (timeline) {
            rootTimeline.getScene().mouseEvent(MouseEvent.ENTERED, MouseButton.LEFT);
        }
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        mouseEvent(MouseEvent.EXITED, MouseButton.LEFT);

        mouse.setEntered(false);

        if (timeline) {
            rootTimeline.getScene().mouseEvent(MouseEvent.EXITED, MouseButton.LEFT);
        }
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
        mouseEvent(MouseEvent.DRAGGED, mouseButton);
        rootObject.setMouseEvent(MouseEvent.DRAGGED);
        if (timeline) {
            rootTimeline.getScene().mouseEvent(MouseEvent.DRAGGED, mouseButton);
            rootTimeline.getScene().setMouseEvent(MouseEvent.DRAGGED);
        }


        updateMouse();
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        mouse.setMoved(true);

        mouseEvent(MouseEvent.MOVED, MouseButton.LEFT);
        rootObject.setMouseEvent(MouseEvent.MOVED);
        if (timeline) {
            rootTimeline.getScene().mouseEvent(MouseEvent.MOVED,  MouseButton.LEFT);
            rootTimeline.getScene().setMouseEvent(MouseEvent.MOVED);
        }

        updateMouse();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int wheelRotation = e.getWheelRotation();

        mouse.setWheelRotation(wheelRotation);

        if (wheelRotation != 0) {
            mouseEvent(MouseEvent.WHEEL_ROTATED, MouseButton.NONE);
        }
    }

    private final void updateMouse() {
        mouse.setPrvX(mouse.getX());
        mouse.setPrvY(mouse.getY());

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
        if (timeline) {
            rootTimeline.getScene().keyEvent(KeyEvent.PRESSED);
        }
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        keyboard.setReleased(true);
        keyboard.setKey(java.awt.event.KeyEvent.CHAR_UNDEFINED);
        keyboard.setKeyCode(java.awt.event.KeyEvent.VK_UNDEFINED);

        keyEvent(KeyEvent.RELEASED);
        if (timeline) {
            rootTimeline.getScene().keyEvent(KeyEvent.RELEASED);
        }
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
        keyboard.setTyped(true);
        keyboard.setKey(e.getKeyChar());
        keyboard.setKeyCode(e.getKeyCode());

        keyEvent(KeyEvent.TYPED);
        if (timeline) {
            rootTimeline.getScene().keyEvent(KeyEvent.TYPED);
        }
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
        } else if (suffix.matches("pdf|PDF")) {
            capture(file, background, ImageType.PDF);
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
    public void reset() {
        rootObject.resetObjects();
    }

    @Override
    public void initSet() {
        rootObjectInit = true;
        rootObject = null;

        initRootOject();

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
    public void drawWithGraphics(Graphics g) {
        setGLParam(g.getGL());

        drawObjects(g);

        updateObjects();

        // Calculate real fps.
        {
            frame++;
            long now = System.currentTimeMillis();
            long elapse = now - baseTime;
            if (1000 < elapse) {
                workingFPS = (double)frame * 1000.0 / (double)elapse;
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
                case PDF: {
                    BufferedImage bi = Screenshot.readToBufferedImage(width,
                            height, !saveBackground);
                    PDDocument doc = new PDDocument();
                    PDRectangle rect = new PDRectangle(width, height);
                    PDPage page = new PDPage(rect);
                    doc.addPage(page);
                    PDXObjectImage ximage = new PDJpeg(doc, bi);
                    PDPageContentStream contentStream = new PDPageContentStream(
                            doc, page);
                    contentStream.drawImage(ximage, 0, 0);
                    contentStream.close();
                    doc.save(saveFile);
                    doc.close();
                    break;
                }
                }
            } catch (GLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (COSVisitorException e) {
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

    public int getPreMouseX() {
        return mouse.getPrvX();
    }

    public int getPreMouseY() {
        return mouse.getPrvY();
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

    // ----------

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    private final void drawObjects(Graphics g) {
        rootObject.clearSelectionList();
        rootObject.rootBufRender(g, getMouseX(), getMouseY(), false,0);
        rootObject.rootSelectionbufRender(g, getMouseX(), getMouseY(), 0);
        update(g);
    }

    private final void updateObjects() {
        for (Updatable obj : updateObjectList) {
            obj.update();
        }
    }

    public void glTest(Graphics g) {
        //g.getGL().glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        //g.getGL().glLoadIdentity();
        g.getGL().glColor3d(1.0, 1.0, 1.0);
        g.getGL().glTranslatef(-1.5f, 0.0f, -6.0f);
        g.getGL().glBegin(GL2.GL_TRIANGLES);
        g.getGL().glVertex3f(100.0f, 100.0f, 0.0f);
        g.getGL().glVertex3f(0.0f, 0.0f, 0.0f);
        g.getGL().glVertex3f(300.0f, 0.0f, 0.0f);
        g.getGL().glEnd();
        g.getGL().glTranslatef(1.5f, 0.0f, 0.0f);
        g.getGL().glColor3d(0.5, 0.5, 0.5);
        g.getGL().glBegin(GL2.GL_QUADS);
        g.getGL().glVertex3f(0.0f, 400.0f, 0.0f);
        g.getGL().glVertex3f(200.0f, 400.0f, 0.0f);
        g.getGL().glVertex3f(200.0f, -100.0f, 0.0f);
        g.getGL().glVertex3f(0.0f, -100.0f, 0.0f);
        g.getGL().glEnd();
        //g.getGL().glFlush();
    }

    // TODO: should change access public to private final
    public void update(Graphics g) {
        if (rootObject.isResetObject() ) {
            rootObject.resetObjects();
            rootObject.setResetObject(false);
        }

        if (!rootObjectInit)
            rootObjectInit = true;
        update();
    }

    private static TweenManager tweenManager = null;

    private TweenManager getTweenManager() {
        if (tweenManager == null) {
            tweenManager = new TweenManager();
            rootObject.addTweenManager(tweenManager);
        }

        return tweenManager;
    }

    public void addTween(Tween t) {
        getTweenManager().add(t);
    }

    public void addTween(TweenSerialGroup g) {
        getTweenManager().add(g);
    }

    public void addTween(TweenParallelGroup g) {
        getTweenManager().add(g);
    }

    public void removeTween(Tween t) {
        getTweenManager().remove(t);
    }

    /////////////////
    public void setMask(Mask mask){
        rootObject.setMask(mask);
    }

    public void clearTween(){
        tweenManager = null;
        rootObject.clearTweenManager();
    }


   public void setPosition(double x, double y, double z){
       rootObject.setPosition(x, y, z);
   }

   public void setPosition(double x, double y){
       rootObject.setPosition(x, y);
   }


   public void setRotation(double angle, double x,double y, double z) {
       rootObject.setRotation(angle, x, y, z);
   }

   public void addObject(Object obj) {
      // addObject(0, obj);
       if(rootObjectInit){
           rootObject.add(obj);
           if(obj instanceof TimelineRender){
               rootTimeline = (Timeline)obj;
               timeline = true;
               rootTimeline.setKeyboard(keyboard);
               rootTimeline.setMouse(mouse);
               rootTimeline.setPopup(popupMenu);
               rootTimeline.setApplet(this);
           }
       }
   }

   public void addObject(int index, Object obj) {
       if (obj instanceof Element || obj instanceof Group || obj instanceof TimelineRender) {
           rootObject.add(index, obj);
       } else {
           throw new CasmiRuntimeException("The added object is not rendarable");
       }

       if(obj instanceof TimelineRender){
           rootTimeline = (Timeline)obj;
           timeline = true;
           rootTimeline.setKeyboard(keyboard);
           rootTimeline.setMouse(mouse);
           rootTimeline.setPopup(popupMenu);
           rootTimeline.setApplet(this);
       }
       // NOTE: ???
      /* if (rootObject instanceof TimelineRender) {
           timeline = true;
           rootTimelineRender = (TimelineRender)rootObject;
           if (rootTimelineRender instanceof Timeline) {
               rootTimeline = (Timeline)rootTimelineRender;
               rootTimeline.setApplet(this);
           }
       }*/
   }

   public void addObject(List<Object> objectList) {
       for (Object obj : objectList) {
           addObject(obj);
       }
   }

    public void removeObject(int index) {
        rootObject.remove(index);
    }


    public Object getObject(int index) {
        return rootObject.get(index);
    }

    public void clearObject() {
        rootObject.clear();
    }

    public void addUpdateObject(Updatable obj) {
        addUpdateObject(0, obj);
    }

    public void addUpdateObject(int index, Updatable obj) {
        if (obj instanceof Updatable) {
            updateObjectList.add(index, obj);
        } else {
            throw new CasmiRuntimeException("The added object is not updatable");
        }
    }

    public Updatable getUpdateObject(int index) {
        return updateObjectList.get(index);
    }

    public void removeUpdateObject(int index) {
        updateObjectList.remove(index);
    }

    public void removeUpdateObject(Updatable obj) {
        updateObjectList.remove(obj);
    }

    public void clearUpdateObject() {
        updateObjectList.clear();
    }

    public void setPerspective() {
        rootObject.addPerse(new Perspective());
    }

    public void setPerspective(double fov, double aspect, double zNear,    double zFar) {
        rootObject.addPerse(new Perspective(fov, aspect, zNear, zFar));
    }

    public void setPerspective(Perspective perspective) {
        rootObject.addPerse(perspective);
    }

    public void setOrtho() {
        rootObject.addPerse(new Ortho());
    }

    public void setOrtho(double left, double right, double bottom, double top,
            double near, double far) {
        rootObject.addPerse(new Ortho(left, right, bottom, top, near, far));
    }

    public void setOrtho(Ortho ortho) {
        rootObject.addPerse(ortho);
    }

    public void setFrustum() {
        rootObject.addPerse(new Frustum());
    }

    public void setFrustum(double left, double right, double bottom,
            double top, double near, double far) {
        rootObject.addPerse(new Frustum(left, right, bottom, top, near, far));
    }

    public void setFrustum(Frustum frustum) {
        rootObject.addPerse(frustum);
    }

    public void setCamera() {
        rootObject.addCamera(new Camera());
    }

    public void setCamera(double eyeX, double eyeY, double eyeZ,
            double centerX, double centerY, double centerZ, double upX,
            double upY, double upZ) {
        rootObject.addCamera(new Camera(eyeX, eyeY, eyeZ, centerX, centerY,
                centerZ, upX, upY, upZ));
    }

    public void setCamera(Camera camera) {
        rootObject.addCamera(camera);
    }

    public void getCamera(int index) {
        rootObject.getCamera(index);
    }

    public void addLight(Light light) {
        rootObject.addLight(light);
    }

    public void getLight(int index) {
        rootObject.getLight(index);
    }

    public void addLight(int index, Light light) {
        rootObject.addLight(index, light);
    }

    public void removeLight(int index) {
        rootObject.remove(index);
    }

    public void applyMatrix(DoubleBuffer matrix) {
        rootObject.applyMatrix(matrix);
    }

    public void applyMatix(double matrix[]) {
        rootObject.applyMatrix(matrix);
    }

    public void loadMatrix(DoubleBuffer matrix) {
        rootObject.loadMatrix(matrix);
    }

    public void loadMatix(double matrix[]) {
        rootObject.loadMatrix(matrix);
    }

    public void setBackGroundColor(double gray) {
        rootObject.setBackGroundColor(new BackGround(gray));
    }

    public void setBackGroundColor(double r, double g, double b) {
        rootObject.setBackGroundColor(new BackGround(r, g, b));
    }

    public void setBackGroundColor(Color color) {
        rootObject.setBackGroundColor(new BackGround(color));
    }

    public void setBackGroundColor(ColorSet colorset) {
        rootObject.setBackGroundColor(new BackGround(colorset));
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
 * @author takashi
 *
 */
interface GraphicsDrawable {
    public void drawWithGraphics(Graphics g);
    public void reset();
    public void initSet();
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
    private GL2 gl;
    GLU glu;
    GLUT glut;
    private Graphics g = null;
    private GraphicsDrawable d = null;
    boolean reset = false;

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
        if (reset)
            d.reset();
        else
            d.initSet();
        reset = true;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        synchronized (this) {
            gl.glViewport(0, 0, this.width, this.height);

            g.ortho();
            gl.glClearStencil(0);
            gl.glEnable(GL2.GL_DEPTH_TEST);
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
            gl.glEnable(GL2.GL_BLEND);

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

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
        this.g.setWidth(w);
        this.g.setHeight(h);
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }
}
