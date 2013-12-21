/*
 * DynNetwork plugin for Cytoscape 3.0 (http://www.cytoscape.org/).
 * Copyright (C) 2013 Jimmy Mahesh Morzaria
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
package org.cytoscape.dyn.internal.graphMetrics;

import java.util.Comparator;

import org.cytoscape.model.CyNode;

/**
 * @author Jimmy
 *
 */
public class MyComparator implements Comparator<CyNode> {

	@Override
	public int compare(CyNode node1, CyNode node2) {
		// TODO Auto-generated method stub
		return (node2.getSUID()>node1.getSUID() ? -1 : node1.getSUID() == node2.getSUID() ? 0 : 1);
	}

}
