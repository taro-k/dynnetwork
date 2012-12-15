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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.cytoscape.view.presentation.RenderingEngine;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

/**
 * <code> PNGWriter </code> is a PNG exporter class.
 * 
 * @author Sabina Sara Pfister
 *
 */
public class PNGWriter
{
	private final Double width;
	private final Double height;
	private final double zoom;
	
	private final RenderingEngine<?> engine;
	private final OutputStream stream;

	/**
	 * <code> PNGWriter </code> constructor.
	 * @param engine
	 * @param stream
	 */
	public PNGWriter(final RenderingEngine<?> engine, final OutputStream stream)
	{
		if (engine == null)
			throw new NullPointerException("Rendering Engine is null.");
		if (stream == null)
			throw new NullPointerException("Stream is null.");
		
		this.engine = engine;
		this.stream = stream;

		width = engine.getViewModel().getVisualProperty(BasicVisualLexicon.NETWORK_WIDTH);
		height = engine.getViewModel().getVisualProperty(BasicVisualLexicon.NETWORK_HEIGHT);
		zoom = 600;
	}

	public void export() throws IOException
	{
		final double scale = zoom / 100.0; 	
		final int heightInPixels = (int) ((zoom/100) * height);
		final int widthInPixels = (int) ((zoom/100) * width);
		final BufferedImage image = new BufferedImage(widthInPixels, heightInPixels, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.scale(scale, scale);
		engine.printCanvas(g);
		g.dispose();
		
		try {
			ImageIO.write(image, "png", stream);
		} finally {
			stream.close();
		}
		
	}

}
