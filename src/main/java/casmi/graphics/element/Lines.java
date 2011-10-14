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

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.matrix.Vertex;

/**
 * Lines class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Lines extends Element implements Renderable {

    public static final int LINES         =1;
    public static final int LINES_3D      =3;
    public static final int LINE_LOOP = 51;
    
    private ArrayList<Double> x;
    private ArrayList<Double> y;
    private ArrayList<Double> z;
    
    private double X = 0;
    private double Y = 0;
    
    private Vertex tmpv = new Vertex(0,0,0);
    
    public enum JoinMode {
        CORNER, CORNERS, RADIUS, CENTER
    };

    private int MODE;

    public Lines() {
        x= new ArrayList<Double>();
        y= new ArrayList<Double>();
        z= new ArrayList<Double>();
    }
    
    public void vertex(float x, float y){
        MODE = LINES;
        this.x.add((double)x);
        this.y.add((double)y);
    }
    
    public void vertex(float x, float y,float z){
        MODE = LINES_3D;
        this.x.add((double)x);
        this.y.add((double)y);
        this.z.add((double)z);
    }
    
    public void vertex(double x, double y){
        MODE = LINES;
        this.x.add(x);
        this.y.add(y);
    }
    
    public void vertex(double x, double y,double z){
        MODE = LINES_3D;
        this.x.add(x);
        this.y.add(y);
        this.z.add(z);
    }
    
    public void vertex(Vertex v){
        MODE = LINES_3D;
        this.x.add(v.x);
        this.y.add(v.y);
        this.z.add(v.z);
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


    @Override
    public void render(GL gl, GLU glu, int width, int height) {

    	calcG();
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glDisable(GL.GL_DEPTH_TEST);
        
        getSceneStrokeColor().setup(gl);
        double tmpx,tmpy,tmpz;

        gl.glPushMatrix();
        gl.glTranslated(X, Y, 0);
        gl.glRotated(rotate, 0, 0, 1.0);
        this.setTweenParameter(gl);
        
        switch (MODE) {
        case LINES:
            gl.glLineWidth(this.strokeWidth);
            gl.glBegin(GL.GL_LINE_STRIP);
            for(int i = 0;i<x.size();i++){
                tmpx = (Double)this.x.get(i);
                tmpy = (Double)this.y.get(i);
            gl.glVertex2d(tmpx-X, tmpy-Y);
            }
            gl.glEnd();
            break;
        case LINES_3D:
            gl.glLineWidth(this.strokeWidth);
            gl.glBegin(GL.GL_LINE_STRIP);
            for(int i = 0;i<x.size();i++){
                tmpx = (Double)this.x.get(i);
                tmpy = (Double)this.y.get(i);
                tmpz = (Double)this.z.get(i);
            gl.glVertex3d(tmpx-X, tmpy-Y, tmpz);
            }
            gl.glEnd();
            break;
      
        default:
            break;
        }
        
        gl.glPopMatrix();
        
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glEnable(GL.GL_DEPTH_TEST);
    }
    
    private void calcG(){
    	X=Y=0;
    	for(int i=0;i<x.size();i++){
    		X += x.get(i);
    		Y += y.get(i);}
    	X/=x.size();
    	Y/=y.size();
    }
    
    public double getX(){
    	return this.X;
    }
    
    public double getY(){
    	return this.Y;
    }
    
    public void setX(double x){
    	this.X = x;
    }
    
    public void setY(double y){
    	this.Y = y;
    }
    
    
    public void setXY(double x, double y){
    	this.X = x;
    	this.Y = y;
    }

}
