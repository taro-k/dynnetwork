/*
 * DynNetwork plugin for Cytoscape 3.0 (http://www.cytoscape.org/).
 * Copyright (C) 2012 Sabina Sara Pfister
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.cytoscape.dyn.internal.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytoscape.dyn.internal.model.tree.DynAttribute;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.model.tree.DynIntervalTreeImpl;
import org.cytoscape.dyn.internal.util.KeyPairs;
import org.cytoscape.group.CyGroupManager;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 * <code> DynNetworkImpl </code> implements the interface {@link DynNetwork}
 * and provides method and data structures to record and retrieve network dynamic 
 * information
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class DynNetworkImpl<T> implements DynNetwork<T>
{	
	private final CyNetwork network;
	private final CyGroupManager groupManager;
	
	private int visibleNodes;
	private int visibleEdges;
	
	private final boolean isDirected;

	private final Map<String, Long> cyNodes;
	private final Map<String, Long> cyEdges;

	private List<DynInterval<T>> currentNodes;
	private List<DynInterval<T>> currentEdges;
	private List<DynInterval<T>> currentGraphsAttr;
	private List<DynInterval<T>> currentNodesAttr;
	private List<DynInterval<T>> currentEdgesAttr;

	private final DynIntervalTreeImpl<T> graphTree;
	private final DynIntervalTreeImpl<T> nodeTree;
	private final DynIntervalTreeImpl<T> edgeTree;
	private final DynIntervalTreeImpl<T> graphTreeAttr;
	private final DynIntervalTreeImpl<T> nodeTreeAttr;
	private final DynIntervalTreeImpl<T> edgeTreeAttr;

	private final Map<KeyPairs,DynAttribute<T>> graphTable;
	private final Map<KeyPairs,DynAttribute<T>> nodeTable;
	private final Map<KeyPairs,DynAttribute<T>> edgeTable;

	private double minStartTime = Double.POSITIVE_INFINITY;
	private double maxStartTime = Double.NEGATIVE_INFINITY;
	private double minEndTime = Double.POSITIVE_INFINITY;
	private double maxEndTime = Double.NEGATIVE_INFINITY;

	public DynNetworkImpl(
			final CyNetwork network,
			final CyGroupManager groupManager,
			final boolean isDirected)
	{
		this.network = network;
		this.groupManager = groupManager;
		
		this.visibleNodes = 0;
		this.visibleEdges = 0;
		
		this.isDirected = isDirected;

		cyNodes = new HashMap<String, Long>();
		cyEdges = new HashMap<String, Long>();

		this.currentNodes = new ArrayList<DynInterval<T>>();
		this.currentEdges = new ArrayList<DynInterval<T>>();
		this.currentGraphsAttr = new ArrayList<DynInterval<T>>();
		this.currentNodesAttr = new ArrayList<DynInterval<T>>();
		this.currentEdgesAttr = new ArrayList<DynInterval<T>>();

		this.graphTree = new DynIntervalTreeImpl<T>();
		this.nodeTree = new DynIntervalTreeImpl<T>();
		this.edgeTree = new DynIntervalTreeImpl<T>();
		this.graphTreeAttr = new DynIntervalTreeImpl<T>();
		this.nodeTreeAttr = new DynIntervalTreeImpl<T>();
		this.edgeTreeAttr = new DynIntervalTreeImpl<T>();

		this.graphTable = new HashMap<KeyPairs,DynAttribute<T>>();
		this.nodeTable = new HashMap<KeyPairs,DynAttribute<T>>();
		this.edgeTable = new HashMap<KeyPairs,DynAttribute<T>>();
	}

	@Override
	public synchronized void insertGraph(String column, DynInterval<T> interval)
	{
		setMinMaxTime(interval);
		setDynAttribute(column, interval);
		graphTable.put(interval.getAttribute().getKey(), interval.getAttribute());
		graphTree.insert(interval, this.network.getSUID());
	}

	@Override
	public synchronized void insertNode(CyNode node, String column, DynInterval<T> interval)
	{
		setMinMaxTime(interval);
		setDynAttribute(node, column, interval);
		nodeTable.put(interval.getAttribute().getKey(), interval.getAttribute());
		nodeTree.insert(interval, node.getSUID());
	}

	@Override
	public synchronized void insertEdge(CyEdge edge, String column, DynInterval<T> interval)
	{
		setMinMaxTime(interval);
		setDynAttribute(edge, column, interval);
		edgeTable.put(interval.getAttribute().getKey(), interval.getAttribute());
		edgeTree.insert(interval, edge.getSUID());
	}

	@Override
	public synchronized void insertGraphAttr(String column, DynInterval<T> interval)
	{
		setMinMaxTime(interval);
		setDynAttribute(column, interval);
		graphTable.put(interval.getAttribute().getKey(), interval.getAttribute());
//		graphTreeAttr.insert(interval, this.network.getSUID());
	}

	@Override
	public synchronized void insertNodeAttr(CyNode node, String column, DynInterval<T> interval)
	{
		setMinMaxTime(interval);
		setDynAttribute(node, column, interval);
		nodeTable.put(interval.getAttribute().getKey(), interval.getAttribute());
//		nodeTreeAttr.insert(interval, node.getSUID());
	}

	@Override
	public synchronized void insertEdgeAttr(CyEdge edge, String column, DynInterval<T> interval)
	{
		setMinMaxTime(interval);
		setDynAttribute(edge, column, interval);
		edgeTable.put(interval.getAttribute().getKey(), interval.getAttribute());
//		edgeTreeAttr.insert(interval, edge.getSUID());
	}

	@Override
	public synchronized void removeGraph() 
	{
		this.graphTree.clear();
		this.nodeTree.clear();
		this.edgeTree.clear();
		this.graphTable.clear();
		this.nodeTable.clear();
		this.edgeTable.clear();
	}

	@Override
	public synchronized void removeNode(CyNode node) 
	{
		Iterable<CyEdge> edgeList = this.network.getAdjacentEdgeIterable(node, CyEdge.Type.ANY);
		while (edgeList.iterator().hasNext())
			this.removeEdge(edgeList.iterator().next());

		KeyPairs key = new KeyPairs(CyNetwork.NAME, node.getSUID());
		for (DynInterval<T> interval : nodeTable.get(key).
				getRecursiveIntervalList(new ArrayList<DynInterval<T>>()))
			nodeTree.remove(interval, node.getSUID());
		nodeTable.remove(key);
	}

	@Override
	public synchronized void removeEdge(CyEdge edge)
	{
		KeyPairs key = new KeyPairs(CyNetwork.NAME, edge.getSUID());
		for (DynInterval<T> interval : edgeTable.get(key).
				getRecursiveIntervalList(new ArrayList<DynInterval<T>>()))
			edgeTree.remove(interval, edge.getSUID());
		edgeTable.remove(key);
	}

	@Override
	public void removeGraphAttr() 
	{

	}

	@Override
	public void removeNodeAttr(CyNode node) 
	{

	}

	@Override
	public void removeEdgeAttr(CyEdge edge) 
	{

	}
	
	@Override
	public List<DynInterval<T>> getIntervals()
	{
		List<DynInterval<T>> list = this.graphTree.getIntervals(this.network.getSUID());
		list.addAll(this.graphTreeAttr.getIntervals(this.network.getSUID()));
		return list;
	}
	
	@Override
	public List<DynInterval<T>> getIntervals(CyNode node)
	{
		List<DynInterval<T>> list = this.nodeTree.getIntervals(node.getSUID());
		list.addAll(this.nodeTreeAttr.getIntervals(node.getSUID()));
		return list;
	}
	
	@Override
	public List<DynInterval<T>> getIntervals(CyEdge edge)
	{
		List<DynInterval<T>> list = this.edgeTree.getIntervals(edge.getSUID());
		list.addAll(this.edgeTreeAttr.getIntervals(edge.getSUID()));
		return list;
	}
	
	@Override
	public List<DynInterval<T>> searchNodes(DynInterval<T> interval)
	{
		return nodeTree.search(interval);
	}

	@Override
	public List<DynInterval<T>> searchEdges(DynInterval<T> interval)
	{
		return edgeTree.search(interval);
	}
	
	@Override
	public List<DynInterval<T>> searchNodesNot(DynInterval<T> interval)
	{
		return nodeTree.searchNot(interval);
	}

	@Override
	public List<DynInterval<T>> searchEdgesNot(DynInterval<T> interval)
	{
		return edgeTree.searchNot(interval);
	}
	
	@Override
	public List<DynInterval<T>> searchChangedNodes(DynInterval<T> interval)
	{
		List<DynInterval<T>> tempList = nodeTree.search(interval);
		List<DynInterval<T>> changedList = nonOverlap(currentNodes, tempList);
		this.visibleNodes = tempList.size();
		currentNodes = tempList;
		return changedList;
	}

	@Override
	public List<DynInterval<T>> searchChangedEdges(DynInterval<T> interval)
	{
		List<DynInterval<T>> tempList = edgeTree.search(interval);
		List<DynInterval<T>> changedList = nonOverlap(currentEdges, tempList);
		this.visibleEdges = tempList.size();
		currentEdges = tempList;
		return changedList;
	}
	
	@Override
	public List<DynInterval<T>> searchGraphsAttr(DynInterval<T> interval)
	{
		return graphTreeAttr.search(interval);
	}

	@Override
	public List<DynInterval<T>> searchNodesAttr(DynInterval<T> interval)
	{
		return nodeTreeAttr.search(interval);
	}

	@Override
	public List<DynInterval<T>> searchEdgesAttr(DynInterval<T> interval)
	{
		return edgeTreeAttr.search(interval);
	}
	
	@Override
	public List<DynInterval<T>> searchChangedGraphsAttr(DynInterval<T> interval)
	{
		List<DynInterval<T>> tempList = graphTreeAttr.search(interval);
		List<DynInterval<T>> changedList = nonOverlap(currentGraphsAttr, tempList);
		currentGraphsAttr = tempList;
		return changedList;
	}

	@Override
	public List<DynInterval<T>> searchChangedNodesAttr(DynInterval<T> interval)
	{
		List<DynInterval<T>> tempList = nodeTreeAttr.search(interval);
		List<DynInterval<T>> changedList = nonOverlap(currentNodesAttr, tempList);
		currentNodesAttr = tempList;
		return changedList;
	}

	@Override
	public List<DynInterval<T>> searchChangedEdgesAttr(DynInterval<T> interval)
	{
		List<DynInterval<T>> tempList = edgeTreeAttr.search(interval);
		List<DynInterval<T>> changedList = nonOverlap(currentEdgesAttr, tempList);
		currentEdgesAttr = tempList;
		return changedList;
	}

	@Override
	public DynAttribute<T> getDynAttribute(CyNetwork network, String column)
	{
		return this.graphTable.get(new KeyPairs(column, network.getSUID()));
	}

	@Override
	public DynAttribute<T> getDynAttribute(CyNode node, String column)
	{
		return this.nodeTable.get(new KeyPairs(column, node.getSUID()));
	}

	@Override
	public DynAttribute<T> getDynAttribute(CyEdge edge, String column)
	{
		return this.edgeTable.get(new KeyPairs(column, edge.getSUID()));
	}
	
	@Override
	public CyNetwork getNetwork() 
	{
		return this.network;
	}
	
	@Override
	public CyNode getNode(long key) 
	{
		return network.getNode(key);
	}

	@Override
	public CyEdge getEdge(long key) 
	{
		return network.getEdge(key);
	}

	@Override
	public long getCyNode(String id)
	{
		return cyNodes.get(id);
	}

	@Override
	public long getCyEdge(String id) 
	{
		return cyEdges.get(id);
	}

	@Override
	public boolean containsCyNode(String id)
	{
		return cyNodes.containsKey(id);
	}

	@Override
	public boolean containsCyEdge(String id) 
	{
		return cyEdges.containsKey(id);
	}

	@Override
	public void setCyNode(String id, long value) 
	{
		cyNodes.put(id, value);
	}

	@Override
	public void setCyEdge(String id, long value) 
	{
		cyEdges.put(id, value);
	}
	
	@Override
	public void writeGraphTable(String name, T value) 
	{
		network.getRow(this.network).set(name, value);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T readGraphTable(String name, T value) 
	{
		return (T) network.getRow(this.network).get(name, value.getClass());
	}
	
	@Override
	public void writeNodeTable(CyNode node, String name, T value) 
	{
		network.getRow(node).set(name, value);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T readNodeTable(CyNode node, String name, T value) 
	{
		return (T) network.getRow(node).get(name, value.getClass());
	}

	@Override
	public void writeEdgeTable(CyEdge edge, String name, T value) 
	{
		network.getRow(edge).set(name, value);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T readEdgeTable(CyEdge edge, String name, T value) 
	{
		return (T) network.getRow(edge).get(name, value.getClass());
	}
	
	@Override
	public void finalizeNetwork() 
	{
		createAttrIntervalTrees();
//		print();
//		System.out.println("Interval Edge Tree Order");
//		this.edgeTree.print();
//		for (CyGroup group : this.groupManager.getGroupSet(this.network))
//		{
//			group.removeNodes(new ArrayList<CyNode>());
//			Iterable<CyEdge> metaEdgeList = this.network.getAdjacentEdgeIterable(group.getGroupNode(), CyEdge.Type.ANY);
//			while (metaEdgeList.iterator().hasNext())
//				System.out.println("yes!");
//		}
	}

	@Override
	public double getMinTime()
	{
		if (Double.isInfinite(minStartTime))
			if (Double.isInfinite(minEndTime))
				return -1;
			else
				return minEndTime;
		else
			return minStartTime;
	}

	@Override
	public double getMaxTime()
	{
		if (Double.isInfinite(maxEndTime))
			if (Double.isInfinite(maxStartTime))
				return 1;
			else
				return maxStartTime;
		else
			return maxEndTime;
	}
	
	@Override
	public boolean isDirected() 
	{
		return this.isDirected;
	}
	
	@Override
	public int getVisibleNodes()
	{
		return this.visibleNodes;
	}

	@Override
	public int getVisibleEdges()
	{
		return this.visibleEdges;
	}

	private synchronized void setDynAttribute(String column, DynInterval<T> interval)
	{
		KeyPairs key = new KeyPairs(column, this.network.getSUID());
		if (this.graphTable.containsKey(key))
			this.graphTable.get(key).addInterval(interval);
		else
			this.graphTable.put(key, new DynAttribute<T>(interval, key));
		overwriteGraphIntervals(this.graphTable.get(key), interval);

		if (!column.equals(CyNetwork.NAME))
			this.graphTable.get(new KeyPairs(CyNetwork.NAME, this.network.getSUID()))
			.addChildren(this.graphTable.get(key));
	}

	private synchronized void setDynAttribute(CyNode node, String column, DynInterval<T> interval)
	{
		KeyPairs key = new KeyPairs(column, node.getSUID());
		if (this.nodeTable.containsKey(key))
			this.nodeTable.get(key).addInterval(interval);
		else
			this.nodeTable.put(key, new DynAttribute<T>(interval, key));
		overwriteNodeIntervals(this.nodeTable.get(key), interval);

		if (!column.equals(CyNetwork.NAME))
			this.nodeTable.get(new KeyPairs(CyNetwork.NAME, node.getSUID()))
			.addChildren(this.nodeTable.get(key));
	}

	private synchronized void setDynAttribute(CyEdge edge, String column, DynInterval<T> interval)
	{
		KeyPairs key = new KeyPairs(column, edge.getSUID());
		if (this.edgeTable.containsKey(key))
			this.edgeTable.get(key).addInterval(interval);
		else
			this.edgeTable.put(key, new DynAttribute<T>(interval, key));
		overwriteEdgeIntervals(this.edgeTable.get(key), interval);

		if (!column.equals(CyNetwork.NAME))
			this.edgeTable.get(new KeyPairs(CyNetwork.NAME, edge.getSUID()))
			.addChildren(this.edgeTable.get(key));
	}
	
	private List<DynInterval<T>> nonOverlap(List<DynInterval<T>> list1, List<DynInterval<T>> list2) 
	{
		List<DynInterval<T>> diff = new ArrayList<DynInterval<T>>();
		for (DynInterval<T> i : list1)
			if (!list2.contains(i))
				diff.add(i);
		for (DynInterval<T> i : list2)
			if (!list1.contains(i))
				diff.add(i);
		return diff;
	}

	private synchronized void setMinMaxTime(DynInterval<T> interval)
	{
		double start = interval.getStart();
		double end = interval.getEnd();
		if (!Double.isInfinite(start))
		{
			minStartTime = Math.min(minStartTime, start);
			maxStartTime = Math.max(maxStartTime, start);
		}
		if (!Double.isInfinite(end))
		{
			maxEndTime = Math.max(maxEndTime, end);
			minEndTime = Math.min(minEndTime, end);
		}
	}
	
	private void createAttrIntervalTrees()
	{
		for (DynAttribute<T> attr : graphTable.values())
			for (DynInterval<T> interval : attr.getIntervalList())
				graphTreeAttr.insert(interval, attr.getRow());	

		for (DynAttribute<T> attr : nodeTable.values())
			for (DynInterval<T> interval : attr.getIntervalList())
				nodeTreeAttr.insert(interval, attr.getRow());

		for (DynAttribute<T> attr : edgeTable.values())
			for (DynInterval<T> interval : attr.getIntervalList())
				edgeTreeAttr.insert(interval, attr.getRow());
	}

	private void overwriteGraphIntervals(DynAttribute<T> attr, DynInterval<T> interval)
	{
		for (DynInterval<T> i : attr.getIntervalList())
			if (interval!=i && i.compareTo(interval)>0)
			{
				if (interval.getStart()>i.getStart() && interval.getEnd()>=i.getEnd())
					i.setEnd(interval.getStart());
				else if (interval.getStart()==i.getStart() && interval.getEnd()>i.getEnd())
					interval.setStart(i.getEnd());
				else if (interval.getEnd()<i.getEnd() && interval.getStart()<=i.getStart())
					i.setStart(interval.getEnd());
				else if (interval.getEnd()==i.getEnd() && interval.getStart()<i.getStart())
					interval.setEnd(i.getStart());
				else
				{
					String label = this.readGraphTable(CyNetwork.NAME, (T) "string").toString();
					System.out.println("\nXGMML Parser Warning: inconsistent attribute interval for graph label=" + label + 
							"\n  > attr=" + attr.getColumn() + " value=" + i.getValue() + " start=" + i.getStart() + " end=" + i.getEnd() +
							"\n  > attr=" + attr.getColumn() + " value=" + interval.getValue() + " start=" + interval.getStart() + " end=" + interval.getEnd());
				}
			}
	}
	
	private void overwriteNodeIntervals(DynAttribute<T> attr, DynInterval<T> interval)
	{
		for (DynInterval<T> i : attr.getIntervalList())
			if (interval!=i && i.compareTo(interval)>0)
			{
				if (interval.getStart()>i.getStart() && interval.getEnd()>=i.getEnd())
					i.setEnd(interval.getStart());
				else if (interval.getStart()==i.getStart() && interval.getEnd()>i.getEnd())
					interval.setStart(i.getEnd());
				else if (interval.getEnd()<i.getEnd() && interval.getStart()<=i.getStart())
					i.setStart(interval.getEnd());
				else if (interval.getEnd()==i.getEnd() && interval.getStart()<i.getStart())
					interval.setEnd(i.getStart());
				else
				{
					String label = this.readNodeTable(this.getNode(interval.getAttribute().getRow()),CyNetwork.NAME, (T) "string").toString();
					System.out.println("\nXGMML Parser Warning: inconsistent attribute interval for node label=" + label + 
							"\n  > attr=" + attr.getColumn() + " value=" + i.getValue() + " start=" + i.getStart() + " end=" + i.getEnd() +
							"\n  > attr=" + attr.getColumn() + " value=" + interval.getValue() + " start=" + interval.getStart() + " end=" + interval.getEnd());
				}
			}
	}
	
	private void overwriteEdgeIntervals(DynAttribute<T> attr, DynInterval<T> interval)
	{
		for (DynInterval<T> i : attr.getIntervalList())
			if (interval!=i && i.compareTo(interval)>0)
			{
				if (interval.getStart()>i.getStart() && interval.getEnd()>=i.getEnd())
					i.setEnd(interval.getStart());
				else if (interval.getStart()==i.getStart() && interval.getEnd()>i.getEnd())
					interval.setStart(i.getEnd());
				else if (interval.getEnd()<i.getEnd() && interval.getStart()<=i.getStart())
					i.setStart(interval.getEnd());
				else if (interval.getEnd()==i.getEnd() && interval.getStart()<i.getStart())
					interval.setEnd(i.getStart());
				else
				{   
					String label = this.readEdgeTable(this.getEdge(interval.getAttribute().getRow()),CyNetwork.NAME, (T) "string").toString();
					System.out.println("\nXGMML Parser Warning: inconsistent attribute interval for edge label=" + label + 
							"\n  > attr=" + attr.getColumn() + " value=" + i.getValue() + " start=" + i.getStart() + " end=" + i.getEnd() +
							"\n  > attr=" + attr.getColumn() + " value=" + interval.getValue() + " start=" + interval.getStart() + " end=" + interval.getEnd());
				}
			}
	}
	
	private void print()
	{
		DecimalFormat formatter = new DecimalFormat("#0.000");
		
		System.out.println("\nELEMENT\tLABEL\tCOLUMN\tVALUE\tSTART\tEND");
		
		for (DynAttribute<T> attr : graphTable.values())
			for (DynInterval<T> interval : attr.getIntervalList())
			{
				System.out.println("graph" + "\t" + this.readGraphTable(CyNetwork.NAME, (T) "string") + "\t" + attr.getKey().getColumn() + 
						"\t" + interval.getValue() + "\t" + formatter.format(interval.getStart()) + "\t" + formatter.format(interval.getEnd()));
				graphTreeAttr.insert(interval, attr.getRow());
			}
		
		for (DynAttribute<T> attr : nodeTable.values())
			for (DynInterval<T> interval : attr.getIntervalList())
			{
				if (this.getNode(interval.getAttribute().getRow())!=null)
				System.out.println("node" + "\t" + this.readNodeTable(this.getNode(interval.getAttribute().getRow()),CyNetwork.NAME, (T) "string") + "\t" + attr.getKey().getColumn() + 
						"\t" + interval.getValue() + "\t" + formatter.format(interval.getStart()) + "\t" + formatter.format(interval.getEnd()));
				nodeTreeAttr.insert(interval, attr.getRow());
			}

		for (DynAttribute<T> attr : edgeTable.values())
			for (DynInterval<T> interval : attr.getIntervalList())
			{
				if (this.getEdge(interval.getAttribute().getRow())!=null)
				System.out.println("edge" + "\t" + this.readEdgeTable(this.getEdge(interval.getAttribute().getRow()),CyNetwork.NAME, (T) "string") + "\t" + attr.getKey().getColumn() + 
						"\t" + interval.getValue() + "\t" + formatter.format(interval.getStart()) + "\t" + formatter.format(interval.getEnd()));
				edgeTreeAttr.insert(interval, attr.getRow());
			}
	}
	
}
