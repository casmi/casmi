package casmi.graphics.object;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;

public class Perspective extends Element implements ObjectRender,Perse{
	private double fov;
	private double aspect;
	private double zNear;
	private double zFar;
	private boolean def = false;
	
	public Perspective(double fov, double aspect, double zNear, double zFar) {
		this.fov = fov;
		this.aspect = aspect;
		this.zNear = zNear;
		this.zFar = zFar;
    }
	
	public Perspective(){
		def = true;
	}
	
	public void set(double fov, double aspect, double zNear, double zFar) {
		this.fov = fov;
		this.aspect = aspect;
		this.zNear = zNear;
		this.zFar = zFar;
    }
    
	@Override
	public void render(Graphics g){
		if(def){
			g.perspective();
		} else {
			g.perspective(fov, aspect, zNear, zFar);
		}
	}
	
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
    }
    
    @Override
    public void simplerender(Graphics g){
    	if(def){
			g.simpleperspective();
		} else {
			g.simpleperspective(fov, aspect, zNear, zFar);
		}
    }
    
}
