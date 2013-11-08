package org.cytoscape.dyn.internal.view.task;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.model.tree.DynIntervalDouble;
import org.cytoscape.dyn.internal.view.gui.DynCytoPanelImpl;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;

/**
 * <code> DynNetworkViewRefreshTask </code> is the task that is responsible for refreshing
 * the visualization of a dynamic network {@link DynNetwork}.
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public class DynNetworkViewRefreshTask<T,C> extends AbstractDynNetworkViewTask<T,C> 
{
	private double time;
	
	/**
	 * <code> DynNetworkViewTask </code> constructor.
	 * @param panel
	 * @param view
	 * @param layout
	 * @param queue
	 */
	public DynNetworkViewRefreshTask(
			final DynCytoPanelImpl<T,C> panel,
			final DynNetworkView<T> view,
			final Transformator<T> transformator,
			final BlockingQueue queue) 
	{
		super(panel, view, transformator, queue);
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

		// refresh node and edges visual properties
		transformator.refresh(dynNetwork,view,timeInterval);
		
		view.updateView();
		
		queue.unlock(); 
	}
	
	@SuppressWarnings("unchecked")
	private void setParameters()
	{
		this.time = this.panel.getTime();
		if (time>=panel.getMaxTime())
			timeInterval = (DynInterval<T>) new DynIntervalDouble(time-0.0000001, time-0.0000001);
		else
			timeInterval = (DynInterval<T>) new DynIntervalDouble(time, time);
	}

}

