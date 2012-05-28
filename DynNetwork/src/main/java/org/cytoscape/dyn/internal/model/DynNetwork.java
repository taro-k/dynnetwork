package org.cytoscape.dyn.internal.model;

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
	public CyNetwork getNetwork();
	
	public DynIntervalTree<T> getIntervalTree();
	
	public void insert(DynInterval<T> interval);
	
	public void remove(DynInterval<T> interval);
	
	public void print();

}
