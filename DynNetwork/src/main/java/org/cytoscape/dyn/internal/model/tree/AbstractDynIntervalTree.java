package org.cytoscape.dyn.internal.model.tree;

import java.util.ArrayList;
import java.util.List;



/**
 * <code> AbstractDynIntervalTree </code> abstract class for the implementation of a the interval tree.
 *  
 * @author sabina
 *
 * @param <T>
 */
public abstract class AbstractDynIntervalTree<T> implements DynIntervalTree<T>
{	
	protected final DynNode<T> root;
	protected final DynNode<T> nil;
	
	protected List<DynInterval<T>> currentIntervals = new ArrayList<DynInterval<T>>();
	
	public AbstractDynIntervalTree()
	{
		this.nil = new DynNode<T>();
		this.root = new DynNode<T>();
	}
	
	public AbstractDynIntervalTree(DynNode<T> root)
	{
		this();
		this.root.setLeft(root);
	}
	
	public AbstractDynIntervalTree(DynInterval<T> interval)
	{
		this(new DynNode<T>(interval, new DynNode<T>()));
	}

	@Override
	public DynNode<T> getRoot()
	{
		return root.getLeft();
	}
	
	@Override
	public void insert(DynInterval<T> interval)
	{	
		insert(new DynNode<T>(interval, nil), root.getLeft());
	}
	
	abstract protected void insert(DynNode<T> z, DynNode<T> root);
	
	@Override
	public void remove(DynInterval<T> interval)
	{
		for (DynNode<T> n : searchNodes(interval))
			if (n.getIntervalList().size()>1)
				n.getIntervalList().remove(interval);
			else
				remove(n);
	}
	
	abstract protected void remove(DynNode<T> z);
	
	@Override
	public List<DynInterval<T>> search(DynInterval<T> interval)
	{
		return searchIntervals(interval, new ArrayList<DynInterval<T>>());
	}
	
	protected List<DynInterval<T>> searchIntervals(DynInterval<T> interval, List<DynInterval<T>> intervalList)
    {
    	for (DynNode<T> node : searchNodes(interval))
    		for (DynInterval<T> i : node.getIntervalList())
    			intervalList.add(i);
    	return intervalList;
    }
	
	protected List<DynNode<T>> searchNodes(DynInterval<T> interval)
	{
		return root.getLeft().searchNodes(interval, new ArrayList<DynNode<T>>());
	}
	
	@Override
	public void clear()
	{
		this.nil.setParent(this.nil);
		this.nil.setLeft(this.nil);
		this.nil.setRight(this.nil);
		this.root.setLeft(this.nil);
	}
	
	@Override
	public void print(DynNode<T> node)
	{
//		System.out.print(root.getLeft().toString(""));
//		System.out.println("\nINTERVLAS #1");
//		for (DynInterval<T> interval : searchIntervals(new DynInterval<T>(4,7)))
//			System.out.println(interval.getStart() + " " + interval.getEnd());
//		
//		this.remove(new DynInterval<T>(4,7));
//		
//		System.out.println("\nREMOVE #1");
//		System.out.print(root.getLeft().toString(""));
	}

}
