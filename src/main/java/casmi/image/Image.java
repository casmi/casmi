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
import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import casmi.exception.CasmiRuntimeException;
import casmi.graphics.color.Color;
import casmi.graphics.color.RGBColor;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

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

    /**
     * Creates a new Image object using width and height.
     *
     * @param width
     *                     The width of the Image.
     * @param height
     *                     The height of the Image.
     */
    public Image(int width, int height) {
        this.width  = width;
        this.height = height;

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Creates a new Image object using the Image path.
     *
     * @param path
     *                     The path of this Image.
     */
    public Image(String path) {
        java.io.File imgLoc = new java.io.File(path);

        try {
           BufferedImage preimg = ImageIO.read(imgLoc);
           img =  convertByteToInt(preimg);
        } catch (IOException e) {
           e.printStackTrace();
        }

        this.width  = img.getWidth();
        this.height = img.getHeight();
    }

    /**
     * Creates a new Image object using the Image url.
     *
     * @param url
     *            The URL of this Image.
     */
    public Image(URL url) {
        try {
            BufferedImage preimg = ImageIO.read(url);
            img = convertByteToInt(preimg);
         } catch (IOException e) {
            e.printStackTrace();
         }

         this.width  = img.getWidth();
         this.height = img.getHeight();
    }

    /**
     * Creates a new Image object using BufferedImage.
     *
     * @param image
<<<<<<< HEAD
     * 					The BufferedImge of this Image.
=======
     *                     The BufferedImge of this Image.
>>>>>>> 16121fd9fe4eeaef3cb56619769a3119a9e6531a
     *
     * @see java.awt.image.BufferedImage
     */
    public Image(BufferedImage image) {
         this.img = copyImage(image);
         this.width  = this.img.getWidth();
         this.height = this.img.getHeight();
    }

    /**
     * Copies a Image object using BuffedImage.
     *
     * @param image
<<<<<<< HEAD
     * 					The BufferedImage of this Image.
=======
     *                     The BufferedImage of this Image.
>>>>>>> 16121fd9fe4eeaef3cb56619769a3119a9e6531a
     *
     * @return
     *                     The copy of this Image.
     */
    public BufferedImage copyImage(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return newImage;
    }

    private BufferedImage convertByteToInt(BufferedImage src) {
    	BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
    	int[] pixels = ((DataBufferInt)(dst.getRaster().getDataBuffer())).getData();
    	byte[] binary = ((DataBufferByte)(src.getRaster().getDataBuffer())).getData();
    	int r, g, b, a;
    	int pixelsize = src.getWidth() * src.getHeight();
    	if (src.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
    	    for (int i = 0, j = 0; i < pixelsize; i++) {
    			a = binary[j++] & 0xff;
    			b = binary[j++] & 0xff;
    			g = binary[j++] & 0xff;
    			r = binary[j++] & 0xff;
    			pixels[i] = (a<<24) | (r<<16) | (g<<8) | b;
    		}
    	}
    	if (src.getType() == BufferedImage.TYPE_3BYTE_BGR) {
    		for (int i = 0, j = 0; i < pixelsize; i++) {
    			b = binary[j++] & 0xff;
    			g = binary[j++] & 0xff;
    			r = binary[j++] & 0xff;
    			pixels[i] = 0xff000000 | (r<<16) | (g<<8) | b;
    		}
    	}
    	return dst;
    }

    /**
     * Returns the clone of the input image.
     *
     * @param image  The image you want to clone.
     * @return       The clone image of the input image.
     */
    public static Image clone(Image image) {
        Image cloneImage = new Image(image.img);
        return cloneImage;
    }

    /**
     * Loads a texture using image data.
     */
    public final void loadTexture() {
        texture = null;
        texture = AWTTextureIO.newTexture(GLProfile.get(GLProfile.GL2), img, true);
        if (texture == null) {
            throw new CasmiRuntimeException("Cannot load texture");
        }
    }

    public final void reloadTexture(GL2 gl) {
        texture.updateImage(gl, AWTTextureIO.newTextureData(GLProfile.get(GLProfile.GL2), img, false));
    }

    /**
     * Makes texture to be used.
     * This function is similar to glEnable( GL_TEXTURE_2D ) and glBindTexture() in OpenGL
     *
     * @param gl
     *                     The variable of GL2.
     */
    public void enableTexture(GL2 gl) {
        if( texture != null ) {
            texture.enable(gl);
            texture.bind(gl);
        }
    }

    /**
     * Makes texture not to be used.
     *
     * This function is similar to glDisable( GL_TEXTURE_2D ) in OpenGL
     *
     * @param gl
     *                     The variable of GL2.
     */
    public void disableTexture(GL2 gl) {
        if( texture != null ) {
            texture.disable(gl);
        }
    }
    /**
     * Sets the ImageMode of this Image.
     *
     * @param mode
     * 					The Image mode of this Image.
     *
     * @see casmi.image.ImageMode
     */
    public void imageMode(ImageMode mode) {
        this.mode = mode;
    }

    /**
     * Returns the pixel color of this Image.
     *
     * @param x
     *                     The x-coordinate of the pixel.
     * @param y
     *                     The y-coordinate of the pixel.
     * @return
     *                     The color of the pixel.
     */
    public final Color getColor(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;

        return new RGBColor((pixels[idx] >> 16 & 0x000000ff) / 255.0,
                            (pixels[idx] >> 8  & 0x000000ff) / 255.0,
                            (pixels[idx]       & 0x000000ff) / 255.0,
                            (pixels[idx] >> 24)              / 255.0);
    }

    /**
     * Returns the red color value of the pixel data in this Image.
     *
     * @param x
     *                     The x-coordinate of the pixel.
     * @param y
     *                     The y-coordinate of the pixel.
     * @return
     *                     The red color value of the pixel.
     */
    public final double getRed(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;

        return (pixels[idx] >> 16 & 0x000000ff);
    }

    /**
     * Returns the green color value of the pixel data in this Image.
     *
     * @param x
     *                     The x-coordinate of the pixel.
     * @param y
     *                     The y-coordinate of the pixel.
     * @return
     *                     The green color value of the pixel.
     */
    public final double getGreen(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;

        return (pixels[idx] >> 8  & 0x000000ff);
    }

    /**
     * Returns the blue color value of the pixel data in this Image.
     *
     * @param x
     *                     The x-coordinate of the pixel.
     * @param y
     *                     The y-coordinate of the pixel.
     * @return
     *                     The blue color value of the pixel.
     */
    public final double getBlue(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;

        return (pixels[idx]       & 0x000000ff);
    }

    /**
     * Returns the gray-scale value of the pixel data in this Image.
     *
     * @param x
     *                     The x-coordinate of the pixel.
     * @param y
     *                     The y-coordinate of the pixel.
     * @return
     *                     The gray-scale value of the pixel.
     */
    public final double getGray(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;

        return ((pixels[idx]       & 0x000000ff)+(pixels[idx] >> 8  & 0x000000ff)+(pixels[idx] >> 16 & 0x000000ff))/3;
    }

    /**
     * Returns the alpha value of the pixel data in this Image.
     *
     * @param x
     *                     The x-coordinate of the pixel.
     * @param y
     *                     The y-coordinate of the pixel.
     * @return
     *                     The alpha value of the pixel.
     */
    public final double getAlpha(int x, int y) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        int idx = x + y * width;

        return (pixels[idx] >> 24);
    }

    /**
     * Sets the color value of the pixel data in this Image.
     *
     * @param color
<<<<<<< HEAD
     * 					The color value of the pixel.
=======
     *                     The color value of the pixel.
>>>>>>> 16121fd9fe4eeaef3cb56619769a3119a9e6531a
     * @param x
     *                     The x-coordinate of the pixel.
     * @param y
     *                     The y-coordinate of the pixel.
     */
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

    /**
     * Sets the alpha value of the pixel data in this Image.
     *
     * @param alpha
     *                     The alpha value of the pixel.
     * @param x
     *                     The x-coordinate of the pixel.
     * @param y
     *                     The y-coordinate of the pixel.
     */
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

    /**
     * Sets the color values to this Image.
     *
     * @param colors
<<<<<<< HEAD
     * 					The array of Color which size is width * height of this Image.
=======
     *                     The array of Color which size is width * height of this Image.
>>>>>>> 16121fd9fe4eeaef3cb56619769a3119a9e6531a
     */
    public final void setColors(Color[] colors) {
        int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
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

    /**
     * Returns Texture binded this Image
     *
     * @return
     *                     The Texture object binded this Image.
     */
    public final Texture getTexture() {
        return texture;
    }

    /**
     * Returns ImageMode of this Image.
     *
     * @return
<<<<<<< HEAD
     * 					The ImageMode of the Image.
=======
     *                     The ImageMode of the Image.
>>>>>>> 16121fd9fe4eeaef3cb56619769a3119a9e6531a
     *
     * @see casmi.image.ImageMode
     */
    public final ImageMode getMode() {
        return mode;
    }

    /**
     * Sets ImageMode of this Image.
     *
     * @param mode
<<<<<<< HEAD
     * 					The ImageMode of the Image.
=======
     *                     The ImageMode of the Image.
>>>>>>> 16121fd9fe4eeaef3cb56619769a3119a9e6531a
     *
     * @see casmi.image.ImageMode
     */
    public final void setMode(ImageMode mode) {
        this.mode = mode;
    }

    /**
     * Returns the width of this Image.
     *
     * @return
     *                     The width of the Image.
     */
    public final int getWidth() {
        return width;
    }

    /**
     * Returns the height of this Image.
     *
     * @return
     *                     The height of the Image.
     */
    public final int getHeight() {
        return height;
    }

    public BufferedImage getImg() {
        return img;
    }
}
