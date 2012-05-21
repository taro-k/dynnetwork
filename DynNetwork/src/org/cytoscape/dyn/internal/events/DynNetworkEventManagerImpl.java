package org.cytoscape.dyn.internal.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.cytoscape.dyn.internal.model.DynAttribute;
import org.cytoscape.dyn.internal.model.DynData;
import org.cytoscape.dyn.internal.model.DynInterval;
import org.cytoscape.group.CyGroup;
import org.cytoscape.group.CyGroupFactory;
import org.cytoscape.group.CyGroupManager;
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
//TODO: and should be thread safe (currentnode, currentedge, etc. are NOT!)!

/**
 * Tentative Class for managing dynamical data
 * @author sabina
 *
 * @param <T>
 */
public class DynNetworkEventManagerImpl<T> extends AbstractDynNetworkEventManager<T>
{
	private final Map<String, Long> cyNodes;
	private final Map<String, Long> cyEdges;

	private final DynData<T> dynGraphsAttr;
	private final DynData<T> dynNodesAttr;
	private final DynData<T> dynEdgesAttr;
	
	private final Stack<CyGroup> groupNodeStack;
	
	private final CyGroupManager groupManager;
	private final CyGroupFactory groupFactory;
	
	protected CyRootNetwork parentNetwork;
	protected CyNetwork currentNetwork;
	protected CyGroup currentGroup;
	protected CyNode currentNode;
	protected CyEdge currentEdge;
	
	private boolean isDirected = true;
	
	public DynNetworkEventManagerImpl(
			CyNetworkFactory networkFactory,
			CyRootNetworkManager rootNetworkManager,
			CyGroupManager groupManager,
			CyGroupFactory groupFactory)
	{
		super(networkFactory, rootNetworkManager);
		this.groupManager = groupManager;
		this.groupFactory = groupFactory;
		cyNodes = new HashMap<String, Long>();
		cyEdges = new HashMap<String, Long>();
		dynGraphsAttr = new DynData<T>();
		dynNodesAttr = new DynData<T>();
		dynEdgesAttr = new DynData<T>();
		groupNodeStack = new Stack<CyGroup>();
	}
	
	/**
	 * Handle graphs
	 * **/
	@Override
	public void addGraph(String id, String label, String start, String end, String directed)
	{
		parentNetwork = createRootNetwork(directed);
		currentNetwork = createGraph(parentNetwork, id, label, start, end);
		setElement(currentNetwork, id, label, null, start, end);
	}

	/**
	 * Handle nodes
	 * **/
	@Override
	public void addNode(String id, String label, String start, String end)
	{
		currentNode = createNode(currentNetwork, id, label, start, end);
		setElement(currentNode, id, label, null, start, end);
	}
	
	/**
	 * Handle meta-nodes
	 * **/
	@Override
	public void addMetaNode(String id, String label, String start, String end)
	{
		currentGroup = groupFactory.createGroup(currentNetwork, currentNode, true);
		groupNodeStack.push(currentGroup);
		groupManager.addGroup(currentGroup);
	}
	
	/**
	 * Handle edges
	 * **/
	@Override
	public void addEdge(String id, String label, String source, String target, String start, String end)
	{
		if(cyNodes.containsKey(source) && cyNodes.containsKey(target))
		{
			currentEdge = createEdge(currentNetwork, id, label, source, target, start, end);
			setElement(currentEdge, id, label, null, start, end);
		}
		else
			System.out.println("XGMML Parser Warning: edge " + id==null?label:id + " was ignored (unidentified nodes)!");
	}
	
	public void addEdgeToGroup(String id, String label, String source, String target, String start, String end)
	{
		currentEdge = createEdge(currentNetwork, id, label, source, target, start, end);
		setElement(currentEdge, id, label, null, start, end);
	}
	
	/**
	 * Handle graph attributes
	 * **/
	@Override
	public void addGraphAttribute(String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(currentNetwork, attName, attValue, attType, start, end);
	}
	
	/**
	 * Handle node attributes
	 * **/
	@Override
	public void addNodeAttribute(String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(currentNode, attName, attValue, attType, start, end);
	}
	
	/**
	 * Handle edge attributes
	 * **/
	@Override
	public void addEdgeAttribute(String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(currentEdge, attName, attValue, attType, start, end);
	}

	@Override
	public void deleteEdge(CyEdge edge) {}

	@Override
	public void deleteEdgeAttribute(CyEdge edge, String label) {}

	@Override
	public void deleteGraph(CyNetwork netwrok) {}
	
	@Override
	public void deleteSubGraph(CyNetwork netwrok) {}

	@Override
	public void deleteGraphAttribute(CyNetwork netwrok, String label) {}

	@Override
	public void deleteNode(CyNode node) {}

	@Override
	public void deleteNodeAttribute(CyNode node, String label) {}

	private void setElement(CyNetwork network, String id, String label, String value, String start, String end)
	{
		currentNetwork.getRow(network).set(CyNetwork.NAME, label);
		dynGraphsAttr.add(getInterval(null,start,end), "none", network.getSUID(), CyNetwork.NAME);
	}
	
	private void setElement(CyNode node, String id, String label, String value, String start, String end)
	{
		currentNetwork.getRow(node).set(CyNetwork.NAME, label);
		dynNodesAttr.add(getInterval(null,start,end), "none", node.getSUID(), CyNetwork.NAME);
	}
	
	private void setElement(CyEdge edge, String id, String label, String value, String start, String end)
	{
		currentNetwork.getRow(edge).set(CyNetwork.NAME, label);
		dynEdgesAttr.add(getInterval(null,start,end), "none", edge.getSUID(), CyNetwork.NAME);
	}

	@SuppressWarnings("unchecked")
	private void setAttributes(CyNetwork network, String attName, String attValue, String attType, String start, String end)
	{
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(currentNetwork.getDefaultNetworkTable(), network, attName, attr);
		DynAttribute<T> attribute = (DynAttribute<T>) typeIntervalMap.getTypedValue(typeIntervalMap.getType(attType));
		dynGraphsAttr.add(getInterval((T)attr ,start, end), attribute, network.getSUID(), attName);
	}
	
	@SuppressWarnings("unchecked")
	private void setAttributes(CyNode node, String attName, String attValue, String attType, String start, String end)
	{
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(currentNetwork.getDefaultNodeTable(), node, attName, attr);
		DynAttribute<T> attribute = (DynAttribute<T>) typeIntervalMap.getTypedValue(typeIntervalMap.getType(attType));
		dynNodesAttr.add(getInterval((T)attr, start, end), attribute, node.getSUID(), attName);
	}
	
	@SuppressWarnings("unchecked")
	private void setAttributes(CyEdge edge, String attName, String attValue, String attType, String start, String end)
	{
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(currentNetwork.getDefaultEdgeTable(), edge, attName, attr);
		DynAttribute<T> attribute = (DynAttribute<T>) typeIntervalMap.getTypedValue(typeIntervalMap.getType(attType));
		dynEdgesAttr.add(getInterval((T)attr, start, end), attribute, edge.getSUID(), attName);
	}

	private void addRow(CyTable table, CyIdentifiable ci, String attName, Object attr)
	{
		if (table.getColumn(attName)==null)
			table.createColumn(attName, attr.getClass(), false);
		currentNetwork.getRow(ci).set(attName, attr);
	}
	
	private CyRootNetwork createRootNetwork(String directed)
	{
		this.isDirected = directed.equals("1")?true:false;
		return this.rootNetworkManager.getRootNetwork(networkFactory.createNetwork());
	}

	private CyNetwork createGraph(CyRootNetwork rootNetwork, String id, String label, String start, String end)
	{
		CyNetwork network = rootNetwork.getBaseNetwork();
		networks.add(network);
		return network;
	}

	private CyNode createNode(CyNetwork network, String id, String label, String start, String end)
	{
		CyNode node;
		if (!cyNodes.containsKey(id))
			node = network.addNode();
		else
			node = network.getNode(cyNodes.get(id));
		cyNodes.put(id, node.getSUID());
		
		if (!groupNodeStack.isEmpty())
		{
			ArrayList<CyNode> groupNodes = new ArrayList<CyNode>();
			groupNodes.add(node);
			currentGroup.addNodes(groupNodes);
		}
		
		return node;
	}
	
	private CyEdge createEdge(CyNetwork network, String id, String label, String source, String target, String start, String end)
	{
			CyNode nodeSource = network.getNode(cyNodes.get(source));
			CyNode nodeTarget = network.getNode(cyNodes.get(target));

			CyEdge edge;
			if (!cyEdges.containsKey(id))
				edge = this.currentNetwork.addEdge(nodeSource, nodeTarget, isDirected);
			else
				edge = currentNetwork.getEdge(cyEdges.get(id));
			cyEdges.put(id, edge.getSUID());

			if (!groupNodeStack.isEmpty())
			{
				ArrayList<CyEdge> groupEdges = new ArrayList<CyEdge>();
				groupEdges.add(edge);
				currentGroup.addEdges(groupEdges);
			}

			return edge;
	}
	
	public void exitMetaNode()
	{
		groupNodeStack.pop();
		if (!groupNodeStack.isEmpty()) 
			currentGroup = groupNodeStack.peek();
	}
	
	public void exitGraph()
	{
	}
	
	private DynInterval<T> getInterval(T value, String start, String end)
	{
		return new DynInterval<T>(value, parseStart(start), parseEnd(end));
	}
	
	private DynInterval<T> getInterval(DynInterval<T> parentInterval, T value, String start, String end)
	{
		return new DynInterval<T>(value, 
				max(parentInterval.getStart(), parseStart(start)) ,
				min(parentInterval.getEnd(), parseEnd(end)) );
	}

	public boolean checkGraph(CyNetwork c, DynInterval<T> interval)
	{
		return dynGraphsAttr.isInRange(interval, c.getSUID(), CyNetwork.NAME);
	}

	public boolean checkNode(CyNode n, DynInterval<T> interval)
	{
		return dynNodesAttr.isInRange(interval, n.getSUID(), CyNetwork.NAME);
	}

	public boolean checkEdge(CyEdge e, DynInterval<T> interval)
	{
		return dynEdgesAttr.isInRange(interval, e.getSUID(), CyNetwork.NAME);
	}

	public T getDynGraphsAttr(CyNetwork c, String attName, DynInterval<T> interval)
	{
		return dynGraphsAttr.getValue(interval, c.getSUID(), attName);
	}

	public T getDynNodesAttr(CyNode n, String attName, DynInterval<T> interval)
	{
		return dynNodesAttr.getValue(interval, n.getSUID(), attName);
	}

	public T getDynEdgesAttr(CyEdge e, String attName, DynInterval<T> interval)
	{
		return dynEdgesAttr.getValue(interval, e.getSUID(), attName);
	}

	public DynData<T> getDynGraphsAttr()
	{
		return dynGraphsAttr;
	}

	public DynData<T> getDynNodesAttr()
	{
		return dynNodesAttr;
	}

	public DynData<T> getDynEdgesAttr()
	{
		return dynEdgesAttr;
	}
	
	private double min(double a, double b)
	{
		if (a>b)
			return b;
		else
			return a;
	}
	
	private double max(double a, double b)
	{
		if (a<b)
			return b;
		else
			return a;
	}
	
	private double parseStart(String start)
	{
		if (start!=null)
			return Double.parseDouble(start);
		else
			return Double.NEGATIVE_INFINITY;
	}
	
	private double parseEnd(String end)
	{
		if (end!=null)
			return Double.parseDouble(end);
		else
			return Double.POSITIVE_INFINITY;
	}
	
}
