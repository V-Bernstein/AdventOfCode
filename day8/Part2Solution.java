import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;
import java.lang.StringBuilder;

class Entry {
    private List<String> sigPatterns;
    private List<String> outputs;

    public Entry() {
        this.sigPatterns = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public List<String> getSigPatterns() {
        return sigPatterns;
    }

    public List<String> getOutputs() {
        return outputs;
    }

    public void setSigPatterns(List<String> sigPatterns) {
        this.sigPatterns = sigPatterns;
    }

    public void setOutputs(List<String> outputs) {
        this.outputs = outputs;
    }

    public void print() {
        System.out.print("Patterns: ");
        for (int i = 0; i < sigPatterns.size(); i++) {
            System.out.print(sigPatterns.get(i) + ", ");
        }
        System.out.print("Outputs: ");
        for (int i = 0; i < outputs.size(); i++) {
            System.out.print(outputs.get(i) + ", ");
        }
        System.out.println("");
    }
}

public class Part2Solution {
    private final static String FILE_NAME = "input.txt";
	
    public static void main(String[] args) {
        List<Entry> outputs = parseInput(FILE_NAME);
        if (outputs.isEmpty()) {
	        System.out.println("Error parsing file");
	        return;
	    }
    
        Map<String, Integer> signalToDigitMap = generateSignalToDigitMap();
        long solution = 0;
        for (Entry entry : outputs) {
            char[] charMap = generateCharMapForSignals(entry.getSigPatterns());
            List<String> correctedOutputs = replaceAndSortOutputs(charMap, entry.getOutputs());
    
            int value = 0;
            for (String s : correctedOutputs) {
                Integer digit = signalToDigitMap.get(s);
                value *= 10;
                value += digit;
            }
            solution += value;
        }

        System.out.println("Solution is: " + solution);
    }

   private static List<Entry> parseInput(String fileName) {
    	List<Entry> retList = new ArrayList<>();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
    	   while ((line = br.readLine()) != null) {
            int indexOfLine = line.indexOf("|");
            String sigPatterns = line.substring(0, indexOfLine - 1);
            String outputs = line.substring(indexOfLine + 2);
    	   	String[] sigs = sigPatterns.split(" ");
    	   	String[] digits = outputs.split(" ");
            Entry lineEntry = new Entry();
            lineEntry.setSigPatterns(Arrays.asList(sigs));
            lineEntry.setOutputs(Arrays.asList(digits));
            retList.add(lineEntry);
	       }
    	} catch(Exception e) {
	       System.out.println(e);
    	}
	    return retList;
   }

    private static char[] generateCharMapForSignals(List<String> signals) {
        char[] retArr = new char[7];
        String one = getSignalsWithLength(signals, 2).get(0);
        String four = getSignalsWithLength(signals, 4).get(0);
        String seven = getSignalsWithLength(signals, 3).get(0);
        String eight = getSignalsWithLength(signals, 7).get(0);
        // "a" = missing digit between 7 and 1
        retArr[toIndex('a')] = subtractSignals(seven, one).charAt(0);
        String temp = subtractSignals(eight, seven);
        temp = subtractSignals(temp, four); // "e" and "g"
        List<String> lengthSix = getSignalsWithLength(signals, 6);

        // "g" = the length 6 string containing one of the digits left when removing 7 and 4 from 8
        // "e" = 8 - 7, 4, and 9
        for (String s : lengthSix) {
            boolean containsZero = s.indexOf(temp.charAt(0)) >= 0;
            boolean containsOne = s.indexOf(temp.charAt(1)) >= 0;
            if (!containsZero || !containsOne) {
                String nine = s;
                String eg = temp;
                temp = subtractSignals(temp, nine);
                retArr[toIndex('e')] = temp.charAt(0);
                retArr[toIndex('g')] = subtractSignals(eg, temp).charAt(0);
                break;
            }
        }
                
        // "f" = search all nums and find which one of the two digits from 1 are only absent once
        int oneA = 0;
        int oneB = 0;
        for (String s : signals) {
            if (s.indexOf(one.charAt(0)) < 0) {
                oneA++;
            }
            if (s.indexOf(one.charAt(1)) < 0) {
                oneB++;
            }
        }
        if (oneA == 1) {
            retArr[toIndex('f')] = one.charAt(0);
        } else if (oneB == 1) {
            retArr[toIndex('f')] = one.charAt(1);
        }
    
        // "c" = 1 - "f"
        temp = subtractSignals(one, "" + retArr[toIndex('f')]);
        retArr[toIndex('c')] = temp.charAt(0);

        // "b" = 0 - acefg (search length 6s which contain both 'c' and 'e')
        for (String s : lengthSix) {
            char c = retArr[toIndex('c')];
            char e = retArr[toIndex('e')];
            if (s.indexOf(c) >= 0 && s.indexOf(e) >= 0) {
                String zero = s;
                char a = retArr[toIndex('a')];
                char f = retArr[toIndex('f')];
                char g = retArr[toIndex('g')];
                StringBuilder sb = new StringBuilder();
                sb.append(a);
                sb.append(c);
                sb.append(e);
                sb.append(f);
                sb.append(g);
                temp = subtractSignals(zero, sb.toString());
                retArr[toIndex('b')] = temp.charAt(0);
                // "d" = 8 - 0
                temp = subtractSignals(eight, zero);
                retArr[toIndex('d')] = temp.charAt(0);
                break;
            }
        }
        return retArr;
    }

   private static int toIndex(char c) {
        return c - 'a';
    }

    private static List<String> replaceAndSortOutputs(char[] charMap, List<String> outputs) {
        List<String> retVal = new ArrayList<>();
        for (String s : outputs) {
            char[] newChars = new char[s.length()];
            char[] oldChars = s.toCharArray();
            for (int i = 0; i < oldChars.length; i++) {
                newChars[i] = (char)('a' + find(charMap, oldChars[i])); // Translate
            }
            Arrays.sort(newChars);
            retVal.add(new String(newChars));
        }
        return retVal;
    }

    private static int find(char[] arr, char target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    private static String subtractSignals(String signal1, String signal2) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < signal1.length(); i++) {
            char charToCheck = signal1.charAt(i);
            if (signal2.indexOf(charToCheck) < 0) { // Char is not in signal 2; keep it
                sb.append(charToCheck);
            }
        }
        return sb.toString();
    }

    private static List<String> getSignalsWithLength(List<String> signals, int tgtLength) {
        List<String> retList = new ArrayList<>();
        for (String s : signals) {
            if (s.length() == tgtLength) {
                retList.add(s);
            }
        }
        return retList;
    }

    private static Map<String, Integer> generateSignalToDigitMap() {
        Map<String, Integer> retMap = new HashMap<>();
        retMap.put("abcefg", 0);
        retMap.put("cf", 1);
        retMap.put("acdeg", 2);
        retMap.put("acdfg", 3);
        retMap.put("bcdf", 4);
        retMap.put("abdfg", 5);
        retMap.put("abdefg", 6);
        retMap.put("acf", 7);
        retMap.put("abcdefg", 8);
        retMap.put("abcdfg", 9);
        return retMap;
    }
}
