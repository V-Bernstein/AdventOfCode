import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.lang.StringBuilder;

class Parser {
    private String packets;
    private List<String> instructions;

    public Parser(String packets) {
        this.packets = packets;
        this.instructions = new ArrayList<>();
    }

    public String getPackets() {
        return this.packets;
    }

    public void setPackets(String packets) {
        this.packets = packets;
    }

    public List<String> getInstructions() {
        return this.instructions;
    }

    public void addInstruction(String instruction) {
        this.instructions.add(instruction);
    }
}

public class Part2Solution {
    private final static String FILE_NAME = "input.txt";
    private final static int VERSION_LENGTH = 3;
    private final static int TYPE_ID_LENGTH = 3;
    private final static int LENGTH_TYPE_LENGTH = 1;
    private final static int LIT_VAL_PACKET_TYPE = 4;

    public static void main(String[] args) {
        Map<Character, String> hexToBinMap = getHexToBinMap();
        String origInput = parseInput(FILE_NAME);
        if (origInput == null) {
	        System.out.println("Error parsing file");
	        return;
	    }

        String binInput = translateToBinary(origInput);
        Parser parser = new Parser(binInput);
        while (parser.getPackets().length() > 0) {
            parsePacket(parser);
        }
        System.out.println("Solution is: " + run(parser.getInstructions()));
    }

    private static long run(List<String> instructions) {
        List<Long> args = new ArrayList<>();
        String op = null;
        int idx = 0;
        while (idx < instructions.size()) {
            String instr = instructions.get(idx);
            if (isLiteral(instr)) {
                args.add(Long.parseLong(instr));
            } else if (instr.equals("(")) { // Keep going
            } else if (instr.equals(")")) {
                long result = calculate(op, args);
                return result;
            } else { // op
                if (op == null) {
                    op = instr;
                } else { // Sub exp
                    int instrEnd = findEnd(instructions.subList(idx, instructions.size()-1)) + idx;
                    long subResult = run(instructions.subList(idx, instrEnd+1));
                    args.add(subResult);
                    idx = instrEnd; // skip sub instr
                }
            }
            idx += 1;
        }
        System.out.println("No ) found in run");
        return -1;
    }

    private static long calculate(String op, List<Long> args) {
        switch (op) {
            case "+":
                return add(args);
            
            case "*":
                return multiply(args);
        
            case "min":
                return minimum(args);
            
            case "max":
                return maximum(args);

            case ">":
                return greaterThan(args);

            case "<":
                return lessThan(args);

            case "=":
                return equals(args);

            default:
                System.out.println("DEFAULTED");
                return -1;
        }
    }

    private static long add(List<Long> args) {
        long sum = 0;
        for (Long arg : args) {
            sum += arg;
        }
        return sum;
    }

    private static long multiply(List<Long> args) {
        long product = 1;
        for (Long arg : args) {
            product *= arg;
        }
        return product;
    }

    private static long minimum(List<Long> args) {
        long min = args.get(0);
        for (Long arg : args) {
            if (arg < min) {
                min = arg;
            }
        }
        return min;
    }

    private static long maximum(List<Long> args) {
        long max = args.get(0);
        for (Long arg : args) {
            if (arg > max) {
                max = arg;
            }
        }
        return max;
    }

    private static long greaterThan(List<Long> args) {
        return args.get(0) > args.get(1) ? 1 : 0;
    }

    private static long lessThan(List<Long> args) {
        return args.get(0) < args.get(1) ? 1 : 0;
    }

    private static long equals(List<Long> args) {
        return args.get(0).equals(args.get(1)) ? 1 : 0;
    }

    private static int findEnd(List<String> instructions) {
        int numOpens = 0;
        for (int i = 0; i < instructions.size(); i++) {
            if (instructions.get(i).equals("(")) {
                numOpens++;
            }
            if (instructions.get(i).equals(")")) {
                numOpens --;
                if (numOpens == 0) {
                    return i;
                }
            }
        }
        System.out.println("Couldn't find end!");
        return -1;
    }

    private static boolean isLiteral(String instr) {
        try {
            long num = Long.parseLong(instr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // All parse functions also consume the packet
    private static void parsePacket(Parser parser) {
        String packet = parser.getPackets();
        packet = packet.substring(VERSION_LENGTH);
        int packetType = Integer.parseInt(packet.substring(0, TYPE_ID_LENGTH), 2);
        packet = packet.substring(TYPE_ID_LENGTH);
        String op = getOp(packetType);
        if (!op.equals("lit")) {
            parser.addInstruction(op);
        }
        parser.setPackets(packet);
        if (packetType == LIT_VAL_PACKET_TYPE) {
            parseLiteralValuePacket(parser);
        } else {
            parseOperatorValuePacket(parser);
        }
        if (isAllZeros(parser.getPackets())) { // 0 padding, ignore
            parser.setPackets("");
        }
    }

    private static void parseOperatorValuePacket(Parser parser) {
        String packet = parser.getPackets();
        int lengthType = Integer.parseInt(packet.substring(0, LENGTH_TYPE_LENGTH), 2);
        packet = packet.substring(LENGTH_TYPE_LENGTH);
        parser.addInstruction("(");
        if (lengthType == 0) { // Next 15 bits are the total length in bits of sub-packets
            int subPacketsLength = Integer.parseInt(packet.substring(0, 15), 2);
            String subPackets = packet.substring(15, 15 + subPacketsLength);
            
            parser.setPackets(subPackets);
            while (parser.getPackets().length() > 0) {
                parsePacket(parser);
            }
            packet = packet.substring(15 + subPacketsLength);
            parser.setPackets(packet);
        } else { // Next 11 bits are the number of sub-packets
            long numSubPackets = Long.parseLong(packet.substring(0, 11), 2);
            packet = packet.substring(11);
            parser.setPackets(packet);
            for (int i = 0; i < numSubPackets; i++) {
                parsePacket(parser);
            }
        }
        parser.addInstruction(")"); 
    }

    private static void parseLiteralValuePacket(Parser parser) {
        String packet = parser.getPackets();
        StringBuilder sb = new StringBuilder();
        boolean lastGroup = false;
        while (!lastGroup) {
            String bitGroup = packet.substring(0, 5);
            lastGroup = bitGroup.charAt(0) == '0';
            sb.append(bitGroup.substring(1));
            packet = packet.substring(5);
        }
        
        parser.setPackets(packet);
        String literal = Long.toString(Long.parseLong(sb.toString(), 2));
        parser.addInstruction(literal);
    }

    private static String getOp(int opCode) {
        String[] ops = new String[]{"+", "*", "min", "max", "lit", ">", "<", "="};
        return ops[opCode];
    }

    private static boolean isAllZeros(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }

    private static String translateToBinary(String s) {
        Map<Character, String> hexToBinMap = getHexToBinMap();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            sb.append(hexToBinMap.get(s.charAt(i)));
        }
        return sb.toString();
    }

    private static Map<Character, String> getHexToBinMap() {
        Map<Character, String> retMap = new HashMap<>();
        retMap.put('0', "0000");
        retMap.put('1', "0001");
        retMap.put('2', "0010");
        retMap.put('3', "0011");
        retMap.put('4', "0100");
        retMap.put('5', "0101");
        retMap.put('6', "0110");
        retMap.put('7', "0111");
        retMap.put('8', "1000");
        retMap.put('9', "1001");
        retMap.put('A', "1010");
        retMap.put('B', "1011");
        retMap.put('C', "1100");
        retMap.put('D', "1101");
        retMap.put('E', "1110");
        retMap.put('F', "1111");
        return retMap;
    }

    private static String parseInput(String fileName) {
        try (FileReader fr = new FileReader(fileName)) {
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            return line;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
