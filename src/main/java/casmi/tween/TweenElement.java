/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011-2012, Xcoo, Inc.
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

package casmi.tween;

import java.util.ArrayList;
import java.util.List;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;

public class TweenElement implements Tweenable {
//	public static final int POSITION = 0;
//	public static final int POSITION_3D = 1;
//	public static final int ROTATION_2D = 2;
//	public static final int ROTATION_3D = 3;
//	public static final int ALPHA = 4;
//	public static final int ALPHA_STROKE = 10;
//	public static final int ALPHA_FILL = 11;
//	public static final int SCALE = 5;
//	public static final int SCALE_3D = 6;
//	public static final int SCALE_X = 7;
//	public static final int SCALE_Y = 8;
//	public static final int SCALE_Z = 9;
	
	private double positionX, positionY, positionZ;
	private double rotationX, rotationY, rotationZ;
	private double scaleX, scaleY, scaleZ;
	private double strokeAlpha, fillAlpha;
	private static final int NUM_ELEMENT_VALUES = 11;
	private double[] initialValues = new double [NUM_ELEMENT_VALUES];
	
	private Element element;
	
	public TweenElement (Element e){
		this.element = e;
		init();
		reset();
	}
		
	public Element getElement(){
		return element;
	}

	@Override
	public List<Float> getTweenValues(TweenType tweenType) {

		List<Float> result = new ArrayList<Float>();
		
		switch (tweenType) {
		case POSITION:
			result.add( (float) positionX );
			result.add( (float) positionY );
			break;
			
		case POSITION_3D:
			result.add( (float) positionX );
			result.add( (float) positionY );
			result.add( (float) positionZ );
			break;
		case ROTATION_2D:
			result.add( (float) rotationZ );
			break;
		case ROTATION_3D:
			result.add( (float) rotationX );
			result.add( (float) rotationY );
			result.add( (float) rotationZ );
			break;
		case ALPHA:
			result.add( (float) fillAlpha );
			break;
		case ALPHA_STROKE:
			result.add( (float) strokeAlpha);
			break;
		case ALPHA_FILL:
			result.add( (float) fillAlpha);
			break;
		case SCALE:
			result.add( (float) scaleX );
			break;
		case SCALE_3D:
			result.add( (float) scaleX );
			result.add( (float) scaleY );
			result.add( (float) scaleZ );
			break;
		case SCALE_X:
			result.add( (float) scaleX );
			break;
		case SCALE_Y:
			result.add( (float) scaleY );
			break;
		case SCALE_Z:
			result.add( (float) scaleZ );
			break;
		}
		
		return result;
	}

	@Override
	public void update(Graphics g, TweenType tweenType, List<Float> newValues) {
		g.pushMatrix();
		switch (tweenType) {
			case POSITION:
				positionX = newValues.get(0);
				positionY = newValues.get(1);
				break;
			case POSITION_3D:
				positionX = newValues.get(0);
				positionY = newValues.get(1);
				positionZ = newValues.get(2);
				break;
			case ROTATION_2D:
				rotationZ = newValues.get(0);
				break;
			case ROTATION_3D:
				rotationX = newValues.get(0);
				rotationY = newValues.get(1);
				rotationZ = newValues.get(2);
				break;
			case ALPHA:
				strokeAlpha = newValues.get(0);
				fillAlpha = newValues.get(0);
				break;
			case ALPHA_STROKE:
				strokeAlpha = newValues.get(0);
				break;
			case ALPHA_FILL:
				fillAlpha = newValues.get(0);
				break;
			case SCALE:
				scaleX = newValues.get(0);
				scaleY = newValues.get(0);
				break;
			case SCALE_3D:
				scaleX = newValues.get(0);
				scaleY = newValues.get(1);
				scaleZ = newValues.get(2);
				break;
			case SCALE_X:
				scaleX = newValues.get(0);
				break;
			case SCALE_Y:
				scaleY = newValues.get(0);
				break;
			case SCALE_Z:
				scaleZ = newValues.get(0);
				break;
		}
		render(g,tweenType);
		g.popMatrix();
		
	}
	
	@Override
	public void end(Graphics g, TweenType tweenType){

		render(g,tweenType);
	}
	
	@Override
	public void render(Graphics g, TweenType tweenType){
		switch (tweenType) {
		case POSITION:

			element.setPosition(positionX, positionY);
			break;
		case POSITION_3D:

			element.setPosition(positionX, positionY, positionZ);
			break;
		case ROTATION_2D:

			element.setRotation(rotationZ);
			break;
		case ROTATION_3D:

			element.setRotation(rotationX, rotationY, rotationZ); 
			break;
		case ALPHA:
		case ALPHA_STROKE:
		case ALPHA_FILL:

			element.setStrokeColorAlpha((int)strokeAlpha);
			element.setFillColorAlpha((int)fillAlpha);
			break;
		case SCALE:
		case SCALE_3D:
		case SCALE_X:
		case SCALE_Y:
		case SCALE_Z:

			element.setScaleX(scaleX);
			element.setScaleY(scaleY);
			element.setScaleZ(scaleZ);
			break;
		}
	}
	
	private void init(){
		initialValues[0] = this.element.getX();
		initialValues[1] = this.element.getY();
		initialValues[2] = this.element.getZ();
		initialValues[3] = this.element.getRotationX();
		initialValues[4] = this.element.getRotationY();
		initialValues[5] = this.element.getRotationZ();
		initialValues[6] = this.element.getScaleX();
		initialValues[7] = this.element.getScaleY();
		initialValues[8] = this.element.getScaleZ();
		initialValues[9] = this.element.getStrokeColor().getA();
		initialValues[10] = this.element.getFillColor().getA();
		this.element.setTween(true);
	}
	
	public void reset(){
		positionX = initialValues[0];
		positionY = initialValues[1];
		positionZ = initialValues[2];
		rotationX= initialValues[3];
		rotationY= initialValues[4];
		rotationZ= initialValues[5];
		scaleX= initialValues[6];
		scaleY= initialValues[7];
		scaleZ= initialValues[8];
		strokeAlpha = initialValues[9];
		fillAlpha = initialValues[10];

		
		element.setPosition(positionX, positionY, positionZ);
		element.setRotation(rotationX, rotationY, rotationZ);
		element.setScale(scaleX, scaleY, scaleZ);
		element.setStrokeColorAlpha((int)strokeAlpha);
		element.setFillColorAlpha((int)fillAlpha);
		

	}
	
}