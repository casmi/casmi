/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 *  casmi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
  
package casmi.graphics.element;

import java.net.URL;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.image.Image;
import casmi.matrix.Vertex;

/**
 * Texture class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Texture extends Element implements Renderable {

    public static final int LINES         =1;
    public static final int LINES_3D      =3;
    public static final int LINE_LOOP = 51;
    
    private ArrayList<Double> x;
    private ArrayList<Double> y;
    private ArrayList<Double> z;
    private ArrayList<Float> nx;
    private ArrayList<Float> ny;

    private Vertex tmpv = new Vertex(0,0,0);
   
    private int MODE;
    
    private Image image;
    

    public double Width,Height;
    private static boolean reloadFlag = false;
    
    private double X;
    private double Y;

    public Texture(String s) {
        image = new Image(s);
        Width = image.width;
        Height = image.height;
        x= new ArrayList<Double>();
        y= new ArrayList<Double>();
        z= new ArrayList<Double>();
        nx=new ArrayList<Float>();
        ny=new ArrayList<Float>();
        
        reloadFlag = true;
    }
    
    public Texture(URL url) {
        image = new Image(url);
        Width = image.width;
        Height = image.height;

        
        x= new ArrayList<Double>();
        y= new ArrayList<Double>();
        z= new ArrayList<Double>();
        nx=new ArrayList<Float>();
        ny=new ArrayList<Float>();
        
        reloadFlag = true;
    }
    
    public void copyTexture(Texture src,Texture dst){
    	
    }
    
    public void vertex(float x, float y,float nx,float ny){
        MODE = LINES;
        this.x.add((double)x);
        this.y.add((double)y);
        this.nx.add(nx);
        this.ny.add(ny);
    }
    
    public void vertex(float x, float y,float z, float nx,float ny){
        MODE = LINES_3D;
        this.x.add((double)x);
        this.y.add((double)y);
        this.z.add((double)z);
        this.nx.add(nx);
        this.ny.add(ny);
    }
    
    public void vertex(double x, double y, double nx,double ny){
        MODE = LINES;
        this.x.add(x);
        this.y.add(y);
        this.nx.add((float)nx);
        this.ny.add((float)ny);
    }
    
    public void vertex(double x, double y, double z, double nx,double ny){
        MODE = LINES_3D;
        this.x.add(x);
        this.y.add(y);
        this.z.add(z);
        this.nx.add((float)nx);
        this.ny.add((float)ny);
    }
    
    public void vertex(Vertex v, float nx, float ny){
        MODE = LINES_3D;
        this.x.add(v.x);
        this.y.add(v.y);
        this.z.add(v.z);
        this.nx.add(nx);
        this.ny.add(ny);
    }
    
    public Vertex getVertex(int i){
        tmpv.x = x.get(i);
        tmpv.y = y.get(i);
        tmpv.z = z.get(i);
        return tmpv;
    }
    
    public void removeVertex(int i){
        this.x.remove(i);
        this.y.remove(i);
        this.z.remove(i);
    }
    
    public void setVertex(int i, double x, double y){
        this.x.set(i, x);
        this.y.set(i, y);
        this.z.set(i, 0d);
    }
    
    public void setVertex(int i, double x, double y, double z){
        this.x.set(i, x);
        this.y.set(i, y);
        this.z.set(i, z);
    }
    
    public Image getImage(){
        return this.image;
    }
    
    public void enableTexture(){
    	this.image.enableTexture();
    }
    
    public void disableTexture(){
    	this.image.disableTexture();
    }


    @Override
    public void render(GL gl, GLU glu, int width, int height) {
 

        if (reloadFlag) {
            Graphics.reloadTextures();
            reloadFlag = false;
        }
        gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glPushMatrix();
        this.setTweenParameter(gl);
        getSceneFillColor().setup(gl);
        double tmpx,tmpy,tmpz;
        float tmpnx,tmpny;
        image.enableTexture();
        if(x.size()<1){
        	Image img = getImage();
        	gl.glBegin(GL.GL_QUADS);
			switch (img.mode) {
			default:
			case CORNER:
				gl.glTexCoord2f(0.0f, 1.0f);
				gl.glVertex2f((float) X+tX, (float) (Y+tY - Height*tSY));
				gl.glTexCoord2f(0.0f, 0.0f);
				gl.glVertex2f((float) X+tX, (float) Y+tY);
				gl.glTexCoord2f(1.0f, 0.0f);
				gl.glVertex2f((float) (X+tX + Width*tSX ), (float) Y+tY);
				gl.glTexCoord2f(1.0f, 1.0f);
				gl.glVertex2f((float) (X+tX + Width*tSX ), (float) (Y+tY - Height*tSY));
				break;
			case CENTER:
				gl.glTexCoord2f(0.0f, 1.0f);
				gl.glVertex2f((float) (X+tX - Width*tSX / 2.0), (float) (Y+tY - Height*tSY / 2.0));
				gl.glTexCoord2f(0.0f, 0.0f);
				gl.glVertex2f((float) (X+tX - Width*tSX / 2.0), (float) (Y+tY + Height*tSY / 2.0));
				gl.glTexCoord2f(1.0f, 0.0f);
				gl.glVertex2f((float) (X+tX + Width*tSX / 2.0), (float) (Y+tY + Height*tSY / 2.0));
				gl.glTexCoord2f(1.0f, 1.0f);
				gl.glVertex2f((float) (X+tX + Width*tSX / 2.0), (float) (Y+tY - Height*tSY / 2.0));
				break;
			}
			gl.glEnd();
        }else{
        switch (MODE) {
        case LINES:
                gl.glBegin(GL.GL_POLYGON);
                for(int i = 0;i<x.size();i++){
                    tmpx = (Double)this.x.get(i);
                    tmpy = (Double)this.y.get(i);
                    tmpnx= (Float)this.nx.get(i);
                    tmpny= (Float)this.ny.get(i);
                    gl.glTexCoord2f(tmpnx, tmpny);
                    gl.glVertex2d(tmpx, tmpy);
                }
                gl.glEnd();    
                break;
        case LINES_3D:
                gl.glBegin(GL.GL_POLYGON);
                for(int i = 0;i<x.size();i++){
                    tmpx = (Double)this.x.get(i);
                    tmpy = (Double)this.y.get(i);
                    tmpz = (Double)this.z.get(i);
                    tmpnx= (Float)this.nx.get(i);
                    tmpny= (Float)this.ny.get(i);
                gl.glVertex3d(tmpx, tmpy, tmpz);
                gl.glTexCoord2f(tmpnx, tmpny);
                gl.glVertex3d(tmpx, tmpy, tmpz);
                }
                gl.glEnd();
        default:
            break;
        }   
        }
        image.disableTexture();
        gl.glPopMatrix();
        gl.glEnable(GL.GL_DEPTH_TEST);
    }
   

	public double getX() {
		return X;
	}

	public void setX(double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}
	
	public double getWidth(){
		return Width;
	}
	
	public double getHeight(){
		return Height;
	}
	
	public void setWidth(double width){
		this.Width = width;
	}
	
	public void setHeight(double height){
		this.Height = height;
	}
	
	public void set(double x, double y, double width, double height){
		this.X = x;
		this.Y = y;
		this.Width = width;
		this.Height = height;
		
	}
    
}
