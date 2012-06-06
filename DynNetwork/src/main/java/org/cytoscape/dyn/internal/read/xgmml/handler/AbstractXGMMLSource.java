package org.cytoscape.dyn.internal.read.xgmml.handler;

import java.util.ArrayList;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.DynNetworkFactory;
import org.cytoscape.dyn.internal.stream.Sink;
import org.cytoscape.dyn.internal.stream.Source;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

public abstract class AbstractXGMMLSource<T> implements Source
{
	// TODO: in the future we can easely implement events and listeners. 
	//       for the moment I keep it like this since it's faster.
	
	//FIXME: iterate over list
	
	protected ArrayList<DynNetworkFactory<T>> sinkList = new ArrayList<DynNetworkFactory<T>>();
	
	protected DynNetwork<T> addGraph(
			String id, String label, String start, String end, String directed)
	{
		return sinkList.get(0).addedGraph(id, label, start, end, directed);
	}
	
	protected CyNode addNode(DynNetwork<T> currentNetwork, CyGroup group, 
			String id, String label, String start, String end)
	{
		return sinkList.get(0).addedNode(currentNetwork, group, id, label, start, end);
	}
	
	protected CyEdge addEdge(DynNetwork<T> currentNetwork, 
			String id, String label, String source, String target, String start, String end)
	{
		return sinkList.get(0).addedEdge(currentNetwork, id, label, source, target, start, end);
	}
	
	protected CyGroup addGroup(DynNetwork<T> currentNetwork, CyNode currentNode)
	{
		return sinkList.get(0).addedGroup(currentNetwork, currentNode);
	}
	
	protected void addGraphAttribute(DynNetwork<T> currentNetwork, 
			String name, String value, String Type, String start, String end)
	{
		sinkList.get(0).addedGraphAttribute(currentNetwork, name, value, Type, start, end);
	}
	
	protected void addNodeAttribute(DynNetwork<T> network, CyNode currentNode, 
			String name, String value, String Type, String start, String end)
	{
		sinkList.get(0).addedNodeAttribute(network, currentNode, name, value, Type, start, end);
	}
	
	protected void addEdgeAttribute(DynNetwork<T> network, CyEdge currentEdge, 
			String name, String value, String Type, String start, String end)
	{
		sinkList.get(0).addedEdgeAttribute(network, currentEdge, name, value, Type, start, end);
	}
	
	protected void deleteGraph(DynNetwork<T> netwrok)
	{
		sinkList.get(0).deletedGraph(netwrok);
	}

	protected void deleteNode(DynNetwork<T> currentNetwork, CyNode node)
	{
		sinkList.get(0).deletedNode(currentNetwork, node);
	}
	
	protected void deleteEdge(DynNetwork<T> currentNetwork, CyEdge edge)
	{
		sinkList.get(0).deletedEdge(currentNetwork, edge);
	}
	
	protected void deleteGraphAttribute(DynNetwork<T> currentNetwork, CyNetwork netwrok, String label)
	{
//		sinkList.get(0).deletedGraphAttribute(currentNetwork, netwrok, label);
	}
	
	protected void deleteNodeAttribute(DynNetwork<T> currentNetwork, CyNode node, String label)
	{
//		sinkList.get(0).deletedNodeAttribute(currentNetwork, node, label);
	}
	
	protected void deleteEdgeAttribute(DynNetwork<T> currentNetwork, CyEdge edge, String label)
	{
//		sinkList.get(0).deletedEdgeAttribute(currentNetwork, edge, label);
	}
	
	protected void finalize(DynNetwork<T> currentNetwork)
	{
		sinkList.get(0).finalize(currentNetwork);
	}

	@Override
	public void addSink(Sink sink) {}
	
	public void addSink(DynNetworkFactory<T> sink)
	{
		this.sinkList.add((DynNetworkFactory<T>) sink);
	}

	@Override
	public void removeSink(Sink sink)
	{
		if (this.sinkList.contains(sink))
			this.sinkList.remove(sink);
	}
}
