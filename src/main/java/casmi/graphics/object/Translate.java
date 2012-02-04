package casmi.graphics.object;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;

public class Translate extends Element implements ObjectRender{
	private double x;
	private double y;
	private double z;
	
	public Translate(double x,double y,double z) {
		this.x = x;
		this.y = y;
		this.z = z;
    }
	
	public void setTranslate(double x,double y,double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setTranslateX(double x){
		this.x = x;
	}
	
	public void setTranslateY(double y){
		this.y = y;
	}
	
	public void setTranslateZ(double z){
		this.z = z;
	}
	
	public double getTranslateX(){
		return this.x;
	}
	
	public double getTranslateY(){
		return this.y;
	}
	
	public double getTranslateZ(){
		return this.z;
	}


	
	@Override
	public void render(Graphics g){
		g.translate(x, y, z);
	}
	
	
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
    }
    
}
