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

import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.gui.DynCytoPanel;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;

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
public final class DynNetworkViewTask<T,C> extends AbstractDynNetworkViewTask<T,C> 
{
	private final int visibility;
	private final double alpha;
	private final double n;
	
	private boolean updateNodes = true;

	public DynNetworkViewTask(
			final DynCytoPanel<T,C> panel,
			final DynNetworkView<T> view,
			final BlockingQueue queue,
			final double low, 
			final double high, 
			final int visibility) 
	{
		super(panel, view, queue, low, high);
		this.visibility = visibility;
		this.alpha = 0.2;
		this.n = 20;
	}

	@Override
	public void run() 
	{
		queue.lock();
		
		if (this.cancelled==true)
			updateNodes=false;
		
		timeInterval = new DynInterval<T>(low, high);
		
		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchChangedNodes(timeInterval);
		for (DynInterval<T> interval : intervalList)
			switchTransparency(dynNetwork.getNode(interval.getAttribute().getKey().getRow()), visibility);
		
		// update edges
		intervalList = dynNetwork.searchChangedEdges(timeInterval);
		for (DynInterval<T> interval : intervalList)
			switchTransparency(dynNetwork.getEdge(interval.getAttribute().getKey().getRow()), visibility);

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
		if (this.updateNodes)
		{
			intervalList = view.searchChangedNodePositions(timeInterval);
			if (!intervalList.isEmpty())
				for (int i=0;i<n;i++)
					updatePosition(intervalList, alpha);
		}
		
		panel.setNodes(dynNetwork.getVisibleNodes());
		panel.setEdges(dynNetwork.getVisibleEdges());
		
		view.updateView();
		
		queue.unlock(); 
	}

}

