package org.cytoscape.dyn.internal.view;

import java.util.List;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class DynNetworkViewTaskAll<T> extends AbstractTask 
{
	private final DynNetworkView<T> view;
	private final DynNetwork<T> dynNetwork;
	private final BlockingQueue queue;
	private final double low;
	private final double high;
	private final CyGroup group;

	public DynNetworkViewTaskAll(
			final DynNetworkView<T> view,
			final DynNetwork<T> dynNetwork,
			final BlockingQueue queue,
			double low, double high,
			final CyGroup group) 
	{
		this.view = view;
		this.dynNetwork = dynNetwork;
		this.queue = queue;
		this.low = low;
		this.high = high;
		this.group = group;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception 
	{
		queue.lock(); 
		
		// reset node and edges
		List<CyNode> nodeList = group.getNodeList();
		nodeList.add(group.getGroupNode());
		List<CyEdge> edgeList = group.getInternalEdgeList();
		edgeList.addAll(group.getExternalEdgeList());

		for (CyNode node : nodeList)
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE, false);
		for (CyEdge edge : edgeList)
			view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE, false);

		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchNodes(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : intervalList)
		{
			CyNode node = dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow());
			if (node!=null)
				view.writeVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE, true);
		}

		// update edges
		intervalList = dynNetwork.searchEdges(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : intervalList)
		{
			CyEdge edge = dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow());
			if (edge!=null && edge.getSource()!=null && edge.getTarget()!=null)
				view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE, true);
		}

		view.updateView();
		
		queue.unlock(); 

	}

}

