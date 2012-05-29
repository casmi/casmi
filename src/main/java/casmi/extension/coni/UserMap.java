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

import org.OpenNI.SceneMetaData;

import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Texture;
import casmi.image.Image;

/**
 * A class for access each pixel and whole user data
 * This class can also generate a texture from user data.
 *
 * @see casmi.extension.coni.CONI
 * 
 * @author T. Takeuchi
 */
public class UserMap {
    
    private static final Color[] COLORS = {
        new RGBColor(ColorSet.RED),
        new RGBColor(ColorSet.BLUE),
        new RGBColor(ColorSet.CYAN),
        new RGBColor(ColorSet.GREEN),
        new RGBColor(ColorSet.MAGENTA),
        new RGBColor(ColorSet.PINK),
        new RGBColor(ColorSet.YELLOW),
        new RGBColor(ColorSet.WHITE)
    };
    
    private final SceneMetaData smd;
    private final Texture tex;
    
    UserMap(SceneMetaData smd) {
        this.smd = smd;
        tex = new Texture(new Image(getWidth(), getHeight()));
    }
    
    final void update() { 
        updateTexture();
    }
    
    private final void updateTexture() {
        ShortBuffer buf = smd.getData().createShortBuffer();
        buf.rewind();
        
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                short u = buf.get();
                if (0 < u) {
                    tex.getImage().setColor(COLORS[u % COLORS.length], x, y);
                } else {
                    tex.getImage().setColor(new RGBColor(0.0, 0.0), x, y);
                }
            }
        }
        
        tex.getImage().reloadTexture();
    }
    
    public final int getUser(int x, int y) {
        ShortBuffer buf = smd.getData().createShortBuffer();
        buf.position(x + y * getWidth());
        return (int)buf.get();
    }
    
    public final int[] getUserArray() {
        ShortBuffer buf = smd.getData().createShortBuffer();
        buf.rewind();
        int[] u = new int[getWidth() * getHeight()];
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                u[x + y * getWidth()] = (int)buf.get();
            }
        }
        return u;
    }
    
    public final Texture getTexture() {
        return tex;
    }
    
    public final int getWidth() {
        return smd.getXRes();
    }
    
    public final int getHeight() {
        return smd.getYRes();
    }
}
