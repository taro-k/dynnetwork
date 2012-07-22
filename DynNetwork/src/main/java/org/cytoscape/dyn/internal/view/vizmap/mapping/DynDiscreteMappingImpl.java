package org.cytoscape.dyn.internal.view.vizmap.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.vizmap.mappings.AbstractVisualMappingFunction;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;

public class DynDiscreteMappingImpl<K, V> extends AbstractVisualMappingFunction<K, V> implements DiscreteMapping<K, V> {

	// contains the actual map elements (sorted)
	private final Map<K, V> attribute2visualMap;

	
	/**
	 * Constructor.
	 * 
	 * @param attrName
	 * @param attrType
	 * @param vp
	 */
	public DynDiscreteMappingImpl(final String attrName, final Class<K> attrType, final VisualProperty<V> vp) {
		super(attrName, attrType, vp);
		attribute2visualMap = new HashMap<K, V>();
	}

	@Override
	public String toString() {
		return DiscreteMapping.DISCRETE;
	}

	@Override
	public V getMappedValue(final CyRow row) {
		V value = null;
		
		if (row != null && row.isSet(columnName)) {
			// Skip if source attribute is not defined.
			// ViewColumn will automatically substitute the per-VS or global default, as appropriate
			final CyColumn column = row.getTable().getColumn(columnName);
			final Class<?> attrClass = column.getType();

			if (attrClass.isAssignableFrom(List.class)) {
				List<?> list = row.getList(columnName, column.getListElementType());

				if (list != null) {
					for (Object item : list) {
						// TODO: should we convert other types to String?
						String key = item.toString();
						value = attribute2visualMap.get(key);

						if (value != null)
							break;
					}
				}
			} else {
				Object key = row.get(columnName, columnType);

				if (key != null)
					value = attribute2visualMap.get(key);
			}
		}
		
		return value;
	}
	
	@Override
	public V getMapValue(K key) {
		return attribute2visualMap.get(key);
	}

	@Override
	public <T extends V> void putMapValue(final K key, final T value) {
		attribute2visualMap.put(key, value);
	}

	@Override
	public <T extends V> void putAll(Map<K, T> map) {
		attribute2visualMap.putAll(map);
	}

	@Override
	public Map<K, V> getAll() {
		return attribute2visualMap;
	}
}
