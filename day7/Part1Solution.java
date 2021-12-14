import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;

public class Part1Solution {
    private final static String FILE_NAME = "input.txt";
	
    public static void main(String[] args) {
        List<Integer> positions = parseInput(FILE_NAME);
        if (positions == null || positions.isEmpty()) {
	        System.out.println("Error parsing file");
	        return;
	    }
    
        int maxPos = -4012;
        int minPos = Integer.MAX_VALUE;
        for (Integer pos : positions) {
            if (pos > maxPos) {
                maxPos = pos;
            }
   
            if (pos < minPos) {
                minPos = pos;
            }
        }

        int minFuelCost = Integer.MAX_VALUE;
        // TODO: This is O(n^2), can we do better?
        for (int pos = minPos; pos <= maxPos; pos++) {
            int fuelCost = calcFuelCostForPos(positions, pos);
            if (fuelCost < minFuelCost) {
                minFuelCost = fuelCost;
            }
        }

        System.out.println("Solution is: " + minFuelCost);
    }

   private static List<Integer> parseInput(String fileName) {
	List<Integer> retList = new ArrayList<>();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
	   while ((line = br.readLine()) != null) {
	   	String[] positions = line.split(",");
		for (String s : positions) {
			retList.add(Integer.parseInt(s));
		}
	   }
	} catch(Exception e) {
	       System.out.println(e);
	}
	return retList;
   }

    private static int calcFuelCostForPos(List<Integer> positions, int target) {
        int fuelCost = 0;
        for (Integer pos : positions) {
            fuelCost += Math.abs(pos - target);
        }
        return fuelCost;
    }
}
