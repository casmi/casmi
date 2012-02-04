package casmi.graphics.object;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.element.Element;

public class BackGround extends Element implements ObjectRender{
	private double red;
	private double green;
	private double blue;
	private double gray;
	private Color c;
	private ColorSet cset;
	private enum colorMode {
		RGB, Gray, Color, ColorSet
	}
	
	private colorMode mode;
	
	public BackGround(double gray) {
		this.gray = gray;
		mode = colorMode.Gray;
    }
	
	public BackGround(double r,double g,double b){
		this.red = r;
		this.green = g;
		this.blue = b;
		mode = colorMode.RGB;
	}
	
	public BackGround(Color c){
		this.c = c;
		mode = colorMode.Color;
	}
	
	public BackGround(ColorSet cset){
		this.cset = cset;
		mode = colorMode.ColorSet;
	}

	
	@Override
	public void render(Graphics g){
		switch(mode){
		case Gray:
			g.background((float)gray);
			break;
		case RGB:
			g.background((float)red, (float)green, (float)blue);
			break;
		case Color:
			g.background(c);
			break;
		case ColorSet:
			g.background(cset);
			break;
		}
	}
	
	
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
    }
    
}
