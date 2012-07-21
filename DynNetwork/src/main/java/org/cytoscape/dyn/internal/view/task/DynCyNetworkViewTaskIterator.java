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

import org.cytoscape.dyn.internal.tree.DynInterval;
import org.cytoscape.dyn.internal.view.gui.AdvancedDynCytoPanel;
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;

/**
 * <code> DynCyNetworkViewTaskIterator </code> is the task that is responsible for updating
 * the visualization of a static network {@link CyNetwork} by incrementing or 
 * decrementing the time with a given timeStep. In order to increase speed performance, only 
 * elements that changed from the last visualization are updated (by interval tree search over 
 * all elements). The dynamics of the visualization is stored in {@link DynLayout}.
 * 
 * @author sabina
 *
 * @param <T>
 * @param <C>
 */
public class DynCyNetworkViewTaskIterator<T,C> extends AbstractCyNetworkViewTask<T,C> 
{
	private final JSlider slider;
	private final int timeStep;
	
	private double time;

	public DynCyNetworkViewTaskIterator(
			final AdvancedDynCytoPanel<T,C> panel,
			final CyNetworkView view,
			final DynLayout<T> layout,
			final BlockingQueue queue,
			final double low, 
			final double high, 
			final JSlider slider,
			final int timestep) 
	{
		super(panel, view, layout, queue, low, high);
		this.slider = slider;
		this.timeStep = timestep;
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
		
		// update node positions
		List<DynInterval<T>> intervalList = layout.searchChangedNodePositions(timeInterval);
		if (!intervalList.isEmpty())
			updatePosition(intervalList, layout.getAlpha(), layout.getN());

	}


}

