package org.cytoscape.dyn.internal.view.model;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;

public final class DynNetworkViewFactoryImpl<T> implements DynNetworkViewFactory<T>
{
	private final DynNetworkViewManager<T> viewManager;
	private final CyNetworkViewFactory cyNetworkViewFactory;
	private final CyNetworkViewManager networkViewManager;
	private final VisualMappingManager vmm;
	
	public DynNetworkViewFactoryImpl(
			DynNetworkViewManager<T> viewManager,
			CyNetworkViewFactory cyNetworkViewFactory,
			final CyNetworkViewManager networkViewManager,
			final VisualMappingManager vmm)
	{
		this.viewManager = viewManager;
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		this.networkViewManager = networkViewManager;
		this.vmm = vmm;
	}
	
	@Override
	public DynNetworkView<T> createView(DynNetwork<T> dynNetwork)
	{
		DynNetworkViewImpl<T> dynNetworkView = new DynNetworkViewImpl<T>(dynNetwork, networkViewManager,cyNetworkViewFactory,vmm);
		viewManager.addDynNetworkView(dynNetwork, dynNetworkView);
		return dynNetworkView;
	}

	@Override
	public CyEdge addedEdge(DynNetwork<T> dynNetwork, String id, String label,
			String source, String target, String start, String end) 
	{
		return null;
	}

	@Override
	public void addedEdgeAttribute(DynNetwork<T> dynNetwork,
			CyEdge currentEdge, String name, String value, String Type,
			String start, String end) 
	{
		
	}

	@Override
	public DynNetwork<T> addedGraph(String id, String label, String start,
			String end, String directed) 
			{
		return null;
	}

	@Override
	public void addedGraphAttribute(DynNetwork<T> dynNetwork, String name,
			String value, String Type, String start, String end) 
	{

	}

	@Override
	public CyGroup addedGroup(DynNetwork<T> dynNetwork, CyNode currentNode) 
	{
		return null;
	}

	@Override
	public CyNode addedNode(DynNetwork<T> dynNetwork, CyGroup group, String id,
			String label, String start, String end) 
	{
		return null;
	}

	@Override
	public void addedNodeAttribute(DynNetwork<T> dynNetwork,
			CyNode currentNode, String name, String value, String Type,
			String start, String end) 
	{

	}

	@Override
	public void deletedEdge(DynNetwork<T> dynNetwork, CyEdge edge) 
	{

	}

	@Override
	public void deletedGraph(DynNetwork<T> dynNetwork) 
	{

	}

	@Override
	public void deletedNode(DynNetwork<T> dynNetwork, CyNode node) 
	{

	}
	
	@Override
	public void finalizeNetwork(DynNetwork<T> dynNetwork) 
	{

	}
	


}
