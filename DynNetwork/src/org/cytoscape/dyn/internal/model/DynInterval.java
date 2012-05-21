package org.cytoscape.dyn.internal.model;

/**
 * This Class represent a value and its interval in time
 * @author sabina
 *
 * @param <T>
 */
public final class DynInterval<T> implements Comparable<DynInterval<T>>
{
	private T value;
	private double start;
	private double end;
	
	public static double minTime = Double.POSITIVE_INFINITY;
	public static double maxTime = Double.NEGATIVE_INFINITY;

	public DynInterval(T value, double start, double end)
	{
		this.value = value;
		this.start = start;
		this.end = end;
		
		if (!Double.isInfinite(start))
			minTime = Math.min(minTime, start);
		if (!Double.isInfinite(end))
			maxTime = Math.max(maxTime, end);
	}

	public DynInterval(double start, double end)
	{
		this(null, start, end);
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
    
}
