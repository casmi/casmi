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

import javax.media.opengl.GL;

import casmi.graphics.color.Color;
import casmi.graphics.color.ColorMode;
import casmi.graphics.color.ColorSet;
import casmi.graphics.material.Material;
import casmi.graphics.object.Mask;
import casmi.matrix.Vertex;

/**
 * Element class. Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
abstract public class Element implements Cloneable, Renderable {

	private int sR = 0;
	private int sG = 0;
	private int sB = 0;
	private int sA = 255;
	private int fR = 255;
	private int fG = 255;
	private int fB = 255;
	private int fA = 255;
	private double sceneA = 1.0;
	protected Texture texture;
	private Mask mask;

	protected double x = 0;
	protected double y = 0;
	protected double z = 0;
	protected double rotateX = 0, rotateY = 0;
	protected double scaleX = 1.0;
	protected double scaleY = 1.0;
	protected double scaleZ = 1.0;

	protected double rotate = 0;
	protected double tAS;
	protected double tAF;

	protected Color strokeColor = new Color(sR, sG, sB, sA);
	protected Color fillColor = new Color(fR, fG, fB, fA);
	protected Color scenestrokeColor = new Color(sR, sG, sB,
			(int) (sA * sceneA));
	protected Color scenefillColor = new Color(fR, fG, fB, (int) (fA * sceneA));
	protected Material material = new Material();

	protected boolean stroke = true;
	protected boolean fill = true;
	protected boolean tween = false;

	protected float strokeWidth = 1;

	private ArrayList<MouseEventCallback> mouseEventCallbacks;
	private boolean mouseover = false;
	private boolean premouseover = false;
	private boolean selectionbuffer = false;

	protected boolean enableTexture = false;
	protected boolean visible = true;
	protected boolean gradation = false;

	/**
	 * Returns the width of this Element's stroke.
	 * 
	 * @return strokeWidth The width of the Element's stroke.
	 */
	public double getStrokeWidth() {
		return (double) strokeWidth;
	}

	/**
	 * Sets the width of this Element's stroke.
	 * 
	 * @param strokeWidth
	 *            The width of the Element's stroke.
	 */
	public void setStrokeWidth(double strokeWidth) {
		this.strokeWidth = (float) strokeWidth;
	}

	/**
	 * Returns the color of this Element's stroke.
	 * 
	 * @return strokeColor The color of the Element's stroke.
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Sets the color of this Element's stroke.
	 * 
	 * @param strokeColor
	 *            The color of the Element's stroke.
	 */
	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
		this.tAS = strokeColor.getA();
	}
	
	public void setStrokeColorAlpha(int alpha) {
		this.strokeColor.setA(alpha);
		this.tAS = alpha;
	}

	/**
	 * Sets the color of this Element's stroke.
	 * 
	 * @param strokeColor
	 *            The color of the Element's stroke.
	 */
	public void setStrokeColor(ColorSet strokeColor) {
		this.strokeColor = Color.color(strokeColor);
	}

	public void setStrokeColor(ColorSet strokeColor, int alpha) {
		this.strokeColor = Color.color(strokeColor);
		this.strokeColor.setA(alpha);
		this.tAS = alpha;
	}

	/**
	 * Returns the color of this Element's fill.
	 * 
	 * @return fillColor The color of the Element's fill.
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Sets the color of this Element's fill.
	 * 
	 * @param fillColor
	 *            The color of the Element's fill.
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		this.tAF = fillColor.getA();
	}
	
	public void setFillColorAlpha(int alpha) {
		this.fillColor.setA(alpha);
		this.tAF = alpha;
	}

	/**
	 * Sets the color of this Element's fill.
	 * 
	 * @param fillColor
	 *            The color of the Element's fill.
	 */
	public void setFillColor(ColorSet fillColor) {
		this.fillColor = Color.color(fillColor);
	}
	
	public void setFillColor(ColorSet fillColor, int alpha) {
		this.fillColor = Color.color(fillColor);
		this.fillColor.setA(alpha);
		this.tAF = alpha;
	}

	public boolean isStroke() {
		return stroke;
	}

	/**
	 * Enables or disables drawing the stroke (outline)
	 * 
	 * @param stroke
	 */
	public void setStroke(boolean stroke) {
		this.stroke = stroke;
	}

	public boolean isFill() {
		return fill;
	}

	/**
	 * Enables or disables filling geometry
	 * 
	 * @param fill
	 */
	public void setFill(boolean fill) {
		this.fill = fill;
	}

	public void colorMode(ColorMode colorMode) {
		this.fillColor.setColorMode(colorMode);
		this.strokeColor.setColorMode(colorMode);
	}

	/**
	 * Sets Material to this Element
	 * 
	 * @param m
	 *            The Material of this Element.
	 */
	public void setMaterial(Material m) {
		this.material = m;
	}

	public void setSceneAlpha(double alpha) {
		this.strokeColor.setA((int) alpha);
		this.fillColor.setA((int) alpha);
	}

	public void setAlpha(double alpha) {
		this.sceneA = alpha;
	}

	public Color getSceneStrokeColor() {
		Color.copyColor(this.strokeColor, this.scenestrokeColor);
		if (tween == true) {
			this.scenestrokeColor.setA((int) (gettAS() * sceneA));
		} else
			this.scenestrokeColor
					.setA((int) (this.strokeColor.getA() * sceneA));
		return this.scenestrokeColor;
	}

	public Color getSceneFillColor() {
		Color.copyColor(this.fillColor, this.scenefillColor);
		if (tween == true) {
			this.scenefillColor.setA((int) (gettAF() * sceneA));

		} else
			this.scenefillColor.setA((int) (this.fillColor.getA() * sceneA));
		return this.scenefillColor;
	}

	public Color getSceneColor(Color c) {
		Color.copyColor(c, this.scenefillColor);
		if (tween == true) {
			this.scenefillColor.setA((int) (gettAF() * sceneA));

		} else
			this.scenefillColor.setA((int) (c.getA() * sceneA));
		return this.scenefillColor;
	}

	public void setTween(boolean tween) {
		this.tween = tween;
	}

	public boolean isTween() {
		return this.tween;
	}

	protected void setTweenParameter(GL gl) {

			gl.glTranslated(x, y, z);
			gl.glScaled(scaleX, scaleY, scaleZ);
			gl.glRotated(rotate, 0.0, 0.0, 1.0);
			gl.glRotated(rotateX, 1.0, 0.0, 0.0);
			gl.glRotated(rotateY, 0.0, 1.0, 0.0);

	}

	protected void setTextTweenParameter(GL gl) {

			gl.glTranslated(x, y, z);
			gl.glScaled(scaleX, scaleY, scaleZ);
			gl.glRotated(rotate, 0.0, 0.0, 1.0);
			gl.glRotated(rotateX, 1.0, 0.0, 0.0);
			gl.glRotated(rotateY, 0.0, 1.0, 0.0);

	}

	public double gettAS() {
		return tAS;
	}

	public void settAS(double tAS) {
		this.tAS = tAS;
	}

	public double gettAF() {
		return tAF;
	}

	public void settAF(double tAF) {
		this.tAF = tAF;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setPosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setPosition(Vertex v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
	}

	public void setRotation(double angle) {
		this.rotate = angle;
	}

	public void setRotation(double angle, double x, double y, double z) {
		this.rotateX = angle * x;
		this.rotateY = angle * y;
		this.rotate = angle * z;
	}

	public void setRotation(double x, double y, double z) {
		this.rotateX = x;
		this.rotateY = y;
		this.rotate = z;
	}

	public double getRotation() {
		return this.rotate;
	}

	public void setRotationX(double angle) {
		this.rotateX = angle;
	}

	public double getRotationX() {
		return this.rotateX;
	}

	public void setRotationY(double angle) {
		this.rotateY = angle;
	}

	public double getRotationY() {
		return this.rotateY;
	}

	public void setRotationZ(double angle) {
		this.rotate = angle;
	}

	public double getRotationZ() {
		return this.rotate;
	}

	public double getScaleX() {
		return this.scaleX;
	}

	public double getScaleY() {
		return this.scaleY;
	}

	public double getScaleZ() {
		return this.scaleZ;
	}
	
	public void setScale(double scale) {
		this.scaleX = scale;
		this.scaleY = scale;
		this.scaleZ = scale;
	}
	
	public void setScale(double scaleX, double scaleY, double scaleZ) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
	}

	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}

	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}

	public void setScaleZ(double scaleZ) {
		this.scaleZ = scaleZ;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
		this.enableTexture = true;
	}

	public void enableTexture() {
		this.enableTexture = true;
	}

	public void disableTexture() {
		this.enableTexture = false;
	}

	public void addMouseEventCallback(MouseEventCallback callback) {
		if (mouseEventCallbacks == null) {
			mouseEventCallbacks = new ArrayList<MouseEventCallback>(3);
		}
		mouseEventCallbacks.add(callback);
	}

	public ArrayList<MouseEventCallback> getMouseOverCallback() {
		if (mouseEventCallbacks == null)
			return null;
		else
			return mouseEventCallbacks;
	}

	public void callMouseOverCallback(boolean b) {
		for (int i = 0; i < mouseEventCallbacks.size(); i++) {
			if (mouseEventCallbacks.get(i) instanceof MouseOverCallback) {
				MouseOverCallback mOver = (MouseOverCallback) mouseEventCallbacks
						.get(i);
				if (b == true) {
					if (mouseover == false) {
						mOver.run(
								MouseEventCallback.MouseOverTypes.ENTERED, this);
					}
					mOver.run(
							MouseEventCallback.MouseOverTypes.EXISTED, this);
					mouseover = true;
				} else {
					mOver.run(
							MouseEventCallback.MouseOverTypes.EXITED, this);
				}
			}
		}
	}

	public void callMouseClickCallback(casmi.MouseEvent e) {
		for (int i = 0; i < mouseEventCallbacks.size(); i++) {
			if (mouseEventCallbacks.get(i) instanceof MouseClickCallback) {
				MouseClickCallback mClick = (MouseClickCallback) mouseEventCallbacks
						.get(i);
				if (e == casmi.MouseEvent.CLICKED)
					mClick.run(
							MouseEventCallback.MouseClickTypes.CLICKED, this);
				if (e == casmi.MouseEvent.PRESSED)
					mClick.run(
							MouseEventCallback.MouseClickTypes.PRESSED, this);
				if (e == casmi.MouseEvent.RELEASED)
					mClick.run(
							MouseEventCallback.MouseClickTypes.RELEASED, this);
				if (e == casmi.MouseEvent.DRAGGED)
					mClick.run(
							MouseEventCallback.MouseClickTypes.DRAGGED, this);
				if (e == casmi.MouseEvent.MOVED)
					mClick.run(
							MouseEventCallback.MouseClickTypes.MOVED, this);
			}
		}
	}

	@Override
	public Element clone() {
		try {
			Element r = (Element) super.clone();
			return r;
		} catch (CloneNotSupportedException ce) {
			ce.printStackTrace();
		}
		return null;

	}

	public boolean isMouseover() {
		return mouseover;
	}

	public void setMouseover(boolean bool) {
		mouseover = bool;
	}

	public boolean isPreMouseover() {
		return premouseover;
	}

	public void setPreMouseover(boolean bool) {
		premouseover = bool;
	}

	public void visible() {
		visible = true;
	}

	public boolean isvisible() {
		return visible;
	}

	public void hidden() {
		visible = false;
	}

	public boolean isGradation() {
		return gradation;
	}

	public void setGradation(boolean bool) {
		gradation = bool;
	}

	public boolean isSelectionbuffer() {
		return selectionbuffer;
	}

	public void setSelectionbuffer(boolean selectionbuffer) {
		this.selectionbuffer = selectionbuffer;
	}

	public Mask getMask() {
		return mask;
	}

	public boolean isMasked() {
		if (mask == null)
			return false;
		else
			return true;
	}

	public void setMask(Mask mask) {
		this.mask = mask;
	}

}
