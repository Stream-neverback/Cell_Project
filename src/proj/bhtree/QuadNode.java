package proj.bhtree;
import proj.Cell;
import proj.bhtree.BHTree;
import proj.Cell;
public class QuadNode {

    private final double x_center;
    private final double y_center;
    private final double length_x;
    private final double length_y;

    public QuadNode(double x_center, double y_center, double length_x, double length_y) {
        this.x_center = x_center;
        this.y_center = y_center;
        this.length_x = length_x;
        this.length_y = length_y;
    }

    public double getLengthX() {
        return length_x;
    }
    public double getLengthY() {
        return length_y;
    }
    public double getX() {
        return x_center;
    }
    public double getY() {
        return y_center;
    }

    public QuadNode NW_Q() { //
        return new QuadNode(this.getX() - this.getLengthX() / 4, this.getY() - this.getLengthY() / 4, this.getLengthX() / 2, this.getLengthY() / 2);
    }

    public QuadNode NE_Q() {
        return new QuadNode(this.getX() + this.getLengthX() / 4, this.getY() - this.getLengthY() / 4, this.getLengthX() / 2, this.getLengthY() / 2);
    }

    public QuadNode SW_Q() {
        return new QuadNode(this.getX() - this.getLengthX() / 4, this.getY() + this.getLengthY() / 4, this.getLengthX() / 2, this.getLengthY() / 2);
    }

    public QuadNode SE_Q() {
        return new QuadNode(this.getX() + this.getLengthX() / 4, this.getY() + this.getLengthY() / 4, this.getLengthX() / 2, this.getLengthY() / 2);
    }
    public boolean contains(double x, double y) {
        double halfLenX = this.length_x / 2.0;
        double halfLenY = this.length_y / 2.0;
        return (x <= this.x_center + halfLenX &&
                x >= this.x_center- halfLenX &&
                y <= this.y_center + halfLenY &&
                y >= this.y_center - halfLenY);
    }
    public boolean contains_rec(double x, double y, double half_length) {
        double x_dis = Math.abs(x - this.x_center);
        double y_dis = Math.abs(y - this.y_center);
        double halfLenX = this.length_x / 2.0;
        double halfLenY = this.length_y / 2.0;
        return (x_dis <= half_length + halfLenX &&
                y_dis <= half_length + halfLenY);
    }

    @Override
    public String toString() {
        return String.format("%f %f %f %f", this.x_center, this.y_center, this.length_x, this.length_y);
    }
}

