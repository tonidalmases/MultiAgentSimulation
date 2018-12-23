package utilities;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import population.Cell;

public class GameOfLifeGUI {

	private int rows;
	private int cols;
	
	private JPanel[][] panels;
	
	public GameOfLifeGUI(int rows, int cols) {
	    JFrame frame = new JFrame("Game of Life");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLayout(new GridLayout(rows, cols));

	    panels = new JPanel[rows][cols];
	    
	    for(int i=0;i<rows;i++) {
			for(int j=0;j<cols;j++) {
				JPanel p = new JPanel();
				p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				panels[i][j] = p;
				frame.add(p);
			}
		}
	    
	    frame.pack();
	    frame.setSize(1000, 1000);
	    frame.setVisible(true);
	    
	    this.rows = rows;
	    this.cols = cols;
	}
	
	public void updateSpace(Cell[][] space) {
		for(int i=0;i<rows;i++) {
			for(int j=0;j<cols;j++) {
				if(space[i][j].isPopulated()) 
					panels[i][j].setBackground(Color.GRAY);
				else
					panels[i][j].setBackground(Color.WHITE);
			}
		}
	}
	
}
