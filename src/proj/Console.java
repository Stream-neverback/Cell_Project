package proj;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.awt.Color;

public class Console {
    final Cell[] cells;
    final double[] range;
    final boolean terminalMode;
    List<Pair<Double, Integer>> queue = new LinkedList<>();

    public Console(String mode) { // 是否是一个txt文件作为输入呢？
        Scanner input = new Scanner(System.in);
        double range_x = input.nextInt();
        double range_y = input.nextInt();
        this.range = new double[]{0, range_x, 0, range_y};
        int count = input.nextInt();
        cells = new Cell[count];
        for (int i = 0; i < count; i++) {
            double x = input.nextDouble(); // input coordinate x of the cell
            double y = input.nextDouble(); // input coordinate y of the cell
            double r = input.nextDouble(); // input radius of the cell
            double p = input.nextDouble(); // input perception range of the cell
            String c = input.next(); // input color of the cell
            Color color = c.equals("r") ? Color.RED : c.equals("g") ? Color.GREEN : c.equals("b") ? Color.BLUE : Color.YELLOW; // judge colors
            Cell cell = new Cell(r, x, y, color, p); // initialize cell
            cells[i] = cell; // add the cell into array
        }
        terminalMode = !mode.equals("gui"); // default is gui
        if (terminalMode) { // only terminal mode should input query
            int n = input.nextInt();
            for (int i = 0; i < n; i++) {
                double time = input.nextDouble(); // time
                int idx = input.nextInt(); // cell index
                this.queue.add(new Pair<>(time, idx));
            }
            queue = queue.stream().sorted().collect(Collectors.toList());
        }
        input.close();
    }

    public Cell[] getParticles() {
        return cells;
    }

    public double[] getRange() {
        return range;
    }

    public boolean isTerminalMode() {
        return terminalMode;
    }

    static class Pair<K extends Comparable<K>, V> implements Comparable<Pair<K, V>> {
        public K key;
        public V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(Pair<K, V> o) {
            return this.key.compareTo(o.key);
        }
    }
}

