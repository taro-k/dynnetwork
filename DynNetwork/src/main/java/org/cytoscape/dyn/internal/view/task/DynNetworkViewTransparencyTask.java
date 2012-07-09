package org.cytoscape.dyn.internal.view.task;

import java.util.List;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * <code> DynNetworkViewTransparencyTask </code> is responsible for updating the {@link CyNetworkView}
 * everytime the background transparency level is set to show/hide elements of the network that are
 * not in the current time interval.
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class DynNetworkViewTransparencyTask<T> extends AbstractTask 
{
	private final DynNetworkView<T> view;
	private final DynNetwork<T> dynNetwork;
	private final BlockingQueue queue;
	private final double low;
	private final double high;
	private final int visibility;

	public DynNetworkViewTransparencyTask(
			final DynNetworkView<T> view,
			final DynNetwork<T> dynNetwork,
			final BlockingQueue queue,
			final double low, final double high, final int visibility) 
	{
		this.view = view;
		this.dynNetwork = dynNetwork;
		this.queue = queue;
		this.low = low;
		this.high = high;
		this.visibility = visibility;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception 
	{
		queue.lock(); 
		
		DynInterval<T> timeInterval = new DynInterval<T>(low, high);
		
		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchNodesNot(timeInterval);
		for (DynInterval<T> interval : intervalList)
			setTransparency(dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow()));
		
		// update edges
		intervalList = dynNetwork.searchEdgesNot(timeInterval);
		for (DynInterval<T> interval : intervalList)
			setTransparency(dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow()),interval);

		view.updateView();
		
		queue.unlock(); 

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

