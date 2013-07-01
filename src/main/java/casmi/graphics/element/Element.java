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

import casmi.MouseStatus;
import casmi.callback.MouseClickCallback;
import casmi.callback.MouseClickEventType;
import casmi.callback.MouseEventCallback;
import casmi.callback.MouseOverCallback;
import casmi.callback.MouseOverEventType;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.material.Material;
import casmi.graphics.object.Renderable;
import casmi.graphics.object.Resettable;
import casmi.matrix.Vector3D;

/**
 * Element class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 *
 */
abstract public class Element implements Cloneable, Renderable, Resettable {
    protected double strokeRed   = 0.0;
	protected double strokeGreen = 0.0;
	protected double strokeBlue  = 0.0;
//	protected double strokeAlpha = 1.0;

	protected double fillRed   = 1.0;
	protected double fillGreen = 1.0;
	protected double fillBlue  = 1.0;
//	protected double fillAlpha = 1.0;

	private double sceneA = 1.0;

//	protected Mask mask;

	public boolean enableMask = true;

	protected double x = 0.0;
	protected double y = 0.0;
	protected double z = 0.0;
	protected double rotateX = 0.0, rotateY = 0.0;
	protected double scaleX = 1.0;
	protected double scaleY = 1.0;
	protected double scaleZ = 1.0;

	protected double rotate = 0.0;

	protected Color strokeColor      = new RGBColor(strokeRed, strokeGreen, strokeBlue, 1.0);
	protected Color fillColor        = new RGBColor(fillRed, fillGreen, fillBlue, 1.0);
	protected Color sceneStrokeColor = new RGBColor(strokeRed, strokeGreen, strokeBlue, 1.0 * sceneA);
	protected Color sceneFillColor   = new RGBColor(fillRed, fillGreen, fillBlue, 1.0 * sceneA);
	protected Material material      = new Material();
	protected boolean ismaterial = false;

	protected boolean stroke = true;
	protected boolean fill   = true;

	protected float strokeWidth = 1.0f;

	private ArrayList<MouseEventCallback> mouseEventCallbacks = null;
	private boolean currentMouseOver = false;
	private boolean prevMouseOver = false;

	private boolean depthTest = true;

	protected boolean enableTexture = false;
	protected boolean visible = true;
	protected boolean gradation = false;

	protected boolean reset = false;
	protected boolean init = true;

	/**
	 * Returns the width of this Element's stroke.
	 *
	 * @return
	 * 			 The width of the Element's stroke.
	 */
	public double getStrokeWidth() {
		return strokeWidth;
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
	 * @return
	 * 				The color of the Element's stroke.
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Sets the color of this Element's stroke.
	 *
	 * @param color
	 *            The color of the Element's stroke.
	 */
	public void setStrokeColor(Color color) {
		this.strokeColor = color;
//		this.strokeAlpha = color.getAlpha();
	}

	/**
	 * Sets the alpha of this Element's stroke.
	 *
	 * @param alpha
	 *            The color of the Element's stroke.
	 */
	public void setStrokeColorAlpha(double alpha) {
		this.strokeColor.setAlpha(alpha);
//		this.strokeAlpha = alpha;
	}

	/**
	 * Sets the color of this Element's stroke.
	 *
	 * @param colorSet
	 *            The colorSet of the Element's stroke.
	 */
	public void setStrokeColor(ColorSet colorSet) {
		strokeColor = new RGBColor(colorSet);
	}

	/**
	 * Sets the color of this Element's stroke.
	 *
	 * @param colorSet
	 *            The colorSet of the Element's stroke.
	 * @param alpha
	 * 			  The alpha of the Element's stroke.
	 */
	public void setStrokeColor(ColorSet colorSet, double alpha) {
	    strokeColor = new RGBColor(colorSet);
//	    this.strokeAlpha = alpha;
	}

	/**
	 * Returns the color of this Element's fill.
	 *
	 * @return
	 * 			 The color of the Element's fill.
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Sets the color of this Element's fill.
	 *
	 * @param color
	 *            The color of the Element's fill.
	 */
	public void setFillColor(Color color) {
		this.fillColor = color;
//		this.fillAlpha = color.getAlpha();
	}

	/**
	 * Sets the alpha of this Element's fill.
	 *
	 * @param alpha
	 *            The color of the Element's fill.
	 */
	public void setFillColorAlpha(double alpha) {
		this.fillColor.setAlpha(alpha);
//		this.fillAlpha = alpha;
	}

	/**
	 * Sets the color of this Element's fill.
	 *
	 * @param colorSet
	 *            The color of the Element's fill.
	 */
	public void setFillColor(ColorSet colorSet) {
		this.fillColor = new RGBColor(colorSet);
	}

	/**
	 * Sets the color of this Element's fill.
	 *
	 * @param colorSet
	 *            The color of the Element's fill.
	 * @param alpha
	 * 			  The alpha of the Element's fill.
	 */
	public void setFillColor(ColorSet colorSet, double alpha) {
		this.fillColor = new RGBColor(colorSet, alpha);
//		this.fillAlpha = alpha;
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

	/**
	 * Sets Material to this Element
	 *
	 * @param m
	 *            The Material of this Element.
	 */
	public void setMaterial(Material m) {
		this.material = m;
		this.ismaterial = true;
	}

	public void setSceneAlpha(double alpha) {
		this.strokeColor.setAlpha(alpha);
		this.fillColor.setAlpha(alpha);
	}

    public void setAlpha(double alpha) {
		this.sceneA = alpha;
	}

	public Color getSceneStrokeColor() {
//		sceneStrokeColor = strokeColor.clone();
//		if (tween) {
//			sceneStrokeColor.setAlpha(this.strokeAlpha * sceneA);
//		} else {
//			sceneStrokeColor.setAlpha(strokeColor.getAlpha() * sceneA);
//		}
//		return sceneStrokeColor;
	    return strokeColor;
	}

	public Color getSceneFillColor() {
//	    sceneFillColor = fillColor.clone();
//		if (tween) {
//			sceneFillColor.setAlpha(this.fillAlpha * sceneA);
//		} else {
//			this.sceneFillColor.setAlpha(fillColor.getAlpha() * sceneA);
//		}
//		return this.sceneFillColor;
	    return fillColor;
	}

	public Color getSceneColor(Color color) {
//	    sceneFillColor = color.clone();
//		if (tween) {
//			sceneFillColor.setAlpha(this.fillAlpha * sceneA);
//		} else {
//			sceneFillColor.setAlpha(color.getAlpha() * sceneA);
//		}
//		return sceneFillColor;
	    return color;
	}

//	/**Decides the Element has the tween or not.
//	 *
//	 * @param tween
//	 *
//	 */
//	public void setTween(boolean tween) {
//		this.tween = tween;
//	}

//	/**Gets that the Element has has the tween or not.
//	 *
//	 * @return
//	 * 			Returns the tween prediction.
//	 */
//	public boolean isTween() {
//		return this.tween;
//	}

	protected void move(GL2 gl) {
	    gl.glTranslated(x, y, z);
	    gl.glScaled(scaleX, scaleY, scaleZ);
	    gl.glRotated(rotate,  0.0, 0.0, 1.0);
	    gl.glRotated(rotateX, 1.0, 0.0, 0.0);
	    gl.glRotated(rotateY, 0.0, 1.0, 0.0);
	}

//	public double getStrokeAlpha() {
//		return strokeAlpha;
//	}
//
//	public void setStrokeAlpha(double strokeAlpha) {
//		this.strokeAlpha = strokeAlpha;
//	}
//
//	public double getFillAlpha() {
//		return fillAlpha;
//	}
//
//	public void setFillAlpha(double fillAlpha) {
//		this.fillAlpha = fillAlpha;
//	}

	/**Gets x-coordinate of the Element.
	 *
	 * @return
	 * 				x-coordinate
	 */
	public double getX() {
		return this.x;
	}

	/**Gets y-coordinate of the Element.
	 *
	 * @return
	 * 				y-coordinate
	 */
	public double getY() {
		return this.y;
	}

	/**Gets z-coordinate of the Element.
	 *
	 * @return
	 * 				z-coordinate
	 */
	public double getZ() {
		return this.z;
	}

	/**Gets the position of the Element.
	 *
	 * @return
	 * 				the position of the Element
	 */
	public Vector3D getPosition() {
		Vector3D v = new Vector3D(x, y, z);
		return v;
	}

	/**Sets x-coordinate of the Element.
	 *
	 * @param x
	 * 				x-coordinate to set.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**Sets y-coordinate of the Element.
	 *
	 * @param y
	 * 				y-coordinate to set.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**Sets z-coordinate of the Element.
	 *
	 * @param z
	 * 				z-coordinate to set.
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**Sets the position of the Element in 2D.
	 *
	 * @param x
	 * 				x-coordinate
	 * @param y
	 * 				y-coordinate
	 */
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**Sets the position of the Element in 3D.
	 *
	 * @param x
	 * 				x-coordinate
	 * @param y
	 * 				y-coordinate
	 * @param z
	 * 				z-coordinate
	 */
	public void setPosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**Sets the position of the Element in 2D.
	 *
	 * @param v
	 * 				the vertex of the position of the Element
	 */
	public void setPosition(Vector3D v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
	}

	/**Flips the Element. You can choose the way of flip with 0 or 1.
	 *
	 * @param mode
	 * 				You can choose the way of flip with 0 or 1.
	 * 				If you choose 0, the Element flips round on y-axis.
	 * 				If you choose 1, the Element flips round on x-axis.
	 * 				If you choose other numbers, the Element flips round on z-axis.
	 */
	public void flip(int mode) {
		switch (mode) {
		case 0:
			this.rotateY += 180;
			break;
		case 1:
			this.rotateX += 180;
			break;
		default:
			this.rotate += 180;
			break;
		}
	}

	/**Sets the rotation angle of the Element round on z-axis.
	 *
	 * @param angle
	 * 					The angle of rotation
	 */
	public void setRotation(double angle) {
		this.rotate = angle;
	}

	/**Sets the rotation angle of the Element.
	 * This method wraps the glRotate method.
	 *
	 * @param angle
	 * 					The angle of rotation
	 * @param x
	 * 					The rate of rotation angle round x-axis
	 * @param y
	 * 					The rate of rotation angle round x-axis
	 * @param z
	 * 					The rate of rotation angle round x-axis
	 */
	public void setRotation(double angle, double x, double y, double z) {
		this.rotateX = angle * x;
		this.rotateY = angle * y;
		this.rotate  = angle * z;
	}

	/**Sets the rotation angle of the Element.
	 *
	 * @param x
	 * 					The rotation angle round x-axis
	 * @param y
	 * 					The rotation angle round x-axis
	 * @param z
	 * 					The rotation angle round x-axis
	 */
	public void setRotation(double x, double y, double z) {
		this.rotateX = x;
		this.rotateY = y;
		this.rotate  = z;
	}

	/**Gets the rotation angle round on z-axis.
	 *
	 * @return
	 * 				The rotation angle round on z-axis
	 */
	public double getRotation() {
		return this.rotate;
	}

	/**Sets the rotation angle of the Element round on x-axis.
	 *
	 * @param angle
	 * 					The angle of rotation round on x-axis
	 */
	public void setRotationX(double angle) {
		this.rotateX = angle;
	}

	/**Gets the rotation angle round x-axis.
	 *
	 * @return
	 * 				The rotation angle round on x-axis
	 */
	public double getRotationX() {
		return this.rotateX;
	}

	/**Sets the rotation angle of the Element round on y-axis.
	 *
	 * @param angle
	 * 					The angle of rotation round on y-axis
	 */
	public void setRotationY(double angle) {
		this.rotateY = angle;
	}

	/**Gets the rotation angle round y-axis.
	 *
	 * @return
	 * 				The rotation angle round on y-axis
	 */
	public double getRotationY() {
		return this.rotateY;
	}

	/**Sets the rotation angle of the Element round on z-axis.
	 *
	 * @param angle
	 * 					The angle of rotation round on z-axis
	 */
	public void setRotationZ(double angle) {
		this.rotate = angle;
	}

	/**Gets the rotation angle round z-axis.
	 *
	 * @return
	 * 				The rotation angle round on z-axis
	 */
	public double getRotationZ() {
		return this.rotate;
	}

	/**Gets the scale of x-axis.
	 *
	 * @return
	 * 				The scale of x-axis
	 */
	public double getScaleX() {
		return this.scaleX;
	}

	/**Gets the scale of y-axis.
	 *
	 * @return
	 * 				The scale of y-axis
	 */
	public double getScaleY() {
		return this.scaleY;
	}

	/**Gets the scale of z-axis.
	 *
	 * @return
	 * 				The scale of z-axis
	 */
	public double getScaleZ() {
		return this.scaleZ;
	}

	/**Sets the scale of the Element
	 *
	 * @param scale
	 * 					The scale of the Element
	 */
	public void setScale(double scale) {
		this.scaleX = scale;
		this.scaleY = scale;
		this.scaleZ = scale;
	}

	/**Sets the scale of the Element
	 *
	 * @param scaleX
	 * 					The scale of the Element of x-axis direction
	 * @param scaleY
	 * 					The scale of the Element of y-axis direction
	 * @param scaleZ
	 * 					The scale of the Element of z-axis direction
	 */
	public void setScale(double scaleX, double scaleY, double scaleZ) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
	}

	/**Sets the scale of the Element of x-axis direction.
	 *
	 * @param scaleX
	 * 					The scale of the Element of x-axis direction
	 */
	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}

	/**Sets the scale of the Element of y-axis direction.
	 *
	 * @param scaleY
	 * 					The scale of the Element of y-axis direction
	 */
	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}

	/**Sets the scale of the Element of z-axis direction.
	 *
	 * @param scaleZ
	 * 					The scale of the Element of z-axis direction
	 */
	public void setScaleZ(double scaleZ) {
		this.scaleZ = scaleZ;
	}

//	/**Sets the texture to the Element.
//	 *
//	 * @param texture
//	 */
//	public void setTexture(Texture texture) {
//		this.texture = texture;
//		this.enableTexture = true;
//	}

	/**Enables the texture.
	 *
	 */
	public void enableTexture() {
		this.enableTexture = true;
	}

	/**Disables the texture.
	 *
	 */
	public void disableTexture() {
		this.enableTexture = false;
	}

	/**Check texture is enable or not.
	 *
	 */
	public boolean isEnableTexture() {
	    return this.enableTexture;
	}

    /**
    *
    */
	public void triggerMouseEvent(MouseStatus status, boolean selected) {
	    if (mouseEventCallbacks == null || mouseEventCallbacks.size() == 0) {
	        return; // do nothing
	    }

	    prevMouseOver = currentMouseOver;
	    currentMouseOver = selected;

	    for (MouseEventCallback c : mouseEventCallbacks ) {
            if (c instanceof MouseOverCallback) {
                MouseOverCallback callback = (MouseOverCallback) c;

                if (currentMouseOver) {
                    if (!prevMouseOver) {
                        callback.run(MouseOverEventType.ENTERED, this);
                    } else {
                        callback.run(MouseOverEventType.EXISTED, this);
                    }
                } else {
                    if (prevMouseOver) {
                        callback.run(MouseOverEventType.EXITED, this);
                    }
                }
            }
        }

	    if (currentMouseOver && status != null) {
	        for (MouseEventCallback c : mouseEventCallbacks ) {
	            if (c instanceof MouseClickCallback) {
	                MouseClickCallback callback = (MouseClickCallback) c;

	                switch (status) {
	                case CLICKED:
	                    callback.run(MouseClickEventType.CLICKED, this);
	                    break;

	                case PRESSED:
	                    callback.run(MouseClickEventType.PRESSED, this);
	                    break;

	                case RELEASED:
	                    callback.run(MouseClickEventType.RELEASED, this);
	                    break;

	                case DRAGGED:
	                    callback.run(MouseClickEventType.DRAGGED, this);
	                    break;

	                case MOVED:
	                    callback.run(MouseClickEventType.MOVED, this);
	                    break;

	                default:
	                    break;
	                }
	            }
	        }
	    }
	}

	/**Adds the mosueEventCallback to the Element
	 *
	 * @param callback
	 * 					mouseEventCallback
	 */
	public void addMouseEventCallback(MouseEventCallback callback) {
		if (mouseEventCallbacks == null) {
			mouseEventCallbacks = new ArrayList<MouseEventCallback>();
		}
		mouseEventCallbacks.add(callback);
	}

	/**Returns the callbacks that are added to the Element.
	 *
	 * @return
	 * 				The callbacks that are added to the Element.
	 */
	public List<MouseEventCallback> getMouseEventCallbacks() {
	    return mouseEventCallbacks;
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

	/**
	 * Modify visibility of the Element.
	 *
	 * @param visible {@code true} to make the Element visible, {@code false} to
	 *     hide the Element.
	 */
	public void setVisible(boolean visible) {
	    this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isGradation() {
		return gradation;
	}

	public void setGradation(boolean bool) {
		gradation = bool;
	}

//	public Mask getMask() {
//		return mask;
//	}
//
//	public void setMask(Mask mask) {
//		this.mask = mask;
//	}
//
//	public void clearMask() {
//		this.mask = null;
//	}
//
//	public boolean isMasked() {
//		if(enableMask)
//			return mask != null;
//		else
//			return false;
//    }

	public boolean isDepthTest() {
		return depthTest;
	}

	public void setDepthTest(boolean depthTest) {
		this.depthTest = depthTest;
	}

//	public void remove() {
//		this.removeElement = true;
//	}
//
//	public boolean isRemove() {
//		return this.removeElement;
//	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public void enableMask() {
		enableMask = true;
	}

	public void disableMask() {
		this.enableMask = false;
	}

	@Override
    public void reset(GL2 gl) {}
}
