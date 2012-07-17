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

package org.cytoscape.dyn.internal.view.model;

import java.util.List;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
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
	 * Read visual property of node.
	 * @param node
	 * @param vp
	 * @return visual property
	 */
	public double readVisualProperty(CyNode node, VisualProperty<Double> vp);

	/**
	 * Write visual property of node.
	 * @param node
	 * @param vp
	 * @param value
	 */
	public void writeVisualProperty(CyNode node, VisualProperty<Integer> vp, int value);
	
	/**
	 * Write visual property of node.
	 * @param node
	 * @param vp
	 * @param value
	 */
	public void writeVisualProperty(CyNode node, VisualProperty<Double> vp, double value);
	
	/**
	 * Write locked visual property of node.
	 * @param node
	 * @param vp
	 * @param value
	 */
	public void writeLockedVisualProperty(CyNode node, VisualProperty<Integer> vp, int value);
	
	/**
	 * Write locked visual property of node.
	 * @param node
	 * @param vp
	 * @param value
	 */
	public void writeLockedVisualProperty(CyNode node, VisualProperty<Double> vp, double value);

	/**
	 * Read visual property of edge.
	 * @param edge
	 * @param vp
	 * @return visual property
	 */
	public int readVisualProperty(CyEdge edge, VisualProperty<Integer> vp);
	
	/**
	 * Read visual property of edge.
	 * @param edge
	 * @param vp
	 * @return visual property
	 */
	public double readVisualProperty(CyEdge edge, VisualProperty<Double> vp);
	
	/**
	 * Write visual property of edge.
	 * @param edge
	 * @param vp
	 * @param value
	 */
	public void writeVisualProperty(CyEdge edge, VisualProperty<Integer> vp, int value);
	
	/**
	 * Write visual property of edge.
	 * @param edge
	 * @param vp
	 * @param value
	 */
	public void writeVisualProperty(CyEdge edge, VisualProperty<Double> vp, double value);
	
	/**
	 * Write locked visual property of edge.
	 * @param edge
	 * @param vp
	 * @param value
	 */
	public void writeLockedVisualProperty(CyEdge edge, VisualProperty<Integer> vp, int value);
	
	/**
	 * Write locked visual property of edge.
	 * @param edge
	 * @param vp
	 * @param value
	 */
	public void writeLockedVisualProperty(CyEdge edge, VisualProperty<Double> vp, double value);

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
	 * Initialize transparency values.
	 * @param visibility
	 */
	public void initTransparency(int visibility);
	
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
