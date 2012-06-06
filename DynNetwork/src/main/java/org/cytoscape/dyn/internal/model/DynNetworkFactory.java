package org.cytoscape.dyn.internal.model;

import org.cytoscape.dyn.internal.stream.Sink;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;


/**
 * <code> DynIntervalTreeFactory </code> is a singleton factory object used for instantiating 
 * {@link DynNetwork} objects. The <code> DynIntervalTreeFactory </code> should be available 
 * as an OSGi service.
 */
public interface DynNetworkFactory<T> extends Sink
{
	public DynNetwork<T> addedGraph(String id, String label, String start, String end, String directed);
	
	public CyNode addedNode(DynNetwork<T> dynNetwork, CyGroup group, String id, String label, String start, String end);
	
	public CyEdge addedEdge(DynNetwork<T> dynNetwork, String id, String label, String source, String target, String start, String end);
	
	public CyGroup addedGroup(DynNetwork<T> dynNetwork, CyNode currentNode);
	
	public void addedGraphAttribute(DynNetwork<T> dynNetwork, String name, String value, String Type, String start, String end);
	
	public void addedNodeAttribute(DynNetwork<T> dynNetwork, CyNode currentNode, String name, String value, String Type, String start, String end);
	
	public void addedEdgeAttribute(DynNetwork<T> dynNetwork, CyEdge currentEdge, String name, String value, String Type, String start, String end);
	
	public void deletedGraph(DynNetwork<T> dynNetwork);

	public void deletedNode(DynNetwork<T> dynNetwork, CyNode node);
	
	public void deletedEdge(DynNetwork<T> dynNetwork, CyEdge edge);
	
	public void finalize(DynNetwork<T> dynNetwork);
}
