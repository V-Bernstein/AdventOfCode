import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.lang.StringBuilder;

public class Part1Solution {
    private final static String FILE_NAME = "input.txt";
    private final static int NUM_STEPS = 10;

    public static void main(String[] args) {
        String template = getTemplate(FILE_NAME);
        Map<String, String> insertions = parseInput(FILE_NAME);
        if (insertions == null) {
	        System.out.println("Error parsing file");
	        return;
	    }

        for (int step = 0; step < NUM_STEPS; step++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < template.length()-1; i++) {
                if (i > 0) { // Remove duplicates if this isn't the first window
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append(insertions.get(template.substring(i, i+2)));
            }
            template = sb.toString();
        }
        Map<Character, Long> charCounts = getCharCounts(template);
        Long mostCommonCount = 0L;
        Long leastCommonCount = Long.valueOf(template.length());

        for (Map.Entry<Character, Long> entry : charCounts.entrySet()) {
            Long count = entry.getValue();
            if (count > mostCommonCount) {
                mostCommonCount = count;
            }
            if (count < leastCommonCount) {
                leastCommonCount = count;
            }
        }

        Long solution = mostCommonCount - leastCommonCount;

        System.out.println("Solution is: " + solution);
    }

    private static Map<Character, Long> getCharCounts(String s) {
        Map<Character, Long> retMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            Character curChar = s.charAt(i);
            if (retMap.containsKey(curChar)) {
                Long count = retMap.get(curChar) + 1L;
                retMap.put(curChar, count);
            } else {
                retMap.put(curChar, 1L);
            }
        }
        
        return retMap;
    }

    private static Map<String, String> parseInput(String fileName) {
        Map<String, String> retMap = new HashMap<>();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
	       while ((line = br.readLine()) != null) {
            if (line.indexOf("->") < 0) {
                continue;
            }
            String[] pairs = line.split(" -> ");
            StringBuilder replacement = new StringBuilder();
            replacement.append(pairs[0].charAt(0));
            replacement.append(pairs[1]);
            replacement.append(pairs[0].charAt(1));
            retMap.put(pairs[0], replacement.toString());
           }
    	} catch(Exception e) {
	       System.out.println(e);
           return null;
	    }
    	return retMap;
   }
    
    private static String getTemplate(String fileName) {
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line = br.readLine();
           return line;
    	} catch(Exception e) {
	       System.out.println(e);
	    }
        return null;
    }
}
