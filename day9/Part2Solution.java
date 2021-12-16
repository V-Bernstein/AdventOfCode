import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeSet;

class LowPoint {
    private final int row;
    private final int col;
    
    public LowPoint(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }
    
    public void print() {
        System.out.println("LowPoint at: (" + row + ", " + col + ")");
    }
}

public class Part2Solution {
    private final static String FILE_NAME = "input.txt";
    private final static int INPUT_LENGTH = 100;
    private final static int INPUT_WIDTH = 100;

    public static void main(String[] args) {
        int[][] heightMap = parseInput(FILE_NAME);
        if (heightMap == null) {
	        System.out.println("Error parsing file");
	        return;
	    }

        List<LowPoint> lowPoints = getLowPoints(heightMap);
        TreeSet<Integer> basinSizes = new TreeSet<>();
        for (LowPoint lp : lowPoints) {
            basinSizes.add(getBasinSize(lp, heightMap));
        }
        int solution = basinSizes.pollLast();
        solution *= basinSizes.pollLast();
        solution *= basinSizes.pollLast();
        System.out.println("Solution is: " + solution);
    }
    
    private static int getBasinSize(LowPoint lp, int[][] heightMap) {
        boolean[][] isCounted = new boolean[heightMap.length][heightMap[0].length];
        return getBasinSizeHelper(lp.getRow(), lp.getCol(), heightMap, isCounted);
    }
    
    private static int getBasinSizeHelper(int row, int col, int[][] heightMap, boolean[][] isCounted) {
        if (row < 0 || row >= heightMap.length || col < 0 || col >= heightMap[0].length) {
            return 0;
        }
        if (heightMap[row][col] == 9 || isCounted[row][col]) {
            return 0;
        }
        isCounted[row][col] = true;
        return 1 
                + getBasinSizeHelper(row - 1, col, heightMap, isCounted)
                + getBasinSizeHelper(row, col + 1, heightMap, isCounted)
                + getBasinSizeHelper(row + 1, col, heightMap, isCounted)
                + getBasinSizeHelper(row, col - 1, heightMap, isCounted);
    }

   private static List<LowPoint> getLowPoints(int[][] heightMap) {
        List<LowPoint> lowPoints = new ArrayList<>();
        boolean[][] isLowest = new boolean[INPUT_LENGTH][INPUT_WIDTH];
        for (int i = 0; i < INPUT_LENGTH; i++) { // Set all fields to true instead of the default false
            Arrays.fill(isLowest[i], true);
        }
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
                        lowPoints.add(new LowPoint(row, col));
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
        return lowPoints;
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
