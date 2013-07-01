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

package casmi.graphics.material;

import javax.media.opengl.GL2;

/**
 * Material class.
 * Wrap JOGL and make it easy to use.
 *
 * @author Y. Ban
 *
 */
public class Material {

    private float shininess = 1.0f;
    private float ambient[] = {0,0,0,1.0f};
    private float diffuse[] = {0,0,0,1.0f};
    private float specular[] = {0,0,0,1.0f};
    private float emissive[] = {0,0,0,1.0f};
    private Boolean Sh = false;
    private Boolean Am = false;
    private Boolean Di = false;
    private Boolean Sp = false;
    private Boolean Em = false;

    /**
     * Creates Material object.
     */
    public Material(){

    }

    /**
     * Sets the amount of gloss in the surface of shapes.
     *
     * @param shininess
     *                 The amount of gloss in the surface of shapes of this Material.
     */
    public void setShininess(float shininess){
        this.shininess = shininess;
        Sh = true;
    }

    /**
     * Sets the ambient reflectance for shapes drawn to the screen.
     *
     * @param ambient
     *                 The ambient reflectance of this Material.
     */
    public void setAmbient(float ambient[]){
        for(int i = 0; i<ambient.length;i++)
            this.ambient[i]=ambient[i];
        Am = true;
    }

    /**
     * Sets the ambient reflectance for shapes drawn to the screen.
     *
     * @param red
     *                 The red color of the ambient reflectance.
     * @param green
     *                 The green color of the ambient reflectance.
     * @param blue
     *                 The blue color of the ambient reflectance.
     */
    public void setAmbient(float red, float green, float blue){
        ambient[0]=red;
        ambient[1]=green;
        ambient[2]=blue;
        Am = true;
    }

    /**
     * Sets the ambient reflectance for shapes drawn to the screen.
     *
     * @param gray
     *                 The gray-scale value of the ambient reflectance.
     */
    public void setAmbient(float gray){
        ambient[0]=gray;
        ambient[1]=gray;
        ambient[2]=gray;
        Am = true;
    }

    /**
     * Sets the diffuse color of the materials used for shapes drawn to the screen.
     *
     * @param diffuse
     *                 The diffuse color of the materials
     */
    public void setDiffuse(float diffuse[]){
        for(int i = 0; i<diffuse.length;i++)
            this.diffuse[i]=diffuse[i];
        Di = true;
    }

    /**
     * Sets the diffuse color of the materials used for shapes drawn to the screen.
     *
     * @param red
     *                 The red color of the diffuse.
     * @param green
     *                 The green color of the diffuse.
     * @param blue
     *                 The blue color of the diffuse.
     */
    public void setDiffuse(float red, float green, float blue){
        diffuse[0] = red;
        diffuse[1] = green;
        diffuse[2] = blue;
        Di = true;
    }

    /**
     * Sets the diffuse color of the materials used for shapes drawn to the screen.
     *
     * @param gray
     *                 The gray-scale value scale of the diffuse.
     */
    public void setDiffuse(float gray){
        diffuse[0] = gray;
        diffuse[1] = gray;
        diffuse[2] = gray;
        Di = true;
    }

    /**
     * Sets the specular color of the materials used for shapes drawn to the screen.
     *
     * 	@param specular
     * 				The specular color of the Material.
     */
    public void setSpecular(float specular[]){
        for(int i = 0; i<specular.length;i++)
            this.specular[i]=specular[i];
        Sp = true;
    }

    /**
     * Sets the specular color of the materials used for shapes drawn to the screen.
     *
     * @param red
     *                 The red color of the specular.
     * @param green
     *                 The green color of the specular.
     * @param blue
     *                 The blue color of the specular.
     */
    public void setSpecular(float red, float green, float blue){
        specular[0] = red;
        specular[1] = green;
        specular[2] = blue;
        Sp = true;
    }

    /**
     * Sets the specular color of the materials used for shapes drawn to the screen.
     *
     * @param gray
     *                 The gray-scale value of the specular.
     */
    public void setSpecular(float gray){
        specular[0] = gray;
        specular[1] = gray;
        specular[2] = gray;
        Sp = true;
    }

    /**
     * Sets the emissive color of the material used for drawing shapes drawn to the screen.
     *
     * @param emissive
     *                 The emissive color of the Material.
     */
    public void setEmissive(float emissive[]){
        for(int i = 0; i<emissive.length;i++)
            this.emissive[i]=emissive[i];
        Em = true;
    }

    /**
     * Sets the emissive color of the material used for drawing shapes drawn to the screen.
     *
     * @param red
     *                 The red color of the emissive.
     * @param green
     *                 The green color of the emissive.
     * @param blue
     *                 The blue color of the emissive.
     */
    public void setEmissive(float red, float green, float blue){
        emissive[0] = red;
        emissive[1] = green;
        emissive[2] = blue;
        Em = true;
    }

    /**
     *Sets the emissive color of the material used for drawing shapes drawn to the screen.
     *
     *@param gray
     *                The gray-scale value of the emissive.
     */
    public void setEmissive(float gray){
        emissive[0] = gray;
        emissive[1] = gray;
        emissive[2] = gray;
        Em = true;
    }

    public void setup(GL2 gl) {
        if(Sh==true)
            gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess);
        if(Am==true)
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient,0);
        if(Di==true)
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse,0);
        if(Sp==true)
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular,0);
        if(Em==true)
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, emissive,0);
    }
}
