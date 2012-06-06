package org.cytoscape.dyn.internal.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.cytoscape.dyn.internal.model.tree.DynAttribute;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.model.tree.DynIntervalTreeImpl;
import org.cytoscape.dyn.internal.util.KeyPairs;
import org.cytoscape.group.CyGroup;
import org.cytoscape.group.CyGroupManager;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

public final class DynNetworkImpl<T> implements DynNetwork<T>
{	
	private final ReentrantReadWriteLock lock;
	private final Lock read;
	private final Lock write;
	
	private CyNetwork network;
	private final CyGroupManager groupManager;
	
	private final Map<String, Long> cyNodes;
	private final Map<String, Long> cyEdges;
	
	protected List<DynInterval<T>> currentNodes;
	protected List<DynInterval<T>> currentEdges;
	protected List<DynInterval<T>> currentNodesAttr;
	protected List<DynInterval<T>> currentEdgesAttr;
	
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
			CyNetwork network,
			final CyGroupManager groupManager)
	{
		this.network = network;
		this.groupManager = groupManager;
		
		cyNodes = new HashMap<String, Long>();
		cyEdges = new HashMap<String, Long>();
		
		this.currentNodes = new ArrayList<DynInterval<T>>();
		this.currentEdges = new ArrayList<DynInterval<T>>();
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
		
		this.lock = new ReentrantReadWriteLock();
		this.read  = lock.readLock();
		this.write = lock.writeLock();
	}

	@Override
	public CyNetwork getNetwork() 
	{
		read.lock();
		try{
			return this.network;
		} finally {
			read.unlock();
		}
	}

	@Override
	public CyNode readNodeTable(long key) 
	{
		read.lock();
		try{
			return network.getNode(key);
		} finally {
			read.unlock();
		}
	}

	@Override
	public void writeNodeTable(CyNode node, String name, T value) 
	{
		write.lock();
		try {
			network.getRow(node).set(name, value);
		} finally {
			write.unlock();
		}
	}

	@Override
	public CyEdge readEdgeTable(long key) 
	{
		read.lock();
		try{
			return network.getEdge(key);
		} finally {
			read.unlock();
		}
	}

	@Override
	public void writeEdgeTable(CyEdge edge, String name, T value) 
	{
		write.lock();
		try {
			network.getRow(edge).set(name, value);
		} finally {
			write.unlock();
		}
	}

	@Override
	public void insertGraph(String column, DynInterval<T> interval)
	{
		write.lock();
		try {
			setMinMaxTime(interval);
			setDynAttribute(column, interval);
			graphTable.put(interval.getAttribute().getKey(), interval.getAttribute());
			graphTree.insert(interval);
		} finally {
			write.unlock();
		}
	}

	@Override
	public void insertNode(CyNode node, String column, DynInterval<T> interval)
	{
		write.lock();
		try {
			setMinMaxTime(interval);
			setDynAttribute(node, column, interval);
			nodeTable.put(interval.getAttribute().getKey(), interval.getAttribute());
			nodeTree.insert(interval);
		} finally {
			write.unlock();
		}
	}

	@Override
	public void insertEdge(CyEdge edge, String column, DynInterval<T> interval)
	{
		write.lock();
		try {
			setMinMaxTime(interval);
			setDynAttribute(edge, column, interval);
			edgeTable.put(interval.getAttribute().getKey(), interval.getAttribute());
			edgeTree.insert(interval);
		} finally {
			write.unlock();
		}
	}
	
	@Override
	public void insertGraphAttr(String column, DynInterval<T> interval)
	{
		write.lock();
		try {
			setMinMaxTime(interval);
			setDynAttribute(column, interval);
			graphTable.put(interval.getAttribute().getKey(), interval.getAttribute());
			graphTreeAttr.insert(interval);
		} finally {
			write.unlock();
		}
	}

	@Override
	public void insertNodeAttr(CyNode node, String column, DynInterval<T> interval)
	{
		write.lock();
		try {
			setMinMaxTime(interval);
			setDynAttribute(node, column, interval);
			nodeTable.put(interval.getAttribute().getKey(), interval.getAttribute());
			nodeTreeAttr.insert(interval);
		} finally {
			write.unlock();
		}
	}

	@Override
	public void insertEdgeAttr(CyEdge edge, String column, DynInterval<T> interval)
	{
		write.lock();
		try {
			setMinMaxTime(interval);
			setDynAttribute(edge, column, interval);
			edgeTable.put(interval.getAttribute().getKey(), interval.getAttribute());
			edgeTreeAttr.insert(interval);
		} finally {
			write.unlock();
		}
	}
	
	@Override
	public void removeGraph() 
	{
		write.lock();
		try {
			this.network = null;
			this.graphTree.clear();
			this.nodeTree.clear();
			this.edgeTree.clear();
			this.graphTable.clear();
			this.nodeTable.clear();
			this.edgeTable.clear();
		} finally {
			write.unlock();
		}
	}

	@Override
	public void removeNode(CyNode node) 
	{
		write.lock();
		try {
			Iterable<CyEdge> edgeList = this.network.getAdjacentEdgeIterable(node, CyEdge.Type.ANY);
			while (edgeList.iterator().hasNext())
				this.removeEdge(edgeList.iterator().next());
			
			KeyPairs key = new KeyPairs(CyNetwork.NAME, node.getSUID());
			for (DynInterval<T> interval : nodeTable.get(key).
					getRecursiveIntervalList(new ArrayList<DynInterval<T>>()))
				nodeTree.remove(interval);
			nodeTable.remove(key);
		} finally {
			write.unlock();
		}
	}

	@Override
	public void removeEdge(CyEdge edge)
	{
		write.lock();
		try {
			KeyPairs key = new KeyPairs(CyNetwork.NAME, edge.getSUID());
			for (DynInterval<T> interval : edgeTable.get(key).
					getRecursiveIntervalList(new ArrayList<DynInterval<T>>()))
				edgeTree.remove(interval);
			edgeTable.remove(key);
		} finally {
			write.unlock();
		}	
	}

	@Override
	public List<DynInterval<T>> searchGraphs(DynInterval<T> interval)
	{
		read.lock();
		try{
			return graphTree.search(interval);
		} finally {
			read.unlock();
		}
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
	public List<DynInterval<T>> searchNodesAttr(DynInterval<T> interval)
	{
		read.lock();
		try{
			return nodeTreeAttr.search(interval);
		} finally {
			read.unlock();
		}
	}
	
	@Override
	public List<DynInterval<T>> searchEdgesAttr(DynInterval<T> interval)
	{
		read.lock();
		try{
			return edgeTreeAttr.search(interval);
		} finally {
			read.unlock();
		}
	}
	
	@Override
	public List<DynInterval<T>> searchChangedNodesAttr(DynInterval<T> interval)
	{
		write.lock();
		try{
			List<DynInterval<T>> tempList = nodeTreeAttr.search(interval);
			List<DynInterval<T>> changedList = new ArrayList<DynInterval<T>>(nonOverLap(currentNodesAttr, tempList));
			currentNodesAttr = tempList;
			return changedList;
		} finally {
			write.unlock();
		}
	}
	
	@Override
	public List<DynInterval<T>> searchChangedEdgesAttr(DynInterval<T> interval)
	{
		write.lock();
		try{
			List<DynInterval<T>> tempList = edgeTreeAttr.search(interval);
			List<DynInterval<T>> changedList = nonOverLap(currentEdgesAttr, tempList);
			currentEdgesAttr = tempList;
			return changedList;
		} finally {
			write.unlock();
		}
	}
	
	@Override
	public List<DynInterval<T>> searchNodes(DynInterval<T> interval)
	{
		read.lock();
		try{
			return nodeTree.search(interval);
		} finally {
			read.unlock();
		}
	}
	
	@Override
	public List<DynInterval<T>> searchEdges(DynInterval<T> interval)
	{
		read.lock();
		try{
			return edgeTree.search(interval);
		} finally {
			read.unlock();
		}
	}
	
	@Override
	public List<DynInterval<T>> searchChangedNodes(DynInterval<T> interval)
	{
		write.lock();
		try{
			List<DynInterval<T>> tempList = nodeTree.search(interval);
			List<DynInterval<T>> changedList = new ArrayList<DynInterval<T>>(nonOverLap(currentNodes, tempList));
			currentNodes = tempList;
			return changedList;
		} finally {
			write.unlock();
		}
	}
	
	@Override
	public List<DynInterval<T>> searchChangedEdges(DynInterval<T> interval)
	{
		write.lock();
		try{
			List<DynInterval<T>> tempList = edgeTree.search(interval);
			List<DynInterval<T>> changedList = nonOverLap(currentEdges, tempList);
			currentEdges = tempList;
			return changedList;
		} finally {
			write.unlock();
		}
	}
	
	@Override
	public DynAttribute<T> getDynAttribute(CyNetwork network, String column)
	{
		read.lock();
		try{
			return this.graphTable.get(new KeyPairs(column, network.getSUID()));
		} finally {
			read.unlock();
		}
	}
	
	@Override
	public DynAttribute<T> getDynAttribute(CyNode node, String column)
	{
		read.lock();
		try{
			return this.nodeTable.get(new KeyPairs(column, node.getSUID()));
		} finally {
			read.unlock();
		}
	}
	
	@Override
	public DynAttribute<T> getDynAttribute(CyEdge edge, String column)
	{
		read.lock();
		try{
			return this.edgeTable.get(new KeyPairs(column, edge.getSUID()));
		} finally {
			read.unlock();
		}
	}
	
	@Override
    public void collapseAllGroups()
    {
		write.lock();
		try {
			for (CyGroup group : this.groupManager.getGroupSet(this.network))
                group.collapse(this.network);
		} finally {
			write.unlock();
		}   
    }
    
	@Override
    public void expandAllGroups()
    {
		write.lock();
		try {
			for (CyGroup group : this.groupManager.getGroupSet(this.network))
                group.expand(this.network);
		} finally {
			write.unlock();
		} 
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
	public void print()
	{
		this.nodeTree.print(this.nodeTree.getRoot());
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
	
	private void setDynAttribute(String column, DynInterval<T> interval)
	{
		KeyPairs key = new KeyPairs(column, this.network.getSUID());
		if (this.graphTable.containsKey(key))
			this.graphTable.get(key).addInterval(interval);
		else
			this.graphTable.put(key, new DynAttribute<T>(interval, key));
		
		if (!column.equals(CyNetwork.NAME))
			this.graphTable.get(new KeyPairs(CyNetwork.NAME, this.network.getSUID()))
			.addChildren(this.graphTable.get(key));
	}
	
	private void setDynAttribute(CyNode node, String column, DynInterval<T> interval)
	{
		KeyPairs key = new KeyPairs(column, node.getSUID());
		if (this.nodeTable.containsKey(key))
			this.nodeTable.get(key).addInterval(interval);
		else
			this.nodeTable.put(key, new DynAttribute<T>(interval, key));
		
		if (!column.equals(CyNetwork.NAME))
			this.nodeTable.get(new KeyPairs(CyNetwork.NAME, node.getSUID()))
			.addChildren(this.nodeTable.get(key));
	}
	
	private void setDynAttribute(CyEdge edge, String column, DynInterval<T> interval)
	{
		KeyPairs key = new KeyPairs(column, edge.getSUID());
		if (this.edgeTable.containsKey(key))
			this.edgeTable.get(key).addInterval(interval);
		else
			this.edgeTable.put(key, new DynAttribute<T>(interval, key));
		
		if (!column.equals(CyNetwork.NAME))
			this.edgeTable.get(new KeyPairs(CyNetwork.NAME, edge.getSUID()))
			.addChildren(this.edgeTable.get(key));
	}
	
	private List<DynInterval<T>> nonOverLap(List<DynInterval<T>> list1, List<DynInterval<T>> list2) 
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
	
	private void setMinMaxTime(DynInterval<T> interval)
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
	
	
	
}
