package org.cytoscape.dyn.internal.events;

import java.util.LinkedHashSet;
import java.util.Set;

import org.cytoscape.dyn.internal.util.DynIntervalTypeMap;
import org.cytoscape.dyn.internal.util.ObjectTypeMap;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;

/**
 * Tentative Class for managing dynamical data
 * @author sabina
 *
 * @param <T>
 */
public abstract class AbstractDynNetworkEventManager<T> {
	
	protected final static String XLINK = "http://www.w3.org/1999/xlink";
	
	protected final ObjectTypeMap typeMap;
	protected final DynIntervalTypeMap typeIntervalMap;
	
	protected final Set<CyNetwork> networks;
	
	protected final CyNetworkFactory networkFactory;
	protected final CyRootNetworkManager rootNetworkManager;
	
	public AbstractDynNetworkEventManager(final CyNetworkFactory networkFactory,
			final CyRootNetworkManager rootNetworkManager) {
		this.networkFactory = networkFactory;
		this.rootNetworkManager = rootNetworkManager;
		networks = new LinkedHashSet<CyNetwork>();
		typeMap = new ObjectTypeMap();
		typeIntervalMap = new DynIntervalTypeMap();
	}

	/**
	 * Handle graph
	 * **/
	abstract public CyNetwork addGraph(String id, String label, String start, String end, String directed);
	
	/**
	 * Handle node
	 * **/
	abstract public CyNode addNode(CyNetwork currentNetwork, CyGroup group, String id, String label, String start, String end);
	
	/**
	 * Handle edge
	 * **/
	abstract public CyEdge addEdge(CyNetwork currentNetwork, String id, String label, String source, String target, String start, String end);
	
	/**
	 * Handle group
	 * **/
	abstract public CyGroup addGroup(CyNetwork currentNetwork, CyNode currentNode);
	
	/**
	 * Handle graph attribute
	 * **/
	abstract public void addGraphAttribute(CyNetwork currentNetwork, String name, String value, String Type, String start, String end);
	
	/**
	 * Handle node attribute
	 * **/
	abstract public void addNodeAttribute(CyNetwork network, CyNode currentNode, String name, String value, String Type, String start, String end);
	
	/**
	 * Handle edge attribute
	 * **/
	abstract public void addEdgeAttribute(CyNetwork network, CyEdge currentEdge, String name, String value, String Type, String start, String end);
	
	/**
	 * Delete graph
	 * **/
	abstract public void deleteGraph(CyNetwork netwrok);

	/**
	 * Delete node
	 * **/
	abstract public void deleteNode(CyNetwork currentNetwork, CyNode node);
	
	/**
	 * Delete adge
	 * **/
	abstract public void deleteEdge(CyNetwork currentNetwork, CyEdge edge);
	
	/**
	 * Delete graph attribute
	 * **/
	abstract public void deleteGraphAttribute(CyNetwork currentNetwork, CyNetwork netwrok, String label);
	
	/**
	 * Delete node attribute
	 * **/
	abstract public void deleteNodeAttribute(CyNetwork currentNetwork, CyNode node, String label);
	
	/**
	 * Delete edge attribute
	 * **/
	abstract public void deleteEdgeAttribute(CyNetwork currentNetwork, CyEdge edge, String label);

	public Set<CyNetwork> getNetworks() {
		return networks;
	}
	
}
