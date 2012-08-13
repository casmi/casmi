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


package casmi.extension.graph.view;

import casmi.extension.graph.data.PairData;

import java.util.ArrayList;
import java.util.List;

import casmi.extension.graph.data.MatrixData2D;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Rect;
import casmi.tween.Tween;
import casmi.tween.TweenElement;
import casmi.tween.TweenManager;
import casmi.tween.TweenParallelGroup;
import casmi.tween.TweenSerialGroup;
import casmi.tween.TweenType;
import casmi.tween.equations.Linear;

/**
 * DynamicBarGraph Class.
 * 
 * @author Y.Ban
 */

public class DynamicBarGraph extends Graph{
	private TweenElement tw; 
	private List<Rect> rlist = new ArrayList<Rect>();
	private Color rectColor;
	private double barRatio = 0.8;
	private boolean tweenstart = false;
	private TweenManager manager;
	

	public DynamicBarGraph(double width, double height, MatrixData2D m) {
		super(width, height, m);
		rectColor = new RGBColor(ColorSet.WHITE);
		setGraphBar();
		for(Rect r: rlist)
			add(r);
	}
	
	public DynamicBarGraph(double width, double height, MatrixData2D m, double maxY, double minY) {
		super(width, height, m, maxY, minY);
		rectColor = new RGBColor(ColorSet.WHITE);
		setGraphBar();
		for(Rect r: rlist)
			add(r);
	}
	
	public void setRectColor(Color color){
		rectColor = color;
		for(Rect r: rlist)
			r.setFillColor(rectColor);
		
	}
	
	private void setGraphBar(){
		int count = 0;
		double barSize = (double)width / (double)m.getSize();
		this.axisHolizontal.setOffset(0.5*barSize);
		for(PairData p : m.getData()){
			//setting graph bars
			Rect r = new Rect(barSize * barRatio, 1);
			r.setFillColor(rectColor);
			r.setStroke(false);
			r.setPosition( (count+0.5)*barSize, 0);
			rlist.add(r);
			System.out.println(p.getX() + "\t" + p.getY());
			count++;
		}
	}

	public double getBarRatio() {
		return barRatio;
	}

	public void setBarRatio(double barRatio) {
		this.barRatio = barRatio;
	}
	
	@Override
	public void update(){
 
		if(tweenstart){
			double barSize = (double)width / (double)m.getSize();
			setTweenstart(false);
            manager = new TweenManager();
            addTweenManager(manager);

			tw = null;

			int count = 0;
			for(Rect r: rlist){
				tw = new TweenElement(r);
				TweenSerialGroup tsg = TweenSerialGroup.create(
						TweenParallelGroup.create(
								Tween.to(tw, TweenType.SCALE_Y, 1000, Linear.INOUT).target((float) m.getDataY(count)),
								Tween.to(tw, TweenType.POSITION_3D, 1000, Linear.INOUT).target((count+0.5)*barSize, (float) (m.getDataY(count)/2.0), 0)
								)
						);
				manager.add(tsg);
				count++;
			}    		
		}
	}
	
	public void setTweenstart(boolean start){
		this.tweenstart = start;
	}
	
	public boolean isTweenstart(){
		return tweenstart;
	}


}
