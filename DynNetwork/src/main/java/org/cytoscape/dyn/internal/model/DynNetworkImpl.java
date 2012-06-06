package org.cytoscape.dyn.internal.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
	
	protected List<DynInterval<T>> currentNodes;
	protected List<DynInterval<T>> currentEdges;
	
	private final DynIntervalTreeImpl<T> graphTree;
	private final DynIntervalTreeImpl<T> nodeTree;
	private final DynIntervalTreeImpl<T> edgeTree;
	
	private final Map<KeyPairs,DynAttribute<T>> graphTable;
	private final Map<KeyPairs,DynAttribute<T>> nodeTable;
	private final Map<KeyPairs,DynAttribute<T>> edgeTable;;
	
	public DynNetworkImpl(
			CyNetwork network,
			final CyGroupManager groupManager)
	{
		this.network = network;
		this.groupManager = groupManager;
		
		this.currentNodes = new ArrayList<DynInterval<T>>();
		this.currentEdges = new ArrayList<DynInterval<T>>();
		
		this.graphTree = new DynIntervalTreeImpl<T>();
		this.nodeTree = new DynIntervalTreeImpl<T>();
		this.edgeTree = new DynIntervalTreeImpl<T>();
		
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
	public void insert(String column, DynInterval<T> interval)
	{
		write.lock();
		try {
			setDynAttribute(column, interval);
			graphTable.put(interval.getAttribute().getKey(), interval.getAttribute());
			graphTree.insert(interval);
		} finally {
			write.unlock();
		}
	}

	@Override
	public void insert(CyNode node, String column, DynInterval<T> interval)
	{
		write.lock();
		try {
			setDynAttribute(node, column, interval);
			nodeTable.put(interval.getAttribute().getKey(), interval.getAttribute());
			nodeTree.insert(interval);
		} finally {
			write.unlock();
		}
	}

	@Override
	public void insert(CyEdge edge, String column, DynInterval<T> interval)
	{
		write.lock();
		try {
			setDynAttribute(edge, column, interval);
			edgeTable.put(interval.getAttribute().getKey(), interval.getAttribute());
			edgeTree.insert(interval);
		} finally {
			write.unlock();
		}
	}
	
	@Override
	public void remove() 
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
	public void remove(CyNode node) 
	{
		write.lock();
		try {
			Iterable<CyEdge> edgeList = this.network.getAdjacentEdgeIterable(node, CyEdge.Type.ANY);
			while (edgeList.iterator().hasNext())
				this.remove(edgeList.iterator().next());
			
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
	public void remove(CyEdge edge) 
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
			List<DynInterval<T>> changedList = new ArrayList<DynInterval<T>>(nonOverLap(currentEdges, tempList));
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
	public void print()
	{
		this.nodeTree.print(this.nodeTree.getRoot());
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
	
	private Collection<DynInterval<T>> union(Collection<DynInterval<T>> coll1, Collection<DynInterval<T>> coll2) 
	{
	    Set<DynInterval<T>> union = new HashSet<DynInterval<T>>(coll1);
	    union.addAll(new HashSet<DynInterval<T>>(coll2));
	    return union;
	}

	private Collection<DynInterval<T>> intersect(Collection<DynInterval<T>> coll1, Collection<DynInterval<T>> coll2) 
	{
	    Set<DynInterval<T>> intersection = new HashSet<DynInterval<T>>(coll1);
	    intersection.retainAll(new HashSet<DynInterval<T>>(coll2));
	    return intersection;
	}

	private Collection<DynInterval<T>> nonOverLap(Collection<DynInterval<T>> coll1, Collection<DynInterval<T>> coll2) 
	{
	   Collection<DynInterval<T>> result = union(coll1, coll2);
	   result.removeAll(intersect(coll1, coll2));
	   return result;
	}
	
}
