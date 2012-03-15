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
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLJPanel;
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

import casmi.graphics.Graphics;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.object.BackGround;
import casmi.graphics.object.Camera;
import casmi.graphics.object.Frustum;
import casmi.graphics.object.GraphicsObject;
import casmi.graphics.object.Light;
import casmi.graphics.object.Ortho;
import casmi.graphics.object.Perspective;
import casmi.io.ImageType;
import casmi.io.MovieCodec;
import casmi.timeline.Timeline;
import casmi.timeline.TimelineRender;
import casmi.tween.Tween;
import casmi.tween.TweenManager;
import casmi.tween.TweenParallelGroup;
import casmi.tween.TweenSerialGroup;
import casmi.util.DateUtil;
import casmi.util.FileUtil;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.Screenshot;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;

/**
 * casmi Applet.
 * 
 * @author takashi, Y. Ban, T. Takeuchi
 */
abstract public class Applet extends JApplet implements GraphicsDrawable, MouseListener, MouseMotionListener, KeyListener {

    private int width = 100, height = 100;
    
    private double fps = 30.0;

    private Mouse mouse = new Mouse();
	private char key;
	private int keycode;

	private GLCapabilities caps;
	private GLJPanel panel = null;
	private AppletGLEventListener listener = null;

	private Timer timer;

	private boolean isFullScreen = false;
	private boolean isInitializing = true;

	private boolean keyPressed = false;
	private boolean keyReleased = false;
	private boolean keyTyped = false;

	private boolean runAsApplication = false;

	// private boolean selectionBuffer = false;

	private boolean timeline = false;
	private Timeline rootTimeline;
	private TimelineRender rootTimelineRender;

	private GraphicsObject rootObject;
	private static final int SELECTION_BUFSIZE = 1024 * 1024;
	// private int selectedIndex = 0;

	// for capturing a window
	private ImageType imageType = ImageType.JPG;
	private boolean saveImageFlag = false;
	private boolean saveBackground = true;
	private String saveFile;

	// for recording a movie
	private IMediaWriter mediaWriter;
	private boolean recordFlag = false;
	private boolean recordBackground = true;
	private int recordTime = 0;
	private int recordSpan = 0;
	
	abstract public void setup();

	abstract public void update();

	abstract public void mouseEvent(casmi.MouseEvent e, MouseButton b);

	abstract public void keyEvent(casmi.KeyEvent e);
	
	abstract public void mouseWheelEvent();
	
	class Wheel implements MouseWheelListener {
	    
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
		    int wheelRotation = e.getWheelRotation();
		    mouse.setWheelRotation(wheelRotation);
			if (wheelRotation != 0) {
				mouseWheelEvent();
			}
		}
	}

	class GLRedisplayTask extends TimerTask {
	    
		@Override
		public void run() {
			if (panel != null) { // TODO if no update, do not re-render
				panel.display();
				
				mouse.setPressed(false);
				mouse.setClicked(false);
				mouse.setEntered(false);
				mouse.setExited(false);
				mouse.setReleased(false);
				mouse.setDragged(false);
				mouse.setMoved(false);
				
				keyPressed = false;
				keyReleased = false;
				keyTyped = false;
			}
		}
	}

	@Override
	public void init() {
		rootObject = new GraphicsObject();
		rootObject.setSelectionbuffsize(SELECTION_BUFSIZE);
		this.setup();

		// JOGL setup
		this.caps = new GLCapabilities();
		this.caps.setStencilBits(8);
		this.panel = new GLJPanel(this.caps);
		this.listener = new AppletGLEventListener(this, getWidth(), getHeight());
		panel.addGLEventListener(listener);
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		panel.addMouseWheelListener(new Wheel());
		
		if (!isFullScreen) {
			panel.addKeyListener(this);
		} else {
			AppletRunner.frame.addKeyListener(this);
		}

		add(panel);
		setFocusable(false);
		panel.setFocusable(true);

		timer = new Timer();
		timer.schedule(new GLRedisplayTask(), 0, (long) (1000.0 / fps));

		
		isInitializing = false;
	}

	@Override
	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		super.setSize(new Dimension(w, h));

		if (panel != null) {
			panel.setSize(new Dimension(w, h));
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
	
	/**
	 * @param cursorMode
	 * @deprecated
	 */
	public void cursor(CursorMode cursorMode) {
		setCursor(cursorMode);
	}

	/**
	 * @deprecated
	 */
	public void noCursor() {
		setCursor(CursorMode.NONE);
	}

	public void setCursor(String path, int hotspotX, int hotspotY) throws IOException {
		Image jimage;
		jimage = ImageIO.read(new java.io.File(path));

		Point hotspot = new Point(hotspotX, hotspotY);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Cursor cursor = tk.createCustomCursor(jimage, hotspot,
		    "Custom Cursor");
		setCursor(cursor);
	}
	
	/**
	 * @param path
	 * @param hotspotX
	 * @param hotspotY
	 * @throws IOException
	 * 
	 * @deprecated
	 */
	public void cursor(String path, int hotspotX, int hotspotY) throws IOException {
	    setCursor(path, hotspotX, hotspotY);
	}

	public void setFPS(double fps) {
		this.fps = fps;
	}

	public double getFPS() {
		return fps;
	}

	public boolean isFullScreen() {

		return isFullScreen;
	}

	public void setFullScreen(boolean isFullScreen) {

		if (!isInitializing)
			return;

		this.isFullScreen = isFullScreen;

		if (isFullScreen) {
			AppletRunner.frame.setUndecorated(true);
			AppletRunner.displayDevice.setFullScreenWindow(AppletRunner.frame);
			setSize(AppletRunner.displayDevice.getFullScreenWindow().getWidth(),
					AppletRunner.displayDevice.getFullScreenWindow()
							.getHeight());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouse.setPressed(true);
	    
	    switch (e.getButton()) {
		case MouseEvent.BUTTON1:
		    mouse.setButtonPressed(casmi.MouseButton.LEFT, true);
			mouseEvent(casmi.MouseEvent.PRESSED, casmi.MouseButton.LEFT);
			break;
		case MouseEvent.BUTTON2:
		    mouse.setButtonPressed(casmi.MouseButton.MIDDLE, true);
			mouseEvent(casmi.MouseEvent.PRESSED, casmi.MouseButton.MIDDLE);
			break;
		case MouseEvent.BUTTON3:
		    mouse.setButtonPressed(casmi.MouseButton.RIGHT, true);
			mouseEvent(casmi.MouseEvent.PRESSED, casmi.MouseButton.RIGHT);
			break;
		}
		
		rootObject.callMouseClickCallbackOfChildren(casmi.MouseEvent.PRESSED);
		
		if (timeline) {
			rootTimeline.getScene().mouseEvent(casmi.MouseEvent.PRESSED);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		mouse.setReleased(true);
	    
	    switch (e.getButton()) {
		case MouseEvent.BUTTON1:
		    mouse.setButtonPressed(casmi.MouseButton.LEFT, false);
			mouseEvent(casmi.MouseEvent.RELEASED, casmi.MouseButton.LEFT);
			break;
		case MouseEvent.BUTTON2:
		    mouse.setButtonPressed(casmi.MouseButton.MIDDLE, false);
			mouseEvent(casmi.MouseEvent.RELEASED, casmi.MouseButton.MIDDLE);
			break;
		case MouseEvent.BUTTON3:
		    mouse.setButtonPressed(casmi.MouseButton.RIGHT, true);
			mouseEvent(casmi.MouseEvent.RELEASED, casmi.MouseButton.RIGHT);
			break;
		}
		
		rootObject.callMouseClickCallbackOfChildren(casmi.MouseEvent.RELEASED);
		
		if (timeline) {
			rootTimeline.getScene().mouseEvent(casmi.MouseEvent.RELEASED);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	    mouse.setClicked(true);
	    
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseEvent(casmi.MouseEvent.CLICKED, casmi.MouseButton.LEFT);
			break;
		case MouseEvent.BUTTON2:
			mouseEvent(casmi.MouseEvent.CLICKED, casmi.MouseButton.MIDDLE);
			break;
		case MouseEvent.BUTTON3:
			mouseEvent(casmi.MouseEvent.CLICKED, casmi.MouseButton.RIGHT);
			break;
		}
		
		rootObject.callMouseClickCallbackOfChildren(casmi.MouseEvent.CLICKED);
		
		if (timeline) {
			rootTimeline.getScene().mouseEvent(casmi.MouseEvent.CLICKED);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseEvent(casmi.MouseEvent.ENTERED, casmi.MouseButton.LEFT);
		
		mouse.setEntered(true);
		
		if (timeline) {
			rootTimeline.getScene().mouseEvent(casmi.MouseEvent.ENTERED);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseEvent(casmi.MouseEvent.EXITED, casmi.MouseButton.LEFT);
		
		mouse.setEntered(false);
		
		if (timeline) {
			rootTimeline.getScene().mouseEvent(casmi.MouseEvent.EXITED);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouse.setDragged(true);
	    
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
		    mouse.setButtonPressed(casmi.MouseButton.LEFT, true);
			mouseEvent(casmi.MouseEvent.DRAGGED, casmi.MouseButton.LEFT);
			break;
		case MouseEvent.BUTTON2:
		    mouse.setButtonPressed(casmi.MouseButton.MIDDLE, true);
			mouseEvent(casmi.MouseEvent.DRAGGED, casmi.MouseButton.MIDDLE);
			break;
		case MouseEvent.BUTTON3:
		    mouse.setButtonPressed(casmi.MouseButton.RIGHT, true);
			mouseEvent(casmi.MouseEvent.DRAGGED, casmi.MouseButton.RIGHT);
			break;
		}
		
		rootObject.callMouseClickCallbackOfChildren(casmi.MouseEvent.DRAGGED);
		
		if (timeline) {
			rootTimeline.getScene().mouseEvent(casmi.MouseEvent.DRAGGED);
		}
		
		updateMouse();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse.setMoved(true);
	    
	    mouseEvent(casmi.MouseEvent.MOVED, casmi.MouseButton.LEFT);
		
		rootObject.callMouseClickCallbackOfChildren(casmi.MouseEvent.MOVED);
		
		if (timeline) {
			rootTimeline.getScene().mouseEvent(casmi.MouseEvent.MOVED);
		}
		
		updateMouse();
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
	public void keyPressed(KeyEvent e) {
		keyPressed = true;
		this.key = e.getKeyChar();
		this.keycode = e.getKeyCode();
		keyEvent(casmi.KeyEvent.PRESSED);
		if (timeline) {
			rootTimeline.getScene().keyEvent(casmi.KeyEvent.PRESSED);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyReleased = true;
		this.key = java.awt.event.KeyEvent.CHAR_UNDEFINED;
		this.keycode = java.awt.event.KeyEvent.VK_UNDEFINED;
		keyEvent(casmi.KeyEvent.RELEASED);
		if (timeline) {
			rootTimeline.getScene().keyEvent(casmi.KeyEvent.RELEASED);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		keyTyped = true;
		this.key = e.getKeyChar();
		keyEvent(casmi.KeyEvent.TYPED);
		if (timeline) {
			rootTimeline.getScene().keyEvent(casmi.KeyEvent.TYPED);
		}
	}

	public boolean isKeyPressed() {
		return keyPressed;
	}

	public boolean isKeyReleased() {
		return keyReleased;
	}

	public boolean isKeyTyped() {
		return keyTyped;
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
	// record movie
	// -------------------------------------------------------------------------

	/**
	 * Records the window as a movie file with background by H264 codec.
	 * 
	 * @param file
	 *            an output file.
	 */
	public void record(String file) {
		record(file, true, 0);
	}

	/**
	 * Records the window as a movie file by H264 codec.
	 * 
	 * @param file
	 *            an output file.
	 * @param background
	 *            if true, records with background.
	 * @param sec
	 *            records for specified seconds. if specifies 0, records until
	 *            call {@link #stopRecord()}.
	 */
	public void record(String file, boolean background, int sec) {
		record(file, background, sec, MovieCodec.getDefaultCodec());
	}

	/**
	 * Records the window as a movie file.
	 * 
	 * @param file
	 *            an output file.
	 * @param background
	 *            if true, records with background.
	 * @param sec
	 *            records for specified seconds. if specifies 0, records until
	 *            call {@link #stopRecord()}.
	 * @param codec
	 *            a codec of a movie.
	 */
	public void record(String file, boolean background, int sec,
			MovieCodec codec) {
		stopRecord();

		mediaWriter = ToolFactory.makeWriter(file);
		mediaWriter.addVideoStream(0, 0, MovieCodec.toXugglerCodec(codec),
				width, height);
		recordBackground = background;
		recordSpan = sec * 1000;
		recordTime = 0;
		recordFlag = true;
	}

	/**
	 * Stops recording.
	 */
	public void stopRecord() {
		recordFlag = false;
		if (mediaWriter != null) {
			mediaWriter.close();
		}
	}

	// -------------------------------------------------------------------------

	@Override
	public void drawWithGraphics(Graphics g) {
		this.drawObjects(g);

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

		// record movie
		if (recordFlag) {
			BufferedImage bi = Screenshot.readToBufferedImage(width, height,
					!recordBackground);
			if (recordTime <= 0) {
				recordTime = DateUtil.millis();
			}
			int elapse = DateUtil.millis() - recordTime;
			if (0 < recordSpan && recordSpan < elapse) {
				stopRecord();
			}
			mediaWriter.encodeVideo(0, bi, elapse, TimeUnit.MILLISECONDS);
		}
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

	public int getKeycode() {
		return keycode;
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
	
	public boolean isMousePressed(casmi.MouseButton button) {
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

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	
    private final void drawObjects(Graphics g) {
    	//g.render(rootObject);
    	//rootObject.selectionbufRender(g, getMouseX(), getMouseY());
    	rootObject.bufRender(g, getMouseX(), getMouseY(),false,0);
    	rootObject.selectionbufRender(g, getMouseX(), getMouseY(), 0);
    	update(g);
    }
    
    // TODO: should change access public to private final
    public void update(Graphics g) {
    	update();
    }
    
    private static TweenManager tweenManager = null;
    
    private TweenManager getTweenManager(){
    	if( tweenManager == null ) {
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
    
    public void clearTween(){
    	tweenManager = null;
    	rootObject.clearTweenManager();
    }

//    public void addTweenManager(TweenManager tweenmanager){
//    	rootObject.addTweenManager(tweenmanager);
//    }
//    
//    public void removeTweenManger(int index){
//    	rootObject.removeTweenManager(index);
//    }
//    
//    public void clearTweenManager(){
//   	rootObject.clearTweenManager();
//    }
        
   public void setPosition(double x, double y, double z){
	   rootObject.setPosition(x, y, z);
   }
   
   public void setPosition(double x, double y){
	   rootObject.setPosition(x, y);
   }
   

   public void setRotation(double angle, double x,double y, double z) {
	   rootObject.setRotation(angle, x, y, z);
   }
    
   public void addObject(Object r) {
       rootObject.add(r);
       
       // NOTE: ???
       if (rootObject instanceof TimelineRender) {
           timeline = true;
           rootTimelineRender = (TimelineRender)rootObject;
           if (rootTimelineRender instanceof Timeline) {
               rootTimeline = (Timeline)rootTimelineRender;
               rootTimeline.setApplet(this);
           }
       }
   }
   
   public void addObject(int index, Object r) {
       rootObject.add(index, r);
       
       // NOTE: ???
       if (rootObject instanceof TimelineRender) {
           timeline = true;
           rootTimelineRender = (TimelineRender) rootObject;
           if( rootTimelineRender instanceof Timeline){
               rootTimeline = (Timeline)rootTimelineRender;
               rootTimeline.setApplet(this);
           }
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
    
    public void setPerspective() {
		rootObject.addPerse(new Perspective());
	}

	public void setPerspective(double fov, double aspect, double zNear,	double zFar) {
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
		synchronized (this) {
			gl.glViewport(0, 0, this.width, this.height);

			g.ortho();
			gl.glClearStencil(0);
			gl.glEnable(GL.GL_DEPTH_TEST);
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT
					| GL.GL_STENCIL_BUFFER_BIT);

			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL.GL_BLEND);

			gl.glEnable(GL.GL_LINE_SMOOTH);

			if (d != null) {
				d.drawWithGraphics(g);
			}

			gl.glFlush();
		}
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
//	    System.out.println(x + ", " + y + ", " + width + ", " + height);
	    ((Applet)d).setAppletSize(width, height);    
	    this.setSize(width, height);
	}

	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		this.g.setWidth(w);
		this.g.setHeight(h);
	}
}
