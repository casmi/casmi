package casmi.graphics.element;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.matrix.Vertex;

/**
 * MouseOver class.
 * Wrap JOGL and make it easy to use.
 * 
 * @author Y. Ban
 * 
 */
public class MouseOver extends Element implements Renderable {

    public static final int POINT       = 0;
    public static final int POINTS      = 1;
    public static final int POINT_3D = 2;
    public static final int POINTS_3D = 3;

    private Element obj;
 

    /**
     * Creates a new Point object using x,y-coordinate.
     * 
     * @param x
     *            The x-coordinate of the Point.
     * @param y
     *            The y-coordinate of the Point.
     */
    public MouseOver(Element obj) {
    	this.obj = obj;
    }
    
    public void set(Element obj){
    	this.obj = obj;
    }
    
    public boolean isMouseOver(int x,int y){
    	double rx=x,ry=y;
    	if(obj instanceof Rect){
    		Rect r = (Rect) obj;
    		if(r.getRotate()!=0){
    			rx = Math.cos(-r.getRotate()/180.0*Math.PI)*(x-r.getX()) - Math.sin(-r.getRotate()/180.0*Math.PI)*(y-r.getY())+r.getX();
    			ry = Math.sin(-r.getRotate()/180.0*Math.PI)*(x-r.getX())+ Math.cos(-r.getRotate()/180.0*Math.PI)*(y-r.getY())+r.getY();
    		}
    		return calcRectOver(r,rx-r.tX,ry-r.tY);
    	}
    	if(obj instanceof Ellipse){
    		Ellipse el = (Ellipse) obj;
    		if(el.getRotate()!=0){
    			rx = Math.cos(-el.getRotate()/180.0*Math.PI)*(x-el.getX()) - Math.sin(-el.getRotate()/180.0*Math.PI)*(y-el.getY())+el.getX();
    			ry = Math.sin(-el.getRotate()/180.0*Math.PI)*(x-el.getX())+ Math.cos(-el.getRotate()/180.0*Math.PI)*(y-el.getY())+el.getY();
    		}
    		return calcEllipseOver(el,rx-el.tX,ry-el.tY);
    	}
    	if(obj instanceof Arc){
    		Arc ac = (Arc) obj;
    		if(ac.getRotate()!=0){
    			rx = Math.cos(-ac.getRotate()/180.0*Math.PI)*(x-ac.getX()) - Math.sin(-ac.getRotate()/180.0*Math.PI)*(y-ac.getY())+ac.getX();
    			ry = Math.sin(-ac.getRotate()/180.0*Math.PI)*(x-ac.getX())+ Math.cos(-ac.getRotate()/180.0*Math.PI)*(y-ac.getY())+ac.getY();
    		}
    		return calcArcOver(ac,rx-ac.tX,ry-ac.tY);
    	}
    	if(obj instanceof Triangle){
    		Triangle tr = (Triangle) obj;
    		return calcTriangleOver(tr,rx-tr.tX,ry-tr.tY);
    	}
    	if(obj instanceof Quad){
    		Quad qd = (Quad) obj;
    		return calcQuadOver(qd,rx-qd.tX,ry-qd.tY);
    	}
    	if(obj instanceof RoundRect){
    		RoundRect rr = (RoundRect) obj;
    		if(rr.getRotate()!=0){
    			rx = Math.cos(-rr.getRotate()/180.0*Math.PI)*(x-rr.getX()) - Math.sin(-rr.getRotate()/180.0*Math.PI)*(y-rr.getY())+rr.getX();
    			ry = Math.sin(-rr.getRotate()/180.0*Math.PI)*(x-rr.getX())+ Math.cos(-rr.getRotate()/180.0*Math.PI)*(y-rr.getY())+rr.getY();
    		}
    		return calcRoundrectOver(rr,rx-rr.tX,ry-rr.tY);
    	}
    	if(obj instanceof TextBox){
    		TextBox tb = (TextBox) obj;
    		return calcTextBox(tb, rx - tb.tX, ry - tb.tY); 
    	}
    	else return false;
    }
    
    
    private boolean calcRectOver(Rect r, double x, double y){
    	if((r.getX()-r.getWidth()/2) <= x && (r.getX()+r.getWidth()/2) >= x
				&& (r.getY()-r.getHeight()/2) <= y && (r.getY()+r.getHeight()/2) >= y ){
			return true;
		}
    	else
    		return false;
    }
    
    private boolean calcEllipseOver(Ellipse el, double x, double y){
    	if((el.getX()-el.getWidth()/2) <= x && (el.getX()+el.getWidth()/2) >= x ){
    		double tmp = Math.sqrt(Math.pow((el.getWidth()/2), 2.0) - Math.pow((el.getX()-x), 2.0)); 
    		if(tmp*(el.getHeight()/el.getWidth()) >= (y - el.getY()) && -1*tmp*(el.getHeight()/el.getWidth()) <= (y - el.getY())){
    			return true;
    		}
    	} 	
    	return false;
    }
    
    private boolean calcArcOver(Arc ac, double x, double y){
    	if((ac.getX()-ac.getRotate()/2) <= x && (ac.getX()+ac.getWidth()/2) >= x ){
    		double tmpangle = Math.atan((y-ac.getY())/(x-ac.getX()))*180/Math.PI; 
    		if(tmpangle >= ac.getStart() && tmpangle <= ac.getEnd()){
    			double tmp = Math.sqrt(Math.pow((ac.getWidth()/2), 2.0) - Math.pow((ac.getX()-x), 2.0)); 
    			if(tmp*(ac.getHeight()/ac.getWidth()) >= (y - ac.getY()) && -1*tmp*(ac.getHeight()/ac.getWidth()) <= (y - ac.getY())){
    				return true;
    			}
    		}
    	} 	
    	return false;
    }
    
    private boolean calcRoundrectOver(RoundRect rr, double x, double y){
    	if((rr.getX()-rr.getWidth()/2) <= x && (rr.getX()+rr.getWidth()/2) >= x
				&& (rr.getY()-rr.getHeight()/2) <= y && (rr.getY()+rr.getHeight()/2) >= y ){
			if(x >= (rr.getX()-rr.getWidth()/2) && (x <= rr.getX()-rr.getWidth()/2+rr.getRadius()) &&
					y <= (rr.getY()+rr.getHeight()/2) && y >= (rr.getY()+rr.getHeight()/2-rr.getRadius())){
				if(Math.pow((x - (rr.getX()-rr.getWidth()/2+rr.getRadius())),2.0)
						+Math.pow((y - (rr.getY()+rr.getHeight()/2-rr.getRadius())),2.0)<Math.pow(rr.getRadius(), 2.0))
					return true;
				else
					return false;
			}
			else if(x >= (rr.getX()-rr.getWidth()/2) && (x <= rr.getX()-rr.getWidth()/2+rr.getRadius()) &&
					y >= (rr.getY()-rr.getHeight()/2) && y <= (rr.getY()-rr.getHeight()/2+rr.getRadius())){
				if(Math.pow((x - (rr.getX()-rr.getWidth()/2+rr.getRadius())),2.0)
						+Math.pow((y - (rr.getY()-rr.getHeight()/2+rr.getRadius())),2.0)<Math.pow(rr.getRadius(), 2.0))
					return true;
				else
					return false;
			}
			else if(x <= (rr.getX()+rr.getWidth()/2) && (x >= rr.getX()+rr.getWidth()/2-rr.getRadius()) &&
					y >= (rr.getY()-rr.getHeight()/2) && y <= (rr.getY()-rr.getHeight()/2+rr.getRadius())){
				if(Math.pow((x - (rr.getX()+rr.getWidth()/2-rr.getRadius())),2.0)
						+Math.pow((y - (rr.getY()-rr.getHeight()/2+rr.getRadius())),2.0)<Math.pow(rr.getRadius(), 2.0))
					return true;
				else
					return false;
			}
			else if(x <= (rr.getX()+rr.getWidth()/2) && (x >= rr.getX()+rr.getWidth()/2-rr.getRadius()) &&
					y <= (rr.getY()+rr.getHeight()/2) && y >= (rr.getY()+rr.getHeight()/2-rr.getRadius())){
				if(Math.pow((x - (rr.getX()+rr.getWidth()/2-rr.getRadius())),2.0)
						+Math.pow((y - (rr.getY()+rr.getHeight()/2-rr.getRadius())),2.0)<Math.pow(rr.getRadius(), 2.0))
					return true;
				else
					return false;
			} else
				return true;
		}
    	else
    		return false;
    }
    
    private boolean calcTriangleOver(Triangle tr, double x, double y){
    	Vertex v[] = new Vertex[3]; 
    	double maxy = 0, miny = 10000;
    	int maxnum = 0,minnum = 0;
    	for(int i=0; i<3; i++){
    		if(tr.getConer(i).y >= maxy){
    			maxy = tr.getConer(i).y;
    			maxnum = i;
    		}
    		if(tr.getConer(i).y <= miny){
    			miny = tr.getConer(i).y;
    			minnum = i;
    		}
    	}
    	for(int i=0;i<3;i++){
    		if(i == maxnum)
    			v[0] = tr.getConer(i);
    		else if(i == minnum)
    			v[2] = tr.getConer(i);
    		else
    			v[1] = tr.getConer(i);
    	}
    	
    	
    	if(v[1].x>=v[2].x){
    		if(v[0].y >= y && v[1].y <= y){
    			if(x >= calcPoint(v[0],v[2],y) && x <= calcPoint(v[0],v[1],y))
    				return true;
    		} else if(v[1].y > y && v[2].y <= y){
    			if(x >= calcPoint(v[0],v[2],y) && x <= calcPoint(v[1],v[2],y))
    				return true;
    		}
    	}
    	else{
    		if(v[0].y >= y && v[1].y <= y){
    			if(x <= calcPoint(v[0],v[2],y) && x >= calcPoint(v[0],v[1],y))
    				return true;
    		} else if(v[1].y > y && v[2].y <= y){
    			if(x <= calcPoint(v[0],v[2],y) && x >= calcPoint(v[1],v[2],y))
    				return true;
    		}
    	}
    			 	
    	return false;
    }
    
    private boolean calcQuadOver(Quad qd, double x, double y){
    	Vertex v[] = new Vertex[4]; 
    	
    	for(int i=0;i<4;i++){
    		v[i] = new Vertex(qd.getConer(i).x,qd.getConer(i).y);    		
    	}
    	
    	Vertex tmp = new Vertex(); 
    	int i,j;
    	for(i = 0; i < 3; i++){
    		for(j = 3; j > i; j--){
    			if(v[j-1].y < v[j].y){
    				tmp = v[j];
    				v[j] = v[j-1];
    				v[j-1]=tmp;
    			}
    		}
    	}
    	
    	
    	   	
    	if(v[1].x>=v[2].x){
    		if(v[0].y >= y && v[1].y <= y){
    			//System.out.println("tste0");
    			if(x >= calcPoint(v[0],v[2],y) && x <= calcPoint(v[0],v[1],y))
    				return true;
    		} else if(v[1].y > y && v[2].y <= y){
    		//	System.out.println("tste1");
    			if(x >= calcPoint(v[0],v[2],y) && x <= calcPoint(v[1],v[3],y))
    				return true;
    		} else if(v[2].y > y && v[3].y <= y){
    			//System.out.println("tste2");
    			if(x >= calcPoint(v[2],v[3],y) && x <= calcPoint(v[1],v[3],y))
    				return true;
    		}
    	}
    	else{
    		if(v[0].y >= y && v[1].y <= y){
    			if(x >= calcPoint(v[0],v[1],y) && x <= calcPoint(v[0],v[2],y))
    				return true;
    		} else if(v[1].y > y && v[2].y <= y){
    			if(x >= calcPoint(v[1],v[3],y) && x <= calcPoint(v[0],v[2],y))
    				return true;
    		} else if(v[2].y > y && v[3].y <= y){
    			if(x >= calcPoint(v[1],v[3],y) && x <= calcPoint(v[2],v[3],y))
    				return true;
    		}
    	}
    			 	
    	return false;
    }
    
    private boolean calcTextBox(TextBox tb, double x, double y){
    	if((tb.getX()-tb.getWidth()/2) <= x && (tb.getX()+tb.getWidth()/2) >= x
				&& (tb.getY()-tb.getHeight()/2) <= y && (tb.getY()+tb.getHeight()/2) >= y ){
			return true;
		}
    	else
    		return false;
    }
    
    private double calcPoint(Vertex v0, Vertex v1, double y){
    	double x = 0;
    	x =  (v0.x - v1.x)*(y - v1.y)/(v0.y - v1.y) + v1.x;
    	return x;
    }

    @Override
    public void render(GL gl, GLU glu, int width, int height) {
    		obj.render(gl, glu, width, height);
    }

}
