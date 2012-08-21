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


package casmi.graph.view;

import java.util.ArrayList;
import java.util.List;

import casmi.graph.data.MatrixData2D;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Line;
import casmi.matrix.Vertex;

/**
 * LineGraph Class.
 * 
 * @author Y.Ban
 */

public class LineGraph extends Graph{
	
	private List<Line> lines;
	private RGBColor topColor;
	private RGBColor bottomColor;
	private boolean gradation;
	private boolean useColor;
	private double xDivision;
	private double yDivision;

	public LineGraph(double width, double height, MatrixData2D m) {
		super(width, height, m);
		this.bottomColor = new RGBColor(ColorSet.DARK_BLUE);
		this.topColor = new RGBColor(ColorSet.DARK_ORANGE);
		useColor = true;
		gradation = true;
		lines = new ArrayList<Line>();
		setGraphLine();
	}
	
	public LineGraph(double width, double height, MatrixData2D m, double maxY, double minY) {
		super(width, height, m, maxY, minY);
		this.bottomColor = new RGBColor(ColorSet.DARK_BLUE);
		this.topColor = new RGBColor(ColorSet.DARK_ORANGE);
		useColor = true;
		gradation = true;
		lines = new ArrayList<Line>();
		setGraphLine();
	}
	
	private void setGraphLine(){
		xDivision = width/(double)m.getSize();
		yDivision = height/(double)(maxY-minY);
		lines.clear();
		int index = 0;
		for(int i = 0; i<this.m.getSize()-1; i++){
			Vertex v1 = new Vertex(xDivision*i, yDivision*(this.m.getDataY(i)-minY));
			Vertex v2 = new Vertex(xDivision*(i+1), yDivision*(this.m.getDataY(i+1)-minY));
			Line l = new Line(v1, v2);
			l.setCornerColor(0, RGBColor.lerpColor(this.bottomColor, this.topColor, v1.getY()/(double)height));
			l.setCornerColor(1, RGBColor.lerpColor(this.bottomColor, this.topColor, v2.getY()/(double)height));
        	lines.add(l);
        	add(lines.get(index));
        	index++;
        }
	}
	
	public void resetGraph(){
		this.clearAllObjects();
		this.drawAxis();
		this.add(axisHolizontal);
		this.add(axisVertical);
		setGraphLine();
	}
	
	public void setColor(RGBColor topColor, RGBColor bottomColor){
		this.topColor = topColor;
		this.bottomColor = bottomColor;
	}

	public boolean isGradation() {
		return gradation;
	}

	public void setGradation(boolean gradation) {
		this.gradation = gradation;
	}

	public boolean isUseColor() {
		return useColor;
	}

	public void setUseColor(boolean useColor) {
		this.useColor = useColor;
	}
	
	public void draw() {
		
	}

	public List<Line> getLines(){
		return lines;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	public double getxDivision() {
		return xDivision;
	}


	public double getyDivision() {
		return yDivision;
	}

}
