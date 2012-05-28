package org.cytoscape.dyn.internal.model;

import java.util.List;


/**
 * <code> DynIntervalTree </code> is the interface for the red-black 
 * interval tree.
 *  
 * @author sabina
 *
 * @param <T>
 */
public interface DynIntervalTree<T>
{
    /**
     * Returns root of the tree.
     * @return root
     */
	public DynNode<T> getRoot();
	
    /**
     * Insert new interval to the tree.
	 * @param interval
	 */
	public void insert(DynInterval<T> interval);
   
    /**
     * Removes interval from the tree if exists.
	 * @param interval
	 */
	public void remove(DynInterval<T> interval);
   
    /**
     * Search intervals in the tree.
	 * @param interval
	 * @return list of intervals
	 */
	public List<DynInterval<T>> searchIntervals(DynInterval<T> interval);
	
    /**
     * Search intervals in the tree.
	 * @param interval
	 * @return list of nodes
	 */
	public List<DynNode<T>> searchNodes(DynInterval<T> interval);
	
	public void print(DynNode<T> node);

       
}

