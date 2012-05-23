package org.cytoscape.dyn.internal.model.attributes;

import java.util.ArrayList;
import java.util.List;

import org.cytoscape.dyn.internal.model.DynInterval;

/**
 * <code> DynAttribute </code> is the abstract class to set/request represents all dynamical attributes, i.e. a list of intervals containing the value of type
 * T and the time interval.
 * @author sabina
 *
 * @param <T>
 */
public abstract class DynAttribute<T> {

	//TODO: intervals should be hold in a tree for fast searching
	protected List<DynInterval<T>> intervalList;
	
	protected String type;
	
	public DynAttribute(){
		intervalList = new ArrayList<DynInterval<T>>();
	}
	
	public DynAttribute(DynInterval<T> interval){
		intervalList.add(interval);
	}
		
	public void addInterval(DynInterval<T> interval) {
		intervalList.add(interval);
	}

    public T getFirst(DynInterval<T> interval) {
        for (DynInterval<T> i : intervalList)
        {
        	if (i.compareTo(interval)==1)
        	{
        		return i.getValue();
        	}
        }
		return null;
    }
 
    public boolean getIsInRange(DynInterval<T> interval) {
        for (DynInterval<T> i : intervalList)
        {
        	if (i.compareTo(interval)==1)
        	{
        		return true;
        	}
        }
		return false;
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

	abstract public Class<?> getType();

}
