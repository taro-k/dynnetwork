package org.cytoscape.dyn.internal.model;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;

/**
 * <code> DynNetworkManager </code> extends <code> Sink </code> and is responsible
 * for updating <code> DynNetwork </code>.
 *  
 * @author sabina
 * 
 */
public final class DynNetworkManagerImpl<T> implements DynNetworkManager<T>
{
	private final CyNetworkManager cyNetworkManager;
	private final Map<CyNetwork, DynNetwork<T>> dynNetworkMap;
	
	public DynNetworkManagerImpl(CyNetworkManager cyNetworkManager)
	{
		this.cyNetworkManager = cyNetworkManager;
		this.dynNetworkMap = new WeakHashMap<CyNetwork, DynNetwork<T>>();
	}

	@Override
	public void addDynNetwork(DynNetwork<T> dynNetwork)
	{
		this.cyNetworkManager.addNetwork(dynNetwork.getNetwork());
		this.dynNetworkMap.put(dynNetwork.getNetwork(), dynNetwork);
	}

	@Override
	public DynNetwork<T> getDynNetwork(CyNetwork network)
	{
		return dynNetworkMap.get(network);
	}
	
	@Override
	public Collection<DynNetwork<T>> getDynNetworks() 
	{
		return dynNetworkMap.values();
	}
	
}

