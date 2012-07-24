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
import org.cytoscape.dyn.internal.view.gui.AdvancedDynCytoPanel;
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;

/**
 * <code> DynNetworkViewTask </code> is the task that is responsible for updating
 * the visualization of a static network {@link DynNetwork} by updating the time. In order 
 * to increase speed performance, only elements that changed from the last visualization are 
 * updated (by interval tree search over all elements). The dynamics of the network is stored in 
 * {@link DynNetwork}, whereas the dynamics of the visualization is stored in {@link DynLayout}.
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class DynNetworkViewTask<T,C> extends AbstractDynNetworkViewTask<T,C> 
{
	private final int visibility;
	
	private boolean updateNodes = true;

	/**
	 * <code> DynNetworkViewTask </code> constructor.
	 * @param panel
	 * @param view
	 * @param layout
	 * @param queue
	 * @param low
	 * @param high
	 * @param visibility
	 */
	public DynNetworkViewTask(
			final AdvancedDynCytoPanel<T,C> panel,
			final DynNetworkView<T> view,
			final DynLayout layout,
			final BlockingQueue queue,
			final double low, 
			final double high, 
			final int visibility) 
	{
		super(panel, view, layout, queue, low, high);
		this.visibility = visibility;
	}

	@SuppressWarnings("unchecked")
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
		if (layout!=null && this.updateNodes)
		{
			List<DynInterval<Double>> nodeIntervalList = layout.searchChangedNodePositions((DynInterval<Double>) timeInterval);
			if (!nodeIntervalList.isEmpty())
				updatePosition(nodeIntervalList, layout.getAlpha(), layout.getN());
		}

		panel.setNodes(dynNetwork.getVisibleNodes());
		panel.setEdges(dynNetwork.getVisibleEdges());
		
		view.updateView();
		
		queue.unlock(); 
	}

}

