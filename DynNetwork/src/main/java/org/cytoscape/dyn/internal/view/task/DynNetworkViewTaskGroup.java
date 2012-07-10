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

package org.cytoscape.dyn.internal.view.task;

import java.util.ArrayList;
import java.util.List;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class DynNetworkViewTaskGroup<T> extends AbstractTask 
{
	private final DynNetworkView<T> view;
	private final DynNetwork<T> dynNetwork;
	private final BlockingQueue queue;
	private final double low;
	private final double high;
	private final CyGroup group;
	private final int visibility;

	public DynNetworkViewTaskGroup(
			final DynNetworkView<T> view,
			final DynNetwork<T> dynNetwork,
			final BlockingQueue queue,
			double low, double high,
			final int visibility,
			final CyGroup group) 
	{
		this.view = view;
		this.dynNetwork = dynNetwork;
		this.queue = queue;
		this.low = low;
		this.high = high;
		this.group = group;
		this.visibility = visibility;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception 
	{
		queue.lock();
		
		view.updateView();

		List<CyNode> nodeList = new ArrayList<CyNode>();
		List<CyEdge> edgeList = new ArrayList<CyEdge>();
		if (group.isCollapsed(dynNetwork.getNetwork()))
		{
			nodeList.add(group.getGroupNode());
			edgeList = dynNetwork.getNetwork().getAdjacentEdgeList(group.getGroupNode(), CyEdge.Type.ANY);
		}
		else
		{
			nodeList = group.getNodeList();
			edgeList = group.getInternalEdgeList();
			edgeList.addAll(group.getExternalEdgeList());
		}

		for (CyNode node : nodeList)
		{
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY, visibility);
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY, visibility);
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY, visibility);
		}
		for (CyEdge edge : edgeList)
			view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY, visibility);
		
		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchNodes(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : intervalList)
		{
			CyNode node = dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow());
			if (node!=null && nodeList.contains(node))
			{
				view.writeVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY, 255);
			}
		}
		
		// update edges
		intervalList = dynNetwork.searchEdges(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : intervalList)
		{
			CyEdge edge = dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow());
			if (edge!=null && edgeList.contains(edge))
				view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY, 255);
		}

		view.updateView();
		
		queue.unlock(); 

	}

}

