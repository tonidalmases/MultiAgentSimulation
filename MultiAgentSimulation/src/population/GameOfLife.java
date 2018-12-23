package population;

import java.util.ArrayList;
import java.util.List;

import utilities.GameOfLifeGUI;

public class GameOfLife {

	private GameOfLifeGUI gui;
	private Cell[][] space;
	private int rows;
	private int cols;
	
	public GameOfLife(Cell[][] initialSpace, int rows, int cols) {
		gui = new GameOfLifeGUI(rows, cols);
		space = initialSpace;
		this.rows = rows;
		this.cols = cols;
	}
	
	public void iterate() {
		Cell[][] newSpace = newSpace();
		int aliveNei = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				aliveNei = aliveNeighbors(i, j);
				if (space[i][j].isPopulated()) {
					if (aliveNei <= 1)
						newSpace[i][j].setPopulated(false);
					else if (aliveNei >= 4)
						newSpace[i][j].setPopulated(false);
					else
						newSpace[i][j].setPopulated(true);
				} else {
					if (aliveNei == 3)
						newSpace[i][j].setPopulated(true);
				}
			}
		}
		space = newSpace;
	}
	
	public void printSpace() {
		gui.updateSpace(space);
	}
	
	private Cell[][] newSpace() {
		Cell[][] space = new Cell[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				space[i][j] = new Cell(false);
			}
		}
		return space;
	}

	private int aliveNeighbors(int i, int j) {
		int count = 0;
		List<Cell> listNeighbors = getNeighbors(i, j);
		for(Cell nei : listNeighbors) {
			if(nei.isPopulated()) count++;
		}
		return count;
	}

	private List<Cell> getNeighbors(int i, int j) {
		List<Cell> n = new ArrayList<Cell>();
		n.add(space[(i + (rows - 1)) % rows][(j + (cols - 1)) % cols]);
		n.add(space[(i + (rows - 1)) % rows][(j) % cols]);
		n.add(space[(i + (rows - 1)) % rows][(j + 1) % cols]);
		n.add(space[(i) % rows][(j + (cols - 1)) % cols]);
		n.add(space[(i) % rows][(j + 1) % cols]);
		n.add(space[(i + 1) % rows][(j + (cols - 1)) % cols]);
		n.add(space[(i + 1) % rows][(j) % cols]);
		n.add(space[(i + 1) % rows][(j + 1) % cols]);
		return n;
	}

}
