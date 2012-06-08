package org.cytoscape.dyn.internal.view;

import java.util.List;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.util.KeyPairs;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * <code> DynNetworkViewAttrTask </code> is the task that is responsible for updating
 * all attributes.
 * 
 * @author sabina
 *
 * @param <T>
 */
public class DynNetworkViewAttrTask<T> extends AbstractTask 
{
	private final DynNetworkView<T> view;
	private final DynNetwork<T> dynNetwork;
	private final BlockingQueue queue;
	private final double low;
	private final double high;

	public DynNetworkViewAttrTask(
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

		List<DynInterval<T>> nodeList = dynNetwork.searchChangedNodesAttr(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : nodeList)
		{
			KeyPairs key = interval.getAttribute().getKey();
			CyNode node = dynNetwork.readNodeTable(key.getRow());

			if (node!=null)
				dynNetwork.writeNodeTable(node, key.getColumn(), interval.getValue());
		}	

		List<DynInterval<T>> edgeList = dynNetwork.searchChangedEdgesAttr(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : edgeList)
		{
			KeyPairs key = interval.getAttribute().getKey();
			CyEdge edge = dynNetwork.readEdgeTable(key.getRow());;

			if (edge!=null && edge.getSource()!=null && edge.getTarget()!=null)
				dynNetwork.writeEdgeTable(edge, key.getColumn(), interval.getValue());
		}	

		view.updateView();

		queue.unlock(); 

	}

}

