package org.cytoscape.dyn.internal.model.tree;

import org.cytoscape.view.presentation.property.values.Bend;

/**
 * <code> DynIntervalEdgeBend </code> implements Bend intervals.
 *  
 * @author Sabina Sara Pfister
 */
public class DynIntervalEdgeBend extends AbstractDynInterval<Bend>
{
	
	/**
	 * <code> DynIntervalEdgeBend </code> constructor.
	 * @param interval
	 * @param onValue
	 */
	public DynIntervalEdgeBend(DynInterval<Bend> interval, Bend onValue)
	{
		super(interval, onValue);
	}
	
	/**
	 * <code> DynIntervalEdgeBend </code> constructor.
	 * @param interval
	 */
	public DynIntervalEdgeBend(DynInterval<Bend> interval)
	{
		super(interval);
	}
	
	/**
	 * <code> DynIntervalEdgeBend </code> constructor.
	 * @param onValue
	 * @param start
	 * @param end
	 */
	public DynIntervalEdgeBend(Bend onValue, double start, double end)
	{
		super(onValue, start, end);
	}
	
	/**
	 * <code> DynIntervalEdgeBend </code> constructor.
	 * @param onValue
	 * @param offValue
	 * @param start
	 * @param end
	 */
	public DynIntervalEdgeBend(Bend onValue, Bend offValue, double start, double end)
	{
		super(onValue, offValue, start, end);
	}

	/**
	 * <code> DynIntervalNodeShape </code> constructor.
	 * @param start
	 * @param end
	 */
	public DynIntervalEdgeBend(double start, double end)
	{
		super(start, end);
	}

	@Override
	public int compareTo(DynInterval<Bend> interval)
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
	public Bend getOnValue()
	{
		return onValue;
	}
	
	@Override
	public Bend getOffValue()
	{
		return offValue;
	}
	
	@Override
	public Bend getOverlappingValue(DynInterval<Bend> interval)
	{
		if (this.compareTo(interval)>0)
			return onValue;
		else
			return offValue;
	}
	
	@Override
	public Bend interpolateValue(Bend value2, double alpha)
	{
		return onValue;
	}
	
}
