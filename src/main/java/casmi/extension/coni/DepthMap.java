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
import java.util.Arrays;


import org.OpenNI.DepthMetaData;

import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Texture;
import casmi.image.Image;

/**
 * A class for access each pixel and whole depth data.
 * This class can also generate a texture from depth data.
 *
 * @see casmi.extension.coni.CONI
 * 
 * @author T. Takeuchi
 */
public class DepthMap {

    private final DepthMetaData dmd;
    private final Texture tex;
    
    private float[] histogram;
    
    DepthMap(DepthMetaData dmd) {
        this.dmd = dmd;
        tex = new Texture(new Image(getWidth(), getHeight()));
    }
    
    final void update() {
        updateTexture();
    }
    
    private final void updateTexture() {
        calcHist();
        
        ShortBuffer buf = dmd.getData().createShortBuffer();
        buf.rewind();
        
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                short d = buf.get();
                if (0 < d) {
                    int gray = (int)histogram[d];
                    tex.getImage().setColor(new RGBColor(gray / 255.0), x, y);
                } else {
                    tex.getImage().setColor(new RGBColor(0.0, 0.0), x, y);
                }
            }
        }
        
        tex.getImage().reloadTexture();
    }
    
    public int getDepth(int x, int y) {
        ShortBuffer buf = dmd.getData().createShortBuffer();
        buf.position((x + y * getWidth()) * 3);
        return (int)buf.get();
    }
    
    public int[] getDepthArray() {
        ShortBuffer buf = dmd.getData().createShortBuffer();
        buf.rewind();
        int[] d = new int[getWidth() * getHeight()];
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                d[x + y * getWidth()] = (int)buf.get();
            }
        }
        return d;
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
    
    private final void calcHist() {
        if (histogram == null) {
            histogram = new float[10000];
        }
        
        Arrays.fill(histogram, 0);
        
        ShortBuffer buf = dmd.getData().createShortBuffer();
        buf.rewind();

        int points = 0;
        while (0 < buf.remaining()) {
            short d = buf.get();
            if (d != 0) {
                histogram[d]++;
                points++;
            }
        }
        
        for (int i = 1; i < histogram.length; i++) {
            histogram[i] += histogram[i - 1];
        }

        if (0 < points) {
            for (int i = 1; i < histogram.length; i++) {
                histogram[i] = (int)(256 * (1.0f - (histogram[i] / (float)points)));
            }
        }
    }
    
    public int getWidth() {
        return dmd.getXRes();
    }
    
    public int getHeight() {
        return dmd.getYRes();
    }
}
