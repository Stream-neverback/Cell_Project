package proj.bhtree;
import proj.Cell;
import proj.bhtree.BHTree;
import proj.Cell;
public class QuadNode {

    private final double x_center;
    private final double y_center;
    private final double length;

    public QuadNode(double x_center, double y_center, double length) {
        this.x_center = x_center;
        this.y_center = y_center;
        this.length = length;
    }

    public double getLength() {
        return length;
    }
    public double getX() {
        return x_center;
    }
    public double getY() {
        return y_center;
    }

    public QuadNode NW_Q() { //
        return new QuadNode(this.getX() - this.getLength() / 4, this.getY() - this.getLength() / 4, this.getLength() / 2);
    }

    public QuadNode NE_Q() {
        return new QuadNode(this.getX() + this.getLength() / 4, this.getY() - this.getLength() / 4, this.getLength() / 2);
    }

    public QuadNode SW_Q() {
        return new QuadNode(this.getX() - this.getLength() / 4, this.getY() + this.getLength() / 4, this.getLength() / 2);
    }

    public QuadNode SE_Q() {
        return new QuadNode(this.getX() + this.getLength() / 4, this.getY() + this.getLength() / 4, this.getLength() / 2);
    }
    public boolean contains(double x, double y) {
        double halfLen = this.length / 2.0;
        return (x <= this.x_center + halfLen &&
                x >= this.x_center- halfLen &&
                y <= this.y_center + halfLen &&
                y >= this.y_center - halfLen);
    }
    public boolean contains_rec(double x, double y, double half_length) {
        double x_dis = Math.abs(x - this.x_center);
        double y_dis = Math.abs(y - this.y_center);
        double halfLen = this.length / 2.0;
        return (x_dis <= half_length + halfLen &&
                y_dis <= half_length + halfLen);
    }

    @Override
    public String toString() {
        return String.format("%f %f %f", this.x_center, this.y_center, this.length);
    }
}

