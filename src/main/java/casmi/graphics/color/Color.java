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

package casmi.graphics.color;

import javax.media.opengl.GL;

/**
 * Color class.
 * Wrap JOGL and make it easy to use.
 * <p>
 * Creates colors for storing in variables of the color datatype.
 * The parameters are interpreted as RGB or HSB values depending on the current colorMode(). 
 * The default mode is RGB values from 0 to 255.
 *
 * @see casmi.graphics.color.ColorMode
 * @see casmi.graphics.color.ColorSet
 * 
 * @author Y. Ban
 */
public class Color {

	protected int r = 255;
	protected int g = 255;
	protected int b = 255;
	protected int a = 255;
	protected ColorMode colorMode = ColorMode.RGB;
	
	private int[] tmpColor = { 0, 0, 0 };
	
	/**
     * Creates a new Color object using Grayscale value.
     *
     * @param gray 
     * 				The grayscale value.
     */
	public Color(int gray) {
		this(gray, gray, gray, 255);
	}

	/**
     * Creates a new Color object using Grayscale and alpha values.
     *
     * @param gray 
     * 				The grayscale value.
     * @param alpha 
     * 				The alpha value.
     */
	public Color(int gray, int alpha) {
		this(gray, gray, gray, alpha);
	}

	/**
     * Creates a new Color object using RGB or HSB values.
     *
     * @param cR 
     * 				The R or H value.
     * @param cG 
     * 				The G or S value.
     * @param cB 
     * 				The B value.
     */
	public Color(int cR, int cG, int cB) {
		this(cR, cG, cB, 255);
	}

	/**
     * Creates a new Color object using RGB or HSB and alpha values.
     *
     * @param cR 
     * 				The R or H value.
     * @param cG 
     * 				The G or S value.
     * @param cB 
     * 				The B value.
     * @param alpha 
     * 				The Alpha value.
     */
	public Color(int cR, int cG, int cB, int alpha) {
		this.r = cR;
		this.g = cG;
		this.b = cB;
		this.a = alpha;
	}
	
	/**
     * Creates a new Color object using RGB value in hexadecimal notation (i.e. "#FFCC44" or 0xFFFFCC00)
     *
     * @param colorStr 
     * 				The grayscale value.
     */
	public Color(String colorStr) {
		int tmpR = 255;
		int tmpG = 255;
		int tmpB = 255;
		int tmpA = 255;
		
		if (colorStr.indexOf("#") == 0) {
            if (colorStr.length() == 7) {
                tmpR = Integer.decode("0X" + colorStr.substring(1, 3));
                tmpG = Integer.decode("0X" + colorStr.substring(3, 5));
                tmpB = Integer.decode("0X" + colorStr.substring(5, 7));
            } else if (colorStr.length() == 9) {
                tmpR = Integer.decode("0X" + colorStr.substring(1, 3));
                tmpG = Integer.decode("0X" + colorStr.substring(3, 5));
                tmpB = Integer.decode("0X" + colorStr.substring(5, 7));
                tmpA = Integer.decode("0X" + colorStr.substring(7, 9));
            }
        } else if (colorStr.indexOf("0x") == 0 || colorStr.indexOf("0X") == 0) {
            if (colorStr.length() == 8) {
                tmpR = Integer.decode("0X" + colorStr.substring(2, 4));
                tmpG = Integer.decode("0X" + colorStr.substring(4, 6));
                tmpB = Integer.decode("0X" + colorStr.substring(6, 8));
            } else if (colorStr.length() == 10) {
                tmpR = Integer.decode("0X" + colorStr.substring(2, 4));
                tmpG = Integer.decode("0X" + colorStr.substring(4, 6));
                tmpB = Integer.decode("0X" + colorStr.substring(6, 8));
                tmpA = Integer.decode("0X" + colorStr.substring(8, 10));
            }
        }
		
        this.r = tmpR;
        this.g = tmpG;
        this.b = tmpB;
        this.a = tmpA;
    }
	
	/**
	 * Creates a new Color object from ColorSet.
	 * 
	 * @param colorSet
	 *            ColorSet.
	 *            
	 * @see casmi.graphics.color.ColorSet
	 */
	public Color(ColorSet colorSet) {
		setColor(colorSet);
	}
	
	/**
	 * Creates a new Color object from ColorSet and an alpha value.
	 * 
	 * @param colorSet
	 *            ColorSet.
	 * @param alpha
	 *            alpha value.
	 *            
	 * @see casmi.graphics.color.ColorSet
	 */
	public Color(ColorSet colorSet, int alpha) {
		setColor(colorSet);
		this.a = alpha;
	}
	
	/**
	 * Returns the colorset's RGB values.
	 * @param colorSet
	 * 				The ColorSet.
	 * @return	
	 * 				The ColorSet's RGB values.
	 */
	private final void setColor(ColorSet colorSet) {
	    int[] rgb = ColorSet.getRGB(colorSet);
	    this.r = rgb[0];
	    this.g = rgb[1];
	    this.b = rgb[2];
	}
	
	/**
	 * Returns the colorset's RGB values.
	 * @param colorSet
	 * 				The ColorSet.
	 * @return	
	 * 				The ColorSet's RGB values.
	 */
	public static Color color(ColorSet colorSet) {
	    return new Color(colorSet);
	}
	
	/**
     * Sets the colormode, RGB or HSB.
     * 
     * @param colorMode
     *              The colormode, RGB or HSB.
     **/
    public void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
    }

	public void calcColor() {
		switch (colorMode) {
		default:
		case RGB:
			tmpColor[0] = this.r;
			tmpColor[1] = this.g;
			tmpColor[2] = this.b;
			break;
		case HSB:
			tmpColor = toRGB(this.r, this.g, this.b);
			break;
		}
	}
	
	/**
	 * Convert HSB (Hue, Saturation, Brightness) to RGB values.
	 * 
	 * @param hue
	 * @param saturation
	 * @param brightness
	 * 
	 * @return 
	 *     RGB values.
	 */
	private final int[] toRGB(int hue, int saturation, int brightness) {
		int[] color = { 0, 0, 0 };
		int tmpI, tmpM, tmpN, tmpK;
		double tmpF;
		tmpI = (int) Math.floor(hue / 60.0);
		tmpF = hue / 60.0 - tmpI;
		tmpM = (int) (brightness * (1 - saturation / 255.0));
		tmpN = (int) (brightness * (1 - (saturation / 255.0) * tmpF));
		tmpK = (int) (brightness * (1 - (saturation / 255.0) * (1 - tmpF)));

		switch (tmpI) {
		case 0:
			color[0] = brightness;
			color[1] = tmpK;
			color[2] = tmpM;
			break;
		case 1:
			color[0] = tmpN;
			color[1] = brightness;
			color[2] = tmpM;
			break;
		case 2:
			color[0] = tmpM;
			color[1] = brightness;
			color[2] = tmpK;
			break;
		case 3:
			color[0] = tmpM;
			color[1] = tmpN;
			color[2] = brightness;
			break;
		case 4:
			color[0] = tmpK;
			color[1] = tmpM;
			color[2] = brightness;
			break;
		case 5:
			color[0] = brightness;
			color[1] = tmpM;
			color[2] = tmpN;
			break;
		default:
			break;
		}
		
		return color;
	}

	/**
	 * Calculates a color or colors between two color at a specific increment. 
	 * @param colorSet1
	 * 				interpolate from this color
	 * @param colorSet2
	 * 				interpolate to this color
	 * @param amt
	 * 				between 0.0 and 1.0
	 * @return
	 * 				The calculated color values.
	 */
	public static Color lerpColor(ColorSet colorSet1, ColorSet colorSet2, double amt) {
		 return lerpColor(Color.color(colorSet1), Color.color(colorSet2), amt);
	}
	
	public static Color lerpColor(Color color1, Color color2, double amt) {
		double r, g, b, a;
		r = color2.r * amt + color1.r * (1 - amt);
		g = color2.g * amt + color1.g * (1 - amt);
		b = color2.b * amt + color1.b * (1 - amt);
		a = color2.a * amt + color1.a * (1 - amt);
		return new Color((int)r, (int)g, (int)b, (int)a);
	}
	
	/**
	 * Returns a Color object that shows a complementary color.
	 *  
	 * @return a complementary Color object.
	 */
	public Color getComplementaryColor() {
	    return new Color(255 - r, 255 - g, 255 - b);
	}
	
	public static void copyColor(Color src, Color dst) {
		dst.setR(src.getR());
		dst.setG(src.getG());
		dst.setB(src.getB());
		dst.setA(src.getA());
		dst.colorMode = src.colorMode;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public void setGray(int g) {
		this.r = g;
		this.g = g;
		this.b = g;
	}

	private static float convertNormalColor(int val) {
		return val / 255.0f;
	}

	public float getNormalR() {
		return convertNormalColor(tmpColor[0]);
	}

	public float getNormalG() {
		return convertNormalColor(tmpColor[1]);
	}

	public float getNormalB() {
		return convertNormalColor(tmpColor[2]);
	}

	public float getNormalA() {
		return convertNormalColor(this.a);
	}

	public void setup(GL gl) {
		calcColor();
		gl.glColor4d(this.getNormalR(), this.getNormalG(), this.getNormalB(), this.getNormalA());
	}
}
