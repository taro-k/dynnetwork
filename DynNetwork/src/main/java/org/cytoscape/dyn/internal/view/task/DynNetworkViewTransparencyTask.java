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
import org.cytoscape.dyn.internal.view.gui.AdvancedDynCytoPanel;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.view.model.CyNetworkView;

/**
 * <code> DynNetworkViewTransparencyTask </code> is responsible for updating the {@link CyNetworkView}
 * everytime the background transparency level is set to show/hide elements of the network that are
 * not in the current time interval.
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class DynNetworkViewTransparencyTask<T,C> extends AbstractDynNetworkViewTask<T,C>  
{
	private final int visibility;

	/**
	 * <code> DynNetworkViewTransparencyTask </code> constructor.
	 * @param panel
	 * @param view
	 * @param queue
	 * @param low
	 * @param high
	 * @param visibility
	 */
	public DynNetworkViewTransparencyTask(
			final AdvancedDynCytoPanel<T,C> panel,
			final DynNetworkView<T> view,
			final BlockingQueue queue,
			final double low, 
			final double high, 
			final int visibility) 
	{
		super(panel, view, null, queue, low, high);
		this.visibility = visibility;
	}

	@Override
	public void run() 
	{
		queue.lock(); 
		
		DynInterval<T> timeInterval = new DynInterval<T>(low, high);
		
		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchNodesNot(timeInterval);
		for (DynInterval<T> interval : intervalList)
			setTransparency(dynNetwork.getNode(interval.getAttribute().getKey().getRow()), visibility);
		
		// update edges
		intervalList = dynNetwork.searchEdgesNot(timeInterval);
		for (DynInterval<T> interval : intervalList)
			setTransparency(dynNetwork.getEdge(interval.getAttribute().getKey().getRow()), visibility);

		view.updateView();
		
		queue.unlock();
	}

}

