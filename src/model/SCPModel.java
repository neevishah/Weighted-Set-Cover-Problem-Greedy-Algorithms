/**
 * 
 */
package model;
import java.util.TreeSet;
import util.ElementSet;
import java.util.List;
import java.util.Collection;

/**
 * @author neevi.shah@mail.utoronto.ca
 *
 * Represents the weighted CSP problem; implemented as a sorted set of ElementSet.
 * 
 */

public class SCPModel {

	/**
	 * sorted set implies TreeSet would be a good choice.
	 * _model is the name that matches the name given in GreedySolver
	 * ElementSet has to be imported
	 */
	
	private TreeSet<ElementSet> _model;
	
	public SCPModel() {
		_model = new TreeSet<ElementSet>(); //constructor
	}
	
	public void addSetToCover (int ID, double cost, Collection<Integer> elements) { //method name from TestSCPSoln
		ElementSet es = new ElementSet (ID, cost, elements); //add set to model!
		_model.add(es);
	}
	
	public int getNumE () { //get number of elements in model --TestSCPSoln counts UNIQUE only
		TreeSet<Integer> numE = new TreeSet<Integer> (); //since elementsToCover (from ElementSet.java) is a treeset
			for (ElementSet es : _model) {
				numE.addAll(es.getESet()); //we merge all the elements into 1 big treeset (removes duplicates and orders the elements)
			}
		
			return numE.size();
	}
	
	public int getNumS () { //get number of sets in model
		
		return _model.size();
	}
	
	public TreeSet <Integer> getAllE () { //get all elements of model
		TreeSet <Integer > allE = new TreeSet<Integer> ();
		
		for (ElementSet es : _model) {
			allE.addAll(es.getESet());
		}
		
		return allE;
	}
	
	public TreeSet <ElementSet> getSCP() { //get model
		return _model;
	}
	
	public String toString () { //based on TestSCPSoln.java
	
	/*
	 * Weighted SCP model:
		---------------------
		Number of elements (n): 10
		Number of sets (m): 6
		
*/
		// a StringBuilder appends new characters to the end of the String
		StringBuilder sb = new StringBuilder();
				
		sb.append("\nWeighted  SCP model:\n---------------------\nNumber of elements (n): " + this.getNumE() + "\nNumber of sets (m): " + this.getNumS() + "\n"); //from TestSCPSoln.java
		sb.append("\nSet details:\n----------------------------------------------------------\n");
			
		for (ElementSet es : _model) { //for each elementset in the model
				sb.append("Set ID:   " + es.getSetID() + "   ");
				sb.append("Cost:   " + String.format("%.2f", es.getCost()) + "   "); //based on my testing, cost should only have 2 decimal places when printed regardless of the value
				sb.append("Element IDs: " + es.getESet() + "\n");
			}
						
		return sb.toString();
	}
}
