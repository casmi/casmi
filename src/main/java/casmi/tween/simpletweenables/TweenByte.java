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

package casmi.tween.simpletweenables;

import java.util.ArrayList;
import java.util.List;

import casmi.graphics.Graphics;
import casmi.tween.SimpleTweenable;
import casmi.tween.TweenType;

/**
 * Defines a byte value on which tweens can be applied.
 *
 * @see SimpleTweenable
 */
public class TweenByte implements SimpleTweenable {
	private byte value;
	
	public TweenByte(byte f){
		value = f;
	}
	
	public TweenByte(){
		value = 0;
	}

	@Override
	public List<Float> getTweenValues(TweenType tweenType) {
		List<Float> returnValues = new ArrayList<Float>();
		return returnValues;
	}

	@Override
	public void update(Graphics g, TweenType tweenType, List<Float> newValues) {
		value = (byte) Math.round(newValues.get(0));
	}

	public void end(Graphics g, int tweenType) {
	}
	
	public void render(Graphics g, int tweenType) {
	}
	
	public byte getValue(){
		return value;
	}
	
	public void setValue(byte value){
		this.value = value;
	}

	@Override
	public void end(Graphics g, TweenType tweenType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g, TweenType tweenType) {
		// TODO Auto-generated method stub
		
	}

}
