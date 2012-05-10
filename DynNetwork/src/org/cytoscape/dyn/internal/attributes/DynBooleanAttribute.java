package org.cytoscape.dyn.internal.attributes;

/**
 * {@inheritDoc}
 */
public class DynBooleanAttribute extends DynAttribute<Boolean> {

	public DynBooleanAttribute() {
		super();
	}
	
	public DynBooleanAttribute(DynInterval<Boolean> interval){
		super(interval);
	}

	@Override
	public Class<Boolean> getType() {
		return Boolean.class;
	}

}