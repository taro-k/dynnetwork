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

import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.gui.DynCytoPanel;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;

public final class DynNetworkViewTaskIterator<T,C> extends AbstractDynNetworkViewTask<T,C>
{	
	private final JSlider slider;
	private final int timeStep;
	
	private double time;
	private int visibility;
	private int oldVisibility;
	private final double alpha;
	private final double n;
	
	public DynNetworkViewTaskIterator(
			final DynCytoPanel<T,C> panel,
			final DynNetworkView<T> view,
			final BlockingQueue queue,
			final double low, 
			final double high,
			final JSlider slider,
			final int timestep)
	{
		super(panel, view, queue, low, high);
		this.slider = slider;
		this.timeStep = timestep;
		this.visibility = this.panel.getVisibility();
		this.oldVisibility = visibility;
		this.alpha = 0.2;
		this.n = 20;
	}

	@Override
	public void run() 
	{
		queue.lock();
		
		panel.setValueIsAdjusting(true);
		
		while((timeStep>0 && slider.getValue()<slider.getMaximum()) || (timeStep<0 && slider.getValue()>0))
		{			
			timeStart = System.currentTimeMillis();
			
			updateNetwork();
			updateTransparency();
			view.updateView();
			
			timeEnd = System.currentTimeMillis();
			
			if (timeEnd-timeStart<200)
				try {
					Thread.sleep(200-(int)(timeEnd-timeStart));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
			if (this.cancelled==true)
				break;
		}
		
		panel.setValueIsAdjusting(false);
		
		queue.unlock();	
	}

	public void cancel() 
	{
		this.cancelled = true;
	}
	
	private void updateNetwork()
	{ 
		slider.setValue(slider.getValue()+timeStep);
		time = slider.getValue()*((panel.getMaxTime()-panel.getMinTime())/panel.getSliderMax())+(panel.getMinTime());
		if (time==panel.getMaxTime())
			timeInterval = new DynInterval<T>(time-0.0000001, time+0.0000001);
		else
			timeInterval = new DynInterval<T>(time, time);
		
		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchChangedNodes(timeInterval);
		for (DynInterval<T> interval : intervalList)
			switchTransparency(dynNetwork.getNode(interval.getAttribute().getKey().getRow()), visibility);
		
		// update edges
		intervalList = dynNetwork.searchChangedEdges(timeInterval);
		for (DynInterval<T> interval : intervalList)
			switchTransparency(dynNetwork.getEdge(interval.getAttribute().getKey().getRow()), visibility);
		
		// update graph attributes
		intervalList = dynNetwork.searchChangedGraphsAttr(timeInterval);
		for (DynInterval<T> interval : intervalList)
			updateAttr(interval);
		
		// update node attributes
		intervalList = dynNetwork.searchChangedNodesAttr(timeInterval);
		for (DynInterval<T> interval : intervalList)
			updateAttr(dynNetwork.getNode(interval.getAttribute().getKey().getRow()),interval);

		// update edge attributes
		intervalList = dynNetwork.searchChangedEdgesAttr(timeInterval);
		for (DynInterval<T> interval : intervalList)
			updateAttr(dynNetwork.getEdge(interval.getAttribute().getKey().getRow()),interval);
		
		// update node positions
		intervalList = view.searchChangedNodePositions(timeInterval);
		if (!intervalList.isEmpty())
			for (int i=0;i<n;i++)
				updatePosition(intervalList, alpha);
		
		panel.setNodes(dynNetwork.getVisibleNodes());
		panel.setEdges(dynNetwork.getVisibleEdges());
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
				setTransparency(dynNetwork.getNode(interval.getAttribute().getKey().getRow()), visibility);

			// update edges
			intervalList = dynNetwork.searchEdgesNot(timeInterval);
			for (DynInterval<T> interval : intervalList)
				setTransparency(dynNetwork.getEdge(interval.getAttribute().getKey().getRow()), visibility);
		}
	}
	
}
