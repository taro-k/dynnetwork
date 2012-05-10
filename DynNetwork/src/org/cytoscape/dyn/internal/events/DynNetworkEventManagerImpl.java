package org.cytoscape.dyn.internal.events;

import java.util.HashMap;
import java.util.Map;

import org.cytoscape.dyn.internal.attributes.DynData;
import org.cytoscape.dyn.internal.attributes.DynInterval;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;

//TODO: implement listeners to handle graph events
//TODO: implement event filter, for the moment we do filtering in the handler
//TODO: find a place to store the dynamic information, not here!

/**
 * Tentative Class for managing dynamical data
 * @author sabina
 *
 * @param <T>
 */
public class DynNetworkEventManagerImpl<T> extends AbstractDynNetworkEventManager{
	
	private final Map<String, Long> cyNodes;
	private final Map<String, Long> cyEdges;

	private DynData<T> dynGraphsAttr;
	private DynData<T> dynNodesAttr;
	private DynData<T> dynEdgesAttr;
	
	private boolean isDirected = true;
	
	public DynNetworkEventManagerImpl(CyNetworkFactory networkFactory,
			CyRootNetworkManager rootNetworkManager) {
		super(networkFactory, rootNetworkManager);
		
		cyNodes = new HashMap<String, Long>();
		cyEdges = new HashMap<String, Long>();
		dynGraphsAttr = new DynData<T>();
		dynNodesAttr = new DynData<T>();
		dynEdgesAttr = new DynData<T>();
	}
	
	/**Handle graphs**/

	public void addGraph(String id, String label, String start, String end)
	{
		if (parentNetwork == null)
			parentNetwork = this.createRootNetwork();
		
		currentNetwork = parentNetwork.addSubNetwork();
		setElement(currentNetwork, id, label, null, start, end);
	}
	
	/**
	 * Handle nodes
	 * **/

	public void addNode(String id, String label, String start, String end)
	{
		if (!cyNodes.containsKey(id)){
			currentNode = this.currentNetwork.addNode();
		}
		setElement(currentNode, id, label, null, start, end);
	}
	
	/**
	 * Handle edges
	 * **/
	
	public void addEdge(String id, String label, String source, String target, String start, String end)
	{
		if (!cyEdges.containsKey(id))
			currentEdge = createEdge(id, label, source, target, start, end);
		setElement(currentEdge, id, label, null, start, end);
	}
	
	/**
	 * Handle graph attributes
	 * **/

	public void addGraphAttribute(String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(currentNetwork, attName, attValue, attType, start, end);
	}
	
	/**
	 * Handle node attributes
	 * **/
	
	public void addNodeAttribute(String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(currentNode, attName, attValue, attType, start, end);
	}
	
	/**
	 * Handle edge attributes
	 * **/
	
	public void addEdgeAttribute(String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(currentEdge, attName, attValue, attType, start, end);
	}

	@Override
	public void deleteEdge(CyEdge edge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteEdgeAttribute(CyEdge edge, String label) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGraph(CyNetwork netwrok) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGraphAttribute(CyNetwork netwrok, String label) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteNode(CyNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteNodeAttribute(CyNode node, String label) {
		// TODO Auto-generated method stub
	}
	
	private void setElement(CyIdentifiable ci, String id, String label, String value, String start, String end) {
		
		if (ci instanceof CyNetwork)
		{
			currentNetwork.getRow(ci).set(CyNetwork.NAME, label);
			dynGraphsAttr.add(getInterval(null,start,end), "none", ci.getSUID(), CyNetwork.NAME);
			networks.add((CyNetwork) ci);
		}
		else if (ci instanceof CyNode)
		{
			currentNetwork.getRow(ci).set(CyNetwork.NAME, label);
			dynNodesAttr.add(getInterval(null,start,end), "none", ci.getSUID(), CyNetwork.NAME);
			cyNodes.put(id, ci.getSUID());
		}
		else if (ci instanceof CyEdge)
		{
			currentNetwork.getRow(ci).set(CyNetwork.NAME, label);
			dynEdgesAttr.add(getInterval(null,start,end), "none", ci.getSUID(), CyNetwork.NAME);
			cyEdges.put(id, ci.getSUID());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setAttributes(CyIdentifiable ci, String attName, String attValue, String attType, String start, String end) {
		
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		
		if (ci instanceof CyNetwork)
		{
			addRow(currentNetwork.getDefaultNetworkTable(), currentNetwork, attName, attr);
			dynGraphsAttr.add(getInterval((T)attr,start,end), attType, currentNetwork.getSUID(), attName);
		}
		else if (ci instanceof CyNode)
		{
			addRow(currentNetwork.getDefaultNodeTable(), currentNode, attName, attr);
			dynNodesAttr.add(getInterval((T)attr,start,end), attType, currentNode.getSUID(), attName);
		}
		else if (ci instanceof CyEdge)
		{
			addRow(currentNetwork.getDefaultEdgeTable(), currentEdge, attName, attr);
			dynEdgesAttr.add(getInterval((T)attr,start,end), attType, currentEdge.getSUID(), attName);
		}
	}


	private void addRow(CyTable table, CyIdentifiable ci, String attName, Object attr)
	{
		if (table.getColumn(attName)==null)
			table.createColumn(attName, attr.getClass(), false);
		currentNetwork.getRow(ci).set(attName, attr);
	}
	
	private CyRootNetwork createRootNetwork() {
		final CyNetwork baseNet = networkFactory.createNetwork();
		final CyRootNetwork rootNetwork = rootNetworkManager.getRootNetwork(baseNet);
		return rootNetwork;
	}
	
	private CyEdge createEdge(String id, String label, String source, String target, String start, String end)
	{
		CyEdge edge = null;
		CyNode nodeSource = getNode(source);
		CyNode nodeTarget = getNode(target);
		if (nodeSource!= null && nodeTarget!=null)
		{
			edge = this.currentNetwork.addEdge(nodeSource, nodeTarget, isDirected);
		}
		return edge;
	}
	
	private CyNode getNode(String label) {
		if (cyNodes.containsKey(label))
			return currentNetwork.getNode(cyNodes.get(label));
		return null;
	}

	private DynInterval<T> getInterval(T value, String start, String end)
	{
		DynInterval<T> interval;
		if (value!=null && start!=null && end!=null)
			interval = new DynInterval<T>(value, Double.parseDouble(start), Double.parseDouble(end));
		else if (value!=null && start!=null)
			interval = new DynInterval<T>(value, Double.parseDouble(start));
		else if (value!=null)
			interval = new DynInterval<T>(value);
		else if (start!=null && end!=null)
			interval = new DynInterval<T>(Double.parseDouble(start), Double.parseDouble(end));
		else if (start!=null)
			interval = new DynInterval<T>(Double.parseDouble(start));
		else
			interval = new DynInterval<T>();
		return interval;
	}
	
	
	public boolean checkGraph(CyNetwork c, DynInterval<T> interval) {
		return dynGraphsAttr.isInRange(interval, c.getSUID(), CyNetwork.NAME);
	}

	public boolean checkNode(CyNode n, DynInterval<T> interval) {
		return dynNodesAttr.isInRange(interval, n.getSUID(), CyNetwork.NAME);
	}

	public boolean checkEdge(CyEdge e, DynInterval<T> interval) {
		return dynEdgesAttr.isInRange(interval, e.getSUID(), CyNetwork.NAME);
	}

	public T getDynGraphsAttr(CyNetwork c, String attName, DynInterval<T> interval) {
		return dynGraphsAttr.getValue(interval, c.getSUID(), attName);
	}

	public T getDynNodesAttr(CyNode n, String attName, DynInterval<T> interval) {
		return dynNodesAttr.getValue(interval, n.getSUID(), attName);
	}

	public T getDynEdgesAttr(CyEdge e, String attName, DynInterval<T> interval) {
		return dynEdgesAttr.getValue(interval, e.getSUID(), attName);
	}

	public DynData<T> getDynGraphsAttr() {
		return dynGraphsAttr;
	}

	public DynData<T> getDynNodesAttr() {
		return dynNodesAttr;
	}

	public DynData<T> getDynEdgesAttr() {
		return dynEdgesAttr;
	}
	
	
	
	
}
