package org.cytoscape.dyn.internal.model;

import org.cytoscape.model.CyNetwork;


public final class DynNetworkFactoryImpl<T> implements DynNetworkFactory<T>
{
	@Override
	public DynNetwork<T> createDynNetwork(CyNetwork network)
	{
		return new DynNetworkImpl<T>(network);
	}

}
