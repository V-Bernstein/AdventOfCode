import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Part1Solution {
    private final static String FILE_NAME = "input.txt";
    private final static int INPUT_LENGTH = 100;
    private final static int INPUT_WIDTH = 100;

    public static void main(String[] args) {
        int[][] heightMap = parseInput(FILE_NAME);
        if (heightMap == null) {
	        System.out.println("Error parsing file");
	        return;
	    }

        boolean[][] isLowest = new boolean[INPUT_LENGTH][INPUT_WIDTH];
        for (int i = 0; i < INPUT_LENGTH; i++) { // Set all fields to true instead of the default false
            Arrays.fill(isLowest[i], true);
        }
        int riskSum = 0;
        for (int row = 0; row < INPUT_LENGTH; row++) {
            for (int col = 0; col < INPUT_WIDTH; col++) {
                if (isLowest[row][col]) {
                    // Check if actually lowest
                    boolean low = true; 
                    int curHeight = heightMap[row][col];
                    boolean aboveExists = row -1 > -1;
                    boolean rightExists = col + 1 < INPUT_WIDTH;
                    boolean belowExists = row + 1 < INPUT_LENGTH;
                    boolean leftExists = col - 1 > -1;
                    // above
                    if (aboveExists) {
                        low = low && curHeight < heightMap[row-1][col];
                    }
                    // right 
                    if (rightExists) {
                        low = low && curHeight < heightMap[row][col+1];
                    }
                    // below
                    if (belowExists) {
                        low = low && curHeight < heightMap[row+1][col];
                    }
                    // left
                    if (leftExists) {
                        low = low && curHeight < heightMap[row][col-1];
                    }
                    if (low) { // Set all adjacent squares to false. Technically only below and left matter *shrug*
                        riskSum += curHeight + 1;
                        if (aboveExists) {
                            isLowest[row-1][col] = false;
                        }
                        if (rightExists) {
                            isLowest[row][col+1] = false;
                        }
                        if (belowExists) {
                            isLowest[row+1][col] = false;
                        }
                        if (leftExists) {
                            isLowest[row][col-1] = false;
                        }
                    }
                }
            }
        }
        System.out.println("Solution is: " + riskSum);
    }

   private static int[][] parseInput(String fileName) {
        int[][] retArr = new int[INPUT_LENGTH][INPUT_WIDTH];
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
           int row = 0;
	       while ((line = br.readLine()) != null) {
    	   	   String[] heightStrings = line.split("");
               int[] heights = new int[INPUT_WIDTH];
    	       for (int i = 0; i < heightStrings.length; i++) {
                    heights[i] = Integer.parseInt(heightStrings[i]);
    		   }
               retArr[row] = heights; 
               row++;
    	   }
    	} catch(Exception e) {
	       System.out.println(e);
           return null;
	    }
    	return retArr;
   }
}
