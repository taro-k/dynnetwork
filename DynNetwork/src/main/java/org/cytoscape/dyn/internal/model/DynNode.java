package org.cytoscape.dyn.internal.model;

import java.util.List;

/**
 * <code> DynNode </code> represent a node in the red-black interval tree <code> IntervalTree </code>.
 * To each node is associated an interval <code> DynInterval </code> and a color (black or red). 
 *  
 * @author sabina
 *
 * @param <T>
 */
public final class DynNode<T>
{
	private DynNode<T> parent;
	private DynNode<T> left;
	private DynNode<T> right;
	
	private boolean isBlack = true;	
	private DynInterval<T> interval;
	private double max = Double.NEGATIVE_INFINITY;

	public DynNode()
	{
		this.parent = this;
		this.left = this;
		this.right = this;
	}
	
	public DynNode(DynInterval<T> interval, DynNode<T> nil)
	{
		this.parent = nil;
		this.left = nil;
		this.right = nil;
		this.interval = interval;
	}
	
	public DynNode(DynNode<T> template)
	{
		this.interval = template.interval;
		this.max = template.max;
		this.isBlack = template.isBlack;
	}

	public boolean isLeaf()
	{
		return (interval==null);
	}
	
	public DynInterval<T> getInterval()
	{
		return interval;
	}

	public void setInterval(DynInterval<T> interval)
	{
		this.interval = interval;
	}

	public DynNode<T> getParent()
	{
		return parent;
	}
	
	public void setParent(DynNode<T> parent) {
		this.parent = parent;
	}
	
	public DynNode<T> getLeft()
	{
		return left;
	}

	public DynNode<T> getRight()
	{
		return right;
	}
	
	public void setLeft(DynNode<T> left)
	{
		this.left = left;
		left.parent = this;
	}	

	public void setRight(DynNode<T> right)
	{
		this.right = right;
		right.parent = this;
	}
	
	public double getMax()
	{
		return max;
	}
	
	public void computeMax()
	{
		if (this.interval!=null)
			this.max = Math.max(Math.max(this.left.max, this.right.max),this.interval.getEnd());
	}
	
	public boolean isBlack()
	{
		return this.isBlack;
	}

	public void isBlack(boolean isBlack)
	{
		this.isBlack = isBlack;
	}
	
	public DynNode<T> deepCopy()
	{
		if (!this.isLeaf())
		{
			DynNode<T> copy = new DynNode<T>(this);
			copy.setLeft(this.getLeft().deepCopy());
			copy.setRight(this.getRight().deepCopy());
			return copy;
		}
		else
			return this;
	}
	
	public List<DynNode<T>> searchNodes(DynInterval<T> interval, List<DynNode<T>> nodeList)
	{
		if (!this.isLeaf() && interval.getStart()<=this.getMax())
		{
			this.left.searchNodes(interval, nodeList);
			if (this.getInterval().compareTo(interval)>0)
				nodeList.add(this);
			this.right.searchNodes(interval, nodeList);
		}
		return nodeList;
	}

	public void print()
	{
		if(!this.isLeaf())	
		{
			this.getLeft().print();
			System.out.println("NODE " + " " + this.isBlack + " " + interval.getStart() + " " + interval.getEnd() + " " + this.max);
			this.getRight().print();
		}
		else
			System.out.println("LEAF " + this.isBlack);
	}
	

}
