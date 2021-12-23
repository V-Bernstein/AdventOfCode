import java.io.*;
import java.util.Arrays;

public class Part2Solution {
    private final static String FILE_NAME = "input.txt";

    public static void main(String[] args) {
        int[][] base = parseInput(FILE_NAME);
        if (base == null) {
	        System.out.println("Error parsing file");
	        return;
	    }

        int[][] inputs = multiplyArr(base);
        int[][] costs = new int[inputs.length][inputs[0].length];
        for (int i = 0; i < inputs.length; i++) {
            Arrays.fill(costs[i], Integer.MAX_VALUE);
        }
        costs[0][0] = 1;

        for (int row = 0; row < costs.length; row++) {
            for (int col = 0; col < costs[0].length; col++) {
                if (row > 0) {
                    costs[row][col] = inputs[row][col] + costs[row-1][col];
                } else if (col > 0) {
                    costs[row][col] = inputs[row][col] + costs[row][col-1];
                }
            }
        }
        setCosts(inputs, costs, 0, 0, 0 - inputs[0][0]);
        int solution = costs[costs.length-1][costs[0].length-1];

        System.out.println("Solution is: " + solution);
    }

    private static void setCosts(int[][] inputs, int[][] costs, int row, int col, int curCost) {
        if (row < 0 || row >= inputs.length || col < 0 || col >= inputs[0].length) {
            return;
        }
        
        if (curCost + inputs[row][col] >= costs[row][col]) { // No change; ignore, don't propagate
            return;
        }

        costs[row][col] = curCost + inputs[row][col];

        // Propagate
        setCosts(inputs, costs, row - 1, col, costs[row][col]); // up
        setCosts(inputs, costs, row + 1, col, costs[row][col]); // down
        setCosts(inputs, costs, row, col-1, costs[row][col]); // left
        setCosts(inputs, costs, row, col+1, costs[row][col]); // right
    }

    private static int[][] multiplyArr(int[][] arr) {
        int origHeight = arr.length;
        int origWidth = arr[0].length;
        int[][] retArr = new int[origHeight * 5][origWidth * 5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                copyArray(retArr, arr, i * origHeight, j * origWidth, i + j);
            }
        }
        return retArr;
    }

    private static void copyArray(int[][] target, int[][] orig, int startRow, int startCol, int additive) {
        for (int row = startRow; row < startRow + orig.length; row++) {
            for (int col = startCol; col < startCol + orig[0].length; col++) {
                target[row][col] = orig[row - startRow][col - startCol] + additive;
                if (target[row][col] > 9) {
                    target[row][col] -= 9;
                }
            }
        }
    }

   private static int[][] parseInput(String fileName) {
        int numRows = 0;
        int numCols = 0;
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           br.mark(20000);
           String line;
	       while ((line = br.readLine()) != null) {
                numCols = line.length();
                numRows++;
    	   }
           br.reset();
           
           int[][] retArr = new int[numRows][numCols];
           int row = 0;
	       while ((line = br.readLine()) != null) {
                String[] levels = line.split("");
                for (int i = 0; i < levels.length; i++) {
                    retArr[row][i] = Integer.parseInt(levels[i]);
                }
                row++;
    	   }
           return retArr;
    	} catch(Exception e) {
	       System.out.println(e);
           return null;
	    }
   }
}
