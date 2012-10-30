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

package casmi.timeline;

/**
 * Modes of dissolving in timeline.
 *  
 * @author Y. Ban
 */
public enum DissolveMode {
    
    /** Normal dissolve. */
    BLACK,
    
    /** Cross dissolve. */
    CROSS, 
    
    /** Slide to left dissolve  */
    SLIDE_LEFT,
    
    /** Slide to right dissolve  */
    SLIDE_RIGHT,

    /** Slide to top dissolve  */
    SLIDE_TOP,

    /** Slide to bottom dissolve  */
    SLIDE_BOTTOM,
    
    /** Change from left dissolve  */
    CURTAIN_LEFT,
    
    /** Change from right dissolve  */
    CURTAIN_RIGHT,
    
    /** Change from top dissolve  */
    CURTAIN_TOP,
    
    /** Change from bottom dissolve  */
    CURTAIN_BOTTOM,
    
}
