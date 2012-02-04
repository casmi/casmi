package casmi.graphics.object;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;

public class Camera extends Element implements ObjectRender{
	private double eyeX;
	private double eyeY;
	private double eyeZ;
	private double centerX;
	private double centerY;
	private double centerZ;
	private double upX;
	private double upY;
	private double upZ;
	private boolean def = false;
	
	public Camera(double eyeX, double eyeY, double eyeZ, double centerX,
			double centerY, double centerZ, double upX, double upY, double upZ) {
		this.eyeX = eyeX;
		this.eyeY = eyeY;
		this.eyeZ = eyeZ;
		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;
		this.upX = upX;
		this.upY = upY;
		this.upZ = upZ;
    }
	
	public Camera(){
		def = true;
	}
	
	public void set(double eyeX, double eyeY, double eyeZ, double centerX,
			double centerY, double centerZ, double upX, double upY, double upZ) {
		this.eyeX = eyeX;
		this.eyeY = eyeY;
		this.eyeZ = eyeZ;
		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;
		this.upX = upX;
		this.upY = upY;
		this.upZ = upZ;
    }
    
	public void render(Graphics g){
		if(def){
			g.camera();
		} else {
			g.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		}
	}
	
	public double getEyeX() {
		return eyeX;
	}

	public void setEyeX(double eyeX) {
		this.eyeX = eyeX;
	}

	public double getEyeY() {
		return eyeY;
	}

	public void setEyeY(double eyeY) {
		this.eyeY = eyeY;
	}

	public double getEyeZ() {
		return eyeZ;
	}

	public void setEyeZ(double eyeZ) {
		this.eyeZ = eyeZ;
	}

	public double getCenterX() {
		return centerX;
	}

	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}

	public double getCenterZ() {
		return centerZ;
	}

	public void setCenterZ(double centerZ) {
		this.centerZ = centerZ;
	}
	
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
    }
    
}
