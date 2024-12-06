import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Container;

public class BoardPanel extends JPanel implements Runnable{
    public static void main(String[] args) {
    }

    ArrayList<TraceState> paths = null;

    public BoardPanel(ArrayList<TraceState> bestPaths) {
        paths = bestPaths;
    }

    @Override
    public void run() {
        JFrame myFrame = new JFrame("Circuit Tracer");

        myFrame.setLocation(new Point(100, 100));
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container pane = myFrame.getContentPane();
        pane.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        JPanel titleBarPanel = new JPanel();
        titleBarPanel.setLayout(new FlowLayout());
        JButton fileButton = new JButton("File");

        titleBarPanel.add(fileButton);
        titleBarPanel.add(new JButton("Help"));
        pane.add(titleBarPanel, BorderLayout.NORTH);
        
        JPanel solutionsPanel = new JPanel();
        solutionsPanel.setLayout(new BoxLayout(solutionsPanel, BoxLayout.PAGE_AXIS));
        
        int solutionNum = 1;
        for (TraceState trace : paths) {
            JPanel gridPanel = new JPanel();
            CircuitBoard board = trace.getBoard();
            int rows = board.numRows();
            int cols = board.numCols();
            gridPanel.setLayout(new GridLayout(rows, cols));

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    String currentPiece = String.valueOf(board.charAt(row, col));
                    gridPanel.add(new JButton(currentPiece));
                }
            }
            
            solutionsPanel.add(new JButton("Solution " + solutionNum));
            solutionNum++;
            pane.add(gridPanel, BorderLayout.CENTER);
        }
        
        pane.add(solutionsPanel, BorderLayout.EAST);
        
        myFrame.pack();
        myFrame.setLocationByPlatform(true);
        myFrame.setVisible(true);
    }
}