package org.cytoscape.dyn.internal.model.attributes;

import org.cytoscape.dyn.internal.model.tree.DynInterval;

/**
 * {@inheritDoc}
 */
public class DynBooleanAttribute extends DynAttribute<Boolean>
{

	public DynBooleanAttribute()
	{
		super();
	}
	
	public DynBooleanAttribute(DynInterval<Boolean> interval)
	{
		super(interval);
	}

	@Override
	public Class<Boolean> getType()
	{
		return Boolean.class;
	}

}