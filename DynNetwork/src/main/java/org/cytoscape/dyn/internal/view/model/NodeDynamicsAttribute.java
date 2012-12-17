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

package org.cytoscape.dyn.internal.view.model;

import org.cytoscape.dyn.internal.layout.DynLayoutFactory;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyNode;

/**
 * <code> NodeGraphicsAttribute </code> is used to store node graphics attributes
 * to be added later to the visualization.
 * 
 * @author Sabina Sara Pfister
 * 
 */
public final class NodeDynamicsAttribute<T>
{
//	private final DynNetwork<T> currentNetwork;
	private final CyNode currentNode;
	
	private final String x;
	private final String y;
	private final String start;
	private final String end;
	
	/**
	 * <code> NodeGraphicsAttribute </code> constructor.
	 * @param currentNetwork
	 * @param currentNode
	 * @param x
	 * @param y
	 * @param end
	 * @param start
	 */
	public NodeDynamicsAttribute(
			DynNetwork<T> currentNetwork,
			CyNode currentNode,
			final String x, 
			final String y,   
			final String start,
			final String end)
	{
//		this.currentNetwork = currentNetwork;
		this.currentNode = currentNode;
		this.x = x;
		this.y = y;
		this.start = start;
		this.end = end;
	}

	/**
	 * Add node graphics attribute.
	 * @param viewFactory
	 */
	public void add(DynNetworkView<T> dynNetworkView, DynLayoutFactory<T> layoutFactory)
	{
		layoutFactory.setNodeDynamics(dynNetworkView, currentNode, x, y, start, end);
	}
	
}
