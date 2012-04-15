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
 * @author Y. Ban, T. Takeuchi
 * 
 */
public class Text extends Element implements Renderable {

    private Font font;
    private String str;
    private String[] strArray;
    private FontRenderContext frc;
    private TextLayout[] layout;
    private TextRenderer textRenderer;
    private TextAlign align = TextAlign.LEFT;
    private double leading = 0.0;
    
    private boolean selection = false;
    
    /**
     * Creates a new Text object.
     */
    public Text() {
        this(null);
    }
    
    /**
     * Creates a new Text object using letters to be displayed.
     * 
     * @param text
     *           The letters to be displayed.      
     */
    public Text(String text) {
        this(text, new Font());
    }
    
    /**
     * Creates a new Text object using letters to be displayed, and Font.
     * 
     * @param text
     *           The letters to be displayed.      
     * @param font
     *           The font of text.                          
     */
    public Text(String text, Font font) {
        this(text, font, 0, 0, 0);
    } 
    
    /**
     * Creates a new Text object using letters to be displayed, x,y-coordinate.
     * 
     * @param text
     *           The letters to be displayed.      
     * @param x
     *           The x-coordinate of text.      
     * @param y
     *           The y-coordinate of text.
     */
    public Text(String text, double x, double y) {
        this(text, new Font(), x, y);
    }
    
    /**
     * Creates a new Text object using letters to be displayed, x,y-coordinate and Font.
     * 
     * @param text
     *           The letters to be displayed. 
     * @param font
     *           The font of text.      
     * @param x
     *           The x-coordinate of text.      
     * @param y
     *           The y-coordinate of text.
     */
    public Text(String text, Font font, double x, double y) {
        this(text, font, x, y, 0);
    }
    
    /**
     * Creates a new Text object using letters to be displayed, x,y,z-coordinate.
     * 
     * @param text
     *           The letters to be displayed.      
     * @param x
     *           The x-coordinate of text.      
     * @param y
     *           The y-coordinate of text.      
     * @param z
     *           The z-coordinate of text.
     */
    public Text(String text, double x, double y, double z) {
        this(text, new Font(), x, y, z);
    }
    
    /**
     * Creates a new Text object using letters to be displayed, x,y,z-coordinate and Font.
     * 
     * @param text
     *           The letters to be displayed.      
     * @param font
     *           The font of text.
     * @param x
     *           The x-coordinate of text.      
     * @param y
     *           The y-coordinate of text.      
     * @param z
     *           The z-coordinate of text.
     */
    public Text(String text, Font font, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.font = font;
        if (text == null) {
            this.str = "";
        } else {
            this.str = text;
        }
        strArray = this.str.split("\n");
        leading = font.getSize() * 1.2;
        
        try{
            textRenderer = new TextRenderer(font.getAWTFont(), true, true);
            frc = new FontRenderContext(new AffineTransform(), false, false);
            layout = new TextLayout[strArray.length];
            for(int num = 0; num < strArray.length; num++) {
                layout[num] = new TextLayout(strArray[num], font.getAWTFont(), frc);
            }
        } catch(java.lang.IllegalArgumentException e) {
        	// ignore
        }
    }
    
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
    	if (this.fillColor.getAlpha() < 1.0 || this.strokeColor.getAlpha() < 1.0 || this.isDepthTest()==false)
    		gl.glDisable(GL.GL_DEPTH_TEST);
        
        gl.glPushMatrix();
        {
            if (!isSelection()) {
                this.setTextTweenParameter(gl);
                textRenderer.begin3DRendering();
                if (stroke) {
                    textRenderer.setColor((float)getSceneStrokeColor().getRed(),
                                          (float)getSceneStrokeColor().getGreen(),
                                          (float)getSceneStrokeColor().getBlue(),
                                          (float)getSceneStrokeColor().getAlpha());
                } else if (fill) {
                    textRenderer.setColor((float)getSceneFillColor().getRed(),
                                          (float)getSceneFillColor().getGreen(),
                                          (float)getSceneFillColor().getBlue(),
                                          (float)getSceneFillColor().getAlpha());
                }
                double tmpX = 0;
                double tmpY = 0;
                for (int i = 0; i < strArray.length; i++) {
                    switch (align) {
                    default:
                    case LEFT:
                        break;
                    case CENTER:
                        tmpX = -getWidth(i) / 2.0;
                        break;
                    case RIGHT:
                        tmpX = -getWidth(i);
                    }
                    textRenderer.draw3D(strArray[i], (int)tmpX, (int)(tmpY - leading * i), (int)z, 1.0f);

                }
                textRenderer.end3DRendering();
            } else {
                this.setTextTweenParameter(gl);
                double tmpX = 0;
                double tmpY = 0;

                for (int i = 0; i < strArray.length; i++) {
                    switch (align) {
                    default:
                    case LEFT:
                        break;
                    case CENTER:
                        tmpX = -getWidth(i) / 2.0;
                        break;
                    case RIGHT:
                        tmpX = -getWidth(i);
                    }

                    gl.glBegin(GL.GL_QUADS);
                    {
                        gl.glVertex2d(tmpX, (tmpY - leading * i) - getDescent(i));
                        gl.glVertex2d(tmpX, (tmpY - leading * i) + getAscent(i));
                        gl.glVertex2d(tmpX + getWidth(i), (tmpY - leading * i) + getAscent(i));
                        gl.glVertex2d(tmpX + getWidth(i), (tmpY - leading * i) - getDescent(i));
                    }
                    gl.glEnd();
                }
            }
        }
        gl.glPopMatrix();
        if (this.fillColor.getAlpha() < 1.0 || this.strokeColor.getAlpha() < 1.0 || this.isDepthTest()==false)
        	gl.glEnable(GL.GL_DEPTH_TEST);
    }

    /**
     * Returns descent of the current font at its current size and line.
     *
     * @param line
     *           The number of lines.                          
     * @return 
     *           The descent of text.    
     */
    public double getDescent(int line) {
        if (layout[line] == null) return 0;
        return layout[line].getDescent();
    }

    /**
     * Returns ascent of the current font at its current size and line.
     * 
     * @param line
     *           The number of lines.                          
     * @return 
     *           The ascent of text.    
     */
    public double getAscent(int line) {
        if (layout[line] == null) return 0;
        return layout[line].getAscent();
    }
    
    /**
     * Returns descent of the current font at its current size.
     *                           
     * @return 
     *           The descent of text.    
     */
    public double getDescent() {
        return getDescent(0);
    }
    
    /**
     * Returns ascent of the current font at its current size.
     *                           
     * @return 
     *           The ascent of text.    
     */
    public double getAscent() {
        return getAscent(0);
    }
    
    /**
     * Returns letter's width of the current font at its current size.
     *                           
     * @return 
     *           The letter's width.    
     */
    public double getWidth() {
        return getWidth(0);
    }
    
    
    /**
     * Returns letter's height of the current font at its current size.
     *                           
     * @return 
     *           The letter's height.    
     */
    public double getHeight() {
        return getHeight(0);
    }
    
    /**
     * Returns letter's width of the current font at its current size and line.
     *
     * @param line
     *           The number of lines.                                                     
     * @return 
     *           The letter's width.    
     */
    public double getWidth(int line) {
        if (strArray.length == 0) return 0.0;
        return textRenderer.getBounds(strArray[line]).getWidth();
    }
    
    /**
     * Returns letter's height of the current font at its current size and line.
     *
     * @param line
     *           The number of lines.                                                     
     * @return 
     *           The letter's height.    
     */
    public double getHeight(int line) {
        if (strArray.length == 0) return 0.0;
        return textRenderer.getBounds(strArray[line]).getHeight();
    }
    
    /**
     * Returns the TextLayout of this Text.
     * 
     * @return
     *           The TextLayout of this Text.
     */
    public TextLayout getLayout() {
        return layout[0];
    }
    
    /**
     * Returns the TextLayout of the i line.
     *
     * @param line
     *           The number of lines. 
     * @return
     *           The TextLayout of the i line.
     */
    public TextLayout getLayout(int line) {
        return layout[line];
    }
    
    /**
     * Returns the current alignment for drawing text. 
     * The parameters LEFT, CENTER, and RIGHT set the display characteristics of
     * the letters in relation to the values for the x and y parameters of the 
     * text() function. 
     * 
     * @return
     *        The TextAlign of the text.   
     */
    public TextAlign getAlign() {
        
        return align;
    }
    
    /**
     * Sets the current alignment for drawing text. 
     * The parameters LEFT, CENTER, and RIGHT set 
     * the display characteristics of the letters 
     * in relation to the values for the x and y 
     * parameters of the text() function. 
     * 
     * @param align
     *           Either LEFT, CENTER or LIGHT.
     */
    public void setAlign(TextAlign align){
        this.align = align;       
    }

    public void setLeading(double leading) {
        this.leading = leading;
    }
    
    /**
     * Returns the leading of this letters.
     * 
     * @return
     *          The leading of this Text.
     */
    public double getLeading() {
        return leading;
    }
    
    public int getLine() {
        return strArray.length;
    }
    
    /**
     * Returns the letters of this Text.
     * 
     * @return
     *          The letters of this Text.
     */
    public String getText() {
        return str;
    }
    
    /**
     * Sets the letters of this Text.
     * 
     * @param str
     *          The letters to be displayed.
     */
    public void setText(String str) {
        this.str = str;
        setArrayText(str);
    }
    
    protected String[] getArrayText() {
        return strArray; 
    }
    
    protected void setArrayText(String str) {
        strArray = null;
        this.str = str;
        strArray = this.str.split("\n");
    }
    
    /**
     * Returns the TextRenderer of this Text.
     * 
     * @return
     *           The TextRenderer of this Text.
     */
    public TextRenderer getRenderer() {
        return textRenderer;
    }
        
    public Font getFont() {
        return font;
    }
    
    public void setFont(Font font) {
        this.font = font;
        textRenderer = new TextRenderer(font.getAWTFont(), true, true);
    }

	public boolean isSelection() {
		return selection;
	}

	public void setSelection(boolean selection) {
		this.selection = selection;
	}
}
