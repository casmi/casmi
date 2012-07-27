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

package casmi.graphics.object;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.element.Element;

/**
 * @author Y. Ban
 */
public class BackGround extends Element implements ObjectRender{
	private double red;
	private double green;
	private double blue;
	private double gray;
	private Color c;
	private ColorSet cset;
	private enum colorMode {
		RGB, Gray, Color, ColorSet
	}
	
	private colorMode mode;
	
	public BackGround(double gray) {
		this.gray = gray;
		mode = colorMode.Gray;
    }
	
	public BackGround(double r,double g,double b){
		this.red = r;
		this.green = g;
		this.blue = b;
		mode = colorMode.RGB;
	}
	
	public BackGround(Color c){
		this.c = c;
		mode = colorMode.Color;
	}
	
	public BackGround(ColorSet cset){
		this.cset = cset;
		mode = colorMode.ColorSet;
	}

	
	@Override
	public void render(Graphics g){
		switch(mode){
		case Gray:
			g.background((float)gray);
			break;
		case RGB:
			g.background((float)red, (float)green, (float)blue);
			break;
		case Color:
			g.background(c);
			break;
		case ColorSet:
			g.background(cset);
			break;
		}
	}
	
	
    @Override
    public void render(GL2 gl, GLU glu, int width, int height) {
    }
    
}
