package org.cytoscape.dyn.internal.view.task;

import java.util.ArrayList;
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

public class DynNetworkViewTaskGroup<T> extends AbstractTask 
{
	private final DynNetworkView<T> view;
	private final DynNetwork<T> dynNetwork;
	private final BlockingQueue queue;
	private final double low;
	private final double high;
	private final CyGroup group;

	public DynNetworkViewTaskGroup(
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

		List<CyNode> nodeList = new ArrayList<CyNode>();
		List<CyEdge> edgeList = new ArrayList<CyEdge>();
		if (group.isCollapsed(dynNetwork.getNetwork()))
		{
			nodeList.add(group.getGroupNode());
			edgeList = dynNetwork.getNetwork().getAdjacentEdgeList(group.getGroupNode(), CyEdge.Type.ANY);
		}
		else
		{
			nodeList = group.getNodeList();
			edgeList = group.getInternalEdgeList();
			edgeList.addAll(group.getExternalEdgeList());
		}

		for (CyNode node : nodeList)
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE, false);
		for (CyEdge edge : edgeList)
			view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE, false);

		// update edges
		List<DynInterval<T>> intervalList = dynNetwork.searchEdges(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : intervalList)
		{
			CyEdge edge = dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow());
			if (edge!=null && edgeList.contains(edge))
				view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE, true);
			else if (dynNetwork.isMetaEdge(interval.getAttribute().getKey().getRow()) &&
					edgeList.contains(dynNetwork.isMetaEdge(interval.getAttribute().getKey().getRow())))
			{
				CyEdge metaEdge = dynNetwork.getMetaEdge(interval.getAttribute().getKey().getRow());
				view.writeVisualProperty(metaEdge, BasicVisualLexicon.EDGE_VISIBLE, true);
			}
		}
		
		// update nodes
		intervalList = dynNetwork.searchNodes(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : intervalList)
		{
			CyNode node = dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow());
			if (node!=null && nodeList.contains(node))
				view.writeVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE, true);
		}
		
//		if (group.isCollapsed(dynNetwork.getNetwork()))
//			view.viewNestedImage();

		view.updateView();
		
		queue.unlock(); 

	}

}

