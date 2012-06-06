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
	public void insertGraph(String column, DynInterval<T> interval);
	
	public void insertNode(CyNode node, String column, DynInterval<T> interval);
	
	public void insertEdge(CyEdge ede, String column, DynInterval<T> interval);
	
	public void insertGraphAttr(String column, DynInterval<T> interval);
	
	public void insertNodeAttr(CyNode node, String column, DynInterval<T> interval);
	
	public void insertEdgeAttr(CyEdge ede, String column, DynInterval<T> interval);
	
	public void removeGraph();
	
	public void removeNode(CyNode node);
	
	public void removeEdge(CyEdge edge);
	
	public void removeGraphAttr();
	
	public void removeNodeAttr(CyNode node);
	
	public void removeEdgeAttr(CyEdge edge);
	
	public List<DynInterval<T>> searchGraphs(DynInterval<T> interval);
	
	public List<DynInterval<T>> searchNodes(DynInterval<T> interval);
	
	public List<DynInterval<T>> searchChangedNodes(DynInterval<T> interval);
	
	public List<DynInterval<T>> searchEdges(DynInterval<T> interval);
	
	public List<DynInterval<T>> searchChangedEdges(DynInterval<T> interval);
	
	public List<DynInterval<T>> searchNodesAttr(DynInterval<T> interval);
	
	public List<DynInterval<T>> searchChangedNodesAttr(DynInterval<T> interval);
	
	public List<DynInterval<T>> searchEdgesAttr(DynInterval<T> interval);
	
	public List<DynInterval<T>> searchChangedEdgesAttr(DynInterval<T> interval);
	
	public DynAttribute<T> getDynAttribute(CyNetwork network, String column);
	
	public DynAttribute<T> getDynAttribute(CyNode node, String column);
	
	public DynAttribute<T> getDynAttribute(CyEdge edge, String column);
	
    public void collapseAllGroups();
    
    public void expandAllGroups();
	
	public CyNetwork getNetwork();
	
	public long getCyNode(String id);
	
	public long getCyEdge(String id);
	
	public boolean containsCyNode(String id);
	
	public boolean containsCyEdge(String id);
	
	public void setCyNode(String id, long value);
	
	public void setCyEdge(String id, long value);
	
	public CyNode readNodeTable(long key);
	
	public void writeNodeTable(CyNode node, String name, T value);
	
	public CyEdge readEdgeTable(long key);
	
	public void writeEdgeTable(CyEdge edge, String name, T value);
	
	public double getMinTime();
	
	public double getMaxTime();
	
	public void print();

}
