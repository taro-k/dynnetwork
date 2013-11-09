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

package org.cytoscape.dyn.internal.model.attribute;

import org.cytoscape.dyn.internal.io.read.util.KeyPairs;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.view.presentation.property.values.ArrowShape;

/**
 * <code> DynArrowShapeAttribute </code> implements ArrowShapeVisualProperty attributes and contains
 * a list of their interval times.
 * 
 * @author Sabina Sara Pfister
 * 
 */
public class DynArrowShapeAttribute extends AbstractDynAttribute<ArrowShape>
{
	/**
	 * <code> DynArrowShapeAttribute </code> constructor.
	 */
	public DynArrowShapeAttribute()
	{
		super(ArrowShape.class);
	}
	
	/**
	 * <code> DynArrowShapeAttribute </code> constructor.
	 * @param interval
	 * @param key
	 */
	public DynArrowShapeAttribute(DynInterval<ArrowShape> interval, KeyPairs key)
	{
		super(ArrowShape.class,interval,key);
	}
	
	@Override
	public ArrowShape getMinValue()
    {
    	return null;
    }
    
	@Override
	public ArrowShape getMaxValue()
	{
		return null;
	}
	
}
