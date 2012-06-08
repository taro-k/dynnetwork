package org.cytoscape.dyn.internal.model;

import java.util.Collection;

import org.cytoscape.model.CyNetwork;

public interface DynNetworkManager<T>
{
	public void addDynNetwork(DynNetwork<T> dynNetwork);

	public DynNetwork<T> getDynNetwork(CyNetwork network);
	
	public Collection<DynNetwork<T>> getDynNetworks();
}
