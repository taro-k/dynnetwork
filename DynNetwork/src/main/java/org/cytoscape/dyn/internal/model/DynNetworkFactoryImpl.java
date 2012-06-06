package org.cytoscape.dyn.internal.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cytoscape.dyn.internal.model.tree.DynAttribute;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.util.ObjectTypeMap;
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


public final class DynNetworkFactoryImpl<T> implements DynNetworkFactory<T>
{
	private final ObjectTypeMap typeMap;
	
	private final CyGroupManager groupManager;
	private final CyGroupFactory groupFactory;
	private final CyNetworkFactory networkFactory;
	private final CyRootNetworkManager rootNetworkManager;
	private final DynNetworkManagerImpl<T> manager;
	
	private final Map<String, Long> cyNodes;
	private final Map<String, Long> cyEdges;
	private final Collection<CyNode> removeNodeList;
	private final Collection<CyEdge> removeEdgeList;
	
	private boolean isDirected = true;
	
	public DynNetworkFactoryImpl(
			final CyNetworkFactory networkFactory,
			final CyRootNetworkManager rootNetworkManager,
			final CyGroupManager groupManager,
			final CyGroupFactory groupFactory,
			DynNetworkManagerImpl<T> manager)
	{
		this.networkFactory = networkFactory;
		this.rootNetworkManager = rootNetworkManager;
		this.groupManager = groupManager;
		this.groupFactory = groupFactory;
		this.manager = manager;
		
		typeMap = new ObjectTypeMap();
		cyNodes = new HashMap<String, Long>();
		cyEdges = new HashMap<String, Long>();
		removeNodeList = new ArrayList<CyNode>();
		removeEdgeList = new ArrayList<CyEdge>();
	}

	@Override
	public DynNetwork<T> addedGraph(String id, String label, String start, String end, String directed)
	{
		DynNetwork<T> dynNetwork = createGraph(createRootNetwork(directed), id, label, start, end);
		setElement(dynNetwork, id, label, null, start, end);
		return dynNetwork;
	}

	@Override
	public CyNode addedNode(DynNetwork<T> dynNetwork, CyGroup group, String id, String label, String start, String end)
	{
		CyNode currentNode = createNode(dynNetwork.getNetwork(), group, id, label, start, end);
		setElement(dynNetwork, currentNode, group, id, label, null, start, end);
		return currentNode;
	}
	
	@Override
	public CyGroup addedGroup(DynNetwork<T> dynNetwork, CyNode currentNode)
	{
		CyGroup currentGroup = groupFactory.createGroup(dynNetwork.getNetwork(), currentNode, true);
		groupManager.addGroup(currentGroup);
		return currentGroup;
	}
	
	@Override
	public CyEdge addedEdge(DynNetwork<T> dynNetwork, String id, String label, String source, String target, String start, String end)
	{
		if(cyNodes.containsKey(source) && cyNodes.containsKey(target))
		{
			CyEdge currentEdge = createEdge(dynNetwork.getNetwork(), id, label, source, target, start, end);
			setElement(dynNetwork, currentEdge, id, label, null, start, end);
			return currentEdge;
		}
		else
			return null;
	}
	
	@Override
	public void addedGraphAttribute(DynNetwork<T> dynNetwork, String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(dynNetwork, attName, attValue, attType, start, end);
	}
	
	@Override
	public void addedNodeAttribute(DynNetwork<T> dynNetwork, CyNode currentNode, String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(dynNetwork, currentNode, attName, attValue, attType, start, end);
	}
	
	@Override
	public void addedEdgeAttribute(DynNetwork<T> dynNetwork, CyEdge currentEdge, String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(dynNetwork, currentEdge, attName, attValue, attType, start, end);
	}

	@Override
	public void deletedGraph(DynNetwork<T> dynNetwork)
	{
		dynNetwork.remove();
		this.getRootNetwork(dynNetwork).removeSubNetwork(this.getRootNetwork(dynNetwork).getBaseNetwork());
	}

	@Override
	public void deletedNode(DynNetwork<T> dynNetwork, CyNode node) 
	{
		dynNetwork.remove(node);
		dynNetwork.getNetwork().removeNodes(getNodeRemoveList(node));
	}
	
	@Override
	public void deletedEdge(DynNetwork<T> dynNetwork, CyEdge edge)
	{
		dynNetwork.remove(edge);
		dynNetwork.getNetwork().removeEdges(getEdgeRemoveList(edge));
	}
	
	@Override
	public void finalize(DynNetwork<T> dynNetwork) 
	{
		dynNetwork.print();
	}

	private void setElement(DynNetwork<T> dynNetwork, String id, String label, String value, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		network.getRow(network).set(CyNetwork.NAME, label);
		dynNetwork.insert(CyNetwork.NAME, getInterval(null,null,start,end));
	}
	
	private void setElement(DynNetwork<T> dynNetwork, CyNode node, CyGroup group, String id, String label, String value, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		network.getRow(node).set(CyNetwork.NAME, label);
		if (group==null)
			dynNetwork.insert(node, CyNetwork.NAME, getInterval(null,dynNetwork,null,start,end));
		else
			dynNetwork.insert(node, CyNetwork.NAME, getInterval(null,dynNetwork,group.getGroupNode(),null,start,end));
	}
	
	private void setElement(DynNetwork<T> dynNetwork, CyEdge edge, String id, String label, String value, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		network.getRow(edge).set(CyNetwork.NAME, label);
		dynNetwork.insert(edge, CyNetwork.NAME, getInterval(null,dynNetwork,edge.getSource(),edge.getTarget(),null,start,end));
	}

	@SuppressWarnings("unchecked")
	private void setAttributes(DynNetwork<T> dynNetwork, String attName, String attValue, String attType, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(network, network.getDefaultNetworkTable(), network, attName, attr);
		dynNetwork.insert(attName, getInterval((Class<T>) attr.getClass(), dynNetwork, (T)attr ,start, end));
	}
	
	@SuppressWarnings("unchecked")
	private void setAttributes(DynNetwork<T> dynNetwork, CyNode node, String attName, String attValue, String attType, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(network, network.getDefaultNodeTable(), node, attName, attr);
		dynNetwork.insert(node, attName, getInterval((Class<T>) attr.getClass(),dynNetwork, (T)attr ,start, end));
	}
	
	@SuppressWarnings("unchecked")
	private void setAttributes(DynNetwork<T> dynNetwork, CyEdge edge, String attName, String attValue, String attType, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(network, network.getDefaultEdgeTable(), edge, attName, attr);
		dynNetwork.insert(edge, attName, getInterval((Class<T>) attr.getClass(),dynNetwork,edge, (T)attr, start, end));
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

	private DynNetwork<T> createGraph(CyRootNetwork rootNetwork, String id, String label, String start, String end)
	{
		CyNetwork network = rootNetwork.getBaseNetwork();
		DynNetworkImpl<T> dynNet = new DynNetworkImpl<T>(network, groupManager);
		manager.addDynNetwork(dynNet);
		return dynNet;
	}

	private CyNode createNode(CyNetwork network, CyGroup group, String id, String label, String start, String end)
	{
		CyNode node;
		if (!cyNodes.containsKey(id))
			node = network.addNode();
		else
		{
			System.out.println("\nXGMML Parser Warning: updated node id=" + id + " label=" + label + " (duplicate)");
			node = network.getNode(cyNodes.get(id));
		}
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
			{
				System.out.println("\nXGMML Parser Warning: updated edge id=" + id + " label=" + label 
						+ " source=" + source + " target=" + target + " (duplicate)");
				edge = network.getEdge(cyEdges.get(id));
			}
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
	
	private Collection<CyNode> getNodeRemoveList(CyNode node)
	{
		this.removeNodeList.clear();
		this.removeNodeList.add(node);
		return this.removeNodeList;
	}
	
	private Collection<CyEdge> getEdgeRemoveList(CyEdge edge)
	{
		this.removeEdgeList.clear();
		this.removeEdgeList.add(edge);
		return this.removeEdgeList;
	}
	
	private CyRootNetwork getRootNetwork(DynNetwork<T> dynNetwork)
	{
		return 	this.rootNetworkManager.getRootNetwork(dynNetwork.getNetwork());
	}

	private DynInterval<T> getInterval(Class<T> type, T value, String start, String end)
	{
		return new DynInterval<T>(type, value, parseStart(start), parseEnd(end));
	}

	private DynInterval<T> getInterval(Class<T> type, DynNetwork<T> dynNetwork, T value, String start, String end)
	{
		DynAttribute<T> parentAttr = dynNetwork.getDynAttribute(dynNetwork.getNetwork(), CyNetwork.NAME);
		return new DynInterval<T>(type, value, 
				max(parentAttr.getMinTime(), parseStart(start)) ,
				min(parentAttr.getMaxTime(), parseEnd(end)) );
	}
	
	private DynInterval<T> getInterval(Class<T> type, DynNetwork<T> dynNetwork, CyNode node, T value, String start, String end)
	{
		DynAttribute<T> parentAttr = dynNetwork.getDynAttribute(node, CyNetwork.NAME);
		return new DynInterval<T>(type, value, 
				max(parentAttr.getMinTime(), parseStart(start)) ,
				min(parentAttr.getMaxTime(), parseEnd(end)) );
	}
	
	private DynInterval<T> getInterval(Class<T> type, DynNetwork<T> dynNetwork, CyNode souce, CyNode target, T value, String start, String end)
	{
		DynAttribute<T> parentAttrSoruce = dynNetwork.getDynAttribute(souce, CyNetwork.NAME);
		DynAttribute<T> parentAttrTarget = dynNetwork.getDynAttribute(target, CyNetwork.NAME);
			return new DynInterval<T>(type, value, 
				max(max(parentAttrSoruce.getMinTime(),parentAttrTarget.getMinTime()), parseStart(start)) ,
				min(min(parentAttrSoruce.getMaxTime(),parentAttrTarget.getMaxTime()), parseEnd(end)) );
	}
	
	private DynInterval<T> getInterval(Class<T> type, DynNetwork<T> dynNetwork, CyEdge edge, T value, String start, String end)
	{
		DynAttribute<T> parentAttr = dynNetwork.getDynAttribute(edge, CyNetwork.NAME);
		return new DynInterval<T>(type, value, 
				max(parentAttr.getMinTime(), parseStart(start)) ,
				min(parentAttr.getMaxTime(), parseEnd(end)) );
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
