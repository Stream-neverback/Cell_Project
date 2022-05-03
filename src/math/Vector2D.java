package math;

public class Vector2D implements Cloneable {
    double x, y;
    final double EPS = Double.MIN_VALUE;
    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double dot(Vector2D v){
        return this.x*v.x + this.y*v.y;
    }

    public Vector2D norm(){
        double length = x*x+y*y;
        length = Math.sqrt(length);
        this.x /= length+EPS;
        this.y /= length+EPS;
        return this;
    }

    public Vector2D multiply(double s){
        this.x *= s;
        this.y *= s;
        return this;
    }

    public Vector2D add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2D rotate(double rad){
        double newX = this.x*Math.cos(rad)-this.y*Math.sin(rad);
        double newY = this.x*Math.sin(rad)+this.y*Math.cos(rad);
        this.x = newX;
        this.y = newY;
        return this;
    }

    public Vector2D rotate(){
        double foo = y;
        this.y = -this.x;
        this.x = foo;
        return this;
    }

    @Override
    public Vector2D clone() {
        Vector2D ret = null;
        try {
            ret = (Vector2D)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public String toString() {
        return String.format("%10.3E %10.3E", x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
