/*
 * DynNetwork plugin for Cytoscape 3.0 (http://www.cytoscape.org/).
 * Copyright (C) 2012 Sabina Sara Pfister
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.cytoscape.dyn.internal.view.vizmap.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.vizmap.mappings.AbstractVisualMappingFunction;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;

/**
 * <code> DynDiscreteMappingImpl </code> is a dynamic implementation of a 
 * {@link AbstractVisualMappingFunction}.
 * 
 * @author cytoscape
 *
 */
public class DynDiscreteMappingImpl<K, V> extends AbstractVisualMappingFunction<K, V> implements DiscreteMapping<K, V> 
{

	private final Map<K, V> attribute2visualMap;
	
	/**
	 * Constructor.
	 * 
	 * @param attrName
	 * @param attrType
	 * @param vp
	 */
	public DynDiscreteMappingImpl(
			final String attrName, 
			final Class<K> attrType, 
			final VisualProperty<V> vp) 
	{
		super(attrName, attrType, vp);
		attribute2visualMap = new HashMap<K, V>();
	}

	@Override
	public String toString() 
	{
		return DiscreteMapping.DISCRETE;
	}

	@Override
	public V getMappedValue(final CyRow row) 
	{
		V value = null;
		
		if (row != null && row.isSet(columnName)) 
		{
			final CyColumn column = row.getTable().getColumn(columnName);
			final Class<?> attrClass = column.getType();

			if (attrClass.isAssignableFrom(List.class)) 
			{
				List<?> list = row.getList(columnName, column.getListElementType());

				if (list != null) 
				{
					for (Object item : list) 
					{
						String key = item.toString();
						value = attribute2visualMap.get(key);

						if (value != null)
							break;
					}
				}
			} 
			else 
			{
				Object key = row.get(columnName, columnType);

				if (key != null)
					value = attribute2visualMap.get(key);
			}
		}
		
		return value;
	}
	
	@Override
	public V getMapValue(K key) 
	{
		return attribute2visualMap.get(key);
	}

	@Override
	public <T extends V> void putMapValue(final K key, final T value) 
	{
		attribute2visualMap.put(key, value);
	}

	@Override
	public <T extends V> void putAll(Map<K, T> map) 
	{
		attribute2visualMap.putAll(map);
	}

	@Override
	public Map<K, V> getAll() 
	{
		return attribute2visualMap;
	}
}
