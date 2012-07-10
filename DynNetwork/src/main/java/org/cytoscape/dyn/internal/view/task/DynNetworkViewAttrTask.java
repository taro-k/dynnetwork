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
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * <code> DynNetworkViewAttrTask </code> is the task that is responsible for updating
 * all attributes when the current time interval is changed.
 * 
 * @author sabina
 *
 * @param <T>
 */
public class DynNetworkViewAttrTask<T> extends AbstractTask 
{
	private final DynNetwork<T> dynNetwork;
	private final BlockingQueue queue;
	private final double low;
	private final double high;

	public DynNetworkViewAttrTask(
			final DynNetwork<T> dynNetwork,
			final BlockingQueue queue,
			double low, double high) 
	{
		this.dynNetwork = dynNetwork;
		this.queue = queue;
		this.low = low;
		this.high = high;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception 
	{
		queue.lock(); 

		//update selected node attributes
		List<CyNode> nodeListSelected = CyTableUtil.getNodesInState(dynNetwork.getNetwork(),"selected",true);
		if (!nodeListSelected.isEmpty())
		{
			List<DynInterval<T>> intervalList = dynNetwork.searchNodesAttr(new DynInterval<T>(low, high));
			for (CyNode node : nodeListSelected)
				for (DynInterval<T> interval : intervalList)
					if (nodeListSelected.contains(dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow())))
					{	
						dynNetwork.writeNodeTable(node, interval.getAttribute().getKey().getColumn(), interval.getValue());
//						System.out.println("time " + low + " set " + interval.getValue());
					}
		}

		//update selected edge attributes
		List<CyEdge> edgeListSelected = CyTableUtil.getEdgesInState(dynNetwork.getNetwork(),"selected",true);
		if (!edgeListSelected.isEmpty())
		{
			List<DynInterval<T>> intervalList = dynNetwork.searchEdgesAttr(new DynInterval<T>(low, high));
			for (CyEdge edge : edgeListSelected)
				for (DynInterval<T> interval : intervalList)
					if (edgeListSelected.contains(dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow())))
					{
						dynNetwork.writeEdgeTable(edge, interval.getAttribute().getKey().getColumn(), interval.getValue());
//						System.out.println("time " + low + " set " + interval.getValue());
					}
		}

		queue.unlock(); 

	}

}

