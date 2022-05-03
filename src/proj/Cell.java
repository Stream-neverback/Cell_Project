package proj;
import proj.bhtree.BHTree;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
public class Cell {
    static double dt = 1/15;
    static double delta = 1/15;
    static int total_num = 0;
    int id;
    private final double radius;
    private double pos_x;
    private double pos_y;
    private int color_index;
    private Color color;
    double perception_r;
    static final int RED = 0;
    static final int GREEN = 1;
    static final int BLUE = 2;
    static final int YELLOW = 3;

    public Cell(double radius, double position_x, double position_y, Color color, double perception_radius){
        this.id = total_num;
        this.total_num += 1;
        this.radius = radius;
        this.pos_x = position_x;
        this.pos_y = position_y;
        this.color = color;
        if(this.color == Color.RED) this.color_index = RED;
        if(this.color == Color.GREEN) this.color_index = GREEN;
        if(this.color == Color.BLUE) this.color_index = BLUE;
        if(this.color == Color.YELLOW) this.color_index = YELLOW;
        this.perception_r = perception_radius;
    }

    public Cell(Color color){
        this(1, 0, 0, color, 1);
    }
    public Cell(){
        this(1, 0, 0, Color.RED, 1);
    }
    public double distanceTo(Cell other){
        return Math.sqrt(Math.pow((this.pos_x - other.pos_x), 2.0) + Math.pow((this.pos_y - other.pos_y), 2.0));
    }

    public Boolean Cell_overleap(Cell other){
        return distanceTo(other) >= Math.pow((this.radius + other.radius), 2.0);
    }

    public boolean in(BHTree.QNode q) {
        return q.contains(this.pos_x, this.pos_y);
    }
    public void move(){
        switch(this.color_index){
            case RED:
                this.pos_y = this.pos_y + delta;
                break;

            case GREEN:
                this.pos_y = this.pos_y - delta;
                break;

            case BLUE:
                this.pos_x = this.pos_x - delta;
                break;

            case YELLOW:
                this.pos_x = this.pos_x + delta;
                break;
            default: break;
        }
    }

    public void draw() {
        StdDraw.setPenColor(this.color);
        StdDraw.filledCircle(this.pos_x, this.pos_y, this.radius);
    }

    public static void main(String[] args) {
        Cell cell = new Cell(0.1,0.5,0.5,Color.RED, 10);
        for(int i = 0; i<10000; i++) {
            cell.move();
        }
        System.out.println(cell.id);
        System.out.println(cell.pos_y);

        cell.draw();
    }
}
