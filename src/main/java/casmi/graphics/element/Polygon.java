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

import javax.media.opengl.GL;
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

	public static final int LINES = 1;
	public static final int LINES_3D = 3;
	public static final int LINE_LOOP = 51;

	private List<Double> x;
	private List<Double> y;
	private List<Double> z;

	private double X = 0;
	private double Y = 0;
	private int size;

	private Vertex tmpv = new Vertex(0, 0, 0);

	private ArrayList<Color> cornerColor;

	private int MODE;

	public Polygon() {
		x = new ArrayList<Double>();
		y = new ArrayList<Double>();
		z = new ArrayList<Double>();
	}

	public void vertex(float x, float y) {
		MODE = LINES;
		this.x.add((double) x);
		this.y.add((double) y);
		setSize(this.x.size());
		calcG();
	}

	public void vertex(float x, float y, float z) {
		MODE = LINES_3D;
		this.x.add((double) x);
		this.y.add((double) y);
		this.z.add((double) z);
		setSize(this.x.size());
		calcG();
	}

	public void vertex(double x, double y) {
		MODE = LINES;
		this.x.add(x);
		this.y.add(y);
		setSize(this.x.size());
		calcG();
	}

	public void vertex(double x, double y, double z) {
		MODE = LINES_3D;
		this.x.add(x);
		this.y.add(y);
		this.z.add(z);
		setSize(this.x.size());
		calcG();
	}

	public void vertex(Vertex v) {
		MODE = LINES_3D;
		this.x.add(v.x);
		this.y.add(v.y);
		this.z.add(v.z);
		setSize(this.x.size());
		calcG();
	}

	public Vertex getVertex(int index) {
		tmpv.x = x.get(index);
		tmpv.y = y.get(index);
		tmpv.z = z.get(index);
		calcG();
		return tmpv;
	}

	public void removeVertex(int index) {
		this.x.remove(index);
		this.y.remove(index);
		this.z.remove(index);
		if (isGradation() == true)
			this.cornerColor.remove(index);
		setSize(this.x.size());
		calcG();
	}

	public void setVertex(int i, double x, double y) {
		this.x.set(i, x);
		this.y.set(i, y);
		this.z.set(i, 0d);
		calcG();
	}

	public void setVertex(int i, double x, double y, double z) {
		this.x.set(i, x);
		this.y.set(i, y);
		this.z.set(i, z);
		calcG();
	}

	@Override
	public void render(GL gl, GLU glu, int width, int height) {
		if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001) {
			gl.glDisable(GL.GL_DEPTH_TEST);
		}

		double tmpx, tmpy, tmpz;

		gl.glPushMatrix();
//		gl.glTranslated(X, Y, 0);
		gl.glRotated(rotate, 0, 0, 1.0);
		this.setTweenParameter(gl);

		switch (MODE) {
		case LINES:
			if (this.fill) {
				getSceneFillColor().setup(gl);
				gl.glBegin(GL.GL_POLYGON);
				for (int i = 0; i < this.size; i++) {
					tmpx = (Double) this.x.get(i);
					tmpy = (Double) this.y.get(i);
					gl.glVertex2d(tmpx - X, tmpy - Y);
				}
				gl.glEnd();
			}

			if (this.stroke) {
				gl.glLineWidth(this.strokeWidth);
				getSceneStrokeColor().setup(gl);
				gl.glBegin(GL.GL_LINE_STRIP);
				for (int i = 0; i < this.size; i++) {
					tmpx = (Double) this.x.get(i);
					tmpy = (Double) this.y.get(i);
					gl.glVertex2d(tmpx - X, tmpy - Y);
				}
				tmpx = (Double) this.x.get(0);
				tmpy = (Double) this.y.get(0);
				gl.glVertex2d(tmpx - X, tmpy - Y);
				gl.glEnd();
			}
			break;
		case LINES_3D:
			if (this.fill) {
				this.fillColor.setup(gl);
				gl.glBegin(GL.GL_POLYGON);
				for (int i = 0; i < this.size; i++) {
					tmpx = (Double) this.x.get(i);
					tmpy = (Double) this.y.get(i);
					tmpz = (Double) this.z.get(i);
					if (isGradation() == true)
						getSceneColor(this.cornerColor.get(i)).setup(gl);
					gl.glVertex3d(tmpx - X, tmpy - Y, tmpz);
				}
				gl.glEnd();
			}

			if (this.stroke) {
				gl.glLineWidth(this.strokeWidth);
				this.strokeColor.setup(gl);
				gl.glBegin(GL.GL_LINE_STRIP);
				for (int i = 0; i < x.size(); i++) {
					tmpx = (Double) this.x.get(i);
					tmpy = (Double) this.y.get(i);
					tmpz = (Double) this.z.get(i);
					gl.glVertex3d(tmpx - X, tmpy - Y, tmpz);
				}
				tmpx = (Double) this.x.get(0);
				tmpy = (Double) this.y.get(0);
				tmpz = (Double) this.z.get(0);
				gl.glVertex3d(tmpx - X, tmpy - Y, tmpz);
				gl.glEnd();
			}
			break;

		default:
			break;
		}

		gl.glPopMatrix();

		if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001) {
			gl.glEnable(GL.GL_DEPTH_TEST);
		}
	}

	private void calcG() {
		X = Y = 0;
		for (int i = 0; i < x.size(); i++) {
			X += x.get(i);
			Y += y.get(i);
		}
		X /= x.size();
		Y /= y.size();
	}

	public double getX() {
		return this.X;
	}

	public double getY() {
		return this.Y;
	}

	public void setX(double x) {
		this.X = x;
	}

	public void setY(double y) {
		this.Y = y;
	}

	public void setXY(double x, double y) {
		this.X = x;
		this.Y = y;
	}

	public void setCornerColor(int index, Color color) {
		if (cornerColor == null) {
			for (int i = 0; i < x.size(); i++) {
				cornerColor.add(new RGBColor(this.fillColor.getRed(),
				                             this.fillColor.getGreen(),
				                             this.fillColor.getBlue(),
				                             this.fillColor.getAlpha()));
			}
			setGradation(true);
		}
		if (cornerColor.size() < x.size()) {
			while (cornerColor.size() != x.size()) {
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
