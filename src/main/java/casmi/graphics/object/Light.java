package casmi.graphics.object;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.element.Element;
import casmi.matrix.Vertex;

public class Light extends Element implements ObjectRender{
	

	private Vertex dv;
	private int index;
	private Vertex v;
	private Color c;
	private double angle = 30;
	
	public enum LightMode {
		AMBIENT, DIRECTION, POINT, SPOT
	}
	
	LightMode mode;
	
	public Light(LightMode mode) {
		this.mode = mode;
		v = new Vertex();
		dv = new Vertex();
		c = new Color(0,0,0);
    }
	
	public Light(){
		this.mode = LightMode.AMBIENT;
		v = new Vertex();
		dv = new Vertex();
		c = new Color(0,0,0);
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public void setColor(Color c){
		this.c = c;
	}
	
	public void setColor(ColorSet c){
		this.c = Color.color(c);
	}
	
	public void setColor(int r, int g, int b){
		this.c.setR(r);
		this.c.setG(g);
		this.c.setB(b);
	}
	
	public void setAlpha(int alpha){
		this.c.setA(alpha);
	}
	
	public void setDirection(double x,double y, double z) {
		this.dv.set(x, y, z);
    }
	
	public void setMode(LightMode mode){
		this.mode = mode;
	}
    
	@Override
	public void render(Graphics g){
		v.set(x, y, z);
		switch(mode){
		case AMBIENT:
			g.ambientLight(index, c, v);
			break;
		case DIRECTION:
			g.directionalLight(index, c, dv);
			break;
		case POINT:
			g.pointLight(index, c, v);
			break;
		case SPOT:
			g.spotLight(index, c, v, (float)dv.getX(), (float)dv.getY(), (float)dv.getZ(), (float)angle);
			
		}
		
	}
	
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
    }


	public double getDirectionX() {
		return dv.getX();
	}


	public void setDirectionX(double directionX) {
		this.dv.setX(directionX);
	}


	public double getDirectionY() {
		return dv.getY();
	}


	public void setDirectionY(double directionY) {
		this.dv.setY(directionY);
	}


	public double getDirectionZ() {
		return dv.getZ();
	}


	public void setDirectionZ(double directionZ) {
		this.dv.setZ(directionZ);
	}


	public double getAngle() {
		return angle;
	}


	public void setAngle(double angle) {
		this.angle = angle;
	}
    
}
