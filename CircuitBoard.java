import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail and Nathan Marquis
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
		String headerString = fileScan.nextLine();
		int numRows = 0;
		int numCols = 0;
		int numStarts = 0;
		int numFinishes = 0;
		String headerFormatExceptionText = "On the first line should be 2 integers, # rows and # columns";
		String bodyFormatExceptionText = "The matrix should only contain [O,X] and one each of [1,2]";
		
		// throw FileNotFoundException if Scanner cannot read the file
		// throw InvalidFileFormatException if any issues are encountered while parsing the file
		// Header line check
		String[] headerInputs = headerString.split("\\s+"); // Split by whitespace
		if (headerInputs.length != 2) {
			throw new InvalidFileFormatException(headerFormatExceptionText);
		}

		try {
			numRows = Integer.parseInt(headerInputs[0]);
			numCols = Integer.parseInt(headerInputs[1]);

		} catch (NumberFormatException e) {
			throw new InvalidFileFormatException(headerFormatExceptionText);
		}
		
		// Check amount of rows
		int rowCount = 0;
		Scanner bodyScanner = new Scanner(new File(filename));
		bodyScanner.nextLine();
		while (bodyScanner.hasNextLine()) {
			// Make sure empty lines are not counted.
			if (!bodyScanner.nextLine().isEmpty()) {
				rowCount++;
			}
		}
		bodyScanner.close();
		
		if (rowCount != numRows) {
			throw new InvalidFileFormatException("Improper # of rows");
		}

		// Check amount of columns
		Scanner thirdScan = new Scanner(new File(filename));
		thirdScan.nextLine();
		while (thirdScan.hasNextLine()) {
			String nextLine = thirdScan.nextLine();
			String[] numChar = nextLine.split("\\s+");
			if (numChar.length != numCols) {
				thirdScan.close();;
				throw new InvalidFileFormatException("Improper # of columns");
			}
		}
		thirdScan.close();;

		ROWS = numRows;
		COLS = numCols;

		// Create new board of proper dimension (rows and columns)
		board = new char[numRows][numCols];

		try {
			for (int i = 0; i < numRows; i++) {
				for (int j = 0; j < numCols; j++) {
					char newPoint = fileScan.next().charAt(0);
					// Create start point, increment to see if there are too many
					if (newPoint == START) {
						startingPoint = new Point(i, j);
						numStarts++;
					// Create end point, increment to see if there are too many
					} else if (newPoint == END) {
						endingPoint = new Point(i, j);
						numFinishes++;
					// Ensures that the character is either O or X
					} else if (newPoint == OPEN || newPoint == CLOSED) {
					} else {
						throw new InvalidFileFormatException(bodyFormatExceptionText);
					}
					// Adds point to the board
					board[i][j] = newPoint;
				}
			}

			} catch (NoSuchElementException e) {
			throw new InvalidFileFormatException("File should contain no empty spaces in matrix");
		}
		fileScan.close();
	
		// Check for ONLY one start and finish
		if (numStarts != 1 || numFinishes != 1) {
			throw new InvalidFileFormatException(bodyFormatExceptionText);
		}



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
	
}