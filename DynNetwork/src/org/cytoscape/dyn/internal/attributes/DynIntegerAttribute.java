package org.cytoscape.dyn.internal.attributes;

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


