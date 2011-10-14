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

import javax.media.opengl.GL;

import casmi.graphics.color.Color;
import casmi.graphics.color.ColorMode;
import casmi.graphics.color.ColorSet;
import casmi.graphics.material.Material;

/**
 * Element class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
abstract public class Element implements Renderable{

    private int sR = 0;
    private int sG = 0;
    private int sB = 0;
    private int sA = 255;
    private int fR = 255;
    private int fG = 255;
    private int fB = 255;
    private int fA = 255;
    private double sceneA = 1.0;
    
    protected double rotate=0;    
    protected float tX=0,tY=0,tZ=0;
    protected double tAS,tAF;
    protected float tRX=0,tRY=0,tRZ=0;
    protected double tSX=1.0,tSY=1.0,tSZ=1.0;

    protected Color strokeColor = new Color(sR, sG, sB, sA);
    protected Color fillColor = new Color(fR, fG, fB, fA);
    protected Color scenestrokeColor = new Color(sR, sG, sB, (int)(sA*sceneA));
    protected Color scenefillColor = new Color(fR, fG, fB, (int)(fA*sceneA));
    protected Material material = new Material(); 

    protected boolean stroke = true;
    protected boolean fill = true;
    protected boolean tween = false;

    protected float strokeWidth = 1;

    /**
     * Returns the width of this Element's stroke.
     * 
     * @return strokeWidth
     *              The width of the Element's stroke.
     */
    public double getStrokeWidth() {   	
        return (double)strokeWidth;
    }

    /**
     * Sets the width of this Element's stroke.
     * 
     * @param strokeWidth
     *              The width of the Element's stroke.
     */
    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = (float)strokeWidth;
    }

    /**
     * Returns the color of this Element's stroke.
     * 
     * @return strokeColor
     *              The color of the Element's stroke.
     */
    public Color getStrokeColor() {
        return strokeColor;
    }

    /**
     * Sets the color of this Element's stroke.
     * 
     * @param strokeColor
     *              The color of the Element's stroke.
     */
    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }
    
    /**
     * Sets the color of this Element's stroke.
     * 
     * @param strokeColor
     *              The color of the Element's stroke.
     */
    public void setStrokeColor(ColorSet strokeColor) {
        this.strokeColor = Color.color(strokeColor);
    }
    
    public void setStrokeColor(ColorSet strokeColor ,int alpha) {
        this.strokeColor = Color.color(strokeColor);
        this.strokeColor.setA(alpha);
    }

    /**
     * Returns the color of this Element's fill.
     * 
     * @return fillColor
     *              The color of the Element's fill.
     */
    public Color getFillColor() {
        return fillColor;
    }
 
    /**
     * Sets the color of this Element's fill.
     * 
     * @param fillColor
     *              The color of the Element's fill.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
    
    /**
     * Sets the color of this Element's fill.
     * 
     * @param fillColor
     *              The color of the Element's fill.
     */
    public void setFillColor(ColorSet fillColor) {
        this.fillColor = Color.color(fillColor);
    }

    public boolean isStroke() {
        return stroke;
    }

    /**
     * Enables or disables drawing the stroke (outline)
     * 
     * @param stroke
     */
    public void setStroke(boolean stroke) {
        this.stroke = stroke;
    }
    
    

    public boolean isFill() {
        return fill;
    }

    /**
     * Enables or disables filling geometry
     * 
     * @param fill
     */
    public void setFill(boolean fill) {
        this.fill = fill;
    }
    
    public void colorMode(ColorMode c){
        this.fillColor.colorMode(c);
        this.strokeColor.colorMode(c);
    }
    
    /**
     * Sets Material to this Element
     * 
     * @param m
     *              The Material of this Element.            
     */
    public void setMaterial(Material m){
        this.material = m;
    }
    
    public void setSceneAlpha(double alpha){
    	this.strokeColor.setA((int)alpha);
    	this.fillColor.setA((int)alpha); 
    }
    
    public void setAlpha(double alpha){
    	this.sceneA = alpha;
    }
    
    public Color getSceneStrokeColor(){
    	Color.copyColor(this.strokeColor, this.scenestrokeColor);
    	if(tween == true){
    		this.scenestrokeColor.setA((int)(tAS*sceneA));}
    	else
    		this.scenestrokeColor.setA((int)(this.strokeColor.getA()*sceneA));
    	return this.scenestrokeColor;
    }
    
    public Color getSceneFillColor(){
    	Color.copyColor(this.fillColor, this.scenefillColor);
    	if(tween == true){
    		this.scenefillColor.setA((int)(tAF*sceneA));
    		
    	}
    	else
    		this.scenefillColor.setA((int)(this.fillColor.getA()*sceneA));
		return this.scenefillColor;
    }
    
    public void setEndTweenXYZ(float x,float y, float z){
    	tX = x;
    	tY = y;
    	tZ = z;
    }
    
    public void setEndTweenScale(double sx,double sy,double sz){
    	tSX = sx;
    	tSY = sy;
    	tSZ = sz;
    }
    
    public void setEndTweenA(double as, double af){
    	tAS = as;
    	tAF = af;
    }
    
    public void setEndTweenRotate(float rx,float ry, float rz){
    	tRX = rx;
    	tRY = ry;
    	tRZ = rz;
    }
    
    protected void setTweenParameter(GL gl){
    	if(tween==true){
    	gl.glTranslated(tX, tY, tZ);
    	gl.glScaled(tSX, tSY, tSZ);
    	gl.glRotated(tRZ, 0.0, 0.0, 1.0);
    	gl.glRotated(tRX, 1.0, 0.0, 1.0);
    	gl.glRotated(tRY, 0.0, 1.0, 0.0);
    	  
    	}
    }
    
    protected void setTextTweenParameter(GL gl){
    	if(tween==true){
    	gl.glTranslated(tX, tY, tZ);
    	gl.glScaled(tSX, tSY, tSZ);
    	}
    }
    
    public double[] setTween(){
    	double A[] = new double[2];
    	A[0] = this.strokeColor.getA();
    	A[1] = this.fillColor.getA();
    	tween = true;
    	return A;
    }



}
