package org.cytoscape.dyn.internal.model;

import org.cytoscape.dyn.internal.model.attributes.DynAttribute;
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
	public DynAttribute<T> getDynAtribute(CyNetwork network);
	
	public DynAttribute<T> getDynAtribute(CyNode node); 
	
	public DynAttribute<T> getDynAtribute(CyEdge ede);
	
	public DynAttribute<T> getDynAtribute(CyNetwork network, String attName);
	
	public DynAttribute<T> getDynAtribute(CyNode node, String attName); 
	
	public DynAttribute<T> getDynAtribute(CyEdge ede, String attName);
	
	public void insert(CyNetwork network, DynInterval<T> interval);
	
	public void insert(CyNode node, DynInterval<T> interval);
	
	public void insert(CyEdge ede, DynInterval<T> interval);
	
	public void remove(CyNetwork network, DynInterval<T> interval);
	
	public void remove(CyNode node, DynInterval<T> interval);
	
	public void remove(CyEdge ede, DynInterval<T> interval);
	
	public DynIntervalTree<T> getIntervalTree();
	
	public CyNetwork getNetwork();
	
	public void print();

}
