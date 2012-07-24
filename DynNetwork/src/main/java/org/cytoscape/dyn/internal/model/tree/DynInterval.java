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

import org.cytoscape.dyn.internal.model.attribute.DynAttribute;


/**
 * <code> DynInterval </code> represent a value list and its right half-open interval. 
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
	
	/**
	 * <code> DynInterval </code> constructor.
	 * @param type
	 * @param value
	 * @param start
	 * @param end
	 */
	public DynInterval(Class<T> type, T value, double start, double end)
	{
		this.value = value;
		this.type = type;
		this.start = start;
		this.end = end;
	}

	/**
	 * <code> DynInterval </code> constructor.
	 * @param start
	 * @param end
	 */
	public DynInterval(double start, double end)
	{
		this(null, null, start, end);
	}

	@Override
	public int compareTo(DynInterval<T> interval)
	{
		if ((start <= interval.end && interval.start <= end) &&	
				((start < interval.end && interval.start < end) ||
				 (interval.start == interval.end && (start <= interval.end && interval.start < end)) ||
				 (start == end && (start < interval.end && interval.start <= end)) ||
				 (start == end && interval.start == interval.end && start == interval.end)))
			return 1;
		else
			return -1;
	}

	/**
	 * Get interval value.
	 * @return value
	 */
	public T getValue()
	{
		return value;
	}
	
	/**
	 * Get interval value if the time interval overlaps with the given time interval..
	 * @param interval
	 * @return value
	 */
	public T getValue(DynInterval<T> interval)
	{
		if (this.compareTo(interval)>0)
			return value;
		else
			return null;
	}

	/**
	 * Set time interval start.
	 * @param start
	 */
	public void setStart(double start)
	{
		this.start = start;
	}

	/**
	 * Set time interval end.
	 * @param end
	 */
	public void setEnd(double end)
	{
		this.end = end;
	}

	/**
	 * Get time interval start.
	 * @return start
	 */
	public double getStart()
	{
		return start;
	}

	/**
	 * Get time interval end.
	 * @return end
	 */
	public double getEnd()
	{
		return end;
	}
	
	/**
	 * Get the attribute corresponding to this time interval.
	 * @return
	 */
	public DynAttribute<T> getAttribute()
	{
		return attribute;
	}

	/**
	 * Set the attribute corresponding to this time interval.
	 * @param attribute
	 */
	public void setAttribute(DynAttribute<T> attribute) 
	{
		this.attribute = attribute;
	}
	
	/**
	 * Return class type
	 * @return class
	 */
	public Class<T> getType()
	{
		return type;
	}
    
}
