package org.cytoscape.dyn.internal.action;

import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 * <code> DynNetworkEventManager </code> is an interface which provides to methods for dynamic 
 * updates of CyNetwork.
 * 
 * @author sabina
 *
 */
public interface DynNetworkEventManager {
	
	/**
	 * Add graph
	 * @param id
	 * @param label
	 * @param start
	 * @param end
	 * @param directed
	 * @return
	 */
	CyNetwork addGraph(String id, String label, String start, String end, String directed);
	
	/**
	 * Add node to current network.
	 * @param currentNetwork
	 * @param group
	 * @param id
	 * @param label
	 * @param start
	 * @param end
	 * @return
	 */
	CyNode addNode(CyNetwork currentNetwork, CyGroup group, String id, String label, String start, String end);
	
	/**
	 * Add edge to current network.
	 * @param currentNetwork
	 * @param id
	 * @param label
	 * @param source
	 * @param target
	 * @param start
	 * @param end
	 * @return
	 */
	CyEdge addEdge(CyNetwork currentNetwork, String id, String label, String source, String target, String start, String end);
	
	/**
	 * Add group to current network.
	 * @param currentNetwork
	 * @param currentNode
	 * @return
	 */
	CyGroup addGroup(CyNetwork currentNetwork, CyNode currentNode);
	
	/**
	 * Add graph attribute to current network.
	 * @param currentNetwork
	 * @param name
	 * @param value
	 * @param Type
	 * @param start
	 * @param end
	 */
	void addGraphAttribute(CyNetwork currentNetwork, String name, String value, String Type, String start, String end);
	
	/**
	 * Add node attribute to current node in current network.
	 * @param network
	 * @param currentNode
	 * @param name
	 * @param value
	 * @param Type
	 * @param start
	 * @param end
	 */
	void addNodeAttribute(CyNetwork network, CyNode currentNode, String name, String value, String Type, String start, String end);
	
	/**
	 * Add edge attribute to current edge in current network.
	 * @param network
	 * @param currentEdge
	 * @param name
	 * @param value
	 * @param Type
	 * @param start
	 * @param end
	 */
	void addEdgeAttribute(CyNetwork network, CyEdge currentEdge, String name, String value, String Type, String start, String end);
	
	/**
	 * Delete graph from current network.
	 * @param netwrok
	 */
	void deleteGraph(CyNetwork netwrok);

	/**
	 * Delete node from current network.
	 * @param currentNetwork
	 * @param node
	 */
	void deleteNode(CyNetwork currentNetwork, CyNode node);
	
	/**
	 * Delete edge from current network.
	 * @param currentNetwork
	 * @param edge
	 */
	void deleteEdge(CyNetwork currentNetwork, CyEdge edge);
	
	/**
	 * Delete graph attribute from current network.
	 * @param currentNetwork
	 * @param netwrok
	 * @param label
	 */
	void deleteGraphAttribute(CyNetwork currentNetwork, CyNetwork netwrok, String label);
	
	/**
	 * Delete node attribute from current network.
	 * @param currentNetwork
	 * @param node
	 * @param label
	 */
	void deleteNodeAttribute(CyNetwork currentNetwork, CyNode node, String label);
	
	/**
	 * Delete edge attribute from current network.
	 * @param currentNetwork
	 * @param edge
	 * @param label
	 */
	void deleteEdgeAttribute(CyNetwork currentNetwork, CyEdge edge, String label);


}
