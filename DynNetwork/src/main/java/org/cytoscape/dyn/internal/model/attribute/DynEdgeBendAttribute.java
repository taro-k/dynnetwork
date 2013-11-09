package org.cytoscape.dyn.internal.model.attribute;

import org.cytoscape.dyn.internal.io.read.util.KeyPairs;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.view.presentation.property.values.Bend;

/**
 * <code> DynEdgeBendAttribute </code> implements EdgeBendVisualProperty attributes and contains
 * a list of their interval times.
 * 
 * @author Sabina Sara Pfister
 * 
 */
public class DynEdgeBendAttribute extends AbstractDynAttribute<Bend>
{
	/**
	 * <code> DynNodeShapeAttribute </code> constructor.
	 */
	public DynEdgeBendAttribute()
	{
		super(Bend.class);
	}
	
	/**
	 * <code> DynEdgeBendAttribute </code> constructor.
	 * @param interval
	 * @param key
	 */
	public DynEdgeBendAttribute(DynInterval<Bend> interval, KeyPairs key)
	{
		super(Bend.class,interval,key);
	}
	
	@Override
	public Bend getMinValue()
    {
    	return null;
    }
    
	@Override
	public Bend getMaxValue()
	{
		return null;
	}
	
}
