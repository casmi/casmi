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


package casmi.extension.graph.view;

import java.util.ArrayList;
import java.util.List;

import casmi.extension.graph.data.MatrixData2D;
import casmi.extension.graph.data.PairData;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Arc;
import casmi.graphics.group.Group;
import casmi.tween.Tween;
import casmi.tween.TweenCallback;
import casmi.tween.TweenCallbackTypes;
import casmi.tween.TweenManager;
import casmi.tween.TweenSerialGroup;
import casmi.tween.equations.Linear;
import casmi.tween.simpletweenables.TweenDouble;
import casmi.util.Random;

/**
 * DynamicCircleGraph Class.
 * 
 * @author Y.Ban
 */

public class DynamicCircleGraph extends Group{

	private TweenDouble tw; 
	private List<Arc> arclist = new ArrayList<Arc>();
	private List<Color> colorList = new ArrayList<Color>();
	private boolean tweenstart = false;
	private TweenManager manager;
	private MatrixData2D data;
	private double radius;
	private GraphTurn turn = GraphTurn.COUNTERCLOCKWISE;
	private int tweenMilliSecond = 1500;
    TweenCallback tweencallback;
	
	public DynamicCircleGraph(MatrixData2D m, double radius){
		init(m, radius);
	}
	
	public DynamicCircleGraph(MatrixData2D m, double radius, GraphTurn turn){
		this.setTurn(turn);
		init(m, radius);
	}
	
	private void init(MatrixData2D m, double radius){
		data = m;
		this.radius = radius;
        tw = new TweenDouble();
        tw.setValue(0);
        setGraphArc();
        if(turn == GraphTurn.CLOCKWISE){
        	for(Arc arc: arclist)
    			arc.setRotationY(180);
        }
        
        tweencallback = new TweenCallback() {

            @Override
            public void run(TweenCallbackTypes eventType, Tween tween) {
            	arclist.get(arclist.size()-1).setEnd(451.0);
            }
        };
	}
	
	@Override
	public void update() {
		if(tweenstart){
			setTweenstart(false);
            manager = new TweenManager();
            addTweenManager(manager);
            tw = new TweenDouble();
            tw.setValue(0);
			TweenSerialGroup tsg = TweenSerialGroup.create(
					Tween.to(tw, tweenMilliSecond, Linear.INOUT).target(360).addCompleteCallback(tweencallback)
					);

			manager.add(tsg); 
		}
		
		if(tw.getValue()!=0 && tw.getValue()!=361){
			for(int i=0; i<arclist.size(); i++){
				if(arclist.get(i).getStart()-90 < tw.getValue() ){
					arclist.get(i).visible();
					double maxRad = 0;
					if(i == arclist.size()-1)
						maxRad = 360;
					else
						maxRad = arclist.get(i+1).getStart()-90;
					if(maxRad > tw.getValue()){
						if(i>0){
							arclist.get(i-1).setEnd(arclist.get(i).getStart());
						}
						arclist.get(i).setEnd(tw.getValue()+90);
					}
				}
			}
		}
	}
	
	public void resetArc(){
		this.clearAllObjects();
		System.out.println("reset!!");
		for(Arc arc: arclist)
			add(arc);
	}
	
	private void setGraphArc(){
		int count = 0;
		double total = 0;
		for(PairData p : data.getData())
			total += p.getY();
		for(PairData p : data.getData()){
			//setting graph bars
			double startRad = 0;
			for(int i = 0; i < count; i++)
				startRad += (data.getDataY(i)*360.0/total);
			
			Arc arc = new Arc(this.radius, startRad+90, startRad+91);
			colorList.add(new RGBColor(ColorSet.values()[Random.random(ColorSet.values().length)]));
			arc.setFillColor(colorList.get(count));
			arc.setStroke(false);
			arc.hidden();
			arclist.add(arc);
			add(arclist.get(count));
			System.out.println(p.getX() + "\t" + p.getY());
			count++;
		}
	}
	
	public Color getArcColor(int index){
		if(index>=colorList.size())
			index = colorList.size()-1;
		if(index < 0)
			index = 0;
		return colorList.get(index);
	}
	
	public void setArcColor(int index, Color color){
		if(index>=colorList.size())
			index = colorList.size()-1;
		if(index < 0)
			index = 0;
		colorList.set(index, color);
	}

	public MatrixData2D getData() {
		return data;
	}

	public void setData(MatrixData2D data) {
		this.data = data;
	}

	public boolean isTweenstart() {
		return tweenstart;
	}

	public void setTweenstart(boolean tweenstart) {
		this.tweenstart = tweenstart;
	}

	public int getTweenMilliSecond() {
		return tweenMilliSecond;
	}

	public void setTweenMilliSecond(int tweenMilliSecond) {
		this.tweenMilliSecond = tweenMilliSecond;
	}

	public GraphTurn getTurn() {
		return turn;
	}

	public void setTurn(GraphTurn turn) {
		this.turn = turn;
	}

}
