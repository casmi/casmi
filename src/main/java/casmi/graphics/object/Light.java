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
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Element;
import casmi.matrix.Vector3D;

/**
 * Light object class.
 * Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 */
public class Light extends Element implements ObjectRender {

    private Vector3D dv;
    private int index;
    private Vector3D v;
    private Color color;
    private double angle = 30.0;
    private LightMode lightMode = LightMode.NONE;

    private float shininess[] = {1.0f};
    private float ambient[] = {0,0,0,1.0f};
    private float diffuse[] = {0,0,0,1.0f};
    private float specular[] = {0,0,0,1.0f};
    private float emissive[] = {0,0,0,1.0f};

    private boolean Sh = false;
    private boolean Am = false;
    private boolean Di = false;
    private boolean Sp = false;
    private boolean Em = false;

    /**
     * Creates Light object.
     *
     */
    public Light() {
        this(LightMode.NONE);
    }

    /**
     * Creates Light object using lightMode.
     *
     * @param lightMode
     *                     The lightMode of this light.
     *
     * @see casmi.graphics.object.LightMode
     */
    public Light(LightMode lightMode) {
        this.lightMode = lightMode;
        this.v         = new Vector3D();
        this.dv        = new Vector3D();
        this.color     = new RGBColor(0.0, 0.0, 0.0);
    }

    /**
     * Returns the index of Light.
     *
     * @return
     *                     The index of Light.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of Light.
     *
     * @param index
     *                     The index of Light.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Sets the color of Light.
     *
     * @param color
     *                     The color of Light.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the color of Light.
     *
     * @param colorSet
     *                     The ColorSet of Light.
     */
    public void setColor(ColorSet colorSet) {
        this.color = new RGBColor(colorSet);
    }

    /**
     * Sets the direction of Light.
     *
     * @param x
     *                     The x-direction of Light.
     * @param y
     *                     The y-direction of Light.
     * @param z
     *                     The z-direction of Light.
     */
    public void setDirection(double x,double y, double z) {
        this.dv.set(x, y, z);
    }


    /**
     * Returns the LightMode
     *
     * @return
     *                     The LightMode of this Light.
     *
     * @see casmi.graphics.object.LightMode
     */
    public LightMode getLightMode() {
        return lightMode;
    }

    /**
     * Sets the LightMode
     *
     * @param lightMode
     *                     The LightMode of this Light.
     *
     * @see casmi.graphics.object.LightMode
     */
    public void setLightMode(LightMode lightMode) {
        this.lightMode = lightMode;
    }

    @Override
    public void render(Graphics g) {
        v.set(x, y, z);

        float pos[] = {(float) v.getX(),(float) v.getY(),(float) v.getZ(), 1.0f};
        g.getGL().glLightfv(GL2.GL_LIGHT0+index, GL2.GL_POSITION, pos, 0);
        if(Am==true)
            g.getGL().glLightfv(GL2.GL_LIGHT0+index, GL2.GL_AMBIENT, ambient, 0);
        if(Di==true)
            g.getGL().glLightfv(GL2.GL_LIGHT0+index, GL2.GL_DIFFUSE, diffuse, 0);
        if(Sp==true)
            g.getGL().glLightfv(GL2.GL_LIGHT0+index, GL2.GL_SPECULAR,specular, 0);
        if(Em==true)
            g.getGL().glLightfv(GL2.GL_LIGHT0+index, GL2.GL_EMISSION,emissive, 0);
        if(Sh==true)
            g.getGL().glLightfv(GL2.GL_LIGHT0+index, GL2.GL_SHININESS,shininess, 0);

        switch(lightMode){
        case AMBIENT:
            g.ambientLight(index, color, true, v);
            break;
        case DIRECTION:
            g.directionalLight(index, color, true, dv);
            break;
        case POINT:
            g.pointLight(index, color, true, v);
            break;
        case SPOT:
            g.spotLight(index, color, true, v, (float)dv.getX(), (float)dv.getY(), (float)dv.getZ(), (float)angle);
            break;
        case NONE:
            break;

        }
    }

    @Override
    public void render(GL2 gl, GLU glu, int width, int height) {
    }

    /**
     * Returns the x-direction of Light
     *
     * @return
     *                         The x-direction of Light.
     */
    public double getDirectionX() {
        return dv.getX();
    }

    /**
     * Sets the x-direction of Light
     *
     * @param directionX
     *                         The x-direction of Light.
     */
    public void setDirectionX(double directionX) {
        this.dv.setX(directionX);
    }

    /**
     * Returns the x-direction of Light
     *
     * @return
     *                         The x-direction of Light.
     */
    public double getDirectionY() {
        return dv.getY();
    }

    /**
     * Sets the x-direction of Light
     *
     * @param directionY
     *                         The y-direction of Light.
     */
    public void setDirectionY(double directionY) {
        this.dv.setY(directionY);
    }

    /**
     * Returns the z-direction of Light
     *
     * @return
     *                         The z-direction of Light.
     */
    public double getDirectionZ() {
        return dv.getZ();
    }

    /**
     * Sets the z-direction of Light
     *
     * @param directionZ
     *                         The z-direction of Light.
     */
    public void setDirectionZ(double directionZ) {
        this.dv.setZ(directionZ);
    }

    /**
     * Returns the spot light's angle.
     *
     * @return
     *                         The angle of the spot light.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Sets the spot light's angle.
     *
     * @param angle
     *                         The angle of the spot light.
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Returns the shininess of Light.
     *
     * @return
     *                         The shininess of Light.
     */
    public float[] getShininess() {
        return shininess;
    }

    /**
     * Sets the shininess of Light.
     *
     * @param shininess
     *                         The shininess of Light.
     */
    public void setShininess(float shininess[]) {
        this.shininess = shininess;
        Sh = true;
    }

    /**
     * Returns the parameter of the ambient light.
     *
     * @return
     *                         The parameter of the ambient light.
     */
    public float[] getAmbient() {
        return ambient;
    }

    /**
     * Sets the parameter of the ambient light.
     *
     * @param ambient
     *                         The parameter of the ambient light.
     */
    public void setAmbient(float ambient[]) {
        this.ambient = ambient;
        Am = true;
    }

    /**
     * Returns the diffuse color of Light.
     *
     * @return
     *                         The diffuse color of Light.
     */
    public float[] getDiffuse() {
        return diffuse;
    }

    /**
     * Sets the diffuse color of Light.
     *
     * @param diffuse
     *                         The diffuse color of Light.
     */
    public void setDiffuse(float diffuse[]) {
        this.diffuse = diffuse;
        Di = true;
    }

    /**
     * Returns the specular color of Light.
     *
     * @return
     *                         The specular color of Light.
     */
    public float[] getSpecular() {
        return specular;
    }

    /**
     * Sets the specular color of Light.
     *
     * @param specular
     *                         The specular color of Light.
     */
    public void setSpecular(float specular[]) {
        this.specular = specular;
        Sp = true;
    }

    /**
     * Returns the emissive color of Light.
     *
     * @return
     *                         The emissive color of Light.
     */
    public float[] getEmissive() {
        return emissive;
    }

    /**
     * Sets the emissive color of Light.
     *
     * @param emissive
     *                         The emissive color of Light.
     */
    public void setEmissive(float emissive[]) {
        this.emissive = emissive;
        Em = true;
    }
}
