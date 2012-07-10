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

import java.nio.ShortBuffer;


import org.OpenNI.IRMetaData;

import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Texture;
import casmi.image.Image;

/**
 * A class for access each pixel and whole IR data.
 * This class can also generate a texture from IR data.
 *
 * @see casmi.extension.coni.CONI
 * 
 * @author T. Takeuchi
 */
public class IRMap {

    private final IRMetaData irmd;
    private final Texture tex;
    
    IRMap(IRMetaData irmd) {
        this.irmd = irmd;
        tex = new Texture(new Image(getWidth(), getHeight()));
    }
    
    final void update() {
        updateTexture();
    }
    
    private final void updateTexture() {
        ShortBuffer buf = irmd.getData().createShortBuffer();
        buf.rewind();
        
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                int ir = (int)buf.get();
                tex.getImage().setColor(new RGBColor(ir / 255.0), x, y);
            }
        }
        
        tex.getImage().reloadTexture();
    }
    
    public int getUser(int x, int y) {
        ShortBuffer buf = irmd.getData().createShortBuffer();
        buf.position((x + y * getWidth()) * 3);
        return (int)buf.get();
    }
    
    public int[] getUserArray() {
        ShortBuffer buf = irmd.getData().createShortBuffer();
        buf.rewind();
        int[] ir = new int[getWidth() * getHeight()];
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                ir[x + y * getWidth()] = (int)buf.get();
            }
        }
        return ir;
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
        return irmd.getXRes();
    }
    
    public int getHeight() {
        return irmd.getYRes();
    }
}
