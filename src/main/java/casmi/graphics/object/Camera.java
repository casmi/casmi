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

package casmi.graphics.object;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;

/**
 * Camera class.
 * Wrap JOGL and make it easy to use. This class is similar to gluLookAt() in OpenGL
 * 
 * @author Y. Ban
 */
public class Camera extends Element implements ObjectRender {
	
    private double eyeX;
	private double eyeY;
	private double eyeZ;
	
	private double centerX;
	private double centerY;
	private double centerZ;
	
	private double upX;
	private double upY;
	private double upZ;
	
	private boolean def = false;
	
	/**
	 * Creates Camera object setting the eye position, the center of the scene and which axis is facing upward.
	 * 
	 * @param eyeX
	 * 					The x-coordinate for the eye.
	 * @param eyeY
	 * 					The y-coordinate for the eye.
	 * @param eyeZ
	 * 					The z-coordinate for the eye.
	 * @param centerX
	 * 					The x-coordinate for the center of the scene.
	 * @param centerY
	 * 					The y-coordinate for the center of the scene.
	 * @param centerZ
	 * 					The z-coordinate for the center of the scene.
	 * @param upX
	 * 					Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
	 * @param upY
	 * 					Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
	 * @param upZ
	 * 					Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
	 */
	public Camera(double eyeX,    double eyeY,    double eyeZ, 
	              double centerX, double centerY, double centerZ,
	              double upX,     double upY,     double upZ) {
	    
		this.eyeX = eyeX;
		this.eyeY = eyeY;
		this.eyeZ = eyeZ;
		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;
		this.upX = upX;
		this.upY = upY;
		this.upZ = upZ;
    }
	
	/**
	 * Creates Camera object using default parameter.
	 * The default values are camera(width/2.0, height/2.0,
	 *  (height/2.0) / tan(PI*60.0 / 360.0), width/2.0, height/2.0, 0, 0, 1, 0)
	 */
	public Camera() {
		def = true;
	}
	
	@Override
	public void render(Graphics g) {
		if (def) {
			g.camera();
		} else {
			g.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		}
	}
	
	/**
	 * Sets the eye position, the center of the scene and which axis is facing upward.
	 * 
	 * @param eyeX
	 * 					The x-coordinate for the eye.
	 * @param eyeY
	 * 					The y-coordinate for the eye.
	 * @param eyeZ
	 * 					The z-coordinate for the eye.
	 * @param centerX
	 * 					The x-coordinate for the center of the scene.
	 * @param centerY
	 * 					The y-coordinate for the center of the scene.
	 * @param centerZ
	 * 					The z-coordinate for the center of the scene.
	 * @param upX
	 * 					Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
	 * @param upY
	 * 					Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
	 * @param upZ
	 * 					Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
	 */
	public void set(double eyeX,    double eyeY,    double eyeZ, 
	                double centerX,	double centerY, double centerZ,
	                double upX,     double upY,     double upZ) {
	    
		this.eyeX = eyeX;
		this.eyeY = eyeY;
		this.eyeZ = eyeZ;
		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;
		this.upX = upX;
		this.upY = upY;
		this.upZ = upZ;
    }
	
	/**
	 * Sets the eye position of this Camera.
	 * 
	 * @param eyeX
	 * 					The x-coordinate for the eye.
	 * @param eyeY
	 * 					The y-coordinate for the eye.
	 * @param eyeZ
	 * 					The z-coordinate for the eye.
	 */
	public void setEye(double eyeX, double eyeY, double eyeZ) {
	    this.eyeX = eyeX;
	    this.eyeY = eyeY;
	    this.eyeZ = eyeZ;
	}
	
	/**
	 * Sets the center of the scene of this Camera.
	 * 
	 * @param centerX
	 * 					The x-coordinate for the center of the scene.
	 * @param centerY
	 * 					The y-coordinate for the center of the scene.
	 * @param centerZ
	 * 					The z-coordinate for the center of the scene.
	 */
	public void setCenter(double centerX, double centerY, double centerZ) {
	    this.centerX = centerX;
	    this.centerY = centerY;
	    this.centerZ = centerZ;
	}
	
	/**
	 * Sets which axis is facing upward.
	 * 
	 * @param upX
	 * 					Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
	 * @param upY
	 * 					Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
	 * @param upZ
	 * 					Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
	 */
	public void setOrientation(double upX, double upY, double upZ) {
	    this.upX = upX;
	    this.upY = upY;
	    this.upZ = upZ;
	}
	
	/**
	 * Returns the x-coordinate of the eye position of this Camera.
	 * 
	 * @return
	 * 					The x-coordinate for the eye.
	 */
	public double getEyeX() {
		return eyeX;
	}

	/**
	 * Sets the x-coordinate of the eye position of this Camera.
	 * 
	 * @param eyeX
	 * 					The x-coordinate for the eye.
	 */
	public void setEyeX(double eyeX) {
		this.eyeX = eyeX;
	}

	/**
	 * Returns the y-coordinate of the eye position of this Camera.
	 * 
	 * @return
	 * 					The y-coordinate for the eye.
	 */
	public double getEyeY() {
		return eyeY;
	}

	/**
	 * Sets the y-coordinate of the eye position of this Camera.
	 * 
	 * @param eyeY
	 * 					The y-coordinate for the eye.
	 */
	public void setEyeY(double eyeY) {
		this.eyeY = eyeY;
	}

	/**
	 * Returns the z-coordinate of the eye position of this Camera.
	 * 
	 * @return
	 * 					The z-coordinate for the eye.
	 */
	public double getEyeZ() {
		return eyeZ;
	}

	/**
	 * Sets the z-coordinate of the eye position of this Camera.
	 * 
	 * @param eyeZ
	 * 					The z-coordinate for the eye.
	 */
	public void setEyeZ(double eyeZ) {
		this.eyeZ = eyeZ;
	}

	/**
	 * Returns the center of the scene of this Camera.
	 * 
	 * @return
	 * 					The X-coordinate for the center of the scene.
	 */
	public double getCenterX() {
		return centerX;
	}

	/**
	 * Sets the center of the scene of this Camera.
	 * 
	 * @param centerX
	 * 					The X-coordinate for the center of the scene.
	 */
	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}

	/**
	 * Returns the center of the scene of this Camera.
	 * 
	 * @return
	 * 					The Y-coordinate for the center of the scene.
	 */
	public double getCenterY() {
		return centerY;
	}

	/**
	 * Sets the center of the scene of this Camera.
	 * 
	 * @param centerY
	 * 					The Y-coordinate for the center of the scene.
	 */
	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}

	/**
	 * Returns the center of the scene of this Camera.
	 * 
	 * @return
	 * 					The Z-coordinate for the center of the scene.
	 */
	public double getCenterZ() {
		return centerZ;
	}

	/**
	 * Sets the center of the scene of this Camera.
	 * 
	 * @param centerZ
	 * 					The Z-coordinate for the center of the scene.
	 */
	public void setCenterZ(double centerZ) {
		this.centerZ = centerZ;
	}
	
    @Override
    public void render(GL2 gl, GLU glu, int width, int height) {
    }
}
