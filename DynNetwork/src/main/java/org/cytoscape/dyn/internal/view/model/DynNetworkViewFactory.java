package org.cytoscape.dyn.internal.view.model;

import org.cytoscape.dyn.internal.model.DynNetwork;

public interface DynNetworkViewFactory<T> 
{
	public DynNetworkView<T> createView(DynNetwork<T> dynNetwork);
}
