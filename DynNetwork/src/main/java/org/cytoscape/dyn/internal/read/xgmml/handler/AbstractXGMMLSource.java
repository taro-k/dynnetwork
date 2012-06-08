package org.cytoscape.dyn.internal.read.xgmml.handler;

import java.util.ArrayList;

import org.cytoscape.dyn.internal.event.Sink;
import org.cytoscape.dyn.internal.event.Source;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 * <code> AbstractXGMMLSource </code> is an abstract class to generates 
 * graph events from file parsing.
 * 
 * @author sabina
 *
 * @param <T>
 */
public abstract class AbstractXGMMLSource<T> implements Source<T>
{
	// Note: i don't implement generation of events, since xgmml reading is almost sequential 
	// and direct calling of sink methods is much faster.

	protected ArrayList<Sink<T>> sinkList = new ArrayList<Sink<T>>(2);

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
			String name, String value, String type, String start, String end)
	{
		sinkList.get(0).addedGraphAttribute(currentNetwork, name, value, type, start, end);
	}
	
	protected void addNodeAttribute(DynNetwork<T> network, CyNode currentNode, 
			String name, String value, String type, String start, String end)
	{
		sinkList.get(0).addedNodeAttribute(network, currentNode, name, value, type, start, end);
	}
	
	protected void addEdgeAttribute(DynNetwork<T> network, CyEdge currentEdge, 
			String name, String value, String type, String start, String end)
	{
		sinkList.get(0).addedEdgeAttribute(network, currentEdge, name, value, type, start, end);
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
	
	abstract protected void finalizeNetwork(DynNetwork<T> dynNetwork);
	
	abstract protected DynNetworkView<T> createView(DynNetwork<T> dynNetwork);

	@Override
	public void addSink(Sink<T> sink) 
	{
		sinkList.add(sink);
	}
	
	@Override
	public void removeSink(Sink<T> sink) 
	{
		if (sinkList.contains(sink))
			sinkList.remove(sink);
	}
	
}
