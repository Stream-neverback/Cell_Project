package proj;
import proj.bhtree.BHTree;
import proj.bhtree.QuadNode;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.Color;
public class Cell {
    static double dt = 1.0/15.0;
    static double delta = 1.0/15.0;
    static int total_num = 0;
    private double red_num = 0;
    private double green_num = 0;
    private double blue_num = 0;
    private double yellow_num = 0;
    int id;
    private final double radius;
    private double pos_x;
    private double pos_y;
    private int color_index;
    private int last_color_index;
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
        if(this.color == Color.RED) this.color_index = RED; red_num = 1;
        if(this.color == Color.GREEN) this.color_index = GREEN; green_num = 1;
        if(this.color == Color.BLUE) this.color_index = BLUE; blue_num = 1;
        if(this.color == Color.YELLOW) this.color_index = YELLOW; yellow_num = 1;
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
    public double x_distanceTo(Cell other){
        return Math.abs(other.pos_x-this.pos_x);
    }
    public double y_distanceTo(Cell other){
        return Math.abs(other.pos_y-this.pos_y);
    }

    public boolean inDetection(Cell other){
        return (this.x_distanceTo(other) <= this.perception_r &&
                this.y_distanceTo(other) <= this.perception_r);

    }

    public Boolean CellOverlap(Cell other){
        return distanceTo(other) >= Math.pow((this.radius + other.radius), 2.0);
    }

    public boolean in(QuadNode q) {
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
    public double getX(){
        return this.pos_x;
    }
    public double getY(){
        return this.pos_y;
    }
    public double getRadius(){
        return this.radius;
    }
    public double getPerception_r(){
        return this.perception_r;
    }
    public Color getColor(){
        return this.color;
    }
    public int getColorIndex(){
        return this.color_index;
    }
    public void add_red_num(){ this.red_num += 1; }
    public void add_green_num(){ this.green_num += 1; }
    public void add_blue_num(){ this.blue_num += 1; }
    public void add_yellow_num(){ this.yellow_num += 1; }

    public void add_num(Cell cell){
        switch(cell.getColorIndex()){
            case RED:
                this.red_num += 1;
                break;

            case GREEN:
                this.green_num += 1;
                break;

            case BLUE:
                this.blue_num += 1;
                break;

            case YELLOW:
                this.yellow_num += 1;
                break;
            default: break;
        }
    }
    public void check_color() {
        double sum_num = this.red_num + this.blue_num + this.green_num + this.yellow_num;
        // check red
        if (this.color == Color.RED){
            if(this.red_num >= 3 && this.red_num/sum_num > 0.7){
                this.color = Color.GREEN;
                this.color_index = GREEN;
                this.red_num -= 1;
                this.green_num += 1;
            } else if (this.yellow_num >= 1 && this.yellow_num/sum_num < 0.1) {
                this.color = Color.YELLOW;
                this.color_index = YELLOW;
                this.red_num -= 1;
                this.yellow_num += 1;
            }
        }

        // check green
        if (this.color == Color.GREEN){
            if(this.green_num >= 3 && this.green_num/sum_num > 0.7){
                this.color = Color.BLUE;
                this.color_index = BLUE;
                this.green_num -= 1;
                this.blue_num += 1;
            } else if (this.red_num >= 1 && this.red_num/sum_num < 0.1) {
                this.color = Color.RED;
                this.color_index = RED;
                this.green_num -= 1;
                this.red_num += 1;
            }
        }

        // check blue
        if (this.color == Color.BLUE){
            if(this.blue_num >= 3 && this.blue_num/sum_num > 0.7){
                this.color = Color.YELLOW;
                this.color_index = YELLOW;
                this.blue_num -= 1;
                this.yellow_num += 1;
            } else if (this.green_num >= 1 && this.green_num/sum_num < 0.1) {
                this.color = Color.GREEN;
                this.color_index = GREEN;
                this.blue_num -= 1;
                this.green_num += 1;
            }
        }
        // check blue
        if (this.color == Color.BLUE){
            if(this.blue_num >= 3 && this.blue_num/sum_num > 0.7){
                this.color = Color.YELLOW;
                this.color_index = YELLOW;
                this.blue_num -= 1;
                this.yellow_num += 1;
            } else if (this.green_num >= 1 && this.green_num/sum_num < 0.1) {
                this.color = Color.GREEN;
                this.color_index = GREEN;
                this.blue_num -= 1;
                this.green_num += 1;
            }
        }

        // check yellow
        if (this.color == Color.YELLOW){
            if(this.yellow_num >= 3 && this.yellow_num/sum_num > 0.7){
                this.color = Color.RED;
                this.color_index = RED;
                this.yellow_num -= 1;
                this.red_num += 1;
            } else if (this.yellow_num >= 1 && this.yellow_num/sum_num < 0.1) {
                this.color = Color.BLUE;
                this.color_index = BLUE;
                this.yellow_num -= 1;
                this.blue_num += 1;
            }
        }
    }

    public static void main(String[] args) {
        Cell cell = new Cell(0.1,0.5,0,Color.RED, 10);
        for(int i = 0; i<10; i++) {
            cell.move();
            cell.draw();
        }
//        System.out.println(cell.id);
        System.out.println(cell.getColorIndex());
        System.out.println(cell.getY());

//        cell.draw();
    }
}
