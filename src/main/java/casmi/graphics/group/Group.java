package casmi.graphics.group;

import casmi.graphics.object.GraphicsObject;


abstract public class Group extends GraphicsObject{

	
	public Group(){
		super();
	}
	
	abstract public void setup();
	
	abstract public void update();

}
