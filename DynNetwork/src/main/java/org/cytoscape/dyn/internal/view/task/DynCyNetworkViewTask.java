package org.cytoscape.dyn.internal.view.task;

import java.util.List;

import org.cytoscape.dyn.internal.tree.DynInterval;
import org.cytoscape.dyn.internal.view.gui.AdvancedDynCytoPanel;
import org.cytoscape.dyn.internal.view.layout.DynLayout;
import org.cytoscape.view.model.CyNetworkView;

public class DynCyNetworkViewTask<T,C> extends AbstractCyNetworkViewTask<T,C> 
{
	private final double alpha;
	private final int n;
	
	private boolean updateNodes = true;

	public DynCyNetworkViewTask(
			final AdvancedDynCytoPanel<T,C> panel,
			final CyNetworkView view,
			final DynLayout<T> layout,
			final BlockingQueue queue,
			final double low, 
			final double high, 
			final int visibility) 
	{
		super(panel, view, layout, queue, low, high);
		this.alpha = 0.2;
		this.n = 15;
	}

	@Override
	public void run() 
	{
		queue.lock();
		
		if (this.cancelled==true)
			updateNodes=false;
		
		timeInterval = new DynInterval<T>(low, high);
		
		// update node positions
		if (layout!=null && this.updateNodes)
		{
			List<DynInterval<T>> intervalList = layout.searchChangedNodePositions(timeInterval);
			if (!intervalList.isEmpty())
				updatePosition(intervalList, alpha, n);
		}
		
		view.updateView();
		
		queue.unlock(); 
	}

}

