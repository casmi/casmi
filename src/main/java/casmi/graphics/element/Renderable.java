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

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public interface Renderable {
    void render(GL gl, GLU glu, int width, int height);
    void setAlpha(double alpha);
    void setEndTweenXYZ(float x,float y,float z);
	void setEndTweenRotate(float xr,float yr,float zr);
	void setEndTweenScale(double sx,double sy,double sz);
	void setEndTweenA(double as,double af);
	double[] setTween();
}
