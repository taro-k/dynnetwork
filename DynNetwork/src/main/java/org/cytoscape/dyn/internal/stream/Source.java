package org.cytoscape.dyn.internal.stream;

import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 * <code> Source </code> produces graph events (elements and attributes), but does not
 * contain a graph instance.
 *  
 * @author sabina
 * 
 */
public interface Source
{
	void addSink(Sink sink);
	
	void removeSink(Sink sink);
	
	CyNetwork addGraph(String id, String label, String start, String end, String directed);
	
	CyNode addNode(CyNetwork currentNetwork, CyGroup group, String id, String label, String start, String end);
	
	CyEdge addEdge(CyNetwork currentNetwork, String id, String label, String source, String target, String start, String end);
	
	CyGroup addGroup(CyNetwork currentNetwork, CyNode currentNode);
	
	void addGraphAttribute(CyNetwork currentNetwork, String name, String value, String Type, String start, String end);
	
	void addNodeAttribute(CyNetwork network, CyNode currentNode, String name, String value, String Type, String start, String end);
	
	void addEdgeAttribute(CyNetwork network, CyEdge currentEdge, String name, String value, String Type, String start, String end);
	
	void deleteGraph(CyNetwork netwrok);

	void deleteNode(CyNetwork currentNetwork, CyNode node);
	
	void deleteEdge(CyNetwork currentNetwork, CyEdge edge);
	
	void deleteGraphAttribute(CyNetwork currentNetwork, CyNetwork netwrok, String label);
	
	void deleteNodeAttribute(CyNetwork currentNetwork, CyNode node, String label);
	
	void deleteEdgeAttribute(CyNetwork currentNetwork, CyEdge edge, String label);
	
}
