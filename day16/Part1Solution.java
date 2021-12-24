import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.lang.StringBuilder;

class Parser {
    private String packets;
    private int versionNumTotal;

    public Parser(String packets) {
        this.packets = packets;
        this.versionNumTotal = 0;
    }

    public String getPackets() {
        return this.packets;
    }

    public void setPackets(String packets) {
        this.packets = packets;
    }

    public int getTotal() {
        return versionNumTotal;
    }

    public void addVersionNum(int versionNum) {
        this.versionNumTotal += versionNum;
    }
}

public class Part1Solution {
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
        System.out.println("Solution is: " + parser.getTotal());
    }

    // All parse functions also consume the packet
    private static void parsePacket(Parser parser) {
        String packet = parser.getPackets();
        int version = Integer.parseInt(packet.substring(0, VERSION_LENGTH), 2);
        packet = packet.substring(VERSION_LENGTH);
        parser.addVersionNum(version);
        int packetType = Integer.parseInt(packet.substring(0, TYPE_ID_LENGTH), 2);
        packet = packet.substring(TYPE_ID_LENGTH);
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
        int totalBits = VERSION_LENGTH + TYPE_ID_LENGTH;
        String packet = parser.getPackets();
        int lengthType = Integer.parseInt(packet.substring(0, LENGTH_TYPE_LENGTH), 2);
        packet = packet.substring(LENGTH_TYPE_LENGTH);
        totalBits += LENGTH_TYPE_LENGTH;
        if (lengthType == 0) { // Next 15 bits are the total length in bits of sub-packets
            int subPacketsLength = Integer.parseInt(packet.substring(0, 15), 2);
            String subPackets = packet.substring(15, 15 + subPacketsLength);
            totalBits += 15 + subPacketsLength;
            
            parser.setPackets(subPackets);
            while (parser.getPackets().length() > 0) {
                parsePacket(parser);
            }
            packet = packet.substring(15 + subPacketsLength); 
            parser.setPackets(packet);
        } else { // Next 11 bits are the number of sub-packets
            int numSubPackets = Integer.parseInt(packet.substring(0, 11), 2);
            packet = packet.substring(11);
            parser.setPackets(packet);
            for (int i = 0; i < numSubPackets; i++) {
                parsePacket(parser);
            }
        }
    }

    private static void parseLiteralValuePacket(Parser parser) {
        int totalBits = VERSION_LENGTH + TYPE_ID_LENGTH;
        String packet = parser.getPackets();
        boolean lastGroup = false;
        while (!lastGroup) {
            String bitGroup = packet.substring(0, 5);
            lastGroup = bitGroup.charAt(0) == '0';
            totalBits += 5;
            packet = packet.substring(5);
        }
        
        parser.setPackets(packet);
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
