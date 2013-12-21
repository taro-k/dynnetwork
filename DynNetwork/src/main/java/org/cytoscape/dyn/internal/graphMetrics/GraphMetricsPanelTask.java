/*
 * DynNetwork plugin for Cytoscape 3.0 (http://www.cytoscape.org/).
 * Copyright (C) 2013 Jimmy Mahesh Morzaria
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
package org.cytoscape.dyn.internal.graphMetrics;

import java.util.Properties;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.dyn.internal.CyActivator;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewManagerImpl;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * @author Jimmy
 * 
 */
public class GraphMetricsPanelTask<T, C> extends AbstractTask {

	private CyActivator<T, C> cyActivator;
	private DynNetworkViewManagerImpl<T> dynNetViewManager;
	private CyNetworkView cyNetworkView;

	/**
	 * @param cyActivator
	 * @param dynNetViewManager
	 * @param cyNetworkView
	 */
	public GraphMetricsPanelTask(CyActivator<T, C> cyActivator,
			DynNetworkViewManagerImpl<T> dynNetViewManager,
			CyNetworkView cyNetworkView) {
		this.cyActivator = cyActivator;
		this.dynNetViewManager = dynNetViewManager;
		this.cyNetworkView = cyNetworkView;

	}

	@Override
	public void run(TaskMonitor monitor) throws Exception {
		// TODO Auto-generated method stub
		monitor.setTitle("Building the User Interface");
		DynNetworkView<T> view = dynNetViewManager
				.getDynNetworkView(cyNetworkView);
		DynNetwork<T> dynamicnetwork = view.getNetwork();
		GraphMetricsPanel<T, C> graphMetricsPanel = new GraphMetricsPanel<T, C>(
				this.cyActivator, dynamicnetwork);
		this.cyActivator.getCyServiceRegistrar().registerService(
				graphMetricsPanel, CytoPanelComponent.class, new Properties());

	}

}
