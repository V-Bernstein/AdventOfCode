import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class Part1Solution {
    public static void main(String[] args) {
        List<String> readings = new ArrayList<>();
	parseInputIntoList(readings, "input.txt");
        if (readings.isEmpty()) {
	        System.out.println("Error parsing file");
	        return;
	}

	int numOfInputBits = readings.get(0).length();
	int[][] bitsCounter = new int[numOfInputBits][2]; // initialized with 0s as Java default
	for (int i = 0; i < readings.size(); i++) {
		for (int j = 0; j < numOfInputBits; j++) {
	              countBit(readings.get(i).charAt(j), bitsCounter[j]);
	        }
	}

	char[] mostCommonBits = new char[numOfInputBits];
	char[] leastCommonBits = new char[numOfInputBits];
	for (int bit = 0; bit < numOfInputBits; bit++) {
	      mostCommonBits[bit] = getMaxBit(bitsCounter[bit][0], bitsCounter[bit][1]);
	      leastCommonBits[bit] = getMinBit(bitsCounter[bit][0], bitsCounter[bit][1]);
	}
	int gammaRate = convertCharArrayToBinary(mostCommonBits);
        int epsilonRate = convertCharArrayToBinary(leastCommonBits);
        System.out.println("Solution is: " + (gammaRate * epsilonRate));
    }

   private static void parseInputIntoList(List<String> list, String fileName) {
       try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
           while ((line = br.readLine()) != null) {
                   list.add(line);
	    }
	} catch(Exception e) {
	       System.out.println(e);
	}
   }

  private static void countBit(char bit, int[] bitsCounter) {
      if (bit == '0') {
          bitsCounter[0] ++;
       } else { // '1'
	  bitsCounter[1] ++;
       }
  }

  private static char getMaxBit(int zeroes, int ones) {
	if (zeroes > ones) {
		return '0';
	} else {
		return '1';
	}
  }		  

  private static char getMinBit(int zeroes, int ones) {
	if (zeroes < ones) {
		return '0';
	} else {
		return '1';
	}
  }		  

  private static int convertCharArrayToBinary(char[] arr) {
     int retVal = 0;
     for (char c : arr) {
         retVal = (retVal << 1) + (c - '0');
     }
     return retVal;
  }
}
