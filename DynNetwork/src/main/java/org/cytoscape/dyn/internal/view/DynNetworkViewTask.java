package org.cytoscape.dyn.internal.view;

import java.util.List;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * <code> DynNetworkViewTask </code> is the task that is responsible for updating
 * all attributes.
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class DynNetworkViewTask<T> extends AbstractTask 
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

		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchChangedNodes(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : intervalList)
		{
			CyNode node = dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow());
			if (node!=null)
				view.writeVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE, 
						!view.readVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE));
		}

		// update edges
		intervalList = dynNetwork.searchChangedEdges(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : intervalList)
		{
			CyEdge edge = dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow());
			if (edge!=null && edge.getSource()!=null && edge.getTarget()!=null)
				view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE, 
						!view.readVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE));
		}
		
		//update graph attributes
		intervalList = dynNetwork.searchChangedGraphsAttr(new DynInterval<T>(low, high));
		for (DynInterval<T> interval : intervalList)
		{
			dynNetwork.writeGraphTable(interval.getAttribute().getKey().getColumn(), interval.getValue());
//			System.out.println("time " + low + " set " + interval.getValue());
		}

		//update selected node attributes
		List<CyNode> nodeListSelected = CyTableUtil.getNodesInState(dynNetwork.getNetwork(),"selected",true);
		if (!nodeListSelected.isEmpty())
		{
			intervalList = dynNetwork.searchNodesAttr(new DynInterval<T>(low, high));
			for (CyNode node : nodeListSelected)
				for (DynInterval<T> interval : intervalList)
					if (nodeListSelected.contains(dynNetwork.readNodeTable(interval.getAttribute().getKey().getRow())))
					{	
						dynNetwork.writeNodeTable(node, interval.getAttribute().getKey().getColumn(), interval.getValue());
//						System.out.println("time " + low + " set " + interval.getValue());
					}
		}

		//update selected edge attributes
		List<CyEdge> edgeListSelected = CyTableUtil.getEdgesInState(dynNetwork.getNetwork(),"selected",true);
		if (!edgeListSelected.isEmpty())
		{
			intervalList = dynNetwork.searchEdgesAttr(new DynInterval<T>(low, high));
			for (CyEdge edge : edgeListSelected)
				for (DynInterval<T> interval : intervalList)
					if (edgeListSelected.contains(dynNetwork.readEdgeTable(interval.getAttribute().getKey().getRow())))
					{
						dynNetwork.writeEdgeTable(edge, interval.getAttribute().getKey().getColumn(), interval.getValue());
//						System.out.println("time " + low + " set " + interval.getValue());
					}
		}

		view.updateView();
		
		queue.unlock(); 

	}

}

