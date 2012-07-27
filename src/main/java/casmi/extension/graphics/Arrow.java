package casmi.extension.graphics;

import casmi.graphics.element.Rect;
import casmi.graphics.element.Triangle;
import casmi.graphics.group.Group;

public class Arrow extends Group{
	
	private Triangle triangle;
	private Rect rect;
	private double topSize;
	private double length;
	private double width;
	private ArrowAlign align = ArrowAlign.BOTTOM;
	
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

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
