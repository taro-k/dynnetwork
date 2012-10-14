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

package org.cytoscape.dyn.internal.io.write.graphics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;

import org.cytoscape.dyn.internal.io.write.AbstractDynNetworkViewWriterFactory;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.view.presentation.RenderingEngine;

/**
 * <code> SVGWriterFactory </code> extends {@link AbstractDynNetworkViewWriterFactory}. 
 * Is used to create instance of the image writer {@link SVGWriter}.
 * 
 * @author Sabina Sara Pfister
 *
 */
public class SVGWriterFactory<T> extends AbstractDynNetworkViewWriterFactory<T> 
{
	private final RenderingEngine<?> engine;
	private final File file;
	private DecimalFormat formatter = new DecimalFormat("#0.000");

	/**
	 * <code> SVGWriterFactory </code> constructor.
	 * @param fileFilter
	 * @param engine
	 * @param stream
	 */
	public SVGWriterFactory(
			final RenderingEngine<?> engine, 
			final File file) 
	{
		if (engine == null)
			throw new NullPointerException("Rendering Engine is null.");
		
		this.engine = engine;
		this.file = file;
	}

	@Override
	public void updateView(DynNetwork<T> dynNetwork, double currentTime) 
	{
		File outputFile = new File(trim(file.getAbsolutePath()) + 
				"_" + Calendar.getInstance().getTimeInMillis() +
				"_Time_" + formatter.format(currentTime) + ".svg");

		try {
			(new SVGWriter(engine, new FileOutputStream(outputFile,false))).export();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private String trim(String str)
	{
		if (str.lastIndexOf('.')>0)
			return str.substring(0, str.lastIndexOf('.'));
		else
			return str;
	}

}
