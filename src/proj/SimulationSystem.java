

import edu.princeton.cs.algs4.StdDraw;
import proj.bhtree.BHTree;
import proj.Cell;
import proj.Console;
//import proj.oltree.OverlapTree;

import java.util.*;

public class SimulationSystem {
    private static SimulationSystem instance;
    private double[] range;
    private Cell[] cells;
    private boolean isGUIMode;
    private double dt;
    public boolean hasCollision=true;
    public boolean noSpeedUp=false;
    public boolean noSpeedUpCollision=false;
    public boolean benchmark=false;

    static{
        instance = new SimulationSystem();
    }

    private SimulationSystem(){

    }

    public static SimulationSystem getInstance(){
        if(null == instance)
            instance = new SimulationSystem();
        return instance;
    }

    public void simulation(Console console, final double dt){
        Cell[] cells = console.getCells();
        double[] range = console.getRange();
        final boolean isGUIMode = !console.isTerminalMode();
        final Cell[] finalParticles = cells;
        if(range.length==1){
            double a = range[0];
            range = new double[4];
            range[0] = 0;  range[1] = a;
            range[2] = 0;  range[3] = a;
        }
        assert range.length==4;
        this.range = range;
        this.cells = cells;
        this.isGUIMode = isGUIMode;
        this.dt = dt;
        if(isGUIMode) {
            StdDraw.show();
            StdDraw.enableDoubleBuffering();
            StdDraw.setXscale(range[0], range[1]);
            StdDraw.setYscale(range[2], range[3]);
        }
        int cnt = 0;
        for (double t = 0.0; true; t = t + dt) {
            long begin = 0;
            if(benchmark)
                begin = System.nanoTime();
            if(noSpeedUp){
//                Arrays.stream(cells).parallel().forEach(cell -> {
//                    cell.clearGravity();
//                    Arrays.stream(particles).parallel().forEach(particle::applyGravity);
//                });
//                if(hasCollision) {
//                    Arrays.stream(particles).forEach(p -> p.collide(Arrays.asList(finalParticles)));
//                }
            }
            else{
                BHTree.QNode qNode = new BHTree.QNode((range[0]+range[1])/2, (range[2]+range[3])/2,
                    Math.max((range[1]-range[0]), (range[3]-range[2])));
                BHTree tree = new BHTree(qNode);
                Arrays.stream(cells).parallel()
                        .filter(p -> p.in(qNode))
                        .forEachOrdered(tree::insert);
                Arrays.stream(cells).parallel().forEach(particle -> {
//                    particle.clearGravity();
                    tree.updateGravity(particle);
                    particle.update(dt);
                });
//                if(hasCollision) {
//                    if(!noSpeedUpCollision){
//                        OverlapTree olTree = new OverlapTree(particles);
//                        olTree.putBuckets(particles);
//                        olTree.collision();
//                    } else{
//                        Arrays.stream(particles).forEach(p -> p.collide(Arrays.asList(finalParticles)));
//                    }
//                }
            }
            if(benchmark){
                long end = System.nanoTime();
                if(cnt%10==0)
                    System.err.println(String.format("%.3f", (end-begin)*1e-3)+"us @"+cells.length+"\tparticles\tnoSpeedUp="+noSpeedUp+"\tnoSpeedUpCollision="+noSpeedUpCollision+"\thasCollision="+hasCollision);
                cnt++;
            }
            if(isGUIMode) {
                StdDraw.clear(StdDraw.BLACK);
                Arrays.stream(cells).parallel().forEachOrdered(cells::draw);
                StdDraw.show();
            }
            else{

                while(true) {
                    if(console.queue.size()==0)
                        return;
                    Console.Pair<Double, Integer> pair = console.queue.get(0);
                    if(pair.key<=t){
                        Cell c = cells[pair.value];
                        System.out.println(c);
                        console.queue.remove(0);
                    }
                    else
                        break;
                }
            }
        }
    }

    public double[] getRange() {
        return range;
    }

    public Cell[] getCells() {
        return cells;
    }

    public boolean isGUIMode() {
        return isGUIMode;
    }

    public double getDt() {
        return dt;
    }

}
