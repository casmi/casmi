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

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.font.Font;

import com.sun.opengl.util.j2d.TextRenderer;

/**
 * Text class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Text extends Element implements Renderable {

    private int x;
    private int y;
    private int z;
    private double tmpx=0,tmpy=0;
    private Font font;
    private String string;
    private String[] strAry;
    private FontRenderContext frc;
    private TextLayout[] layout;
    private TextRenderer tr;
    public enum TextAlign {RIGHT,CENTER,LEFT};
    private TextAlign modeX = TextAlign.LEFT;
    private double leading;
    
    /**
     * Creates a new Text object using letters to be displayed, x,y-coordinate and Font.
     * 
     * @param string
     *           The letters to be displayed.      
     * @param i
     *           The x-coordinate of text.      
     * @param j
     *           The y-coordinate of text.
     * @param f
     *           The font of text.                          
     */
    public Text(String string, int i, int j,Font f) {
        this.x = i;
        this.y = j;
        this.z = 0;
        font = f;
        this.string = string;
        strAry = this.string.split("\n");
        tr = new TextRenderer(font.getAWTFont(), true, true); 
        frc = new FontRenderContext(new AffineTransform(), false, false);
        layout = new TextLayout[strAry.length];
        for(int num = 0; num<strAry.length; num++){
            layout[num] = new TextLayout(this.strAry[num], font.getAWTFont(), frc);
        }
        this.leading = font.getSize()+2;            
        
    }
    
    /**
     * Creates a new Text object using letters to be displayed, x,y,z-coordinate and Font.
     * 
     * @param string
     *           The letters to be displayed.      
     * @param i
     *           The x-coordinate of text.      
     * @param j
     *           The y-coordinate of text.      
     * @param k
     *           The z-coordinate of text.
     * @param f
     *           The font of text.                          
     */
    public Text(String string, int i, int j, int k, Font f) {
        this.x = i;
        this.y = j;
        this.z = k;
        font = f;
        this.string = string;
        strAry = this.string.split("\n");
        tr = new TextRenderer(font.getAWTFont(), true, true); 
        frc = new FontRenderContext(new AffineTransform(), false, false);
        layout = new TextLayout[strAry.length];
        for(int num = 0; num<strAry.length; num++){
            layout[num] = new TextLayout(this.strAry[num], font.getAWTFont(), frc);
        }
    }   

    
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
        if(this.strokeColor.getA()!=1)
            gl.glDisable(GL.GL_DEPTH_TEST);
        
            tr.begin3DRendering();
            if(this.stroke == true){
                this.strokeColor.calcColor();
                tr.setColor(this.strokeColor.getNormalR(), this.strokeColor.getNormalG(), this.strokeColor.getNormalB(), this.strokeColor.getNormalA());}
            else if(this.fill == true){
                this.fillColor.calcColor();
                tr.setColor(this.fillColor.getNormalR(), this.fillColor.getNormalG(), this.fillColor.getNormalB(), this.fillColor.getNormalA());
            }
            tmpx = x; 
            tmpy = y;
            for(int i = 0; i<strAry.length; i++){
                switch(modeX){
                default:
                case LEFT:
                    break;
                case CENTER:
                    tmpx = x - textWidth(i)/2.0;
                    break;
                case RIGHT:
                    tmpx = x - textWidth(i);
                }
                     tr.draw3D(strAry[i], (int)tmpx, (int)(tmpy-leading*i), z, 1.0f);
            }
                tr.end3DRendering();
        
        if(this.strokeColor.getA()!=1)
            gl.glEnable(GL.GL_DEPTH_TEST);
    }

    /**
     * Returns descent of the current font at its current size and line.
     *
     * @param i
     *           The number of lines.                          
     * @return 
     *           The descent of text.    
     */
    public double textDescent(int i) {
        if(layout[i]!=null)
            return layout[i].getDescent();
        else 
            return 0;
    }

    /**
     * Returns ascent of the current font at its current size and line.
     * 
     * @param i
     *           The number of lines.                          
     * @return 
     *           The ascent of text.    
     */
    public double textAscent(int i) {
        if(layout[i]!=null)
            return layout[i].getAscent();
        else 
            return 0;
    }
    
    /**
     * Returns descent of the current font at its current size.
     *                           
     * @return 
     *           The descent of text.    
     */
    public double textDescent() {
        return textDescent(0);
    }

    
    /**
     * Returns ascent of the current font at its current size.
     *                           
     * @return 
     *           The ascent of text.    
     */
    public double textAscent() {
        return textAscent(0);
    }
    
    /**
     * Returns letter's width of the current font at its current size.
     *                           
     * @return 
     *           The letter's width.    
     */
    public double textWidth() {
        return textWidth(0);
    }
    
    
    /**
     * Returns letter's height of the current font at its current size.
     *                           
     * @return 
     *           The letter's height.    
     */
    public double textHeight() {
        return textHeight(0);
    }
    
    /**
     * Returns letter's width of the current font at its current size and line.
     *
     * @param i
     *           The number of lines.                                                     
     * @return 
     *           The letter's width.    
     */
    public double textWidth(int i) {
        return tr.getBounds(strAry[i]).getWidth();
    }
    
    /**
     * Returns letter's height of the current font at its current size and line.
     *
     * @param i
     *           The number of lines.                                                     
     * @return 
     *           The letter's height.    
     */
    public double textHeight(int i) {
        return tr.getBounds(strAry[i]).getHeight();
    }
    
    /**
     * Returns the TextLayout of this Text.
     * 
     * @return
     *           The TextLayout of this Text.
     */
    public TextLayout getLayout(){
        return layout[0];
    }
    
    /**
     * Returns the TextLayout of the i line.
     *
     * @param i
     *           The number of lines. 
     * @return
     *           The TextLayout of the i line.
     */
    public TextLayout getLayout(int i){
        return layout[i];
    }
    
    /**
     * Sets the current alignment for drawing text. 
     * The parameters LEFT, CENTER, and RIGHT set 
     * the display characteristics of the letters 
     * in relation to the values for the x and y 
     * parameters of the text() function. 
     * 
     * @param modex
     *           Either LEFT, CENTER or LIGHT.
     */
    public void textAlign(TextAlign modex){
        this.modeX = modex;       
    }

    public void textLeading(double leading) {
        this.leading = leading;
    }
    
    /**
     * Returns the leading of this letters.
     * 
     * @return
     *          The leading of this Text.
     */
    public double getLeading() {
        return this.leading;
    }
    
    /**
     * Returns the letters of this Text.
     * 
     * @return
     *          The letters of this Text.
     */
    public String getText() {
        return getText(0);
    }
    
    /**
     * Returns the letters of the i line.
     *
     * @param i
     *           The number of lines.                                                 
     * @return
     *           The letters to be displayed.
     */
    public String getText(int i) {
        return this.strAry[i];
    }
    
    /**
     * Sets the letters of this Text.
     * 
     * @param s
     *          The letters to be displayed.
     */
    public void setText(String s){
        setText(0,s);
    }
    
    /**
     * Sets the letters of the i line.
     *
     * @param i
     *           The number of lines.                                                 
     * @param s
     *           The letters to be displayed.
     */
    public void setText(int i, String s){
        this.strAry[i] = s;
    }
    
    /**
     * Returns the TextRenderer of this Text.
     * 
     * @return
     *           The TextRenderer of this Text.
     */
    public TextRenderer getRenderer(){
        return tr;
    }
    
}
