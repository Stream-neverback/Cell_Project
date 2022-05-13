package proj.kdtree;


//import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import proj.Cell;


public class CellSET {
    private SET<Cell> rb;

    /**
     * Construct an empty set of points.
     */
    public CellSET() {
        rb = new SET<>();
    }

    /**
     * Is the set empty?
     * @return {@code true} if this SET is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return rb.isEmpty();
    }

    /**
     * @return the number of points in the set.
     */
    public int size() {
        return rb.size();
    }

    /**
     * Add the point to the set (if it is not already in the set).
     *
     * Note that no check for presence is made here before attempting to add,
     * since the documentation of algs4.SET explicitly states:
     * "Adds the key to this set (if it is not already present)"
     *
     * Note however that this implementation explicitly checks for a null
     * argument,even though algs4.SET also performs this check.  This is
     * because the assignment API requires this check to be made.
     *
     * In the worst case, this implementation takes time proportional to the
     * logarithm of the number of points in the set.
     * This is because, in the worst case, algs4.SET.add() takes logarithmic
     * time.
     * That is because, in the worst case, java.util.TreeSet.add() takes
     * logarithmic time.
     *
     * @param p the point to add
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public void insert(Cell p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called insert() with a null Point2D");
        rb.add(p);
    }

    /**
     * Does the set contain point p?
     *
     * In the worst case, this implementation takes time proportional to the
     * logarithm of the number of points in the set.
     * This is because, in the worst case, algs4.SET.contains() takes
     * logarithmic time.
     * That is because, in the worst case, java.util.TreeSet.contains() takes
     * logarithmic time.
     *
     * @param p the point to look for
     * @return {@code true} if the SET contains point p;
     *         {@code false} otherwise
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public boolean contains(Cell p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called contains() with a null Point2D");
        return rb.contains(p);
    }

    /**
     * Draw all points to standard draw.
     */
    public void draw() {
        for (Cell p: rb) p.draw();
    }

    /**
     * All points that are inside the rectangle.
     *
     * In the worst (and best) case, this implementation takes time
     * proportional to the number of points in the set.
     *
     * @param rect the RectHV within which to look for points
     * @return an iterator to all of the points within the given RectHV
     * @throws NullPointerException if {@code rect} is {@code null}
     */
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

    /**
     * A nearest neighbor in the set to point p; null if the set is empty.
     *
     * In the worst (and best) case, this implementation takes time
     * proportional to the number of points in the set.
     *
     * @param p the point from which to search for a neighbor
     * @return the nearest neighbor to the given point p,
     *         {@code null} otherwise.
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public Cell nearest(Cell p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called contains() with a null Point2D");

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

    /**
     * Unit testing of the methods (optional).
     * @param args
     */
    public static void main(String[] args) {

    }
}
