import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayDeque;

public class Part1Solution {
    private final static String FILE_NAME = "input.txt";

    public static void main(String[] args) {
        List<String> inputs = parseInput(FILE_NAME);
        if (inputs == null || inputs.isEmpty()) {
	        System.out.println("Error parsing file");
	        return;
	    }

        List<Character> openers = getOpenersList();
        List<Character> closers = getClosersList();
        Map<Character, Character> openCloseMap = getOpenCloseMap();
        Map<Character, Integer> pointsMap = getPointsMap();

        int solution = 0;
        for (String s : inputs) {
            ArrayDeque<Character> openerStack = new ArrayDeque<>();
            for (int i = 0; i < s.length(); i++) {
                Character curChar = s.charAt(i);
                if (openers.contains(curChar)) {
                    openerStack.push(curChar);
                }
                
                if (closers.contains(curChar)) {
                    Character matchingOpener = openerStack.pop();
                    if (openCloseMap.get(matchingOpener) != curChar) {
                       solution += pointsMap.get(curChar); 
                       break; 
                    }
                }
            }
        }


        System.out.println("Solution is: " + solution);
    }

   private static List<String> parseInput(String fileName) {
        List<String> retList = new ArrayList<>();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
	       while ((line = br.readLine()) != null) {
                retList.add(line);
    	   }
    	} catch(Exception e) {
	       System.out.println(e);
           return null;
	    }
    	return retList;
   }

    private static List<Character> getOpenersList() {
        List<Character> retList = new ArrayList<>();
        retList.add('(');
        retList.add('{');
        retList.add('[');
        retList.add('<');
        return retList;
    }
    
    private static List<Character> getClosersList() {
        List<Character> retList = new ArrayList<>();
        retList.add(')');
        retList.add('}');
        retList.add(']');
        retList.add('>');
        return retList;
    }

    private static Map<Character, Character> getOpenCloseMap() {
        Map<Character, Character> retMap = new HashMap<>();
        retMap.put('(', ')');
        retMap.put('{', '}');
        retMap.put('[', ']');
        retMap.put('<', '>');
        return retMap;
    }

    private static Map<Character, Integer> getPointsMap() {
        Map<Character, Integer> retMap = new HashMap<>();
        retMap.put(')', 3);
        retMap.put('}', 1197);
        retMap.put(']', 57);
        retMap.put('>', 25137);
        return retMap;
    }
}
