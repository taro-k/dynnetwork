package org.cytoscape.dyn.internal.events;

import java.util.LinkedHashSet;
import java.util.Set;

import org.cytoscape.dyn.internal.util.ObjectTypeMap;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;

public abstract class AbstractDynNetworkEventManager {
	
	protected final static String XLINK = "http://www.w3.org/1999/xlink";

	protected final ObjectTypeMap typeMap;
	
	protected final Set<CyNetwork> networks;
	
	protected CyRootNetwork parentNetwork;
	protected CyNetwork currentNetwork;
	protected CyNode currentNode;
	protected CyEdge currentEdge;
	protected CyRow currentRow;
	
	protected final CyNetworkFactory networkFactory;
	protected final CyRootNetworkManager rootNetworkManager;
	
	public AbstractDynNetworkEventManager(final CyNetworkFactory networkFactory,
			final CyRootNetworkManager rootNetworkManager) {
		this.networkFactory = networkFactory;
		this.rootNetworkManager = rootNetworkManager;
		networks = new LinkedHashSet<CyNetwork>();
		typeMap = new ObjectTypeMap();
	}

	abstract public void addGraph(String id, String label, String start, String end);
	
	abstract public void addNode(String id, String label, String start, String end);
	
	abstract public void addEdge(String id, String label, String source, String target, String start, String end);
	
	abstract public void addGraphAttribute(String name, String value, String Type, String start, String end);
	
	abstract public void addNodeAttribute(String name, String value, String Type, String start, String end);
	
	abstract public void addEdgeAttribute(String name, String value, String Type, String start, String end);
	
	abstract public void deleteGraph(CyNetwork netwrok);
	
	abstract public void deleteNode(CyNode node);
	
	abstract public void deleteEdge(CyEdge edge);
	
	abstract public void deleteGraphAttribute(CyNetwork netwrok, String label);
	
	abstract public void deleteNodeAttribute(CyNode node, String label);
	
	abstract public void deleteEdgeAttribute(CyEdge edge, String label);

	public Set<CyNetwork> getNetworks() {
		return networks;
	}

	public CyRootNetwork getParentNetwork() {
		return parentNetwork;
	}

	public void setParentNetwork(CyRootNetwork parentNetwork) {
		this.parentNetwork = parentNetwork;
	}

	public CyNetwork getCurrentNetwork() {
		return currentNetwork;
	}

	public void setCurrentNetwork(CyNetwork currentNetwork) {
		this.currentNetwork = currentNetwork;
	}

	public CyNode getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(CyNode currentNode) {
		this.currentNode = currentNode;
	}

	public CyEdge getCurrentEdge() {
		return currentEdge;
	}

	public void setCurrentEdge(CyEdge currentEdge) {
		this.currentEdge = currentEdge;
	}

	public CyRow getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(CyRow currentRow) {
		this.currentRow = currentRow;
	}
	
	
}
