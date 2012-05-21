package org.cytoscape.dyn.internal.model;

/**
 * {@inheritDoc}
 */
public class DynStringAttribute extends DynAttribute<String> {

	public DynStringAttribute() {
		super();
	}
	
	public DynStringAttribute(DynInterval<String> interval){
		super(interval);
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

}


