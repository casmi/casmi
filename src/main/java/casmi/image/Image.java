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

package casmi.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import casmi.graphics.Graphics;
import casmi.graphics.color.Color;
import casmi.graphics.color.RGBColor;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

/**
 * Image class.
 * Wrap javax.imageio.ImageIO and make it easy to use.
 * 
 * @author Y. Ban
 */
public class Image {
    
    private static final ImageMode DEFAULT_IMAGE_MODE = ImageMode.CENTER;
    
    protected final int width;
    protected final int height;
    
    protected Texture texture;
    protected ImageMode mode = DEFAULT_IMAGE_MODE;

    private BufferedImage img;
    private BufferedImage preimg;
    
    public Image(int width, int height) {
        this.width  = width;
        this.height = height;
        
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics.addTextureImage(this);
    }
    
    public Image(String path) {
        java.io.File imgLoc = new java.io.File(path);
        
        try {
           preimg = ImageIO.read(imgLoc);
           img =  convertBytetoInt(preimg);
        } catch (IOException e) {
           e.printStackTrace();
        }
        
        this.width  = img.getWidth();
        this.height = img.getHeight();
        
        Graphics.addTextureImage(this);
    }
    
    public Image(URL url) {
        try {
            preimg = ImageIO.read(url);
            img =  convertBytetoInt(preimg);
         } catch (IOException e) {
            e.printStackTrace();
         }

         this.width  = img.getWidth();
         this.height = img.getHeight();
         
         Graphics.addTextureImage(this);
    }
    
    public Image(BufferedImage image) {
         this.img = copyImage(image);
         this.width  = this.img.getWidth();
         this.height = this.img.getHeight();
         
         Graphics.addTextureImage(this);
    }
    
    public BufferedImage copyImage(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = (Graphics2D)newImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return newImage;
    }
    
    public void convertBytetoInt(BufferedImage src,BufferedImage dst){
    	dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
    	int[] pixels = ((DataBufferInt)(dst.getRaster().getDataBuffer())).getData();
    	byte[] binary = ((DataBufferByte)(src.getRaster().getDataBuffer())).getData();
    	int r,g,b,a;
    	int pixelsize = src.getWidth()*src.getHeight();
    	if(src.getType() == BufferedImage.TYPE_4BYTE_ABGR){
    		for(int i=0,j=0;i<pixelsize;i++) {
    			a = binary[j++]&0xff;
    			b = binary[j++]&0xff;
    			g = binary[j++]&0xff;
    			r = binary[j++]&0xff;
    			pixels[i] = (a<<24)|(r<<16)|(g<<8)|b;
    		}
    	}
    	if(src.getType() == BufferedImage.TYPE_3BYTE_BGR){
    		for(int i=0,j=0;i<pixelsize;i++) {
    			b = binary[j++]&0xff;
    			g = binary[j++]&0xff;
    			r = binary[j++]&0xff;	
    			pixels[i] =0xff000000|(r<<16)|(g<<8)|b;
    		}
    	}
    }
    
    public BufferedImage convertBytetoInt(BufferedImage src){
    	BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
    	int[] pixels = ((DataBufferInt)(dst.getRaster().getDataBuffer())).getData();
    	byte[] binary = ((DataBufferByte)(src.getRaster().getDataBuffer())).getData();
    	int r,g,b,a;
    	int pixelsize = src.getWidth()*src.getHeight();
    	if(src.getType() == BufferedImage.TYPE_4BYTE_ABGR){
    		for(int i=0,j=0;i<pixelsize;i++) {
    			a = binary[j++]&0xff;
    			b = binary[j++]&0xff;
    			g = binary[j++]&0xff;
    			r = binary[j++]&0xff;
    			pixels[i] = (a<<24)|(r<<16)|(g<<8)|b;
    		}
    	}
    	if(src.getType() == BufferedImage.TYPE_3BYTE_BGR){
    		for(int i=0,j=0;i<pixelsize;i++) {
    			b = binary[j++]&0xff;
    			g = binary[j++]&0xff;
    			r = binary[j++]&0xff;	
    			pixels[i] =0xff000000|(r<<16)|(g<<8)|b;
    		}
    	}
    	return dst;
    }
    public static Image clone(Image image){
    	Image cloneImage = new Image(image.img);
    	return cloneImage;
    }

    public final void loadTexture() {
        texture = TextureIO.newTexture(img, true);
        if(texture == null)
        	System.out.println("can not load texture!");
    }

    public final void unloadTexture() {
        if( texture != null ) {
            texture.dispose();
        }
    }
    
    public final void reloadTexture() {
        unloadTexture();
        loadTexture();
    }
    
    public void enableTexture() {
        if( texture != null ) {
            texture.enable();
            texture.bind();	 
        }
    }
    
    public void disableTexture() {
        if( texture != null ) {
            texture.disable();
        }
    }
    
    public void imageMode(ImageMode mode) {
        this.mode = mode;
    }
    
    public final Color getColor(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;
        
        return new RGBColor((pixels[idx] >> 16 & 0x000000ff) / 255.0, 
                            (pixels[idx] >> 8  & 0x000000ff) / 255.0,
                            (pixels[idx]       & 0x000000ff) / 255.0,
                            (pixels[idx] >> 24)              / 255.0);
    }
    
    public final double getR(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;
        
        return (pixels[idx] >> 16 & 0x000000ff); 
    }
    
    public final double getG(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;
        
        return (pixels[idx] >> 8  & 0x000000ff);
    }
    
    public final double getB(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;
        
        return (pixels[idx]       & 0x000000ff);
    }
    
    public final double getGray(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;
        
        return ((pixels[idx]       & 0x000000ff)+(pixels[idx] >> 8  & 0x000000ff)+(pixels[idx] >> 16 & 0x000000ff))/3;
    }
    
    public final double getA(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;
        
        return (pixels[idx] >> 24);
    }
    
    public final void setColor(Color color, int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int red   = (int)(color.getRed()   * 255.0);
        int green = (int)(color.getGreen() * 255.0);
        int blue  = (int)(color.getBlue()  * 255.0);
        int alpha = (int)(color.getAlpha() * 255.0);
        pixels[x + y * width] = alpha << 24 | 
                                red   << 16 |
                                green << 8  |
                                blue;
    }
    
    public final void setA(double alpha, int x, int y){
    	int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;
    	int tmpR = pixels[idx] >> 16 & 0x000000ff;
        int tmpG = pixels[idx] >> 8  & 0x000000ff;
        int tmpB = pixels[idx]       & 0x000000ff;
        pixels[idx] = (int)alpha << 24 | 
        				    tmpR << 16 |
        			        tmpG << 8  |
        			        tmpB;
    }
    
    public final void setColors(Color[] colors) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int idx = x + y * width;
                if (colors.length <= idx) break;
                int red   = (int)(colors[idx].getRed()   * 255.0);
                int green = (int)(colors[idx].getGreen() * 255.0);
                int blue  = (int)(colors[idx].getBlue()  * 255.0);
                int alpha = (int)(colors[idx].getAlpha() * 255.0);
                pixels[idx] = alpha << 24 | 
                              red   << 16 |
                              green << 8  |
                              blue;
            }
        }
    }

    public final Texture getTexture() {
        return texture;
    }

    public final ImageMode getMode() {
        return mode;
    }

    public final void setMode(ImageMode mode) {
        this.mode = mode;
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }
    
//    public BufferedImage getIm(){
//    	return img;
//    }
}
