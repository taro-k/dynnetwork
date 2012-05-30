package org.cytoscape.dyn.internal.model;

import java.util.ArrayList;
import java.util.List;

/**
 * <code> DynIntervalTreeImpl </code> implements the interval tree for fast searching of intervals 
 * in a given time range. An interval tree is a red-black tree that maintains a dynamic set of elements,
 * with each node <code> DynNode </code> containing an interval <code> DynInterval </code>. 
 * The leaf nodes do not contain data, and reference to a dummy sentinel node (nil). The root is also a nil
 * sentinel node, whose left reference points to the network root.
 *  
 * @author sabina
 *
 * @param <T>
 */
public final class DynIntervalTreeImpl<T> implements DynIntervalTree<T>
{
	private final DynNode<T> root;
	private final DynNode<T> nil;
	
	public DynIntervalTreeImpl()
	{
		this.nil = new DynNode<T>();
		this.root = new DynNode<T>();
	}
	
	public DynIntervalTreeImpl(DynNode<T> root)
	{
		this();
		this.root.setLeft(root);
	}
	
	public DynIntervalTreeImpl<T> deepCopy(DynNode<T> root)
	{
		return new DynIntervalTreeImpl<T>(root.deepCopy());
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
	
	@Override
	public void remove(DynInterval<T> interval)
	{
		for (DynNode<T> n : searchNodes(interval))
			remove(n);
	}
	
	@Override
	public List<DynInterval<T>> searchIntervals(DynInterval<T> interval)
	{
		return searchIntervals(interval, new ArrayList<DynInterval<T>>());
	}
	
	@Override
	public List<DynNode<T>> searchNodes(DynInterval<T> interval)
	{
		return root.getLeft().searchNodes(interval, new ArrayList<DynNode<T>>());
	}
	
	@Override
	public void print(DynNode<T> node)
	{
		System.out.print(node.toString(""));
	}

	private void insert(DynNode<T> z, DynNode<T> root)
	{
		int dir = 0;
		while (!root.isLeaf())
		{
			dir = (z.getInterval().getStart()<root.getInterval().getStart() || 
					(z.getInterval().getStart()==root.getInterval().getStart() &&
					z.getInterval().getEnd()<root.getInterval().getEnd()))?0:1;
			
			root.getChildren(dir).setParent(root);
			root.setMax(max(z,root));
			root = root.getChildren(dir);
		}
		root.getParent().setChildren(dir, z);
		
		z.isBlack(false);
		while (!z.getParent().isBlack())
		{
			if (!z.getParent().getParent().getChildren(1-dir).isBlack())
			{
				z.getParent().getParent().isBlack(false);
				z.getParent().getParent().getLeft().isBlack(true);
				z.getParent().getParent().getRight().isBlack(true);
				z = z.getParent().getParent();
			}
			else
			{
				if (z == z.getParent().getChildren(1-dir))
				{	
					z = z.getParent();
					singleRotation(z,dir);
				}
				singleRotation(z.getParent().getParent(), 1-dir);
			}
		}
		
		this.root.getLeft().isBlack(true);
	}
	
	private void remove(DynNode<T> z)
	{
	
	}
	
//	private void remove(DynNode<T> z)
//	{
//		DynNode<T> y;
//		DynNode<T> x;
//		
//		if (z.getLeft().isLeaf() || z.getRight().isLeaf())
//			y = z;
//		else
//			y = this.successor(z);
//		if (y.getLeft().isLeaf())
//			x = y.getLeft();
//		else
//			x = y.getRight();
//		x.setParent(y.getParent());
//		if (root == x.getParent())
//			root.setLeft(x);
//		else if (y == y.getParent().getLeft())
//			y.getParent().setLeft(x);
//		else
//			y.getParent().setRight(x);
//		if (y!=z)
//		{
//			if (y.isBlack())
//				removeFixUp(x);
//			y.setLeft(z.getLeft());
//			y.setRight(z.getRight());
//			y.setParent(z.getParent());
//			y.isBlack(z.isBlack());
//			if (z==z.getParent().getLeft())
//				z.getParent().setLeft(y);
//			else
//				z.getParent().setRight(y);
//		}
//		else if (y.isBlack())
//			removeFixUp(x);
//	}
//	
//	private void removeFixUp(DynNode<T> x)
//	{
//		while (x!=root && x.isBlack())
//			if (x==x.getParent().getLeft())
//			{
//				DynNode<T> w = x.getParent().getRight();
//				if (!w.isBlack())
//				{
//					w.isBlack(true);
//					x.getParent().isBlack(false);
//					leftRotate(x.getParent());
//					w = x.getParent().getRight();
//				}
//				if (w.getLeft().isBlack() && w.getRight().isBlack()) {
//	                w.isBlack(false);
//	                x = x.getParent();
//	            } 
//	            else {
//	                if (w.getRight().isBlack()) {
//	                	w.getLeft().isBlack(true);
//	                	w.isBlack(false);
//	                    rightRotate(w);
//	                    w = x.getParent().getRight();
//	                }
//	                w.isBlack(x.getParent().isBlack());
//	                x.getParent().isBlack(true);
//	                w.getRight().isBlack(true);
//	                leftRotate(x.getParent());
//	                x = root;
//	            }
//			}
//			else
//			{
//				DynNode<T> w = x.getParent().getLeft();
//				if (!w.isBlack())
//				{
//					w.isBlack(true);
//					x.getParent().isBlack(false);
//					rightRotate(x.getParent());
//					w = x.getParent().getLeft();
//				}
//				if (w.getRight().isBlack() && w.getLeft().isBlack()) {
//	                w.isBlack(false);
//	                x = x.getParent();
//	            } 
//	            else {
//	                if (w.getLeft().isBlack()) {
//	                	w.getRight().isBlack(true);
//	                	w.isBlack(false);
//	                    leftRotate(w);
//	                    w = x.getParent().getLeft();
//	                }
//	                w.isBlack(x.getParent().isBlack());
//	                x.getParent().isBlack(true);
//	                w.getLeft().isBlack(true);
//	                rightRotate(x.getParent());
//	                x = root;
//	            }
//			}
//		x.isBlack(true);
//	}      
//    private DynNode<T> successor(DynNode<T> root)
//    {
//    	DynNode<T> right = root.getRight();
//    	if (!right.isLeaf())
//    		return this.getTreeMinimum(right);
//    	else
//    		return this.getUp(right);
//    }
//    
//    private DynNode<T> getUp(DynNode<T> root)
//    {
//    	DynNode<T> up = root.getParent();
//    	while (!up.isLeaf() && root == up.getRight())
//    	{
//    		root = up;
//    		up = up.getParent();
//    	}
//    	return up;
//    }
//
//    private DynNode<T> getTreeMinimum(DynNode<T> root)
//    {
//    	DynNode<T> left = root;
//    	while (!root.getLeft().isLeaf())
//    		left = left.getLeft();
//    	return left;
//    }
//
//    private DynNode<T> getTreeMaximum(DynNode<T> root)
//    {
//    	DynNode<T> right = root;
//    	while (!root.getRight().isLeaf())
//    		right = right.getLeft();
//    	return right;
//    }
	
	private DynNode<T> singleRotation(DynNode<T> root, int dir)
	{
    	DynNode<T> pivot = root.getChildren(1-dir);
    	root.setChildren(1-dir,pivot.getChildren(dir));
    	if (root == root.getParent().getChildren(dir))
    		root.getParent().setChildren(dir,pivot);
    	else
    		root.getParent().setChildren(1-dir,pivot);
    	pivot.setChildren(dir,root);
    	root.isBlack(false);
    	pivot.isBlack(true);
    	
    	pivot.setMax(root.getMax());
    	root.setMax(max(root.getLeft(),root.getRight(),root));
    	return pivot;
    }
	
    private List<DynInterval<T>> searchIntervals(DynInterval<T> interval, List<DynInterval<T>> intervalList)
    {
    	for (DynNode<T> node : searchNodes(interval))
    		intervalList.add(node.getInterval());
    	return intervalList;
    }
    
    private double max(DynNode<T> a, DynNode<T> b)
    {
    	return Math.max(a.getMax(), b.getMax());
    }
    
    private double max(DynNode<T> a, DynNode<T> b, DynNode<T> c)
    {
    	return Math.max(Math.max(a.getMax(), b.getMax()), c.getInterval().getEnd());
    }
		
}
