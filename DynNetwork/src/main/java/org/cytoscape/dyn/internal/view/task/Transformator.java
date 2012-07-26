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

package org.cytoscape.dyn.internal.view.task;

import java.util.List;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

/**
 * <code> Transformator </code> is used to change visual properties by interpolating
 * the values to change.
 * 
 * @author Sabina Sara Pfister
 *
 */
public class Transformator
{
	private double alpha;
    private int iterations;
    private int delay;
	
	private double timeStart;
	private double timeEnd;
	
	private int onCounter;
	private int offCounter;

	/**
	 * <code> Transformator </code> constructor.
	 */
	public Transformator() 
	{
		this.alpha = 0.3;
		this.iterations = 10;
		this.delay = (int) (500/iterations);
			
	}
	
	/**
	 * Run transformation on given interval lists.
	 * @param <T>
	 * @param interval list changedNodes
	 * @param interval list changedEdges
	 * @param interval list changedNodePositions
	 */
	public <T> void run(
			final DynNetwork<T> dynNetwork,
			final DynNetworkView<T> view,
			final DynInterval<T> timeInterval,
			final int visibility)
	{
		this.onCounter = 255;
		this.offCounter = visibility;
		
		List<DynInterval<T>> nodes = dynNetwork.searchChangedNodes(timeInterval);
		List<DynInterval<T>> edges = dynNetwork.searchChangedEdges(timeInterval);
		
		if (nodes.isEmpty() && edges.isEmpty())
			return;
		
		for (int i=0;i<iterations;i++)
		{
			timeStart = System.currentTimeMillis();
			
			onCounter = (int) ((1-alpha)*onCounter+alpha*visibility);
			offCounter = (int) ((1-alpha)*offCounter+alpha*255);

			for (DynInterval<T> interval : nodes)
				if (interval.isOn())
					updateTransparency(view, dynNetwork.getNode(interval.getAttribute().getKey().getRow()),offCounter);
				else
					updateTransparency(view, dynNetwork.getNode(interval.getAttribute().getKey().getRow()),onCounter);

			for (DynInterval<T> interval : edges)
				if (interval.isOn())
					updateTransparency(view, dynNetwork.getEdge(interval.getAttribute().getKey().getRow()),offCounter);
				else
					updateTransparency(view, dynNetwork.getEdge(interval.getAttribute().getKey().getRow()),onCounter);

			timeEnd = System.currentTimeMillis();
			if (timeEnd-timeStart<delay)
				try {
					Thread.sleep(delay-(int) (timeEnd-timeStart));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				view.updateView();
		}
		
		for (DynInterval<T> interval : nodes)
			if (interval.isOn())
				updateTransparency(view, dynNetwork.getNode(interval.getAttribute().getKey().getRow()),255);
			else
				updateTransparency(view, dynNetwork.getNode(interval.getAttribute().getKey().getRow()),visibility);
		
		view.updateView();
	}

	/**
	 * Run transformation on given interval lists.
	 * @param <T>
	 * @param interval list changedNodes
	 * @param interval list changedEdges
	 * @param interval list changedNodePositions
	 */
	@SuppressWarnings("unchecked")
	public <T> void run(
			final DynNetwork<T> dynNetwork,
			final DynNetworkView<T> view,
			final DynInterval<T> timeInterval,
			final DynLayout layout,
			final int visibility)
	{
		this.onCounter = 255;
		this.offCounter = visibility;
		
		List<DynInterval<T>> nodes = dynNetwork.searchChangedNodes(timeInterval);
		List<DynInterval<T>> edges = dynNetwork.searchChangedEdges(timeInterval);
		List<DynInterval<Double>> nodesPosX = layout.searchChangedNodePositionsX((DynInterval<Double>) timeInterval);
		List<DynInterval<Double>> nodesPosY = layout.searchChangedNodePositionsY((DynInterval<Double>) timeInterval);

		if (nodes.isEmpty() && edges.isEmpty() && nodesPosX.isEmpty() && nodesPosY.isEmpty())
			return;
		
		for (int i=0;i<iterations;i++)
		{
			timeStart = System.currentTimeMillis();
			
			onCounter = (int) ((1-alpha)*onCounter+alpha*visibility);
			offCounter = (int) ((1-alpha)*offCounter+alpha*255);

			for (DynInterval<T> interval : nodes)
				if (interval.isOn())
					updateTransparency(view, dynNetwork.getNode(interval.getAttribute().getKey().getRow()),offCounter);
				else
					updateTransparency(view, dynNetwork.getNode(interval.getAttribute().getKey().getRow()),onCounter);

			for (DynInterval<T> interval : edges)
				if (interval.isOn())
					updateTransparency(view, dynNetwork.getEdge(interval.getAttribute().getKey().getRow()),offCounter);
				else
					updateTransparency(view, dynNetwork.getEdge(interval.getAttribute().getKey().getRow()),onCounter);

			for (DynInterval<Double> interval : nodesPosX)
					updatePositionX(view,dynNetwork.getNode(interval.getAttribute().getKey().getRow()),interval.getAttribute().getKey().getColumn(),(Double)interval.getValue());
			
			for (DynInterval<Double> interval : nodesPosY)
				updatePositionY(view,dynNetwork.getNode(interval.getAttribute().getKey().getRow()),interval.getAttribute().getKey().getColumn(),(Double)interval.getValue());

			timeEnd = System.currentTimeMillis();
			if (timeEnd-timeStart<delay)
				try {
					Thread.sleep(delay-(int) (timeEnd-timeStart));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				view.updateView();
		}
		
		for (DynInterval<T> interval : nodes)
			if (interval.isOn())
				updateTransparency(view, dynNetwork.getNode(interval.getAttribute().getKey().getRow()),255);
			else
				updateTransparency(view, dynNetwork.getNode(interval.getAttribute().getKey().getRow()),visibility);
		
		view.updateView();
	}

	private final <T> void updateTransparency(final DynNetworkView<T> view, final CyNode node, final int value)
	{
		if (node!=null)
		{
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY,value);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY,value);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY,value);
		}
	}
	
	private final <T> void updateTransparency(final DynNetworkView<T> view, final CyEdge edge, final int value)
	{
		if (edge!=null)
		{
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY,value);
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_LABEL_TRANSPARENCY,value);
		}
	}

	private final <T> void updatePositionX(final DynNetworkView<T> view, final CyNode node, final String attr, final double value)
	{
		if (node!=null)
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION, 
					(1-alpha)*view.readVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION)+alpha*value);
	}
	
	private final <T> void updatePositionY(final DynNetworkView<T> view, final CyNode node, final String attr, final double value)
	{
		if (node!=null)
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_Y_LOCATION, 
					(1-alpha)*view.readVisualProperty(node, BasicVisualLexicon.NODE_Y_LOCATION)+alpha*value);
	}

}
