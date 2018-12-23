package simulation;

import java.util.concurrent.TimeUnit;

import population.Cell;
import population.GameOfLife;

public class GameOfLifeSimulation {

	private static final int ROWS = 50;
	private static final int COLS = 50;

	private static final long TIME_SLEEP = 20;
	private static final int ITERATIONS = 100000;

	public static void main(String[] args) throws InterruptedException {
		Cell[][] space = initializeSpaceCells(ROWS, COLS);

		GameOfLife gol = new GameOfLife(space, ROWS, COLS);

		gol.printSpace();
		TimeUnit.MILLISECONDS.sleep(TIME_SLEEP);

		long init;
		long time = 0;

		for (int i = 0; i < ITERATIONS; i++) {
			init = System.nanoTime();
			gol.iterate();
			time += (System.nanoTime() - init);
			gol.printSpace();
			TimeUnit.MILLISECONDS.sleep(TIME_SLEEP);
		}

		System.out.println("CELLS - Time per iteration: " + (time / ITERATIONS) + " ns");
	}
	
	private static Cell[][] initializeSpaceCells(int rows, int cols) {
		Cell[][] space = newSpace(rows, cols);
		
		// Glider 1
		space[1][2].setPopulated(true);
		space[2][3].setPopulated(true);
		space[3][1].setPopulated(true);
		space[3][2].setPopulated(true);
		space[3][3].setPopulated(true);
		
		// Glider 2
		space[1][22].setPopulated(true);
		space[2][23].setPopulated(true);
		space[3][21].setPopulated(true);
		space[3][22].setPopulated(true);
		space[3][23].setPopulated(true);
		
		// Glider 2
		space[21][2].setPopulated(true);
		space[22][3].setPopulated(true);
		space[23][1].setPopulated(true);
		space[23][2].setPopulated(true);
		space[23][3].setPopulated(true);
		
		return space;
	}
	
	private static Cell[][] newSpace(int rows, int cols) {
		Cell[][] space = new Cell[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				space[i][j] = new Cell(false);
			}
		}
		return space;
	}
	
}
