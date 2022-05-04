package proj.bhtree;
import proj.Cell;
import proj.bhtree.QuadNode;
public class BHTree {
    private Cell cell;
    private final QuadNode qNode;
    private BHTree nwChild;
    private BHTree neChild;
    private BHTree swChild;
    private BHTree seChild;

    public BHTree(QuadNode q) {
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
            nwChild = new BHTree(new QuadNode(qNode.getX() - qNode.getLength() /4.0,
                    qNode.getY() + qNode.getLength() /4.0, qNode.getLength() /2.0));
            neChild = new BHTree(new QuadNode(qNode.getX() + qNode.getLength() /4.0,
                    qNode.getY() + qNode.getLength() /4.0, qNode.getLength() /2.0));
            seChild = new BHTree(new QuadNode(qNode.getX() + qNode.getLength() /4.0,
                    qNode.getY() - qNode.getLength() /4.0, qNode.getLength() /2.0));
            swChild = new BHTree(new QuadNode(qNode.getX() - qNode.getLength() /4.0,
                    qNode.getY() - qNode.getLength() /4.0, qNode.getLength() /2.0));

            _insertRealChild(this.cell);
            _insertRealChild(cell);

//            this.cell = this.cell.add(cell);
        }
    }


    private void _insertRealChild(Cell b) {
        if (b.in(new QuadNode(qNode.getX() - qNode.getLength() /4.0, qNode.getY() + qNode.getLength() /4.0, qNode.getLength() /2.0)))
            nwChild.insert(b);
        else if (b.in(new QuadNode(qNode.getX() + qNode.getLength() /4.0, qNode.getY() + qNode.getLength() /4.0, qNode.getLength() /2.0)))
            neChild.insert(b);
        else if (b.in(new QuadNode(qNode.getX() + qNode.getLength() /4.0, qNode.getY() - qNode.getLength() /4.0, qNode.getLength() /2.0)))
            seChild.insert(b);
        else if (b.in(new QuadNode(qNode.getX() - qNode.getLength() /4.0, qNode.getY() - qNode.getLength() /4.0, qNode.getLength() /2.0)))
            swChild.insert(b);
    }

    private boolean isLeaf() {
        return (nwChild == null && neChild == null && swChild == null && seChild == null);
    }




    public String toString() {
        return String.format("cell %s\r\n nw: %s\tne: %s\tsw: %s\tse:%s", cell, nwChild, neChild, swChild, seChild);
    }

    
}
