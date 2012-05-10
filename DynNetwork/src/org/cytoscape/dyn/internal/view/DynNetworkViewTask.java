package org.cytoscape.dyn.internal.view;

import java.util.Iterator;

import org.cytoscape.dyn.internal.attributes.DynInterval;
import org.cytoscape.dyn.internal.events.DynNetworkEventManagerImpl;
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

//		 Iterate over edges and edges attributes and update changes
		Iterator<KeyPairs> keyItrE = manager.getDynEdgesAttr().getDynTable().keySet().iterator();
		while (keyItrE.hasNext())
		{
			KeyPairs key = keyItrE.next();
			CyEdge edge = view.readEdgeTable(key.getRow());
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
		
		
		// alternative code (throws exception in SimpleNetwork, when iterating over the edges)
		
//		List<CyNode> nodeList = view.getNodes();
//		
//		for (CyNode node : nodeList)
//		{
//			if (manager.checkNode(node, new DynInterval<T>(low, high))!=
//				view.readVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE))
//			{
//				view.writeVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE, 
//						!view.readVisualProperty(node, BasicVisualLexicon.NODE_VISIBLE));
//			}
//
//			String columnName;
//			Iterator<String> attrIterator = view.getNodeAttributes(node);
//			while (attrIterator.hasNext())
//			{
//				columnName = attrIterator.next();
//				if (columnName!="name")
//					view.writeNodeTable(node, columnName, manager.getDynNodesAttr(node, columnName, new DynInterval<T>(low, high)));
//			}
//		}
//
//		
//		
//		List<CyEdge> edgeList = view.getEdges();
//		
//		for (CyEdge edge : edgeList)
//		{
//			if (manager.checkEdge(edge, new DynInterval<T>(low, high))!=
//				view.readVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE))
//			{
//				view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE, 
//						!view.readVisualProperty(edge, BasicVisualLexicon.EDGE_VISIBLE));
//			}
//			
//			String columnName;
//			Iterator<String> attrIterator = view.getEdgeAttributes(edge);
//			while (attrIterator.hasNext())
//			{
//				columnName = attrIterator.next();
//				if (columnName!="name")
//					view.writeEdgeTable(edge, columnName, manager.getDynEdgesAttr(edge, columnName, new DynInterval<T>(low, high)));
//			}
//		}

		view.updateView();

		queue.unlock(); 

	}

}

