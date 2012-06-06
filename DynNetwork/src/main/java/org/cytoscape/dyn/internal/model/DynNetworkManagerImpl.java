package org.cytoscape.dyn.internal.model;

import java.util.ArrayList;
import java.util.List;

import org.cytoscape.model.CyNetwork;

/**
 * <code> DynNetworkManager </code> extends <code> Sink </code> and is responsible
 * for updating <code> DynNetwork </code>.
 *  
 * @author sabina
 * 
 */
public class DynNetworkManagerImpl<T> implements DynNetworkManager<T>
{
	private List<DynNetwork<T>> dynNets;
	
	public DynNetworkManagerImpl()
	{
		dynNets = new ArrayList<DynNetwork<T>>();
	}

	@Override
	public void addDynNetwork(DynNetwork<T> dynNetwork)
	{
		this.dynNets.add(dynNetwork);
	}

	@Override
	public DynNetwork<T> getDynNetwork(CyNetwork network)
	{
		for (DynNetwork<T> dynNet : dynNets)
		{
			if (dynNet.getNetwork()==network)
				return dynNet;
		}
		return null;
	}	
	
}

