import java.io.*;

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

class Probe {
    private Coord curPos;
	private int xVelocity;
	private int yVelocity;

	public Probe(int xVelocity, int yVelocity) {
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
        this.curPos = new Coord(0, 0);
	}

	public Coord getCoords() {
		return this.curPos;
	}

    public Coord move() {
        int curX = this.curPos.getX();
        int curY = this.curPos.getY();
        int newX = curX + this.xVelocity;
        int newY = curY + this.yVelocity;
        this.curPos = new Coord(newX, newY);

        if (this.xVelocity > 0) {
            this.xVelocity--;
        } else if (this.xVelocity < 0) {
            this.xVelocity++;
        }

        this.yVelocity--;

        return this.curPos;
    }

	public void print() {
		System.out.print("xV: " + this.xVelocity + ", yV: " + this.yVelocity);
        this.curPos.print();
    }
}

class TargetArea {
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public TargetArea(int minX, int maxX, int minY, int maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMaxX() {
        return this.maxX;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public boolean isOnTarget(Coord point) {
        int x = point.getX();
        int y = point.getY();
        return (this.minX <= x && this.maxX >= x) && (this.minY <= y && this.maxY >= y);
    }

    /* Two cases:
        The first is where the probe goes past the max X bound of the area, in which case it can never move backward
        The second is where the probe falls past the lowest point (min Y) of the area, in which case it can never fly up
    */
    public boolean isPastTarget(Coord point) {
        int x = point.getX();
        int y = point.getY();
        return x > this.maxX || y < this.minY;
    }

    public void print() {
        System.out.println(this.minX + " <= x <= " + this.maxX + " : " + this.minY + " <= y <= " + this.maxY);
    }
}

public class Part2Solution {
    private final static String FILE_NAME = "input.txt";

    public static void main(String[] args) {
        TargetArea ta = parseInput(FILE_NAME);
        if (ta == null) {
	        System.out.println("Error parsing file");
	        return;
	    }
        ta.print();
    
        int xVelocity = findStartingXVelocity(ta.getMinX());
        int yVelocity = 1000; // Number carefully selected out of thin air
        int numPossibilities = 0;

        for (int x = xVelocity; x <= ta.getMaxX(); x++) { // If the velocity is > maxX it will overshoot on the first move
            for (int y = yVelocity; y >= ta.getMinY(); y--) {
                Probe probe = new Probe(x, y);
                Coord curPoint = probe.getCoords();
                while (!ta.isPastTarget(curPoint)) {
                    probe.move();
                    curPoint = probe.getCoords();
                    if (ta.isOnTarget(curPoint)) {
                        numPossibilities++;
                        break;
                    }
                }
            }
        }

        System.out.println("Solution is: " + numPossibilities);
    }

    // If the velocity is too low the probe will stop, due to drag, before getting to the minX of the target
    private static int findStartingXVelocity(int minX) {
        for (int i = 1; i < minX; i++) {
            if (minX / i < i) { // This is the smallest initial velocity to reach minX within # steps without reaching 0 velocity
                return i;
            }
        }
        return 1;
    }

    private static TargetArea parseInput(String fileName) {
        try (FileReader fr = new FileReader(fileName)) {
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            int xStartIdx = line.indexOf("x=") + 2;
            int xEndIdx = line.indexOf(",");
            String xValuesSubstring = line.substring(xStartIdx, xEndIdx);
            int[] xRange = parseSubstring(xValuesSubstring);

            int yStartIdx = line.indexOf("y=") + 2;
            String yValuesSubstring = line.substring(yStartIdx);
            int[] yRange = parseSubstring(yValuesSubstring);
            return new TargetArea(xRange[0], xRange[1], yRange[0], yRange[1]);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    // Takes string of format ##..## where ## can be any int
    // Returns a 2-item array of parsed ints
    private static int[] parseSubstring(String s) {
        int dotIdx = s.indexOf("..");
        int min = Integer.parseInt(s.substring(0, dotIdx));
        int max = Integer.parseInt(s.substring(dotIdx + 2));
        return new int[]{min, max};
    }
}
