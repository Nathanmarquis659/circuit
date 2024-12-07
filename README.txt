****************
* CircuitTracer
* CS221
* 12/6/2024
* Nathan Marquis
**************** 

OVERVIEW:

 CircuitTracer takes in command line arguments to initiate a brute-force search algorithm
 to get from a starting point to an end point on a board in as few steps as possible. All 
 solutions with the minimum number of steps are kept and output at program termination.


INCLUDED FILES:

 * CircuitBoard.java - source file creates CircuitBoards from an input file to be used with 
    CircuitTracer when seeking a solution from start point to finish point
 * CircuitTracer.java - source file seeks to find path from start to finish on a given CircuitBoard
 * Storage.java - source file creates either stack or queue to store TraceState objects in 
    CircuitTracer
 * TraceState.java - source file creates objects that show the path represented in points from 
    a starting point on the board to the end point. Keeps track of each instance of a solution 
    to be output in CircuitTracer
 * InvalidFileFormatException.java - file defines exception when an input file does not meet the
    defined template
 * OccupiedPositionException.java - file defines exception when a space has been occupied already 
    on the CircuitBoard grid
 * CircuitTracerTester.java - test file used to verify proper output and error handling of the 
    CircuitTracer class
 * README - this file ; explanation of program
 * ./boards - folder with examples of valid and invalid input files for the CircuitTracerTester class


COMPILING AND RUNNING:

 From the directory containing all source files, compile the
 driver class (and all dependencies) with the command:
 $ javac CircuitTracerTester.java

 Run the compiled class file with the command:
 $ java CircuitTracerTester

 Console output will give the results after the program finishes
 with the total number and proportion of tests passed.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 CircuitTracer takes in command line arguments to determine whether or not a stack or queue 
 will be used, as well as if the output should be in the form of console output or a graphical
 user interface, the final portion of the input is a filename. This file is used to create
 a CircuitBoard object when the proper formatting is used. The file should have the number
 of rows and columns in the header line, and a matrix after that which corresponds to the 
 claimed number of rows/columns. The matrix should contain one 1 and one 2, and the rest of the 
 positions should be filled up with 'X' and 'O' characters for closed and open slots. 
 
 After creating a CircuitBoard, it is used by CircuitTracer to determine every possible path
 from start (1) to end (2), and meanwhile compile an Arraylist of the paths with the fewest 
 steps. These paths are contained in the form of TraceState objects which track all of the 
 coordinates required to reach their outcome. A stack or queue as mentioned earlier will 
 use the Storage class to create the corresponding data structure formatted for TraceState 
 instances. The choice of stack or queue will reach the same result but will have different 
 benefits and drawbacks, such as increased memory usage for the queue. 

TESTING:

 Testing is accomplished using the CircuitTracerTester class which takes in valid and invalid
 input files with their corresponding output or error handling.


ANALYSIS:

 i.     The Storage choice will determine whether or not the paths will be executed in several 
        shooting motions (stack) vs a slow outgrowth from the origin (queue). Another way to
        put it is the difference between breadth and depth first strategies. The stack would 
        be a depth first, and the queue a breadth first. 
 ii.    The total number of search states will be the same. Both these Storage types will 
        ultimately search every possible route, but they will potentially accrue faster
        with stack over queue. Stack may actually reach a potential solution much faster,
        but the total number will be the same in the end. 
 iii.   Stack will have the ability to find solutions in fewer steps with luck/planning.
        Queue is very consistent in that it each path will only be at most 1 away from the rest,
        while the stack can go shooting off in different directions to try to find a solution. 
 iv.    Queue can guarantee that the first solution has the least number of steps because each 
        branch will all fall under the same number of steps within 1. So each possible answer of 
        4 steps will be reached first in a queue, where a stack might have by chance gotten a 
        solution of the LARGEST number of steps that would later be overturned. 
 v.     A queue will have a larger number of states at once because they will all be held as they
        slowly increment by one. Stack on the other hand will get through one search fully, then
        move on to the next eliminating them all one by one until there are none left. 
 vi.    I am not sure what n would be, after some research about the runtimes of breadth and depth
        first searches I found that it will be the sum of the vertices and edges, but I cannot 
        express this in terms of n.


DISCUSSION:
 
 Discuss the issues you encountered during programming (development)
 and testing. What problems did you have? What did you have to research
 and learn on your own? What kinds of errors did you get? How did you 
 fix them?
 
 What parts of the project did you find challenging? Is there anything
 that finally "clicked" for you in the process of working on this project?

 Much trouble was had in trying to figure out how to effectively deal with all possible errors involved
 when creating and using new CircuitBoards for me. The CircuitTracer was more straightforward because of 
 the provided pseudocode, but making the boards right took me more time. I also had some trouble 
 when initially trying to understand Storage and how we were meant to be using the TraceState objects, 
 but this eventually became evident while working with the data. 

 I have a branch on github devoted to the GUI, but I cannot make it pass the tests! It has the proper
 behaviour and outputs for console vs GUI mode, yet the test suite will say that I failed the tests 
 nonetheless! I was troubled by this and am still trying to make it work, unfortunately I do not want 
 to lose points in the testing to POTENTIALLY make it up with a partially working GUI. So I decided to 
 leave it out. 
 
 
EXTRA CREDIT:

 Attempted but could not make the test class work with it. 


----------------------------------------------------------------------------