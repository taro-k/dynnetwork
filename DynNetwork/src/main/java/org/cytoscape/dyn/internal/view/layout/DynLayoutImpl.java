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

package org.cytoscape.dyn.internal.view.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytoscape.dyn.internal.io.util.KeyPairs;
import org.cytoscape.dyn.internal.model.attribute.DynDoubleAttribute;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.model.tree.DynIntervalTree;
import org.cytoscape.dyn.internal.model.tree.DynIntervalTreeImpl;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

/**
 * <code> DynLayoutImpl </code> implements the interface {@link DynLayout}
 * and provides method to store dynamic visualization information in the form of 
 * intervals {@link DynInterval} stored in the interval tree {@link DynIntervalTree}.
 * For each node we store a series of intervals corresponding to its x,y, and z
 * positions in time. The interval tree guarantees that the write and read operation
 * to update the visualization are minimal and asynchronous.
 * 
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public final class DynLayoutImpl implements DynLayout
{
	private final CyNetworkView view;
	
	private double alpha = 1;
	private int n = 1;
	
	private List<DynInterval<Double>> currentNodesX;
	private List<DynInterval<Double>> currentNodesY;
	
	private final DynIntervalTreeImpl<Double> nodeXPositionsTree;
	private final DynIntervalTreeImpl<Double> nodeYPositionsTree;
	
	private final Map<KeyPairs,DynDoubleAttribute> node_X_Pos;
	private final Map<KeyPairs,DynDoubleAttribute> node_Y_Pos;

	/**
	 * <code> DynLayoutImpl </code> constructor.
	 * @param view
	 */
	public DynLayoutImpl(CyNetworkView view)
	{
		this.view = view;

		this.currentNodesX = new ArrayList<DynInterval<Double>>();
		this.currentNodesY = new ArrayList<DynInterval<Double>>();
		this.nodeXPositionsTree = new DynIntervalTreeImpl<Double>();
		this.nodeYPositionsTree = new DynIntervalTreeImpl<Double>();

		this.node_X_Pos = new HashMap<KeyPairs,DynDoubleAttribute>();
		this.node_Y_Pos = new HashMap<KeyPairs,DynDoubleAttribute>();
	}
	
	@Override
	public synchronized void insertNodePositionX(CyNode node, DynInterval<Double> interval)
	{
		setDynAttributeX(node,interval);
		node_X_Pos.put(interval.getAttribute().getKey(), (DynDoubleAttribute) interval.getAttribute());
		nodeXPositionsTree.insert(interval, node.getSUID());
	}

	@Override
	public synchronized void insertNodePositionY(CyNode node, DynInterval<Double> interval)
	{
		setDynAttributeY(node, (DynInterval<Double>) interval);
		node_Y_Pos.put(interval.getAttribute().getKey(), (DynDoubleAttribute)interval.getAttribute());
		nodeYPositionsTree.insert(interval, node.getSUID());
	}
	
	@Override
	public synchronized void removeNode(CyNode node)
	{
		KeyPairs key = new KeyPairs("node_X_Pos", node.getSUID());
		for (DynInterval<Double> interval : node_X_Pos.get(key).getIntervalList())
			nodeXPositionsTree.remove(interval, node.getSUID());
		node_X_Pos.remove(key);
		
		key = new KeyPairs("node_Y_Pos", node.getSUID());
		for (DynInterval<Double> interval : node_Y_Pos.get(key).getIntervalList())
			nodeYPositionsTree.remove(interval, node.getSUID());
		node_Y_Pos.remove(key);

	}
	
	@Override
	public synchronized void removeAllIntervals()
	{
		nodeXPositionsTree.clear();
		nodeYPositionsTree.clear();
		this.node_X_Pos.clear();
		this.node_Y_Pos.clear();
		currentNodesX.clear();
		currentNodesY.clear();
	}
	
	@Override
	public List<DynInterval<Double>> getIntervalsX(CyNode node)
	{
		return nodeXPositionsTree.getIntervals(node.getSUID());
	}
	
	@Override
	public List<DynInterval<Double>> getIntervalsY(CyNode node)
	{
		return nodeYPositionsTree.getIntervals(node.getSUID());
	}
	
	@Override
	public List<DynInterval<Double>> searchNodePositionsX(DynInterval<Double> interval)
	{
		return nodeXPositionsTree.search(interval);
	}
	
	@Override
	public List<DynInterval<Double>> searchNodePositionsY(DynInterval<Double> interval)
	{
		return nodeXPositionsTree.search(interval);
	}
	
	@Override
	public List<DynInterval<Double>> searchChangedNodePositionsX(DynInterval<Double> interval)
	{
		List<DynInterval<Double>> tempList = nodeXPositionsTree.search(interval);
		List<DynInterval<Double>> changedList = nonOverlap(currentNodesX, tempList);
		currentNodesX = tempList;
		return changedList;
	}
	
	@Override
	public List<DynInterval<Double>> searchChangedNodePositionsY(DynInterval<Double> interval)
	{
		List<DynInterval<Double>> tempList = nodeYPositionsTree.search(interval);
		List<DynInterval<Double>> changedList = nonOverlap(currentNodesY, tempList);
		currentNodesY = tempList;
		return changedList;
	}
	
	@Override
	public List<DynInterval<Double>> searchNodePositionsNotX(DynInterval<Double> interval)
	{
		return nodeXPositionsTree.searchNot(interval);
	}
	
	@Override
	public List<DynInterval<Double>> searchNodePositionsNotY(DynInterval<Double> interval)
	{
		return nodeYPositionsTree.searchNot(interval);
	}
	
	@Override
	public void initNodePositions(double time) 
	{
		for (DynInterval<Double> interval : this.searchChangedNodePositionsX(new DynInterval<Double>(time, time)))
		{
			CyNode node = view.getModel().getNode(interval.getAttribute().getKey().getRow());
			if (node!=null)
				view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, (Double) interval.getValue());;
		}
		
		for (DynInterval<Double> interval : this.searchChangedNodePositionsY(new DynInterval<Double>(time, time)))
				{
					CyNode node = view.getModel().getNode(interval.getAttribute().getKey().getRow());
					if (node!=null)
						view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, (Double) interval.getValue());;
				}
	}
	
	@Override
	public CyNetworkView getNetworkView() 
	{
		return this.view;
	}
	
	private List<DynInterval<Double>> nonOverlap(List<DynInterval<Double>> list1, List<DynInterval<Double>> list2) 
	{
		List<DynInterval<Double>> diff = new ArrayList<DynInterval<Double>>();
		for (DynInterval<Double> i : list2)
			if (!list1.contains(i))
				diff.add(i);
		return diff;
	}
	
	private synchronized void setDynAttributeX(CyNode node, DynInterval<Double> interval)
	{
		KeyPairs key = new KeyPairs("node_X_Pos", node.getSUID());
		if (this.node_X_Pos.containsKey(key))
			this.node_X_Pos.get(key).addInterval(interval);
		else
			this.node_X_Pos.put(key, new DynDoubleAttribute(interval, key));
	}
	
	private synchronized void setDynAttributeY(CyNode node, DynInterval<Double> interval)
	{
		KeyPairs key = new KeyPairs("node_Y_Pos", node.getSUID());
		if (this.node_Y_Pos.containsKey(key))
			this.node_Y_Pos.get(key).addInterval(interval);
		else
			this.node_Y_Pos.put(key, new DynDoubleAttribute(interval, key));
	}

	@Override
	public double getAlpha() 
	{
		return alpha;
	}

	@Override
	public void setAlpha(double alpha) 
	{
		this.alpha = alpha;
	}

	@Override
	public int getN() 
	{
		return n;
	}

	@Override
	public void setN(int n) 
	{
		this.n = n;
	}
	
}
