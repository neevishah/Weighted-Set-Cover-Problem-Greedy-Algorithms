/**
 * 
 */
package solver;
import util.ElementSet;
import model.SCPModel;

/**
 * @author neevi.shah@mail.utoronto.ca
 *
 * Implementation  of  Chvatal’s  algorithm  heuristic;  this  class should extendGreedySolver 
 * and only define a constructor and overridenextBestSet().
 *
 * Rather than add the set that covers the most elements, 
 * Chvatal’s algorithm adds the set that has the smallest cost per uncovered
 * element, called the cost-coverage ratio.  
 * 
 * Already included in GreedySolver: As with the other greedy algorithms, Chvatal’s algorithm starts 
 * with an empty solution and stops when alpha percentage of elements have 
 * been covered or further improvement is not possible.
 * 
 */

public class ChvatalSolver extends GreedySolver { //inherits from GreedySolver
	
	public ChvatalSolver() {
		_name = "Chvatal"; //name changes for each class, set name for the solver here (shown when program is run)
	}

	/**
	 * Override nextBestSet
	 */
	
	@Override
	public ElementSet nextBestSet() {
		
		ElementSet bset = null;
		double costCovRatio=0; //cost-coverage ratio of the current set is initialized to 0.
		double smallestRatio = Double.MAX_VALUE; //set to MAX value so that initially, ratio of a set is always < than it. 
		//if we pick a specific value, there is a risk that the ratio will be more than it and the if statement will not function 
		//as intended.
		
		int uncE=0; //counter
		
		for (ElementSet es : _model.getSCP()) { //for each element set in the model
			//costCovRatio = es.getCost()/(double)_uncElements.size();
			uncE=0; //reset counter for each element set
			
			for (Integer element : es.getESet()) {
				if (_uncElements.contains(element)) { //if the elements in the element set are also uncovered, increase the counter
					uncE++;
				}
			}
			
			costCovRatio = es.getCost()/(double)uncE++; //the cost-coverage ratio is calculated by 
			//dividing the cost of that element set by the uncovered element amount (given by the counter)
			//I chose to cast it to double because usually ratios involve decimal numbers or fractions rather than integers
			
			if (costCovRatio < smallestRatio) { //the solver looks for the smallest cost per uncovered element ratio
				smallestRatio = costCovRatio; //if costCovRatio for the current element set is smaller than our smallest ratio, 
				//we update our smallest ratio
				bset = es; //this set has the best ratio, so it is the nextBestSet
			}
		}
		
		printSelectedSet(bset); //print each selected set using the formatting provided in TestSCPSoln
		
		return bset; //return next best set
	}
}