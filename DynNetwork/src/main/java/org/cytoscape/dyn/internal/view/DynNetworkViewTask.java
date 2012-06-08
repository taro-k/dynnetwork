package org.cytoscape.dyn.internal.view;

import java.util.List;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.util.KeyPairs;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * This Class is used to update the network structure and the visualization depending on the
 * desired time range
 * @author sabina
 *
 * @param <T>
 */
public class DynNetworkViewTask<T> extends AbstractTask 
{
	private final DynNetworkView<T> view;
	private final DynNetwork<T> dynNetwork;
	private final BlockingQueue queue;
	private final double low;
	private final double high;

	public DynNetworkViewTask(
			final DynNetworkView<T> view,
			final DynNetwork<T> dynNetwork,
			final BlockingQueue queue,
			double low, double high) 
	{
		this.view = view;
		this.dynNetwork = dynNetwork;
		this.queue = queue;
		this.low = low;
		this.high = high;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception 
	{
		queue.lock(); 

		List<DynInterval<T>> nodeList = dynNetwork.searchChangedNodes(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : nodeList)
		{
			KeyPairs key = interval.getAttribute().getKey();
			CyNode node = dynNetwork.readNodeTable(key.getRow());

			if (node!=null)
				view.writeVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE, 
						!view.readVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE));
		}

		
		List<DynInterval<T>> edgeList = dynNetwork.searchChangedEdges(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : edgeList)
		{
			KeyPairs key = interval.getAttribute().getKey();
			CyEdge edge = dynNetwork.readEdgeTable(key.getRow());

			if (edge!=null)
				view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE, 
						!view.readVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE));
		}

		view.updateView();
		queue.unlock(); 

	}

}

