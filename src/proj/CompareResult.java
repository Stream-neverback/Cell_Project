package proj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CompareResult {
    public static void main(String[] args){
        String path1 = "../result/result.txt";
        File file1 = new File(path1);
        Scanner input1 = null;
        try {
            input1 = new Scanner(file1);
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner input2 = new Scanner(System.in);
        int wrong_cnt = 0; // count how many wrongs are there
        int total_cnt = 0;
        while (true) {
            total_cnt++;
            double res_x;
            try {
                res_x = input1.nextDouble();
            } catch (
                    NoSuchElementException e) {
//                e.printStackTrace();
                break;
            }
            double res_y = input1.nextDouble();
            String res_c = input1.next();
            double ans_x = input2.nextDouble();
            double ans_y = input2.nextDouble();
            String ans_c = input2.next();
            boolean compare_0 = Math.abs(res_x - ans_x) < 0.001;
            boolean compare_1 = Math.abs(res_y - ans_y) < 0.001;
            if (!compare_0 || !compare_1 || !res_c.equals(ans_c)) {
                System.out.println(total_cnt + " is wrong!");
                System.out.println("wrong! answer is: " + ans_x + " " + ans_y + " " + ans_c);
                System.out.println("your answer is: " + res_x + " " + res_y + " " + res_c);
                wrong_cnt++;
            }
        }
        System.out.println("Total wrongs: " + wrong_cnt + ", ratio: " + wrong_cnt / (double) total_cnt);
    }

}
