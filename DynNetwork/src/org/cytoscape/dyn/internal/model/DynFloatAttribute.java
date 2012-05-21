package org.cytoscape.dyn.internal.model;

/**
 * {@inheritDoc}
 */
public class DynFloatAttribute extends DynAttribute<Float> {

	public DynFloatAttribute() {
		super();
	}
	
	public DynFloatAttribute(DynInterval<Float> interval){
		super(interval);
	}

	@Override
	public Class<Float> getType() {
		return Float.class;
	}

}