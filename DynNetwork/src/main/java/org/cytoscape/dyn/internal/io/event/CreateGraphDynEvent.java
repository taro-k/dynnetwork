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

/**
 * <code> CreateGraphDynEvent </code> implements the interface {@link DynEvent}
 * for the generation of graph events.
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class CreateGraphDynEvent<T> implements DynEvent
{
	private final String id;
	private final String label;
	private final String start;
	private final String end;
	private final String directed;
	
	private final Sink<T> sink;

	public CreateGraphDynEvent(
			final String sourceId, 
			final double timeStamp, 
			final String id,
			final String label, 
			final String start, 
			final String end, 
			final String directed,
			final Sink<T> sink) 
	{
		this.id = id;
		this.label = label;
		this.start = start;
		this.end = end;
		this.directed = directed;
		this.sink = sink;
	}
	
	@Override
	public Object call() throws Exception
	{
		return sink.addedGraph(id, label, start, end, directed);
	}

}
