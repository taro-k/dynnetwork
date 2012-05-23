package org.cytoscape.dyn.internal.events;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetwork;

/**
 * This interface provides an handler for dynamic updates of CyNetwork.
 * @author sabina
 *
 */
public interface DynNetworkEventManager {
	
	void addGraph(String id, String label);
	
	void addNode(String id, String label);
	
	void addEdge(String id, String label, String source, String target);
	
	void addGraphAttribute(String name, String value, String Type);
	
	void addNodeAttribute(String name, String value, String Type);
	
	void addEdgeAttribute(String name, String value, String Type);
	
	void deleteGraph(CyNetwork netwrok);
	
	void deleteNode(CyNode node);
	
	void deleteEdge(CyEdge edge);
	
	void deleteGraphAttribute(CyNetwork netwrok, String label);
	
	void deleteNodeAttribute(CyNode node, String label);
	
	void deleteEdgeAttribute(CyEdge edge, String label);

	void setParentNetwork(CyRootNetwork parentNetwork);

	void setCurrentNetwork(CyNetwork currentNetwork);

	void setCurrentNode(CyNode currentNode);

	void setCurrentEdge(CyEdge currentEdge);


}
