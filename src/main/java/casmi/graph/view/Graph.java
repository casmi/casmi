/*   casmi examples
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package casmi.graph.view;

import java.util.ArrayList;
import java.util.List;

import casmi.graph.data.MatrixData2D;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.font.Font;
import casmi.graphics.group.Group;
import casmi.tween.TweenElement;
import casmi.tween.TweenManager;

/**
 * Graph Class.
 * 
 * @author Y.Ban
 */

public class Graph extends Group{

	protected MatrixData2D m;
	protected Axis axisHolizontal;
	protected Axis axisVertical;
	protected double width, height;
	protected double minY,maxY;
	protected boolean tweenstart = false;
	protected boolean tweenreset = false;
	protected TweenManager manager;
	protected int tweenMillSec = 1000;
	protected TweenElement tw; 
	protected List<TweenElement> twlist = new ArrayList<TweenElement>();
	
	public Graph(double width, double height, MatrixData2D m){
		setSize(width, height);
		setMatrixData2D(m);
		minY = m.getMin();
		maxY = m.getMax();
		axisHolizontal = new Axis(GraphAxis.HORIZONTAL,this.width, this.m);
		axisVertical = new Axis(GraphAxis.VERTICAL, this.height, this.m);
		add(axisHolizontal);
		add(axisVertical);
	}
	
	public Graph(double width, double height, MatrixData2D m, double max, double min){
		setSize(width, height);
		setMatrixData2D(m);
		this.minY = min;
		this.maxY = max;
		axisHolizontal = new Axis(GraphAxis.HORIZONTAL,this.width, this.m);
		axisVertical = new Axis(GraphAxis.VERTICAL, this.height, this.m, max, min);
		add(axisHolizontal);
		add(axisVertical);
	}
	
	public void setSize(double width, double height){
		this.width = width;
		this.height = height;
	}
	
	public double getWidth(){
		return width;
	}
	
	public double getHeight(){
		return height;
	}
	
	public void setAxisNameFont(Font f){
		this.axisHolizontal.setNameFont(f);
		this.axisVertical.setNameFont(f);
	}
	
	public void setAxisDivisionFont(Font f){
		this.axisHolizontal.setDivisionFont(f);
		this.axisVertical.setDivisionFont(f);
	}
	
	public void setAxisColor(Color color){
		axisHolizontal.setStrokeColor(color);
		axisVertical.setStrokeColor(color);
	}
	
	public void setAxisColor(ColorSet color){
		axisHolizontal.setStrokeColor(color);
		axisVertical.setStrokeColor(color);
	}
	
	public void setAxisName(GraphAxis axis, String name){
		switch(axis){
		case HORIZONTAL:
			this.axisHolizontal.setAxisName(name);
			break;
		case VERTICAL:
			this.axisVertical.setAxisName(name);
			break;
		}
	}
	
	public Axis getAxisX(){
		return axisHolizontal;
	}
	
	public Axis getAxisY(){
		return axisVertical;
	}
	
	public void drawAxis(){
		this.axisHolizontal.setData(m);
		this.axisVertical.setData(m);
	}
	
	public void setMatrixData2D(MatrixData2D m){
		this.m = m;
	}
	
	public MatrixData2D getMatrixData2D(){
		return m;
	}
	
	public void setDivisionSpace(GraphAxis axis, double space){
		switch(axis){
		case HORIZONTAL:
			this.axisHolizontal.setDivisionDiff(space);
			break;
		case VERTICAL:
			this.axisVertical.setDivisionDiff(space);
			break;
		}
	}
	
	public void startTween() {
		this.tweenstart = true;
	}
	

	public int getTweenMilliSec() {
		return tweenMillSec;
	}

	public void setTweenMilliSec(int tweenMilliSec) {
		this.tweenMillSec = tweenMilliSec;
	}
	
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
