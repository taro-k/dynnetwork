package org.cytoscape.dyn.internal.stream;

import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 * <code> Sink </code> receives graph events (elements and attributes), but does not
 * contain a graph instance. Graph listeners are called each time a graph element (node
 * or edge) or attributed is added, changed or removed.
 *  
 * @author sabina
 * 
 */
public interface Sink
{
	CyNetwork addedGraph(String id, String label, String start, String end, String directed);
	
	CyNode addedNode(CyNetwork currentNetwork, CyGroup group, String id, String label, String start, String end);
	
	CyEdge addedEdge(CyNetwork currentNetwork, String id, String label, String source, String target, String start, String end);
	
	CyGroup addedGroup(CyNetwork currentNetwork, CyNode currentNode);
	
	void addedGraphAttribute(CyNetwork currentNetwork, String name, String value, String Type, String start, String end);
	
	void addedNodeAttribute(CyNetwork network, CyNode currentNode, String name, String value, String Type, String start, String end);
	
	void addedEdgeAttribute(CyNetwork network, CyEdge currentEdge, String name, String value, String Type, String start, String end);
	
	void deletedGraph(CyNetwork netwrok);

	void deletedNode(CyNetwork currentNetwork, CyNode node);
	
	void deletedEdge(CyNetwork currentNetwork, CyEdge edge);
	
	void deletedGraphAttribute(CyNetwork currentNetwork, CyNetwork netwrok, String label);
	
	void deletedNodeAttribute(CyNetwork currentNetwork, CyNode node, String label);
	
	void deletedEdgeAttribute(CyNetwork currentNetwork, CyEdge edge, String label);
}
