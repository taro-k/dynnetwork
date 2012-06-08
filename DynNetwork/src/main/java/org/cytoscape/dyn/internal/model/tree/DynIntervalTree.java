package org.cytoscape.dyn.internal.model.tree;

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
     * Search overlapping intervals in the tree.
	 * @param interval
	 * @return list of overlapping intervals with the given interval.
	 */
	public List<DynInterval<T>> search(DynInterval<T> interval);

    /**
     * Clear interval tree.
	 */
	public void clear();
	
    /**
     * Print list of current nodes.
	 * @param root
	 */
	public void print(DynNode<T> node);

       
}

