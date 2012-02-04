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

import casmi.matrix.Vertex;

public class Joint {

    protected Vertex position;
    protected double confidence;
    
    public Joint() {
        this(new Vertex(), 0.0);
    }
    
    public Joint(Vertex position, double confidence) {
        this.position   = position;
        this.confidence = confidence;
    }
    
    public final Vertex getPosition() {
        return position;
    }
    
    public final void setPosition(Vertex position) {
        this.position = position;
    }

    public final double getConfidence() {
        return confidence;
    }

    public final void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
