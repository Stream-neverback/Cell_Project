package bhtree;
import project.Cell;
public class BHTree {
    private Cell cell;
    private final QNode qNode;
    private BHTree nwChild;
    private BHTree neChild;
    private BHTree swChild;
    private BHTree seChild;

    public BHTree(QNode q) {
        this.qNode = q;
        this.cell = null;
        this.nwChild = null;
        this.neChild = null;
        this.swChild = null;
        this.seChild = null;
    }

    public void insert(Cell cell) {
        if (this.cell == null) {
            this.cell = cell;
            return;
        }

        if (! isLeaf()) {
//            this.cell = this.cell.add(cell);
            _insertRealChild(cell);
        }

        else {
            nwChild = new BHTree(new QNode(qNode.centerX - qNode.range /4.0,
                    qNode.centerY + qNode.range /4.0, qNode.range /2.0));
            neChild = new BHTree(new QNode(qNode.centerX + qNode.range /4.0,
                    qNode.centerY + qNode.range /4.0, qNode.range /2.0));
            seChild = new BHTree(new QNode(qNode.centerX + qNode.range /4.0,
                    qNode.centerY - qNode.range /4.0, qNode.range /2.0));
            swChild = new BHTree(new QNode(qNode.centerX - qNode.range /4.0,
                    qNode.centerY - qNode.range /4.0, qNode.range /2.0));

            _insertRealChild(this.cell);
            _insertRealChild(cell);

//            this.cell = this.cell.add(cell);
        }
    }


    private void _insertRealChild(Cell b) {
        if (b.in(new QNode(qNode.centerX - qNode.range /4.0, qNode.centerY + qNode.range /4.0, qNode.range /2.0)))
            nwChild.insert(b);
        else if (b.in(new QNode(qNode.centerX + qNode.range /4.0, qNode.centerY + qNode.range /4.0, qNode.range /2.0)))
            neChild.insert(b);
        else if (b.in(new QNode(qNode.centerX + qNode.range /4.0, qNode.centerY - qNode.range /4.0, qNode.range /2.0)))
            seChild.insert(b);
        else if (b.in(new QNode(qNode.centerX - qNode.range /4.0, qNode.centerY - qNode.range /4.0, qNode.range /2.0)))
            swChild.insert(b);
    }

    private boolean isLeaf() {
        return (nwChild == null && neChild == null && swChild == null && seChild == null);
    }


    public void updateGravity(Cell cell) {

        if (this.cell == null || cell.equals(this.cell))
            return;

        if (isLeaf())
            cell.applyGravity(this.cell);
        else {
            double s = qNode.length();
            double d = this.cell.distanceTo(cell);
            double THETA = 0.5;
            if ((s / d) < THETA)
//                cell.applyGravity(this.cell);
            else {
                nwChild.updateGravity(cell);
                neChild.updateGravity(cell);
                swChild.updateGravity(cell);
                seChild.updateGravity(cell);
            }
        }
    }

    public String toString() {
        return String.format("cell %s\r\n nw: %s\tne: %s\tsw: %s\tse:%s", cell, nwChild, neChild, swChild, seChild);
    }

    public static class QNode {

        private final double centerX;
        private final double centerY;
        private final double range;

        public QNode(double centerX, double centerY, double range) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.range = range;
        }

        public double length() {
            return range;
        }

        public boolean contains(double x, double y) {
            double halfLen = this.range / 2.0;
            return (x <= this.centerX + halfLen &&
                    x >= this.centerX - halfLen &&
                    y <= this.centerY + halfLen &&
                    y >= this.centerY - halfLen);
        }

        @Override
        public String toString() {
            return String.format("%f %f %f", centerX, centerY, range);
        }
    }
}
