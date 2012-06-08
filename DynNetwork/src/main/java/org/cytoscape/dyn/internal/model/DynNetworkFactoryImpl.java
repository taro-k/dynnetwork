package org.cytoscape.dyn.internal.model;

import java.util.ArrayList;
import java.util.Collection;

import org.cytoscape.dyn.internal.model.tree.DynAttribute;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.util.ObjectTypeMap;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
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
import org.cytoscape.session.CyNetworkNaming;


public final class DynNetworkFactoryImpl<T> implements DynNetworkFactory<T>
{
	private final ObjectTypeMap typeMap;
	
	private final CyGroupManager groupManager;
	private final CyGroupFactory groupFactory;
	private final CyNetworkFactory networkFactory;
	private final CyRootNetworkManager rootNetworkManager;
	private final DynNetworkManagerImpl<T> manager;
	private final CyNetworkNaming nameUtil;
	
	private boolean isDirected = true;
	
	public DynNetworkFactoryImpl(
			final CyNetworkFactory networkFactory,
			final CyRootNetworkManager rootNetworkManager,
			final CyGroupManager groupManager,
			final CyGroupFactory groupFactory,
			final DynNetworkManagerImpl<T> manager,
			final CyNetworkNaming nameUtil)
	{
		this.networkFactory = networkFactory;
		this.rootNetworkManager = rootNetworkManager;
		this.groupManager = groupManager;
		this.groupFactory = groupFactory;
		this.manager = manager;
		this.nameUtil = nameUtil;
		
		typeMap = new ObjectTypeMap();
	}

	@Override
	public DynNetwork<T> addedGraph(String id, String label, String start, String end, String directed)
	{
		DynNetwork<T> dynNetwork = createGraph(directed, id, label, start, end);
		setElement(dynNetwork, id, label, null, start, end);
		manager.addDynNetwork(dynNetwork);
		return dynNetwork;
	}

	@Override
	public CyNode addedNode(DynNetwork<T> dynNetwork, CyGroup group, String id, String label, String start, String end)
	{
		CyNode currentNode = createNode(dynNetwork, group, id, label, start, end);
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
		if(dynNetwork.containsCyNode(source) && dynNetwork.containsCyNode(target))
		{
			CyEdge currentEdge = createEdge(dynNetwork, id, label, source, target, start, end);
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
		dynNetwork.removeGraph();
		CyRootNetwork rootNetwork = this.rootNetworkManager.getRootNetwork(dynNetwork.getNetwork());
		rootNetwork.removeSubNetwork(rootNetwork.getBaseNetwork());
	}

	@Override
	public void deletedNode(DynNetwork<T> dynNetwork, CyNode node) 
	{
		dynNetwork.removeNode(node);
		dynNetwork.getNetwork().removeNodes(getNodeRemoveList(node));
	}
	
	@Override
	public void deletedEdge(DynNetwork<T> dynNetwork, CyEdge edge)
	{
		dynNetwork.removeEdge(edge);
		dynNetwork.getNetwork().removeEdges(getEdgeRemoveList(edge));
	}
	
	@Override
	public void finalizeNetwork(DynNetwork<T> dynNetwork) 
	{
		dynNetwork.print();
	}

	private void setElement(DynNetwork<T> dynNetwork, String id, String label, String value, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		network.getRow(network).set(CyNetwork.NAME, nameUtil.getSuggestedNetworkTitle(label));
		dynNetwork.insertGraph(CyNetwork.NAME, getInterval(null,null,start,end));
	}
	
	private void setElement(DynNetwork<T> dynNetwork, CyNode node, CyGroup group, String id, String label, String value, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		network.getRow(node).set(CyNetwork.NAME, label);
		if (group==null)
			dynNetwork.insertNode(node, CyNetwork.NAME, getInterval(null,dynNetwork,null,start,end));
		else
			dynNetwork.insertNode(node, CyNetwork.NAME, getInterval(null,dynNetwork,group.getGroupNode(),null,start,end));
	}
	
	private void setElement(DynNetwork<T> dynNetwork, CyEdge edge, String id, String label, String value, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		network.getRow(edge).set(CyNetwork.NAME, label);
		dynNetwork.insertEdge(edge, CyNetwork.NAME, getInterval(null,dynNetwork,edge.getSource(),edge.getTarget(),null,start,end));
	}

	@SuppressWarnings("unchecked")
	private void setAttributes(DynNetwork<T> dynNetwork, String attName, String attValue, String attType, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(network, network.getDefaultNetworkTable(), network, attName, attr);
		dynNetwork.insertGraphAttr(attName, getInterval((Class<T>) attr.getClass(), dynNetwork, (T)attr ,start, end));
	}
	
	@SuppressWarnings("unchecked")
	private void setAttributes(DynNetwork<T> dynNetwork, CyNode node, String attName, String attValue, String attType, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(network, network.getDefaultNodeTable(), node, attName, attr);
		dynNetwork.insertNodeAttr(node, attName, getInterval((Class<T>) attr.getClass(),dynNetwork, (T)attr ,start, end));
	}
	
	@SuppressWarnings("unchecked")
	private void setAttributes(DynNetwork<T> dynNetwork, CyEdge edge, String attName, String attValue, String attType, String start, String end)
	{
		CyNetwork network = dynNetwork.getNetwork();
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		addRow(network, network.getDefaultEdgeTable(), edge, attName, attr);
		dynNetwork.insertEdgeAttr(edge, attName, getInterval((Class<T>) attr.getClass(),dynNetwork,edge, (T)attr, start, end));
	}

	private void addRow(CyNetwork currentNetwork, CyTable table, CyIdentifiable ci, String attName, Object attr)
	{
		if (table.getColumn(attName)==null)
			table.createColumn(attName, attr.getClass(), false);
		currentNetwork.getRow(ci).set(attName, attr);
	}

	private DynNetwork<T> createGraph(String directed, String id, String label, String start, String end)
	{
		this.isDirected = directed.equals("1")?true:false;
		CyRootNetwork rootNetwork = this.rootNetworkManager.getRootNetwork(networkFactory.createNetwork());
		CyNetwork network = rootNetwork.getBaseNetwork();
		DynNetworkImpl<T> dynNet = new DynNetworkImpl<T>(network, groupManager);
		return dynNet;
	}

	private CyNode createNode(DynNetwork<T> dynNetwork, CyGroup group, String id, String label, String start, String end)
	{
		CyNode node;
		if (!dynNetwork.containsCyNode(id))
		{
			node = dynNetwork.getNetwork().addNode();
			dynNetwork.setCyNode(id, node.getSUID());
		}
		else
		{
//			System.out.println("\nXGMML Parser Warning: updated node id=" + id + " label=" + label
//					+ " (duplicate)");
			node = dynNetwork.getNetwork().getNode(dynNetwork.getCyNode(id));
		}
		
		if (group!=null)
		{
			ArrayList<CyNode> groupNodes = new ArrayList<CyNode>();
			groupNodes.add(node);
			group.addNodes(groupNodes);
		}
		
		return node;
	}
	
	private CyEdge createEdge(DynNetwork<T> dynNetwork, String id, String label, String source, String target, String start, String end)
	{
			CyNode nodeSource = dynNetwork.getNetwork().getNode(dynNetwork.getCyNode(source));
			CyNode nodeTarget = dynNetwork.getNetwork().getNode(dynNetwork.getCyNode(target));

			CyEdge edge;
			if (!dynNetwork.containsCyEdge(id))
			{
				edge = dynNetwork.getNetwork().addEdge(nodeSource, nodeTarget, isDirected);
				dynNetwork.setCyEdge(id, edge.getSUID());
			}
			else
			{
//				System.out.println("\nXGMML Parser Warning: updated edge id=" + id + " label=" + label 
//						+ " source=" + source + " target=" + target + " (duplicate)");
				edge = dynNetwork.getNetwork().getEdge(dynNetwork.getCyEdge(id));
			}

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
		ArrayList<CyNode> list = new ArrayList<CyNode>();
		list.add(node);
		return list;
	}
	
	private Collection<CyEdge> getEdgeRemoveList(CyEdge edge)
	{
		ArrayList<CyEdge> list = new ArrayList<CyEdge>();
		list.add(edge);
		return list;
	}

	private DynInterval<T> getInterval(Class<T> type, T value, String start, String end)
	{
		return new DynInterval<T>(type, value, parseStart(start), parseEnd(end));
	}

	private DynInterval<T> getInterval(Class<T> type, DynNetwork<T> dynNetwork, T value, String start, String end)
	{
		DynAttribute<T> parentAttr = dynNetwork.getDynAttribute(dynNetwork.getNetwork(), CyNetwork.NAME);
		return new DynInterval<T>(type, value, 
				max(parentAttr.getMinTime(), parseStart(start)),
				min(parentAttr.getMaxTime(), parseEnd(end)) );
	}
	
	private DynInterval<T> getInterval(Class<T> type, DynNetwork<T> dynNetwork, CyNode node, T value, String start, String end)
	{
		DynAttribute<T> parentAttr = dynNetwork.getDynAttribute(node, CyNetwork.NAME);
		return new DynInterval<T>(type, value, 
				max(parentAttr.getMinTime(), parseStart(start)),
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
				max(parentAttr.getMinTime(), parseStart(start)),
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

	@Override
	public DynNetworkView<T> createView(DynNetwork<T> dynNetwork) 
	{
		return null;
	}

	
	

}
