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
public class Translate extends Element implements ObjectRender {
	
    private double x = 0.0;
	private double y = 0.0;
	private double z = 0.0;
	
	public Translate(double x,double y,double z) {
		this.x = x;
		this.y = y;
		this.z = z;
    }
	
	public void setTranslate(double x,double y,double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setTranslateX(double x) {
		this.x = x;
	}
	
	public void setTranslateY(double y) {
		this.y = y;
	}
	
	public void setTranslateZ(double z) {
		this.z = z;
	}
	
	public double getTranslateX() {
		return this.x;
	}
	
	public double getTranslateY() {
		return this.y;
	}
	
	public double getTranslateZ() {
		return this.z;
	}
	
	@Override
	public void render(Graphics g) {
		g.translate(x, y, z);
	}
	
    @Override
    public void render(GL gl, GLU glu, int width, int height) {}
}
