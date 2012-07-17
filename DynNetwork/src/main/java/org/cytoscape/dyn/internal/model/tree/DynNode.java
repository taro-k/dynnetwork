/*
 * DynNetwork plugin for Cytoscape 3.0 (http://www.cytoscape.org/).
 * Copyright (C) 2012 Sabina Sara Pfister
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.cytoscape.dyn.internal.model.tree;

import java.util.ArrayList;
import java.util.List;



/**
 * <code> DynNode </code> represent a node in the red-black interval tree {@link IntervalTree}.
 * To each node is associated an interval {@link DynInterval} and a color (black or red). 
 *  
 * @author sabina
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public final class DynNode<T>
{
	private DynNode<T> parent;
	private DynNode<T>[] children = new DynNode[2];
	
	private boolean isBlack = true;	
	private List<DynInterval<T>> intervalList = new ArrayList<DynInterval<T>>();
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
		this.intervalList.add(interval);
		this.max = interval.getEnd();
	}

	public boolean isLeaf()
	{
		return (intervalList.isEmpty());
	}
	
	public List<DynInterval<T>> getIntervalList()
	{
		return intervalList;
	}

	public void addInterval(DynInterval<T> interval)
	{
		this.intervalList.add(interval);
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
	
	public double getStart()
	{
		return this.intervalList.get(0).getStart();
	}
	
	public double getEnd()
	{
		return this.intervalList.get(0).getEnd();
	}
	
	public List<DynInterval<T>> searchAll(List<DynInterval<T>> intervalList)
	{
		if (!this.isLeaf())
		{
			this.children[0].searchAll(intervalList);
			for (DynInterval<T> interval : this.intervalList)
				intervalList.add(interval);
			this.children[1].searchAll(intervalList);
		}
		return intervalList;
	}
	
	public List<DynInterval<T>> searchNot(List<DynInterval<T>> intervalList, DynInterval<T> interval)
	{
		if (!this.isLeaf())
		{
			this.children[0].searchNot(intervalList, interval);
			if (this.intervalList.get(0).compareTo(interval)<0)
				for (DynInterval<T> i : this.intervalList)
					intervalList.add(i);
			this.children[1].searchNot(intervalList,interval);
		}
		
		return intervalList;
	}
	
	public List<DynInterval<T>> search(List<DynInterval<T>> intervalList, DynInterval<T> interval)
	{
		if (!this.isLeaf() && interval.getStart()<=this.getMax())
		{
			this.children[0].search(intervalList, interval);
			if (this.intervalList.get(0).compareTo(interval)>0)
				for (DynInterval<T> i : this.intervalList)
					intervalList.add(i);
			if (interval.getEnd()>=this.intervalList.get(0).getStart())
				this.children[1].search(intervalList, interval);
		}
		return intervalList;
	}
	
	public List<DynNode<T>> searchNodes(DynInterval<T> interval, List<DynNode<T>> nodeList)
	{
		if (!this.isLeaf() && interval.getStart()<=this.getMax())
		{
			this.children[0].searchNodes(interval, nodeList);
			if (this.intervalList.get(0).compareTo(interval)>0)
				nodeList.add(this);
			if (interval.getEnd()>this.intervalList.get(0).getStart() ||
					(this.intervalList.get(0).getStart()==this.intervalList.get(0).getEnd() && interval.getEnd()>=this.intervalList.get(0).getStart()))
				this.children[1].searchNodes(interval, nodeList);
		}
		return nodeList;
	}

	public String print(String string)
	{
		if(!this.isLeaf())	
		{
			string = this.children[0].print(string  + "\n");
			string = string + " node " + " " + intervalList.get(0).getStart() + " " + intervalList.get(0).getEnd() + " " + this.max + " >";
			if (!this.getParent().isLeaf())
				string = string + " parent " + " " + this.parent.intervalList.get(0).getStart() + " " + this.parent.intervalList.get(0).getEnd();
			if (!this.children[0].isLeaf())
				string = string + " left  " + " " + this.children[0].intervalList.get(0).getStart() + " " + this.children[0].intervalList.get(0).getEnd();
			if (!this.children[1].isLeaf())
				string = string + " right " + " " + this.children[1].intervalList.get(0).getStart() + " " + this.children[1].intervalList.get(0).getEnd();
			string = this.children[1].print(string  + "\n");	
		}
		return string;
	}
	

}
