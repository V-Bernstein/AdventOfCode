import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Part2Solution {
    private final static String FILE_NAME = "input.txt";
    private final static int NUM_ROWS = 10;
    private final static int NUM_COLS = 10;

    public static void main(String[] args) {
        int[][] inputs = parseInput(FILE_NAME);
        if (inputs == null) {
	        System.out.println("Error parsing file");
	        return;
	    }

        long numFlashes = 0l;
        int numSteps = 1;
        while (true) {
            boolean[][] hasFlashed = new boolean[NUM_ROWS][NUM_COLS]; // Defaulted to false
            incrementLevels(inputs);
            for (int i = 0; i < NUM_ROWS; i++) {
                for (int j = 0; j < NUM_COLS; j++) {
                    numFlashes += calcFlashes(inputs, hasFlashed, i, j);
                }
            }
            if (allFlashed(hasFlashed)) {
                break;
            }
            numSteps ++;
        }

        System.out.println("Solution is: " + numSteps);
    }

    private static boolean allFlashed(boolean[][] hasFlashed) {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (!hasFlashed[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static long calcFlashes(int[][] inputs, boolean[][] hasFlashed, int row, int col) {
        return calcFlashesHelper(inputs, hasFlashed, row, col, false);
    }

    private static long calcFlashesHelper(int[][] inputs, boolean[][] hasFlashed, 
                                          int row, int col, boolean adjacentToFlasher) {
        if (row < 0 || row >= NUM_ROWS || col < 0 || col >= NUM_COLS) {
            return 0;
        }

        if (hasFlashed[row][col]) { // Already flashed, don't increment, don't count
            return 0;
        }

        if (adjacentToFlasher) {
            inputs[row][col] += 1;
        }

        if (inputs[row][col] > 9) { // Octo is gonna flash
            hasFlashed[row][col] = true;
            inputs[row][col] = 0;
            return 1l 
                + calcFlashesHelper(inputs, hasFlashed, row - 1, col - 1, true) 
                + calcFlashesHelper(inputs, hasFlashed, row - 1, col, true) 
                + calcFlashesHelper(inputs, hasFlashed, row - 1, col + 1, true) 
                + calcFlashesHelper(inputs, hasFlashed, row, col - 1, true) 
                + calcFlashesHelper(inputs, hasFlashed, row, col + 1, true) 
                + calcFlashesHelper(inputs, hasFlashed, row + 1, col - 1, true) 
                + calcFlashesHelper(inputs, hasFlashed, row + 1, col, true) 
                + calcFlashesHelper(inputs, hasFlashed, row + 1, col + 1, true);
        }

        return 0;
    }
    
    private static void incrementLevels(int[][] arr) {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                arr[i][j] += 1;
            }
        }
    }

   private static int[][] parseInput(String fileName) {
        int[][] retArr = new int[NUM_ROWS][NUM_COLS];
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
           int[] temp = new int[NUM_ROWS];
           int row = 0;
	       while ((line = br.readLine()) != null) {
                String[] levels = line.split("");
                for (int i = 0; i < levels.length; i++) {
                    retArr[row][i] = Integer.parseInt(levels[i]);
                }
                row++;
    	   }
    	} catch(Exception e) {
	       System.out.println(e);
           return null;
	    }
    	return retArr;
   }
}
