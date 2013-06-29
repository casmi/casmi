package casmi.matrix;


public class Vector2D {

    private double x = 0.0;
    private double y = 0.0;

    public Vector2D() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
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

    public void set(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2D mult(Vector2D a) {
        Vector2D result;
        result = new Vector2D(a.x*this.x, a.y*this.y);
        return result;
    }

    public Vector2D par(Vector2D a) {
        Vector2D result;
        result = new Vector2D(this.x/a.x, this.y/a.y);
        return result;
    }

    public Vector2D mult(double b) {
        Vector2D result;
        result = new Vector2D(this.x*b, this.y*b);
        return result;
    }

    public Vector2D par(double a) {
        Vector2D result;
        result = new Vector2D(this.x/a,this.y/a);
        return result;
    }

    public Vector2D add(double num) {
        Vector2D result;
        result = new Vector2D(this.x+num, this.y+num);
        return result;
    }

    public Vector2D add(Vector2D v) {
        Vector2D result;
        result = new Vector2D(this.x+v.x,this.y+v.y);
        return result;
    }

    public double lengthSquared() {
        return this.x*this.x+this.y*this.y;
    }

    public static double getDistance(Vector2D a, Vector2D b) {
        return Math.sqrt(Math.pow(a.x-b.x,2.0) + Math.pow(a.y - b.y, 2.0));
    }

    public static double dotProduct(Vector2D a, Vector2D b) {
        return (a.x * b.x + a.y * b.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
