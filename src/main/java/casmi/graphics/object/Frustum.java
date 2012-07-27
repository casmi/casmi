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
public class Frustum extends Element implements ObjectRender, Perse {
    
	private double left;
	private double right;
	private double bottom;
	private double top;
	private double near;
	private double far;
	private boolean def = false;
	
	public Frustum(double left, double right, double bottom, double top,
			double near, double far) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.near = near;
		this.far = far;
    }
	
	public Frustum(){
		def = true;
	}
	
	public void set(double left, double right, double bottom, double top,
			double near, double far) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.near = near;
		this.far = far;
    }
    
	@Override
	public void render(Graphics g){
		if(def){
			g.frustum();
		} else {
			g.frustum(left, right, bottom, top, near, far);
		}
	}
	
	@Override
	public void simplerender(Graphics g){
		if(def){
			g.simplefrustum();
		} else {
			g.simplefrustum(left, right, bottom, top, near, far);
		}
	}
	
    @Override
    public void render(GL2 gl, GLU glu, int width, int height) {
    }
    
}
