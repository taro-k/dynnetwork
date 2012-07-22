package org.cytoscape.dyn.internal.view.vizmap.mapping;

import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.vizmap.mappings.AbstractVisualMappingFunction;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;
import org.cytoscape.view.vizmap.mappings.ValueTranslator;

public class DynPassthroughMappingImpl<K, V> extends AbstractVisualMappingFunction<K, V> implements
		PassthroughMapping<K,V> {

	private final ValueTranslator<K, V> translator;

	/**
	 * dataType is the type of the _attribute_ !! currently we force that to be
	 * the same as the VisualProperty;
	 * FIXME: allow different once? but how to coerce?
	 */
	public DynPassthroughMappingImpl(final String columnName, final Class<K> columnType, final VisualProperty<V> vp,
			final ValueTranslator<K, V> translator) {
		super(columnName, columnType, vp);
		this.translator = translator;
	}

	@Override
	public String toString() {
		return PassthroughMapping.PASSTHROUGH;
	}

	@Override
	@SuppressWarnings("unchecked")
	public V getMappedValue(final CyRow row) {
		if (row == null || !row.isSet(columnName))
			return null;

		K tableValue = null;
		final CyColumn column = row.getTable().getColumn(columnName);
		
		if (column != null) {
			final Class<?> columnClass = column.getType();
	
			try {
				tableValue = (K) row.get(columnName, columnClass);
			} catch (ClassCastException cce) {
				// Invalid
				return null;
			}
			
			Object value = translator.translate(tableValue);
			
			if (value instanceof String)
				value = vp.parseSerializableString((String) value);
			
			if (value != null) {
				try {
					return (V) value;
				} catch (ClassCastException cce) {
				}
			}
		}
		
		return null;
	}
}