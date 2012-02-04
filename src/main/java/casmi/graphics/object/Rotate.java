package casmi.graphics.object;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;

public class Rotate extends Element implements ObjectRender{
	private double x;
	private double y;
	private double z;
	
	public Rotate(double x,double y,double z) {
		this.x = x;
		this.y = y;
		this.z = z;
    }
	
	public void setRotate(double x,double y,double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setRotateX(double angle){
		this.x = angle;
	}
	
	public void setRotateY(double angle){
		this.y = angle;
	}
	
	public void setRotateZ(double angle){
		this.z = angle;
	}
	
	public double getRotateX(){
		return this.x;
	}
	
	public double getRotateY(){
		return this.y;
	}
	
	public double getRotateZ(){
		return this.z;
	}
	
	@Override
	public void render(Graphics g){
		g.rotateX(x);
		g.rotateY(y);
		g.rotateZ(z);
	}
	
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
    }
    
}
