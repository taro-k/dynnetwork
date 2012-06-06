package org.cytoscape.dyn.internal.view.model;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;

public class DynNetworkViewFactoryImpl<T> implements DynNetworkViewFactory<T>
{
	private final DynNetworkViewManager<T> viewManager;
	private final CyNetworkViewFactory cyNetworkViewFactory;
	private final CyNetworkViewManager networkViewManager;
	
	public DynNetworkViewFactoryImpl(
			DynNetworkViewManager<T> viewManager,
			CyNetworkViewFactory cyNetworkViewFactory,
			final CyNetworkViewManager networkViewManager)
	{
		this.viewManager = viewManager;
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		this.networkViewManager = networkViewManager;
	}
	
	@Override
	public DynNetworkView<T> createView(DynNetwork<T> dynNetwork)
	{
		DynNetworkViewImpl<T> dynNetworkView = new DynNetworkViewImpl<T>(dynNetwork, networkViewManager, cyNetworkViewFactory);
		viewManager.addDynNetworkView(dynNetwork, dynNetworkView);
		return dynNetworkView;
	}

}
