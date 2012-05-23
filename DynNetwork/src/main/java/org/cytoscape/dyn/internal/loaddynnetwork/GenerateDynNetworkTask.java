package org.cytoscape.dyn.internal.loaddynnetwork;

import org.cytoscape.dyn.internal.events.DynNetworkEventManagerImpl;
import org.cytoscape.io.read.CyNetworkReader;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class GenerateDynNetworkTask<T> extends AbstractTask {
	
	private final String name;
	private final CyNetworkReader viewReader;
	private final CyNetworkManager networkManager;
	private final CyNetworkViewManager networkViewManager;
	private final CyNetworkNaming namingUtil;
	private final DynNetworkEventManagerImpl<T> manager;

	public GenerateDynNetworkTask(final String name, final CyNetworkReader viewReader, final DynNetworkEventManagerImpl<T> manager,
				final CyNetworkManager networkManager, final CyNetworkViewManager networkViewManager,
				final CyNetworkNaming namingUtil) {
		this.name = name;
		this.viewReader = viewReader;
		this.networkManager = networkManager;
		this.networkViewManager = networkViewManager;
		this.namingUtil = namingUtil;
		this.manager = manager;
	}

	public void run(final TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setProgress(0.0);

		final CyNetwork[] networks = viewReader.getNetworks();
		double numNets = (double)(networks.length);
		int i = 0;

		CyNetworkView view = null;
		for (CyNetwork network : networks) {

			String networkName = network.getRow(network).get(CyNetwork.NAME, String.class);
			if(networkName == null || networkName.trim().length() == 0) {
				networkName = name;
				if(networkName == null)
					networkName = "? (Name is missing)";
				
				network.getRow(network).set(CyNetwork.NAME, namingUtil.getSuggestedNetworkTitle(networkName));
			}
			networkManager.addNetwork(network);

			view = viewReader.buildCyNetworkView(network);
			networkViewManager.addNetworkView(view);
//			view.fitContent();
			
			manager.collapseAllGroups(network);
			view.fitContent();

			taskMonitor.setProgress((double)(++i)/numNets);
		}

	}

}
