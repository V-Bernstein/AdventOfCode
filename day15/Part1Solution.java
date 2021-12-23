import java.io.*;
import java.util.Arrays;

public class Part1Solution {
    private final static String FILE_NAME = "input.txt";

    public static void main(String[] args) {
        int[][] inputs = parseInput(FILE_NAME);
        if (inputs == null) {
	        System.out.println("Error parsing file");
	        return;
	    }

        int[][] costs = new int[inputs.length][inputs[0].length];
        for (int i = 0; i < inputs.length; i++) {
            Arrays.fill(costs[i], Integer.MAX_VALUE);
        }
        costs[0][0] = 1; 

        // Pre-fill with basic costs to decrease recursive stack
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
