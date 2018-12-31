import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import model.SCPModel;
import solver.ChvatalSolver;
import solver.GreedyCostSolver;
import solver.GreedyCoverageSolver;
import solver.GreedySolver;

import java.io.*;

/** Example testing class, identical to TestSCPSoln except for classes used.
 * 
 * @author ssanner@mie.utoronto.ca
 *
 */
public class TestSCP {
	
	public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws IOException {
		
/*		SCPModel model = new SCPModel();
		
		// Create a weighted SCP with
		//   Set 1: weight 3.0, elements { 1, 8, 3, 5, 7, 9 }
		//   Set 2: weight 2.0, elements { 1, 5, 9 }
		//   Set 3: weight 2.0, elements { 5, 7, 9 }
		//   Set 4: weight 5.0, elements { 2, 4, 6, 8, 100 }
		//   Set 5: weight 2.0, elements { 2, 6, 100 }
		//   Set 6: weight 2.0, elements { 4, 8 }
		model.addSetToCover(6, 2.0, Arrays.asList(new Integer[] {4,8}));
		model.addSetToCover(5, 2.0, Arrays.asList(new Integer[] {2,6,100}));
		model.addSetToCover(4, 5.0, Arrays.asList(new Integer[] {2,4,6,8,100}));
		model.addSetToCover(3, 2.0, Arrays.asList(new Integer[] {5,7,9}));
		model.addSetToCover(2, 2.0, Arrays.asList(new Integer[] {1,5,9}));
		model.addSetToCover(1, 3.0, Arrays.asList(new Integer[] {1,3,5,7,9}));
		//model.addSetToCover(7, 3.0, Arrays.asList(new Integer[] {0}));
		
		GreedyCoverageSolver CoverageMethod = new GreedyCoverageSolver();
		GreedyCostSolver CostMethod = new GreedyCostSolver();
		ChvatalSolver ChvatalMethod = new ChvatalSolver();
		
		List<GreedySolver> solvers = Arrays.asList(new GreedySolver[] {CoverageMethod, CostMethod, ChvatalMethod});
		
		printComparison(solvers, model, 0.5);
		System.out.println("==========================================================================");
		printComparison(solvers, model, 0.3);
		System.out.println("==========================================================================");
		printComparison(solvers, model, 0.9);*/
		
		File file = new File ("files/SCP_S_10-10.txt");
		SCPModel model2 = ReadModel (file);
		GreedyCoverageSolver CoverageMethod = new GreedyCoverageSolver();
		GreedyCostSolver CostMethod = new GreedyCostSolver();
		ChvatalSolver ChvatalMethod = new ChvatalSolver();
		
		List<GreedySolver> solvers = Arrays.asList(new GreedySolver[] {CoverageMethod, CostMethod, ChvatalMethod});
		
		printComparison(solvers, model2, 0.5);
		System.out.println("==========================================================================");
	}
		
	public static SCPModel ReadModel (File file) throws IOException { //not perfect but shows that file can be read!
		
		BufferedReader fin = new BufferedReader (new FileReader(file));
		fin.readLine();
		
		int setID = 1;
		int setNum = Integer.parseInt(fin.readLine());
		SCPModel read_model = new SCPModel();
		
		while (setID < setNum) {
			double cost = Double.parseDouble(fin.readLine());
			TreeSet<Integer> elements = new TreeSet <Integer>();
			int check = Integer.MAX_VALUE;
			
			while (check != 0) {
				check = Integer.parseInt(fin.readLine());
				
				if (check!=0) {
					elements.add(check);
				}
			}
			
			read_model.addSetToCover(setID, cost, elements);
			setID++;
		}
		return read_model;
	}
	
	
	// set minimum coverage level for solution methods
	public static void printComparison(List<GreedySolver> solvers, SCPModel model, double alpha) {
			
		// Show the model
		System.out.println(model);
		
		// Run all solvers and record winners
		GreedySolver timeWinner = null;
		long minTime = Long.MAX_VALUE;
		
		GreedySolver objWinner = null;
		double minObj = Double.MAX_VALUE;
		
		GreedySolver covWinner = null;
		double maxCov = -Double.MAX_VALUE;
		
		for (GreedySolver s : solvers) {
			s.setMinCoverage(alpha);
			s.setModel(model);
			s.solve();
			s.print();
			s.printRowMetrics();
			
			if (minTime > s.getCompTime()) {
				minTime = s.getCompTime();
				timeWinner = s;
			}
			
			if (minObj > s.getObjFn()) {
				minObj = s.getObjFn();
				objWinner = s;
			}
			
			if (maxCov < s.getCoverage()) {
				maxCov = s.getCoverage();
				covWinner = s;
			}
		}

		System.out.format("\nAlpha: %.2f%%\n\n", 100*alpha);
		System.out.println("Algorithm                   Time (ms)     Obj Fn Val     Coverage (%)");
		System.out.println("---------------------------------------------------------------------");
		System.out.println("---------------------------------------------------------------------");
		for (GreedySolver s : solvers)
			s.printRowMetrics();
		System.out.format("%-25s%12s%15s%17s\n", "Category winner", timeWinner.getName(), objWinner.getName(), covWinner.getName());
		System.out.println("---------------------------------------------------------------------\n");
		
		String overall = "Unclear";
		if (timeWinner.getName().equals(objWinner.getName()) && 
			objWinner.getName().equals(covWinner.getName()))
			overall = timeWinner.getName();
		
		System.out.println("Overall winner: " + overall + "\n");
	}
	
}
