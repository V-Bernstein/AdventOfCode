import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Map;
import java.util.stream.*;

class BingoBoard {
	private int[][] numbers;
	private boolean[][] marked;

	public BingoBoard(int boardSize) {
		this.numbers = new int[boardSize][boardSize]; // Default values are 0
		this.marked = new boolean[boardSize][boardSize]; // Default values are false
	}

	public void setRow(int[] row, int index) {
		this.numbers[index] = row;
	}

	public void printBoard() {
		System.out.println("[");
		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				if (marked[i][j]) {
					System.out.print("*");
				}
				System.out.print(numbers[i][j] + ", ");
			}
			System.out.println("");
		}
		System.out.println("]");
	}

	public int getUnmarkedNumsSum() {
		int sum = 0;
		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				if (!marked[i][j]) {
					sum += numbers[i][j];
				}
			}
		}
		return sum;
	}

	// Returns true if won; false if not
	public boolean mark(int number) {
		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				if (numbers[i][j] == number) {
					marked[i][j] = true;
					return didMoveWin(i, j);
				}
			}
		}
		return false;
	}

	private boolean didMoveWin(int rowIdx, int colIdx) {
		boolean didRowWin = true;
		for (int i = 0; i < numbers.length; i++) {
			if (marked[rowIdx][i] == false) {
				didRowWin = false;
			}
		}

		boolean didColWin = true;
		for (int i = 0; i < numbers.length; i++) {
			if (marked[i][colIdx] == false) {
				didColWin = false;
			}
		}
		return didRowWin || didColWin;
	}
}

public class Part1Solution {
    private final static int BOARD_SIZE = 5;
    private final static String FILE_NAME = "input.txt";
	
    public static void main(String[] args) {
        List<Integer> calledNums = parseCalledNumbersIntoList(FILE_NAME);
        if (calledNums.isEmpty()) {
	        System.out.println("Error parsing file");
	        return;
	}

	List<BingoBoard> boards = parseInputIntoBingoBoards(FILE_NAME);
	TreeMap<Integer, Integer> movesToWinPerBoard = new TreeMap<Integer, Integer>();
	for (Integer i = 0; i < boards.size(); i++) {
		Integer movesToWin = calcMovesToWin(boards.get(i), calledNums);
		movesToWinPerBoard.put(movesToWin, i);
	}
	Map.Entry<Integer, Integer> winningEntry = movesToWinPerBoard.firstEntry();
	BingoBoard winningBoard = boards.get(winningEntry.getValue());
	winningBoard.printBoard();
	System.out.println(winningEntry);
	System.out.println("Winning number: " + calledNums.get(winningEntry.getKey()));
	int score = calcScore(winningBoard, calledNums.get(winningEntry.getKey()));
        System.out.println("Solution is: " + score);
    }

   private static List<Integer> parseCalledNumbersIntoList(String fileName) {
	List<Integer> retList = new ArrayList<>();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
           String line = br.readLine();
	   if (line == null) {
		   return retList;
	   }
	   List<String> tempList = Arrays.asList(line.split(",")); // Just a wrapper on an array, so no fancy functionality
	   retList = tempList.stream().map(it -> Integer.parseInt(it)).collect(Collectors.toList());
	} catch(Exception e) {
	       System.out.println(e);
	}
	return retList;
   }

   private static List<BingoBoard> parseInputIntoBingoBoards(String fileName) {
	List<BingoBoard> retList = new ArrayList<>();
        try(FileReader fr = new FileReader(fileName)) {
           BufferedReader br = new BufferedReader(fr);
	   br.readLine(); // Skip first two lines
	   br.readLine();
           String line;
	   int respLineNum = 0;
	   BingoBoard board = new BingoBoard(BOARD_SIZE);
	   while((line = br.readLine()) != null) {
		int[] nums = new int[BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			nums[i] = Integer.parseInt(line.substring(i*3, (i*3) + 2).trim());
		}
		board.setRow(nums, respLineNum);
		respLineNum++;
		if (respLineNum == BOARD_SIZE) {
			respLineNum = 0;
			retList.add(board);
			board = new BingoBoard(BOARD_SIZE);
			line = br.readLine(); // Skip the empty next line
		}
	   }
	} catch(Exception e) {
	    System.out.println(e);
	}
	return retList;
   }

   private static Integer calcMovesToWin(BingoBoard board, List<Integer> calledNums) {
	 for (Integer i = 0; i < calledNums.size(); i++) {
	 	Integer num = calledNums.get(i);
		boolean won = board.mark(num);
		if (won) {
		  return i;
		}
	 }
	 System.out.println("Board doesn't win D:");
	return calledNums.size();
   }

   private static int calcScore(BingoBoard board, int lastCalledNum) {
	return lastCalledNum * board.getUnmarkedNumsSum();
   }
}
