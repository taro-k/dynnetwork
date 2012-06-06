package org.cytoscape.dyn.internal.view.model;

import java.util.Map;
import java.util.WeakHashMap;

import org.cytoscape.dyn.internal.model.DynNetwork;

public class DynNetworkViewManagerImpl<T> implements DynNetworkViewManager<T>
{
	private final Map<DynNetwork<T>, DynNetworkView<T>> dynNetworkViewMap;

	public DynNetworkViewManagerImpl() 
	{
		dynNetworkViewMap = new WeakHashMap<DynNetwork<T>, DynNetworkView<T>>();
	}
	
	@Override
	public void addDynNetworkView(DynNetwork<T> dynNetwork, DynNetworkView<T> dynNetworkView)
	{
		this.dynNetworkViewMap.put(dynNetwork, dynNetworkView);
	}

	@Override
	public DynNetworkView<T> getDynNetworkView(DynNetwork<T> dynNetwork)
	{
		return dynNetworkViewMap.get(dynNetwork);
	}	
	
}
