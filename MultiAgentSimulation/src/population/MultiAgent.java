package population;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.Constants;
import utilities.MultiAgentGUI;
import utilities.UniformDistribution;

public class MultiAgent {

	private MultiAgentGUI gui;
	private Cell[][] space;
	private int rows;
	private int cols;
	private UniformDistribution ud;
	private Map<String, double[]> probabilityMap;
	
	private long timeNeighbors, timePredator, timeNonPredator, timeEmpty;
	private int itPredator, itNonPredator, itEmpty;

	public MultiAgent(Cell[][] initialSpace, int rows, int cols, Map<String, double[]> probabilityMap,
			UniformDistribution ud) {
		gui = new MultiAgentGUI(rows, cols);
		space = initialSpace;
		this.rows = rows;
		this.cols = cols;
		this.probabilityMap = probabilityMap;
		this.ud = ud;
		
		this.timeNeighbors = 0;
		this.timePredator = 0;
		this.timeNonPredator = 0;
		this.timeEmpty = 0;
		
		this.itPredator = 0;
		this.itNonPredator = 0;
		this.itEmpty = 0;
	}

	public void iterate() {
		Cell[][] newSpace = newSpace();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				newSpace[i][j] = iterateCell(space[i][j], i, j);
			}
		}
		space = newSpace;
	}

	public void printSpace() {
		gui.updateSpace(space);
	}

	public int getPopulationByTeam(int code) {
		int count = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (space[i][j].isPopulated() && space[i][j].getTeam().getCode() == code)
					count++;
			}
		}
		return count;
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

	private Cell iterateCell(Cell cell, int i, int j) {
		Cell newCell;
		List<Cell> neighbors = getNeighbors(i, j);

		if (cell.isPopulated()) {
			if (cell.getTeam().isPredator()) {
				newCell = iteratePredatorCell(cell, neighbors);
			} else {
				newCell = iterateNonPredatorCell(cell, neighbors);
			}
		} else {
			newCell = iterateEmptyCell(cell, neighbors);
		}

		return newCell;
	}

	private Cell iterateEmptyCell(Cell cell, List<Cell> neighbors) {
		long init = System.nanoTime();
		
		Cell newCell;

		Team team = getMostFrequentTeam(neighbors);
		if (team != null) {
			int numNei = getTeamNeighbors(neighbors, team);
			double probNewCell;
			if (team.isPredator()) {
				probNewCell = probabilityMap.get(Constants.EMPTY_NEI_PRED)[numNei];
			} else {
				probNewCell = probabilityMap.get(Constants.EMPTY_NEI_NON_PRED)[numNei];
			}
			if (randomUnderThreshold(probNewCell)) {
				newCell = new Cell(true, team);
			} else {
				newCell = cell;
			}
		} else {
			newCell = cell;
		}
		
		timeEmpty += (System.nanoTime() - init);
		itEmpty++;

		return newCell;
	}

	private Cell iteratePredatorCell(Cell cell, List<Cell> neighbors) {
		long init = System.nanoTime();
		
		Cell newCell;

		int numPredators = 0;
		int numNonPredators = 0;

		for (Cell nei : neighbors) {
			if (nei.isPopulated() && !nei.getTeam().equals(cell.getTeam())) {
				if (nei.getTeam().isPredator()) {
					numPredators++;
				} else {
					numNonPredators++;
				}
			}
		}

		double survival = 0;
		survival += probabilityMap.get(Constants.PRED_NEI_PRED)[numPredators];
		survival += probabilityMap.get(Constants.PRED_NEI_NON_PRED)[numNonPredators];

		if (!randomUnderThreshold(survival / 2)) {
			newCell = new Cell(false);
		} else {
			newCell = cell;
		}

		timePredator += (System.nanoTime() - init);
		itPredator++;
		
		return newCell;
	}

	private Cell iterateNonPredatorCell(Cell cell, List<Cell> neighbors) {
		long init = System.nanoTime();
		
		Cell newCell;

		int numPredators = 0;
		int numNonPredators = 0;

		for (Cell nei : neighbors) {
			if (nei.isPopulated()) {
				if (nei.getTeam().isPredator()) {
					numPredators++;
				} else {
					numNonPredators++;
				}
			}
		}

		double survival = 0;
		survival += probabilityMap.get(Constants.NON_PRED_NEI_PRED)[numPredators];
		survival += probabilityMap.get(Constants.NON_PRED_NEI_NON_PRED)[numNonPredators];

		if (!randomUnderThreshold(survival / 2)) {
			newCell = new Cell(false);
		} else {
			newCell = cell;
		}

		timeNonPredator += (System.nanoTime() - init);
		itNonPredator++;
		
		return newCell;
	}

	private List<Cell> getNeighbors(int i, int j) {
		long init = System.nanoTime();
		
		List<Cell> n = new ArrayList<Cell>();
		n.add(space[(i + (rows - 1)) % rows][(j + (cols - 1)) % cols]);
		n.add(space[(i + (rows - 1)) % rows][(j) % cols]);
		n.add(space[(i + (rows - 1)) % rows][(j + 1) % cols]);
		n.add(space[(i) % rows][(j + (cols - 1)) % cols]);
		n.add(space[(i) % rows][(j + 1) % cols]);
		n.add(space[(i + 1) % rows][(j + (cols - 1)) % cols]);
		n.add(space[(i + 1) % rows][(j) % cols]);
		n.add(space[(i + 1) % rows][(j + 1) % cols]);
		
		timeNeighbors += (System.nanoTime() - init);
		
		return n;
	}

	private Team getMostFrequentTeam(List<Cell> neighbors) {
		Map<Team, Integer> counter = new HashMap<Team, Integer>();
		for (Cell nei : neighbors) {
			if (nei.isPopulated()) {
				if (!counter.containsKey(nei.getTeam()))
					counter.put(nei.getTeam(), 0);
				counter.replace(nei.getTeam(), counter.get(nei.getTeam()) + 1);
			}
		}
		if (!counter.isEmpty()) {
			List<Team> teams = new ArrayList<Team>();
			for (int i = 8; i >= 0; i--) {
				for (Map.Entry<Team, Integer> entry : counter.entrySet()) {
					if (entry.getValue() == i)
						teams.add(entry.getKey());
				}
				if (!teams.isEmpty())
					break;
			}
			return teams.get(ud.nextInt(0, teams.size() - 1));
		} else {
			return null;
		}
	}

	private int getTeamNeighbors(List<Cell> neighbors, Team team) {
		int count = 0;
		for (Cell nei : neighbors) {
			if (nei.isPopulated() && nei.getTeam().equals(team))
				count++;
		}
		return count;
	}

	private boolean randomUnderThreshold(double threshold) {
		return ud.nextDouble() < threshold;
	}

	public long getTimeNeighbors() {
		return timeNeighbors;
	}

	public long getTimePredator() {
		return timePredator;
	}

	public long getTimeNonPredator() {
		return timeNonPredator;
	}

	public long getTimeEmpty() {
		return timeEmpty;
	}

	public int getItPredator() {
		return itPredator;
	}

	public int getItNonPredator() {
		return itNonPredator;
	}

	public int getItEmpty() {
		return itEmpty;
	}

}
