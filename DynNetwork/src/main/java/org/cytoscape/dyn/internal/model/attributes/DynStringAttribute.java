package org.cytoscape.dyn.internal.model.attributes;

import org.cytoscape.dyn.internal.model.tree.DynInterval;

/**
 * {@inheritDoc}
 */
public class DynStringAttribute extends DynAttribute<String>
{

	public DynStringAttribute()
	{
		super();
	}
	
	public DynStringAttribute(DynInterval<String> interval)
	{
		super(interval);
	}

	@Override
	public Class<String> getType()
	{
		return String.class;
	}

}


