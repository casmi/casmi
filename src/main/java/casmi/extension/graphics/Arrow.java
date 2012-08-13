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

package casmi.extension.graphics;

import casmi.graphics.element.Rect;
import casmi.graphics.element.Triangle;
import casmi.graphics.group.Group;
import casmi.matrix.Vertex;

/**
 * Arrow Class.
 * 
 * @author Y. Ban
 */

public class Arrow extends Group{
	
	private Triangle triangle;
	private Rect rect;
	private double topSize;
	private double length;
	private double width;
	private ArrowAlign align = ArrowAlign.BOTTOM;
	private Vertex startPoint, endPoint;
	
	public Arrow(double topSize, double length) {
		this.topSize = topSize;
		this.length = length;
		this.width = topSize/3.0;
		rect = new Rect(this.width,this.length-topSize*Math.cos(Math.PI/6.0));
		rect.setPosition(0, (length-topSize*Math.cos(Math.PI/6.0))/2.0);
		
		triangle = new Triangle(-topSize/2,(length-topSize*Math.cos(Math.PI/6.0)),topSize/2,(length-topSize*Math.cos(Math.PI/6.0)),0,length);
		
		add(rect);
		add(triangle);
		
		
		this.setStroke(false);
	}
	
	public Arrow(double topSize, Vertex start, Vertex end) {
		this.topSize = topSize;
		this.length = Math.sqrt(Math.pow(end.getY()-start.getY(), 2)+Math.pow(end.getX()-start.getX(), 2));
		this.width = topSize/3.0;
		this.align = ArrowAlign.CORNER;
		rect = new Rect(this.width,this.length-topSize*Math.cos(Math.PI/6.0));
		rect.setPosition(0, (length-topSize*Math.cos(Math.PI/6.0))/2.0);
		
		triangle = new Triangle(-topSize/2,(length-topSize*Math.cos(Math.PI/6.0)),topSize/2,(length-topSize*Math.cos(Math.PI/6.0)),0,length);
		
		add(rect);
		add(triangle);
		this.setPosition(start);
		this.setRotation(-Math.atan2(end.getX()-start.getX(), end.getY()-start.getY())*180/Math.PI);
		
		this.startPoint = start;
		this.endPoint = end;
		
		this.setStroke(false);
	}
	
	public void setWidth(double width) {
		this.width = width;
		rect.set(this.width, rect.getHeight());
	}
	
	public void setArrow() {
		switch(align){
		case TOP:
			rect.set(this.width,this.length-topSize*Math.cos(Math.PI/6.0));
			rect.setPosition(0, -topSize*Math.cos(Math.PI/6.0)-(length-topSize*Math.cos(Math.PI/6.0))/2.0);
			triangle.set(-topSize/2,-topSize*Math.cos(Math.PI/6.0),topSize/2,-topSize*Math.cos(Math.PI/6.0),0,0);
			break;
			
		case CENTER:
			rect.set(this.width,this.length-topSize*Math.cos(Math.PI/6.0));
			rect.setPosition(0, -topSize*Math.cos(Math.PI/6.0)/2.0);
			triangle.set(-topSize/2,length/2-topSize*Math.cos(Math.PI/6.0),topSize/2,length/2-topSize*Math.cos(Math.PI/6.0),0,length/2);
			break;
			
			
		case BOTTOM:
			rect.set(this.width,this.length-topSize*Math.cos(Math.PI/6.0));
			rect.setPosition(0, (length-topSize*Math.cos(Math.PI/6.0))/2.0);
			triangle.set(-topSize/2,(length-topSize*Math.cos(Math.PI/6.0)),topSize/2,(length-topSize*Math.cos(Math.PI/6.0)),0,length);
			break;
			
		case CORNER:
			this.length = Math.sqrt(Math.pow(endPoint.getY()-startPoint.getY(), 2)+Math.pow(endPoint.getX()-startPoint.getX(), 2));
			rect.set(this.width,this.length-topSize*Math.cos(Math.PI/6.0));
			rect.setPosition(0, (length-topSize*Math.cos(Math.PI/6.0))/2.0);
			triangle.set(-topSize/2,(length-topSize*Math.cos(Math.PI/6.0)),topSize/2,(length-topSize*Math.cos(Math.PI/6.0)),0,length);
			this.setPosition(startPoint);
			this.setRotation(-Math.atan2(endPoint.getX()-startPoint.getX(), endPoint.getY()-startPoint.getY())*180/Math.PI);
			break;
		}
	}
	
	public void setLength(double length) {
		this.length = length;
		setArrow();
	}
	
	public void setTopSize(double size) {
		this.topSize = size;
		setArrow();
	}
	
	public void setAlign(ArrowAlign align) {
		this.align = align;
		setArrow();
	}
	
	public void setCorner(Vertex start, Vertex end){
		this.startPoint = start;
		this.endPoint = end;
		this.align = ArrowAlign.CORNER;
		setArrow();
	}
	
	public void setStartCorner(double x, double y){
		this.startPoint.set(x, y);
		this.align = ArrowAlign.CORNER;
		setArrow();
	}
	
	public void setEndCorner(double x, double y){
		this.endPoint.set(x, y);
		this.align = ArrowAlign.CORNER;
		setArrow();
	}
	
	public Vertex getStartCorner(){
		return startPoint;
	}
	
	public Vertex getEndCorner(){
		return endPoint;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
