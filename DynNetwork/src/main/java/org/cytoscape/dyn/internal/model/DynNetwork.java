package org.cytoscape.dyn.internal.model;

import java.util.List;

import org.cytoscape.dyn.internal.model.tree.DynAttribute;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.model.tree.DynIntervalTree;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 * <code> DynNetwork </code> is an object that represents a dynamic network
 * composed of {@link CyNode}s, connecting {@link CyEdge}s, attributes, and
 * the respective time intervals {@link DynInterval}s. It provides the link 
 * to the current static {@link CyNewtork}. In addition it maintains the
 * information about dynamic attributes {@link DynAttribute} in form of a
 * {@link DynIntervalTree}. 
 *
 * @author sabina
 *
 */
public interface DynNetwork<T>
{
	/**
	 * Insert graph.
	 * @param column
	 * @param interval
	 */
	public void insertGraph(String column, DynInterval<T> interval);
	
	/**
	 * Insert node.
	 * @param node
	 * @param column
	 * @param interval
	 */
	public void insertNode(CyNode node, String column, DynInterval<T> interval);
	
	/**
	 * Insert edge.
	 * @param ede
	 * @param column
	 * @param interval
	 */
	public void insertEdge(CyEdge ede, String column, DynInterval<T> interval);
	
	/**
	 * Insert graph attribute.
	 * @param column
	 * @param interval
	 */
	public void insertGraphAttr(String column, DynInterval<T> interval);
	
	/**
	 * Insert node attribute.
	 * @param node
	 * @param column
	 * @param interval
	 */
	public void insertNodeAttr(CyNode node, String column, DynInterval<T> interval);
	
	/**
	 * Insert edge attribute.
	 * @param ede
	 * @param column
	 * @param interval
	 */
	public void insertEdgeAttr(CyEdge ede, String column, DynInterval<T> interval);
	
	/**
	 * Remove graph.
	 */
	public void removeGraph();
	
	/**
	 * Remove node.
	 * @param node
	 */
	public void removeNode(CyNode node);
	
	/**
	 * Remove edge.
	 * @param edge
	 */
	public void removeEdge(CyEdge edge);
	
	/**
	 * Remove graph attribute.
	 */
	public void removeGraphAttr();
	
	/**
	 * Remove node attribute.
	 * @param node
	 */
	public void removeNodeAttr(CyNode node);
	
	/**
	 * Remove edge attribute.
	 * @param edge
	 */
	public void removeEdgeAttr(CyEdge edge);

	/**
	 * Search overlapping intervals for nodes given an interval
	 * from the last search.
	 * @param interval
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchNodes(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for edges given an interval
	 * from the last search.
	 * @param interval
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchEdges(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for node attributes given an interval
	 * from the last search.
	 * @param interval
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchNodesAttr(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for edge attributes given an interval
	 * from the last search.
	 * @param interval
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchEdgesAttr(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for nodes given an interval that changed
	 * from the last search.
	 * @param interval
	 * @return list of changed overlapping intervals
	 */
	public List<DynInterval<T>> searchChangedNodes(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for edges given an interval that changed
	 * from the last search.
	 * @param interval
	 * @return list of changed overlapping intervals
	 */
	public List<DynInterval<T>> searchChangedEdges(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for node attributes given an interval that changed
	 * from the last search.
	 * @param interval
	 * @return list of changed overlapping intervals
	 */
	public List<DynInterval<T>> searchChangedNodesAttr(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for edge attributes given an interval that changed
	 * from the last search.
	 * @param interval
	 * @return list of changed overlapping intervals
	 */
	public List<DynInterval<T>> searchChangedEdgesAttr(DynInterval<T> interval);
	
	/**
	 * Get dynamic attribute for given network and name.
	 * @param network
	 * @param column
	 * @return dynamic attribute
	 */
	public DynAttribute<T> getDynAttribute(CyNetwork network, String column);
	
	/**
	 *  Get dynamic attribute for given node and name.
	 * @param node
	 * @param column
	 * @return dynamic attribute
	 */
	public DynAttribute<T> getDynAttribute(CyNode node, String column);
	
	/**
	 *  Get dynamic attribute for given edge and name.
	 * @param edge
	 * @param column
	 * @return dynamic attribute
	 */
	public DynAttribute<T> getDynAttribute(CyEdge edge, String column);
	
	/**
	 * Collapse all Groups.
	 */
    public void collapseAllGroups();
    
    /**
     * Expand all Groups.
     */
    public void expandAllGroups();
	
    /**
     * Get network.
     * @return CyNetwork
     */
	public CyNetwork getNetwork();
	
	/**
	 * Get node.
	 * @param id
	 * @return node
	 */
	public long getCyNode(String id);
	
	/**
	 * Get edge.
	 * @param id
	 * @return edge
	 */
	public long getCyEdge(String id);
	
	/**
	 * Contains node.
	 * @param id
	 * @return boolean
	 */
	public boolean containsCyNode(String id);
	
	/**
	 * Contains edge.
	 * @param id
	 * @return boolean
	 */
	public boolean containsCyEdge(String id);
	
	/**
	 * Set node.
	 * @param id
	 * @param value
	 */
	public void setCyNode(String id, long value);
	
	/**
	 * Set Edge.
	 * @param id
	 * @param value
	 */
	public void setCyEdge(String id, long value);
	
	/**
	 * Get node.
	 * @param key
	 * @return node
	 */
	public CyNode readNodeTable(long key);
	
	/**
	 * Write to node.
	 * @param node
	 * @param name
	 * @param value
	 */
	public void writeNodeTable(CyNode node, String name, T value);
	
	/**
	 * Get edge
	 * @param key
	 * @return edge
	 */
	public CyEdge readEdgeTable(long key);
	
	/**
	 * Write to edge.
	 * @param edge
	 * @param name
	 * @param value
	 */
	public void writeEdgeTable(CyEdge edge, String name, T value);
	
	/**
	 * Get minimum time.
	 * @return minimum time
	 */
	public double getMinTime();
	
	/**
	 * Get maximum time.
	 * @return maximum time
	 */
	public double getMaxTime();
	
	/**
	 * Print out.
	 */
	public void print();

}
