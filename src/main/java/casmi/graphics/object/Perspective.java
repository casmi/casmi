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

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;

/**
 * @author Y. Ban
 */
public class Perspective extends Element implements ObjectRender,Perse {
	
    private double fov;
	private double aspect;
	private double zNear;
	private double zFar;
	private boolean def = false;
	
	public Perspective(double fov, double aspect, double zNear, double zFar) {
		this.fov = fov;
		this.aspect = aspect;
		this.zNear = zNear;
		this.zFar = zFar;
    }
	
	public Perspective() {
		def = true;
	}
	
	public void set(double fov, double aspect, double zNear, double zFar) {
		this.fov = fov;
		this.aspect = aspect;
		this.zNear = zNear;
		this.zFar = zFar;
    }
    
	@Override
	public void render(Graphics g) {
		if (def) {
			double cameraZ = ((g.getHeight() / 2.0) / Math.tan(Math.PI * 60.0 / 360.0));
			g.perspective(Math.PI / 3.0, (double) g.getWidth()
					/ (double) g.getHeight(), cameraZ / 10.0, cameraZ * 10.0);
		} else {
			g.perspective(fov, aspect, zNear, zFar);
		}
	}
	
    @Override
    public void render(GL gl, GLU glu, int width, int height) {}
    
    @Override
    public void simplerender(Graphics g) {
    	if (def) {
			g.simpleperspective();
		} else {
			g.simpleperspective(fov, aspect, zNear, zFar);
		}
    }
}
