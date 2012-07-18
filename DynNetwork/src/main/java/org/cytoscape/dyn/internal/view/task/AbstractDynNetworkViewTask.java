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
import org.cytoscape.dyn.internal.tree.DynInterval;
import org.cytoscape.dyn.internal.view.gui.AdvancedDynCytoPanel;
import org.cytoscape.dyn.internal.view.gui.DynCytoPanel;
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

/**
 * <code> AbstractDynNetworkViewTask </code> is the abstract calls all visual task
 * have to extend. It provides the functionality to communicate with {@link AdvancedDynCytoPanel},
 * and update the visualization.
 * 
 * @author sabina
 *
 * @param <T>
 */
public class AbstractDynNetworkViewTask<T,C>  implements Runnable
{
	protected final DynCytoPanel<T,C> panel;
	protected final DynNetworkView<T> view;
	protected final DynNetwork<T> dynNetwork;
	protected final DynLayout<T> layout;
	protected final BlockingQueue queue;
	protected final double low;
	protected final double high;
	
	protected double timeStart;
	protected double timeEnd;
	
	protected DynInterval<T> timeInterval;
	
	protected boolean cancelled = false;
	
	public AbstractDynNetworkViewTask(
			final DynCytoPanel<T, C> panel,
			final DynNetworkView<T> view,
			final DynLayout<T> layout,
			final BlockingQueue queue, 
			final double low, 
			final double high) 
	{
		this.panel = panel;
		this.view = view;
		this.dynNetwork = view.getNetwork();
		this.layout = layout;
		this.queue = queue;
		this.low = low;
		this.high = high;
	}

	public void cancel() 
	{
		this.cancelled = true;
	}

	@Override
	public void run()
	{
		
	}
	
	protected void updateAttr(DynInterval<T> interval)
	{
		dynNetwork.writeGraphTable(interval.getAttribute().getColumn(), interval.getValue(timeInterval));
	}
	
	protected void updateAttr(CyNode node, DynInterval<T> interval)
	{
		if (node!=null)
			dynNetwork.writeNodeTable(node, interval.getAttribute().getColumn(), interval.getValue(timeInterval));
	}
	
	protected void updateAttr(CyEdge edge, DynInterval<T> interval)
	{
		if (edge!=null)
			dynNetwork.writeEdgeTable(edge, interval.getAttribute().getColumn(), interval.getValue(timeInterval));
	}
	
	protected void updatePosition(List<DynInterval<T>> intervalList, double alpha, int n)
	{
		for (int i=0;i<n;i++)
		{
			timeStart = System.currentTimeMillis();

			for (DynInterval<T> interval : intervalList)
			{
				CyNode node = dynNetwork.getNode(interval.getAttribute().getKey().getRow());
				if (node!=null)
					if (interval.getAttribute().getColumn().equals("node_X_Pos"))
						view.writeVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION, 
								((1-alpha)*view.readVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION)+alpha*(Double)interval.getValue()));
					else if (interval.getAttribute().getColumn().equals("node_Y_Pos"))
						view.writeVisualProperty(node, BasicVisualLexicon.NODE_Y_LOCATION, 
								((1-alpha)*view.readVisualProperty(node, BasicVisualLexicon.NODE_Y_LOCATION)+alpha*(Double)interval.getValue()));
					else if (interval.getAttribute().getColumn().equals("node_Z_Pos"))
						view.writeVisualProperty(node, BasicVisualLexicon.NODE_Z_LOCATION, 
								((1-alpha)*view.readVisualProperty(node, BasicVisualLexicon.NODE_Z_LOCATION)+alpha*(Double)interval.getValue()));
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

	protected void switchTransparency(CyNode node, int visibility)
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

	protected void switchTransparency(CyEdge edge, int visibility)
	{
		if (edge!=null)
		{
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY,
					view.readVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY)<255?255:visibility);
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_LABEL_TRANSPARENCY,
					view.readVisualProperty(edge, BasicVisualLexicon.EDGE_LABEL_TRANSPARENCY)<255?255:visibility);
		}
	}
	
	protected void setTransparency(CyNode node, int visibility)
	{
		if (node!=null)
		{
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY,visibility);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY,visibility);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY,visibility);
		}
	}

	protected void setTransparency(CyEdge edge, int visibility)
	{
		if (edge!=null)
		{
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY,visibility);
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_LABEL_TRANSPARENCY,visibility);
		}
	}
	
	protected void updateTransparency(int visibility) 
	{
		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchNodesNot(timeInterval);
		for (DynInterval<T> interval : intervalList)
			setTransparency(dynNetwork.getNode(interval.getAttribute().getKey().getRow()), visibility);

		// update edges
		intervalList = dynNetwork.searchEdgesNot(timeInterval);
		for (DynInterval<T> interval : intervalList)
			setTransparency(dynNetwork.getEdge(interval.getAttribute().getKey().getRow()), visibility);
	}
	
}
