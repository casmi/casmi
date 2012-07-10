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
package casmi.extension.coni;

import java.nio.ByteBuffer;


import org.OpenNI.ImageMetaData;

import casmi.graphics.color.Color;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Texture;
import casmi.image.Image;

/**
 * A class for access each pixel and whole image.
 * This class can also generate a texture from image data.
 *
 * @see casmi.extension.coni.CONI
 * 
 * @author T. Takeuchi
 */
public class ImageMap {
    
    private final ImageMetaData imd;
    private final Texture tex;
    
    ImageMap(ImageMetaData imd) {
        this.imd = imd;
        tex = new Texture(new Image(getWidth(), getHeight()));
    }
    
    final void update() {
        updateTexture();
    }
    
    private final void updateTexture() {
        Color[] colors = getColorArray();
        tex.getImage().setColors(colors);
        tex.getImage().reloadTexture();
    }
    
    public Color getColor(int x, int y) {
        ByteBuffer buf = imd.getData().createByteBuffer();
        buf.position((x + y * getWidth()) * 3);
        int r = buf.get() & 0xff;
        int g = buf.get() & 0xff;
        int b = buf.get() & 0xff;
        return new RGBColor(r / 255.0, g / 255.0, b / 255.0);
    }
    
    public Color[] getColorArray() {
        ByteBuffer buf = imd.getData().createByteBuffer();
        buf.rewind();
        Color[] c = new Color[getWidth() * getHeight()];
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                int r = buf.get() & 0xff;
                int g = buf.get() & 0xff;
                int b = buf.get() & 0xff;
                c[x + y * getWidth()] = new RGBColor(r / 255.0, g / 255.0, b / 255.0);
            }
        }
        return c;
    }
    
    /**
     * 
     * @return
     *     Texture instance.
     * 
     * @see casmi.graphics.element.Texture
     */
    public Texture getTexture() {
        return tex;
    }
    
    public int getWidth() {
        return imd.getXRes();
    }
    
    public int getHeight() {
        return imd.getYRes();
    }
}
