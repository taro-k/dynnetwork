package org.cytoscape.dyn.internal.model;

import org.cytoscape.dyn.internal.model.attributes.DynAttribute;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

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
	public DynAttribute<T> getDynAtribute(CyNetwork network)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynAttribute<T> getDynAtribute(CyNode node)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynAttribute<T> getDynAtribute(CyEdge ede)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynAttribute<T> getDynAtribute(CyNetwork network, String attName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynAttribute<T> getDynAtribute(CyNode node, String attName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynAttribute<T> getDynAtribute(CyEdge ede, String attName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(CyNetwork network, DynInterval<T> interval)
	{
		intervalTree.insert(interval);
	}

	@Override
	public void insert(CyNode node, DynInterval<T> interval)
	{
		intervalTree.insert(interval);
	}

	@Override
	public void insert(CyEdge ede, DynInterval<T> interval)
	{
		intervalTree.insert(interval);
	}

	@Override
	public void remove(CyNetwork network, DynInterval<T> interval)
	{
		intervalTree.remove(interval);
	}

	@Override
	public void remove(CyNode node, DynInterval<T> interval)
	{
		intervalTree.remove(interval);
	}

	@Override
	public void remove(CyEdge ede, DynInterval<T> interval)
	{
		intervalTree.remove(interval);
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
	public void print()
	{
		intervalTree.print(intervalTree.getRoot());
	}


}
