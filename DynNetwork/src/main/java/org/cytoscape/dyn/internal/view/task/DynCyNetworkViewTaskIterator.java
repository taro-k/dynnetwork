package org.cytoscape.dyn.internal.view.task;

import java.util.List;

import javax.swing.JSlider;

import org.cytoscape.dyn.internal.tree.DynInterval;
import org.cytoscape.dyn.internal.view.gui.AdvancedDynCytoPanel;
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.view.model.CyNetworkView;

public class DynCyNetworkViewTaskIterator<T,C> extends AbstractCyNetworkViewTask<T,C> 
{
	private final JSlider slider;
	private final int timeStep;
	
	private double time;
	private final double alpha;
	private final int n;

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
		this.alpha = 0.2;
		this.n = 15;
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
			updatePosition(intervalList, alpha, n);

	}


}

