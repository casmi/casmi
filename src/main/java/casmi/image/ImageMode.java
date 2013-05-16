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

/**
 * Modifies the location from which Texture and Image draw.
 * The default mode is ImageMode.CORNER,
 * which specifies the location to be the upper left corner of the shape
 * and uses the third and fourth parameters of Texture() to specify the width and height.
 * The syntax ImageMode.CORNERS uses the first and second parameters of Texture()
 * to set the location of one corner and uses the third and fourth parameters to set the opposite corner.
 * The syntax ImageMode.CENTER draws the image from its center point
 * and uses the third and forth parameters of Texture() to specify the image's width and height.
 *
 * @author Y. Ban
 */
public enum ImageMode {

    CORNER,

    CORNERS,

    CENTER
}
