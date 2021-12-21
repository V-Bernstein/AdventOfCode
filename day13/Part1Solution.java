import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

class Coord {
	private int x;
	private int y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void print() {
		System.out.print("(" + x + ", " + y + ")");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (this.getClass() != o.getClass()) return false;
		Coord c = (Coord) o;
		return this.x == c.x && this.y == c.y;
	}

	@Override
	public int hashCode() {
		return (x * y) + x + y;
	}
}

class Fold {
	private char axis;
	private int unit;

	public Fold(char axis, int unit) {
		this.axis = axis;
		this.unit = unit;
	}

	public char getAxis() {
		return this.axis;
	}

	public int getUnit() {
		return this.unit;
	}

	public void print() {
		System.out.println(this.axis + "=" + this.unit);
	}
}

public class Part1Solution {
    private final static String FILE_NAME = "input.txt";

    public static void main(String[] args) {
        List<Coord> coords = getCoords(FILE_NAME);
        boolean[][] inputs = parseCoords(coords);

        List<Fold> folds = getFolds(FILE_NAME);
        inputs = fold(inputs, folds.get(0));

        int numDots = countDots(inputs);
        System.out.println("Solution is: " + numDots);
    }

    private static boolean[][] fold(boolean[][] points, Fold fold) {
        if (fold.getAxis() == 'x') {
            return foldLeft(points, fold.getUnit());
        } else {
            return foldUp(points, fold.getUnit());
        }
    }

    private static boolean[][] foldLeft(boolean[][] points, int indexToFold) {
        boolean[][] retArr = new boolean[points.length][indexToFold];
        
        for (int y = 0; y < points.length; y++) {
            for (int x = 0; x < indexToFold; x++ ) { // Set points before the fold
                retArr[y][x] = points[y][x];
            }
        }

        for (int y = 0; y < points.length; y++) {
            for (int x = indexToFold + 1; x < points[0].length; x++) {
                int newXIndex = points[0].length - 1 - x;
                retArr[y][newXIndex] = points[y][x] || points[y][newXIndex];
            }
        }

        return retArr;
    }

    private static boolean[][] foldUp(boolean[][] points, int indexToFold) {
        boolean[][] retArr = new boolean[indexToFold][points[0].length];
        
        for (int y = 0; y < indexToFold; y++ ) { // Set points before the fold
            retArr[y] = points[y];
        }

        for (int y = indexToFold + 1; y < points.length; y++) {
            for (int x = 0; x < points[0].length; x++) {
                int newYIndex = points.length - 1 - y;
                retArr[newYIndex][x] = points[y][x] || points[newYIndex][x];
            }
        }

        return retArr;
    }

    private static int countDots(boolean[][] points) {
        int count = 0;
        for (int y = 0; y < points.length; y++) {
            for (int x = 0; x < points[0].length; x++) {
                if (points[y][x]) {
                    count++;
                }
            }
        }
        return count;
    }

    private static List<Coord> getCoords(String fileName) {
        List<Coord> retList = new ArrayList<>();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
	       while ((line = br.readLine()) != null) {
                if (line.indexOf(",") < 0) {
                    break;
                }
                String[] coords = line.split(",");
                Coord coord = new Coord(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                retList.add(coord);
    	   }
    	} catch(Exception e) {
	       System.out.println(e);
           return null;
	    }
    	return retList;
    }

    private static List<Fold> getFolds(String fileName) {
        List<Fold> retList = new ArrayList<>();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line;
	       while ((line = br.readLine()) != null) {
                if (line.indexOf(",") > -1 || line.length() == 0) {
                    continue;
                }
                int idxOfEqual = line.indexOf("=");
                char axis = line.charAt(idxOfEqual - 1);
                int unit = Integer.parseInt(line.substring(idxOfEqual+1));
                Fold fold = new Fold(axis, unit);
                retList.add(fold);
    	   }
    	} catch(Exception e) {
	       System.out.println(e);
           return null;
	    }
    	return retList;
    }
   
    private static boolean[][] parseCoords(List<Coord> coords) {
        int maxRow = 0;
        int maxCol = 0;
        for (Coord c : coords) {
            if (c.getY() > maxRow) {
                maxRow = c.getY();
            }
            if (c.getX() > maxCol) {
                maxCol = c.getX();
            }
        }
        
        boolean[][] retArr = new boolean[maxRow+1][maxCol+1];
        
        for (Coord c : coords) {
            retArr[c.getY()][c.getX()] = true;
        }
        return retArr;
    }
}
