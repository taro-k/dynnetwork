package org.cytoscape.dyn.internal.model.attribute;

import org.cytoscape.dyn.internal.io.read.util.KeyPairs;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.view.presentation.property.values.ArrowShape;

/**
 * <code> DynArrowShapeAttribute </code> implements ArrowShapeVisualProperty attributes and contains
 * a list of their interval times.
 * 
 * @author Sabina Sara Pfister
 * 
 */
public class DynArrowShapeAttribute extends AbstractDynAttribute<ArrowShape>
{
	/**
	 * <code> DynArrowShapeAttribute </code> constructor.
	 */
	public DynArrowShapeAttribute()
	{
		super(ArrowShape.class);
	}
	
	/**
	 * <code> DynArrowShapeAttribute </code> constructor.
	 * @param interval
	 * @param key
	 */
	public DynArrowShapeAttribute(DynInterval<ArrowShape> interval, KeyPairs key)
	{
		super(ArrowShape.class,interval,key);
	}
	
	@Override
	public ArrowShape getMinValue()
    {
    	return null;
    }
    
	@Override
	public ArrowShape getMaxValue()
	{
		return null;
	}
	
}
