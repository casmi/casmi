package casmi.graphics.object;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;


import casmi.graphics.Graphics;
import casmi.graphics.element.Element;
/**
 * Arc class. Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 */
public class Mask extends Element implements ObjectRender{
	private List<Element> elements;
	private BufferedImage maskBuff;
	public Mask(){
		elements = new ArrayList<Element>();
	}
	
	/**
     * Add a Graphics Element for stencil mask.
     * 
     * @param element
     * 		Graphics Element for mask.
     */
	public void add(Element element){
		elements.add(element);
	}
	
	public void add(Element element, int index){
		elements.add(index, element);
	}
	
	public void remove(int index){
		elements.remove(index);
	}
	
	public void remove(Element element){
		elements.remove(element);
	}
	
	public void clear(){
		elements.clear();
	}
	
	public void drawingMask(Graphics g){
		for(Element e : elements)
			render(e,g);
	}
	
	public void render(Element el,Graphics g){
		if(el.isvisible()==true){
		g.pushMatrix();
		if(el.isTween() == true){
			el.settAF((int)((int)el.gettAF()*this.getSceneFillColor().getA()/255.0));
			el.settAS((int)((int)el.gettAS()*this.getSceneStrokeColor().getA()/255.0));
			
        g.render(el);
        el.settAF((int)el.gettAF());
        el.settAS((int)el.gettAS());
        } else {
    		el.getFillColor().setA((int)(el.getFillColor().getA()*this.getSceneFillColor().getA()/255.0));
            el.getStrokeColor().setA((int)(el.getStrokeColor().getA()*this.getSceneStrokeColor().getA()/255.0));
            g.render(el);
            el.getFillColor().setA(el.getFillColor().getA());
            el.getStrokeColor().setA(el.getStrokeColor().getA());
        }
        g.popMatrix();
		}
	}

	@Override
	public void render(GL gl, GLU glu, int width, int height) {
		
	}

	@Override
	public void render(Graphics g) {
		GL gl = g.getGL();
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_STENCIL_TEST);
		gl.glStencilFunc(GL.GL_ALWAYS, 1, ~0);
		gl.glStencilOp(GL.GL_KEEP, GL.GL_REPLACE, GL.GL_REPLACE);
		gl.glColorMask(false, false, false, false);
		gl.glDepthMask(false);
		drawingMask(g);
		gl.glColorMask(true, true, true, true);
		gl.glDepthMask(true);
		gl.glStencilOp(GL.GL_KEEP,GL.GL_KEEP,GL.GL_KEEP);
		gl.glStencilFunc( GL.GL_EQUAL, 1, ~0);
	
	}

	public BufferedImage getMaskBuff() {
		return maskBuff;
	}

	public void setMaskBuff(BufferedImage maskBuff) {
		this.maskBuff = maskBuff;
	}
	
	
	
}
