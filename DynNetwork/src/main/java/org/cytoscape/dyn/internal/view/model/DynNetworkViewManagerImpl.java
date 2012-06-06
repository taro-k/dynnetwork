package org.cytoscape.dyn.internal.view.model;

import java.util.Map;
import java.util.WeakHashMap;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.model.CyNetworkViewManager;

public class DynNetworkViewManagerImpl<T> implements DynNetworkViewManager<T>
{
	private final CyNetworkViewManager cyNetworkViewManager;
	private final Map<DynNetwork<T>, DynNetworkView<T>> dynNetworkViewMap;

	public DynNetworkViewManagerImpl(final CyNetworkViewManager cyNetworkViewManager) 
	{
		this.cyNetworkViewManager = cyNetworkViewManager;
		this.dynNetworkViewMap = new WeakHashMap<DynNetwork<T>, DynNetworkView<T>>();
	}
	
	@Override
	public void addDynNetworkView(DynNetwork<T> dynNetwork, DynNetworkView<T> dynNetworkView)
	{
		this.cyNetworkViewManager.addNetworkView(dynNetworkView.getNetworkView());
		this.dynNetworkViewMap.put(dynNetwork, dynNetworkView);
	}

	@Override
	public DynNetworkView<T> getDynNetworkView(DynNetwork<T> dynNetwork)
	{
		return dynNetworkViewMap.get(dynNetwork);
	}	
	
}
