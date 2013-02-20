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

package org.cytoscape.dyn.internal.view.model;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;

/**
 * <code> DynNetworkViewFactoryImpl </code> implements the interface
 * {@link DynNetworkViewFactory} for creating {@link DynNetworkView}s.
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public final class DynNetworkViewFactoryImpl<T> implements DynNetworkViewFactory<T>
{
//	private final GraphicsTypeMap graphicsTypeMap;
	
	private final DynNetworkViewManager<T> viewManager;
	private final CyNetworkViewFactory cyNetworkViewFactory;
	private final CyNetworkViewManager networkViewManager;
	private final VisualMappingManager visualMappingManager;
	private final VisualStyleFactory visualStyleFactory;
	
//	private final Stack<GraphGraphicsAttribute<T>> graphGraphicsList;
//	private final Stack<NodeGraphicsAttribute<T>> nodeGraphicsList;
//	private final Stack<EdgeGraphicsAttribute<T>> edgeGraphicsList;
	
	/**
	 * <code> DynNetworkViewFactoryImpl </code> constructor.
	 * @param viewManager
	 * @param cyNetworkViewFactory
	 * @param networkViewManager
	 * @param visualMappingManager
	 * @param visualStyleFactory
	 */
	public DynNetworkViewFactoryImpl(
			DynNetworkViewManager<T> viewManager,
			CyNetworkViewFactory cyNetworkViewFactory,
			final CyNetworkViewManager networkViewManager,
			final VisualMappingManager visualMappingManager,
			final VisualStyleFactory visualStyleFactory)
	{
		this.viewManager = viewManager;
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		this.networkViewManager = networkViewManager;
		this.visualMappingManager = visualMappingManager;
		this.visualStyleFactory = visualStyleFactory;
		
//		this.graphicsTypeMap = new GraphicsTypeMap();
//		this.graphGraphicsList = new Stack<GraphGraphicsAttribute<T>>();
//		this.nodeGraphicsList = new Stack<NodeGraphicsAttribute<T>>();
//		this.edgeGraphicsList = new Stack<EdgeGraphicsAttribute<T>>();
	}
	
	@Override
	public DynNetworkView<T> createView(DynNetwork<T> dynNetwork)
	{
		DynNetworkViewImpl<T> dynNetworkView = new DynNetworkViewImpl<T>(dynNetwork, networkViewManager,cyNetworkViewFactory,visualMappingManager,visualStyleFactory);
		viewManager.addDynNetworkView(dynNetworkView);
		return dynNetworkView;
	}
	
//	@Override
//	public void addedGraphGraphics(DynNetwork<T> dynNetwork, String fill, String start, String end) 
//	{
//		this.graphGraphicsList.push(new GraphGraphicsAttribute<T>(dynNetwork,fill));
//	}
//	
//	@Override
//	public void addedNodeGraphics(DynNetwork<T> dynNetwork, CyNode currentNode, String type, String height, String width, String size, String x, String y, String fill, String linew, String outline, String start, String end) 
//	{
//		this.nodeGraphicsList.push(new NodeGraphicsAttribute<T>(dynNetwork,currentNode,type,height,width,size,x,y,fill,linew,outline,start,end));
//	}
//	
//	@Override
//	public void addedEdgeGraphics(DynNetwork<T> dynNetwork, CyEdge currentEdge, String width, String fill, String start, String end) 
//	{
//		this.edgeGraphicsList.push(new EdgeGraphicsAttribute<T>(dynNetwork,currentEdge,width,fill, start, end));
//	}
//	
//	@Override
//	public void setGraphGraphics(DynNetwork<T> dynNetwork, String fill, String start, String end)
//	{
//		CyNetworkView view = networkViewManager.getNetworkViews(dynNetwork.getNetwork()).iterator().next();
//		
//		if (fill!=null)
//			view.setVisualProperty(BasicVisualLexicon.NETWORK_BACKGROUND_PAINT, decodeHEXColor(fill));
//	}
//	
//	@Override
//	public void setNodeGraphics(DynNetwork<T> dynNetwork, CyNode currentNode, String type, String h, String w, String s, String x, String y, String fill, String linew, String outline) 
//	{
//		CyNetworkView view = networkViewManager.getNetworkViews(dynNetwork.getNetwork()).iterator().next();
//		
//		if (type!=null && graphicsTypeMap.getTypedValue(graphicsTypeMap.getType(type))!=null)
//			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_SHAPE, (NodeShape) graphicsTypeMap.getTypedValue(graphicsTypeMap.getType(type)));
//		
//		if (h!=null)
//			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_HEIGHT, new Double(h));
//		
//		if (w!=null)
//			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_WIDTH, new Double(w));
//		
//		if (s!=null)
//			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_SIZE, new Double(w));
//		
//		if (x!=null)
//			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, new Double(x));
//		
//		if (y!=null)
//			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, new Double(y));
//		
//		if (linew!=null)
//			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_WIDTH, new Double(linew));
//		
//		if (fill!=null)
//			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, decodeHEXColor(fill));
//		
//		if (outline!=null)
//			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_PAINT, decodeHEXColor(outline));
//		
//	}
//	
//	@Override
//	public void setEdgeGraphics(DynNetwork<T> dynNetwork, CyEdge currentEdge, String width, String fill, String start, String end) 
//	{
//		CyNetworkView view = networkViewManager.getNetworkViews(dynNetwork.getNetwork()).iterator().next();
//		
//		if (width!=null)
//			view.getEdgeView(currentEdge).setVisualProperty(BasicVisualLexicon.EDGE_WIDTH, new Double(width));
//		
//		if (fill!=null)
//			view.getEdgeView(currentEdge).setVisualProperty(BasicVisualLexicon.EDGE_UNSELECTED_PAINT, decodeHEXColor(fill));
//	}
	
	@Override
	public void finalizeNetwork(DynNetworkView<T> dynNetworkView) 
	{	
//		while (!graphGraphicsList.isEmpty())
//			graphGraphicsList.pop().add(this);
//		
//		while (!nodeGraphicsList.isEmpty())
//			nodeGraphicsList.pop().add(this);
//		
//		while (!edgeGraphicsList.isEmpty())
//			edgeGraphicsList.pop().add(this);
	}

}
