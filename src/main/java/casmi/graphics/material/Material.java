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

import javax.media.opengl.GL;

/**
 * Material class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class Material {
    
    private float shininess[] = {1.0f};
    private float ambient[] = {0,0,0,255};
    private float diffuse[] = {0,0,0,255};
    private float specular[] = {0,0,0,255};
    private float emissive[] = {0,0,0,255};
    private Boolean Sh = false;
    private Boolean Am = false;
    private Boolean Di = false;
    private Boolean Sp = false;
    private Boolean Em = false;
    
    public Material(){
        
    }
    
    
    public void shininess(float s){
        shininess[0] = s;
        Sh = true;
    }
    
    public void shininess(float s[]){
        shininess[0] = s[0];
        Sh = true;
    }
    
    public void ambient(float a[]){
        for(int i = 0; i<a.length;i++)
            ambient[i]=a[i];
        Am = true;
    }
    
    public void ambient(float r, float g, float b){
        ambient[0]=r;
        ambient[1]=g;
        ambient[2]=b;
        Am = true;
    }
    
    public void ambient(float gray){
        ambient[0]=gray;
        ambient[1]=gray;
        ambient[2]=gray;
        Am = true;
    }
    
    public void diffuse(float d[]){
        for(int i = 0; i<d.length;i++)
            diffuse[i]=d[i];
        Di = true;
    }
    
    public void diffuse(float r, float g, float b){
        diffuse[0] = r;
        diffuse[1] = g;
        diffuse[2] = b;
        Di = true;
    }
    
    public void diffuse(float gray){
        diffuse[0] = gray;
        diffuse[1] = gray;
        diffuse[2] = gray;
        Di = true;
    }
    
    public void specular(float s[]){
        for(int i = 0; i<s.length;i++)
            diffuse[i]=s[i];
        Sp = true;
    }
    
    public void specular(float r, float g, float b){
        specular[0] = r;
        specular[1] = g;
        specular[2] = b;
        Sp = true;
    }
    
    public void specular(float gray){
        specular[0] = gray;
        specular[1] = gray;
        specular[2] = gray;
        Sp = true;
    }
    
    public void emissive(float e[]){
        for(int i = 0; i<e.length;i++)
            emissive[i]=e[i];
        Em = true;
    }
    
    public void emissive(float r, float g, float b){
        emissive[0] = r;
        emissive[1] = g;
        emissive[2] = b;
        Em = true;
    }
    
    public void emissive(float gray){
        emissive[0] = gray;
        emissive[1] = gray;
        emissive[2] = gray;
        Em = true;
    }
    
    public float[] normalize(float in[]){
        float out[] = new float[in.length];
        for(int i= 0; i<in.length;i++){
            out[i]=(float)(in[i]/255.0f);
        }
        return out;
    }
    
    
    public void setup(GL gl) {
        if(Sh==true)
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, shininess,0);
        if(Am==true)
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, normalize(ambient),0);
        if(Di==true)
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, normalize(diffuse),0);
        if(Sp==true)
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, normalize(specular),0);
        if(Em==true)
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, normalize(emissive),0);
    }

}
