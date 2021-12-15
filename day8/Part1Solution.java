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
        List<String> outputs = parseInput(FILE_NAME);
        if (outputs.isEmpty()) {
	        System.out.println("Error parsing file");
	        return;
	    }
    
        int easyDigitAppearances = 0;
        List<Integer> easyDigits = generateEasyDigitsList(); 

        for (String s : outputs) {
            if (easyDigits.contains(s.length())) {
                easyDigitAppearances ++;
            }
        }

        System.out.println("Solution is: " + easyDigitAppearances);
    }

   private static List<String> parseInput(String fileName) {
    	List<String> retList = new ArrayList<>();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
    	   while ((line = br.readLine()) != null) {
            String output = line.substring(line.indexOf("|") + 2); // +2 due to extra space
    	   	String[] digits = output.split(" ");
            for (String digit : digits) {
                retList.add(digit);
            }
	       }
    	} catch(Exception e) {
	       System.out.println(e);
    	}
	    return retList;
   }

    private static List<Integer> generateEasyDigitsList() {
        Integer[] easyDigits = new Integer[]{2,4,3,7};
        return Arrays.asList(easyDigits);
    }
}
