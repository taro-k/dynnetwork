package org.cytoscape.dyn.internal.model;

import java.util.Collection;

import org.cytoscape.model.CyNetwork;

/**
 * <code> DynNetworkManager </code> is the interface of the {@link DynNetwork}s
 * manager.
 * 
 * @author sabina
 *
 * @param <T>
 */
public interface DynNetworkManager<T>
{
	/**
	 * Add network.
	 * @param dynNetwork
	 */
	public void addDynNetwork(DynNetwork<T> dynNetwork);

	/**
	 * Get network.
	 * @param network
	 * @return network
	 */
	public DynNetwork<T> getDynNetwork(CyNetwork network);
	
	/**
	 * Add all networks.
	 * @return networks
	 */
	public Collection<DynNetwork<T>> getDynNetworks();
}
