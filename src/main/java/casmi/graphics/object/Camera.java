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

import casmi.graphics.Graphics;

/**
 * Camera class.
 * Wrap JOGL and make it easy to use. This class is similar to gluLookAt() in OpenGL
 *
 * @author Y. Ban
 */
public class Camera {

    private double eyeX;
    private double eyeY;
    private double eyeZ;

    private double centerX;
    private double centerY;
    private double centerZ;

    private double upX;
    private double upY;
    private double upZ;

    private boolean def = false;

    /**
     * Creates Camera object setting the eye position, the center of the scene and which axis is facing upward.
     *
     * @param eyeX
     *                     The x-coordinate for the eye.
     * @param eyeY
     *                     The y-coordinate for the eye.
     * @param eyeZ
     *                     The z-coordinate for the eye.
     * @param centerX
     *                     The x-coordinate for the center of the scene.
     * @param centerY
     *                     The y-coordinate for the center of the scene.
     * @param centerZ
     *                     The z-coordinate for the center of the scene.
     * @param upX
     *                     Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
     * @param upY
     *                     Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
     * @param upZ
     *                     Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
     */
    public Camera(double eyeX,    double eyeY,    double eyeZ,
        double centerX, double centerY, double centerZ,
        double upX,     double upY,     double upZ) {

        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;
    }

    /**
     * Creates Camera object using default parameter.
     * The default values are camera(width/2.0, height/2.0,
     *  (height/2.0) / tan(PI*60.0 / 360.0), width/2.0, height/2.0, 0, 0, 1, 0)
     */
    public Camera() {
        def = true;
    }

    public void render(Graphics g) {
        if (def) {
            g.setCamera();
        } else {
            g.setCamera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }
    }

    /**
     * Sets the eye position, the center of the scene and which axis is facing upward.
     *
     * @param eyeX
     *                     The x-coordinate for the eye.
     * @param eyeY
     *                     The y-coordinate for the eye.
     * @param eyeZ
     *                     The z-coordinate for the eye.
     * @param centerX
     *                     The x-coordinate for the center of the scene.
     * @param centerY
     *                     The y-coordinate for the center of the scene.
     * @param centerZ
     *                     The z-coordinate for the center of the scene.
     * @param upX
     *                     Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
     * @param upY
     *                     Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
     * @param upZ
     *                     Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
     */
    public void set(double eyeX,    double eyeY,    double eyeZ,
        double centerX,    double centerY, double centerZ,
        double upX,     double upY,     double upZ) {

        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;
    }

    /**
     * Sets the eye position of this Camera.
     *
     * @param eyeX
     *                     The x-coordinate for the eye.
     * @param eyeY
     *                     The y-coordinate for the eye.
     * @param eyeZ
     *                     The z-coordinate for the eye.
     */
    public void setEye(double eyeX, double eyeY, double eyeZ) {
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;
    }

    /**
     * Sets the center of the scene of this Camera.
     *
     * @param centerX
     *                     The x-coordinate for the center of the scene.
     * @param centerY
     *                     The y-coordinate for the center of the scene.
     * @param centerZ
     *                     The z-coordinate for the center of the scene.
     */
    public void setCenter(double centerX, double centerY, double centerZ) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
    }

    /**
     * Sets which axis is facing upward.
     *
     * @param upX
     *                     Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
     * @param upY
     *                     Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
     * @param upZ
     *                     Setting which axis is facing upward; usually 0.0, 1.0, or -1.0.
     */
    public void setOrientation(double upX, double upY, double upZ) {
        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;
    }

    /**
     * Returns the x-coordinate of the eye position of this Camera.
     *
     * @return
     *                     The x-coordinate for the eye.
     */
    public double getEyeX() {
        return eyeX;
    }

    /**
     * Sets the x-coordinate of the eye position of this Camera.
     *
     * @param eyeX
     *                     The x-coordinate for the eye.
     */
    public void setEyeX(double eyeX) {
        this.eyeX = eyeX;
    }

    /**
     * Returns the y-coordinate of the eye position of this Camera.
     *
     * @return
     *                     The y-coordinate for the eye.
     */
    public double getEyeY() {
        return eyeY;
    }

    /**
     * Sets the y-coordinate of the eye position of this Camera.
     *
     * @param eyeY
     *                     The y-coordinate for the eye.
     */
    public void setEyeY(double eyeY) {
        this.eyeY = eyeY;
    }

    /**
     * Returns the z-coordinate of the eye position of this Camera.
     *
     * @return
     *                     The z-coordinate for the eye.
     */
    public double getEyeZ() {
        return eyeZ;
    }

    /**
     * Sets the z-coordinate of the eye position of this Camera.
     *
     * @param eyeZ
     *                     The z-coordinate for the eye.
     */
    public void setEyeZ(double eyeZ) {
        this.eyeZ = eyeZ;
    }

    /**
     * Returns the center of the scene of this Camera.
     *
     * @return
     *                     The X-coordinate for the center of the scene.
     */
    public double getCenterX() {
        return centerX;
    }

    /**
     * Sets the center of the scene of this Camera.
     *
     * @param centerX
     *                     The X-coordinate for the center of the scene.
     */
    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    /**
     * Returns the center of the scene of this Camera.
     *
     * @return
     *                     The Y-coordinate for the center of the scene.
     */
    public double getCenterY() {
        return centerY;
    }

    /**
     * Sets the center of the scene of this Camera.
     *
     * @param centerY
     *                     The Y-coordinate for the center of the scene.
     */
    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    /**
     * Returns the center of the scene of this Camera.
     *
     * @return
     *                     The Z-coordinate for the center of the scene.
     */
    public double getCenterZ() {
        return centerZ;
    }

    /**
     * Sets the center of the scene of this Camera.
     *
     * @param centerZ
     *                     The Z-coordinate for the center of the scene.
     */
    public void setCenterZ(double centerZ) {
        this.centerZ = centerZ;
    }

    public double getUpX() {
        return this.upX;
    }

    public double getUpY() {
        return this.upY;
    }

    public double getUpZ() {
        return this.upZ;
    }

    /**
     * Gets the eye direction of this Camera.
     *
     * @return
     *                     The eye direction.
     */
    public double[] getCameraDirection() {
        double[] cameraDirection = new double[3];
        double l = Math.sqrt(Math.pow(this.eyeX - this.centerX, 2.0) + Math.pow(this.eyeZ - this.centerZ, 2.0) + Math.pow(this.eyeZ - this.centerZ, 2.0));
        cameraDirection[0] = (this.centerX - this.eyeX) / l;
        cameraDirection[1] = (this.centerY - this.eyeY) / l;
        cameraDirection[2] = (this.centerZ - this.eyeZ) / l;
        return cameraDirection;
    }

    public double[] getViewMatrix() {
        return getViewMatrix(this.eyeX, this.eyeY, this.eyeZ, this.centerX, this.centerY, this.centerZ, this.upX, this.upY, this.upZ);
    }

    public static final double[] getViewMatrix(double ex, double ey, double ez, double tx, double ty, double tz,
        double ux, double uy, double uz) {
        double[] matrix = new double[16];
        double l;

        // z axis = e - t
        tx = ex - tx;
        ty = ey - ty;
        tz = ez - tz;
        l = Math.sqrt(tx * tx + ty * ty + tz * tz);
        matrix[2] = tx / l;
        matrix[6] = ty / l;
        matrix[10] = tz / l;

        // x axis = u X z
        tx = uy * matrix[10] - uz * matrix[ 6];
        ty = uz * matrix[ 2] - ux * matrix[10];
        tz = ux * matrix[ 6] - uy * matrix[ 2];
        l = Math.sqrt(tx * tx + ty * ty + tz * tz);
        matrix[ 0] = tx / l;
        matrix[ 4] = ty / l;
        matrix[ 8] = tz / l;

        //y axis = z X x
        matrix[ 1] = matrix[ 6] * matrix[ 8] - matrix[10] * matrix[ 4];
        matrix[ 5] = matrix[10] * matrix[ 0] - matrix[ 2] * matrix[ 8];
        matrix[ 9] = matrix[ 2] * matrix[ 4] - matrix[ 6] * matrix[ 0];

        matrix[12] = -(ex * matrix[ 0] + ey * matrix[ 4] + ez * matrix[ 8]);
        matrix[13] = -(ex * matrix[ 1] + ey * matrix[ 5] + ez * matrix[ 9]);
        matrix[14] = -(ex * matrix[ 2] + ey * matrix[ 6] + ez * matrix[10]);

        matrix[ 3] = matrix[ 7] = matrix[11] = 0.0;
        matrix[15] = 1.0;
        return matrix;
    }
}
