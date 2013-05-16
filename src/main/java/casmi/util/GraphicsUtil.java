/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2012, Xcoo, Inc.
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

package casmi.util;

import java.util.List;

import casmi.graphics.element.Element;

/**
 * Graphics utility class for easy to use casmi.
 *
 * @author T. Takeuchi
 */
public class GraphicsUtil {

    /**
     * Remove all elements in the specified list from casmi.Applet,
     * casmi.graphics.object.GraphicsObject, or casmi.graphics.group.Group.
     *
     * @param list
     *            List of objects extends Element.
     */
    public static void removeAll(List<? extends Element> list) {
        for (Element e : list) {
            e.remove();
        }
    }
}
