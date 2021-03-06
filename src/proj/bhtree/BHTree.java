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
            if (!hasNoLeaf()) { // have subtrees internal node
//                this.createSubTrees();
                insertChild(cell);
            } else { // external node
//                insertChild(this.cell);
                this.createSubTrees();
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

    private boolean hasNoLeaf() {
        return (nwChild == null && neChild == null && swChild == null && seChild == null);
    }

    public void checkCollision(Cell cell){
        double x_pos = cell.getX();
        double y_pos = cell.getY();
        double radius = cell.getRadius();
        cell.future_move();
        if(cell.getMoveMode()) {
            if (this.cell != null) {
                if (hasNoLeaf()) { // No subtree, no need to go on
//            System.out.println("True");
//                    if (this.qNode.contains_circle(cell.get_future_x(), cell.get_future_y(), radius)) {
                        if (cell.Cell_Overlap(this.cell) && this.cell.id != cell.id) {
                            cell.setMoveMode(false);
                            cell.moveUntilContact(this.cell);
                        }
//                    }
                } else {
//                    if (this.qNode.contains_circle(cell.get_future_x(), cell.get_future_y(), radius)) {
                        if (cell.Cell_Overlap(this.cell) && this.cell.id != cell.id) {
                            cell.setMoveMode(false);
                            cell.moveUntilContact(this.cell);
                        }
//                    }
//                    System.out.printf("distance is %f\r\n", cell.future_distanceTo(this.cell));
                    this.nwChild.checkCollision(cell);
                    this.neChild.checkCollision(cell);
                    this.swChild.checkCollision(cell);
                    this.seChild.checkCollision(cell);
                }
            }
        }
    }

    public void checkDetection(Cell cell) {
//        System.out.println("check");
        double detection_half_length = cell.getPerception_r();
        double x_pos = cell.getX();
        double y_pos = cell.getY();
        if(this.cell != null) {
            if (hasNoLeaf()) { // No subtree, no need to go on
//            System.out.println("True");
                if (this.qNode.contains_rec(x_pos, y_pos, detection_half_length)) {
                    if (cell.inDetection(this.cell) && this.cell.id != cell.id) {
                        cell.add_num(this.cell);
//                        System.out.println(cell.red_num);
//                        cell.check_color();
//                        cell.reset_num();
                    }
                }
            } else {
                if (this.qNode.contains_rec(x_pos, y_pos, detection_half_length)) {
                    if (cell.inDetection(this.cell) && this.cell.id != cell.id) {
//                    System.out.println("False");
                        cell.add_num(this.cell);
//                        System.out.println(cell.red_num);
                    }
                }
                this.nwChild.checkDetection(cell);
                this.neChild.checkDetection(cell);
                this.swChild.checkDetection(cell);
                this.seChild.checkDetection(cell);
            }
        }
    }
    public static void searchNode(){

    }

    public static void changeColor(){
        // length = 0 return
        // else, change color in ranges.
    }




    public String toString() {
        return String.format("cell %s\r\n nw: %s\tne: %s\tsw: %s\tse:%s", cell, nwChild, neChild, swChild, seChild);
    }

    public BHTree getNeChild() {
        return neChild;
    }

    public BHTree getNwChild() {
        return nwChild;
    }

    public BHTree getSeChild() {
        return seChild;
    }

    public BHTree getSwChild() {
        return swChild;
    }

    public Cell getCell() {
        return cell;
    }
}
