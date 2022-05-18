package proj.kdtree;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import proj.Cell;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class KdTreeMine {
    private Node root;
    private int size;
    private double rangeXMin;
    private double rangeXMax;
    private double rangeYMin;
    private double rangeYMax;
    private double collisionRange;
    private double largestRadius;


    public KdTreeMine(double xmin, double ymin, double xmax, double ymax, double largestRadius) {
        rangeXMin = xmin;
        rangeXMax = xmax;
        rangeYMin = ymin;
        rangeYMax = ymax;
        this.largestRadius = largestRadius;
        collisionRange = largestRadius * 2 + 2 / 15;
        size = 0;
    }


    public boolean isEmpty() {
        return root == null;
    }


    public int size() {
        return size;
    }


    public void insert(Cell p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called insert() with a null Cell");

        // new double[] {x_min, y_min, x_max, y_max)
        root = insert(root, p, true, new double[]{rangeXMin, rangeYMin, rangeXMax, rangeYMax});
    }

    private Node insert(Node n, Cell p, boolean evenLevel, double[] coords) {
        if (n == null) {
            size++;
            return new Node(p, coords);
        }

        double cmp = comparePoints(p, n, evenLevel);


        // Handle Nodes which should be inserted to the left
        if (cmp < 0 && evenLevel) {
            coords[2] = n.p.getX(); // lessen x_max
            n.lb = insert(n.lb, p, !evenLevel, coords);
        }

        // Handle Nodes which should be inserted to the bottom
        else if (cmp < 0 && !evenLevel) {
            coords[3] = n.p.getY(); // lessen y_max
            n.lb = insert(n.lb, p, !evenLevel, coords);
        }

        // Handle Nodes which should be inserted to the right
        else if (cmp > 0 && evenLevel) {
            coords[0] = n.p.getX(); // increase x_min
            n.rt = insert(n.rt, p, !evenLevel, coords);
        }

        // Handle Nodes which should be inserted to the top
        else if (cmp > 0 && !evenLevel) {
            coords[1] = n.p.getY(); // increase y_min
            n.rt = insert(n.rt, p, !evenLevel, coords);
        } else if (!n.p.equals(p))
            n.rt = insert(n.rt, p, !evenLevel, coords);


        return n;
    }


    public boolean contains(Cell p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called contains() with a null Cell");
        return contains(root, p, true);
    }

    private boolean contains(Node n, Cell p, boolean evenLevel) {

        // Handle reaching the end of the search
        if (n == null) return false;

        // Check whether the search point matches the current Node's point
        if (n.p.equals(p)) return true;

        double cmp = comparePoints(p, n, evenLevel);

        // Traverse the left path when necessary
        if (cmp < 0) return contains(n.lb, p, !evenLevel);

            // Traverse the right path when necessary, and as tie-breaker
        else return contains(n.rt, p, !evenLevel);
    }


    public void draw() {
        draw(root, true);
    }

    private void draw(Node n, boolean evenLevel) {
        if (n == null) return;

        // Traverse the left Nodes
        draw(n.lb, !evenLevel);

        // Draw the current Node
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.p.draw();

        // Draw the partition line
        StdDraw.setPenRadius();
        if (evenLevel) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.getX(), n.rect.ymin(), n.p.getX(), n.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.getY(), n.rect.xmax(), n.p.getY());
        }

        // Traverse the right Nodes
        draw(n.rt, !evenLevel);
    }


    public Iterable<Cell> range(RectHV rect) {
        if (rect == null) throw new java.lang.NullPointerException(
                "called range() with a null RectHV");

        Stack<Cell> points = new Stack<>();

        // Handle KdTree without a root node yet
        if (root == null) return points;

        Stack<Node> nodes = new Stack<>();
        nodes.push(root);
        while (!nodes.isEmpty()) {

            // Examine the next Node
            Node tmp = nodes.pop();

            // Add contained points to our points stack
            if (rect.contains(tmp.p)) points.push(tmp.p);

            if (tmp.lb != null && rect.intersects(tmp.lb.rect)) {
                nodes.push(tmp.lb);
            }
            if (tmp.rt != null && rect.intersects(tmp.rt.rect)) {
                nodes.push(tmp.rt);
            }
        }
        return points;
    }


    public Cell nearest(Cell p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called contains() with a null Cell");
        if (isEmpty()) return null;
        return nearest(root, p, root.p, true);
    }

    private Cell nearest(Node n, Cell p, Cell champion,
                         boolean evenLevel) {

        // Handle reaching the end of the tree
        if (n == null) return champion;

        // Handle the given point exactly overlapping a point in the BST
        if (n.p.equals(p)) return p;

        // Determine if the current Node's point beats the existing champion
        if (n.p.distanceSquaredTo(p) < champion.distanceSquaredTo(p))
            champion = n.p;


        double toPartitionLine = comparePoints(p, n, evenLevel);


        if (toPartitionLine < 0) {
            champion = nearest(n.lb, p, champion, !evenLevel);

            // Since champion may have changed, recalculate distance
            if (champion.distanceSquaredTo(p) >=
                    toPartitionLine * toPartitionLine) {
                champion = nearest(n.rt, p, champion, !evenLevel);
            }
        } else {
            champion = nearest(n.rt, p, champion, !evenLevel);

            // Since champion may have changed, recalculate distance
            if (champion.distanceSquaredTo(p) >=
                    toPartitionLine * toPartitionLine) {
                champion = nearest(n.lb, p, champion, !evenLevel);
            }
        }

        return champion;
    }


    private double comparePoints(Cell p, Node n, boolean evenLevel) {
        if (evenLevel) {
            return p.getX() - n.p.getX();
        } else return p.getY() - n.p.getY();
    }


    private static class Node {

        // the point
        private final Cell p;

        // the axis-aligned rectangle corresponding to this node
        private final RectHV rect;

        // the left/bottom subtree
        private Node lb;

        // the right/top subtree
        private Node rt;

        private Node(Cell p, double[] coords) {
            this.p = p;
            rect = new RectHV(coords[0], coords[1], coords[2], coords[3]);
        }
    }


    public void checkDetection(Cell cell) {//隐患 若x,y超出边界可能有问题

        double xminOrg = cell.getX() - cell.getPerception_r();
        double xmaxOrg = cell.getX() + cell.getPerception_r();
        double yminOrg = cell.getY() - cell.getPerception_r();
        double ymaxOrg = cell.getY() + cell.getPerception_r();


        double xmin = cell.getX() - cell.getPerception_r() - largestRadius;
        double xmax = cell.getX() + cell.getPerception_r() + largestRadius;
        double ymin = cell.getY() - cell.getPerception_r() - largestRadius;
        double ymax = cell.getY() + cell.getPerception_r() + largestRadius;

        Iterable<Cell> cellsInRange = this.range(new RectHV(xmin, ymin, xmax, ymax));
        ArrayList<Cell> cellArrayList = new ArrayList<>();
        if (cellsInRange != null) {
            for (Cell cell1 : cellsInRange) {
                if (cell.id != cell1.id) {
                    if (cell1.in(xminOrg-cell1.getRadius(), yminOrg, xmaxOrg+cell1.getRadius(), ymaxOrg) || cell1.in(xminOrg, yminOrg-cell1.getRadius(), xmaxOrg, ymaxOrg+cell1.getRadius()) || cell1.nearToCorner(xminOrg, yminOrg, xmaxOrg, ymaxOrg)) {
                        cell.add_num(cell1);
                        cellArrayList.add(cell1);
                    }
                }
            }
//            System.out.println(cellArrayList.size());
        }
    }

    public void checkCollision(Cell cell) {
        cell.setMoveMode(true);
        double xmin = cell.getX() - collisionRange;
        double xmax = cell.getX() + collisionRange;
        double ymin = cell.getY() - collisionRange;
        double ymax = cell.getY() + collisionRange;
        Iterable<Cell> cellsInRange = this.range(new RectHV(xmin, ymin, xmax, ymax));
        ArrayList<Cell> cellsListOverlap = new ArrayList<>();
        if (cellsInRange == null) {
            return;
        }
        for (Cell cell1 : cellsInRange) {
            if (cell.Cell_Overlap(cell1) && cell.id != cell1.id) {
                cellsListOverlap.add(cell1);
//                    cell.setMoveMode(false);
//                    cell.moveUntilContact(cell1);
//                    return;
            }

        }
        if (cellsListOverlap.size() == 0) {
            return;
        }
        ArrayList<Double> distanceList = new ArrayList<>();
        for (Cell cell2 : cellsListOverlap) {
            distanceList.add(cell.unitDistanceUntilContact(cell2));
        }
        Cell cellMinDistance = cellsListOverlap.get(distanceList.indexOf(Collections.min(distanceList)));
//        System.out.println(Collections.min(distanceList));
//        System.out.println(cell.getY() + " go to " + cellMinDistance.getY());
        cell.setMoveMode(false);
        cellMinDistance.setMoveMode(false);
        if (cell.id != cellMinDistance.id) {
            cell.moveUntilContact(cellMinDistance);
        }
//        cell.setMoveMode(false);
//        cellMinDistance.setMoveMode(false);

    }


    public static void main(String[] args) {
        KdTreeMine kdtree = new KdTreeMine(0, 0, 1, 1, 1);
        Cell p = new Cell(0.1, 0.5, 0, Color.RED, 10);
        kdtree.insert(p);
        StdOut.println(kdtree.contains(p));
    }
}


