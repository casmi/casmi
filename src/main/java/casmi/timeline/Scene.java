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

package casmi.timeline;

import casmi.graphics.Graphics;

/**
 * Scene class for time line animation.
 * 
 * @author Y. Ban
 * 
 */

abstract public class Scene  {
	
	private int id;
	private double time;
	private double sceneA = 1.0;

	abstract public void setup();

    abstract public void draw(Graphics g);

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getSceneA() {
		return sceneA;
	}

	public void setSceneA(double sceneA,Graphics g) {
		this.sceneA = sceneA;
		g.setSceneA(this.sceneA);
	}
	
}
