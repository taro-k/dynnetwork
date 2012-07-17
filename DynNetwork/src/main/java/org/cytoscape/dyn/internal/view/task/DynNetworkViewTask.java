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
import org.cytoscape.dyn.internal.view.gui.DynCytoPanel;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
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
	private final double alpha;
	private final double n;

	private DynInterval<T> timeInterval;

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
		this.alpha = 0.2;
		this.n = 1/alpha;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception 
	{
		queue.lock();
		
		timeInterval = new DynInterval<T>(low, high);
		
		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchChangedNodes(timeInterval);
		for (DynInterval<T> interval : intervalList)
			switchTransparency(dynNetwork.getNode(interval.getAttribute().getKey().getRow()));
		
		// update edges
		intervalList = dynNetwork.searchChangedEdges(timeInterval);
		for (DynInterval<T> interval : intervalList)
			switchTransparency(dynNetwork.getEdge(interval.getAttribute().getKey().getRow()));

		// update graph attributes
		intervalList = dynNetwork.searchChangedGraphsAttr(timeInterval);
		for (DynInterval<T> interval : intervalList)
			updateAttr(interval);
		
		// update node attributes
		intervalList = dynNetwork.searchChangedNodesAttr(timeInterval);
		for (DynInterval<T> interval : intervalList)
			updateAttr(dynNetwork.getNode(interval.getAttribute().getKey().getRow()),interval);

		// update edge attributes
		intervalList = dynNetwork.searchChangedEdgesAttr(timeInterval);
		for (DynInterval<T> interval : intervalList)
			updateAttr(dynNetwork.getEdge(interval.getAttribute().getKey().getRow()),interval);
		
		// update node positions
		intervalList = view.searchChangedNodePositions(timeInterval);
		if (!intervalList.isEmpty())
			for (int i=0;i<n;i++)
				updatePosition(i, intervalList);
		
		panel.setNodes(dynNetwork.getVisibleNodes());
		panel.setEdges(dynNetwork.getVisibleEdges());
		
		view.updateView();
		
		queue.unlock(); 
	}
	
	private void updateAttr(DynInterval<T> interval)
	{
		dynNetwork.writeGraphTable(interval.getAttribute().getColumn(), interval.getValue(timeInterval));
	}
	
	private void updateAttr(CyNode node, DynInterval<T> interval)
	{
		if (node!=null)
			dynNetwork.writeNodeTable(node, interval.getAttribute().getColumn(), interval.getValue(timeInterval));
	}
	
	private void updateAttr(CyEdge edge, DynInterval<T> interval)
	{
		if (edge!=null)
			dynNetwork.writeEdgeTable(edge, interval.getAttribute().getColumn(), interval.getValue(timeInterval));
	}
	
	private void updatePosition(int i, List<DynInterval<T>> intervalList)
	{
		for (DynInterval<T> interval : intervalList)
		{
			CyNode node = dynNetwork.getNode(interval.getAttribute().getKey().getRow());
			if (node!=null)
				if (interval.getAttribute().getColumn().equals("node_X_Pos"))
				{
					System.out.println(i + " node " + interval.getAttribute().getKey().getRow() + " target " + interval.getValue() + " current" + view.readVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION));
					view.writeVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION, 
							((1-alpha*i)*view.readVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION)+alpha*i*(Double)interval.getValue()));
					System.out.println(i + " node " + interval.getAttribute().getKey().getRow() + " target " + interval.getValue() + " current" + view.readVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION));
				}
				else if (interval.getAttribute().getColumn().equals("node_Y_Pos"))
					view.writeVisualProperty(node, BasicVisualLexicon.NODE_Y_LOCATION, 
							((1-alpha*i)*view.readVisualProperty(node, BasicVisualLexicon.NODE_Y_LOCATION)+alpha*i*(Double)interval.getValue()));
				else if (interval.getAttribute().getColumn().equals("node_Z_Pos"))
					view.writeVisualProperty(node, BasicVisualLexicon.NODE_Z_LOCATION, 
							((1-alpha*i)*view.readVisualProperty(node, BasicVisualLexicon.NODE_Z_LOCATION)+alpha*i*(Double)interval.getValue()));
		}
		view.updateView();
	}

	private void switchTransparency(CyNode node)
	{
		if (node!=null)
		{
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY,
					view.readVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY)<255?255:visibility);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY,
					view.readVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY)<255?255:visibility);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY,
					view.readVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY)<255?255:visibility);
		}
	}

	private void switchTransparency(CyEdge edge)
	{
		if (edge!=null)
		{
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY,
					view.readVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY)<255?255:visibility);
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_LABEL_TRANSPARENCY,
					view.readVisualProperty(edge, BasicVisualLexicon.EDGE_LABEL_TRANSPARENCY)<255?255:visibility);
		}
	}

}

