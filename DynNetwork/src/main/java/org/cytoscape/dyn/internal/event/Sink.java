package org.cytoscape.dyn.internal.event;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;


/**
 * <code> Sink </code> receives graph events (elements and attributes), but does not
 * contain a graph instance. Graph listeners are called each time a graph element (node
 * or edge) or attributed is added, changed or removed.
 *  
 * @author sabina
 * 
 * @param <T>
 */
public interface Sink<T>
{
	/**
	 * Process added graph event.
	 * @param id
	 * @param label
	 * @param start
	 * @param end
	 * @param directed
	 * @return
	 */
	public DynNetwork<T> addedGraph(String id, String label, String start, String end, String directed);
	
	/**
	 * Process added node event.
	 * @param dynNetwork
	 * @param group
	 * @param id
	 * @param label
	 * @param start
	 * @param end
	 * @return
	 */
	public CyNode addedNode(DynNetwork<T> dynNetwork, CyGroup group, String id, String label, String start, String end);
	
	/**
	 * Process added edge event.
	 * @param dynNetwork
	 * @param id
	 * @param label
	 * @param source
	 * @param target
	 * @param start
	 * @param end
	 * @return
	 */
	public CyEdge addedEdge(DynNetwork<T> dynNetwork, String id, String label, String source, String target, String start, String end);
	
	/**
	 * Process added group event.
	 * @param dynNetwork
	 * @param currentNode
	 * @return
	 */
	public CyGroup addedGroup(DynNetwork<T> dynNetwork, CyNode currentNode);
	
	/**
	 * Process added graph attribute event.
	 * @param dynNetwork
	 * @param name
	 * @param value
	 * @param Type
	 * @param start
	 * @param end
	 */
	public void addedGraphAttribute(DynNetwork<T> dynNetwork, String name, String value, String Type, String start, String end);
	
	/**
	 * Process added node attribute event.
	 * @param dynNetwork
	 * @param currentNode
	 * @param name
	 * @param value
	 * @param Type
	 * @param start
	 * @param end
	 */
	public void addedNodeAttribute(DynNetwork<T> dynNetwork, CyNode currentNode, String name, String value, String Type, String start, String end);
	
	/**
	 * Process added edge attribute event.
	 * @param dynNetwork
	 * @param currentEdge
	 * @param name
	 * @param value
	 * @param Type
	 * @param start
	 * @param end
	 */
	public void addedEdgeAttribute(DynNetwork<T> dynNetwork, CyEdge currentEdge, String name, String value, String Type, String start, String end);
	
	/**
	 * Process deleted graph event.
	 * @param dynNetwork
	 */
	public void deletedGraph(DynNetwork<T> dynNetwork);

	/**
	 * Process deleted node event.
	 * @param dynNetwork
	 * @param node
	 */
	public void deletedNode(DynNetwork<T> dynNetwork, CyNode node);
	
	/**
	 * Process deleted edge event.
	 * @param dynNetwork
	 * @param edge
	 */
	public void deletedEdge(DynNetwork<T> dynNetwork, CyEdge edge);
	
	/**
	 * Process finalize network event.
	 * @param dynNetwork
	 */
	public void finalizeNetwork(DynNetwork<T> dynNetwork);
	
	/**
	 * Process create view event.
	 * @param dynNetwork
	 * @return
	 */
	public DynNetworkView<T> createView(DynNetwork<T> dynNetwork);
}
