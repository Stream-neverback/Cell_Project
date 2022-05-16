package proj;

import edu.princeton.cs.algs4.StdDraw;

import java.io.*;
import java.util.*;

import proj.kdtree.KdTreeMine;

import static java.lang.Thread.sleep;

public class SimulationSystem {
    private static SimulationSystem instance;
    private double[] range;
    private Cell[] cells;
    private boolean isGUIMode;
    private double dt;
    public static String file_version = "";
    public boolean hasCollision = true;
    public boolean noSpeedUp = true;
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
        String path2 = "./sample/sample" + file_version + "_out.txt";
        File file = new File(path2);
        Scanner input = null;
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String path3 = "./result/sample" + file_version + "_res.txt";
        File f=new File(path3);
        FileOutputStream fos1= null;
        try {
            fos1 = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter dos1=new OutputStreamWriter(fos1);
        int wrong_cnt = 0; // count how many wrongs are there
        int total_cnt = 0;
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
//        int loopCnt = 0;
        for (double t = 0.0; true; t = t + dt) {
//            loopCnt+=1;
            long begin = 0;
            begin = System.nanoTime();
            if (isGUIMode) {
                StdDraw.clear(StdDraw.BLACK);
                Arrays.stream(cells).parallel().forEachOrdered(Cell::draw);
                // 在树里面寻找是否有这个cell
//                QuadNode qNode = new QuadNode((range[0] + range[1]) / 2, (range[2] + range[3]) / 2,
//                        (range[1] - range[0]), (range[3] - range[2]));
//                BHTree tree = new BHTree(qNode);

                StdDraw.show();
            } else {
                while (true) {
                    if (console.queue.size() == 0) {
                        double ratio = wrong_cnt/(double) total_cnt;
                        System.out.println("wrong cnt: " + wrong_cnt + " wrong ratio: " + ratio);
                        try {
                            dos1.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    Console.Pair<Double, Integer> pair = console.queue.get(0);
                    if (pair.key <= t) {
                        double ans_x = input.nextDouble();
                        double ans_y = input.nextDouble();
                        String ans_c = input.next();
//                        System.out.println(ans_x + " " + ans_y + " " + ans_c);
                        Cell c = cells[pair.value];
                        String write;
                        total_cnt++;
                        boolean compare_0 = Math.abs(c.getX() - ans_x) < 0.001;
                        boolean compare_1 = Math.abs(c.getY() - ans_y) < 0.001;
//                        if (c.getX() != ans_x || c.getY() != ans_y || !c.colorIndex().equals(ans_c)) {
                        if (!compare_0 || !compare_1 || !c.colorIndex().equals(ans_c)) {
                            System.out.println("wrong! answer is: " + ans_x + " " + ans_y + " " + ans_c);
                            System.out.println("your answer is: " + c.getX() + " " + c.getY() + " " + c.colorIndex());
                            wrong_cnt++;
                            write = c.getX() + " " + c.getY() + " " + c.colorIndex() + " wrong" + "\n";
                        }
                        else
                            write = c.getX() + " " + c.getY() + " " + c.colorIndex() + "\n";
                        try {
                            dos1.write(write);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        console.queue.remove(0);
                    } else
                        break;
                }
            }
            KdTreeMine tree = new KdTreeMine(range[0], range[2], range[1], range[3], console.getMaxR());
            Arrays.stream(cells).parallel()
//                        .filter(c -> c.in(qNode))
                    .forEachOrdered(tree::insert);
            // OK
            Arrays.stream(cells)
//                        .filter(c -> c.in(qNode))
                    .forEach(p -> {
                        tree.checkCollision(p);
//                            System.out.println(p.getMoveMode());
//                            System.out.printf("cell's y is %f\r\n", p.getY());
//                            System.out.printf("cell's fusture_y is %f\r\n", p.get_future_y());
                        p.move();
//                            tree.checkCollision(p);
//                            if(p.getMoveMode()) {
//                                System.out.println(p.getX());
//                                System.out.println(p.getX());
//                            }
                    });
            Arrays.stream(cells).forEach(tree::checkDetection); //查找到之后随机改颜色，或者别的功能，改颜色似乎别的cell也应该改一下
            Arrays.stream(cells).forEach(Cell::check_color);
            Arrays.stream(cells).forEachOrdered(Cell::reset_num);
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


            if (benchmark) {
                long end = System.nanoTime();
                double frame_rate = 1 / ((end - begin) * 1e-9);
                if (cnt % 50 == 0)
//                    System.err.println("Frame rate in benchmark: " + frame_rate);
                cnt++;
            }
            else {
                long end = System.nanoTime();
                if (end-begin<dt*1e9) {
                    try {
                        sleep((long) ((dt - (end - begin) * 1e-9) * 1e3));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                long end_fin = System.nanoTime();
                double frame_rate = 1 / ((end_fin - begin) * 1e-9);
                if (cnt % 15 == 0)
//                    System.err.println("Frame rate under constrained: " + frame_rate);
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
        file_version = "2";
        String file_path = "./sample/sample" + file_version + ".txt";
        Console console = new Console("ter", file_path);
        SimulationSystem s = new SimulationSystem();
        s.simulation(console, 1.0/15.0);
    }
}
