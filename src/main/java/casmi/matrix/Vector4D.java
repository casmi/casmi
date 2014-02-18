package casmi.matrix;

public class Vector4D {
    
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;
    private double w = 0.0;

    public Vector4D() {
        this.set(0, 0, 0, 0);
    }
    
    public Vector4D(double x) {
        this.set(x, x, x, x);
    }
    
    public Vector4D(double x, double y, double z, double w) {
        this.set(x, y, z, w);
    }
    
    public Vector4D(Vector2D v, double z, double w) {
        this.set(v.getX(), v.getY(), z, w);
    }
    
    public Vector4D(Vector3D v, double w) {
        this.set(v.getX(), v.getY(), v.getZ(), w);
    }
    
    public Vector4D(Vector4D v) {
        this.set(v);
    }

    public Vector4D(double[] array) {
        this.set(array);
    }
    
    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }
    
    public void setZ(double z) {
        this.z = z;
    }
    
    public double getW() {
        return this.w;
    }
    
    public void setW(double w) {
        this.w = w;
    }
    
    public double[] getArray() {
        double[] result = new double[4];
        result[0] = this.x;
        result[1] = this.y;
        result[2] = this.z;
        result[3] = this.w;
        return result;
    }
    
    public void set(double[] array) {
        if(array.length >= 1) {
            this.x = array[0];
        }
        if(array.length >= 2) {
            this.y = array[1];
        }
        if(array.length >= 3) {
            this.z = array[2];
        }
        if(array.length >= 4) {
            this.w = array[3];
        }
        if(array.length > 4) {
            System.err.println("Input array length exceeds target dimension.");
        }
        return;
    }
    
    public void set(double x) {
        this.setX(x);
    }

    public void set(double x, double y){
        this.set(x);
        this.setY(y);
    }

    public void set(double x, double y, double z) {
        this.set(x, y);
        this.setZ(z);
    }

    public void set(double x, double y, double z, double w) {
        this.set(x, y, z);
        this.setW(w);
    }

    public void set(Vector4D v) {
        this.set(v.x, v.y, v.z, v.w);
    }
    
    public Vector4D mult(Vector4D a) {
        Vector4D result;
        result = new Vector4D(a.x*this.x, a.y*this.y, a.z*this.z, a.w*this.w);
        return result;
    }

    public Vector4D par(Vector4D a) {
        Vector4D result;
        result = new Vector4D(this.x/a.x, this.y/a.y, this.z/a.z, this.w/a.w);
        return result;
    }

    public Vector4D mult(double b) {
        Vector4D result;
        result = new Vector4D(this.x*b, this.y*b, this.z*b, this.w*b);
        return result;
    }

    public Vector4D par(double b) {
        Vector4D result;
        result = new Vector4D(this.x/b,this.y/b, this.z/b, this.w/b);
        return result;
    }

    public Vector4D add(double b) {
        Vector4D result;
        result = new Vector4D(this.x+b, this.y+b, this.z+b, this.w+b);
        return result;
    }

    public Vector4D add(Vector4D v) {
        Vector4D result;
        result = new Vector4D(this.x+v.x, this.y+v.y, this.z+v.z, this.w+v.w);
        return result;
    }

    public Vector4D sub(Vector4D v) {
        Vector4D result = new Vector4D(this.x-v.x, this.y-v.y, this.z-v.z, this.w-v.w);
        return result;
    }
    
    public double sum() {
        return this.x + this.y + this.z + this.w;
    }
    
    public double lengthSquared() {
        return this.x*this.x + this.y*this.y + this.z*this.z + this.w*this.w;
    }

    public double norm() {
        return Math.sqrt(this.lengthSquared());
    }

    public Vector4D normalized() {
        double norm = this.norm();
        return this.par(norm);
    }
    
    public void normalize() {
        this.set(this.normalized());
    }
    
    public static double getDistance(Vector4D a, Vector4D b) {
        Vector4D sub = a.sub(b);
        return sub.norm();
    }

    public static double dotProduct(Vector4D a, Vector4D b) {
        return a.mult(b).sum();
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w +")";
    }
}
