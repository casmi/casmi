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
  
package casmi.graphics.element;
 
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
 
/**
 * RoundRect class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class RoundRect extends Element implements Renderable {
 
    public enum ShapeMode {
        CORNER, CORNERS, RADIUS, CENTER
    };
 
    private double x;
    private double y;
    private double w;
    private double h;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double x3;
    private double y3;
    private double x4;
    private double y4;
    private double r = 1;
    private double precision = 20.0;
 
    private ShapeMode MODE = ShapeMode.CENTER;
 
 
    private Arc arc[] = new Arc[4];
    
    /**
     * Creates a new RoundRect object using position of the upper-left corner, width and height.
     * 
     * @param x
     *              The x-coordinate of the upper-left corner.
     * @param y
     *              The y-coordinate of the upper-left corner.
     * @param w
     *              The width of the rectangle.
     * @param h 
     *              The height of the rectangle.                          
     */
    public RoundRect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        setRect();
        initArc();
    }
    
    /**
     * Creates a new RoundRect object using width and height.
     * 
 
     * @param w
     *              The width of the rectangle.
     * @param h 
     *              The height of the rectangle.                          
     */
    public RoundRect( double w, double h) {
        this.x = 0;
        this.y = 0;
        this.w = w;
        this.h = h;
        setRect();
        initArc();
    }
    
    /**
     * Creates a new RoundRect object using radius, width and height.
     * 
     * @param r 
     *              The radius of the corner. 
     * @param w
     *              The width of the rectangle.
     * @param h 
     *              The height of the rectangle.                          
     */
    public RoundRect(double r, double w, double h) {
        this.x = 0;
        this.y = 0;
        this.w = w;
        this.h = h;
        this.r = r;
        setRect();
        initArc();
    }
 
    /**
     * Creates a new RoundRect object using radius, position of the upper-left corner, width and height.
     * 
     * @param r 
     *              The radius of the corner. 
     * @param x
     *              The x-coordinate of the upper-left corner.
     * @param y
     *              The y-coordinate of the upper-left corner.
     * @param w
     *              The width of the rectangle.
     * @param h 
     *              The height of the rectangle.                          
     */
    public RoundRect(double r, double x, double y, double w, double h) {
        this.r = r;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        setRect();
        initArc();
    }
 
    private void setArc(){
        arc[0].set(x1 + r, y1 - r,r, 90, 180, precision);
        arc[1].set(x2 + r, y2 + r,r, 180, 270, precision);
        arc[2].set(x3 - r, y3 + r,r, 270, 360, precision);
        arc[3].set(x4 - r, y4 - r,r, 0, 90, precision);
    }
    
    private void initArc(){
        arc[0] = new Arc(x1 + r, y1 - r,r, 90, 180, precision);
        arc[1] = new Arc(x2 + r, y2 + r,r, 180, 270, precision);
        arc[2] = new Arc(x3 - r, y3 + r,r, 270, 360, precision);
        arc[3] = new Arc(x4 - r, y4 - r,r, 0, 90, precision);
    }
 
    public void rectMode(ShapeMode mode) {
        this.MODE = mode;
    }
    
    private void setRect(){
    	  switch (MODE) {
          case CORNER:
              this.x1 = 0.0;
              this.y1 = 0.0;
              this.x2 = 0.0;
              this.y2 = 0.0 - h;
              this.x3 = 0.0 + w;
              this.y3 = 0.0 - h;
              this.x4 = 0.0 + w;
              this.y4 = 0.0;
              break;
          case CORNERS:
              this.x1 = 0.0;
              this.y1 = 0.0;
              this.x2 = 0.0;
              this.y2 = h;
              this.x3 = w;
              this.y3 = h;
              this.x4 = w;
              this.y4 = 0.0;
              break;
          case CENTER:
              this.x1 = 0.0 - w / 2;
              this.y1 = 0.0 + h / 2;
              this.x2 = 0.0 - w / 2;
              this.y2 = 0.0 - h / 2;
              this.x3 = 0.0 + w / 2;
              this.y3 = 0.0 - h / 2;
              this.x4 = 0.0 + w / 2;
              this.y4 = 0.0 + h / 2;
              break;
          case RADIUS:
              this.x1 = 0.0 - w;
              this.y1 = 0.0 + h;
              this.x2 = 0.0 - w;
              this.y2 = 0.0 - h;
              this.x3 = 0.0 + w;
              this.y3 = 0.0 - h;
              this.x4 = 0.0 + w;
              this.y4 = 0.0 + h;
              break;
 
          }
    }
 
    private void calcRect() {
    	setRect();
        setArc();
 
    }
 
    @Override
    public void render(GL gl, GLU glu, int width, int height) {
        calcRect();
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);
        gl.glRotated(this.rotate, 0, 0, 1.0);
        this.setTweenParameter(gl);
        if (this.fill) {
            getSceneFillColor().setup(gl);
            //this.fillColor.setup(gl);
            gl.glBegin(GL.GL_QUADS);
            gl.glVertex2d(x1 + r, y1);
            gl.glVertex2d(x2 + r, y2);
            gl.glVertex2d(x3 - r, y3);
            gl.glVertex2d(x4 - r, y4);
            gl.glEnd();
            gl.glBegin(GL.GL_QUADS);
            gl.glVertex2d(x1, y1 - r);
            gl.glVertex2d(x2, y2 + r);
            gl.glVertex2d(x2 + r, y3 + r);
            gl.glVertex2d(x1 + r, y4 - r);
            gl.glEnd();
            gl.glBegin(GL.GL_QUADS);
            gl.glVertex2d(x4 - r, y1 - r);
            gl.glVertex2d(x3 - r, y2 + r);
            gl.glVertex2d(x3, y3 + r);
            gl.glVertex2d(x4, y4 - r);
            
         /*   gl.glVertex2d(x1, y1 + r);
            gl.glVertex2d(x2, y2 - r);
            gl.glVertex2d(x3, y3 - r);
            gl.glVertex2d(x4, y4 + r);
            gl.glEnd();
            gl.glBegin(GL.GL_QUADS);
            gl.glVertex2d(x1 - r, y1);
            gl.glVertex2d(x2 - r, y2);
            gl.glVertex2d(x2, y3);
            gl.glVertex2d(x1, y4);
            gl.glEnd();
            gl.glBegin(GL.GL_QUADS);
            gl.glVertex2d(x4, y1);
            gl.glVertex2d(x3, y2);
            gl.glVertex2d(x3 + r, y3);
            gl.glVertex2d(x4 + r, y4); */
            
            gl.glEnd();
 
 
            for (int i = 0; i < 4; i++) {
                arc[i].setStroke(false);
                arc[i].setFill(true);
                arc[i].setFillColor(this.fillColor);
                arc[i].render(gl, glu, width, height);}            
            }
        
        if(this.stroke){
            gl.glLineWidth(this.strokeWidth);
           // this.strokeColor.setup(gl);
            getSceneStrokeColor().setup(gl);
            gl.glBegin(GL.GL_LINES);
            gl.glVertex2d(x1, y1-r);
            gl.glVertex2d(x2, y2+r);
            gl.glVertex2d(x2+r, y2);
            gl.glVertex2d(x3-r, y3);
            gl.glVertex2d(x3, y3+r);
            gl.glVertex2d(x4, y4-r);
            gl.glVertex2d(x4-r, y4);
            gl.glVertex2d(x1+r, y1);
            
          /*  gl.glVertex2d(x1-r,y1);
            gl.glVertex2d(x2-r, y2);
            gl.glVertex2d(x2, y2-r);
            gl.glVertex2d(x3, y3-r);
            gl.glVertex2d(x3+r, y3);
            gl.glVertex2d(x4+r, y4);
            gl.glVertex2d(x4, y4+r);
            gl.glVertex2d(x1, y1+r); */
            
            gl.glEnd();
            for (int i = 0; i < 4; i++) {
                arc[i].setFill(false);
                arc[i].setStroke(true);
                arc[i].setStrokeWidth(this.strokeWidth);
                arc[i].setStrokeColor(this.strokeColor);
                arc[i].render(gl, glu, width, height);}            
            }
        gl.glPopMatrix();
        if(this.fillColor.getA()!=1||this.strokeColor.getA()!=1)
            gl.glEnable(GL.GL_DEPTH_TEST);
        
        }
    
    public void setRotate(double angle){
    	this.rotate = angle;
    }
    
    public double getRotate(){
    	return this.rotate;
    }
    
    public double getWidth(){
    	return this.w;
    }
    
    public double getHeight(){
    	return this.h;
    }
    
    public double getX(){
    	return this.x;
    }
    
    public double getY(){
    	return this.y;
    }
    
    public void setWidth(double w){
    	this.w = w;
    }
    
    public void setHeight(double h){
    	this.h = h;
    }
    
    public void setX(double x){
    	this.x = x;
    }
    
    public void setY(double y){
    	this.y = y;
    }
    
    
    public void setXY(double x, double y){
    	this.x = x;
    	this.y = y;
    }
    
    public void setRadius(double radius){
    	this.r = radius;
    }
    
    public double getRadius(){
    	return this.r;
    }
    
    public double getPrecision(){
    	return this.precision;
    }
    
    public void setPrecision(double precision){
    	this.precision = precision;
    }
    
 
 
}