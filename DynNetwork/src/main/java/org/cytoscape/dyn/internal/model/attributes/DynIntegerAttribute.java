package org.cytoscape.dyn.internal.model.attributes;

import org.cytoscape.dyn.internal.model.DynInterval;

/**
 * {@inheritDoc}
 */
public class DynIntegerAttribute extends DynAttribute<Integer> {

	public DynIntegerAttribute() {
		super();
	}
	
	public DynIntegerAttribute(DynInterval<Integer> interval){
		super(interval);
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

}


