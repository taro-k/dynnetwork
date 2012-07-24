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

/**
 * <code> CreateEdgeDynEvent </code> implements the interface {@link DynEvent}
 * for the generation of edge events.
 * 
 * @author sabina
 *
 * @param <T>
 */
public class CreateEdgeDynEvent<T> implements DynEvent
{
	private final DynNetwork<T> network;
	private final String id;
	private final String label;
	private final String source;
	private final String target;
	private final String start;
	private final String end;
	
	private final Sink<T> sink;

	/**
	 * <code> CreateEdgeDynEvent </code> constructor.
	 * @param network
	 * @param id
	 * @param label
	 * @param source
	 * @param target
	 * @param start
	 * @param end
	 * @param sink
	 */
	public CreateEdgeDynEvent(
			final DynNetwork<T> network,
			final String id,
			final String label, 
			final String source,
			final String target,
			final String start, 
			final String end, 
			final Sink<T> sink) 
	{
		this.network = network;
		this.id = id;
		this.label = label;
		this.source = source;
		this.target = target;
		this.start = start;
		this.end = end;
		this.sink = sink;
	}
	
	@Override
	public Object call() throws Exception
	{
		return sink.addedEdge(network, id, label, source, target, start, end);
	}

}
