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

import org.cytoscape.dyn.internal.CyActivator;
import org.cytoscape.dyn.internal.model.DynNetworkManagerImpl;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewManagerImpl;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.task.AbstractNetworkViewTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.Tunable;

public class GraphMetricsTasks<T, C> extends AbstractNetworkViewTaskFactory {

	private DynNetworkViewManagerImpl<T> dynNetViewManager;
	private CyNetworkFactory networkFactory;
	private CyRootNetworkManager rootNetworkManager;
	private CyNetworkNaming nameUtil;
	private DynNetworkManagerImpl<T> dynNetworkManager;
	private CyActivator<T, C> cyActivator;
	private TaskManager<T, C> taskManager;

	/**
	 * @param taskManager
	 * @param dynNetViewManager
	 * @param cyNetworkViewManagerServiceRef
	 * @param cyNetworkFactoryServiceRef
	 * @param cyRootNetworkManagerServiceRef
	 * @param cyNetworkNamingServiceRef
	 * @param dynNetManager
	 */
	public GraphMetricsTasks(TaskManager<T, C> taskManager,
			DynNetworkViewManagerImpl<T> dynNetViewManager,
			CyNetworkViewManager cyNetworkViewManagerServiceRef,
			CyNetworkFactory cyNetworkFactoryServiceRef,
			CyRootNetworkManager cyRootNetworkManagerServiceRef,
			CyNetworkNaming cyNetworkNamingServiceRef,
			DynNetworkManagerImpl<T> dynNetManager,
			CyActivator<T, C> cyActivator) {
		this.taskManager = taskManager;
		this.dynNetViewManager = dynNetViewManager;
		this.networkFactory = cyNetworkFactoryServiceRef;
		this.rootNetworkManager = cyRootNetworkManagerServiceRef;
		this.nameUtil = cyNetworkNamingServiceRef;
		this.dynNetworkManager = dynNetManager;
		this.cyActivator = cyActivator;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.cytoscape.task.NetworkViewTaskFactory#createTaskIterator(org.cytoscape
	 * .view.model.CyNetworkView)
	 */

	@Override
	public TaskIterator createTaskIterator(CyNetworkView arg0) {
		// TODO Auto-generated method stub
		DynDirectedNetworkTask treatNetworkDirected = new DynDirectedNetworkTask();
		TaskIterator iterator = new TaskIterator(treatNetworkDirected);
		taskManager.execute(iterator);
		if (treatNetworkDirected.wantsDirected())
			return new TaskIterator(new DynamicDirectedBetweennessStress<T>(
					dynNetViewManager, arg0, networkFactory,
					rootNetworkManager, nameUtil, dynNetworkManager),
					new DynamicDirectedEccCloseRadCentro<T>(dynNetViewManager,
							arg0, networkFactory, rootNetworkManager, nameUtil,
							dynNetworkManager), new DynamicInOutDegree<T>(
							dynNetViewManager, arg0, networkFactory,
							rootNetworkManager, nameUtil, dynNetworkManager),
					new GraphMetricsPanelTask<T, C>(cyActivator,
							dynNetViewManager, arg0));
		else
			return new TaskIterator(new EigenVector<T>(dynNetViewManager, arg0,
					networkFactory, rootNetworkManager, nameUtil,
					dynNetworkManager), new DynamicDistEccCloseRad<T>(
					dynNetViewManager, arg0, networkFactory,
					rootNetworkManager, nameUtil, dynNetworkManager),
					new DynamicBetweennessStress<T>(dynNetViewManager, arg0,
							networkFactory, rootNetworkManager, nameUtil,
							dynNetworkManager), new DynamicDegree<T>(
							dynNetViewManager, arg0, networkFactory,
							rootNetworkManager, nameUtil, dynNetworkManager),
					new GraphMetricsPanelTask<T, C>(cyActivator,
							dynNetViewManager, arg0));
	}

}