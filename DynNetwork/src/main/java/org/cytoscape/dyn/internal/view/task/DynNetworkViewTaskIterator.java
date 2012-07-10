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

import javax.swing.JSlider;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.gui.DynCytoPanel;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

public final class DynNetworkViewTaskIterator<T,C> implements Runnable 
{
	private final JSlider slider;
	private final int timeStep;
	private boolean cancelled = false;
	
	private final DynCytoPanel<T,C> panel;
	private final DynNetworkView<T> view;
	private final DynNetwork<T> dynNetwork;
	private final BlockingQueue queue;
	
	private double time;
	private DynInterval<T> timeInterval;
	private int visibility;
	private int oldVisibility;
	
	public DynNetworkViewTaskIterator(
			final JSlider slider,
			final int timestep,
			final DynCytoPanel<T,C> panel,
			final DynNetworkView<T> view,
			final DynNetwork<T> dynNetwork,
			final BlockingQueue queue)
	{
		super();
		this.slider = slider;
		this.timeStep = timestep;
		this.panel = panel;
		this.view = view;
		this.dynNetwork = dynNetwork;
		this.queue = queue;
		this.visibility = this.panel.getVisibility();
		this.oldVisibility = visibility;
	}

	@Override
	public void run() 
	{
		queue.lock();
		slider.setValueIsAdjusting(true);
		
		while((timeStep>0 && slider.getValue()<slider.getMaximum()) || (timeStep<0 && slider.getValue()>0))
		{			
			if (this.cancelled==true)
			{
				slider.setValueIsAdjusting(false);
				break;
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (this.cancelled==true)
			{
				slider.setValueIsAdjusting(false);
				break;
			}
			
			updateTime();
			updateNetwork();
			updateTransparency();
			view.updateView();
		}
		
		slider.setValueIsAdjusting(false);
		queue.unlock();
		
	}

	public void cancel() 
	{
		this.cancelled = true;
	}
	
	private void updateTime()
	{ 
		slider.setValue(slider.getValue()+timeStep);
		time = slider.getValue()*((panel.getMaxTime()-panel.getMinTime())/panel.getSliderMax())+(panel.getMinTime());
		if (time==panel.getMaxTime())
			timeInterval = new DynInterval<T>(time-0.0000001, time+0.0000001);
		else
			timeInterval = new DynInterval<T>(time, time);
	}
	
	private void updateNetwork()
	{ 
		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchChangedNodes(timeInterval);
		for (DynInterval<T> interval : intervalList)
			switchTransparency(dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow()));
		
		// update edges
		intervalList = dynNetwork.searchChangedEdges(timeInterval);
		for (DynInterval<T> interval : intervalList)
			switchTransparency(dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow()),interval);

		// update graph attributes
		intervalList = dynNetwork.searchChangedGraphsAttr(timeInterval);
		for (DynInterval<T> interval : intervalList)
			dynNetwork.writeGraphTable(interval.getAttribute().getKey().getColumn(), interval.getValue());

		//update selected node attributes
		List<CyNode> nodeListSelected = CyTableUtil.getNodesInState(dynNetwork.getNetwork(),"selected",true);
		if (!nodeListSelected.isEmpty())
		{
			intervalList = dynNetwork.searchNodesAttr(timeInterval);
			for (CyNode node : nodeListSelected)
				for (DynInterval<T> interval : intervalList)
					if (nodeListSelected.contains(dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow())))
						dynNetwork.writeNodeTable(node, interval.getAttribute().getKey().getColumn(), interval.getValue());
		}

		//update selected edge attributes
		List<CyEdge> edgeListSelected = CyTableUtil.getEdgesInState(dynNetwork.getNetwork(),"selected",true);
		if (!edgeListSelected.isEmpty())
		{
			intervalList = dynNetwork.searchEdgesAttr(timeInterval);
			for (CyEdge edge : edgeListSelected)
				for (DynInterval<T> interval : intervalList)
					if (edgeListSelected.contains(dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow())))
						dynNetwork.writeEdgeTable(edge, interval.getAttribute().getKey().getColumn(), interval.getValue());
		}
	}
	
	private void updateTransparency() 
	{
		oldVisibility = visibility;
		visibility = panel.getVisibility();
		
		if (oldVisibility!=visibility)
		{
			// update nodes
			List<DynInterval<T>> intervalList = dynNetwork.searchNodesNot(timeInterval);
			for (DynInterval<T> interval : intervalList)
				setTransparency(dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow()));

			// update edges
			intervalList = dynNetwork.searchEdgesNot(timeInterval);
			for (DynInterval<T> interval : intervalList)
				setTransparency(dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow()),interval);
		}
	}
	
	private void switchTransparency(CyNode node)
	{
		if (node!=null)
		{
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY,
					Math.abs(view.readVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY)-255)+visibility);
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY,
					Math.abs(view.readVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY)-255)+visibility);
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY,
					Math.abs(view.readVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY)-255)+visibility);
		}
	}

	private void switchTransparency(CyEdge edge, DynInterval<T> interval)
	{
		if (edge!=null)
			view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY,
					Math.abs(view.readVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY)-255)+visibility);
	}
	
	private void setTransparency(CyNode node)
	{
		if (node!=null)
		{
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY,visibility);
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY,visibility);
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY,visibility);
		}
	}

	private void setTransparency(CyEdge edge, DynInterval<T> interval)
	{
		if (edge!=null)
			view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY,visibility);
	}
	
}
