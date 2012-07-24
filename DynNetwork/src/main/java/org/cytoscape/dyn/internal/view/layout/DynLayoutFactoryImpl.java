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

package org.cytoscape.dyn.internal.view.layout;

import org.cytoscape.view.model.CyNetworkView;

/**
 * <code> DynLayoutFactoryImpl </code> implements the interface
 * {@link DynLayoutFactory} for creating {@link DynLayout}s.
 * 
 * @author sabina
 *
 * @param <T>
 */
public class DynLayoutFactoryImpl implements DynLayoutFactory 
{
	private final DynLayoutManager layoutManager;
	
	/**
	 * <code> DynLayoutFactoryImpl </code> constructor.
	 * @param layoutManager
	 */
	public DynLayoutFactoryImpl(DynLayoutManager layoutManager)
	{
		this.layoutManager = layoutManager;
	}
	
	@Override
	public DynLayoutImpl createLayout(CyNetworkView view)
	{
		DynLayoutImpl layout = new DynLayoutImpl(view);
		layoutManager.addDynLayout(layout);
		return layout;
	}

}