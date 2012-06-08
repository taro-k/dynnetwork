package org.cytoscape.dyn.internal.loaddynnetwork;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.dyn.internal.model.DynNetworkManager;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewFactory;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public final class LoadNetworkViewTask<T> extends AbstractTask 
{
	private final CyApplicationManager appManager;
	private final DynNetworkManager<T> dynNetworkManager;
	private final DynNetworkViewFactory<T> dynNetworkViewFactory;
	
	public LoadNetworkViewTask(
			final CyApplicationManager appManager,
			final DynNetworkManager<T> dynNetworkManager,
			final DynNetworkViewFactory<T> dynNetworkViewFactory)
	{
		this.appManager = appManager;
		this.dynNetworkManager = dynNetworkManager;
		this.dynNetworkViewFactory = dynNetworkViewFactory;
	}
	
	public void run(TaskMonitor tm) throws Exception
	{
		tm.setProgress(0.0);
		dynNetworkViewFactory.createView(dynNetworkManager.getDynNetwork(appManager.getCurrentNetwork()));
		tm.setProgress(1.0);
	}
	
}
