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

package casmi.graphics.element;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.matrix.Vertex;

/**
 * Polygon class. Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Polygon extends Element implements Renderable {

	public static final int LINES     = 1;
	public static final int LINES_3D  = 3;
	public static final int LINE_LOOP = 51;

	private List<Double> cornerX;
	private List<Double> cornerY;
	private List<Double> cornerZ;

	private double centerX = 0;
	private double centerY = 0;
	private int size;

	private Vertex tmpV = new Vertex(0, 0, 0);

	private ArrayList<Color> cornerColor;

	private int MODE;

	public Polygon() {
		cornerX = new ArrayList<Double>();
		cornerY = new ArrayList<Double>();
		cornerZ = new ArrayList<Double>();
	}

	public void vertex(float x, float y) {
		MODE = LINES;
		this.cornerX.add((double) x);
		this.cornerY.add((double) y);
		this.cornerZ.add( 0d);
		setSize(this.cornerX.size());
		calcG();
	}

	public void vertex(float x, float y, float z) {
		MODE = LINES_3D;
		this.cornerX.add((double) x);
		this.cornerY.add((double) y);
		this.cornerZ.add((double) z);
		setSize(this.cornerX.size());
		calcG();
	}

	public void vertex(double x, double y) {
		MODE = LINES;
		this.cornerX.add(x);
		this.cornerY.add(y);
		this.cornerZ.add(0d);
		setSize(this.cornerX.size());
		calcG();
	}

	public void vertex(double x, double y, double z) {
		MODE = LINES_3D;
		this.cornerX.add(x);
		this.cornerY.add(y);
		this.cornerZ.add(z);
		setSize(this.cornerX.size());
		calcG();
	}

	public void vertex(Vertex v) {
		MODE = LINES_3D;
		this.cornerX.add(v.getX());
		this.cornerY.add(v.getY());
		this.cornerZ.add(v.getZ());
		setSize(this.cornerX.size());
		calcG();
	}

	public Vertex getVertex(int index) {
		tmpV.setX(cornerX.get(index));
		tmpV.setY(cornerY.get(index));
		tmpV.setZ(cornerZ.get(index));
		calcG();
		return tmpV;
	}

	public void removeVertex(int index) {
		this.cornerX.remove(index);
		this.cornerY.remove(index);
		this.cornerZ.remove(index);
		if (isGradation() == true)
			this.cornerColor.remove(index);
		setSize(this.cornerX.size());
		calcG();
	}

	public void setVertex(int i, double x, double y) {
		this.cornerX.set(i, x);
		this.cornerY.set(i, y);
		this.cornerZ.set(i, 0d);
		calcG();
	}

	public void setVertex(int i, double x, double y, double z) {
		this.cornerX.set(i, x);
		this.cornerY.set(i, y);
		this.cornerZ.set(i, z);
		calcG();
	}

	@Override
	public void render(GL2 gl, GLU glu, int width, int height) {
		if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || this.isDepthTest()==false) {
			gl.glDisable(GL2.GL_DEPTH_TEST);
		}

		double tmpx, tmpy, tmpz;

		gl.glPushMatrix();
//		gl.glTranslated(X, Y, 0);
//		gl.glRotated(rotate, 0, 0, 1.0);
		this.setTweenParameter(gl);

		switch (MODE) {
		case LINES:
			if (this.fill) {
				getSceneFillColor().setup(gl);
				gl.glBegin(GL2.GL_POLYGON);
				for (int i = 0; i < this.size; i++) {
					tmpx = (Double) this.cornerX.get(i);
					tmpy = (Double) this.cornerY.get(i);
					gl.glVertex2d(tmpx - this.x, tmpy - this.y);
					
				}
				gl.glEnd();
			}

			if (this.stroke) {
				gl.glLineWidth(this.strokeWidth);
				getSceneStrokeColor().setup(gl);
				gl.glBegin(GL2.GL_LINE_STRIP);
				for (int i = 0; i < this.size; i++) {
					tmpx = (Double) this.cornerX.get(i);
					tmpy = (Double) this.cornerY.get(i);
					gl.glVertex2d(tmpx - this.x, tmpy - this.y);
				}
				tmpx = (Double) this.cornerX.get(0);
				tmpy = (Double) this.cornerY.get(0);
				gl.glVertex2d(tmpx - this.x, tmpy - this.y);
				gl.glEnd();
			}
			break;
		case LINES_3D:
			if (this.fill) {
				this.fillColor.setup(gl);
				gl.glBegin(GL2.GL_POLYGON);
				for (int i = 0; i < this.size; i++) {
					tmpx = (Double) this.cornerX.get(i);
					tmpy = (Double) this.cornerY.get(i);
					tmpz = (Double) this.cornerZ.get(i);
					if (isGradation() == true)
						getSceneColor(this.cornerColor.get(i)).setup(gl);
					gl.glVertex3d(tmpx - this.x, tmpy - this.y, tmpz - this.z);
				}
				gl.glEnd();
			}

			if (this.stroke) {
				gl.glLineWidth(this.strokeWidth);
				this.strokeColor.setup(gl);
				gl.glBegin(GL2.GL_LINE_STRIP);
				for (int i = 0; i < cornerX.size(); i++) {
					tmpx = (Double) this.cornerX.get(i);
					tmpy = (Double) this.cornerY.get(i);
					tmpz = (Double) this.cornerZ.get(i);
					gl.glVertex3d(tmpx - this.x, tmpy - this.y, tmpz - this.z);
				}
				tmpx = (Double) this.cornerX.get(0);
				tmpy = (Double) this.cornerY.get(0);
				tmpz = (Double) this.cornerZ.get(0);
				gl.glEnd();
			}
			break;

		default:
			break;
		}

		gl.glPopMatrix();

		if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || this.isDepthTest()==false) {
			gl.glEnable(GL2.GL_DEPTH_TEST);
		}
	}

	private void calcG() {
		double cenX = 0, cenY = 0, cenZ = 0;
		
		for (int i = 0; i < cornerX.size(); i++) {
			cenX += cornerX.get(i);
			cenY += cornerY.get(i);
			cenZ += cornerZ.get(i);
		}
		this.x = cenX/cornerX.size();
		this.y = cenY/cornerX.size();
		this.z = cenZ/cornerX.size();
	}

	public double getX() {
		return this.centerX;
	}

	public double getY() {
		return this.centerY;
	}

	public void setX(double x) {
		this.centerX = x;
	}

	public void setY(double y) {
		this.centerY = y;
	}

	public void setXY(double x, double y) {
		this.centerX = x;
		this.centerY = y;
	}
    
    @Override
    public void setPosition(double x, double y) {
    	setPosition(x, y, this.z);
    }
    
    @Override
    public void setPosition(double x, double y, double z) {

		for (int i = 0; i < cornerX.size(); i++) {
			cornerX.set(i, cornerX.get(i) + x - this.x);
			cornerY.set(i, cornerY.get(i) + y - this.y);
			cornerZ.set(i, cornerZ.get(i) + z - this.z);
		}
    	calcG();
    }

	public void setCornerColor(int index, Color color) {
		if (cornerColor == null) {
			for (int i = 0; i < cornerX.size(); i++) {
				cornerColor.add(new RGBColor(this.fillColor.getRed(),
				                             this.fillColor.getGreen(),
				                             this.fillColor.getBlue(),
				                             this.fillColor.getAlpha()));
			}
			setGradation(true);
		}
		if (cornerColor.size() < cornerX.size()) {
			while (cornerColor.size() != cornerX.size()) {
				cornerColor.add(new RGBColor(this.fillColor.getRed(),
                                             this.fillColor.getGreen(),
                                             this.fillColor.getBlue(),
                                             this.fillColor.getAlpha()));
			}
		}
		cornerColor.set(index, color);
	}

	public void setCornerColor(int index, ColorSet colorSet) {
	    setCornerColor(index, new RGBColor(colorSet));
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
