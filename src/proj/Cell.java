package proj;

import edu.princeton.cs.algs4.Point2D;
import proj.bhtree.BHTree;
import proj.bhtree.QuadNode;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

public class Cell implements Comparable<Cell> {
    static double wall_width = 0; // x-direction
    static double wall_length = 0; // y-direction
    static double dt = 1.0 / 15.0;
    static double delta = 1.0 / 15.0;
    static int total_num = 0;
    private double red_num = 0;
    private double green_num = 0;
    private double blue_num = 0;
    private double yellow_num = 0;
    public int id;
    private final double radius;
    private double pos_x;
    private double pos_y;
    private double future_pos_x;
    private double future_pos_y;

    public boolean MOVE = true;
    private int color_index;
    private Color color;
    double perception_r;
    static final int RED = 0;
    static final int GREEN = 1;
    static final int BLUE = 2;
    static final int YELLOW = 3;

    @Override
    public int compareTo(Cell that) {
        if (this.pos_y < that.getY()) return -1;
        if (this.pos_y > that.getY()) return +1;
        if (this.pos_x < that.getX()) return -1;
        if (this.pos_x > that.getX()) return +1;
        return 0;
    }

    public void printNum() {
        System.out.printf("red: %s, green: %s, blue: %s, yellow: %s\n", red_num, green_num, blue_num, yellow_num);
    }

    public static void initWall(double wX, double wY) {
        wall_width = wX;
        wall_length = wY;
    }

    public void setColor(Color color, int new_index) {
        this.color = color;
        this.color_index = new_index;
    }

    public Cell(double radius, double position_x, double position_y, Color color, double perception_radius) {
        this.radius = radius;

        this.id = total_num;
        this.total_num += 1;
        this.pos_x = position_x;
        this.pos_y = position_y;
        this.color = color;
        if (this.color == Color.RED) this.color_index = RED;
        if (this.color == Color.GREEN) this.color_index = GREEN;
        if (this.color == Color.BLUE) this.color_index = BLUE;
        if (this.color == Color.YELLOW) this.color_index = YELLOW;
        this.perception_r = perception_radius;
    }

    public Cell(Color color) {
        this(1, 0, 0, color, 1);
    }

    public Cell() {
        this(1, 0, 0, Color.RED, 1);
    }

    public void setMoveMode(boolean mode) {
        this.MOVE = mode;
    }

    public boolean getMoveMode() {
        return this.MOVE;
    }

    public double distanceTo(Cell other) {
        return Math.sqrt(Math.pow((this.pos_x - other.pos_x), 2.0) + Math.pow((this.pos_y - other.pos_y), 2.0));
    }

    public double future_distanceTo(Cell other) {
        this.future_move();
//        other.future_move();
        return Math.sqrt(Math.pow((this.future_pos_x - other.pos_x), 2.0) + Math.pow((this.future_pos_y - other.pos_y), 2.0));
    }

    public double x_distanceTo(Cell other) {
//        System.out.println(other);
        return Math.abs(other.pos_x - this.pos_x);
    }

    public double y_distanceTo(Cell other) {
        return Math.abs(other.pos_y - this.pos_y);
    }

    public boolean inDetection(Cell other) {
        return (this.x_distanceTo(other) <= this.perception_r &&
                this.y_distanceTo(other) <= this.perception_r);

    }

    public void future_move() {
        this.future_pos_x = this.pos_x;
        this.future_pos_y = this.pos_y;
        switch (this.color_index) {
            case RED:
                if (this.pos_y + delta < wall_length - this.radius) {
                    this.future_pos_y = this.pos_y + delta;
                } else {
                    this.future_pos_y = wall_length - this.radius;
                }
                break;

            case GREEN:
                if (this.pos_y - delta > 0 + this.radius) {
                    this.future_pos_y = this.pos_y - delta;
                } else {
                    this.future_pos_y = 0 + this.radius;
                }
                break;

            case BLUE:
                if (this.pos_x - delta > 0 + this.radius) {
                    this.future_pos_x = this.pos_x - delta;
                } else {
                    this.future_pos_x = 0 + this.radius;
                }
                break;

            case YELLOW:
                if (this.pos_x + delta < wall_width - this.radius) {
                    this.future_pos_x = this.pos_x + delta;
                } else {
                    this.future_pos_x = wall_width - this.radius;
                }
                break;
            default:
                break;
        }
    }

    public double get_future_x() {
        return this.future_pos_x;
    }

    public double get_future_y() {
        return this.future_pos_y;
    }

    public boolean Cell_Overlap(Cell other) {
        return future_distanceTo(other) <= this.radius + other.radius;
    }

    public boolean Cell_NotContactWall() {
        return this.pos_x >= this.radius &&
                this.wall_length - this.pos_x >= this.radius &&
                this.pos_y >= this.radius &&
                this.wall_width - this.pos_y >= radius;
    }

    public boolean in(double xmin, double ymin, double xmax, double ymax) {

        return (pos_x >= xmin && pos_y >= ymin && pos_x <= xmax && pos_y <= ymax);
    }


    public boolean in(QuadNode q) {
        return q.contains(this.pos_x, this.pos_y);
    }

    public boolean nearToCorner(double xmin, double ymin, double xmax, double ymax) {
        return (distanceSquaredTo(xmin, ymin) <= radius * radius || distanceSquaredTo(xmax, ymin) <= radius * radius || distanceSquaredTo(xmin, ymax) <= radius * radius || distanceSquaredTo(xmax, ymax) <= radius * radius);

    }

    public double distanceSquaredTo(double x, double y) {
        double dx = this.pos_x - x;
        double dy = this.pos_y - y;
        return dx * dx + dy * dy;
    }

    public boolean isOut() {
        if (pos_x + radius > wall_width || pos_y + radius > wall_length || pos_x - radius < 0 || pos_y - radius < 0) {
            System.out.println("Out! " + (pos_x) + " " + pos_y);
            return true;
        }
        return false;
    }

    public void move() {
        switch (this.color_index) {
            case RED:
                if (this.pos_y + delta < wall_length - this.radius && this.MOVE) {
                    this.pos_y = this.pos_y + delta;
                } else if (this.pos_y + delta >= wall_length - this.radius && this.MOVE) {
                    this.pos_y = wall_length - this.radius;
                }
//                if(isOut()){System.out.println("11111111111111111111111111111111111111111111111111");
//                    System.out.println(this.color);
//                    System.out.println(this.color_index);
//                    System.out.println(this.MOVE);}
                break;

            case GREEN:
                if (this.pos_y - delta > 0 + this.radius && this.MOVE) {
                    this.pos_y = this.pos_y - delta;
                } else if (this.pos_y - delta <= 0 + this.radius && this.MOVE) {
                    this.pos_y = 0 + this.radius;
                }
//                if(isOut()){System.out.println("11111111111111111111111111111111111111111111111111");
//                    System.out.println(this.color);
//                    System.out.println(this.color_index);
//                    System.out.println(this.MOVE);}
                break;

            case BLUE:
                if (this.pos_x - delta > 0 + this.radius && this.MOVE) {
                    this.pos_x = this.pos_x - delta;
                } else if (this.pos_x - delta <= 0 + this.radius && this.MOVE) {
                    this.pos_x = 0 + this.radius;
                }
//                if(isOut()){System.out.println("11111111111111111111111111111111111111111111111111");
//                    System.out.println(this.color);
//                    System.out.println(this.color_index);
//                    System.out.println(this.MOVE);}
                break;

            case YELLOW:
                if (this.pos_x + delta < wall_width - this.radius && this.MOVE) {
                    this.pos_x = this.pos_x + delta;
                } else if (this.pos_x + delta >= wall_width - this.radius && this.MOVE) {
                    this.pos_x = wall_width - this.radius;
                }
//                if(isOut()){System.out.println("11111111111111111111111111111111111111111111111111");
//                    System.out.println(this.color);
//                    System.out.println(this.color_index);
//                    System.out.println(this.MOVE);}
                break;
            default:
                break;
        }
    }

    public double unitDistanceUntilContact(Cell cell) {
        switch (this.color_index) {
            case RED:
                if (this.pos_y + delta < wall_length - this.radius) {
                    double y = Math.sqrt((this.radius + cell.radius) * (this.radius + cell.radius) - this.x_distanceTo(cell) * this.x_distanceTo(cell));

                    double out = cell.pos_y - y-pos_y;
                    return out;
                }
                if (isOut()) {
                    System.out.println("11111111111111111111111111111111111111111111111111");
                    System.out.println(this.color);
                    System.out.println(this.color_index);
                    System.out.println(this.MOVE);
                }
                break;

            case GREEN:
                if (this.pos_y - delta > 0 + this.radius) {
                    double y = Math.sqrt((this.radius + cell.radius) * (this.radius + cell.radius) - this.x_distanceTo(cell) * this.x_distanceTo(cell));
                    double out = cell.pos_y + y-pos_y;
                    return out;
                }
                break;

            case BLUE:
                if (this.pos_x - delta > 0 + this.radius) {
                    double x = Math.sqrt((this.radius + cell.radius) * (this.radius + cell.radius) - this.y_distanceTo(cell) * this.y_distanceTo(cell));
                    double out = cell.pos_x + x-pos_x;
                    return out;
                }
                break;

            case YELLOW:
                if (this.pos_x + delta < wall_width - this.radius) {
                    double x = Math.sqrt((this.radius + cell.radius) * (this.radius + cell.radius) - this.y_distanceTo(cell) * this.y_distanceTo(cell));
                    double out = cell.pos_x - x-pos_x;
                    return out;
                }
                break;
            default:
                break;
        }
        return 0.0;
    }


    public void moveUntilContact(Cell cell) {
        switch (this.color_index) {
            case RED:
                if (this.pos_y + delta < wall_length - this.radius) {
                    double y = Math.sqrt((this.radius + cell.radius) * (this.radius + cell.radius) - this.x_distanceTo(cell) * this.x_distanceTo(cell));
                    this.pos_y = cell.pos_y - y;
                }
                if (isOut()) {
                    System.out.println("11111111111111111111111111111111111111111111111111");
                    System.out.println(this.color);
                    System.out.println(this.color_index);
                    System.out.println(this.MOVE);
                }
                break;

            case GREEN:
                if (this.pos_y - delta > 0 + this.radius) {
                    double y = Math.sqrt((this.radius + cell.radius) * (this.radius + cell.radius) - this.x_distanceTo(cell) * this.x_distanceTo(cell));
                    this.pos_y = cell.pos_y + y;
                }
                break;

            case BLUE:
                if (this.pos_x - delta > 0 + this.radius) {
                    double x = Math.sqrt((this.radius + cell.radius) * (this.radius + cell.radius) - this.y_distanceTo(cell) * this.y_distanceTo(cell));
                    this.pos_x = cell.pos_x + x;
                }
                break;

            case YELLOW:
                if (this.pos_x + delta < wall_width - this.radius) {
                    double x = Math.sqrt((this.radius + cell.radius) * (this.radius + cell.radius) - this.y_distanceTo(cell) * this.y_distanceTo(cell));
                    this.pos_x = cell.pos_x - x;
                    break;
                }
            default:
                break;
        }
    }

    public void draw() {
        StdDraw.setPenColor(this.color);
        StdDraw.filledCircle(this.pos_x, this.pos_y, this.radius);
    }

    public double getX() {
        return this.pos_x;
    }

    public double getY() {
        return this.pos_y;
    }

    public double getRadius() {
        return this.radius;
    }

    public double getPerception_r() {
        return this.perception_r;
    }

    public Color getColor() {
        return this.color;
    }

    public int getColorIndex() {
        return this.color_index;
    }

    public void add_red_num() {
        this.red_num += 1;
    }

    public void add_green_num() {
        this.green_num += 1;
    }

    public void add_blue_num() {
        this.blue_num += 1;
    }

    public void add_yellow_num() {
        this.yellow_num += 1;
    }

//    public double distanceTo(Cell that) {
//        double dx = this.pos_x - that.getX();
//        double dy = this.pos_y - that.getY();
//        return Math.sqrt(dx*dx + dy*dy);
//    }

    public double distanceSquaredTo(Cell that) {
        double dx = this.pos_x - that.getX();
        double dy = this.pos_y - that.getY();
        return dx * dx + dy * dy;
    }

    public void reset_num() {
        this.red_num = 0;
        this.green_num = 0;
        this.blue_num = 0;
        this.yellow_num = 0;
    }

    public void add_num(Cell cell) {
        switch (cell.getColorIndex()) {
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
            default:
                break;
        }
    }

    public void check_color() {
        double sum_num = this.red_num + this.blue_num + this.green_num + this.yellow_num;
//        System.out.printf("sum num is %f\r\n",sum_num);
        // check red
        if (this.color == Color.RED) {
            if (this.red_num >= 3 && this.red_num / sum_num > 0.7) {
                this.color = Color.GREEN;
                this.color_index = GREEN;
//                this.red_num -= 1;
//                this.green_num += 1;
                this.MOVE = true;
            } else if (this.yellow_num >= 1 && this.yellow_num / (sum_num) < 0.1) {
                this.color = Color.YELLOW;
                this.color_index = YELLOW;
//                this.red_num -= 1;
//                this.yellow_num += 1;
                this.MOVE = true;
            }
        }
        // check green
        if (this.color == Color.GREEN) {
            if (this.green_num >= 3 && this.green_num / sum_num > 0.7) {
                this.color = Color.BLUE;
                this.color_index = BLUE;
//                this.green_num -= 1;
//                this.blue_num += 1;
                this.MOVE = true;
            } else if (this.red_num >= 1 && this.red_num / (sum_num) < 0.1) {
                this.color = Color.RED;
                this.color_index = RED;
//                this.green_num -= 1;
//                this.red_num += 1;
                this.MOVE = true;
            }
        }
        // check blue
        if (this.color == Color.BLUE) {
            if (this.blue_num >= 3 && this.blue_num / sum_num > 0.7) {
                this.color = Color.YELLOW;
                this.color_index = YELLOW;
//                this.blue_num -= 1;
//                this.yellow_num += 1;
                this.MOVE = true;
            } else if (this.green_num >= 1 && this.green_num / (sum_num) < 0.1) {
                this.color = Color.GREEN;
                this.color_index = GREEN;
//                this.blue_num -= 1;
//                this.green_num += 1;
                this.MOVE = true;
            }
        }
//        // check blue
//        if (this.color == Color.BLUE) {
//            if (this.blue_num >= 3 && this.blue_num / sum_num > 0.7) {
//                this.color = Color.YELLOW;
//                this.color_index = YELLOW;
////                this.blue_num -= 1;
////                this.yellow_num += 1;
//                this.MOVE = true;
//            } else if (this.green_num >= 1 && this.green_num / sum_num < 0.1) {
//                this.color = Color.GREEN;
//                this.color_index = GREEN;
////                this.blue_num -= 1;
////                this.green_num += 1;
//                this.MOVE = true;
//            }
//        }

        // check yellow
        if (this.color == Color.YELLOW) {
            if (this.yellow_num >= 3 && this.yellow_num / sum_num > 0.7) {
                this.color = Color.RED;
                this.color_index = RED;
//                this.yellow_num -= 1;
//                this.red_num += 1;
                this.MOVE = true;
            } else if (this.blue_num >= 1 && this.blue_num / (sum_num) < 0.1) {
                this.color = Color.BLUE;
                this.color_index = BLUE;
//                this.yellow_num -= 1;
//                this.blue_num += 1;
                this.MOVE = true;
            }
        }
    }

    public static void main(String[] args) {
        Cell.initWall(1, 1);
        Cell cell = new Cell(0.1, 0.5, 0, Color.RED, 10);
        for (int i = 0; i < 10; i++) {
            cell.move();
            cell.draw();
        }
//        System.out.println(cell.id);
//        System.out.println(cell.getColorIndex());
//        System.out.println(cell.getY());

//        cell.draw();
    }

    @Override
    public String toString() {
        String tmp_color = "r";
        switch (this.color_index) {
            case 0: {
                tmp_color = "r";
                break;
            }
            case 1: {
                tmp_color = "g";
                break;
            }
            case 2: {
                tmp_color = "b";
                break;
            }
            case 3: {
                tmp_color = "y";
                break;
            }
            default: {
                tmp_color = "r";
                break;
            }
        }
        return pos_x + " " + pos_y + " " + tmp_color;
    }

    public String colorIndex() {
        String tmp_color = "r";
        switch (this.color_index) {
            case 0: {
                tmp_color = "r";
                break;
            }
            case 1: {
                tmp_color = "g";
                break;
            }
            case 2: {
                tmp_color = "b";
                break;
            }
            case 3: {
                tmp_color = "y";
                break;
            }
            default: {
                tmp_color = "r";
                break;
            }
        }
        return tmp_color;
    }
}
