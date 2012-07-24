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
import java.util.Map;

import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.vizmap.VisualMappingFunction;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;
import org.cytoscape.view.vizmap.mappings.ValueTranslator;

/**
 * <code> DynPassthroughMappingFactory </code> is a dynamic implementation of a 
 * {@link VisualMappingFunctionFactory}.
 * 
 * @author cytoscape
 *
 */
public class DynPassthroughMappingFactory implements VisualMappingFunctionFactory
{

	private static final Map<Class<?>, ValueTranslator<?, ?>> TRANSLATORS = new HashMap<Class<?>, ValueTranslator<?,?>>();
	private static final ValueTranslator<Object, String> DEFAULT_TRANSLATOR = new StringTranslator();
	
	public void addValueTranslator(ValueTranslator<?, ?> translator, Map props) {
		if (translator != null)
			TRANSLATORS.put(translator.getTranslatedValueType(), translator);
	}
	
	public void removeValueTranslator(ValueTranslator<?, ?> translator, Map props) {
	}
	
	@Override
	public <K, V> VisualMappingFunction<K, V> createVisualMappingFunction(final String attributeName,
			final Class<K> attrValueType, final VisualProperty<V> vp) {

		final ValueTranslator<?, ?> translator = TRANSLATORS.get(vp.getRange().getType());

		if (translator != null)
			return new DynPassthroughMappingImpl<K, V>(attributeName, attrValueType, vp,
					(ValueTranslator<K, V>) translator);
		else
			return new DynPassthroughMappingImpl<K, V>(attributeName, attrValueType, vp,
					(ValueTranslator<K, V>) DEFAULT_TRANSLATOR);
	}

	@Override
	public String toString() {
		return PassthroughMapping.PASSTHROUGH;
	}

	@Override
	public Class<?> getMappingFunctionType() {
		return PassthroughMapping.class;
	}

}
