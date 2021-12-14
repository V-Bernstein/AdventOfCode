import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Part1Solution {
    private final static String FILE_NAME = "input.txt";
    private final static Integer FISH_TIMER = 7;
    private final static Integer NEW_FISH_TIMER = 9;
    private final static Integer SIM_DAYS = 80;

    public static void main(String[] args) {
        List<Integer> timers = parseInput(FILE_NAME);
        if (timers.isEmpty()) {
	        System.out.println("Error parsing file");
	        return;
	}

	int[] timeBuckets = new int[NEW_FISH_TIMER];

	for (Integer time : timers) {
		timeBuckets[time] += 1;
	}

	for (int time = 0; time < SIM_DAYS; time ++) {
		int numZeroFish = timeBuckets[0];
		for (int days = 1; days < NEW_FISH_TIMER; days++) {
			timeBuckets[days - 1] = timeBuckets[days]; 
		}
		timeBuckets[NEW_FISH_TIMER-1] = numZeroFish;
		timeBuckets[FISH_TIMER-1] += numZeroFish;
	}

	int totalFish = 0;
	for (int i = 0; i < NEW_FISH_TIMER; i++) {
		totalFish += timeBuckets[i];
	}

        System.out.println("Solution is: " + totalFish);
    }

   private static List<Integer> parseInput(String fileName) {
	List<Integer> retList = new ArrayList<>();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
	   while ((line = br.readLine()) != null) {
	   	String[] fish = line.split(",");
		for (String s : fish) {
			retList.add(Integer.parseInt(s));
		}
	   }
	} catch(Exception e) {
	       System.out.println(e);
	}
	return retList;
   }
}
