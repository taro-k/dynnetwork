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

import java.util.List;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * <code> DynNetworkViewTask </code> is the task that is responsible for updating
 * all elements when the current time interval is changed. In order to increase speed 
 * performance, only elements that changed from the last visualization are updated,
 * and only attributes of selected elements are updated.
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class DynNetworkViewTask<T> extends AbstractTask 
{
	private final DynNetworkView<T> view;
	private final DynNetwork<T> dynNetwork;
	private final BlockingQueue queue;
	private final double low;
	private final double high;
	private final int visibility;

	public DynNetworkViewTask(
			final DynNetworkView<T> view,
			final DynNetwork<T> dynNetwork,
			final BlockingQueue queue,
			final double low, final double high, final int visibility) 
	{
		this.view = view;
		this.dynNetwork = dynNetwork;
		this.queue = queue;
		this.low = low;
		this.high = high;
		this.visibility = visibility;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception 
	{
		queue.lock(); 
		
		DynInterval<T> timeInterval = new DynInterval<T>(low, high);
		
		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchChangedNodes(timeInterval);
		for (DynInterval<T> interval : intervalList)
			switchTransparency(dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow()));
		
		// update edges
		intervalList = dynNetwork.searchChangedEdges(timeInterval);
		for (DynInterval<T> interval : intervalList)
			switchTransparency(dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow()),interval);

		// update graph attributes
		intervalList = dynNetwork.searchChangedGraphsAttr(timeInterval);
		for (DynInterval<T> interval : intervalList)
			dynNetwork.writeGraphTable(interval.getAttribute().getKey().getColumn(), interval.getValue());

		//update selected node attributes
		List<CyNode> nodeListSelected = CyTableUtil.getNodesInState(dynNetwork.getNetwork(),"selected",true);
		if (!nodeListSelected.isEmpty())
		{
			intervalList = dynNetwork.searchNodesAttr(timeInterval);
			for (CyNode node : nodeListSelected)
				for (DynInterval<T> interval : intervalList)
					if (nodeListSelected.contains(dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow())))
						dynNetwork.writeNodeTable(node, interval.getAttribute().getKey().getColumn(), interval.getValue());
		}

		//update selected edge attributes
		List<CyEdge> edgeListSelected = CyTableUtil.getEdgesInState(dynNetwork.getNetwork(),"selected",true);
		if (!edgeListSelected.isEmpty())
		{
			intervalList = dynNetwork.searchEdgesAttr(timeInterval);
			for (CyEdge edge : edgeListSelected)
				for (DynInterval<T> interval : intervalList)
					if (edgeListSelected.contains(dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow())))
						dynNetwork.writeEdgeTable(edge, interval.getAttribute().getKey().getColumn(), interval.getValue());
		}

		view.updateView();
		
		queue.unlock(); 

	}

	private void switchTransparency(CyNode node)
	{
		if (node!=null)
		{
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY,
					Math.abs(view.readVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY)-255)+visibility);
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY,
					Math.abs(view.readVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY)-255)+visibility);
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY,
					Math.abs(view.readVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY)-255)+visibility);
		}
	}

	private void switchTransparency(CyEdge edge, DynInterval<T> interval)
	{
		if (edge!=null)
			view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY,
					Math.abs(view.readVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY)-255)+visibility);
	}

}

