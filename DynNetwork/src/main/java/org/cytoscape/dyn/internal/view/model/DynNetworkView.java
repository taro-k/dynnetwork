package org.cytoscape.dyn.internal.view.model;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.VisualProperty;

/**
 * <code> DynNetworkView </code> is an object that represents a the view of a
 * dynamic network. It provides the link to the current static {@link DynNetwork}. 
 * 
 * @author sabina
 *
 * @param <T>
 */
public interface DynNetworkView<T>
{
	/**
	 * Read visual property of node.
	 * @param node
	 * @param vp
	 * @return visual property
	 */
	public int readVisualProperty(CyNode node, VisualProperty<Integer> vp);

	/**
	 * Write visual property of node.
	 * @param node
	 * @param vp
	 * @param value
	 */
	public void writeVisualProperty(CyNode node, VisualProperty<Integer> vp, int value);

	/**
	 * Read visual property of edge.
	 * @param edge
	 * @param vp
	 * @return visual property
	 */
	public int readVisualProperty(CyEdge edge, VisualProperty<Integer> vp);
	
	/**
	 * Write visual property of edge.
	 * @param edge
	 * @param vp
	 * @param value
	 */
	public void writeVisualProperty(CyEdge edge, VisualProperty<Integer> vp, int value);
	
	/**
	 * Get network view.
	 * @return view
	 */
	public CyNetworkView getNetworkView();
	
	/**
	 * Update view.
	 */
	public void updateView();

	/**
	 * Get network.
	 * @return
	 */
	public DynNetwork<T> getNetwork();
	
	/**
	 * Get current visualization time.
	 * @return time
	 */
	public double getCurrentTime();
	
	/**
	 * Set current visualization time.
	 * @param currentTime
	 */
	public void setCurrentTime(double currentTime);
}
