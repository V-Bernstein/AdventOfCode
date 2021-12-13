import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

class Coord {
	private int xCoord;
	private int yCoord;

	public Coord(int x, int y) {
		this.xCoord = x;
		this.yCoord = y;
	}

	public int getX() {
		return this.xCoord;
	}

	public int getY() {
		return this.yCoord;
	}

	public void print() {
		System.out.print("(" + xCoord + ", " + yCoord + ")");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (this.getClass() != o.getClass()) return false;
		Coord c = (Coord) o;
		return this.xCoord == c.xCoord && this.yCoord == c.yCoord;
	}

	@Override
	public int hashCode() {
		return (xCoord * yCoord) + xCoord + yCoord;
	}
}

class Vent {
	private Coord start;
	private Coord end;

	public Vent(Coord start, Coord end) {
		this.start = start;
		this.end = end;
	}

	public void print() {
		start.print();
		System.out.print(" -> ");
		end.print();
		System.out.println("");
	}

	public List<Coord> getCloudCoverage() {
		List<Coord> retList = new ArrayList<>();
		if (start.getX() == end.getX()) {
			if (start.getY() > end.getY()) {
				for (int i = start.getY(); i >= end.getY(); i--) {
					retList.add(new Coord(start.getX(), i));
				}
			} else {
				for (int i = start.getY(); i <= end.getY(); i++) {
					retList.add(new Coord(start.getX(), i));
				}
			}
			return retList;
		}

		if (start.getY() == end.getY()) {
			if (start.getX() > end.getX()) {
				for (int i = start.getX(); i >= end.getX(); i--) {
					retList.add(new Coord(i, start.getY()));
				}
			} else {
				for (int i = start.getX(); i <= end.getX(); i++) {
					retList.add(new Coord(i, start.getY()));
				}
			}
			return retList;
		}
		return new ArrayList<>();
	}
}

public class Part1Solution {
    private final static String FILE_NAME = "input.txt";
	
    public static void main(String[] args) {
        List<Vent> vents = parseInput(FILE_NAME);
        if (vents == null || vents.isEmpty()) {
	        System.out.println("Error parsing file");
	        return;
	}

	Map<Coord, Integer> covCoords = new HashMap<>();
	int overlappingPoints = 0;
	for (Vent vent : vents) {
		for (Coord coord : vent.getCloudCoverage()) {
			if (covCoords.containsKey(coord)) {
				Integer overlapCount = covCoords.get(coord);
				Integer newOverlapCount = overlapCount + 1;
				covCoords.replace(coord, newOverlapCount);
				if (overlapCount == 1) { // Update our count only when this is a new overlap point
					overlappingPoints ++;
				}
			} else {
				covCoords.put(coord, 1);
			}
		}
	}

        System.out.println("Solution is: " + overlappingPoints);
    }

   private static List<Vent> parseInput(String fileName) {
	List<Vent> retList = new ArrayList<>();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
	   while ((line = br.readLine()) != null) {
	   	String[] coords = line.split(" -> ");
		String[] startCoords = coords[0].split(",");
		String[] endCoords = coords[1].split(",");
		Coord start = new Coord(Integer.parseInt(startCoords[0]), Integer.parseInt(startCoords[1]));
		Coord end = new Coord(Integer.parseInt(endCoords[0]), Integer.parseInt(endCoords[1]));

		if (start.getX() == end.getX() || start.getY() == end.getY()) { // Exclude diagonals for now
			Vent vent = new Vent(start, end);
			retList.add(vent);
		}
	   }
	} catch(Exception e) {
	       System.out.println(e);
	}
	return retList;
   }
}
