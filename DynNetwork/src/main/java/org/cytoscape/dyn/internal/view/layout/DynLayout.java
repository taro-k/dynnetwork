package org.cytoscape.dyn.internal.view.layout;

import java.util.List;

import org.cytoscape.dyn.internal.tree.DynInterval;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;

public interface DynLayout<T>
{
	/**
	 * Insert node position X.
	 * @param node
	 * @param interval
	 */
	public void insertNodePositionX(CyNode node, DynInterval<T> interval);
	
	/**
	 * Insert node position Y.
	 * @param node
	 * @param interval
	 */
	public void insertNodePositionY(CyNode node, DynInterval<T> interval);
	
	/**
	 * Insert node position Z.
	 * @param node
	 * @param interval
	 */
	public void insertNodePositionZ(CyNode node, DynInterval<T> interval);

	/**
	 * Remove intervals and attributes belonging to node.
	 * @param node
	 */
	public void removeNode(CyNode node);
	
	/**
	 * Remove all node position intervals.
	 */
	public void removeAllIntervals();

	/**
	 * Get all position intervals for node.
	 * @param node
	 * @return
	 */
	public List<DynInterval<T>> getIntervals(CyNode node);

	/**
	 * Search all positions of visible nodes
	 * @param interval
	 * @return
	 */
	public List<DynInterval<T>> searchNodePositions(DynInterval<T> interval);
	
	/**
	 * Search all positions of visible nodes that changed from the last time interval.
	 * @param interval
	 * @return
	 */
	public List<DynInterval<T>> searchChangedNodePositions(DynInterval<T> interval);

	/**
	 * Search positions of not visible nodes.
	 * @param interval
	 * @return
	 */
	public List<DynInterval<T>> searchNodePositionsNot(DynInterval<T> interval);
	
	/**
	 * Initialize node positions.
	 * @param time
	 */
	public void initNodePositions(double time);
	
	/**
	 * Get network view.
	 * @return view
	 */
	public CyNetworkView getNetworkView();
}
