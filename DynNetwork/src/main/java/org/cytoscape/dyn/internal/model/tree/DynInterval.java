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

/**
 * <code> DynInterval </code> represent a value and its right half-open interval. 
 * Intervals are convenient for representing events that each occupy a continuous 
 * period of time. A half-open interval is an ordered pair of real numbers [startTime, 
 * endTime[ with startTime =< endTime, where startTime is included and endTime is excluded.
 *  
 * @author sabina
 *
 * @param <T>
 */
public final class DynInterval<T> implements Comparable<DynInterval<T>>
{
	private Class<T> type;
	
	private T value;
	private double start;
	private double end;
	
	private DynAttribute<T> attribute;
	
	public DynInterval(Class<T> type, T value, double start, double end)
	{
		this.value = value;
		this.start = start;
		this.end = end;
	}

	public DynInterval(double start, double end)
	{
		this(null, null, start, end);
	}

	@Override
	public int compareTo(DynInterval<T> interval)
	{
		if ((start <= interval.end && interval.start < end)
				|| (start == end && start <= interval.end && interval.start <= end))
			return 1;
		else
			return -1;
	}
	
	public T getValue()
	{
		return value;
	}
	
	public T getValue(DynInterval<T> interval)
	{
		if (this.compareTo(interval)>0)
			return value;
		else
			return null;
	}

	public void setStart(double start)
	{
		this.start = start;
	}

	public void setEnd(double end)
	{
		this.end = end;
	}

	public double getStart()
	{
		return start;
	}

	public double getEnd()
	{
		return end;
	}
	
	public DynAttribute<T> getAttribute()
	{
		return attribute;
	}

	public void setAttribute(DynAttribute<T> attribute) 
	{
		this.attribute = attribute;
	}
	
	public Class<T> getType()
	{
		return type;
	}
    
}
