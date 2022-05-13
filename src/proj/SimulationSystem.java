package proj;

import edu.princeton.cs.algs4.StdDraw;
import proj.bhtree.BHTree;
import proj.Cell;
import proj.Console;
//import proj.oltree.OverlapTree;
import proj.bhtree.QuadNode;
import java.awt.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import proj.kdtree.KdTreeMine;
public class SimulationSystem {
    private static SimulationSystem instance;
    private double[] range;
    private Cell[] cells;
    private boolean isGUIMode;
    private double dt;
    public boolean hasCollision = true;
    public boolean noSpeedUp = false;
    public boolean noSpeedUpCollision = false;
    public boolean benchmark = true;
    public boolean isMouseMode = false;
    public boolean isOutOfTime = false;


    static {
        instance = new SimulationSystem();
    }

    private SimulationSystem() {

    }

    public static SimulationSystem getInstance() {
        if (null == instance)
            instance = new SimulationSystem();
        return instance;
    }

    public void simulation(Console console, final double dt) {
        Cell[] cells = console.getCells();
        double[] range = console.getRange();
        final boolean isGUIMode = !console.isTerminalMode();
        final Cell[] finalParticles = cells;
        if (range.length == 1) {
            System.out.println("length = 1");
            double a = range[0];
            range = new double[4];
            range[0] = 0;
            range[1] = a;
            range[2] = 0;
            range[3] = a;
        }
        assert range.length == 4;
        this.range = range;
        this.cells = cells;
        this.isGUIMode = isGUIMode;
        this.dt = dt;
        if (isGUIMode) {
            StdDraw.show();
            StdDraw.enableDoubleBuffering();
            double ratio = range[3]/range[1];
            int canvasWidth = 600;
            int canvasHeight = (int) ((double) canvasWidth * ratio);
//            System.out.println(canvasWidth + " " + canvasHeight);
            StdDraw.setCanvasSize(canvasWidth, canvasHeight);
//            System.out.printf("%f,%f,%f,%f",range[0], range[1],range[2], range[3]);
            StdDraw.setXscale(range[0], range[1]);
            StdDraw.setYscale(range[2], range[3]);
        }
        int cnt = 0;
        int loopCnt = 0;
        for (double t = 0.0; true; t = t + dt) {
            loopCnt+=1;
            long begin = 0;
            if (benchmark)
                begin = System.nanoTime();
            if (noSpeedUp) {
            } else {
//                QuadNode qNode = new QuadNode((range[0]+range[1])/2, (range[2]+range[3])/2,
//                    Math.max((range[1]-range[0]), (range[3]-range[2])));
//                BHTree tree = new BHTree(qNode);
//                Arrays.stream(cells).parallel()
//                        .filter(c -> c.in(qNode))
//                        .forEachOrdered(tree::insert);
//                //                    particle.clearGravity();
//                //                    cell.check_color(dt);
//                Arrays.stream(cells).parallel().forEach(tree::checkDetection);


//                if(hasCollision) {
//                    if(!noSpeedUpCollision){
//                        OverlapTree olTree = new OverlapTree(particles);
//                        olTree.putBuckets(particles);
//                        olTree.collision();
//                    } else{
//                        Arrays.stream(particles).forEach(p -> p.collide(Arrays.asList(finalParticles)));
//                    }
//                }
            }
            if (isGUIMode) {
//                System.out.println("loop count is "+loopCnt);
//                System.out.println("a");
                StdDraw.clear(StdDraw.BLACK);
                Arrays.stream(cells).parallel().forEachOrdered(Cell::draw);
                // 在树里面寻找是否有这个cell
//                QuadNode qNode = new QuadNode((range[0] + range[1]) / 2, (range[2] + range[3]) / 2,
//                        (range[1] - range[0]), (range[3] - range[2]));
//                BHTree tree = new BHTree(qNode);
                KdTreeMine tree = new KdTreeMine(range[0], range[2], range[1], range[3], console.getMaxR());
                Arrays.stream(cells).parallel()
//                        .filter(c -> c.in(qNode))
                        .forEachOrdered(p -> {
                            tree.insert(p);
//                            tree.checkCollision(p);
//                            System.out.println(p.getMoveMode());
//                            System.out.printf("cell's y is %f\r\n", p.getY());
//                            System.out.printf("cell's future_y is %f\r\n", p.get_future_y());
//                            p.move();
//                            tree.checkCollision(p);
//                            System.out.println(p.getMoveMode());
                        });
                // OK
                Arrays.stream(cells)
//                        .filter(c -> c.in(qNode))
                        .forEach(p -> {
                            tree.checkCollision(p);
//                            System.out.println(p.getMoveMode());
//                            System.out.printf("cell's y is %f\r\n", p.getY());
//                            System.out.printf("cell's future_y is %f\r\n", p.get_future_y());
                            p.move();
//                            tree.checkCollision(p);
//                            if(p.getMoveMode()) {
//                                System.out.println(p.getX());
//                                System.out.println(p.getX());
//                            }
                        });
//                Arrays.stream(cells)
////                        .filter(c -> c.in(qNode))
//                        .forEach(p -> {
////                            tree.checkCollision(p);
////                            System.out.println(p.getMoveMode());
//                            System.out.printf("cell's y is %f\r\n", p.getY());
////                            System.out.printf("cell's future_y is %f\r\n", p.get_future_y());
////                            }
//                        });

//                Arrays.stream(cells).filter(c -> c.in(qNode))
//                        .forEach(p -> {
////                            tree.checkCollision(p);
////                            System.out.println(p.getMoveMode());
////                            System.out.printf("cell's y is %f\r\n", p.getY());
////                            System.out.printf("cell's future_y is %f\r\n", p.get_future_y());
//                            p.move();
//                            tree.checkCollision(p);
////                            System.out.println(p.getMoveMode());
//                        });
//                StdDraw.setPenColor(Color.RED);
//                StdDraw.circle(0.5, 0.5, 0.2);
//                StdDraw.filledCircle(4, 5, 1);
                Arrays.stream(cells).forEach(tree::checkDetection); //查找到之后随机改颜色，或者别的功能，改颜色似乎别的cell也应该改一下
                Arrays.stream(cells).forEach(p -> {p.check_color();});
                Arrays.stream(cells).forEachOrdered(p -> {p.reset_num();});
                Arrays.stream(cells)
//                        .filter(c -> c.in(qNode))
                        .forEach(p -> {p.setMoveMode(true);});
                if (isMouseMode && StdDraw.isMousePressed()) { // 创意：点击鼠标可以实现某些功能，比如点击一下窗口内如果刚好在某个cell范围内可以更改它的颜色
                    double mouse_pressed_x = StdDraw.mouseX();
                    double mouse_pressed_y = StdDraw.mouseY();
                    // 在树里面寻找是否有这个cell
//                    QuadNode qNode = new QuadNode((range[0] + range[1]) / 2, (range[2] + range[3]) / 2,
//                            (range[1] - range[0]), (range[3] - range[2]));
//                    BHTree tree = new BHTree(qNode);
//                    Arrays.stream(cells).parallel()
//                            .filter(c -> c.in(qNode))
//                            .forEachOrdered(p -> {
//                                tree.insert(p);
////                                p.move();
//                            });
//
//
//
//                    Arrays.stream(cells).parallel().forEach(tree::checkDetection); //查找到之后随机改颜色，或者别的功能，改颜色似乎别的cell也应该改一下
                }
                StdDraw.show();
            } else {

                while (true) {
                    if (console.queue.size() == 0)
                        return;
                    Console.Pair<Double, Integer> pair = console.queue.get(0);
                    if (pair.key <= t) {
                        Cell c = cells[pair.value];
//                        System.out.println(c);
                        console.queue.remove(0);
                    } else
                        break;
                }
            }

            if (benchmark) {
                long end = System.nanoTime();
                double frame_rate = 1 / ((end - begin) * 1e-9);
                if (cnt % 50 == 0)
                    System.err.println("Frame rate: " + frame_rate);
                cnt++;
            }
        }
    }

    public double[] getRange() {
        return range;
    }

    public Cell[] getCells() {
        return cells;
    }

    public boolean isGUIMode() {
        return isGUIMode;
    }

    public double getDt() {
        return dt;
    }

    public static void main(String[] args) {
//        String file_path = "./sample/sample/sample2.txt";
        String file_path = "./sample/sample/sample2.txt";
        Console console = new Console("gui", file_path);
        SimulationSystem s = new SimulationSystem();
        s.simulation(console, 1.0/15.0);
    }

}
