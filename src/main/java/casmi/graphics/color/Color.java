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
	private int a = 255;
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
	
	public Color(ColorSet colorset){
		colorset(colorset);
	}
	
	public Color(ColorSet colorset,int alpha){
		colorset(colorset);
		this.a = alpha;
	}

	
	/**
	 * Returns the colorset's RGB values.
	 * @param c
	 * 				The ColorSet.
	 * @return	
	 * 				The ColorSet's RGB values.
	 */
	private void colorset(ColorSet c) {
		switch (c) {
		case ALICEBLUE:
			this.setR(240);
			this.setG(248);
			this.setB(255);
			break;
		case ANTIQUEWHITE:
			this.setR(250);
			this.setG(235);
			this.setB(215);
			break;
		case AQUA:
			this.setR(0);
			this.setG(255);
			this.setB(255);
			break;
		case AQUAMARINE:
			this.setR(127);
			this.setG(255);
			this.setB(212);
			break;
		case AZURE:
			this.setR(240);
			this.setG(255);
			this.setB(255);
			break;
		case BEIGE:
			this.setR(245);
			this.setG(245);
			this.setB(220);
			break;
		case BISQUE:
			this.setR(255);
			this.setG(228);
			this.setB(196);
			break;
		case BLACK:
			this.setR(0);
			this.setG(0);
			this.setB(0);
			break;
		case BLANCHEDALMOND:
			this.setR(255);
			this.setG(235);
			this.setB(205);
			break;
		case BLUE:
			this.setR(0);
			this.setG(0);
			this.setB(255);
			break;
		case BLUEVIOLET:
			this.setR(138);
			this.setG(43);
			this.setB(226);
			break;
		case BROWN:
			this.setR(165);
			this.setG(42);
			this.setB(42);
			break;
		case BURLYWOOD:
			this.setR(222);
			this.setG(184);
			this.setB(135);
			break;
		case CADETBLUE:
			this.setR(95);
			this.setG(158);
			this.setB(160);
			break;
		case CHARTREUSE:
			this.setR(127);
			this.setG(255);
			this.setB(0);
			break;
		case CHOCOLATE:
			this.setR(210);
			this.setG(105);
			this.setB(30);
			break;
		case CORAL:
			this.setR(255);
			this.setG(127);
			this.setB(80);
			break;
		case CORNFLOWERBLUE:
			this.setR(100);
			this.setG(149);
			this.setB(237);
			break;
		case CORNSILK:
			this.setR(255);
			this.setG(248);
			this.setB(220);
			break;
		case CRIMSON:
			this.setR(220);
			this.setG(20);
			this.setB(60);
			break;
		case CYAN:
			this.setR(0);
			this.setG(255);
			this.setB(255);
			break;
		case DARKBLUE:
			this.setR(0);
			this.setG(0);
			this.setB(139);
			break;
		case DARKCYAN:
			this.setR(0);
			this.setG(139);
			this.setB(139);
			break;
		case DARKGOLDENROD:
			this.setR(184);
			this.setG(134);
			this.setB(11);
			break;
		case DARKGRAY:
			this.setR(169);
			this.setG(169);
			this.setB(169);
			break;
		case DARKGREEN:
			this.setR(0);
			this.setG(100);
			this.setB(0);
			break;
		case DARKGREY:
			this.setR(169);
			this.setG(169);
			this.setB(169);
			break;
		case DARKKHAKI:
			this.setR(189);
			this.setG(183);
			this.setB(107);
			break;
		case DARKMAGENTA:
			this.setR(139);
			this.setG(0);
			this.setB(139);
			break;
		case DARKOLIVEGREEN:
			this.setR(85);
			this.setG(107);
			this.setB(47);
			break;
		case DARKORANGE:
			this.setR(255);
			this.setG(140);
			this.setB(0);
			break;
		case DARKORCHID:
			this.setR(153);
			this.setG(50);
			this.setB(204);
			break;
		case DARKRED:
			this.setR(139);
			this.setG(0);
			this.setB(0);
			break;
		case DARKSALMON:
			this.setR(233);
			this.setG(150);
			this.setB(122);
			break;
		case DARKSEAGREEN:
			this.setR(143);
			this.setG(188);
			this.setB(143);
			break;
		case DARKSLATEBLUE:
			this.setR(72);
			this.setG(61);
			this.setB(139);
			break;
		case DARKSLATEGRAY:
			this.setR(47);
			this.setG(79);
			this.setB(79);
			break;
		case DARKSLATEGREY:
			this.setR(47);
			this.setG(79);
			this.setB(79);
			break;
		case DARKTURQUOISE:
			this.setR(0);
			this.setG(206);
			this.setB(209);
			break;
		case DARKVIOLET:
			this.setR(148);
			this.setG(0);
			this.setB(211);
			break;
		case DEEPPINK:
			this.setR(255);
			this.setG(20);
			this.setB(147);
			break;
		case DEEPSKYBLUE:
			this.setR(0);
			this.setG(191);
			this.setB(255);
			break;
		case DIMGRAY:
			this.setR(105);
			this.setG(105);
			this.setB(105);
			break;
		case DIMGREY:
			this.setR(105);
			this.setG(105);
			this.setB(105);
			break;
		case DODGERBLUE:
			this.setR(30);
			this.setG(144);
			this.setB(255);
			break;
		case FIREBRICK:
			this.setR(178);
			this.setG(34);
			this.setB(34);
			break;
		case FLORALWHITE:
			this.setR(255);
			this.setG(250);
			this.setB(240);
			break;
		case FORESTGREEN:
			this.setR(34);
			this.setG(139);
			this.setB(34);
			break;
		case FUCHSIA:
			this.setR(255);
			this.setG(0);
			this.setB(255);
			break;
		case GAINSBORO:
			this.setR(220);
			this.setG(220);
			this.setB(220);
			break;
		case GHOSTWHITE:
			this.setR(248);
			this.setG(248);
			this.setB(255);
			break;
		case GOLD:
			this.setR(255);
			this.setG(215);
			this.setB(0);
			break;
		case GOLDENROD:
			this.setR(218);
			this.setG(165);
			this.setB(32);
			break;
		case GRAY:
			this.setR(128);
			this.setG(128);
			this.setB(128);
			break;
		case GREEN:
			this.setR(0);
			this.setG(128);
			this.setB(0);
			break;
		case GREENYELLOW:
			this.setR(173);
			this.setG(255);
			this.setB(47);
			break;
		case GREY:
			this.setR(128);
			this.setG(128);
			this.setB(128);
			break;
		case HONEYDEW:
			this.setR(240);
			this.setG(255);
			this.setB(240);
			break;
		case HOTPINK:
			this.setR(255);
			this.setG(105);
			this.setB(180);
			break;
		case INDIANRED:
			this.setR(205);
			this.setG(92);
			this.setB(92);
			break;
		case INDIGO:
			this.setR(75);
			this.setG(0);
			this.setB(130);
			break;
		case IVORY:
			this.setR(255);
			this.setG(255);
			this.setB(240);
			break;
		case KHAKI:
			this.setR(240);
			this.setG(230);
			this.setB(140);
			break;
		case LAVENDER:
			this.setR(230);
			this.setG(230);
			this.setB(250);
			break;
		case LAVENDERBLUSH:
			this.setR(255);
			this.setG(240);
			this.setB(245);
			break;
		case LAWNGREEN:
			this.setR(124);
			this.setG(252);
			this.setB(0);
			break;
		case LEMONCHIFFON:
			this.setR(255);
			this.setG(250);
			this.setB(205);
			break;
		case LIGHTBLUE:
			this.setR(173);
			this.setG(216);
			this.setB(230);
			break;
		case LIGHTCORAL:
			this.setR(240);
			this.setG(128);
			this.setB(128);
			break;
		case LIGHTCYAN:
			this.setR(224);
			this.setG(255);
			this.setB(255);
			break;
		case LIGHTGOLDENRODYELLOW:
			this.setR(250);
			this.setG(250);
			this.setB(210);
			break;
		case LIGHTGRAY:
			this.setR(211);
			this.setG(211);
			this.setB(211);
			break;
		case LIGHTGREEN:
			this.setR(144);
			this.setG(238);
			this.setB(144);
			break;
		case LIGHTGREY:
			this.setR(211);
			this.setG(211);
			this.setB(211);
			break;
		case LIGHTPINK:
			this.setR(255);
			this.setG(182);
			this.setB(193);
			break;
		case LIGHTSALMON:
			this.setR(255);
			this.setG(160);
			this.setB(122);
			break;
		case LIGHTSEAGREEN:
			this.setR(32);
			this.setG(178);
			this.setB(170);
			break;
		case LIGHTSKYBLUE:
			this.setR(135);
			this.setG(206);
			this.setB(250);
			break;
		case LIGHTSLATEGRAY:
			this.setR(119);
			this.setG(136);
			this.setB(153);
			break;
		case LIGHTSLATEGREY:
			this.setR(119);
			this.setG(136);
			this.setB(153);
			break;
		case LIGHTSTEELBLUE:
			this.setR(176);
			this.setG(196);
			this.setB(222);
			break;
		case LIGHTYELLOW:
			this.setR(255);
			this.setG(255);
			this.setB(224);
			break;
		case LIME:
			this.setR(0);
			this.setG(255);
			this.setB(0);
			break;
		case LIMEGREEN:
			this.setR(50);
			this.setG(205);
			this.setB(50);
			break;
		case LINEN:
			this.setR(250);
			this.setG(240);
			this.setB(230);
			break;
		case MAGENTA:
			this.setR(255);
			this.setG(0);
			this.setB(255);
			break;
		case MAROON:
			this.setR(128);
			this.setG(0);
			this.setB(0);
			break;
		case MEDIUMAQUAMARINE:
			this.setR(102);
			this.setG(205);
			this.setB(170);
			break;
		case MEDIUMBLUE:
			this.setR(0);
			this.setG(0);
			this.setB(205);
			break;
		case MEDIUMORCHID:
			this.setR(186);
			this.setG(85);
			this.setB(211);
			break;
		case MEDIUMPURPLE:
			this.setR(147);
			this.setG(112);
			this.setB(219);
			break;
		case MEDIUMSEAGREEN:
			this.setR(60);
			this.setG(179);
			this.setB(113);
			break;
		case MEDIUMSLATEBLUE:
			this.setR(123);
			this.setG(104);
			this.setB(238);
			break;
		case MEDIUMSPRINGGREEN:
			this.setR(0);
			this.setG(250);
			this.setB(154);
			break;
		case MEDIUMTURQUOISE:
			this.setR(72);
			this.setG(209);
			this.setB(204);
			break;
		case MEDIUMVIOLETRED:
			this.setR(199);
			this.setG(21);
			this.setB(133);
			break;
		case MIDNIGHTBLUE:
			this.setR(25);
			this.setG(25);
			this.setB(112);
			break;
		case MINTCREAM:
			this.setR(245);
			this.setG(255);
			this.setB(250);
			break;
		case MISTYROSE:
			this.setR(255);
			this.setG(228);
			this.setB(225);
			break;
		case MOCCASIN:
			this.setR(255);
			this.setG(228);
			this.setB(181);
			break;
		case NAVAJOWHITE:
			this.setR(255);
			this.setG(222);
			this.setB(173);
			break;
		case NAVY:
			this.setR(0);
			this.setG(0);
			this.setB(128);
			break;
		case OLDLACE:
			this.setR(253);
			this.setG(245);
			this.setB(230);
			break;
		case OLIVE:
			this.setR(128);
			this.setG(128);
			this.setB(0);
			break;
		case OLIVEDRAB:
			this.setR(107);
			this.setG(142);
			this.setB(35);
			break;
		case ORANGE:
			this.setR(255);
			this.setG(165);
			this.setB(0);
			break;
		case ORANGERED:
			this.setR(255);
			this.setG(69);
			this.setB(0);
			break;
		case ORCHID:
			this.setR(218);
			this.setG(112);
			this.setB(214);
			break;
		case PALEGOLDENROD:
			this.setR(238);
			this.setG(232);
			this.setB(170);
			break;
		case PALEGREEN:
			this.setR(152);
			this.setG(251);
			this.setB(152);
			break;
		case PALETURQUOISE:
			this.setR(175);
			this.setG(238);
			this.setB(238);
			break;
		case PALEVIOLETRED:
			this.setR(219);
			this.setG(112);
			this.setB(147);
			break;
		case PAPAYAWHIP:
			this.setR(255);
			this.setG(239);
			this.setB(213);
			break;
		case PEACHPUFF:
			this.setR(255);
			this.setG(218);
			this.setB(185);
			break;
		case PERU:
			this.setR(205);
			this.setG(133);
			this.setB(63);
			break;
		case PINK:
			this.setR(255);
			this.setG(192);
			this.setB(203);
			break;
		case PLUM:
			this.setR(221);
			this.setG(160);
			this.setB(221);
			break;
		case POWDERBLUE:
			this.setR(176);
			this.setG(224);
			this.setB(230);
			break;
		case PURPLE:
			this.setR(128);
			this.setG(0);
			this.setB(128);
			break;
		case RED:
			this.setR(255);
			this.setG(0);
			this.setB(0);
			break;
		case ROSYBROWN:
			this.setR(188);
			this.setG(136);
			this.setB(153);
			break;
		case ROYALBLUE:
			this.setR(65);
			this.setG(105);
			this.setB(255);
			break;
		case SADDLEBROWN:
			this.setR(139);
			this.setG(69);
			this.setB(19);
			break;
		case SALMON:
			this.setR(250);
			this.setG(128);
			this.setB(114);
			break;
		case SANDYBROWN:
			this.setR(244);
			this.setG(164);
			this.setB(96);
			break;
		case SEAGREEN:
			this.setR(46);
			this.setG(139);
			this.setB(87);
			break;
		case SEASHELL:
			this.setR(255);
			this.setG(245);
			this.setB(238);
			break;
		case SIENNA:
			this.setR(160);
			this.setG(82);
			this.setB(45);
			break;
		case SILVER:
			this.setR(192);
			this.setG(192);
			this.setB(192);
			break;
		case SKYBLUE:
			this.setR(135);
			this.setG(206);
			this.setB(235);
			break;
		case SLATEBLUE:
			this.setR(106);
			this.setG(90);
			this.setB(205);
			break;
		case SLATEGRAY:
			this.setR(112);
			this.setG(128);
			this.setB(144);
			break;
		case SLATEGREY:
			this.setR(112);
			this.setG(128);
			this.setB(144);
			break;
		case SNOW:
			this.setR(255);
			this.setG(250);
			this.setB(250);
			break;
		case SPRINGGREEN:
			this.setR(0);
			this.setG(255);
			this.setB(127);
			break;
		case STEELBLUE:
			this.setR(70);
			this.setG(130);
			this.setB(180);
			break;
		case TAN:
			this.setR(210);
			this.setG(180);
			this.setB(140);
			break;
		case TEAL:
			this.setR(0);
			this.setG(128);
			this.setB(128);
			break;
		case THISTLE:
			this.setR(216);
			this.setG(191);
			this.setB(216);
			break;
		case TOMATO:
			this.setR(255);
			this.setG(99);
			this.setB(71);
			break;
		case TURQUOISE:
			this.setR(64);
			this.setG(224);
			this.setB(208);
			break;
		case VIOLET:
			this.setR(238);
			this.setG(130);
			this.setB(238);
			break;
		case WHEAT:
			this.setR(245);
			this.setG(222);
			this.setB(179);
			break;
		case WHITE:
			this.setR(255);
			this.setG(255);
			this.setB(255);
			break;
		case WHITESMOKE:
			this.setR(245);
			this.setG(245);
			this.setB(245);
			break;
		case YELLOW:
			this.setR(255);
			this.setG(255);
			this.setB(0);
			break;
		case YELLOWGREEN:
			this.setR(154);
			this.setG(205);
			this.setB(50);
			break;
		default:
			break;
		}

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
			tmpcolor = calcHSBtoRGB();
			break;
		}
	}

	private int[] calcHSBtoRGB() {
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
	
	public static void copyColor(Color src, Color dst){
		dst.setR(src.getR());
		dst.setG(src.getG());
		dst.setB(src.getB());
		dst.setA(src.getA());
		dst.colormode = src.colormode;
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
