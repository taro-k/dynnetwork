package org.cytoscape.dyn.internal.read.xgmml.handler;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.cytoscape.dyn.internal.event.CreateEdgeAttrDynEvent;
import org.cytoscape.dyn.internal.event.CreateGraphAttrDynEvent;
import org.cytoscape.dyn.internal.event.CreateNodeAttrDynEvent;
import org.cytoscape.dyn.internal.event.Sink;
import org.cytoscape.dyn.internal.event.Source;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

public abstract class AbstractXGMMLSource<T> implements Source<T>
{
	// Note: for events that require an object reference, the methods in the sink
	// interface are called directly, since we have anyway to wait until the object is created
	// and returned. This is true for the creation of graphs, nodes, and edges. For the creation
	// of attributes we can generate events to be processes in another thread, since we don't
	// have to wait for them to finish.

	protected ArrayList<Sink<T>> sinkList = new ArrayList<Sink<T>>(2);
	
	final ExecutorService producers = Executors.newFixedThreadPool(10);
	final ExecutorService consumers = Executors.newFixedThreadPool(10);

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
		producers.submit(new CreateGraphAttrDynEvent<T>(currentNetwork, name, value, type, start, end, sinkList.get(0)));
//		sinkList.get(0).addedGraphAttribute(currentNetwork, name, value, type, start, end);
	}
	
	protected void addNodeAttribute(DynNetwork<T> network, CyNode currentNode, 
			String name, String value, String type, String start, String end)
	{
		producers.submit(new CreateNodeAttrDynEvent<T>(network, currentNode, name, value, type, start, end, sinkList.get(0)));
//		sinkList.get(0).addedNodeAttribute(network, currentNode, name, value, type, start, end);
	}
	
	protected void addEdgeAttribute(DynNetwork<T> network, CyEdge currentEdge, 
			String name, String value, String type, String start, String end)
	{
		producers.submit(new CreateEdgeAttrDynEvent<T>(network, currentEdge, name, value, type, start, end, sinkList.get(0)));
//		sinkList.get(0).addedEdgeAttribute(network, currentEdge, name, value, type, start, end);
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
	
	protected void finalizeNetwork(DynNetwork<T> dynNetwork) throws InterruptedException
	{
		producers.shutdown();
		producers.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		consumers.shutdown();
		consumers.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);

//		sinkList.get(0).finalizeNetwork(dynNetwork);
	}
	
	protected DynNetworkView<T> createView(DynNetwork<T> dynNetwork) throws InterruptedException
	{
		return null;
	}

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
