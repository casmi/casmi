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

package casmi;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * Cursor types.
 *
 * @author Y. Ban, T. Takeuchi
 */
public enum CursorMode {

    DEFAULT,

    CROSS,

    HAND,

    MOVE,

    TEXT,

    WAIT,

    RESIZE_NORTH,

    RESIZE_NORTH_EAST,

    RESIZE_EAST,

    RESIZE_SOUTH_EAST,

    RESIZE_SOUTH,

    RESIZE_SOUTH_WEST,

    RESIZE_WEST,

    RESIZE_NORTH_WEST,

    NONE;

    public static CursorMode getCursorMode(java.awt.Cursor awtCursor) {
        switch (awtCursor.getType()) {
        case java.awt.Cursor.DEFAULT_CURSOR:
        default:
            return DEFAULT;
        case java.awt.Cursor.CROSSHAIR_CURSOR:
            return CROSS;
        case java.awt.Cursor.HAND_CURSOR:
            return HAND;
        case java.awt.Cursor.TEXT_CURSOR:
            return TEXT;
        case java.awt.Cursor.WAIT_CURSOR:
            return WAIT;
        case java.awt.Cursor.MOVE_CURSOR:
            return MOVE;
        case java.awt.Cursor.N_RESIZE_CURSOR:
            return RESIZE_NORTH;
        case java.awt.Cursor.NE_RESIZE_CURSOR:
            return RESIZE_NORTH_EAST;
        case java.awt.Cursor.E_RESIZE_CURSOR:
            return RESIZE_EAST;
        case java.awt.Cursor.SE_RESIZE_CURSOR:
            return RESIZE_SOUTH_EAST;
        case java.awt.Cursor.S_RESIZE_CURSOR:
            return RESIZE_SOUTH;
        case java.awt.Cursor.SW_RESIZE_CURSOR:
            return RESIZE_SOUTH_WEST;
        case java.awt.Cursor.W_RESIZE_CURSOR:
            return RESIZE_WEST;
        case java.awt.Cursor.NW_RESIZE_CURSOR:
            return RESIZE_NORTH_WEST;
        }
    }

    public static java.awt.Cursor getAWTCursor(CursorMode cursorMode) {
        switch (cursorMode) {
        case DEFAULT:
        default:
            return new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR);
        case CROSS:
            return new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR);
        case HAND:
            return new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR);
        case MOVE:
            return new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR);
        case TEXT:
            return new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR);
        case WAIT:
            return new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR);
        case RESIZE_NORTH:
            return new java.awt.Cursor(java.awt.Cursor.N_RESIZE_CURSOR);
        case RESIZE_NORTH_EAST:
            return new java.awt.Cursor(java.awt.Cursor.NE_RESIZE_CURSOR);
        case RESIZE_EAST:
            return new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR);
        case RESIZE_SOUTH_EAST:
            return new java.awt.Cursor(java.awt.Cursor.SE_RESIZE_CURSOR);
        case RESIZE_SOUTH:
            return new java.awt.Cursor(java.awt.Cursor.S_RESIZE_CURSOR);
        case RESIZE_SOUTH_WEST:
            return new java.awt.Cursor(java.awt.Cursor.SW_RESIZE_CURSOR);
        case RESIZE_WEST:
            return new java.awt.Cursor(java.awt.Cursor.W_RESIZE_CURSOR);
        case RESIZE_NORTH_WEST:
            return new java.awt.Cursor(java.awt.Cursor.NW_RESIZE_CURSOR);
        case NONE:
            return Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), "");
        }
    }
}
