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

import org.cytoscape.dyn.internal.io.read.util.AttributeTypeMap;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.AbstractIntervalCheck;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.values.Bend;
import org.cytoscape.view.presentation.property.values.BendFactory;
import org.cytoscape.view.presentation.property.values.HandleFactory;

/**
 * <code> EdgeGraphicsAttribute </code> is used to store node graphics attributes
 * to be added later to the visualization.
 * 
 * @author Sabina Sara Pfister
 * 
 */
public final class EdgeGraphicsAttribute<T> extends AbstractIntervalCheck<T>
{
	private final CyEdge currentEdge;
	private final String width;
	private final String fill;
	private final String sourcearrowshape;
	private final String targetarrowshape;
	private final String bend;
	private final String transparency;
	private final String start;
	private final String end;
	
	private final HandleFactory handleFactory;
	private final BendFactory bendFactory;

	
	/**
	 * <code> EdgeGraphicsAttribute </code> constructor.
	 * @param currentNetwork
	 * @param currentEdge
	 * @param width
	 * @param fill
	 * @param sourcearrowshape
	 * @param targetarrowshape
	 * @param bend
	 * @param transparency
	 * @param start
	 * @param end
	 * @param handleFactory
	 * @param bendFactory
	 */
	public EdgeGraphicsAttribute(
			final DynNetwork<T> currentNetwork,
			final CyEdge currentEdge,
			final String width,
			final String fill,
			final String sourcearrowshape,
			final String targetarrowshape,
			final String bend,
			final String transparency,
			final String start,
			final String end,
			final HandleFactory handleFactory,
			final BendFactory bendFactory)
	{
		this.currentEdge = currentEdge;
		this.width = width;
		this.fill = fill;
		this.sourcearrowshape = sourcearrowshape;
		this.targetarrowshape = targetarrowshape;
		this.bend = bend;
		this.transparency = transparency;
		this.start = start;
		this.end = end;
		this.handleFactory = handleFactory;
		this.bendFactory = bendFactory;
	}

	/**
	 * Add edge graphics attribute.
	 * @param dynNetworkView
	 * @param vizMapManager
	 * @param typeMap
	 */
	@SuppressWarnings("unchecked")
	public void add(DynNetworkView<T> dynNetworkView, DynVizMapManager<T> vizMapManager, AttributeTypeMap typeMap)
	{
		CyNetworkView view = dynNetworkView.getNetworkView();
		DynVizMap<T> vizMap = vizMapManager.getDynVizMap(view);
		
		if (width!=null)
		{
			Object attr = typeMap.getTypedValue(typeMap.getType("real"), width);
			vizMap.insertEdgeGraphics(
					currentEdge,
					(VisualProperty<T>) BasicVisualLexicon.EDGE_WIDTH,
					"GRAPHICS.edge.width",
					getIntervalAttr(dynNetworkView.getNetwork(),currentEdge,"GRAPHICS.edge.width",(T) attr ,start, end));
		}
		if (fill!=null)
		{
			Object attr = typeMap.getTypedValue(typeMap.getType("paint"), fill);
			vizMap.insertEdgeGraphics(
					currentEdge,
					(VisualProperty<T>) BasicVisualLexicon.EDGE_PAINT,
					"GRAPHICS.edge.fill",
					getIntervalAttr(dynNetworkView.getNetwork(),currentEdge,"GRAPHICS.edge.fill",(T) attr ,start, end));
			
			attr = typeMap.getTypedValue(typeMap.getType("paint"), fill);
			vizMap.insertEdgeGraphics(
					currentEdge,
					(VisualProperty<T>) BasicVisualLexicon.EDGE_UNSELECTED_PAINT,
					"GRAPHICS.edge.fill",
					getIntervalAttr(dynNetworkView.getNetwork(),currentEdge,"GRAPHICS.edge.fill",(T) attr ,start, end));
			
			attr = typeMap.getTypedValue(typeMap.getType("paint"), fill);
			vizMap.insertEdgeGraphics(
					currentEdge,
					(VisualProperty<T>) BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT,
					"GRAPHICS.edge.fill",
					getIntervalAttr(dynNetworkView.getNetwork(),currentEdge,"GRAPHICS.edge.fill",(T) attr ,start, end));
		}
		if (sourcearrowshape!=null)
		{
			Object attr = typeMap.getTypedValue(typeMap.getType("EDGE_" + sourcearrowshape), sourcearrowshape);
			vizMap.insertEdgeGraphics(
					currentEdge,
					(VisualProperty<T>) BasicVisualLexicon.EDGE_SOURCE_ARROW_SHAPE,
					"sourcearrowshape",
					getIntervalAttr(dynNetworkView.getNetwork(),currentEdge,"GRAPHICS.edge.sourcearrowshape",(T) attr ,start, end));
		}
		if (targetarrowshape!=null)
		{
			Object attr = typeMap.getTypedValue(typeMap.getType("EDGE_" + targetarrowshape), targetarrowshape);
			vizMap.insertEdgeGraphics(
					currentEdge,
					(VisualProperty<T>) BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE,
					"targetarrowshape",
					getIntervalAttr(dynNetworkView.getNetwork(),currentEdge,"GRAPHICS.edge.targetarrowshape",(T) attr ,start, end));
		}
		if (bend!=null)
		{
			Object attr = (Object) bendFactory.createBend();
			((Bend) attr).insertHandleAt(0, handleFactory.createHandle(
					dynNetworkView.getNetworkView(), 
					dynNetworkView.getNetworkView().getEdgeView(this.currentEdge), 0, 0));
			
//			Double.parseDouble(bend)
			
			vizMap.insertEdgeGraphics(
					currentEdge,
					(VisualProperty<T>) BasicVisualLexicon.EDGE_BEND,
					"bend",
					getIntervalAttr(dynNetworkView.getNetwork(),currentEdge,"GRAPHICS.edge.bend",(T) attr ,start, end));
		}
		if (transparency!=null)
		{
			Object attr = typeMap.getTypedValue(typeMap.getType("integer"), transparency);
			vizMap.insertEdgeGraphics(
					currentEdge,
					(VisualProperty<T>) BasicVisualLexicon.EDGE_TRANSPARENCY,
					"GRAPHICS.edge.transparency",
					getIntervalAttr(dynNetworkView.getNetwork(),currentEdge,"GRAPHICS.edge.transparency",(T) attr ,start, end));
		}
	}
	
}
