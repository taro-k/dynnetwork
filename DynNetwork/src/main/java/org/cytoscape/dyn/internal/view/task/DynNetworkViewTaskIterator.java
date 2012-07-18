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
import org.cytoscape.dyn.internal.tree.DynInterval;
import org.cytoscape.dyn.internal.view.gui.AdvancedDynCytoPanel;
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;

/**
 * <code> DynNetworkViewTaskIterator </code> is the task that is responsible for updating
 * the visualization of a dynamic network {@link DynNetwork} by recursively incrementing or 
 * decrementing the time with a given timeStep. In order to increase speed performance, only 
 * elements that changed from the last visualization are updated (by interval tree search over 
 * all elements). The dynamics of the network is stored in {@link DynNetwork}, whereas the dynamics of the
 * visualization is stored in {@link DynLayout}.
 * 
 * @author sabina
 *
 * @param <T>
 * @param <C>
 */
public final class DynNetworkViewTaskIterator<T,C> extends AbstractDynNetworkViewTask<T,C>
{	
	private final JSlider slider;
	private final int timeStep;
	
	private double time;
	private int visibility;
	private int oldVisibility;
	private final double alpha;
	private final int n;
	
	public DynNetworkViewTaskIterator(
			final AdvancedDynCytoPanel<T,C> panel,
			final DynNetworkView<T> view,
			final DynLayout<T> layout,
			final BlockingQueue queue,
			final double low, 
			final double high,
			final JSlider slider,
			final int timestep,
			final double alpha,
			final int n)
	{
		super(panel, view, layout, queue, low, high);
		this.slider = slider;
		this.timeStep = timestep;
		this.visibility = this.panel.getVisibility();
		this.oldVisibility = visibility;
		this.alpha = alpha;
		this.n = n;
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
			
			oldVisibility = visibility;
			visibility = panel.getVisibility();
			if (oldVisibility!=visibility)
				updateTransparency(visibility);
			
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
		if (layout!=null)
		{
			intervalList = layout.searchChangedNodePositions(timeInterval);
			if (!intervalList.isEmpty())
				updatePosition(intervalList, alpha, n);
		}
		
		panel.setNodes(dynNetwork.getVisibleNodes());
		panel.setEdges(dynNetwork.getVisibleEdges());
	}
	
}
