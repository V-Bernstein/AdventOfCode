import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.*;
import java.util.function.Function;

public class Part2Solution {
    public static void main(String[] args) {
        List<String> readings = new ArrayList<>();
	parseInputIntoList(readings, "input.txt");
        if (readings.isEmpty()) {
	        System.out.println("Error parsing file");
	        return;
	}

	String oxGenRatingBinary = getOxGenRating(readings);
	String carbonScrubRatingBinary = getCarbonScrubRating(readings);

	int oxGenRating = convertCharArrayToBinary(oxGenRatingBinary.toCharArray());
	int carbonScrubRating = convertCharArrayToBinary(carbonScrubRatingBinary.toCharArray());
	System.out.println("Solution is: " + (oxGenRating * carbonScrubRating));
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

   private static String getOxGenRating(List<String> readings) {
	   List<String> oxGenReadings = new ArrayList<>(readings);
	   oxGenReadings = getRatingHelper(oxGenReadings, (numBits) -> getMaxBit(numBits[0], numBits[1]));
	   return oxGenReadings.get(0);
   }

   private static String getCarbonScrubRating(List<String> readings) {
	List<String> carbonScrubReadings = new ArrayList<>(readings);
	carbonScrubReadings = getRatingHelper(carbonScrubReadings, (numBits) -> getMinBit(numBits[0], numBits[1]));
	return carbonScrubReadings.get(0);
   }

   private static List<String> getRatingHelper(List<String> readings, Function <Integer[], Character> detFunc) {
	int bitIndex = 0;
	while (readings.size() > 1) {
		Integer[] numBits = countBitsAtIndex(readings, bitIndex);
		char criteria = detFunc.apply(numBits);
		readings = applyBitCriteria(readings, criteria, bitIndex);
		bitIndex++;
	}
	return readings;
   }

   private static Integer[] countBitsAtIndex(List<String> readings, int index) {
	Integer[] numBits = new Integer[2];
	numBits[0] = 0;
	numBits[1] = 0;
	for (String reading : readings) {
      		if (reading.charAt(index) == '0') {
	            numBits[0]++;
		} else { // '1'
		    numBits[1]++;
	        }
	}
	return numBits;
   }

   private static List<String> applyBitCriteria(List<String> readings, char criteria, int index) {
	return readings.stream().filter(it -> it.charAt(index) == criteria).collect(Collectors.toList());
   }

  private static char getMaxBit(int zeroes, int ones) {
	if (zeroes > ones) {
		return '0';
	} else {
		return '1';
	}
  }		  

  private static char getMinBit(int zeroes, int ones) {
	if (zeroes <= ones) {
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
