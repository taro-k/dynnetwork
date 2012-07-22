package org.cytoscape.dyn.internal.view.vizmap.mapping;

import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.vizmap.VisualMappingFunction;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;

public class DynDiscreteMappingFactory implements VisualMappingFunctionFactory
{

	@Override
	public <K, V> VisualMappingFunction<K, V> createVisualMappingFunction(final String attributeName,
			Class<K> attrValueType, final VisualProperty<V> vp) {

		return new DynDiscreteMappingImpl<K, V>(attributeName, attrValueType, vp);
	}

	@Override
	public String toString() {
		return DiscreteMapping.DISCRETE;
	}

	@Override
	public Class<?> getMappingFunctionType() {
		return DiscreteMapping.class;
	}

}
