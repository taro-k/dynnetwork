package org.cytoscape.dyn.internal.model.tree;

import org.cytoscape.view.presentation.property.values.ArrowShape;

/**
 * <code> DynIntervalArrowShape </code> implements ArrowShape intervals.
 *  
 * @author Sabina Sara Pfister
 */
public class DynIntervalArrowShape extends AbstractDynInterval<ArrowShape>
{
	/**
	 * <code> DynIntervalArrowShape </code> constructor.
	 * @param interval
	 * @param onValue
	 */
	public DynIntervalArrowShape(DynInterval<ArrowShape> interval, ArrowShape onValue)
	{
		super(interval, onValue);
	}
	
	/**
	 * <code> DynIntervalArrowShape </code> constructor.
	 * @param interval
	 */
	public DynIntervalArrowShape(DynInterval<ArrowShape> interval)
	{
		super(interval);
	}
	
	/**
	 * <code> DynIntervalArrowShape </code> constructor.
	 * @param onValue
	 * @param start
	 * @param end
	 */
	public DynIntervalArrowShape(ArrowShape onValue, double start, double end)
	{
		super(onValue, start, end);
	}
	
	/**
	 * <code> DynIntervalArrowShape </code> constructor.
	 * @param onValue
	 * @param offValue
	 * @param start
	 * @param end
	 */
	public DynIntervalArrowShape(ArrowShape onValue,  ArrowShape offValue, double start, double end)
	{
		super(onValue, offValue, start, end);
	}

	/**
	 * <code> DynIntervalArrowShape </code> constructor.
	 * @param start
	 * @param end
	 */
	public DynIntervalArrowShape(double start, double end)
	{
		super(start, end);
	}

	@Override
	public int compareTo(DynInterval<ArrowShape> interval)
	{
		if ((start <= interval.getEnd() && interval.getStart() <= end) &&	
				((start < interval.getEnd() && interval.getStart() < end) ||
				 (interval.getStart() == interval.getEnd() && (start <= interval.getEnd() && interval.getStart() < end)) ||
				 (start == end && (start < interval.getEnd() && interval.getStart() <= end)) ||
				 (start == end && interval.getStart() == interval.getEnd() && start == interval.getEnd())))
			return 1;
		else
			return -1;
	}

	@Override
	public ArrowShape getOnValue()
	{
		return onValue;
	}
	
	@Override
	public ArrowShape getOffValue()
	{
		return offValue;
	}
	
	@Override
	public ArrowShape getOverlappingValue(DynInterval<ArrowShape> interval)
	{
		if (this.compareTo(interval)>0)
			return onValue;
		else
			return offValue;
	}
	
	@Override
	public ArrowShape interpolateValue(ArrowShape value2, double alpha)
	{
		return onValue;
	}
	
	@Override
	public String getType()
	{
		return "A";
	}
	
}
