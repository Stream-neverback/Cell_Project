package proj;

import java.awt.*;
import java.io.*;
import java.util.Random;

public class RandomSampleGenerate {

    public static void SampleGenerate(){
        Random random = new Random();
        String path = "../script/random_sample.txt";
        File f=new File(path);
        FileOutputStream fos1= null;
        try {
            fos1 = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter dos1=new OutputStreamWriter(fos1);
        int areaX = random.nextInt(1000)+50;
        int areaY = random.nextInt(1000)+50;
        int cellTotal = (int) Math.sqrt(areaX * areaY) * 2;
        double cellTotalD = cellTotal;
        Cell[] cell = new Cell[cellTotal];
        try {
            dos1.write(areaX + " " + areaY + "\n" + cellTotal + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < cellTotal; i++) {
            String write = "";
            double x = 0;
            double y = 0;
            double r = 0;
            double p = 0;
            String c = "r";
            Color color = Color.BLUE;
            Cell singelCell;
            boolean flag = false;
            while(!flag) {
                x = random.nextInt(areaX);
                y = random.nextInt(areaY);
                r = random.nextInt((int) (Math.min(areaX,areaY)/Math.round((Math.sqrt(cellTotalD)))));
                r = r / 2;
                p = 20 * random.nextInt((int) (Math.min(areaX,areaY)/Math.round((Math.sqrt(cellTotalD)))));
                if (x - r < 0 || y - r < 0 || x + r > areaX || y + r > areaY || r <= 0 || p <= 0) continue;
                int k = random.nextInt(4);
                switch (k){
                    case(0): {
                        c="r";
                        color = Color.RED;
                        break;
                    }
                    case(1): {
                        c="g";
                        color = Color.GREEN;
                        break;
                    }
                    case(3): {
                        c="y";
                        color = Color.YELLOW;
                        break;
                    }
                    default: {
                        c="b";
                        color = Color.BLUE;
                        break;
                    }
                }
                singelCell = new Cell(r, x, y, color, p);
                if (i == 0) break;
                for (int j = 0; j < i; j++) {
                    if (singelCell.Cell_Overlap(cell[j]) || singelCell.Cell_inOtherCell(cell[j])) {
                        flag = false;
                        break;
                    }
                    else if (j == i - 1) flag = true;
                }
            }
            write = x + " " + y + " " + r + " " + p + " " + c + "\n";
            cell[i] = new Cell(r, x, y, color, p);
//            for (int j = 0; j < i; j++) {
//                for (int k = 0; k < i; k++) {
//                    if (cell[j].Cell_Overlap(cell[k]) && j != k) {
//                        System.out.println("cell " + j + " overlaps with cell " + k);
//                    }
//                }
//            }
            try {
                dos1.write(write);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            dos1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        SampleGenerate();
    }

}
