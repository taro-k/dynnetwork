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
import org.cytoscape.dyn.internal.view.gui.DynCytoPanelImpl;
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

/**
 * <code> DynNetworkViewTaskIterator </code> is the task that is responsible for updating
 * the visualization of a dynamic network {@link DynNetwork} by recursively incrementing or 
 * decrementing the time with a given timeStep. In order to increase speed performance, only 
 * elements that changed from the last visualization are updated (by interval tree search over 
 * all elements). The dynamics of the network is stored in {@link DynNetwork}, whereas the dynamics of the
 * visualization is stored in {@link DynLayout}.
 * 
 * @author Sabina Sara Pfister
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
	private int smoothness;
	
	/**
	 * <code> DynNetworkViewTaskIterator </code> constructor.
	 * @param panel
	 * @param view
	 * @param layout
	 * @param queue
	 * @param slider
	 * @param timestep
	 */
	public DynNetworkViewTaskIterator(
			final DynCytoPanelImpl<T,C> panel,
			final DynNetworkView<T> view,
			final DynLayout<T> layout,
			final Transformator transformator,
			final BlockingQueue queue,
			final JSlider slider,
			final int timestep)
	{
		super(panel, view, layout, transformator, queue);
		this.slider = slider;
		this.timeStep = timestep;
	}

	@Override
	public void run() 
	{
		queue.lock();
		
		if (this.cancelled==true)
		{
			queue.unlock();	
			return;
		}
		
		setParameters();
		
		panel.setValueIsAdjusting(true);
		
		while((timeStep>0 && slider.getValue()<slider.getMaximum()) || (timeStep<0 && slider.getValue()>0))
		{			
			timeStart = System.currentTimeMillis();
			
			smoothness = panel.getSmoothness();
			updateNetwork();

			oldVisibility = visibility;
			visibility = panel.getVisibility();
			if (oldVisibility!=visibility)
				updateTransparency(visibility);
			
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
		if (time>=panel.getMaxTime())
			timeInterval = new DynInterval<T>(time-0.0000001, time+0.0000001);
		else
			timeInterval = new DynInterval<T>(time, time);
		
		// update graph attributes
		for (DynInterval<T> interval : view.searchChangedGraphsAttr(timeInterval))
			updateAttr(interval);
		
		// update node attributes
		for (DynInterval<T> interval : view.searchChangedNodesAttr(timeInterval))
			updateAttr(dynNetwork.getNode(interval),interval);

		// update edge attributes
		for (DynInterval<T> interval : view.searchChangedEdgesAttr(timeInterval))
			updateAttr(dynNetwork.getEdge(interval),interval);
		
		// update node and edges visual properties
		if (layout!=null)
			transformator.run(dynNetwork,view,timeInterval,layout,visibility,smoothness);
		else
			transformator.run(dynNetwork,view,timeInterval,visibility,smoothness);
		
		panel.setNodes(view.getVisibleNodes());
		panel.setEdges(view.getVisibleEdges());
	}
	
	public void updateTransparency(int visibility) 
	{
		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchNodesNot(timeInterval);
		for (DynInterval<T> interval : intervalList)
			setTransparency(dynNetwork.getNode(interval), visibility);

		// update edges
		intervalList = dynNetwork.searchEdgesNot(timeInterval);
		for (DynInterval<T> interval : intervalList)
			setTransparency(dynNetwork.getEdge(interval), visibility);

		view.updateView();
		
	}
	
	private void setTransparency(CyNode node, int visibility)
	{
		if (node!=null)
		{
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY,visibility);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY,visibility);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY,visibility);
		}
	}

	private void setTransparency(CyEdge edge, int visibility)
	{
		if (edge!=null)
		{
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY,visibility);
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_LABEL_TRANSPARENCY,visibility);
		}
	}
	
	private void setParameters()
	{
		this.time = this.panel.getTime();
		if (time>=panel.getMaxTime())
			timeInterval = new DynInterval<T>(time-0.0000001, time+0.0000001);
		else
			timeInterval = new DynInterval<T>(time, time);
		
		this.visibility = this.panel.getVisibility();
		this.oldVisibility = visibility;
		this.smoothness = panel.getSmoothness();
	}
	
}
