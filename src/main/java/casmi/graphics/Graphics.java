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

package casmi.graphics;

import java.nio.DoubleBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.color.Color;
import casmi.graphics.element.Renderable;
import casmi.image.Image;
import casmi.matrix.Vertex;
import casmi.timeline.TimelineRender;

import com.sun.opengl.util.GLUT;

/**
 * Graphics class. Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Graphics {

	private GL gl;
	private GLU glu;
	private GLUT glut;

	private int width;
	private int height;
	private double sceneAlpha = 1.0;

	private static List<Image> textureImages = new CopyOnWriteArrayList<Image>();

	public void render(Renderable r) {
		r.setAlpha(sceneAlpha);
		r.render(this.gl, this.glu, this.width, this.height);
	}
	
	public void render(TimelineRender tr){
		tr.render(this);
	}

	public Graphics(GL g, GLU Glu, GLUT Glut, int width, int height) {
		this.gl = g;
		this.glu = Glu;
		this.glut = Glut;

		this.width = width;
		this.height = height;
	}

	public GL getGL(){
	    return gl;
	}
	
	public GLU getGLU(){
	    return glu;
	}
	
	public GLUT getGLUT(){
	    return glut;
	}
	
	public void init() {
		for (Image img : textureImages) {
			img.unloadTexture();
			img.loadTexture();
		}
	}

	/**
	 * Returns the width of the display window. 
	 */
	public int getWidth() {
		return width;
	}

	/**
     * Sets the width of the display window. 
     */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
     * Returns the height of the display window. 
     */
	public int getHeight() {
		return height;
	}

	/**
     * Sets the height of the display window. 
     */
	public void setHeight(int height) {
		this.height = height;
	}

	// Background Color

	/**
	  *Sets the background to a grayscale value.
	  *
	  * @param gray 
	  *            The grayscale value of the background.
	  */
	public void background(float gray) {
		gl.glClearColor(gray / 255, gray / 255, gray / 255, 1);
	}
	
	/**
     *Sets the background to a grayscale value.
     *
     * @param gray 
     *            The grayscale value of the background.
     * @param alpha
     *            The alpha opacity of the background.           
     */
	public void background(float gray, float alpha) {
		gl.glClearColor(gray / 255, gray / 255, gray / 255, alpha / 255);
	}

	/**
     *Sets the background to a RGB and value.
     *
     * @param x,y,z 
     *            The RGB value of the background.
     */
	public void background(float x, float y, float z) {
		gl.glClearColor(x / 255, y / 255, z / 255, 1);
	}

	/**
     *Sets the background to a RGB and alpha value.
     *
     * @param x,y,z 
     *            The RGB value of the background.
     * @param alpha
     *            The alpha opacity of the background.           
     */
	public void background(float x, float y, float z, float a) {
		gl.glClearColor(x / 255, y / 255, z / 255, a / 255);
	}
	
	/**
     *Sets the background to a RGB or HSB and alpha value.
     *
     * @param c 
     *            The RGB or HSB value of the background.
     */
	public void background(Color c) {
	    c.calcColor();
	    gl.glClearColor(c.getNormalR(), c.getNormalG(), c.getNormalB(), (float) (c.getNormalA()*sceneAlpha));
	}
	
	
	public void setcolor(Color c){
		c.calcColor();
		gl.glColor4d(c.getNormalR(), c.getNormalG(), c.getNormalB(), c.getNormalA()*sceneAlpha);
	}
	
	public void setcolor(float gray){
		//System.out.println(sceneAlpha);
		gl.glColor4d(gray/255.0f, gray/255.0f, gray/255.0f, 1.0*sceneAlpha);
	}
	
	public void setcolor(int x, int y, int z, int a){
		Color c = new Color(x, y, z, a);
		c.calcColor();
		gl.glColor4d(c.getNormalR(), c.getNormalG(), c.getNormalB(), c.getNormalA()*sceneAlpha);
	}
	
	public void setcolor(int x, int y, int z){
		setcolor(x, y, z, 255);
	}
	

	// Matrix Stack

	/**
	 * Pushes a copy of the current transformation matrix onto the stack.
	 */
	public void pushMatrix() {
		gl.glPushMatrix();
	}

	/**
	 * Replaces the current transformation matrix with the top of the stack.
	 */
	public void popMatrix() {
		gl.glPopMatrix();
	}

	/**
	 * Resets the current transformation matrix.
	 */
	public void resetMatrix() {
		gl.glLoadIdentity();
	}

	/**
	 * Applies the transformation matrix.
	 */
	public void applyMatrix(double n[]) {
		gl.glMultMatrixd(java.nio.DoubleBuffer.wrap(n));
	}
	
	public void applyMatrix(DoubleBuffer n){
		gl.glMultMatrixd(n);
	}

	/**
	 * Loads the transformation matrix.
	 */
	public void loadMatrix(double n[]) {
		gl.glLoadMatrixd(java.nio.DoubleBuffer.wrap(n));
	}
	
	public void loadMatrix(DoubleBuffer n){
		gl.glLoadMatrixd(n);
	}

	public enum MatrixMode {
		PROJECTION, MODELVIEW
	};

	/**
	 * Sets the MatrixMode.
	 * 
	 * @param mode
	 *             Either PROJECTION or MODELVIEW
	 */
	public void matrixMode(MatrixMode mode) {
		switch (mode) {
		case PROJECTION:
			gl.glMatrixMode(GL.GL_PROJECTION);
			break;
		case MODELVIEW:
			gl.glMatrixMode(GL.GL_MODELVIEW);
			break;
		default:
			break;
		}
	}

	// Matrix Transform

	/**
	 * Translates in X and Y.
	 */
	public void translate(double tx, double ty) {
		gl.glTranslated(tx, ty, 0.0);
	}

	/**
	 * Translates in X, Y and Z.
	 */
	public void translate(double tx, double ty, double tz) {
		gl.glTranslated(tx, ty, tz);
	}

	/**
	 * Rotates around the X axis.
	 */
	public void rotateX(double angle) {
		gl.glRotated(angle, 1.0, 0, 0);
	}

	/**
     * Rotates around the Y axis.
     */
	public void rotateY(double angle) {
		gl.glRotated(angle, 0, 1.0, 0);
	}

	/**
     * Rotates around the Z axis.
     */
	public void rotateZ(double angle) {
		gl.glRotated(angle, 0, 0, 1.0);
	}

	/**
	 * Rotates about a vector in space. Same as the glRotated() function.
	 */
	public void rotate(double angle, double vx, double vy, double vz) {
		gl.glRotated(angle, vx, vy, vz);
	}

	/**
	 * Scales in all dimensions.
	 */
	public void scale(double s) {
		gl.glScaled(s, s, 1.0);
	}
	
	/**
	 * Scales in X and Y. Equivalent to scale(sx, sy, 1).
	 */
	public void scale(double sx, double sy) {
		gl.glScaled(sx, sy, 1.0);
	}

	/**
     * Scales in X ,Y and Z.
     */
	public void scale(double sx, double sy, double sz) {
		gl.glScaled(sx, sy, sz);
	}

	// Light
    /**
     * Sets the RGB value of the ambientLight
     */
	public void ambientLight(float r, float g, float b) {
		float ambient[] = { r, g, b, 255 };
		normalize(ambient);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient, 0);
	}

	/**
     * Sets the RGB value and the position of the ambientLight
     */
	public void ambientLight(float r, float g, float b, float x, float y,
			float z) {
		float ambient[] = { r, g, b, 255 };
		float position[] = { x, y, z, 1.0f };
		normalize(ambient);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient, 0);
	}

	/**
     * Sets the RGB value and the position of the ambientLight
     */
	public void ambientLight(float r, float g, float b, Vertex v) {
		float ambient[] = { r, g, b, 255 };
		float position[] = { (float) v.x, (float) v.y, (float) v.z, 1.0f };
		normalize(ambient);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient, 0);
	}

	/**
     * Sets the color value of the ambientLight
     */
	public void ambientLight(Color c) {
		c.calcColor();
		float ambient[] = { c.getNormalR(), c.getNormalG(), c.getNormalB(),
				c.getNormalA() };
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient, 0);
	}
	
	/**
     * Sets the color value and the position of the ambientLight
     */
	public void ambientLight(Color c, Vertex v) {
        c.calcColor();
        float ambient[] = { c.getNormalR(), c.getNormalG(), c.getNormalB(),
                c.getNormalA() };
        float position[] = { (float) v.x, (float) v.y, (float) v.z, 1.0f };
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient, 0);
    }

	/**
     * Sets the RGB value of the No.i ambientLight
     */
	public void ambientLight(int i, float r, float g, float b) {
		float ambient[] = { r, g, b, 255 };
		normalize(ambient);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0 + i);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_AMBIENT, ambient, 0);
	}

	/**
     * Sets the RBG value and the position of the No.i ambientLight
     */
	public void ambientLight(int i, float r, float g, float b, float x,
			float y, float z) {
		float ambient[] = { r, g, b, 255 };
		float position[] = { x, y, z, 1.0f };
		normalize(ambient);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0 + i);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_POSITION, position, 0);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_AMBIENT, ambient, 0);
	}

	/**
     * Sets the RBG value and the position of the No.i ambientLight
     */
	public void ambientLight(int i, float r, float g, float b, Vertex v) {
		float ambient[] = { r, g, b, 255 };
		float position[] = { (float) v.x, (float) v.y, (float) v.z, 1.0f };
		normalize(ambient);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0 + i);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_POSITION, position, 0);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_AMBIENT, ambient, 0);
	}

	/**
     * Sets the color value of the No.i ambientLight
     */
	public void ambientLight(int i, Color c) {
		c.calcColor();
		float ambient[] = { c.getNormalR(), c.getNormalG(), c.getNormalB(),
				c.getNormalA() };
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0 + i);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_AMBIENT, ambient, 0);
	}
	
	/**
     * Sets the color value and the position of the No.i ambientLight
     */
	public void ambientLight(int i, Color c, Vertex v) {
        c.calcColor();
        float ambient[] = { c.getNormalR(), c.getNormalG(), c.getNormalB(),
                c.getNormalA() };
        float position[] = { (float) v.x, (float) v.y, (float) v.z, 1.0f };
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0 + i);
        gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_POSITION, position, 0);
        gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_AMBIENT, ambient, 0);
    }

	/**
     * Sets the color value and the position of the No.i directionalLight
     */
	public void directionalLight(int i, Color c, float x, float y, float z) {
		c.calcColor();
		float color[] = { c.getNormalR(), c.getNormalG(), c.getNormalB(),
				c.getNormalA() };
		float pos[] = { x, y, z, 0 };
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0 + i);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_POSITION, pos, 0);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_DIFFUSE, color, 0);
	}

	/**
     * Sets the color value and the position of the No.i directionalLight
     */
	public void directionalLight(int i, Color c, Vertex v) {
		c.calcColor();
		float color[] = { c.getNormalR(), c.getNormalG(), c.getNormalB(),
				c.getNormalA() };
		float pos[] = { (float) v.x, (float) v.y, (float) v.z, 0 };
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0 + i);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_POSITION, pos, 0);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_DIFFUSE, color, 0);
	}

	/**
     * Sets the color value and the position of the No.i pointLight
     */
	public void pointLight(int i, Color c, float x, float y, float z) {
		c.calcColor();
		float color[] = { c.getNormalR(), c.getNormalG(), c.getNormalB(),
				c.getNormalA() };
		float pos[] = { x, y, z, 0 };
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0 + i);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_POSITION, pos, 0);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_DIFFUSE, color, 0);
	}

	/**
     * Sets the color value and the position of the No.i pointLight
     */
	public void pointLight(int i, Color c, Vertex v) {
		c.calcColor();
		float color[] = { c.getNormalR(), c.getNormalG(), c.getNormalB(),
				c.getNormalA() };
		float pos[] = { (float) v.x, (float) v.y, (float) v.z, 0 };
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0 + i);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_POSITION, pos, 0);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_DIFFUSE, color, 0);
	}

	/**
     * Sets the color value, position, direction and the angle of the spotlight cone of the No.i spotLight
     */
	public void spotLight(int i, Color c, Vertex v, float nx, float ny,
			float nz, float angle) {
		c.calcColor();
		float color[] = { c.getNormalR(), c.getNormalG(), c.getNormalB(),
				c.getNormalA() };
		float pos[] = { (float) v.x, (float) v.y, (float) v.z, 0 };
		float direction[] = { nx, ny, nz };
		float a[] = { angle };
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0 + i);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_POSITION, pos, 0);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_DIFFUSE, color, 0);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_SPOT_DIRECTION, direction, 0);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_SPOT_CUTOFF, a, 0);
	}

	/**
	 * Sets the falloff rates for point lights, spot lights, and ambient lights.
	 */
	public void lightFalloff(int i, float constant, float liner, float quadratic) {
		float c[] = { constant };
		float l[] = { liner };
		float q[] = { quadratic };
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0 + i);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_CONSTANT_ATTENUATION, c, 0);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_LINEAR_ATTENUATION, l, 0);
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_QUADRATIC_ATTENUATION, q, 0);
	}

	/**
	 * Sets the specular color for No.i light. 
	 */
	public void lightSpecular(int i, Color c) {
		c.calcColor();
		float color[] = { c.getNormalR(), c.getNormalG(), c.getNormalB(),
				c.getNormalA() };
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_SPECULAR, color, 0);
	}

	/**
     * Sets the diffuse color for No.i light. 
     */
	public void lightDiffuse(int i, Color c) {
		c.calcColor();
		float color[] = { c.getNormalR(), c.getNormalG(), c.getNormalB(),
				c.getNormalA() };
		gl.glLightfv(GL.GL_LIGHT0 + i, GL.GL_DIFFUSE, color, 0);
	}

	/**
	 * Returns the array normalized from 0-255 to 0-1.0.
	 */
	public float[] normalize(float in[]) {
		float out[] = new float[in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = (float) (in[i] / 255.0f);
		}
		return out;
	}

	//Image
	/**
	 * Starts to draw polygon.
	 */
	public void beginShape() {
		gl.glBegin(GL.GL_POLYGON);
	}

	/**
	 * Ends to draw polygon.
	 */
	public void endShape() {
		gl.glEnd();
	}

	/**
	 * Enables texture.
	 */
	public void texture(Image image) {
		image.enableTexture();
	}

	/**
     * Disables texture.
     */
	public void notexture(Image image) {
		image.disableTexture();
	}

	/**
	 * Sets texture vertex(nx,ny) at position(x,y).
	 * */
	public void vertex(float x, float y, float nx, float ny) {
		gl.glTexCoord2f(nx, ny);
		gl.glVertex2f(x, y);
	}

	/**
     * Sets texture vertex(nx,ny) at position(x,y,z).
     * */
	public void vertex(float x, float y, float z, float nx, float ny) {
		gl.glTexCoord2f(nx, ny);
		gl.glVertex3f(x, y, z);
	}

	/**
     * Sets texture vertex(nx,ny) at position v.
     * */
	public void vertex(Vertex v, float nx, float ny) {
		gl.glTexCoord2f(nx, ny);
		gl.glVertex3d(v.x, v.y, v.z);
	}
	
	/**
	 * Displays texture img at position (x,y). 
	 * If 
	 */
	public void image(Image img, double x, double y) {
		if (img.texture != null) {
		    gl.glDisable(GL.GL_DEPTH_TEST);
		    img.enableTexture();
			gl.glBindTexture(GL.GL_TEXTURE_2D, img.texture.getTextureObject());
			gl.glBegin(GL.GL_QUADS);
			switch (img.mode) {
			default:
			case CORNER:
				gl.glTexCoord2f(0.0f, 1.0f);
				gl.glVertex2f((float) x, (float) y - img.height);
				gl.glTexCoord2f(0.0f, 0.0f);
				gl.glVertex2f((float) x, (float) y);
				gl.glTexCoord2f(1.0f, 0.0f);
				gl.glVertex2f((float) x + img.width, (float) y);
				gl.glTexCoord2f(1.0f, 1.0f);
				gl.glVertex2f((float) x + img.width, (float) y - img.height);
				break;
			case CENTER:
				gl.glTexCoord2f(0.0f, 1.0f);
				gl.glVertex2f((float) x - img.width / 2.0f, (float) y
						- img.height / 2.0f);
				gl.glTexCoord2f(0.0f, 0.0f);
				gl.glVertex2f((float) x - img.width / 2.0f, (float) y
						+ img.height / 2.0f);
				gl.glTexCoord2f(1.0f, 0.0f);
				gl.glVertex2f((float) x + img.width / 2.0f, (float) y
						+ img.height / 2.0f);
				gl.glTexCoord2f(1.0f, 1.0f);
				gl.glVertex2f((float) x + img.width / 2.0f, (float) y
						- img.height / 2.0f);
				break;
			}
			gl.glEnd();
			img.disableTexture();
			gl.glEnable(GL.GL_DEPTH_TEST);
		}

	}

	/**
	 *  Displays texture img at the point(x,y) and sets the width and height (w,h) of the texture.
	 *  A call to imageMode(CORNERS) will change the width and height parameters to define
	 *  the x and y values of the opposite corner of the image. 
	 * */
	public void image(Image img, double x, double y, double w, double h) {
		if (img.texture != null) {
            gl.glDisable(GL.GL_DEPTH_TEST);
			img.enableTexture();
			gl.glBindTexture(GL.GL_TEXTURE_2D, img.texture.getTextureObject());
			gl.glBegin(GL.GL_QUADS);
			switch (img.mode) {
			default:
			case CORNER:
				gl.glTexCoord2f(0.0f, 1.0f);
				gl.glVertex2f((float) x, (float) (y - h));
				gl.glTexCoord2f(0.0f, 0.0f);
				gl.glVertex2f((float) x, (float) y);
				gl.glTexCoord2f(1.0f, 0.0f);
				gl.glVertex2f((float) (x + w), (float) y);
				gl.glTexCoord2f(1.0f, 1.0f);
				gl.glVertex2f((float) (x + w), (float) (y - h));
				break;
			case CORNERS:
				gl.glTexCoord2f(0.0f, 1.0f);
				gl.glVertex2f((float) x, (float) h);
				gl.glTexCoord2f(0.0f, 0.0f);
				gl.glVertex2f((float) x, (float) y);
				gl.glTexCoord2f(1.0f, 0.0f);
				gl.glVertex2f((float) w, (float) y);
				gl.glTexCoord2f(1.0f, 1.0f);
				gl.glVertex2f((float) w, (float) h);
				break;
			case CENTER:
				gl.glTexCoord2f(0.0f, 1.0f);
				gl.glVertex2f((float) (x - w / 2.0), (float) (y - h / 2.0));
				gl.glTexCoord2f(0.0f, 0.0f);
				gl.glVertex2f((float) (x - w / 2.0), (float) (y + h / 2.0));
				gl.glTexCoord2f(1.0f, 0.0f);
				gl.glVertex2f((float) (x + w / 2.0), (float) (y + h / 2.0));
				gl.glTexCoord2f(1.0f, 1.0f);
				gl.glVertex2f((float) (x + w / 2.0), (float) (y - h / 2.0));
				break;
			}
			gl.glEnd();
			img.disableTexture();
            gl.glEnable(GL.GL_DEPTH_TEST);
		}

	}

	// textures
	public static void addTextureImage(Image img) {
		textureImages.add(img);
	}
	
	public static void reloadTextures() {
	    for (Image img : textureImages) {
            img.unloadTexture();
            img.loadTexture();
        }
	}

	// camera
	/**
	 * Sets a perspective projection applying foreshortening, making distant objects appear smaller 
	 * than closer ones. The parameters define a viewing volume with the shape of truncated pyramid. 
	 * Objects near to the front of the volume appear their actual size, while farther objects appear 
	 * smaller. This projection simulates the perspective of the world more accurately than orthographic 
	 * projection. 
	 * 
	 * @param fov
	 *             field-of-view angle for vertical direction
	 * @param aspect
	 *             ratio of width to height
	 * @param zNear
	 *             z-position of nearest clipping plane
	 * @param zFar
	 *             z-position of nearest farthest plane
	 */
	public void perspective(double fov, double aspect, double zNear, double zFar) {
		matrixMode(MatrixMode.PROJECTION);
		resetMatrix();
		glu.gluPerspective(fov, aspect, zNear, zFar);
		matrixMode(MatrixMode.MODELVIEW);
		resetMatrix();
	}

	/**
	 * Sets a default perspective. 
	 */
	public void perspective() {
		double cameraZ = ((height / 2.0) / Math.tan(Math.PI * 60.0 / 360.0));
		matrixMode(MatrixMode.PROJECTION);
		resetMatrix();
		glu.gluPerspective(Math.PI / 3.0, (double) this.width
				/ (double) this.height, cameraZ / 10.0, cameraZ * 10.0);
		matrixMode(MatrixMode.MODELVIEW);
		resetMatrix();
	}

	/**
	 * Sets an orthographic projection and defines a parallel clipping volume. All objects with the same 
	 * dimension appear the same size, regardless of whether they are near or far from the camera. The parameters 
	 * to this function specify the clipping volume where left and right are the minimum and maximum x values, 
	 * top and bottom are the minimum and maximum y values, and near and far are the minimum and maximum z values.
	 * 
	 * @param left
	 *             left plane of the clipping volume
	 * @param right
	 *             right plane of the clipping volume
	 * @param bottom
	 *             bottom plane of the clipping volume
	 * @param top
	 *             top plane of the clipping volume
	 * @param near
	 *             maximum distance from the origin to the viewer
	 * @param far
	 *             maximum distance from the origin away from the viewer
	 */
	public void ortho(double left, double right, double bottom, double top,
			double near, double far) {
		matrixMode(MatrixMode.PROJECTION);
		resetMatrix();
		gl.glOrtho(left, right, bottom, top, near, far);
		matrixMode(MatrixMode.MODELVIEW);
		resetMatrix();
	}

	/**
	 * Sets the default orthographic projection.
	 */
	public void ortho() {
		matrixMode(MatrixMode.PROJECTION);
		resetMatrix();
		gl.glOrtho(0, this.width, 0, this.height, -1.0e10, 1.0e10);
		matrixMode(MatrixMode.MODELVIEW);
		resetMatrix();
	}

	/**Sets a perspective matrix defined through the parameters. Works like
	 * glFrustum, except it wipes out the current perspective matrix rather 
	 * than muliplying itself with it.
	 * 
	 * @param left
	 *             left coordinate of the clipping plane
	 * @param right
	 *             right coordinate of the clipping plane
	 * @param bottom
	 *             bottom coordinate of the clipping plane
	 * @param top
	 *             top coordinate of the clipping plane
	 * @param near
	 *             near component of the clipping plane
	 * @param far
	 *             far component of the clipping plane 
	 */
	public void frustum(double left, double right, double bottom, double top,
			double near, double far) {
		matrixMode(MatrixMode.PROJECTION);
		resetMatrix();
		gl.glFrustum(left, right, bottom, top, near, far);
		matrixMode(MatrixMode.MODELVIEW);
		resetMatrix();
	}

	/**
	 * Sets the position of the camera through setting the eye position, the 
	 * center of the scene, and which axis is facing upward. Moving the eye 
	 * position and the direction it is pointing (the center of the scene) 
	 * allows the images to be seen from different angles. 
	 * 
	 * @param eyeX
	 *             x-coordinate for the eye
	 * @param eyeY
	 *             y-coordinate for the eye
	 * @param eyeZ
	 *             z-coordinate for the eye
	 * @param centerX
	 *             x-coordinate for the center of the scene
	 * @param centerY
	 *             x-coordinate for the center of the scene
	 * @param centerZ
	 *             z-coordinate for the center of the scene
	 * @param upX
	 *             usually 0.0, 1.0, or -1.0    
	 * @param upY
	 *             usually 0.0, 1.0, or -1.0
	 * @param upZ
	 *             usually 0.0, 1.0, or -1.0
	 */
	public void camera(double eyeX, double eyeY, double eyeZ, double centerX,
			double centerY, double centerZ, double upX, double upY, double upZ) {
		glu.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY,
				upZ);
	}

	/**
	 * Sets the default camera position.
	 */
	public void camera() {
		glu.gluLookAt(width / 2.0, height / 2.0,
				(height / 2.0) / Math.tan(Math.PI * 60.0 / 360.0), width / 2.0,
				height / 2.0, 0, 0, 1, 0);
	}
	
	public void setSceneA(double a){
		this.sceneAlpha = a;
	}
	
	public double getSceneA(){
		return this.sceneAlpha;
	}
	

}
