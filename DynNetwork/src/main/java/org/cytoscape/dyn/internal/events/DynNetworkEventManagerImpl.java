package org.cytoscape.dyn.internal.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.cytoscape.dyn.internal.model.DynData;
import org.cytoscape.dyn.internal.model.DynInterval;
import org.cytoscape.dyn.internal.model.attributes.DynAttribute;
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

/**
 * {@inheritDoc}
 */
public class DynNetworkEventManagerImpl<T> extends AbstractDynNetworkEventManager<T>
{
	private final CyGroupManager groupManager;
	private final CyGroupFactory groupFactory;
	
	private final Map<String, Long> cyNodes;
	private final Map<String, Long> cyEdges;

	private final DynData<T> dynGraphsAttr;
	private final DynData<T> dynNodesAttr;
	private final DynData<T> dynEdgesAttr;
	
	private boolean isDirected;
	
	private String spaces = "";
	
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
	}
	
	@Override
	public CyNetwork addGraph(String id, String label, String start, String end, String directed)
	{
		CyNetwork currentNetwork = createGraph(createRootNetwork(directed), id, label, start, end);
		setElement(currentNetwork, id, label, null, start, end);
		return currentNetwork;
	}

	@Override
	public CyNode addNode(CyNetwork currentNetwork, CyGroup group, String id, String label, String start, String end)
	{
		CyNode currentNode = createNode(currentNetwork, group, id, label, start, end);
		setElement(currentNetwork, currentNode, group, id, label, null, start, end);
		return currentNode;
	}
	
	@Override
	public CyGroup addGroup(CyNetwork currentNetwork, CyNode currentNode)
	{
		CyGroup currentGroup = groupFactory.createGroup(currentNetwork, currentNode, true);
		groupManager.addGroup(currentGroup);
		return currentGroup;
	}
	
	@Override
	public CyEdge addEdge(CyNetwork currentNetwork, String id, String label, String source, String target, String start, String end)
	{
		if(cyNodes.containsKey(source) && cyNodes.containsKey(target))
		{
			CyEdge currentEdge = createEdge(currentNetwork, id, label, source, target, start, end);
			setElement(currentNetwork, currentEdge, id, label, null, start, end);
			return currentEdge;
		}
		else
		{
			System.out.println("XGMML Parser Warning: edge " + (id==null?label:id) + " was ignored (unidentified nodes)!");
			return null;
		}
	}
	
	@Override
	public void addGraphAttribute(CyNetwork currentNetwork, String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(currentNetwork, attName, attValue, attType, start, end);
	}
	
	@Override
	public void addNodeAttribute(CyNetwork currentNetwork, CyNode currentNode, String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(currentNetwork, currentNode, attName, attValue, attType, start, end);
	}
	
	@Override
	public void addEdgeAttribute(CyNetwork currentNetwork, CyEdge currentEdge, String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(currentNetwork, currentEdge, attName, attValue, attType, start, end);
	}

	@Override
	public void deleteEdge(CyNetwork currentNetwork, CyEdge edge) {}

	@Override
	public void deleteEdgeAttribute(CyNetwork currentNetwork, CyEdge edge, String label) {}

	@Override
	public void deleteGraph(CyNetwork netwrok) {}

	@Override
	public void deleteGraphAttribute(CyNetwork currentNetwork, CyNetwork netwrok, String label) {}

	@Override
	public void deleteNode(CyNetwork currentNetwork, CyNode node) {}

	@Override
	public void deleteNodeAttribute(CyNetwork currentNetwork, CyNode node, String label) {}
	
	public void collapseAllGroups(CyNetwork currentNetwork)
	{
		for (CyGroup group : groupManager.getGroupSet(currentNetwork))
			group.collapse(currentNetwork);
	}
	
	public void expandAllGroups(CyNetwork currentNetwork)
	{
		for (CyGroup group : groupManager.getGroupSet(currentNetwork))
			group.expand(currentNetwork);
	}
	
	// TODO: this is a hack!!!!
	public void setGroupsAttributes(CyNetwork currentNetwork)
	{
		for (CyGroup group : groupManager.getGroupSet(currentNetwork))
		{
			if(group.isCollapsed(currentNetwork))
				group.collapse(currentNetwork);
			else
				group.expand(currentNetwork);
		}
	}

	private void setElement(CyNetwork network, String id, String label, String value, String start, String end)
	{
		network.getRow(network).set(CyNetwork.NAME, label);
		dynGraphsAttr.add(getInterval(null,start,end), "none", network.getSUID(), CyNetwork.NAME);
	}
	
	private void setElement(CyNetwork network, CyNode node, CyGroup group, String id, String label, String value, String start, String end)
	{
		network.getRow(node).set(CyNetwork.NAME, label);
		if (group==null)
			dynNodesAttr.add(getInterval(network,null,start,end), "none", node.getSUID(), CyNetwork.NAME);
		else
			dynNodesAttr.add(getInterval(group.getGroupNode(),null,start,end), "none", node.getSUID(), CyNetwork.NAME);
	}
	
	private void setElement(CyNetwork network, CyEdge edge, String id, String label, String value, String start, String end)
	{
		network.getRow(edge).set(CyNetwork.NAME, label);
		dynEdgesAttr.add(getInterval(edge.getSource(),edge.getTarget(),null,start,end), "none", edge.getSUID(), CyNetwork.NAME);
	}

	@SuppressWarnings("unchecked")
	private void setAttributes(CyNetwork network, String attName, String attValue, String attType, String start, String end)
	{
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(network, network.getDefaultNetworkTable(), network, attName, attr);
		DynAttribute<T> attribute = (DynAttribute<T>) typeIntervalMap.getTypedValue(typeIntervalMap.getType(attType));
		dynGraphsAttr.add(getInterval(network, (T)attr ,start, end), attribute, network.getSUID(), attName);
	}
	
	@SuppressWarnings("unchecked")
	private void setAttributes(CyNetwork network, CyNode node, String attName, String attValue, String attType, String start, String end)
	{
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(network, network.getDefaultNodeTable(), node, attName, attr);
		DynAttribute<T> attribute = (DynAttribute<T>) typeIntervalMap.getTypedValue(typeIntervalMap.getType(attType));
		dynNodesAttr.add(getInterval(node, (T)attr, start, end), attribute, node.getSUID(), attName);
	}
	
	@SuppressWarnings("unchecked")
	private void setAttributes(CyNetwork network, CyEdge edge, String attName, String attValue, String attType, String start, String end)
	{
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(network, network.getDefaultEdgeTable(), edge, attName, attr);
		DynAttribute<T> attribute = (DynAttribute<T>) typeIntervalMap.getTypedValue(typeIntervalMap.getType(attType));
		dynEdgesAttr.add(getInterval(edge, (T)attr, start, end), attribute, edge.getSUID(), attName);
	}

	private void addRow(CyNetwork currentNetwork, CyTable table, CyIdentifiable ci, String attName, Object attr)
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

	private CyNode createNode(CyNetwork network, CyGroup group, String id, String label, String start, String end)
	{
		CyNode node;
		if (!cyNodes.containsKey(id))
			node = network.addNode();
		else
			node = network.getNode(cyNodes.get(id));
		cyNodes.put(id, node.getSUID());
		
		if (group!=null)
		{
			ArrayList<CyNode> groupNodes = new ArrayList<CyNode>();
			groupNodes.add(node);
			group.addNodes(groupNodes);
		}
		
		return node;
	}
	
	private CyEdge createEdge(CyNetwork network, String id, String label, String source, String target, String start, String end)
	{
			CyNode nodeSource = network.getNode(cyNodes.get(source));
			CyNode nodeTarget = network.getNode(cyNodes.get(target));

			CyEdge edge;
			if (!cyEdges.containsKey(id))
				edge = network.addEdge(nodeSource, nodeTarget, isDirected);
			else
				edge = network.getEdge(cyEdges.get(id));
			cyEdges.put(id, edge.getSUID());

			for (CyGroup group : groupManager.getGroupsForNode(nodeSource))
			{
				ArrayList<CyEdge> groupEdges = new ArrayList<CyEdge>();
				groupEdges.add(edge);
				group.addEdges(groupEdges);
			}
			
			for (CyGroup group : groupManager.getGroupsForNode(nodeTarget))
			{
				ArrayList<CyEdge> groupEdges = new ArrayList<CyEdge>();
				groupEdges.add(edge);
				group.addEdges(groupEdges);
			}

			return edge;
	}

	private DynInterval<T> getInterval(T value, String start, String end)
	{
//		System.out.println(
//				spaces + "    Interval:" + " start = " + parseStart(start) + " end = " + parseEnd(end));
//		System.out.println(
//				spaces + "    Parent:" + " start = " + null + " end = " + null);
//		System.out.println(
//				spaces + "    Final:" + " start = " + parseStart(start) + " end = " + parseEnd(end));
		
		return new DynInterval<T>(value, parseStart(start), parseEnd(end));
	}

	private DynInterval<T> getInterval(CyNetwork network, T value, String start, String end)
	{
		DynAttribute<T> parentAttr = dynGraphsAttr.getDynAttribute(network.getSUID(), CyNetwork.NAME);
		
//		System.out.println(
//				spaces + "    Interval:" + " start = " + parseStart(start) + " end = " + parseEnd(end));
//		System.out.println(
//				spaces + "    Parent:" + " start = " + parentAttr.getMinTime() + " end = " + parentAttr.getMaxTime());
//		System.out.println(
//				spaces + "    Final:" + " start = " + max(parentAttr.getMinTime(), parseStart(start)) + " end = " + min(parentAttr.getMaxTime(), parseEnd(end)));
		
		return new DynInterval<T>(value, 
				max(parentAttr.getMinTime(), parseStart(start)) ,
				min(parentAttr.getMaxTime(), parseEnd(end)) );
	}
	
	private DynInterval<T> getInterval(CyNode node, T value, String start, String end)
	{
		DynAttribute<T> parentAttr = dynNodesAttr.getDynAttribute(node.getSUID(), CyNetwork.NAME);
		
//		System.out.println(
//				spaces + "    Interval:" + " start = " + parseStart(start) + " end = " + parseEnd(end));
//		System.out.println(
//				spaces + "    Parent:" + " start = " + parentAttr.getMinTime() + " end = " + parentAttr.getMaxTime());
//		System.out.println(
//				spaces + "    Final:" + " start = " + max(parentAttr.getMinTime(), parseStart(start)) + " end = " + min(parentAttr.getMaxTime(), parseEnd(end)));
		
		return new DynInterval<T>(value, 
				max(parentAttr.getMinTime(), parseStart(start)) ,
				min(parentAttr.getMaxTime(), parseEnd(end)) );
	}
	
	private DynInterval<T> getInterval(CyNode souce, CyNode target, T value, String start, String end)
	{
		DynAttribute<T> parentAttrSoruce = dynNodesAttr.getDynAttribute(souce.getSUID(), CyNetwork.NAME);
		DynAttribute<T> parentAttrTarget = dynNodesAttr.getDynAttribute(target.getSUID(), CyNetwork.NAME);
		
//		System.out.println(
//				spaces + "    Interval:" + " start = " + parseStart(start) + " end = " + parseEnd(end));
//		System.out.println(
//				spaces + "    Parent:" + " start = " + parentAttrSoruce.getMinTime() + " end = " + parentAttrSoruce.getMaxTime());
//		System.out.println(
//				spaces + "    Parent:" + " start = " + parentAttrTarget.getMinTime() + " end = " + parentAttrTarget.getMaxTime());
//		System.out.println(
//				spaces + "    Final:" + " start = " + max(max(parentAttrSoruce.getMinTime(),parentAttrTarget.getMinTime()), parseStart(start)) + " end = " + min(min(parentAttrSoruce.getMaxTime(),parentAttrTarget.getMaxTime()), parseEnd(end)));
		
		return new DynInterval<T>(value, 
				max(max(parentAttrSoruce.getMinTime(),parentAttrTarget.getMinTime()), parseStart(start)) ,
				min(min(parentAttrSoruce.getMaxTime(),parentAttrTarget.getMaxTime()), parseEnd(end)) );
	}
	
	private DynInterval<T> getInterval(CyEdge edge, T value, String start, String end)
	{
		DynAttribute<T> parentAttr = dynEdgesAttr.getDynAttribute(edge.getSUID(), CyNetwork.NAME);
		
//		System.out.println(
//				spaces + "    Interval:" + " start = " + parseStart(start) + " end = " + parseEnd(end));
//		System.out.println(
//				spaces + "    Parent:" + " start = " + parentAttr.getMinTime() + " end = " + parentAttr.getMaxTime());
//		System.out.println(
//				spaces + "    Final:" + " start = " + max(parentAttr.getMinTime(), parseStart(start)) + " end = " + min(parentAttr.getMaxTime(), parseEnd(end)));
		
		return new DynInterval<T>(value, 
				max(parentAttr.getMinTime(), parseStart(start)) ,
				min(parentAttr.getMaxTime(), parseEnd(end)) );
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

	public void setSpaces(String spaces) {
		this.spaces = spaces;
	}
	
}
