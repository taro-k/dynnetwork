/*
 * DynNetwork plugin for Cytoscape 3.0 (http://www.cytoscape.org/).
 * Copyright (C) 2012 Sabina Sara Pfister
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.cytoscape.dyn.internal.view.layout;

import java.util.List;

import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;

/**
 * <code> DynLayout </code> is the interface to represent the dynamics in time
 * of a {@link CyNetworkView} and is implemented by {@link DynLayoutImpl}.
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public interface DynLayout
{
	/**
	 * Insert node position X.
	 * @param node
	 * @param interval
	 */
	public void insertNodePositionX(CyNode node, DynInterval<Double> interval);
	
	/**
	 * Insert node position Y.
	 * @param node
	 * @param interval
	 */
	public void insertNodePositionY(CyNode node, DynInterval<Double> interval);
	
	/**
	 * Insert node position Z.
	 * @param node
	 * @param interval
	 */
	public void insertNodePositionZ(CyNode node, DynInterval<Double> interval);

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
	public List<DynInterval<Double>> getIntervals(CyNode node);

	/**
	 * Search all positions of visible nodes
	 * @param interval
	 * @return
	 */
	public List<DynInterval<Double>> searchNodePositions(DynInterval<Double> interval);
	
	/**
	 * Search all positions of visible nodes that changed from the last time interval.
	 * @param interval
	 * @return
	 */
	public List<DynInterval<Double>> searchChangedNodePositions(DynInterval<Double> interval);

	/**
	 * Search positions of not visible nodes.
	 * @param interval
	 * @return
	 */
	public List<DynInterval<Double>> searchNodePositionsNot(DynInterval<Double> interval);
	
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

	/**
	 * Get constant alpha (position updating speed).
	 * @return
	 */
	double getAlpha();

	/**
	 * Set constant alpha (position updating speed).
	 * @param alpha
	 */
	void setAlpha(double alpha);

	/**
	 * Get constant n (position updating iterations).
	 * @return
	 */
	int getN();

	/**
	 * Set constant n (position updating iterations).
	 * @param n
	 */
	void setN(int n);

	
}
