package simulation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import population.Cell;
import population.MultiAgent;
import population.Team;
import utilities.CSVEditor;
import utilities.Constants;
import utilities.MT19937;
import utilities.UniformDistribution;

public class MultiAgentSimulation {

	private static final int ROWS = 50;
	private static final int COLS = 50;

	private static final long TIME_SLEEP = 1000;
	private static final int ITERATIONS = 10000;
	private static final int SAMPLE = 100;
	private static final int EXPERIMENTS = 20;

	private static final double EMPTY_RATIO = 0.5;

	public static void main(String[] args) throws InterruptedException {
		
		MT19937 mt = new MT19937(0);
		UniformDistribution ud = new UniformDistribution(mt);

		List<Team> teams = newListTeams();
		Map<String, double[]> map = getProbabilityMap();

		Cell[][] space;
		MultiAgent ma;

		long init;
		long time = 0;

		CSVEditor editor = new CSVEditor("C:/Programes/Eclipse/files/01_MultiAgent.csv");
		editor.writeLine(new String[] { "Experiment", "Iteration", "Team 0", "Team 1" });
		String[] population;

		for (int j = 0; j < EXPERIMENTS; j++) {

			space = initializeSpace(ROWS, COLS, teams, ud);
			ma = new MultiAgent(space, ROWS, COLS, map, ud);

			for (int i = 0; i < ITERATIONS; i++) {
				if (i % SAMPLE == 0) {
					population = new String[] { String.valueOf(j), String.valueOf(i),
							String.valueOf(ma.getPopulationByTeam(0)), String.valueOf(ma.getPopulationByTeam(1)) };
					editor.writeLine(population);
				}
				init = System.nanoTime();
				ma.iterate();
				time += (System.nanoTime() - init);
				
				ma.printSpace();
				TimeUnit.MILLISECONDS.sleep(TIME_SLEEP);
			}

			population = new String[] { String.valueOf(j), String.valueOf(ITERATIONS),
					String.valueOf(ma.getPopulationByTeam(0)), String.valueOf(ma.getPopulationByTeam(1)) };
			editor.writeLine(population);

			ma.printSpace();

			computePerformance(ma, time, j);

		}

		editor.closeEditor();
		
		System.out.println("End of the simulation");

	}

	private static List<Team> newListTeams() {
		List<Team> teams = new ArrayList<Team>();
		teams.add(new Team(0, Color.RED, true));
		teams.add(new Team(0, Color.BLUE, false));
		return teams;
	}

	private static Cell[][] initializeSpace(int rows, int cols, List<Team> teams, UniformDistribution ud) {
		Cell[][] space = newRandomSpace(rows, cols, teams, ud);
		return space;
	}

	private static Cell[][] newRandomSpace(int rows, int cols, List<Team> teams, UniformDistribution ud) {
		Cell[][] space = new Cell[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (ud.nextDouble() < EMPTY_RATIO) {
					space[i][j] = new Cell(false);
				} else {
					space[i][j] = new Cell(true, teams.get(ud.nextInt(0, teams.size() - 1)));
				}
			}
		}
		return space;
	}

	private static Map<String, double[]> getProbabilityMap() {
		Map<String, double[]> map = new HashMap<String, double[]>();

		map.put(Constants.PRED_NEI_PRED, getProbabilityArray(0, 0.5, 0.4, 0.3, 0.2, 0.1, 0, 0, 0));
		map.put(Constants.PRED_NEI_NON_PRED, getProbabilityArray(0, 0.5, 0.6, 0.7, 0.8, 0.9, 1, 1, 1));

		map.put(Constants.NON_PRED_NEI_PRED, getProbabilityArray(0.8, 0.6, 0.4, 0.2, 0, 0, 0, 0, 0));
		map.put(Constants.NON_PRED_NEI_NON_PRED, getProbabilityArray(0.2, 0.4, 0.6, 0.8, 0.6, 0.4, 0.2, 0, 0));

		map.put(Constants.EMPTY_NEI_PRED, getProbabilityArray(0, 0.2, 0.4, 0.6, 0.4, 0.2, 0, 0, 0));
		map.put(Constants.EMPTY_NEI_NON_PRED, getProbabilityArray(0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.4, 0.3, 0.2));

		return map;
	}

	private static double[] getProbabilityArray(double n0, double n1, double n2, double n3, double n4, double n5,
			double n6, double n7, double n8) {
		double[] array = new double[9];
		array[0] = n0;
		array[1] = n1;
		array[2] = n2;
		array[3] = n3;
		array[4] = n4;
		array[5] = n5;
		array[6] = n6;
		array[7] = n7;
		array[8] = n8;
		return array;
	}

	private static void computePerformance(MultiAgent ma, long time, int exp) {
		System.out.println("Experiment " + exp);

		long cells = ROWS * COLS * ITERATIONS;
		System.out.println("Time iteration " + (time / ITERATIONS) + " ns");
		System.out.println("Iterations: " + (time / cells) + " ns");
		System.out.println("Neighbors: " + (cells) + " times, " + (ma.getTimeNeighbors() / cells) + " ns");

		if (ma.getItEmpty() > 0)
			System.out.println(
					"Empty cell: " + (ma.getItEmpty()) + " times, " + (ma.getTimeEmpty() / ma.getItEmpty()) + " ns");
		if (ma.getItPredator() > 0)
			System.out.println("Predator cell: " + (ma.getItPredator()) + " times, "
					+ (ma.getTimePredator() / ma.getItPredator()) + " ns");
		if (ma.getItNonPredator() > 0)
			System.out.println("Non-predator cell: " + (ma.getItNonPredator()) + " times, "
					+ (ma.getTimeNonPredator() / ma.getItNonPredator()) + " ns");
	}

}
