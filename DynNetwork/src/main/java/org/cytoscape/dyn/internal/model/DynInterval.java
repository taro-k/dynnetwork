package org.cytoscape.dyn.internal.model;

/**
 * This Class represent a value and its right half-open interval. Intervals are convenient 
 * for representing events that each occupy a continuous period of time. A half-open interval 
 * is an ordered pair of real numbers [startTime, endTime[ with startTime =< endTime, where 
 * startTime is included and endTime is excluded.
 *  
 * @author sabina
 *
 * @param <T>
 */
public final class DynInterval<T> implements Comparable<DynInterval<T>>
{
	private T value;
	private double start;
	private double end;
	
	public static double minStartTime = Double.POSITIVE_INFINITY;
	public static double maxStartTime = Double.NEGATIVE_INFINITY;
	public static double minEndTime = Double.POSITIVE_INFINITY;
	public static double maxEndTime = Double.NEGATIVE_INFINITY;
	
	public DynInterval(T value, double start, double end)
	{
		this.value = value;
		this.start = start;
		this.end = end;
		
		if (!Double.isInfinite(start))
		{
			minStartTime = Math.min(minStartTime, start);
			maxStartTime = Math.max(maxStartTime, start);
		}
		if (!Double.isInfinite(end))
		{
			maxEndTime = Math.max(maxEndTime, end);
			minEndTime = Math.min(minEndTime, end);
		}
	}

	public DynInterval(double start, double end)
	{
		this(null, start, end);
	}
	
	public DynInterval<T> deepCopy()
	{
		return new DynInterval<T>(this.value, this.start, this.end);
	}

	@Override
	public int compareTo(DynInterval<T> interval)
	{
		if (start <= interval.end && interval.start < end)
			return 1;
		else
			return -1;
	}

	public T getValue() {
		return value;
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

	public static double getMinTime()
	{
		if (Double.isInfinite(minStartTime))
			if (Double.isInfinite(minEndTime))
				return -1;
			else
				return minEndTime;
		else
			return minStartTime;
	}

	public static double getMaxTime()
	{
		if (Double.isInfinite(maxEndTime))
			if (Double.isInfinite(maxStartTime))
				return 1;
			else
				return maxStartTime;
		else
			return maxEndTime;
	}
    
}
