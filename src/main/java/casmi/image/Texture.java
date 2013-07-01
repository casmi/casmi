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

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.media.opengl.GL2;

import casmi.graphics.element.TextureFlipMode;
import casmi.graphics.element.TextureRotationMode;

/**
 * Texture class. Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Texture {

    public static final int LINES = 1;
    public static final int LINES_3D = 3;
    public static final int LINE_LOOP = 51;

    protected Image image;

    protected double width;
    protected double height;

    protected boolean requireToLoad = false;
    protected boolean requireToReload = false;

    private float[][] corner = { {0.0f, 1.0f}, {0.0f, 0.0f}, {1.0f, 0.0f}, {1.0f, 1.0f}};

    /**
     * Creates a new Texture using the Image's path.
     *
     * @param path The path of Image.
     *
     */
    public Texture(String path) {
        this(new Image(path));
    }

    /**
     * Creates a new Texture using the Image's url.
     *
     * @param url The url of Image.
     *
     */
    public Texture(URL url) {
        this(new Image(url));
    }

    /**
     * Creates a new Texture using the Image.
     *
     * @param image The Image of this Texture.
     *
     */
    public Texture(Image image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();

        requireToLoad = true;
        requireToReload = false;
    }

    public final Image getImage() {
        return this.image;
    }

    public final void enableTexture(GL2 gl) {
        image.enableTexture(gl);
    }

    public final void disableTexture(GL2 gl) {
        image.disableTexture(gl);
    }

    public void render(GL2 gl) {
        if (requireToLoad) {
            image.loadTexture();
            requireToLoad = false;
        }

        if (requireToReload) {
            image.reloadTexture(gl);
            requireToReload = false;
        }
    }

    /**
     * Gets the width of this Texture.
     *
     * @return The width of the Texture.
     */
    public final double getWidth() {
        return width;
    }

    /**
     * Gets the height of this Texture.
     *
     * @return The height of the Texture.
     */
    public final double getHeight() {
        return height;
    }

    /**
     * Rotates the way of mapping the texture.
     *
     * @param mode The alignment of the position of the Texture.
     *
     * @see casmi.image.ImageMode
     */
	public void rotation(TextureRotationMode mode){
		float[][] tmp = corner.clone();
		switch (mode) {
		case HALF:
			corner[0] = tmp[2];
			corner[1] = tmp[3];
			corner[2] = tmp[0];
			corner[3] = tmp[1];
			break;
		case CLOCKWIZE:
			corner[0] = tmp[3];
			corner[1] = tmp[0];
			corner[2] = tmp[1];
			corner[3] = tmp[2];
			break;
		case COUNTERCLOCKWIZE:
			corner[0] = tmp[1];
			corner[1] = tmp[2];
			corner[2] = tmp[3];
			corner[3] = tmp[0];
			break;
		default:
			break;
		}
	}

	/**
     * Flips the way of mapping the texture.
     *
     * @param mode The alignment of the position of the Texture.
     *
     * @see casmi.image.ImageMode
     */
	public void flip(TextureFlipMode mode){
		float[][] tmp = corner.clone();
		switch (mode) {
		case VERTICAL:
			corner[0] = tmp[1];
			corner[1] = tmp[0];
			corner[2] = tmp[3];
			corner[3] = tmp[2];
			break;
		case HORIZONTAL:
			corner[0] = tmp[3];
			corner[1] = tmp[2];
			corner[2] = tmp[1];
			corner[3] = tmp[0];
			break;
		default:
			break;
		}
	}

	public void setTextureCorner(int index, double x,double y){
		corner[index][0] = (float)x;
		corner[index][1] = (float)y;
	}

	public float getTextureCorner(int index1,int index2){
		return corner[index1][index2];
	}

	public void reload() {
	    requireToReload = true;
	}

	public void load() {
	    requireToLoad = true;
	}

	public BufferedImage getBufferedImage() {
	    return image.getImg();
	}
}
