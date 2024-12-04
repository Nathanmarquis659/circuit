import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail
 */
public class CircuitBoard {
	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;

	//constants you may find useful
	private final int ROWS; //initialized in constructor
	private final int COLS; //initialized in constructor
	private final char OPEN = 'O';	//capital 'o', an open position
	private final char CLOSED = 'X';//a blocked position
	private final char TRACE = 'T';	//part of the trace connecting 1 to 2
	private final char START = '1';	//the starting component
	private final char END = '2';	//the ending component
	private final String ALLOWED_CHARS = "OXT12"; //useful for validating with indexOf

	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot open or read the file
	 * @throws InvalidFileFormatException for any file formatting or content issue
	 */
	public CircuitBoard(String filename) throws FileNotFoundException {
		Scanner fileScan = new Scanner(new File(filename));
		String firstLine = fileScan.nextLine();
		int numRows = 0;
		int numCols = 0;
		int numOnes = 0;
		int numTwos = 0;
		
		// throw FileNotFoundException if Scanner cannot read the file
		// throw InvalidFileFormatException if any issues are encountered while parsing the file
		//TODO: parse the given file to populate the char[][]
		// CHECKING FOR VALID FIRST LINE INPUT //
		String[] parts = firstLine.split("\\s+"); // Split by whitespace
		if (parts.length != 2) {
			throw new InvalidFileFormatException("First line should contain two numeric values.");
		}

		try {
			numRows = Integer.parseInt(parts[0]);
			numCols = Integer.parseInt(parts[1]);

		} catch (NumberFormatException e) {
			throw new InvalidFileFormatException("First line should contain two numeric values.");
		}

		// SCAN IN BOARD VALUES INTO 2D ARRAY // 
		board = new char[numRows][numCols];

		try {
			for (int i = 0; i < numRows; i++) {
				for (int j = 0; j < numCols; j++) {
					
					board[i][j] = fileScan.next().charAt(0);

					if (board[i][j] != 'O' && board[i][j] != 'X' && board[i][j] != '1' && board[i][j] != '2') {
						throw new InvalidFileFormatException("This file contains invalid characters.");
					} 
					if (board[i][j] == '1') { // Set starting point
						startingPoint = new Point(i, j);
						numOnes++;
					}
					if (board[i][j] == '2') { // Set ending point
						endingPoint = new Point(i, j);
						numTwos++;
					}
					
				}
			}
			} catch (NoSuchElementException e) {
			throw new InvalidFileFormatException("This file has an empty space in comparison to the dimensions of the board.");
		}
	
		// CHECKING FOR # OF 1'S AND 2's //
		if (numOnes != 1 || numTwos != 1) {
			throw new InvalidFileFormatException("This file contains excess 1's and 2's, file should contain one 1 and one 2.");
		}

		// CHECKING FOR VALID # OF ROWS //
		int rowCount = 0;
		fileScan.close();
		Scanner secScanner = new Scanner(new File(filename));
		secScanner.nextLine();
		while (secScanner.hasNextLine()) {
            // Make sure empty lines are not counted.
                if (!secScanner.nextLine().isEmpty()) {
                    rowCount++;
                }
            }
        // If rows match make file valid.
        if (rowCount != numRows) {
            throw new InvalidFileFormatException("Rows do not match.");
        }

		// CHECKING FOR VALID # OF COLS //
		Scanner thirdScan = new Scanner(new File(filename));
		thirdScan.nextLine();
		while (thirdScan.hasNextLine()) {
			String nextLine = thirdScan.nextLine();
			String[] numChar = nextLine.split("\\s+"); // Split by whitespace
			if (numChar.length != numCols) {
				throw new InvalidFileFormatException("Column do not match.");
			}
		}
		thirdScan.close();;
		secScanner.close();

		ROWS = numRows;
		COLS = numCols;


	}
	
	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/** Utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}
	
	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}
	
	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}
	
	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}
	
	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}
	
	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}
	
	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
}// class CircuitBoard
