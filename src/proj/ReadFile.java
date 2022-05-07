package proj;

import java.awt.*;
import java.io.*;
import java.util.Arrays;

public class ReadFile {
    private static String[] words = new String[2000]; // assuming length is less than 2000

    public static void main(String[] args) throws IOException {
        int m = 0;
        int state = 0; // state = 0 -> read area; state = 1, 2 -> read cell info; state = 3, 4 -> read query
        int count = 0; // number of cells
        int count_tmp = 0; // tmp counter
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("D:\\Learning\\SUSTech2022春\\DSAA\\proj\\Cell_Project\\src\\proj\\input.txt")));
            String line_str; // read line by lilne
            while ((line_str = br.readLine()) != null) {
                String[] str_split = line_str.split(" ");
                if (state == 0) { // read area
                    int x = Integer.parseInt(str_split[0]); // area x
                    int y = Integer.parseInt(str_split[1]); // area y
                    System.out.println(x + " " + y);
                    state = 1;
                } else if (state == 1) { // read number of cells
                    count = Integer.parseInt(str_split[0]);
                    System.out.println(count);
                    state = 2;
                } else if (state == 2) {
                    double x = Double.parseDouble(str_split[0].toString()); // input coordinate x of the cell
                    double y = Double.parseDouble(str_split[1].toString()); // input coordinate y of the cell
                    double r = Double.parseDouble(str_split[2].toString()); // input radius of the cell
                    double p = Double.parseDouble(str_split[3].toString()); // input perception range of the cell
                    String c = str_split[4]; // input color of the cell
                    Color color = c.equals("r") ? Color.RED : c.equals("g") ? Color.GREEN : c.equals("b") ? Color.BLUE : Color.YELLOW; // judge colors
                    System.out.println(x + " " + y + " " + r + " " + p + " " + c);
                    count_tmp += 1;
                    if (count_tmp == count) {
                        state = 3;
                        count_tmp = 0;
                    }
                } else if (state == 3) { // read length of query
                    count = Integer.parseInt(str_split[0]);
                    System.out.println(count);
                    state = 4;
                } else if (state == 4) { // read info of query cells
                    int x = Integer.parseInt(str_split[0]); // time
                    int y = Integer.parseInt(str_split[1]); // cell index
                    System.out.println(x + " " + y);
                    count_tmp += 1;
                    if (count_tmp == count) state = 5;
                }
            }
            br.close(); // close file io
        } catch (Exception e) {
            System.out.println("File IO exception!");
            e.printStackTrace();
        }
    }

}