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

package org.cytoscape.dyn.internal.layout;

import java.util.Stack;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.dyn.internal.view.model.NodeDynamicsAttribute;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

/**
 * <code> DynLayoutFactoryImpl </code> implements the interface
 * {@link DynLayoutFactory} for creating {@link DynLayout}s.
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public class DynLayoutFactoryImpl<T> implements DynLayoutFactory<T> 
{
	private final DynLayoutManager<T> layoutManager;
	private final Stack<NodeDynamicsAttribute<T>> nodeDynamicsList;
	
	/**
	 * <code> DynLayoutFactoryImpl </code> constructor.
	 * @param layoutManager
	 */
	public DynLayoutFactoryImpl(
			DynLayoutManager<T> layoutManager)
	{
		this.layoutManager = layoutManager;
		this.nodeDynamicsList = new Stack<NodeDynamicsAttribute<T>>();
	}
	
	@Override
	public DynLayout<T> createLayout(DynNetworkView<T> dynNetworkView)
	{
		DynLayout<T> layout = new DynLayoutImpl<T>(dynNetworkView.getNetworkView());
		layoutManager.addDynLayout(layout);
		return layout;
	}
	
	@Override
	public DynLayout<T> createLayout(DynNetworkView<T> dynNetworkView, Object context)
	{
		DynLayout<T> layout = new DynLayoutImpl<T>(dynNetworkView.getNetworkView());
		layoutManager.addDynLayout(layout);
		layoutManager.addDynContext(layout, context);
		return layout;
	}
	
	@Override
	public DynLayout<T> createLayout(CyNetworkView networkView) 
	{
		DynLayout<T> layout = new DynLayoutImpl<T>(networkView);
		layoutManager.addDynLayout(layout);
		return layout;
	}

	@Override
	public DynLayout<T> createLayout(CyNetworkView networkView, Object context) 
	{
		DynLayout<T> layout = new DynLayoutImpl<T>(networkView);
		layoutManager.addDynLayout(layout);
		layoutManager.addDynContext(layout, context);
		return layout;
	}

	@Override
	public void finalizeNetwork(DynNetworkView<T> dynNetworkView) 
	{	
		if (!nodeDynamicsList.isEmpty())
		{
			while (!nodeDynamicsList.isEmpty())
				nodeDynamicsList.pop().add(dynNetworkView,this);

//			dynNetworkView.getNetworkView().fitContent();
//			dynNetworkView.getNetworkView().updateView();
		}
	}

	@Override
	public void removeLayout(DynNetworkView<T> dynNetworkView) 
	{
		layoutManager.removeDynLayout(dynNetworkView.getNetworkView());
	}
	
	@Override
	public void removeLayout(CyNetworkView networkView) 
	{
		layoutManager.removeDynLayout(networkView);
	}

	@Override
	public void addedNodeDynamics(DynNetwork<T> dynNetwork, CyNode currentNode,
			String x, String y, String start, String end) 
	{
		this.nodeDynamicsList.push(new NodeDynamicsAttribute<T>(dynNetwork,currentNode,x,y,start,end));
	}

	@Override
	public void setNodeDynamics(DynNetworkView<T> dynNetworkView, CyNode currentNode,
			String x, String y, String start, String end) 
	{
		CyNetworkView view = dynNetworkView.getNetworkView();
		DynLayout<T> layout = layoutManager.getDynLayout(view);
		
		if (x!=null)
			layout.insertNodePositionX(
				currentNode,new DynInterval<T>(Double.parseDouble(x),parseStart(start),parseEnd(end)));
		if (y!=null)
			layout.insertNodePositionY(
				currentNode,new DynInterval<T>(Double.parseDouble(y),parseStart(start),parseEnd(end)));
		
		layoutManager.getDynLayout(view).finalize();
		initializePositions(dynNetworkView, layout);
	}
	
	private void initializePositions(DynNetworkView<T> dynView, DynLayout<T> layout)
	{
		for (DynInterval<T> i : layout.getIntervalsX())
		{
			CyNode node = dynView.getNetwork().getNode(i);
			if (node!=null)
				dynView.writeVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION, (Double) i.getOnValue());
		}
			
		for (DynInterval<T> i : layout.getIntervalsY())
		{
			CyNode node = dynView.getNetwork().getNode(i);
			if (node!=null)
				dynView.writeVisualProperty(node, BasicVisualLexicon.NODE_Y_LOCATION, (Double) i.getOnValue());
		}	
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