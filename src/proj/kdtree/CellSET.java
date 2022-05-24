package proj.kdtree;


//import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import proj.Cell;

import java.util.ArrayList;
import java.util.Collections;


public class CellSET {


    private double rangeXMin;
    private double rangeXMax;
    private double rangeYMin;
    private double rangeYMax;
    private double collisionRange;
    private double largestRadius;



    private SET<Cell> rb;


    public CellSET(double xmin, double ymin, double xmax, double ymax, double largestRadius) {
        rangeXMin = xmin;
        rangeXMax = xmax;
        rangeYMin = ymin;
        rangeYMax = ymax;
        this.largestRadius = largestRadius;
        collisionRange = largestRadius * 2 + 2.0 / 15;
        rb = new SET<>();
    }


    public boolean isEmpty() {
        return rb.isEmpty();
    }


    public int size() {
        return rb.size();
    }


    public void insert(Cell p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called insert() with a null Cell");
        rb.add(p);
    }


    public boolean contains(Cell p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called contains() with a null Cell");
        return rb.contains(p);
    }


    public void draw() {
        for (Cell p: rb) p.draw();
    }


    public Iterable<Cell> range(RectHV rect) {
        if (rect == null) throw new java.lang.NullPointerException(
                "called range() with a null RectHV");

        // Touch each point to see whether the given rect contains it.
        Stack<Cell> ans = new Stack<>();
        for (Cell p: rb) {
            if (rect.contains(p)) ans.push(p);
        }
        return ans;
    }


    public Cell nearest(Cell p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called contains() with a null Cell");

        if (rb.isEmpty()) return null;

        // Initialize loop variables
        Cell closestPoint = null; // null initialization required by Java?
        double closestDistance = Double.MAX_VALUE;

        // Touch every point to see which is the closest to the given point p
        for (Cell point: rb) {
            double currentDistance = p.distanceTo(point);
            if (currentDistance < closestDistance) {
                closestDistance = currentDistance;
                closestPoint = point;
            }
        }

        return closestPoint;
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
        double xmin = cell.getX() - collisionRange - largestRadius;
        double xmax = cell.getX() + collisionRange + largestRadius;
        double ymin = cell.getY() - collisionRange - largestRadius;
        double ymax = cell.getY() + collisionRange + largestRadius;
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

    }
}
