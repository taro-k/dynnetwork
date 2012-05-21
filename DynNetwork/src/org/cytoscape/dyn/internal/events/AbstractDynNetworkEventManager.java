package org.cytoscape.dyn.internal.events;

import java.util.LinkedHashSet;
import java.util.Set;

import org.cytoscape.dyn.internal.util.DynIntervalTypeMap;
import org.cytoscape.dyn.internal.util.ObjectTypeMap;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;

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

	abstract public void addGraph(String id, String label, String start, String end, String directed);
	
	abstract public void addNode(String id, String label, String start, String end);
	
	abstract public void addMetaNode(String id, String label, String start, String end);
	
	abstract public void addEdge(String id, String label, String source, String target, String start, String end);
	
	abstract public void addGraphAttribute(String name, String value, String Type, String start, String end);
	
	abstract public void addNodeAttribute(String name, String value, String Type, String start, String end);
	
	abstract public void addEdgeAttribute(String name, String value, String Type, String start, String end);
	
	abstract public void deleteGraph(CyNetwork netwrok);
	
	abstract public void deleteSubGraph(CyNetwork netwrok);
	
	abstract public void deleteNode(CyNode node);
	
	abstract public void deleteEdge(CyEdge edge);
	
	abstract public void deleteGraphAttribute(CyNetwork netwrok, String label);
	
	abstract public void deleteNodeAttribute(CyNode node, String label);
	
	abstract public void deleteEdgeAttribute(CyEdge edge, String label);

	public Set<CyNetwork> getNetworks() {
		return networks;
	}
	
}
