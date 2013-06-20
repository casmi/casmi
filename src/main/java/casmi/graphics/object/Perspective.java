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

package casmi.graphics.object;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import casmi.graphics.Graphics;
import casmi.graphics.element.Element;

/**
 * Perspective class. Works like glFrustum.
 * Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Perspective extends Element implements ObjectRender,Projection {

    private double fov;
    private double aspect;
    private double zNear;
    private double zFar;
    private boolean def = false;

    /**
     * Creates a new Perspective object applying foreshortening,
     *  making distant objects appear smaller than closer ones.
     *
     * @param fov
     *                     The field-of-view angle (in radians) for vertical direction.
     * @param aspect
     *                     The ratio of width to height.
     * @param zNear
     *                     The z-position of nearest clipping plane.
     * @param zFar
     *                     The z-position of nearest farthest plane.
     */
    public Perspective(double fov, double aspect, double zNear, double zFar) {
        this.fov = fov;
        this.aspect = aspect;
        this.zNear = zNear;
        this.zFar = zFar;
    }

    /**
     * Creates a Perspective object with default values.
     * The default values are:
     * Perspective(PI/3.0, width/height, cameraZ/10.0, cameraZ*10.0)
     * where cameraZ is ((height/2.0) / tan(PI*60.0/360.0))
     *
     */
    public Perspective() {
        def = true;
    }

    /**
     * Sets a perspective projection applying foreshortening,
     *  making distant objects appear smaller than closer ones.
     *
     * @param fov
     *                     The field-of-view angle (in radians) for vertical direction.
     * @param aspect
     *                     The ratio of width to height.
     * @param zNear
     *                     The z-position of nearest clipping plane.
     * @param zFar
     *                     The z-position of nearest farthest plane.
     */
    public void set(double fov, double aspect, double zNear, double zFar) {
        this.fov = fov;
        this.aspect = aspect;
        this.zNear = zNear;
        this.zFar = zFar;
    }

    @Override
    public void render(Graphics g) {
        if (def) {
            double cameraZ = ((g.getHeight() / 2.0) / Math.tan(Math.PI * 60.0 / 360.0));
            g.perspective(Math.PI / 3.0, (double) g.getWidth()
                    / (double) g.getHeight(), cameraZ / 10.0, cameraZ * 10.0);
        } else {
            g.perspective(fov, aspect, zNear, zFar);
        }
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height) {}

    @Override
    public void simplerender(Graphics g) {
        if (def) {
            g.simpleperspective();
        } else {
            g.simpleperspective(fov, aspect, zNear, zFar);
        }
    }
}
