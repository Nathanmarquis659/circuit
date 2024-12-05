import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail and Nathan Marquis
 */
public class CircuitTracer {

	/** Launch the program. 
	 * 
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		new CircuitTracer(args); //create this with args
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private void printUsage() {
		System.out.println("Usage: $ java CircuitTracer <-s | -q> <-c | -g> filename");
		System.out.println("       where -s selects stack as the ADT");
		System.out.println("             -q selects queue as the ADT");
		System.out.println("             -c displays program output in console");
		System.out.println("             -g displays program output in GUI");
		System.out.println();
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {
		if (args.length != 3) {
			printUsage();
			return; //exit the constructor immediately
		}

		Storage<TraceState> stateStore = null;
		ArrayList<TraceState> bestPaths = new ArrayList<>();
		CircuitBoard currentBoard = null;

		// Check user console input
		switch(args[0]) { //checking first input
			case "-s":
				stateStore = Storage.getStackInstance();
				break;
			case "-q":
				stateStore = Storage.getQueueInstance();
				break;
			default:
				printUsage();
				return;
			}
		
		switch(args[1]) { //checking second input
			case "-c":
				break;
			case "-g":
				System.out.println("Sorry, GUI currently not available.");
				return;
			default:
				printUsage();
				return;
		}

		// Exception Handling for constructor
		try {
			currentBoard = new CircuitBoard(args[2]);
		} catch (InvalidFileFormatException e) {
			System.out.println(e.toString());
			return;
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
			return;
		}

		// Adding a new traceState object for each position adjacent to the initial position.

		if (currentBoard.isOpen(currentBoard.getStartingPoint().x + 1, currentBoard.getStartingPoint().y)) {
			stateStore.store(new TraceState(currentBoard, currentBoard.getStartingPoint().x + 1, currentBoard.getStartingPoint().y));
		}
		if (currentBoard.isOpen(currentBoard.getStartingPoint().x - 1, currentBoard.getStartingPoint().y)) {
			stateStore.store(new TraceState(currentBoard, currentBoard.getStartingPoint().x - 1, currentBoard.getStartingPoint().y));
		}
		if (currentBoard.isOpen(currentBoard.getStartingPoint().x, currentBoard.getStartingPoint().y + 1)) {
			stateStore.store(new TraceState(currentBoard, currentBoard.getStartingPoint().x, currentBoard.getStartingPoint().y + 1));
		}
		if (currentBoard.isOpen(currentBoard.getStartingPoint().x, currentBoard.getStartingPoint().y - 1)) {
			stateStore.store(new TraceState(currentBoard, currentBoard.getStartingPoint().x, currentBoard.getStartingPoint().y - 1));
		}
		
		TraceState initial;

		while (!stateStore.isEmpty()) {
			initial = stateStore.retrieve();
			if (initial.isSolution()) { // If it is a solution, check if its better or the same as any current solutions we may have.
				if (bestPaths.isEmpty() || initial.pathLength() == bestPaths.get(0).pathLength()) {
					bestPaths.add(initial);
				} else if (initial.pathLength() < bestPaths.get(0).pathLength()) {
					bestPaths = new ArrayList<>();
					bestPaths.add(initial);
				}
			} else { // If it is not a solution, continue to generate the next paths.
				if (initial.isOpen(initial.getRow() + 1, initial.getCol())) {
					stateStore.store(new TraceState(initial, initial.getRow() + 1, initial.getCol()));
				}
				if (initial.isOpen(initial.getRow() - 1, initial.getCol())) {
					stateStore.store(new TraceState(initial, initial.getRow() - 1, initial.getCol()));
				}
				if (initial.isOpen(initial.getRow(), initial.getCol() + 1)) {
					stateStore.store(new TraceState(initial, initial.getRow(), initial.getCol() + 1));
				}
				if (initial.isOpen(initial.getRow(), initial.getCol() - 1)) {
					stateStore.store(new TraceState(initial, initial.getRow(), initial.getCol() - 1));
				}
			}
		}
		
		for (TraceState path : bestPaths) {
			System.out.println(path.toString());
		}
	}
	
}