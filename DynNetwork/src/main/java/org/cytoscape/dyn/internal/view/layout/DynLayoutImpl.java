package org.cytoscape.dyn.internal.view.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytoscape.dyn.internal.tree.DynAttribute;
import org.cytoscape.dyn.internal.tree.DynInterval;
import org.cytoscape.dyn.internal.tree.DynIntervalTreeImpl;
import org.cytoscape.dyn.internal.util.KeyPairs;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public final class DynLayoutImpl<T> implements DynLayout<T>
{
	private final CyNetworkView view;
	
	private List<DynInterval<T>> currentNodes;
	private final DynIntervalTreeImpl<T> nodePositionsTree;
	
	private final Map<KeyPairs,DynAttribute<T>> node_X_Pos;
	private final Map<KeyPairs,DynAttribute<T>> node_Y_Pos;
	private final Map<KeyPairs,DynAttribute<T>> node_Z_Pos;

	public DynLayoutImpl(CyNetworkView view)
	{
		this.view = view;

		this.currentNodes = new ArrayList<DynInterval<T>>();
		this.nodePositionsTree = new DynIntervalTreeImpl<T>();

		this.node_X_Pos = new HashMap<KeyPairs,DynAttribute<T>>();
		this.node_Y_Pos = new HashMap<KeyPairs,DynAttribute<T>>();
		this.node_Z_Pos = new HashMap<KeyPairs,DynAttribute<T>>();
	}
	
	@Override
	public synchronized void insertNodePositionX(CyNode node, DynInterval<T> interval)
	{
		setDynAttributeX(node,interval);
		node_X_Pos.put(interval.getAttribute().getKey(), interval.getAttribute());
		nodePositionsTree.insert(interval, node.getSUID());
	}

	@Override
	public synchronized void insertNodePositionY(CyNode node, DynInterval<T> interval)
	{
		setDynAttributeY(node,interval);
		node_Y_Pos.put(interval.getAttribute().getKey(), interval.getAttribute());
		nodePositionsTree.insert(interval, node.getSUID());
	}
	
	@Override
	public synchronized void insertNodePositionZ(CyNode node, DynInterval<T> interval)
	{
		setDynAttributeZ(node,interval);
		node_Z_Pos.put(interval.getAttribute().getKey(), interval.getAttribute());
		nodePositionsTree.insert(interval, node.getSUID());
	}
	
	@Override
	public synchronized void removeNode(CyNode node)
	{
		KeyPairs key = new KeyPairs("node_X_Pos", node.getSUID());
		for (DynInterval<T> interval : node_X_Pos.get(key).getIntervalList())
			nodePositionsTree.remove(interval, node.getSUID());
		node_X_Pos.remove(key);
		
		key = new KeyPairs("node_Y_Pos", node.getSUID());
		for (DynInterval<T> interval : node_Y_Pos.get(key).getIntervalList())
			nodePositionsTree.remove(interval, node.getSUID());
		node_Y_Pos.remove(key);
		
		key = new KeyPairs("node_Z_Pos", node.getSUID());
		for (DynInterval<T> interval : node_Z_Pos.get(key).getIntervalList())
			nodePositionsTree.remove(interval, node.getSUID());
		node_Z_Pos.remove(key);

	}
	
	@Override
	public synchronized void removeAllIntervals()
	{
		nodePositionsTree.clear();
		this.node_X_Pos.clear();
		this.node_Y_Pos.clear();
		this.node_Z_Pos.clear();
		currentNodes.clear();
	}
	
	@Override
	public List<DynInterval<T>> getIntervals(CyNode node)
	{
		return nodePositionsTree.getIntervals(node.getSUID());
	}
	
	@Override
	public List<DynInterval<T>> searchNodePositions(DynInterval<T> interval)
	{
		return nodePositionsTree.search(interval);
	}
	
	@Override
	public List<DynInterval<T>> searchChangedNodePositions(DynInterval<T> interval)
	{
		List<DynInterval<T>> tempList = nodePositionsTree.search(interval);
		List<DynInterval<T>> changedList = nonOverlap(currentNodes, tempList);
		currentNodes = tempList;
		return changedList;
	}
	
	@Override
	public List<DynInterval<T>> searchNodePositionsNot(DynInterval<T> interval)
	{
		return nodePositionsTree.searchNot(interval);
	}
	
	@Override
	public void initNodePositions(double time) 
	{
		List<DynInterval<T>> intervalList = this.searchChangedNodePositions(new DynInterval<T>(time, time));
		System.out.println(time + " " + intervalList.size());
		for (DynInterval<T> interval : intervalList)
		{
			CyNode node = view.getModel().getNode(interval.getAttribute().getKey().getRow());
			if (node!=null)
				if (interval.getAttribute().getColumn().equals("node_X_Pos"))
					view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, (Double) interval.getValue());
				else if (interval.getAttribute().getColumn().equals("node_Y_Pos"))
					view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, (Double) interval.getValue());
				else if (interval.getAttribute().getColumn().equals("node_Z_Pos"))
					view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Z_LOCATION, (Double) interval.getValue());
		}
	}
	
	@Override
	public CyNetworkView getNetworkView() 
	{
		return this.view;
	}
	
	private List<DynInterval<T>> nonOverlap(List<DynInterval<T>> list1, List<DynInterval<T>> list2) 
	{
		List<DynInterval<T>> diff = new ArrayList<DynInterval<T>>();
		for (DynInterval<T> i : list2)
			if (!list1.contains(i))
				diff.add(i);
		return diff;
	}
	
	private synchronized void setDynAttributeX(CyNode node, DynInterval<T> interval)
	{
		KeyPairs key = new KeyPairs("node_X_Pos", node.getSUID());
		if (this.node_X_Pos.containsKey(key))
			this.node_X_Pos.get(key).addInterval(interval);
		else
			this.node_X_Pos.put(key, new DynAttribute<T>(interval, key));
	}
	
	private synchronized void setDynAttributeY(CyNode node, DynInterval<T> interval)
	{
		KeyPairs key = new KeyPairs("node_Y_Pos", node.getSUID());
		if (this.node_Y_Pos.containsKey(key))
			this.node_Y_Pos.get(key).addInterval(interval);
		else
			this.node_Y_Pos.put(key, new DynAttribute<T>(interval, key));
	}
	
	private synchronized void setDynAttributeZ(CyNode node, DynInterval<T> interval)
	{
		KeyPairs key = new KeyPairs("node_Z_Pos", node.getSUID());
		if (this.node_Z_Pos.containsKey(key))
			this.node_Z_Pos.get(key).addInterval(interval);
		else
			this.node_Z_Pos.put(key, new DynAttribute<T>(interval, key));
	}

}
