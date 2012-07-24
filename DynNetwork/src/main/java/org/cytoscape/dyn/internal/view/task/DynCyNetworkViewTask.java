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
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;

/**
 * <code> DynCyNetworkViewTask </code> is the task that is responsible for updating
 * the visualization of a static network {@link CyNetwork} by updating the time. In order 
 * to increase speed performance, only elements that changed from the last visualization are 
 * updated (by interval tree search over all elements). The dynamics of the visualization is 
 * stored in {@link DynLayout}.
 * 
 * @author sabina
 *
 * @param <T>
 * @param <C>
 */
public class DynCyNetworkViewTask<T,C> extends AbstractCyNetworkViewTask<T,C> 
{
	private boolean updateNodes = true;

	/**
	 * <code> DynCyNetworkViewTask </code> constructor.
	 * @param panel
	 * @param view
	 * @param layout
	 * @param queue
	 * @param low
	 * @param high
	 * @param visibility
	 */
	public DynCyNetworkViewTask(
			final AdvancedDynCytoPanel<T,C> panel,
			final CyNetworkView view,
			final DynLayout layout,
			final BlockingQueue queue,
			final double low, 
			final double high, 
			final int visibility) 
	{
		super(panel, view, layout, queue, low, high);
	}

	@Override
	public void run() 
	{
		queue.lock();
		
		if (this.cancelled==true)
			updateNodes=false;
		
		DynInterval<Double> timeInterval = new DynInterval<Double>(low, high);
		
		// update node positions
		if (layout!=null && this.updateNodes)
		{
			List<DynInterval<Double>> nodeIntervalList = layout.searchChangedNodePositions((DynInterval<Double>) timeInterval);
			if (!nodeIntervalList.isEmpty())
				updatePosition(nodeIntervalList, layout.getAlpha(), layout.getN());
		}
		
		view.updateView();
		
		queue.unlock(); 
	}

}

