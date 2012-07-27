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
	
	public void setEye(double eyeX, double eyeY, double eyeZ) {
	    this.eyeX = eyeX;
	    this.eyeY = eyeY;
	    this.eyeZ = eyeZ;
	}
	
	public void setCenter(double centerX, double centerY, double centerZ) {
	    this.centerX = centerX;
	    this.centerY = centerY;
	    this.centerZ = centerZ;
	}
	
	public void setOrientation(double upX, double upY, double upZ) {
	    this.upX = upX;
	    this.upY = upY;
	    this.upZ = upZ;
	}
	
	public double getEyeX() {
		return eyeX;
	}

	public void setEyeX(double eyeX) {
		this.eyeX = eyeX;
	}

	public double getEyeY() {
		return eyeY;
	}

	public void setEyeY(double eyeY) {
		this.eyeY = eyeY;
	}

	public double getEyeZ() {
		return eyeZ;
	}

	public void setEyeZ(double eyeZ) {
		this.eyeZ = eyeZ;
	}

	public double getCenterX() {
		return centerX;
	}

	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}

	public double getCenterZ() {
		return centerZ;
	}

	public void setCenterZ(double centerZ) {
		this.centerZ = centerZ;
	}
	
    @Override
    public void render(GL2 gl, GLU glu, int width, int height) {
    }
}
