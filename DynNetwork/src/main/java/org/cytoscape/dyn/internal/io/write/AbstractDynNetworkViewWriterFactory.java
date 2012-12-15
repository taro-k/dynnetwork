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

package org.cytoscape.dyn.internal.io.write;

import java.io.File;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.RenderingEngine;

/**
 * <code> AbstractDynNetworkViewWriterFactory </code> is an abstract class to generates 
 * images from a network view.
 * 
 * @author Sabina Sara Pfister
 * 
 */
public abstract class AbstractDynNetworkViewWriterFactory<T> implements DynNetworkViewWriterFactory<T> 
{	
	@Override
	public CyEdge addedEdge(DynNetwork<T> dynNetwork, String id, String label,
			String source, String target, String start, String end) 
	{
		return null;
	}

	@Override
	public void addedEdgeAttribute(DynNetwork<T> dynNetwork,
			CyEdge currentEdge, String name, String value, String Type,
			String start, String end) 
	{

	}

	@Override
	public void addedEdgeGraphics(DynNetwork<T> dynNetwork, CyEdge currentEdge,
			String width, String fill) 
	{

	}

	@Override
	public DynNetwork<T> addedGraph(String id, String label, String start,
			String end, String directed) 
	{
		return null;
	}

	@Override
	public void addedGraphAttribute(DynNetwork<T> dynNetwork, String name,
			String value, String Type, String start, String end) 
	{

	}

	@Override
	public void addedGraphGraphics(DynNetwork<T> dynNetwork, String fill) 
	{

	}

	@Override
	public CyGroup addedGroup(DynNetwork<T> dynNetwork, CyNode currentNode) 
	{
		return null;
	}

	@Override
	public CyNode addedNode(DynNetwork<T> dynNetwork, CyGroup group, String id,
			String label, String start, String end) 
	{
		return null;
	}

	@Override
	public void addedNodeAttribute(DynNetwork<T> dynNetwork,
			CyNode currentNode, String name, String value, String Type,
			String start, String end) 
	{

	}

	@Override
	public void addedNodeGraphics(DynNetwork<T> dynNetwork, CyNode currentNode,
			String type, String height, String width, String x, String y,
			String fill, String linew, String outline) 
	{

	}

	@Override
	public DynNetworkView<T> createView(DynNetwork<T> dynNetwork) 
	{
		return null;
	}

	@Override
	public void deletedEdge(DynNetwork<T> dynNetwork, CyEdge edge) 
	{

	}

	@Override
	public void deletedGraph(DynNetwork<T> dynNetwork) 
	{

	}

	@Override
	public void deletedNode(DynNetwork<T> dynNetwork, CyNode node) 
	{

	}

	@Override
	public void finalizeNetwork(DynNetwork<T> dynNetwork) 
	{
		
	}

	@Override
	abstract public void updateView(DynNetwork<T> dynNetwork, double currentTime);

}
