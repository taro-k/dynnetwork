package org.cytoscape.dyn.internal.view.model;

import java.util.Collection;

import org.cytoscape.dyn.internal.model.DynNetwork;

public interface DynNetworkViewManager<T>
{
	public void addDynNetworkView(DynNetwork<T> dynNetwork, DynNetworkView<T> dynNetworkView);

	public DynNetworkView<T> getDynNetworkView(DynNetwork<T> dynNetwork);
	
	public Collection<DynNetworkView<T>> getDynNetworkViews();
}