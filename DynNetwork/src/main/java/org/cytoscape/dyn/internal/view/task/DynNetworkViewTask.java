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
import org.cytoscape.dyn.internal.view.gui.DynCytoPanel;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * <code> DynNetworkViewTask </code> is the task that is responsible for updating
 * all elements when the current time interval is changed. In order to increase speed 
 * performance, only elements that changed from the last visualization are updated 
 * (by interval tree search over all elements), and only attributes of selected elements 
 * are updated (by linear search in single elements).
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class DynNetworkViewTask<T,C> extends AbstractTask 
{
	private final DynCytoPanel<T,C> panel;
	private final DynNetworkView<T> view;
	private final DynNetwork<T> dynNetwork;
	private final BlockingQueue queue;
	private final double low;
	private final double high;
	private final int visibility;

	public DynNetworkViewTask(
			final DynCytoPanel<T,C> panel,
			final DynNetworkView<T> view,
			final DynNetwork<T> dynNetwork,
			final BlockingQueue queue,
			final double low, final double high, final int visibility) 
	{
		this.panel = panel;
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
		{
			switchTransparency(dynNetwork.getNode(interval.getAttribute().getKey().getRow()));
//			System.out.println(low + " node " + interval.getValue() + " " + interval.getStart() + " " + interval.getEnd() + " " + view.readVisualProperty(dynNetwork.getNode(interval.getAttribute().getKey().getRow()), BasicVisualLexicon.NODE_TRANSPARENCY));
		}
		
		// update edges
//		System.out.println("EDGES");
		intervalList = dynNetwork.searchChangedEdges(timeInterval);
		for (DynInterval<T> interval : intervalList)
		{
			switchTransparency(dynNetwork.getEdge(interval.getAttribute().getKey().getRow()));
//			System.out.println(low + " edge " + interval.getValue() + " " + interval.getStart() + " " + interval.getEnd() + " " + view.readVisualProperty(dynNetwork.getEdge(interval.getAttribute().getKey().getRow()), BasicVisualLexicon.EDGE_TRANSPARENCY));
		}

		// update graph attributes
		List<String> columnList = new ArrayList<String>();
		for (DynInterval<T> interval : dynNetwork.getIntervals())
		{
			if (interval.getValue(timeInterval)!=null || !columnList.contains(interval.getAttribute().getKey().getColumn()))
			{
				dynNetwork.getNetwork().getRow(dynNetwork.getNetwork()).set(CyNetwork.SELECTED, true);
				dynNetwork.writeGraphTable(interval.getAttribute().getKey().getColumn(), interval.getValue(timeInterval));
			}
			columnList.add(interval.getAttribute().getKey().getColumn());
		}

		// update node attributes
		List<CyNode> selectedNodes = CyTableUtil.getNodesInState(dynNetwork.getNetwork(),"selected",true);
		for (CyNode node : dynNetwork.getNetwork().getNodeList())
		{
			columnList.clear();
			for (DynInterval<T> interval : dynNetwork.getIntervals(node))
			{
				if (interval.getValue(timeInterval)!=null || !columnList.contains(interval.getAttribute().getKey().getColumn()))
				{
					if (selectedNodes.contains(node))
						dynNetwork.getNetwork().getRow(node).set(CyNetwork.SELECTED, true);
					dynNetwork.writeNodeTable(node, interval.getAttribute().getKey().getColumn(), interval.getValue(timeInterval));
				}
				columnList.add(interval.getAttribute().getKey().getColumn());
			}
		}

		// update edge attributes
		List<CyEdge> selectedEdges = CyTableUtil.getEdgesInState(dynNetwork.getNetwork(),"selected",true);
		for (CyEdge edge : dynNetwork.getNetwork().getEdgeList())
		{
			columnList.clear();
			for (DynInterval<T> interval : dynNetwork.getIntervals(edge))
			{
				if (interval.getValue(timeInterval)!=null || !columnList.contains(interval.getAttribute().getKey().getColumn()))
				{
					if (selectedEdges.contains(edge))
						dynNetwork.getNetwork().getRow(edge).set(CyNetwork.SELECTED, true);
					dynNetwork.writeEdgeTable(edge, interval.getAttribute().getKey().getColumn(), interval.getValue(timeInterval));
				}
				columnList.add(interval.getAttribute().getKey().getColumn());
			}
		}
		
		panel.setNodes(dynNetwork.getVisibleNodes());
		panel.setEdges(dynNetwork.getVisibleEdges());
		
		queue.unlock(); 
	}

	private void switchTransparency(CyNode node)
	{
		if (node!=null)
		{
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY,
					Math.abs(view.readVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY)-255)+visibility);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY,
					Math.abs(view.readVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY)-255)+visibility);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY,
					Math.abs(view.readVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY)-255)+visibility);
		}
	}

	private void switchTransparency(CyEdge edge)
	{
		if (edge!=null)
		{
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY,
					Math.abs(view.readVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY)-255)+visibility);
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_LABEL_TRANSPARENCY,
					Math.abs(view.readVisualProperty(edge, BasicVisualLexicon.EDGE_LABEL_TRANSPARENCY)-255)+visibility);
		}
	}

}

