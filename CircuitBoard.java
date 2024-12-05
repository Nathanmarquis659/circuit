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
	private final String ALLOWED_CHARS = "OX12"; //useful for validating with indexOf

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
		int numRows = 0;
		int numCols = 0;
		int numStarts = 0;
		int numFinishes = 0;
		int rowCount = 0;
		String headerFormatExceptionText = "On the first line should be 2 integers, # rows and # columns";
		String bodyFormatExceptionText = "The matrix should only contain [O,X] and one each of [1,2]";
		
		try {
			// Header line check
			Scanner formatScanner = new Scanner(new File(filename));
			String headerString = formatScanner.nextLine();
			String[] headerInputs = headerString.split("\\s+");

			if (headerInputs.length != 2) {
				formatScanner.close();
				throw new InvalidFileFormatException(headerFormatExceptionText);
			}

			numRows = Integer.parseInt(headerInputs[0]);
			numCols = Integer.parseInt(headerInputs[1]);
			while (formatScanner.hasNextLine()) {
				String nextLine = formatScanner.nextLine();
				// Only filled lines
				if (!nextLine.isEmpty()) {
					String[] numChar = nextLine.split("\\s+");
					// Check amount of columns
					if (numChar.length != numCols) {
						formatScanner.close();;
						throw new InvalidFileFormatException("Improper # of columns");
					}
					rowCount++;
				}
			}
			formatScanner.close();
			
			// Check amount of rows
			if (rowCount != numRows) {
				throw new InvalidFileFormatException("Improper # of rows");
			}
			
			ROWS = numRows;
			COLS = numCols;
			
			// Create new board of proper dimension (rows and columns)
			board = new char[numRows][numCols];
	
			Scanner fileScanner = new Scanner(new File(filename));
			// Skips header line
			fileScanner.nextLine();
			for (int i = 0; i < numRows; i++) {
				for (int j = 0; j < numCols; j++) {
					char newPoint = fileScanner.next().charAt(0);
					// Check if character is allowed
					if (ALLOWED_CHARS.indexOf(newPoint) == -1) {
						fileScanner.close();
						throw new InvalidFileFormatException(bodyFormatExceptionText);
					// Create start point, increment to see if there are too many
					} else if (newPoint == START) {
						startingPoint = new Point(i, j);
						numStarts++;
					// Create end point, increment to see if there are too many
					} else if (newPoint == END) {
						endingPoint = new Point(i, j);
						numFinishes++;
					} 
					// Adds point to the board
					board[i][j] = newPoint;
				}
			}
			fileScanner.close();
			// Check for ONLY one start and finish
			if (numStarts != 1 || numFinishes != 1) {
				throw new InvalidFileFormatException(bodyFormatExceptionText);
			}
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Cannot find file");
		} catch (NumberFormatException e) {
			throw new InvalidFileFormatException(headerFormatExceptionText);
		} catch (NoSuchElementException e) {
			throw new InvalidFileFormatException("File should contain no empty spaces in matrix");
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