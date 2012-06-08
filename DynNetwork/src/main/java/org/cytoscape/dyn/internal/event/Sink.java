package org.cytoscape.dyn.internal.event;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;


/**
 * <code> Sink </code> receives graph events (elements and attributes), but does not
 * contain a graph instance. Graph listeners are called each time a graph element (node
 * or edge) or attributed is added, changed or removed.
 *  
 * @author sabina
 * 
 */
public interface Sink<T>
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
	
	public void finalizeNetwork(DynNetwork<T> dynNetwork);
	
	public DynNetworkView<T> createView(DynNetwork<T> dynNetwork);
}
