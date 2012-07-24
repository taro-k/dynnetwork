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

package org.cytoscape.dyn.internal.io.event;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyNode;

/**
 * <code> CreateNodeAttrDynEvent </code> implements the interface {@link DynEvent}
 * for the generation of node attribute events.
 * 
 * @author sabina
 *
 * @param <T>
 */
public class CreateNodeAttrDynEvent<T> implements DynEvent
{
	private final DynNetwork<T> network;
	private final CyNode node;
	private final String name;
	private final String value;
	private final String type;
	private final String start;
	private final String end;
	
	private final Sink<T> sink;

	public CreateNodeAttrDynEvent(
			final DynNetwork<T> network,
			final CyNode node,
			final String name,
			final String value, 
			final String type,
			final String start, 
			final String end, 
			final Sink<T> sink) 
	{
		this.network = network;
		this.node = node;
		this.name = name;
		this.value = value;
		this.type = type;
		this.start = start;
		this.end = end;
		this.sink = sink;
	}
	
	@Override
	public Object call() throws Exception
	{
		sink.addedNodeAttribute(network, node, name, value, type, start, end);
		return null;
	}

}