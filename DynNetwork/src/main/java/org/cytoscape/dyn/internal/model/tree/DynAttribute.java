package org.cytoscape.dyn.internal.model.tree;

import java.util.ArrayList;
import java.util.List;

import org.cytoscape.dyn.internal.util.KeyPairs;

/**
 * <code> DynAttribute </code> is the abstract class to set/request represents all 
 * dynamic attributes, i.e. a list of intervals containing the value of type T 
 * and the time interval.
 * 
 * @author sabina
 *
 * @param <T>
 */
public class DynAttribute<T>
{
	private Class<T> type;
	
	private List<DynInterval<T>> intervalList;
	
	private KeyPairs key;
	
	private List<DynAttribute<T>> children;
	
	public DynAttribute(Class<T> type)
	{
		this.type = type;
		intervalList = new ArrayList<DynInterval<T>>();
		children = new ArrayList<DynAttribute<T>>();
	}
	
	public DynAttribute(DynInterval<T> interval, KeyPairs key)
	{
		this(interval.getType());
		this.key = key;
		this.intervalList.add(interval);
		interval.setAttribute(this);
	}
		
	public void addInterval(DynInterval<T> interval)
	{
		intervalList.add(interval);
		interval.setAttribute(this);
	}
	
    public List<DynInterval<T>> getIntervalList()
    {
		return intervalList;
	}
    
    public List<DynInterval<T>> getRecursiveIntervalList(ArrayList<DynInterval<T>> list)
    {
    	for (DynInterval<T> interval : intervalList)
    		list.add(interval);
    	for (DynAttribute<T> attr : children)
    		attr.getRecursiveIntervalList(list);
		return list;
	}
    
    public void setKey(long row, String column)
    {
    	this.key = new KeyPairs(column, row);
    }

	public KeyPairs getKey() 
	{
		return key;
	}
	
	public String getColumn() 
	{
		return key.getColumn();
	}
	
	public long getRow() 
	{
		return key.getRow();
	}
	
	public void addChildren(DynAttribute<T> attr)
	{
		this.children.add(attr);
	}
	
	public void removeChildren(DynAttribute<T> attr)
	{
		if (this.children.contains(attr))
			this.children.remove(attr);
	}

	public Class<T> getType()
	{
		return type;
	}
	
    public double getMinTime()
    {
            double minTime = Double.POSITIVE_INFINITY;
            for (DynInterval<T> i : intervalList)
                    minTime = Math.min(minTime, i.getStart());
            return minTime;
    }

	public double getMaxTime()
    {
            double maxTime = Double.NEGATIVE_INFINITY;
            for (DynInterval<T> i : intervalList)
                    maxTime = Math.max(maxTime, i.getEnd());
            return maxTime;
    }

}
