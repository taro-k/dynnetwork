package org.cytoscape.dyn.internal.attributes;

/**
 * {@inheritDoc}
 */
public class DynDoubleAttribute extends DynAttribute<Double> {

	public DynDoubleAttribute() {
		super();
	}
	
	public DynDoubleAttribute(DynInterval<Double> interval){
		super(interval);
	}

	@Override
	public Class<Double> getType() {
		return Double.class;
	}

}


