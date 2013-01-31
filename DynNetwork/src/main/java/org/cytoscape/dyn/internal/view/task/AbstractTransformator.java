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

package org.cytoscape.dyn.internal.view.task;

import org.cytoscape.dyn.internal.io.event.Sink;
import org.cytoscape.dyn.internal.io.event.Source;
import org.cytoscape.dyn.internal.io.write.DynNetworkViewWriterFactory;

/**
 * <code> AbstractTransformator </code> abstract class to change visual properties by interpolating
 * the values to change..
 * 
 * @author Sabina Sara Pfister
 *
 */
public abstract class AbstractTransformator<T>  implements Source<T>  
{

	protected DynNetworkViewWriterFactory<T> writerFactory;
	
	@Override
	public void addSink(Sink<T> sink) 
	{
		if (sink instanceof DynNetworkViewWriterFactory<?>)
			this.writerFactory = (DynNetworkViewWriterFactory<T>) sink;
	}
	
	@Override
	public void removeSink(Sink<T> sink) 
	{
		this.writerFactory = null;
	}
}
