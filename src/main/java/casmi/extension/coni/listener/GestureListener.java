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

package casmi.extension.coni.listener;

import casmi.extension.coni.CONI;
import casmi.extension.coni.Gesture;
import casmi.matrix.Vertex;

/**
 * GestureListener interface.
 * <p>
 * Implement if you want to use gesture.
 * 
 * @author T. Takeuchi
 */
public interface GestureListener {

    void gestureRecognized(CONI coni, Gesture gesture, Vertex idPosition, Vertex endPosition);
    
    void gestureProgress(CONI coni, Gesture gesture, Vertex position, float progress);
}
