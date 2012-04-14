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
 * Lines class. Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Lines extends Element implements Renderable {

	public static final int LINES     = 1;
	public static final int LINES_3D  = 3;
	public static final int LINE_LOOP = 51;

	private List<Double> x;
	private List<Double> y;
	private List<Double> z;
	private List<Color> colors;

	private double X = 0;
	private double Y = 0;

	private Vertex tmpV = new Vertex(0, 0, 0);

	public enum JoinMode {
		CORNER, CORNERS, RADIUS, CENTER
	};

	private int MODE;

	private Color startColor;
	private Color endColor;
	private Color gradationColor = new RGBColor(0.0, 0.0, 0.0);

	private boolean cornerGradation = false;

	public Lines() {
		x = new ArrayList<Double>();
		y = new ArrayList<Double>();
		z = new ArrayList<Double>();
		colors = new ArrayList<Color>();
	}

	public void vertex(float x, float y) {
		MODE = LINES;
		this.x.add((double) x);
		this.y.add((double) y);
		colors.add(this.strokeColor);
		calcG();
	}

	public void vertex(float x, float y, float z) {
		MODE = LINES_3D;
		this.x.add((double) x);
		this.y.add((double) y);
		this.z.add((double) z);
		colors.add(this.strokeColor);
		calcG();
	}

	public void vertex(double x, double y) {
		MODE = LINES;
		this.x.add(x);
		this.y.add(y);
		colors.add(this.strokeColor);
		calcG();
	}

	public void vertex(double x, double y, double z) {
		MODE = LINES_3D;
		this.x.add(x);
		this.y.add(y);
		this.z.add(z);
		colors.add(this.strokeColor);
		calcG();
	}

	public void vertex(Vertex v) {
		MODE = LINES_3D;
		this.x.add(v.getX());
		this.y.add(v.getY());
		this.z.add(v.getZ());
		colors.add(this.strokeColor);
		calcG();
	}

	public Vertex getVertex(int i) {
		tmpV.setX(x.get(i));
		tmpV.setY(y.get(i));
		tmpV.setZ(z.get(i));

		calcG();
		return tmpV;
	}

	public void removeVertex(int i) {
		this.x.remove(i);
		this.y.remove(i);
		this.z.remove(i);
		this.colors.remove(i);
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
		if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || this.isDepthTest()==false) {
			gl.glDisable(GL.GL_DEPTH_TEST);
		}

		getSceneStrokeColor().setup(gl);
		double tmpx, tmpy, tmpz;

		gl.glPushMatrix();
		this.setTweenParameter(gl);

		switch (MODE) {
		case LINES:
			gl.glLineWidth(this.strokeWidth);
			gl.glBegin(GL.GL_LINE_STRIP);
			for (int i = 0; i < x.size(); i++) {
				tmpx = (Double) this.x.get(i);
				tmpy = (Double) this.y.get(i);
				if (!cornerGradation) {
					if (i == 0 && isGradation() && startColor != null)
						getSceneColor(this.startColor).setup(gl);
					if (i == x.size() - 1 && isGradation() && endColor != null)
						getSceneColor(this.endColor).setup(gl);
					if (i != 0 && i != (x.size() - 1) && isGradation() && endColor != null && startColor != null) {
						gradationColor = RGBColor.lerpColor(this.startColor, this.endColor, (i / (double) (x.size() - 1)));
						getSceneColor(this.gradationColor).setup(gl);
					}
				} else {
					getSceneColor(colors.get(i)).setup(gl);
				}
				gl.glVertex2d(tmpx - X, tmpy - Y);
			}
			gl.glEnd();
			break;
		case LINES_3D:
			gl.glLineWidth(this.strokeWidth);
			gl.glBegin(GL.GL_LINE_STRIP);
			for (int i = 0; i < x.size(); i++) {
				tmpx = (Double) this.x.get(i);
				tmpy = (Double) this.y.get(i);
				tmpz = (Double) this.z.get(i);
				if (!cornerGradation) {
					if (i == 0 && isGradation() && startColor != null)
						getSceneColor(this.startColor).setup(gl);
					if (i == x.size() - 1 && isGradation() && endColor != null)
						getSceneColor(this.endColor).setup(gl);
					if (i != 0 && i != (x.size() - 1) && isGradation() && endColor != null && startColor != null) {
						gradationColor = RGBColor.lerpColor(this.startColor, this.endColor, (i / (double) (x.size() - 1)));
						getSceneColor(this.gradationColor).setup(gl);
					}
				} else {
					getSceneColor(colors.get(i)).setup(gl);
				}
				gl.glVertex3d(tmpx - X, tmpy - Y, tmpz);
			}
			gl.glEnd();
			break;
		default:
			break;
		}

		gl.glPopMatrix();

		if (this.fillColor.getAlpha() < 0.001 || this.strokeColor.getAlpha() < 0.001 || this.isDepthTest()==false) {
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
		setPosition(X, Y);
	}

	public void setStartCornerColor(Color color) {
		if (startColor == null) {
			startColor = new RGBColor(0.0, 0.0, 0.0);
		}
		setGradation(true);
		this.startColor = color;
	}

	public void setStartCornerColor(ColorSet colorSet) {
		setStartCornerColor(new RGBColor(colorSet));
	}

	public void setEndCornerColor(Color color) {
		if (endColor == null) {
			endColor = new RGBColor(0.0, 0.0, 0.0);
		}
		setGradation(true);
		this.endColor = color;
	}

	public void setEndCornerColor(ColorSet colorSet) {
		setEndCornerColor(new RGBColor(colorSet));
	}

	public void setCornerColor(int index, Color color) {
		if (!cornerGradation) {
			cornerGradation = true;
		}
		colors.set(index, color);
	}

	public void setCornerColor(int index, ColorSet colorSet) {
		setCornerColor(index, new RGBColor(colorSet));
	}
}
