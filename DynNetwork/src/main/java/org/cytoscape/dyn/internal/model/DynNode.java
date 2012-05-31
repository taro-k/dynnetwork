package org.cytoscape.dyn.internal.model;

import java.util.List;

import org.cytoscape.dyn.internal.model.tree.DynInterval;

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
	private DynNode<T>[] children = new DynNode[2];
	
	private boolean isBlack = true;	
	private DynInterval<T> interval;
	private double max = Double.NEGATIVE_INFINITY;

	public DynNode()
	{
		this.parent = this;
		this.children[0] = this;
		this.children[1] = this;
	}
	
	public DynNode(DynInterval<T> interval, DynNode<T> nil)
	{
		this.parent = nil;
		this.children[0] = nil;
		this.children[1] = nil;
		this.interval = interval;
		this.max = interval.getEnd();
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
	
	public void setParent(DynNode<T> parent)
	{
		this.parent = parent;
	}
	
	public DynNode<T> getLeft()
	{
		return this.children[0];
	}

	public DynNode<T> getRight()
	{
		return this.children[1];
	}
	
	public DynNode<T> getChildren(int i)
	{
		return this.children[i];
	}
	
	public void setLeft(DynNode<T> left)
	{
		this.children[0] = left;
		left.parent = this;
	}	

	public void setRight(DynNode<T> right)
	{
		this.children[1] = right;
		right.parent = this;
	}
	
	public void setChildren(int i, DynNode<T> children)
	{
		this.children[i] = children;
		children.parent = this;
	}

	public void setMax(double max)
	{
		this.max = max;
	}
	
	public double getMax()
	{
		return max;
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
			copy.setChildren(0,this.children[0].deepCopy());
			copy.setChildren(1,this.children[1].deepCopy());
			return copy;
		}
		else
			return this;
	}
	
	public List<DynNode<T>> searchNodes(DynInterval<T> interval, List<DynNode<T>> nodeList)
	{
		if (!this.isLeaf() && interval.getStart()<=this.getMax())
		{
			this.children[0].searchNodes(interval, nodeList);
			if (this.getInterval().compareTo(interval)>0)
				nodeList.add(this);
			if (interval.getEnd()>this.interval.getStart())
				this.children[1].searchNodes(interval, nodeList);
		}
		return nodeList;
	}

	public String toString(String string)
	{
		if(!this.isLeaf())	
		{
			string = this.children[0].toString(string  + "\n");
			string = string + " node " + " " + this.isBlack + " " + interval.getStart() + " " + interval.getEnd() + " " + this.max + " >";
			if (!this.getParent().isLeaf())
				string = string + " parent " + " " + this.parent.interval.getStart() + " " + this.parent.interval.getEnd();
			if (!this.children[0].isLeaf())
				string = string + " left  " + " " + this.children[0].interval.getStart() + " " + this.children[0].interval.getEnd();
			if (!this.children[1].isLeaf())
				string = string + " right " + " " + this.children[1].interval.getStart() + " " + this.children[1].interval.getEnd();
			string = this.children[1].toString(string  + "\n");	
		}
		return string;
	}
	

}
