package org.cytoscape.dyn.internal.attributes;

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


