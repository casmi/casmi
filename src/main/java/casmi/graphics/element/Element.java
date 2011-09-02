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

import casmi.graphics.color.Color;
import casmi.graphics.color.ColorMode;
import casmi.graphics.material.Material;

/**
 * Element class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
abstract public class Element implements Renderable {

    private int sR = 0;
    private int sG = 0;
    private int sB = 0;
    private int sA = 255;
    private int fR = 255;
    private int fG = 255;
    private int fB = 255;
    private int fA = 255;

    protected Color strokeColor = new Color(sR, sG, sB, sA);
    protected Color fillColor = new Color(fR, fG, fB, fA);
    protected Material material = new Material(); 

    protected boolean stroke = true;
    protected boolean fill = true;

    protected float strokeWidth = 1;

    /**
     * Returns the width of this Element's stroke.
     * 
     * @return strokeWidth
     *              The width of the Element's stroke.
     */
    public float getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * Sets the width of this Element's stroke.
     * 
     * @param strokeWidth
     *              The width of the Element's stroke.
     */
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
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
    


}
