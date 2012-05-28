package org.cytoscape.dyn.internal.model;

import org.cytoscape.model.CyNetwork;

public final class DynNetworkImpl<T> implements DynNetwork<T>
{	
	private final CyNetwork network;
	private final DynIntervalTreeImpl<T> intervalTree;
	
	public DynNetworkImpl(CyNetwork network)
	{
		this.network = network;
		this.intervalTree = new DynIntervalTreeImpl<T>();
	}

	@Override
	public DynIntervalTree<T> getIntervalTree()
	{
		return intervalTree;
	}

	@Override
	public CyNetwork getNetwork()
	{
		return network;
	}

	@Override
	public void insert(DynInterval<T> interval)
	{
		intervalTree.insert(interval);
	}

	@Override
	public void remove(DynInterval<T> interval)
	{
		intervalTree.remove(interval);
		
	}

	@Override
	public void print()
	{
		intervalTree.print(intervalTree.getRoot());
	}


}
