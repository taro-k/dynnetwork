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
 * <code> Source </code> produces graph events (elements and attributes), but does not
 * contain a graph instance.
 *  
 * @author Sabina Sara Pfister
 * 
 * @param <T>
 * 
 */
public interface Source<T>
{
	/**
	 * Add a {@link Sink}.
	 * @param sink
	 */
	void addSink(Sink<T> sink);
	
	/**
	 * Remove a {@link Sink}.
	 * @param sink
	 */
	void removeSink(Sink<T> sink);
	
	/**
	 * Remove all {@link Sink}.
	 */
	void removeSinks();
}
