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
import static casmi.graphics.color.ColorMode.*;

/**
 * Color class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */

/**
 * Creates colors for storing in variables of the color datatype.
 * The parameters are interpreted as RGB or HSB values depending on the current colorMode(). 
 * The default mode is RGB values from 0 to 255
 */

public class Color {

	private int r;
	private int g;
	private int b;
	private int a;
	private ColorMode colormode = RGB;
	private int tmpcolor[] = { 0, 0, 0 };

	
	/**
     * Creates a new Color object using Grayscale value.
     *
     * @param Gray 
     * 				The grayscale value.
     */
	public Color(int Gray) {
		this.setR(Gray);
		this.setG(Gray);
		this.b = Gray;
		this.a = 255;
	}

	/**
     * Creates a new Color object using Grayscale and alpha values.
     *
     * @param Gray 
     * 				The grayscale value.
     * @param cA 
     * 				The alpha value.
     */
	public Color(int Gray, int cA) {
		this.setR(Gray);
		this.setG(Gray);
		this.b = Gray;
		this.a = cA;
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
		this.setR(cR);
		this.setG(cG);
		this.b = cB;
		this.a = 255;
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
     * @param cA 
     * 				The Alpha value.
     */
	public Color(int cR, int cG, int cB, int cA) {
		this.setR(cR);
		this.setG(cG);
		this.b = cB;
		this.a = cA;
	}

	/**
	 * Returns the colorset's RGB values.
	 * @param c
	 * 				The ColorSet.
	 * @return	
	 * 				The ColorSet's RGB values.
	 */
	public static Color color(ColorSet c) {
		switch (c) {
		case ALICEBLUE:
			return new Color(240, 248, 255);
		case ANTIQUEWHITE:
			return new Color(250, 235, 215);
		case AQUA:
			return new Color(0, 255, 255);
		case AQUAMARINE:
			return new Color(127, 255, 212);
		case AZURE:
			return new Color(240, 255, 255);
		case BEIGE:
			return new Color(245, 245, 220);
		case BISQUE:
			return new Color(255, 228, 196);
		case BLACK:
			return new Color(0, 0, 0);
		case BLANCHEDALMOND:
			return new Color(255, 235, 205);
		case BLUE:
			return new Color(0, 0, 255);
		case BLUEVIOLET:
			return new Color(138, 43, 226);
		case BROWN:
			return new Color(165, 42, 42);
		case BURLYWOOD:
			return new Color(222, 184, 135);
		case CADETBLUE:
			return new Color(95, 158, 160);
		case CHARTREUSE:
			return new Color(127, 255, 0);
		case CHOCOLATE:
			return new Color(210, 105, 30);
		case CORAL:
			return new Color(255, 127, 80);
		case CORNFLOWERBLUE:
			return new Color(100, 149, 237);
		case CORNSILK:
			return new Color(255, 248, 220);
		case CRIMSON:
			return new Color(220, 20, 60);
		case CYAN:
			return new Color(0, 255, 255);
		case DARKBLUE:
			return new Color(0, 0, 139);
		case DARKCYAN:
			return new Color(0, 139, 139);
		case DARKGOLDENROD:
			return new Color(184, 134, 11);
		case DARKGRAY:
			return new Color(169, 169, 169);
		case DARKGREEN:
			return new Color(0, 100, 0);
		case DARKGREY:
			return new Color(169, 169, 169);
		case DARKKHAKI:
			return new Color(189, 183, 107);
		case DARKMAGENTA:
			return new Color(139, 0, 139);
		case DARKOLIVEGREEN:
			return new Color(85, 107, 47);
		case DARKORANGE:
			return new Color(255, 140, 0);
		case DARKORCHID:
			return new Color(153, 50, 204);
		case DARKRED:
			return new Color(139, 0, 0);
		case DARKSALMON:
			return new Color(233, 150, 122);
		case DARKSEAGREEN:
			return new Color(143, 188, 143);
		case DARKSLATEBLUE:
			return new Color(72, 61, 139);
		case DARKSLATEGRAY:
			return new Color(47, 79, 79);
		case DARKSLATEGREY:
			return new Color(47, 79, 79);
		case DARKTURQUOISE:
			return new Color(0, 206, 209);
		case DARKVIOLET:
			return new Color(148, 0, 211);
		case DEEPPINK:
			return new Color(255, 20, 147);
		case DEEPSKYBLUE:
			return new Color(0, 191, 255);
		case DIMGRAY:
			return new Color(105, 105, 105);
		case DIMGREY:
			return new Color(105, 105, 105);
		case DODGERBLUE:
			return new Color(30, 144, 255);
		case FIREBRICK:
			return new Color(178, 34, 34);
		case FLORALWHITE:
			return new Color(255, 250, 240);
		case FORESTGREEN:
			return new Color(34, 139, 34);
		case FUCHSIA:
			return new Color(255, 0, 255);
		case GAINSBORO:
			return new Color(220, 220, 220);
		case GHOSTWHITE:
			return new Color(248, 248, 255);
		case GOLD:
			return new Color(255, 215, 0);
		case GOLDENROD:
			return new Color(218, 165, 32);
		case GRAY:
			return new Color(128, 128, 128);
		case GREEN:
			return new Color(0, 128, 0);
		case GREENYELLOW:
			return new Color(173, 255, 47);
		case GREY:
			return new Color(128, 128, 128);
		case HONEYDEW:
			return new Color(240, 255, 240);
		case HOTPINK:
			return new Color(255, 105, 180);
		case INDIANRED:
			return new Color(205, 92, 92);
		case INDIGO:
			return new Color(75, 0, 130);
		case IVORY:
			return new Color(255, 255, 240);
		case KHAKI:
			return new Color(240, 230, 140);
		case LAVENDER:
			return new Color(230, 230, 250);
		case LAVENDERBLUSH:
			return new Color(255, 240, 245);
		case LAWNGREEN:
			return new Color(124, 252, 0);
		case LEMONCHIFFON:
			return new Color(255, 250, 205);
		case LIGHTBLUE:
			return new Color(173, 216, 230);
		case LIGHTCORAL:
			return new Color(240, 128, 128);
		case LIGHTCYAN:
			return new Color(224, 255, 255);
		case LIGHTGOLDENRODYELLOW:
			return new Color(250, 250, 210);
		case LIGHTGRAY:
			return new Color(211, 211, 211);
		case LIGHTGREEN:
			return new Color(144, 238, 144);
		case LIGHTGREY:
			return new Color(211, 211, 211);
		case LIGHTPINK:
			return new Color(255, 182, 193);
		case LIGHTSALMON:
			return new Color(255, 160, 122);
		case LIGHTSEAGREEN:
			return new Color(32, 178, 170);
		case LIGHTSKYBLUE:
			return new Color(135, 206, 250);
		case LIGHTSLATEGRAY:
			return new Color(119, 136, 153);
		case LIGHTSLATEGREY:
			return new Color(119, 136, 153);
		case LIGHTSTEELBLUE:
			return new Color(176, 196, 222);
		case LIGHTYELLOW:
			return new Color(255, 255, 224);
		case LIME:
			return new Color(0, 255, 0);
		case LIMEGREEN:
			return new Color(50, 205, 50);
		case LINEN:
			return new Color(250, 240, 230);
		case MAGENTA:
			return new Color(255, 0, 255);
		case MAROON:
			return new Color(128, 0, 0);
		case MEDIUMAQUAMARINE:
			return new Color(102, 205, 170);
		case MEDIUMBLUE:
			return new Color(0, 0, 205);
		case MEDIUMORCHID:
			return new Color(186, 85, 211);
		case MEDIUMPURPLE:
			return new Color(147, 112, 219);
		case MEDIUMSEAGREEN:
			return new Color(60, 179, 113);
		case MEDIUMSLATEBLUE:
			return new Color(123, 104, 238);
		case MEDIUMSPRINGGREEN:
			return new Color(0, 250, 154);
		case MEDIUMTURQUOISE:
			return new Color(72, 209, 204);
		case MEDIUMVIOLETRED:
			return new Color(199, 21, 133);
		case MIDNIGHTBLUE:
			return new Color(25, 25, 112);
		case MINTCREAM:
			return new Color(245, 255, 250);
		case MISTYROSE:
			return new Color(255, 228, 225);
		case MOCCASIN:
			return new Color(255, 228, 181);
		case NAVAJOWHITE:
			return new Color(255, 222, 173);
		case NAVY:
			return new Color(0, 0, 128);
		case OLDLACE:
			return new Color(253, 245, 230);
		case OLIVE:
			return new Color(128, 128, 0);
		case OLIVEDRAB:
			return new Color(107, 142, 35);
		case ORANGE:
			return new Color(255, 165, 0);
		case ORANGERED:
			return new Color(255, 69, 0);
		case ORCHID:
			return new Color(218, 112, 214);
		case PALEGOLDENROD:
			return new Color(238, 232, 170);
		case PALEGREEN:
			return new Color(152, 251, 152);
		case PALETURQUOISE:
			return new Color(175, 238, 238);
		case PALEVIOLETRED:
			return new Color(219, 112, 147);
		case PAPAYAWHIP:
			return new Color(255, 239, 213);
		case PEACHPUFF:
			return new Color(255, 218, 185);
		case PERU:
			return new Color(205, 133, 63);
		case PINK:
			return new Color(255, 192, 203);
		case PLUM:
			return new Color(221, 160, 221);
		case POWDERBLUE:
			return new Color(176, 224, 230);
		case PURPLE:
			return new Color(128, 0, 128);
		case RED:
			return new Color(255, 0, 0);
		case ROSYBROWN:
			return new Color(188, 143, 143);
		case ROYALBLUE:
			return new Color(65, 105, 225);
		case SADDLEBROWN:
			return new Color(139, 69, 19);
		case SALMON:
			return new Color(250, 128, 114);
		case SANDYBROWN:
			return new Color(244, 164, 96);
		case SEAGREEN:
			return new Color(46, 139, 87);
		case SEASHELL:
			return new Color(255, 245, 238);
		case SIENNA:
			return new Color(160, 82, 45);
		case SILVER:
			return new Color(192, 192, 192);
		case SKYBLUE:
			return new Color(135, 206, 235);
		case SLATEBLUE:
			return new Color(106, 90, 205);
		case SLATEGRAY:
			return new Color(112, 128, 144);
		case SLATEGREY:
			return new Color(112, 128, 144);
		case SNOW:
			return new Color(255, 250, 250);
		case SPRINGGREEN:
			return new Color(0, 255, 127);
		case STEELBLUE:
			return new Color(70, 130, 180);
		case TAN:
			return new Color(210, 180, 140);
		case TEAL:
			return new Color(0, 128, 128);
		case THISTLE:
			return new Color(216, 191, 216);
		case TOMATO:
			return new Color(255, 99, 71);
		case TURQUOISE:
			return new Color(64, 224, 208);
		case VIOLET:
			return new Color(238, 130, 238);
		case WHEAT:
			return new Color(245, 222, 179);
		case WHITE:
			return new Color(255, 255, 255);
		case WHITESMOKE:
			return new Color(245, 245, 245);
		case YELLOW:
			return new Color(255, 255, 0);
		case YELLOWGREEN:
			return new Color(154, 205, 50);
		default:
			break;
		}

		return null;
	}

	/**
	 * Sets the colormode, RGB or HSB.
	 * 
	 * @param cm
	 * 				The colormode, RGB or HSB.
	 **/
	public void colorMode(ColorMode cm) {
		switch (cm) {
		case RGB:
			colormode = RGB;
			break;
		case HSB:
			colormode = HSB;
			break;
		default:
			colormode = RGB;
			break;

		}
	}

	public void calcColor() {
		switch (colormode) {
		default:
		case RGB:
			tmpcolor[0] = this.r;
			tmpcolor[1] = this.g;
			tmpcolor[2] = this.b;
			break;
		case HSB:
			tmpcolor = calcRGBtoHSB();
			break;
		}
	}

	private int[] calcRGBtoHSB() {
		int color[] = { 0, 0, 0 };
		int tmph = this.r;
		int tmps = this.g;
		int tmpb = this.b;
		int tempi, tempm, tempn, tempk;
		double tempf;
		tempi = (int) Math.floor(tmph / 60.0);
		tempf = tmph / 60.0 - tempi;
		tempm = (int) (tmpb * (1 - tmps / 255.0));
		tempn = (int) (tmpb * (1 - (tmps / 255.0) * tempf));
		tempk = (int) (tmpb * (1 - (tmps / 255.0) * (1 - tempf)));

		switch (tempi) {
		case 0:
			color[0] = tmpb;
			color[1] = tempk;
			color[2] = tempm;
			break;
		case 1:
			color[0] = tempn;
			color[1] = tmpb;
			color[2] = tempm;
			break;
		case 2:
			color[0] = tempm;
			color[1] = tmpb;
			color[2] = tempk;
			break;
		case 3:
			color[0] = tempm;
			color[1] = tempn;
			color[2] = tmpb;
			break;
		case 4:
			color[0] = tempk;
			color[1] = tempm;
			color[2] = tmpb;
			break;
		case 5:
			color[0] = tmpb;
			color[1] = tempm;
			color[2] = tempn;
			break;
		default:
			break;
		}
		return color;
	}

	/**
	 * Calculates a color or colors between two color at a specific increment. 
	 * @param c1
	 * 				interpolate from this color
	 * @param c2
	 * 				interpolate to this color
	 * @param amt
	 * 				between 0.0 and 1.0
	 * @return
	 * 				The calculated color values.
	 */
	public static Color lerpColor(Color c1, Color c2, float amt) {
		float r, g, b;
		r = c2.r * amt + c1.r * (1 - amt);
		g = c2.g * amt + c1.g * (1 - amt);
		b = c2.b * amt + c1.b * (1 - amt);
		return new Color((int) r, (int) g, (int) b);
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
		return (float) (val / (float) 255.0);
	}

	public float getNormalR() {
		return convertNormalColor(tmpcolor[0]);
	}

	public float getNormalG() {
		return convertNormalColor(tmpcolor[1]);
	}

	public float getNormalB() {
		return convertNormalColor(tmpcolor[2]);
	}

	public float getNormalA() {
		return convertNormalColor(this.a);
	}

	public void setup(GL gl) {
		calcColor();
		gl.glColor4d(this.getNormalR(), this.getNormalG(), this.getNormalB(), this.getNormalA());
	}
}
