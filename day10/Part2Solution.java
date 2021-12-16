import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayDeque;

public class Part2Solution {
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
        ArrayList<Long> sortedScores = new ArrayList<>();

        for (int idx = 0; idx < inputs.size(); idx++) {
            String s = inputs.get(idx);
            ArrayDeque<Character> openerStack = new ArrayDeque<>();
            ArrayDeque<Character> closerStack = new ArrayDeque<>();
            boolean isCorrupt = false;
            for (int i = 0; i < s.length(); i++) {
                Character curChar = s.charAt(i);
                if (openers.contains(curChar)) {
                    openerStack.push(curChar);
                }
                
                if (closers.contains(curChar)) {
                    Character matchingOpener = openerStack.pop();
                    if (openCloseMap.get(matchingOpener) != curChar) { // Corrupted line
                       isCorrupt = true;
                       break; 
                    }
                }
            }
            if (!isCorrupt) {
                Long lineScore = calcLineScore(openerStack);
                addToScores(sortedScores, lineScore);
            }
        }

        int middle = sortedScores.size() / 2;
        System.out.println("Solution is: " + sortedScores.get(middle));
    }

  private static Long calcLineScore(ArrayDeque<Character> stack) {
        Long retVal = 0L;
        Map<Character, Integer> pointsMap = getPointsMap();
        while(!stack.isEmpty()) {
            Character c = stack.pop();
            retVal *= 5L;
            retVal += pointsMap.get(c);
        }
        return retVal;
  }

   private static void addToScores(List<Long> scores, Long scoreToAdd) {
       for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i) > scoreToAdd) {
                scores.add(i, scoreToAdd);
                return;
            }
       }
       scores.add(scoreToAdd);
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
        retMap.put('(', 1);
        retMap.put('[', 2);
        retMap.put('{', 3);
        retMap.put('<', 4);
        return retMap;
    }
}
