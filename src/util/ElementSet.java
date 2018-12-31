/**
 * 
 */
package util;
import java.util.TreeSet;
import java.util.Collection;

/**
 * @author neevi.shah@mail.utoronto.ca
 *
 * Represents the tuple(Set ID, Cost, Integer elements to cover).  
 * Must inherit  from  interface  Comparable  and  
 * override public int compareTo(Object o) so  that  it  can  be used in a sorted set.  
 * (See notes and example Java code files from lecture.)
 *
 */

public class ElementSet implements Comparable { //Comparable interface orders objects (contains compareTo(Object)) Interfaces have to be implemented
	//whereas classes are extended

	private int _setID;
	private double _cost;
	private TreeSet <Integer> _elementsToCover; //TreeSet is a type of collection
	
	public ElementSet(int ID, double cost, Collection <Integer> elements) { //Collection is a super class for List and TreeSet, elements has to work for the List type for SCPModel.java addSetToCover

		_setID = ID;
		_cost = cost;
		_elementsToCover = new TreeSet<Integer> (elements);
		
	}
	
	@Override //override compareTo that is defined in Comparable
	public int compareTo (Object o) { //from Oracle docs: "Compares this object with the specified object for order. 
		//Returns a negative integer, zero, or a positive integer if this object is 
		//less than, equal to, or greater than the specified object."
		
		if (o instanceof ElementSet) {
			ElementSet es = (ElementSet)o; // cast (allowed bc o is of type ElementSet)
			
			if (this._setID < es._setID) { //if ID of instance is less than es' set ID, it would come before es
				return -1;
			}
			
			else if (this._setID == es._setID) { //if ID of instance is equal to es' set ID, they have the same location in the order
				return 0;
			}
			
			else if (this._setID > es._setID) { //if ID of instance is more than es' set ID, it would come after es
				return 1;
			}
		}
		
		 //else
		return -1;
	}
	
	//the following get methods are needed for toString in SCPModel
	public int getSetID() { //get set ID of element set
		return _setID; 
	}
	
	public double getCost() { //get cost of element set
		return _cost;
	}
	
	public TreeSet <Integer> getESet() { //get elements to cover of element set
		return _elementsToCover;
	}
}
