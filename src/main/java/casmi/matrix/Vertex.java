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
 
package casmi.matrix;

import java.util.Vector;

/**
 * @author Y. Ban
 */
public class Vertex {

    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;

    public Vertex() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }
    
    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0.0;
    }

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    
    public void set(double x, double y, double z){
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }
    
    public void set(double x, double y){
    	this.x = x;
    	this.y = y;
    }
    
    public Vertex mult(Vertex a) {
    	Vertex result;
    	result = new Vertex(a.x*this.x,a.y*this.y);
    	return result;
    }
    
    public Vertex par(Vertex a) {
    	Vertex result;
    	result = new Vertex(this.x/a.x,this.y/a.y);
    	return result;
    }
    
    public Vertex mult(double b) {
    	Vertex result;
    	result = new Vertex(this.x*b,this.y*b);
    	return result; 	
    }
    
    public Vertex par(double a) {
    	Vertex result;
    	result = new Vertex(this.x/a,this.y/a);
    	return result;
    }
    
    public Vertex add(double num) {
    	Vertex result;
    	result = new Vertex(this.x+num, this.y+num);
    	return result;
    }
    
    public Vertex add(Vertex v) {
    	Vertex result;
    	result = new Vertex(this.x+v.x,this.y+v.y);
    	return result;
    }
    
    public double lengthSquared() {
    	return this.x*this.x+this.y*this.y;
    }
}
