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

package org.cytoscape.dyn.internal.read.xgmml.handler;

import org.cytoscape.dyn.internal.event.Sink;
import org.cytoscape.dyn.internal.event.Source;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 * <code> AbstractXGMMLSource </code> is an abstract class to generates 
 * graph events from file parsing.
 * 
 * @author sabina
 *
 * @param <T>
 */
public abstract class AbstractXGMMLSource<T> implements Source<T>
{
	// Note: i don't implement generation of events, since xgmml reading is almost sequential 
	// and direct calling of sink methods is much faster.

	protected Sink<T> sink;

	protected DynNetwork<T> addGraph(
			String id, String label, String start, String end, String directed)
	{
		return sink.addedGraph(id, label, start, end, directed);
	}
	
	protected CyNode addNode(DynNetwork<T> currentNetwork, CyGroup group, 
			String id, String label, String start, String end)
	{
		return sink.addedNode(currentNetwork, group, id, label, start, end);
	}
	
	protected CyEdge addEdge(DynNetwork<T> currentNetwork, 
			String id, String label, String source, String target, String start, String end)
	{
		return sink.addedEdge(currentNetwork, id, label, source, target, start, end);
	}
	
	protected CyGroup addGroup(DynNetwork<T> currentNetwork, CyNode currentNode)
	{
		return sink.addedGroup(currentNetwork, currentNode);
	}
	
	protected void addGraphAttribute(DynNetwork<T> currentNetwork, 
			String name, String value, String type, String start, String end)
	{
		sink.addedGraphAttribute(currentNetwork, name, value, type, start, end);
	}
	
	protected void addNodeAttribute(DynNetwork<T> network, CyNode currentNode, 
			String name, String value, String type, String start, String end)
	{
		sink.addedNodeAttribute(network, currentNode, name, value, type, start, end);
	}
	
	protected void addEdgeAttribute(DynNetwork<T> network, CyEdge currentEdge, 
			String name, String value, String type, String start, String end)
	{
		sink.addedEdgeAttribute(network, currentEdge, name, value, type, start, end);
	}
	
	protected void deleteGraph(DynNetwork<T> netwrok)
	{
		sink.deletedGraph(netwrok);
	}

	protected void deleteNode(DynNetwork<T> currentNetwork, CyNode node)
	{
		sink.deletedNode(currentNetwork, node);
	}
	
	protected void deleteEdge(DynNetwork<T> currentNetwork, CyEdge edge)
	{
		sink.deletedEdge(currentNetwork, edge);
	}
	
	protected void deleteGraphAttribute(DynNetwork<T> currentNetwork, CyNetwork netwrok, String label)
	{
//		sink.deletedGraphAttribute(currentNetwork, netwrok, label);
	}
	
	protected void deleteNodeAttribute(DynNetwork<T> currentNetwork, CyNode node, String label)
	{
//		sink.deletedNodeAttribute(currentNetwork, node, label);
	}
	
	protected void deleteEdgeAttribute(DynNetwork<T> currentNetwork, CyEdge edge, String label)
	{
//		sink.deletedEdgeAttribute(currentNetwork, edge, label);
	}
	
	protected void finalize(DynNetwork<T> currentNetwork)
	{
		sink.finalizeNetwork(currentNetwork);
	}
	
	abstract protected DynNetworkView<T> createView(DynNetwork<T> dynNetwork);

	@Override
	public void addSink(Sink<T> sink) 
	{
		this.sink = sink;
	}
	
	@Override
	public void removeSink(Sink<T> sink) 
	{
		if (this.sink == sink)
			this.sink = null;
	}
	
}
