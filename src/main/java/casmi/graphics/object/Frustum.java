package casmi.graphics.object;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;

public class Frustum extends Element implements ObjectRender,Perse{
	private double left;
	private double right;
	private double bottom;
	private double top;
	private double near;
	private double far;
	private boolean def = false;
	
	public Frustum(double left, double right, double bottom, double top,
			double near, double far) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.near = near;
		this.far = far;
    }
	
	public Frustum(){
		def = true;
	}
	
	public void set(double left, double right, double bottom, double top,
			double near, double far) {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.near = near;
		this.far = far;
    }
    
	@Override
	public void render(Graphics g){
		if(def){
			g.frustum();
		} else {
			g.frustum(left, right, bottom, top, near, far);
		}
	}
	
	@Override
	public void simplerender(Graphics g){
		if(def){
			g.simplefrustum();
		} else {
			g.simplefrustum(left, right, bottom, top, near, far);
		}
	}
	
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
    }
    
}
