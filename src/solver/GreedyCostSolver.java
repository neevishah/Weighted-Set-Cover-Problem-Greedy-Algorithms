/**
 * 
 */
package solver;
import util.ElementSet;
import model.SCPModel;

/**
 * @author neevi.shah@mail.utoronto.ca
 *
 * Implementation  of  the  greedy  cost  heuristic  outlined  above;  this  class should extendGreedySolver 
 * and only define a constructor and override nextBestSet().
 * 
 * The next set selected is the one with the lowest cost that covers at least one 
 * uncovered element (i.e., we do not care how many uncovered elements it covers so long as it covers at least one).
 * 
 */

public class GreedyCostSolver extends GreedySolver {
	
	public GreedyCostSolver() {
		_name = "Cost"; //set name of solver (this will be printed when program is run)
	}

	/**
	 * Override NextBestSet
	 */
	
	@Override
	
	public ElementSet nextBestSet() {

		ElementSet bset = null;
		double smallestCost = Double.MAX_VALUE; //so that we do not run the risk of cost being smaller than smallestCost initially
		boolean covers = false; //true if set includes at least one uncovered element
		double cost=0;
		
		for (ElementSet es : _model.getSCP()) {
			for (Integer element : es.getESet()) {
				if(_uncElements.contains(element)) {
					covers = true;
					cost = es.getCost();
					break;
				}
			}
			
			if (cost < smallestCost && covers==true) {
				smallestCost = cost;
				bset = es;
			}
		}
		
		printSelectedSet(bset); //print each selected set using the formatting provided in TestSCPSoln
		return bset; //return next best set
	}
}
