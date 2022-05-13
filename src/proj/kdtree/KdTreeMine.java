package proj.kdtree;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import proj.Cell;

import java.awt.*;

public class KdTreeMine {
    private Node root;
    private int size;
    private double rangeXMin;
    private double rangeXMax;
    private double rangeYMin;
    private double rangeYMax;

    /**
     * Construct an empty set of points.
     */
    public KdTreeMine(double xmin,double ymin, double xmax, double ymax) {
        rangeXMin=xmin;
        rangeXMax=xmax;
        rangeYMin=ymin;
        rangeYMax=ymax;
        size = 0;
    }

    /**
     * Is the set empty?
     *
     * Note that, while the Nodes in BST store their sizes, that is not done
     * in this implementation.
     *
     * Accordingly, this simplifies the code from a call to a private helper
     * size method to a check of whether or not the root node is null.
     *
     * @return {@code true} if this set is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * @return the number of points in the set.
     */
    public int size() {
        return size;
    }

    /**
     * Add the point to the set (if it is not already in the set).
     *
     * At the root (and every second level thereafter), the x-coordinate is
     * used as the key.
     *
     * This means that if (0.7, 0.2) is the root, then (0.5, 0.9) will be
     * added to the left, since its x-coordinate is smaller than the
     * x-coordinate of the root node.  Similarly, if the next point to be
     * added is (0.8, 0.1), that point will be added to the right of root,
     * since its x-coordinate is larger than the x-coordinate of the root node.
     *
     * So, visually, we would have:
     *       (0.7, 0.2)
     *      /          \
     * (0.5, 0.9)   (0.8, 0.1)
     *
     *
     * At one level below the root (and every second level thereafter), the
     * y-coordinate is used as the key.
     *
     * This means that if we next add (0.6, 0.8), it will be added to the left
     * of (0.5, 0.9).  Similarly, if we next add (0.4, 0.95), it will be added
     * to the right of (0.5, 0.9).
     *
     * So, visually, we would have:
     *              (0.7, 0.2)
     *             /          \
     *        (0.5, 0.9)   (0.8, 0.1)
     *       /          \
     * (0.6, 0.8)   (0.4, 0.95)
     *
     *
     * @param p the point to add
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public void insert(Cell p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called insert() with a null Cell");

        // new double[] {x_min, y_min, x_max, y_max)
        root = insert(root, p, true, new double[] {rangeXMin, rangeYMin, rangeXMax, rangeYMax});
    }

    private Node insert(Node n, Cell p, boolean evenLevel, double[] coords) {
        if (n == null) {
            size++;
            return new Node(p, coords);
        }

        double cmp = comparePoints(p, n, evenLevel);

        /**
         * Traverse down the BST.
         *
         * In subsequent levels, the orientation is orthogonal
         * to the current orientation.
         *
         * Place the point in the left or right nodes accordingly.
         *
         * If the comparison is not affirmatively left or right, then it could
         * be that we're considering literally the same point, in which case
         * the size shouldn't increase, or that we're considering a point
         * which lies on the same partition line, which would need to be added
         * to the BST and increase the size accordingly.
         */

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
        }

        /**
         * Handle Nodes which lie on the same partition line,
         * but aren't the same point.
         *
         * As per the checklist, these "ties" are resolved in favor of the
         * right subtree.
         *
         * It is assumed that the RectHV to be created cannot be shrunk
         * at all, and so none of coords[] values are updated here.
         */
        else if (!n.p.equals(p))
            n.rt = insert(n.rt, p, !evenLevel, coords);

        /**
         * Do nothing for a point which is already in the BST.
         * This is because the BST contains a "set" of points.
         * Hence, duplicates are silently dropped, rather than
         * being added.
         */

        return n;
    }

    /**
     * Does the set contain point p?
     *
     * In the worst case, this implementation takes time proportional to the
     * logarithm of the number of points in the set.
     *
     * @param p the point to look for
     * @return {@code true} if the set contains point p;
     *         {@code false} otherwise
     * @throws NullPointerException if {@code p} is {@code null}
     */
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

    /**
     * Draw all points and partition lines to standard draw.
     */
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
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.getY(), n.rect.xmax(), n.p.getY());
        }

        // Traverse the right Nodes
        draw(n.rt, !evenLevel);
    }

    /**
     * All points that are inside the rectangle.
     *
     * In the worst case, this implementation takes time
     * proportional to the number of points in the set.
     *
     * However, unlike PointSET.range(), in the best case, this implementation
     * takes time proportional to the logarithm of the number of
     * points in the set.
     *
     * @param rect the RectHV within which to look for points
     * @return an iterator to all of the points within the given RectHV
     * @throws NullPointerException if {@code rect} is {@code null}
     */
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

            /**
             * Add Nodes containing promising rectangles to our nodes stack.
             *
             * Note that, since we don't push Nodes onto the stack unless
             * their rectangles intersect with the given RectHV, we achieve
             * pruning as we traverse the BST.
             */
            if (tmp.lb != null && rect.intersects(tmp.lb.rect)) {
                nodes.push(tmp.lb);
            }
            if (tmp.rt != null && rect.intersects(tmp.rt.rect)) {
                nodes.push(tmp.rt);
            }
        }
        return points;
    }



    /**
     * A nearest neighbor in the set to point p; null if the set is empty.
     *
     * In the worst case, this implementation takes time
     * proportional to the number of points in the set.
     *
     * However, unlike PointSET.nearest(), in the best case, this
     * implementation takes time proportional to the logarithm of
     * the number of points in the set.
     *
     * @param p the point from which to search for a neighbor
     * @return the nearest neighbor to the given point p,
     *         {@code null} otherwise.
     * @throws NullPointerException if {@code p} is {@code null}
     */
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

        /**
         * Calculate the distance from the search point to the current
         * Node's partition line.
         *
         * Primarily, the sign of this calculation is useful in determining
         * which side of the Node to traverse next.
         *
         * Additionally, the magnitude to toPartitionLine is useful for pruning.
         *
         * Specifically, if we find a champion whose distance is shorter than
         * to a previous partition line, then we know we don't have to check any
         * of the points on the other side of that partition line, because none
         * can be closer.
         */
        double toPartitionLine = comparePoints(p, n, evenLevel);

        /**
         * Handle the search point being to the left of or below
         * the current Node's point.
         */
        if (toPartitionLine < 0) {
            champion = nearest(n.lb, p, champion, !evenLevel);

            // Since champion may have changed, recalculate distance
            if (champion.distanceSquaredTo(p) >=
                    toPartitionLine * toPartitionLine) {
                champion = nearest(n.rt, p, champion, !evenLevel);
            }
        }

        /**
         * Handle the search point being to the right of or above
         * the current Node's point.
         *
         * Note that, since insert() above breaks point comparison ties
         * by placing the inserted point on the right branch of the current
         * Node, traversal must also break ties by going to the right branch
         * of the current Node (i.e. to the right or top, depending on
         * the level of the current Node).
         */
        else {
            champion = nearest(n.rt, p, champion, !evenLevel);

            // Since champion may have changed, recalculate distance
            if (champion.distanceSquaredTo(p) >=
                    toPartitionLine * toPartitionLine) {
                champion = nearest(n.lb, p, champion, !evenLevel);
            }
        }

        return champion;
    }

    /**
     * The distance and direction from the given point to the given Node's
     * partition line.
     *
     * If the sign of the returned double is negative, then the given point
     * lies or should lie on the left branch of the given Node.
     *
     * Otherwise (including where the difference is exactly 0), then the
     * given point lies or should lie on the right branch of the given Node.
     *
     * @param p the point in question
     * @param n the Node in question
     * @param evenLevel is the current level even?  If so, then the Node's
     *        partition line is vertical, so the point will be to the left
     *        or right of the Node.  If not, then the Node's partition line
     *        is horizontal, so the point will be above or below the Node.
     * @return the distance and direction from p to n's partition line
     */
    private double comparePoints(Cell p, Node n, boolean evenLevel) {
        if (evenLevel) {
            return p.getX() - n.p.getX();
        }
        else return p.getY() - n.p.getY();
    }

    /**
     * The data structure from which a KdTree is created.
     */
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


    public void checkDetection(Cell cell){//隐患 若x,y超出边界可能有问题
        double xmin = cell.getX()-cell.getPerception_r();
        double xmax = cell.getX()+cell.getPerception_r();
        double ymin = cell.getY()-cell.getPerception_r();
        double ymax = cell.getY()+cell.getPerception_r();

        Iterable<Cell> cellsInRange = this.range(new RectHV(xmin,ymin,xmax,ymax));
        if(cellsInRange!=null){
            for(Cell cell1:cellsInRange){
                cell.add_num(cell1);
            }

        }
    }




    /**
     * Unit testing of the methods (optional).
     * @param args
     */
    public static void main(String[] args) {
        KdTreeMine kdtree = new KdTreeMine(0,0,1,1);
        Cell p = new Cell(0.1, 0.5, 0, Color.RED, 10);
        kdtree.insert(p);
        StdOut.println(kdtree.contains(p));
    }
}

