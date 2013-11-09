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

package org.cytoscape.dyn.internal.vizmapper.model;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.view.model.VisualProperty;

/**
 * <code> DVisualLexicon </code> provides access to the VisualLexicon.
 * 
 * @author Sabina Sara Pfister
 *
 */
public class DVisualLexicon 
{
	
	public VisualProperty<?> EDGE_SOURCE_ARROW_UNSELECTED_PAINT;
	public VisualProperty<?> EDGE_TARGET_ARROW_UNSELECTED_PAINT;
	
	private final CyApplicationManager desktopApp;

	/**
	 * <code> DVisualLexicon </code> constructor.
	 * @param desktopApp
	 */
	public DVisualLexicon(final CyApplicationManager desktopApp) 
	{
		this.desktopApp = desktopApp;
	}
	
	/**
	 * Initialize the visual lexicon.
	 * @param engine
	 */
	public void init()
	{
		for (VisualProperty<?> vp : desktopApp.getCurrentRenderingEngine().getVisualLexicon().getAllVisualProperties())
		{
//			System.out.println("* " + vp.getIdString());
			if (vp.getIdString().equals("EDGE_SOURCE_ARROW_UNSELECTED_PAINT"))
			{
//				System.out.println("yes");
				EDGE_SOURCE_ARROW_UNSELECTED_PAINT = vp;
			}
			
			if (vp.getIdString().equals("EDGE_TARGET_ARROW_UNSELECTED_PAINT"))
				EDGE_TARGET_ARROW_UNSELECTED_PAINT = vp;
		}
	}

}
