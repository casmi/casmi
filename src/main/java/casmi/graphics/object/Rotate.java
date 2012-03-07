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
public class Rotate extends Element implements ObjectRender {
	
    private double x = 0.0;
	private double y = 0.0;
	private double z = 0.0;
	
	public Rotate(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
    }
	
	public void setRotate(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setRotateX(double angle) {
		this.x = angle;
	}
	
	public void setRotateY(double angle) {
		this.y = angle;
	}
	
	public void setRotateZ(double angle) {
		this.z = angle;
	}
	
	public double getRotateX() {
		return this.x;
	}
	
	public double getRotateY() {
		return this.y;
	}
	
	public double getRotateZ() {
		return this.z;
	}
	
	@Override
	public void render(Graphics g) {
		g.rotateX(x);
		g.rotateY(y);
		g.rotateZ(z);
	}
	
    @Override
    public void render(GL gl, GLU glu, int width, int height) {}
}
