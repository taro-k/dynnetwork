package org.cytoscape.dyn.internal.model;

import org.cytoscape.dyn.internal.stream.Sink;
import org.cytoscape.model.CyNetwork;

public interface DynNetworkManager<T> extends Sink
{
	public void addDynNetwork(DynNetwork<T> dynNetwork);

	public DynNetwork<T> getDynNetwork(CyNetwork network);
}
