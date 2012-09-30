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
import casmi.graph.data.PairData;
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
	private List<Rect> rlist = new ArrayList<Rect>();
	private Color rectColor;
	private double barRatio = 0.8;
	private int delayMillSec = 300;
	private boolean animation = true;
	DynamicBarGraphTweenType tweenType = DynamicBarGraphTweenType.AT_ONCE;
	

	public DynamicBarGraph(double width, double height, MatrixData2D m) {
		super(width, height, m);
		rectColor = new RGBColor(ColorSet.WHITE);
		setGraphBar();
	}
	
	public DynamicBarGraph(double width, double height, MatrixData2D m, double maxY, double minY) {
		super(width, height, m, maxY, minY);
		rectColor = new RGBColor(ColorSet.WHITE);
		setGraphBar();
	}
	
	public DynamicBarGraph(double width, double height, MatrixData2D m, boolean animation) {
		super(width, height, m);
		rectColor = new RGBColor(ColorSet.WHITE);
		this.animation = animation;
		setGraphBar();
	}
	
	public DynamicBarGraph(double width, double height, MatrixData2D m, double maxY, double minY, boolean animation) {
		super(width, height, m, maxY, minY);
		rectColor = new RGBColor(ColorSet.WHITE);
		this.animation = animation;
		setGraphBar();
	}
	
	public void setBarColor(Color color){
		rectColor = color;
		for(Rect r: rlist)
			r.setFillColor(rectColor);
		
	}
	
	public void setBarColor(ColorSet colorset){
		rectColor = new RGBColor(colorset);
		for(Rect r: rlist)
			r.setFillColor(rectColor);
		
	}
	
	private void setGraphBar(){
		int count = 0;
		for(Rect r: rlist)
			r.remove();
		rlist.clear();
		double barSize = (double)width / (double)m.getSize();
		this.axisHolizontal.setOffset(0.5*barSize);
		for(PairData p : m.getData()){
			//setting graph bars
			Rect r = new Rect(barSize * barRatio, 1);
			r.setFillColor(rectColor);
			r.setStroke(false);
			r.setPosition( (count+0.5)*barSize, 0);
			if(!animation){
				r.setHeight(p.getY());
				r.setY(p.getY()/2.0);
			}
			rlist.add(r);
			System.out.println(p.getX() + "\t" + p.getY());
			count++;
		}

		for(Rect r: rlist)
			add(r);
	}
	
	public void setAnimation(boolean animation) {
		this.animation = animation;
		setGraphBar();
	}

	public double getBarRatio() {
		return barRatio;
	}

	public void setBarRatio(double barRatio) {
		this.barRatio = barRatio;
	}
	
	public void setData(MatrixData2D m){
		this.m = m;
		for(Rect r: rlist)
			r.remove();
		rlist.clear();
		setGraphBar();
		for(Rect r: rlist)
			add(r);
	}
	
	private void animationUpdate() {

		if(tweenreset){
			tweenreset = false;
			for(TweenElement t: twlist){
				t.reset();
			}
		}
 
		if(tweenstart){
			tweenstart = false;
			double barSize = (double)width / (double)m.getSize();
			tweenstart = false;
            manager = new TweenManager();
            addTweenManager(manager);

			tw = null;

			int count = 0;
			for(Rect r: rlist){
				tw = new TweenElement(r);
				twlist.add(tw);
				TweenSerialGroup tsg = null; 
				if(tweenType == DynamicBarGraphTweenType.AT_ONCE){
					tsg = TweenSerialGroup.create(
						TweenParallelGroup.create(
								Tween.to(tw, TweenType.SCALE_Y, tweenMillSec, Linear.INOUT).target((float) m.getDataY(count)),
								Tween.to(tw, TweenType.POSITION_3D, tweenMillSec, Linear.INOUT).target((count+0.5)*barSize, (float) (m.getDataY(count)/2.0), 0)
								)
						);
				} else if(tweenType == DynamicBarGraphTweenType.ORDER) {
					tsg = TweenSerialGroup.create(
							TweenParallelGroup.create(
									Tween.to(tw, TweenType.SCALE_Y, tweenMillSec, Linear.INOUT).target((float) m.getDataY(count)).addDelay(delayMillSec * count),
									Tween.to(tw, TweenType.POSITION_3D, tweenMillSec, Linear.INOUT).target((count+0.5)*barSize, (float) (m.getDataY(count)/2.0), 0).addDelay(delayMillSec * count)
									)
							);
				}
				manager.add(tsg);
				count++;
			}    		
		}
	}
	
	@Override
	public void update(){
		animationUpdate();
	}
	

	
	public void resetTween() {
		this.tweenstart = false;
		this.tweenreset = true;
	}

	
	public boolean isTweenstart(){
		return tweenstart;
	}

	public int getDelayMilliSec() {
		return delayMillSec;
	}

	public void setDelayMilliSec(int delayMilliSec) {
		this.delayMillSec = delayMilliSec;
	}

	
	public void setTweenType(DynamicBarGraphTweenType tweentype){
		this.tweenType = tweentype;
	}
	
	public DynamicBarGraphTweenType getTweenType(){
		return this.tweenType;
	}

}
