package casmi.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import casmi.graphics.Graphics;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

/**
 * Image class.
 * Wrap javax.imageio.ImageIO and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Image {
    
    public enum ImageMode {
        CORNER, CORNERS, CENTER
    };

    public BufferedImage img = null;
    public Texture texture = null;
    public ImageMode mode = ImageMode.CORNER;

    public int width, height;

    public Image(String path) {
        java.io.File imgLoc = new java.io.File(path);
        
        try {
           img = ImageIO.read(imgLoc);
        } catch (IOException e) {
           System.err.println(e.getMessage());
           e.printStackTrace();
        }

        this.width = img.getWidth();
        this.height = img.getHeight();
        
        Graphics.addTextureImage(this);
    }
    
    public Image(URL url) {
        
        try {
            img = ImageIO.read(url);
         } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
         }

         this.width = img.getWidth();
         this.height = img.getHeight();
         
         Graphics.addTextureImage(this);
    }

    public void loadTexture() {
        if (img != null) {
            texture = TextureIO.newTexture(img, true);
        }
    }

    public void unloadTexture() {
        if( texture != null ) {
            texture.dispose();
        }
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
    
    public void imageMode(ImageMode mode){
        this.mode = mode;
    }
    

}
