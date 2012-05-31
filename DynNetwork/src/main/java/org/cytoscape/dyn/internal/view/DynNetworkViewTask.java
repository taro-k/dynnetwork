package org.cytoscape.dyn.internal.view;

import java.util.Iterator;

import org.cytoscape.dyn.internal.action.DynNetworkEventManagerImpl;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.util.KeyPairs;
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
public class DynNetworkViewTask<T> extends AbstractTask {
	
	private final DynNetworkViewSync<T> view;
	private final DynNetworkEventManagerImpl<T> manager;
	private final BlockingQueue queue;
	private final double low;
	private final double high;

	public DynNetworkViewTask(
			final DynNetworkViewSync<T> view,
			final DynNetworkEventManagerImpl<T> manager,
			final BlockingQueue queue,
			double low, double high) {
		this.view = view;
		this.manager = manager;
		this.queue = queue;
		this.low = low;
		this.high = high;
	}

	// Here we update dynamically the network data and the view (now thread safe)

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {

		queue.lock(); 

//		 Iterate over nodes and nodes attributes and update changes
		Iterator<KeyPairs> keyItrN = manager.getDynNodesAttr().getDynTable().keySet().iterator();
		while (keyItrN.hasNext())
		{
			KeyPairs key = keyItrN.next();
			CyNode node = view.readNodeTable(key.getRow());
			if (node!=null)
			{
				if(key.getColumn().equals("name") 
						&& manager.checkNode(node, new DynInterval<T>(low, high))==
							!view.readVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE))
				{
					view.writeVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE, 
							!view.readVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE));
				}
				else if (!key.getColumn().equals("name"))
					view.writeNodeTable(node, key.getColumn(), manager.getDynNodesAttr(node, key.getColumn(), new DynInterval<T>(low, high)));
			}
		}

//		 Iterate over edges and edges attributes and update changes
		Iterator<KeyPairs> keyItrE = manager.getDynEdgesAttr().getDynTable().keySet().iterator();
		while (keyItrE.hasNext())
		{
			KeyPairs key = keyItrE.next();
			CyEdge edge = view.readEdgeTable(key.getRow());
			if (edge!=null)
			{
				if(key.getColumn().equals("name") 
						&& manager.checkEdge(edge, new DynInterval<T>(low, high))==
							!view.readVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE))
				{
					view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE, 
							!view.readVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE));
				}
				else if (!key.getColumn().equals("name"))
					view.writeEdgeTable(edge, key.getColumn(), manager.getDynEdgesAttr(edge, key.getColumn(), new DynInterval<T>(low, high)));
			}
		}

		view.updateView();

		queue.unlock(); 

	}

}

