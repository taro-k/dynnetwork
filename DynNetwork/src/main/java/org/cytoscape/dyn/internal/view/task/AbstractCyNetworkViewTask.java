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
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

/**
 * <code> AbstractCyNetworkViewTask </code> is the abstract calls all visual task
 * have to extend. It provides the functionality to communicate with {@link ?},
 * and update the visualization.
 * 
 * @author sabina
 *
 * @param <T>
 */
public abstract class AbstractCyNetworkViewTask<T,C>  implements Runnable
{
	protected final DynCytoPanel<T,C> panel;
	protected final CyNetworkView view;
	protected final CyNetwork network;
	protected final DynLayout layout;
	protected final BlockingQueue queue;
	protected final double low;
	protected final double high;
	
	protected double timeStart;
	protected double timeEnd;
	
	protected DynInterval<T> timeInterval;
	
	protected boolean cancelled = false;
	
	/**
	 * <code> AbstractCyNetworkViewTask </code> constructor.
	 * @param panel
	 * @param view
	 * @param layout
	 * @param queue
	 * @param low
	 * @param high
	 */
	public AbstractCyNetworkViewTask(
			final DynCytoPanel<T, C> panel,
			final CyNetworkView view,
			final DynLayout layout,
			final BlockingQueue queue, 
			final double low, 
			final double high) 
	{
		this.panel = panel;
		this.view = view;
		this.network = view.getModel();
		this.layout = layout;
		this.queue = queue;
		this.low = low;
		this.high = high;
	}

	/**
	 * Cancel task.
	 */
	public void cancel() 
	{
		this.cancelled = true;
	}

	@Override
	public void run()
	{
		
	}
	
	protected void updatePosition(List<DynInterval<Double>> intervalList, double alpha, int n)
	{
		for (int i=0;i<n;i++)
		{
			timeStart = System.currentTimeMillis();

			for (DynInterval<Double> interval : intervalList)
			{
				CyNode node = network.getNode(interval.getAttribute().getKey().getRow());
				if (node!=null)
					if (interval.getAttribute().getColumn().equals("node_X_Pos"))
						view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, 
								((1-alpha)*view.getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_X_LOCATION)+alpha*(Double)interval.getValue()));
					else if (interval.getAttribute().getColumn().equals("node_Y_Pos"))
						view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, 
								((1-alpha)*view.getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION)+alpha*(Double)interval.getValue()));
					else if (interval.getAttribute().getColumn().equals("node_Z_Pos"))
						view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Z_LOCATION, 
								((1-alpha)*view.getNodeView(node).getVisualProperty(BasicVisualLexicon.NODE_Z_LOCATION)+alpha*(Double)interval.getValue()));
			}

			view.updateView();
			timeEnd = System.currentTimeMillis();

			if (timeEnd-timeStart<50)
				try {
					Thread.sleep(50-(int) (timeEnd-timeStart));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
	
}
