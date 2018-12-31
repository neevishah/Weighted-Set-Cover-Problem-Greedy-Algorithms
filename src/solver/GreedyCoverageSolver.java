/**
 * 
 */
package solver;
import util.ElementSet;
import model.SCPModel;

/**
 * @author neevi.shah@mail.utoronto.ca
 *
 * Implementation of the greedy coverage heuristic outlined above; this class should extendGreedySolver
 * and only define a constructor and override nextBestSet().
 * 
 * start with an empty solution(meaning you haven’t yet selected any sets
 * to be in the final solution), and then add the set that will cover 
 * the most uncovered elements.
 *
 */
public class GreedyCoverageSolver extends GreedySolver {
	
	public GreedyCoverageSolver() {
		_name = "Coverage"; //set name of solver (this will be printed when program is run)
	}

@Override

	public ElementSet nextBestSet() {
		
	ElementSet bset = null; //start off with null - will change when a best set is found
	int mostUncE = 0; //start off with the 'record' or maximum value set to 0 (this will update as we go thru sets)
	int uncE = 0; //uncovered elements of current set 
	
	for (ElementSet es : _model.getSCP()) {
		uncE = 0; //reset for each ElementSet
		
		for (Integer element : es.getESet()) {
			if (_uncElements.contains(element)) { //count the number of uncovered elements for each set
				uncE++;
			}
		}
		
		if (uncE > mostUncE) { //if the current set's number of uncovered elements is greater than the previous 'record' or value of most uncovered elements,
			//replace value with the current set's amount of uncovered elements
			
			mostUncE = uncE; //most uncovered elements (mostUncE) is equal to the max value of uncovered elements found in a set
			bset = es; //set the set with the most uncovered elements as the next best set
		}
	}
	
		printSelectedSet(bset); //print each selected set using the formatting provided in TestSCPSoln
		return bset; //return next best set 
	}
}
