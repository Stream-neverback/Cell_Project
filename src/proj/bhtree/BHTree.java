package proj.bhtree;
import proj.Cell;
import proj.bhtree.QuadNode;
import edu.princeton.cs.algs4.StdOut;
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
        if (this.qNode.contains(cell.getX(), cell.getY())) {
            if (this.cell == null) {
                this.cell = cell;
                return;
            }
            if (!hasLeaf()) { // no subtrees
                this.createSubTrees();
                insertChild(cell);
            } else {
//                insertChild(this.cell);
                insertChild(cell);
            }
        }
        else{
            StdOut.println("Warning: Cell is out of BHTree!");
        }
    }


    private void insertChild(Cell b) {
        if (b.in(nwChild.qNode))
            nwChild.insert(b);
        else if (b.in(neChild.qNode))
            neChild.insert(b);
        else if (b.in(seChild.qNode))
            seChild.insert(b);
        else if (b.in(swChild.qNode))
            swChild.insert(b);
    }
    public void createSubTrees() {
        this.nwChild = new BHTree(qNode.NW_Q());
        this.neChild = new BHTree(qNode.NE_Q());
        this.swChild= new BHTree(qNode.SW_Q());
        this.seChild = new BHTree(qNode.SE_Q());
    }

    private boolean hasLeaf() {
        return (nwChild == null && neChild == null && swChild == null && seChild == null);
    }

//    public boolean checkDetection(QuadNode qnode) {
//        double detection_length = this.cell.getPerception_r();
//        double x_pos = this.cell.getX();
//        double y_pos = this.cell.getY();
//    }



    public String toString() {
        return String.format("cell %s\r\n nw: %s\tne: %s\tsw: %s\tse:%s", cell, nwChild, neChild, swChild, seChild);
    }

    
}
