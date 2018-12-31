package solver;
import java.util.SortedSet; //unused
import java.util.TreeSet;
import model.SCPModel;
import util.ElementSet;
import java.lang.Math;

/** This is the main method that all solvers inherit from.  It is important
 *  to note how this solver calls nextBestSet() polymorphically!  Subclasses
 *  should *not* override solver(), they need only override nextBestSet().
 * 
 *  We'll assume models are well-defined here and do not specify Exceptions
 *  to throw.
 * 
 * @author ssanner@mie.utoronto.ca, neevi.shah@mail.utoronto.ca
 *
 * Students: Note that I've asked Kai to remove the check for time-based winners in this year's autograde checks... 
 * now, the test cases focus on (1) correctness (matching the solution) and 
 * (2) overall efficiency (your solution should run within 2x of the solution time).
 * 
 */

//THERE IS NO CHECK FOR TIME-BASED CATEGORY WINNERS - ignore that when comparing to soln

public abstract class GreedySolver {
	
	protected String _name;			  // name of algorithm type
	protected double _alpha;          // minimum required coverage level in range [0,1]
	protected SCPModel _model;        // the SCP model we're currently operating on
	protected double _objFn;          // objective function value (*total cost sum* of all sets used)
	protected double _coverage;       // actual coverage fraction achieved
	protected long _compTime;         // computation time (ms)
	protected TreeSet<ElementSet> _solnSets; //used below in the print statements
	protected TreeSet<Integer> _uncElements; //uncovered elements
		
	// Basic setter (only one needed)
	public void setMinCoverage(double alpha) { _alpha = alpha; }
	public void setModel(SCPModel model) { _model = model; }
	
	// Basic getters
	public double getMinCoverage() { return _alpha; }
	public double getObjFn() { return _objFn; }
	public double getCoverage() { return _coverage; }
	public long getCompTime() { return _compTime; }
	public String getName() { return _name; }
			
	public void reset() { //clear so that solver can be used again
		_coverage = 0;
		_compTime = 0; //I am resetting comp time, but this will not be super important bc the time-based winner check has been removed
		_objFn = 0;
		_solnSets = new TreeSet<ElementSet>();
		_uncElements = new TreeSet<Integer>();
	}
	
	public void printSelectedSet (ElementSet selected) { 
		System.out.format("- Selected: Set ID:   %d   Cost:   %.2f   Element IDs: %s\n", selected.getSetID(), selected.getCost(), selected.getESet());
	} //format of the information of the selected element set is based on TestSCPSoln
	
	/** Run the simple greedy heuristic -- add the next best set until either
	 *  (1) The coverage level is reached, or 
	 *  (2) There is no set that can increase the coverage.
	 */
	
	public void solve() {
		
		// Reset the solver
		reset();
		
		// Preliminary initializations
		// NOTE: In order to match the solution, pay attention to the following
		//       calculations (where you have to replace ALL-CAPS parts)
		//
		int num_to_cover = (int)Math.ceil(_alpha * _model.getNumE()); //we have to cover alpha % of the total number of elements we have
		int num_can_leave_uncovered = _model.getNumE() - num_to_cover; //this is the amount of leeway we have (based on the value of alpha)
		boolean allPSetsSelected = false;	//all possible sets have not been selected
		_uncElements.addAll(_model.getAllE()); //in the beginning, amount of uncovered elements is ALL the elements within the model
				
		// Begin the greedy selection loop
		long start = System.currentTimeMillis();
		System.out.println("Running '" + getName() + "'...");

		// while (NUM_ELEMENTS_NOT_COVERED > num_can_leave_uncovered && ALL_POSSIBLE_SETS_HAVE_NOT_BEEN_SELECTED)
		//
		//      Call nextBestSet() to get the next best ElementSet to add (if there is one)
		// 		Update solution and local members
				
			while (_uncElements.size() > num_can_leave_uncovered && (allPSetsSelected==false)) { 
				//we have to cover a certain amount of elements and we keep going until all possible sets have been selected
				ElementSet bSet = nextBestSet();
								
				if (bSet==null) { //when there is no nextBestSet
					allPSetsSelected = true; //at this point, all possible sets have been selected and we can end
					break;
				}
				
				else {
					_uncElements.removeAll(bSet.getESet()); //this affects the condition of the while loop.
					//once an element has been covered in the nextBestSet, it is no longer uncovered so we remove it from uncovered elements
					//test: System.out.println("\n" + _uncElements + "\n");
					_solnSets.add(bSet); //add the nextBestSet to our set of solutions
					_objFn = _objFn + bSet.getCost(); // add current _objFn + cost of the nextBestSet
					
					if (_solnSets.size() == _model.getNumS()) { //if all sets of the model are solutions, we have covered all possible sets
						allPSetsSelected = true;
					}				
				}
			}
				
		
		// Record final set coverage, compTime and print warning if applicable
		_coverage = (double) ((_model.getNumE() - _uncElements.size()) / (double) _model.getNumE()); // coverage = sets covered/total sets. casted to double so the division gives a double output that matches the type of _coverage
		_compTime = System.currentTimeMillis() - start; //I am keeping it in case, but time-based category winners are no longer being checked for
		
		if (_coverage < _alpha) 
			System.out.format("\nWARNING: Impossible to reach %.2f%% coverage level.\n", 100*_alpha);
		System.out.println("Done.");
	}
	
	/** Returns the next best set to add to the solution according to the heuristic being used.
	 * 
	 *  NOTE 1: This is the **only** method to be implemented in child classes.
	 *  
	 *  NOTE 2: If no set can improve the solution, returns null to allow the greedy algorithm to terminate.
	 *  
	 *  NOTE 3: This references an ElementSet class which is a tuple of (Set ID, Cost, Integer elements to cover)
	 *          which you must define.
	 * 
	 */
	
	public abstract ElementSet nextBestSet(); // Abstract b/c it must be implemented by subclasses
	
	/** Print the solution
	 * 
	 */
	
	public void print() {
		System.out.println("\n'" + getName() + "' results:");
		System.out.format("'" + getName() + "'   Time to solve: %dms\n", _compTime);
		System.out.format("'" + getName() + "'   Objective function value: %.2f\n", _objFn);
		System.out.format("'" + getName() + "'   Coverage level: %.2f%% (%.2f%% minimum)\n", 100*_coverage, 100*_alpha);
		System.out.format("'" + getName() + "'   Number of sets selected: %d\n", _solnSets.size());
		System.out.format("'" + getName() + "'   Sets selected: ");
		for (ElementSet s : _solnSets) { 
			System.out.print(s.getSetID() + " "); //changed from getId that was given
		}
		System.out.println("\n");
	}
	
	/** Print the solution performance metrics as a row
	 * 
	 */
	
	public void printRowMetrics() {
		System.out.format("%-25s%12d%15.4f%17.2f\n", getName(), _compTime, _objFn, 100*_coverage);
	}	
}
