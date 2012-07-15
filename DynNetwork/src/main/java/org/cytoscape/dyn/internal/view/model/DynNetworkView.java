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
	 * Write locked visual property of node.
	 * @param node
	 * @param vp
	 * @param value
	 */
	public void writeLockedVisualProperty(CyNode node, VisualProperty<Integer> vp, int value);

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
	 * Write locked visual property of edge.
	 * @param edge
	 * @param vp
	 * @param value
	 */
	public void writeLockedVisualProperty(CyEdge edge, VisualProperty<Integer> vp, int value);
	
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
