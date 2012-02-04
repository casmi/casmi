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
import casmi.graphics.element.Renderable;
import casmi.matrix.Vertex;

public class TweenVertex extends Vertex implements Tweenable {
	
//	public static final int POSITION = 0;
//	public static final int POSITION_3D = 1;
	
	private Renderable r;
	private double x, y, z;

	public TweenVertex(double x, double y) {
		super(x, y);
		this.x = x;
		this.y = y;
		this.z = 0;
	}

	public TweenVertex(double x, double y, double z) {
		super(x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Renderable getRenderable() {
		return r;
	}

	@Override
	public List<Float> getTweenValues(TweenType tweenType) {
		List<Float> returnValues = new ArrayList<Float>();
		switch (tweenType) {
		case POSITION:
			returnValues.add( (float) super.getX() );
			returnValues.add( (float) super.getY() );
			break;
		case POSITION_3D:
			returnValues.add( (float) super.getX() );
			returnValues.add( (float) super.getY() );
			returnValues.add( (float) super.getZ() );
			break;
		}
		return returnValues;
	}

	@Override
	public void update(Graphics g, TweenType tweenType, List<Float> newValues) {
		switch (tweenType) {
		case POSITION:
			super.setX(newValues.get(0));
			super.setY(newValues.get(1));
			break;
		case POSITION_3D:
			super.setX(newValues.get(0));
			super.setY(newValues.get(1));
			super.setZ(newValues.get(2));
			break;
		}
	}

	@Override
	public void end(Graphics g, TweenType tweenType) {
	}

	@Override
	public void render(Graphics g, TweenType tweenType) {
	}

	public void reset() {
		super.setX(x);
		super.setY(y);
		super.setZ(z);
	}

}