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
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Element;
import casmi.matrix.Vertex;

/**
 * Light object class.
 * 
 * @author Y. Ban
 */
public class Light extends Element implements ObjectRender {
	
    public enum LightMode {
        AMBIENT, DIRECTION, POINT, SPOT
    }
    
	private Vertex dv;
	private int index;
	private Vertex v;
	private Color color;
	private double angle = 30.0;
	private LightMode lightMode = LightMode.AMBIENT;
	
	public Light() {
	    this(LightMode.AMBIENT);
    }
	
	public Light(LightMode lightMode) {
		this.lightMode = lightMode;
		this.v         = new Vertex();
		this.dv        = new Vertex();
		this.color     = new RGBColor(0.0, 0.0, 0.0);
    }
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setColor(ColorSet colorSet) {
		this.color = new RGBColor(colorSet);
	}
	
	public void setDirection(double x,double y, double z) {
		this.dv.set(x, y, z);
    }
	
	public LightMode getLightMode() {
	    return lightMode;
	}
 	
	public void setLightMode(LightMode lightMode) {
		this.lightMode = lightMode;
	}
    
	@Override
	public void render(Graphics g) {
		v.set(x, y, z);
		switch(lightMode){
		case AMBIENT:
			g.ambientLight(index, color, v);
			break;
		case DIRECTION:
			g.directionalLight(index, color, dv);
			break;
		case POINT:
			g.pointLight(index, color, v);
			break;
		case SPOT:
			g.spotLight(index, color, v, (float)dv.getX(), (float)dv.getY(), (float)dv.getZ(), (float)angle);
			break;
		}
	}
	
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
    }

	public double getDirectionX() {
		return dv.getX();
	}

	public void setDirectionX(double directionX) {
		this.dv.setX(directionX);
	}

	public double getDirectionY() {
		return dv.getY();
	}

	public void setDirectionY(double directionY) {
		this.dv.setY(directionY);
	}

	public double getDirectionZ() {
		return dv.getZ();
	}

	public void setDirectionZ(double directionZ) {
		this.dv.setZ(directionZ);
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
}