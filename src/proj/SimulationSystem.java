package proj;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.*;

import proj.bhtree.BHTree;
import proj.bhtree.QuadNode;
import proj.kdtree.CellSET;
import proj.kdtree.KdTreeMine;
import proj.kdtree.RectHV;

import static java.lang.Thread.sleep;

public class SimulationSystem {
    private static SimulationSystem instance;
    private double[] range;
    private Cell[] cells;
    private double dt;
    public static String file_version = "3";
    public boolean isGUIMode; // 默认为GUI模式
    public boolean noSpeedUp = true; // 两两检测，暴力解法
    public boolean BHSpeedUp = false; // BH树加速
    public boolean KDSpeedUp = false; // KD树加速
    public boolean benchmark = false; // 最佳性能模式，否则是限制为1/15秒的模式
    public boolean isMouseMode = true; // 玩法，包括两种
    public boolean isDeleteMode = false; // 玩法：删除
    public boolean isChangeColorMode = true; // 玩法：随机变换颜色


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
        String path = "../result/result.txt";
        File f=new File(path);
        FileOutputStream fos1= null;
        try {
            fos1 = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter dos1=new OutputStreamWriter(fos1);
        double t_mouse = 0.0; // 点鼠标之后的一段时间都不反应
        Random rand = new Random();
        Cell[] cells = console.getCells();
        double[] range = console.getRange();
        final boolean isGUIMode = !console.isTerminalMode();
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
            StdDraw.setCanvasSize(canvasWidth, canvasHeight);
            StdDraw.setXscale(range[0], range[1]);
            StdDraw.setYscale(range[2], range[3]);
        }
        int cnt = 0;
        ArrayList<Integer> delete_list = new ArrayList<>();
        for (double t = 0.0; true; t = t + dt) {
            long begin = 0;
            begin = System.nanoTime();
            if (isGUIMode) {
                StdDraw.clear(StdDraw.BLACK);
                if (!isDeleteMode){
                    Arrays.stream(cells).parallel().forEachOrdered(Cell::draw);
                }
                else {
                    for (int i = 0; i < cells.length; i++) {
                        if (delete_list.contains(i)) continue;
                        else cells[i].draw();
                    }
                }
                StdDraw.show();
            }
            else {
                while (true) {
                    if (console.queue.size() == 0) {
                        try {
                            dos1.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Terminal mode ends!");
                        return;
                    }
                    Console.Pair<Double, Integer> pair = console.queue.get(0);
                    if (pair.key <= t + dt) {
                        Cell c = cells[pair.value];
                        String write = c.getX() + " " + c.getY() + " " + c.colorIndex() + "\n";
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
//            CellSET brute = new CellSET(range[0], range[2], range[1], range[3], console.getMaxR());
            if (!isDeleteMode) {
                Arrays.stream(cells).parallel()
                        .forEachOrdered(tree::insert);
                Arrays.stream(cells)
                        .forEach(p -> {
                            tree.checkCollision(p);
                            p.move();
                        });
            }
            else {
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else tree.insert(cells[i]);
                }
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else {
                        tree.checkCollision(cells[i]);
                        cells[i].move();
                    }
                }
            }
            if (!isDeleteMode) {
                Arrays.stream(cells).forEach(tree::checkDetection);
                Arrays.stream(cells).forEach(Cell::check_color);
                Arrays.stream(cells).forEachOrdered(Cell::reset_num);
                Arrays.stream(cells).forEach(p -> {p.setMoveMode(true);});
            }
            else {
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else tree.checkDetection(cells[i]);
                }
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else cells[i].check_color();
                }
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else cells[i].reset_num();
                }
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else cells[i].setMoveMode(true);
                }
            }
            if (isGUIMode && isMouseMode && isDeleteMode && t_mouse < t && StdDraw.isMousePressed()) { // 创意：点击鼠标可以实现某些功能，比如点击一下窗口内如果刚好在某个cell范围内可以更改它的颜色
                double mouse_pressed_x = StdDraw.mouseX();
                double mouse_pressed_y = StdDraw.mouseY();
                Iterable<Cell> cellsInRange = tree.range(new RectHV(mouse_pressed_x - 1, mouse_pressed_y - 1, mouse_pressed_x + 1, mouse_pressed_y + 1)); // 在树里面寻找是否有这个cell
                for (Cell cell : cellsInRange) {
                    delete_list.add(cell.id);
                    break;
                }
                t_mouse = t + 1.0;
            }
            else if (isGUIMode && isMouseMode && isChangeColorMode && t_mouse < t && StdDraw.isMousePressed()) {
                double mouse_pressed_x = StdDraw.mouseX();
                double mouse_pressed_y = StdDraw.mouseY();
                Iterable<Cell> cellsInRange = tree.range(new RectHV(mouse_pressed_x - 1, mouse_pressed_y - 1, mouse_pressed_x + 1, mouse_pressed_y + 1)); // 在树里面寻找是否有这个cell
                for (Cell cell : cellsInRange) {
                    int i = rand.nextInt(4);
                    Color c;
                    switch (i){
                        case(0): {
                            c=Color.RED;
                            break;
                        }
                        case(1): {
                            c=Color.GREEN;
                            break;
                        }
                        case(3): {
                            c=Color.YELLOW;
                            break;
                        }
                        default: {
                            c=Color.BLUE;
                            break;
                        }
                    }
                    cell.setColor(c, i);
                        break;
                }
                t_mouse = t + 1.0;
            }
            if (benchmark) {
                long end = System.nanoTime();
                double frame_rate = 1 / ((end - begin) * 1e-9);
                if (cnt % 50 == 0)
                    System.err.println("Frame rate in benchmark: " + frame_rate);
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
                    System.err.println("Frame rate under constrained: " + frame_rate);
                    cnt++;
            }
        }
    }


    public void simulationBrute(Console console, final double dt) {
        String path = "../result/result.txt";
        File f=new File(path);
        FileOutputStream fos1= null;
        try {
            fos1 = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter dos1=new OutputStreamWriter(fos1);
        double t_mouse = 0.0; // 点鼠标之后的一段时间都不反应
        Random rand = new Random();
        Cell[] cells = console.getCells();
        double[] range = console.getRange();
        final boolean isGUIMode = !console.isTerminalMode();
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
            StdDraw.setCanvasSize(canvasWidth, canvasHeight);
            StdDraw.setXscale(range[0], range[1]);
            StdDraw.setYscale(range[2], range[3]);
        }
        int cnt = 0;
        ArrayList<Integer> delete_list = new ArrayList<>();
        for (double t = 0.0; true; t = t + dt) {
            long begin = 0;
            begin = System.nanoTime();
            if (isGUIMode) {
                StdDraw.clear(StdDraw.BLACK);
                if (!isDeleteMode){
                    Arrays.stream(cells).parallel().forEachOrdered(Cell::draw);
                }
                else {
                    for (int i = 0; i < cells.length; i++) {
                        if (delete_list.contains(i)) continue;
                        else cells[i].draw();
                    }
                }
                StdDraw.show();
            }
            else {
                while (true) {
                    if (console.queue.size() == 0) {
                        try {
                            dos1.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Terminal mode ends!");
                        return;
                    }
                    Console.Pair<Double, Integer> pair = console.queue.get(0);
                    if (pair.key <= t + dt) {
                        Cell c = cells[pair.value];
                        String write = c.getX() + " " + c.getY() + " " + c.colorIndex() + "\n";
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
//            KdTreeMine tree = new KdTreeMine(range[0], range[2], range[1], range[3], console.getMaxR());
            CellSET tree = new CellSET(range[0], range[2], range[1], range[3], console.getMaxR());
            if (!isDeleteMode) {
                Arrays.stream(cells).parallel()
                        .forEachOrdered(tree::insert);
                Arrays.stream(cells)
                        .forEach(p -> {
                            tree.checkCollision(p);
                            p.move();
                        });
            }
            else {
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else tree.insert(cells[i]);
                }
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else {
                        tree.checkCollision(cells[i]);
                        cells[i].move();
                    }
                }
            }
            if (!isDeleteMode) {
                Arrays.stream(cells).forEach(tree::checkDetection);
                Arrays.stream(cells).forEach(Cell::check_color);
                Arrays.stream(cells).forEachOrdered(Cell::reset_num);
                Arrays.stream(cells).forEach(p -> {p.setMoveMode(true);});
            }
            else {
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else tree.checkDetection(cells[i]);
                }
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else cells[i].check_color();
                }
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else cells[i].reset_num();
                }
                for (int i = 0; i < cells.length; i++) {
                    if (delete_list.contains(i)) continue;
                    else cells[i].setMoveMode(true);
                }
            }
            if (isGUIMode && isMouseMode && isDeleteMode && t_mouse < t && StdDraw.isMousePressed()) { // 创意：点击鼠标可以实现某些功能，比如点击一下窗口内如果刚好在某个cell范围内可以更改它的颜色
                double mouse_pressed_x = StdDraw.mouseX();
                double mouse_pressed_y = StdDraw.mouseY();
                Iterable<Cell> cellsInRange = tree.range(new RectHV(mouse_pressed_x - 1, mouse_pressed_y - 1, mouse_pressed_x + 1, mouse_pressed_y + 1)); // 在树里面寻找是否有这个cell
                for (Cell cell : cellsInRange) {
                    delete_list.add(cell.id);
                    break;
                }
                t_mouse = t + 0.5;
            }
            else if (isGUIMode && isMouseMode && isChangeColorMode && t_mouse < t && StdDraw.isMousePressed()) {
                double mouse_pressed_x = StdDraw.mouseX();
                double mouse_pressed_y = StdDraw.mouseY();
                Iterable<Cell> cellsInRange = tree.range(new RectHV(mouse_pressed_x - 1, mouse_pressed_y - 1, mouse_pressed_x + 1, mouse_pressed_y + 1)); // 在树里面寻找是否有这个cell
                for (Cell cell : cellsInRange) {
                    int i = rand.nextInt(4);
                    Color c;
                    switch (i){
                        case(0): {
                            c=Color.RED;
                            break;
                        }
                        case(1): {
                            c=Color.GREEN;
                            break;
                        }
                        case(3): {
                            c=Color.YELLOW;
                            break;
                        }
                        default: {
                            c=Color.BLUE;
                            break;
                        }
                    }
                    cell.setColor(c, i);
                    break;
                }
                t_mouse = t + 0.5;
            }
            if (benchmark) {
                long end = System.nanoTime();
                double frame_rate = 1 / ((end - begin) * 1e-9);
                if (cnt % 20 == 0)
                    System.err.println("Frame rate in benchmark: " + frame_rate);
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
                if (cnt % 10 == 0)
                    System.err.println("Frame rate under constrained: " + frame_rate);
                cnt++;
            }
        }
    }





    public Cell[] getCells() {
        return cells;
    }

    public static void main(String[] args) {
//        file_version = "1";
//        String file_path = "./sample/sample" + file_version + ".txt";
//        Console console = new Console("gui", file_path);
//        SimulationSystem s = new SimulationSystem();
//        s.simulation(console, 1.0/15.0);
    }
}