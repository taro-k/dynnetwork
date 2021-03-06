/*
 * DynNetwork plugin for Cytoscape 3.0 (http://www.cytoscape.org/).
 * Copyright (C) 2013 Jimmy Mahesh Morzaria
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
package org.cytoscape.dyn.internal.io.read.csv;

import org.cytoscape.dyn.internal.io.event.Sink;
import org.cytoscape.dyn.internal.io.event.Source;
import org.cytoscape.dyn.internal.layout.model.DynLayoutFactory;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.DynNetworkFactory;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewFactory;
import org.cytoscape.dyn.internal.vizmapper.model.DynVizMapFactory;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.values.BendFactory;
import org.cytoscape.view.presentation.property.values.HandleFactory;

/**
 * @author Jimmy
 * 
 */
public abstract class AbstractCSVSource<T> implements Source<T> {

	protected DynNetworkFactory<T> networkSink;
	protected DynNetworkViewFactory<T> viewSink;
	protected DynLayoutFactory<T> layoutSink;
	protected DynVizMapFactory<T> vizMapSink;
	
	protected HandleFactory handleFactory;
	protected BendFactory bendFactory;

	protected DynNetwork<T> addGraph(String id, String label, String start,
			String end, String directed) {
		return networkSink.addedGraph(id, label, start, end, directed);
	}

	protected CyNode addNode(DynNetwork<T> currentNetwork, String id,
			String label, String start, String end) {
		return networkSink.addedNode(currentNetwork, id, label, start, end);
	}

	protected CyEdge addEdge(DynNetwork<T> currentNetwork, String id,
			String label, String source, String target, String start, String end) {
		return networkSink.addedEdge(currentNetwork, id, label, source, target,
				start, end);
	}

	protected void addGraphAttribute(DynNetwork<T> currentNetwork, String name,
			String value, String type, String start, String end) {
		networkSink.addedGraphAttribute(currentNetwork, name, value, type,
				start, end);
	}

	protected void addNodeAttribute(DynNetwork<T> network, CyNode currentNode,
			String name, String value, String type, String start, String end) {
		networkSink.addedNodeAttribute(network, currentNode, name, value, type,
				start, end);
	}

	protected void addEdgeAttribute(DynNetwork<T> network, CyEdge currentEdge,
			String name, String value, String type, String start, String end) {
		networkSink.addedEdgeAttribute(network, currentEdge, name, value, type,
				start, end);
	}

	protected void addGraphGraphics(DynNetwork<T> network, String fill,
			String start, String end) {
		vizMapSink.addedGraphGraphics(network, fill, start, end);
	}

	protected void addNodeGraphics(DynNetwork<T> network, CyNode currentNode,
			String type, String height, String width, String size, String fill, String labelfill, String labelsize,
			String linew, String outline, String transparency, String start,
			String end) {
		vizMapSink.addedNodeGraphics(network, currentNode, type, height, width,
				size, fill, labelfill, labelsize, linew, outline, transparency, start, end);
	}

	protected void addEdgeGraphics(DynNetwork<T> network, CyEdge currentEdge,
			String width, String fill, String sourcearrowshape, String targetarrowshape, String bend, String transparency, String start,
			String end) {
		vizMapSink.addedEdgeGraphics(network, currentEdge, width, fill, sourcearrowshape, targetarrowshape, bend,
				transparency, start, end, this.handleFactory, this.bendFactory);
	}

	protected void addNodeDynamics(DynNetwork<T> network, CyNode currentNode,
			String x, String y, String start, String end) {
		layoutSink.addedNodeDynamics(network, currentNode, x, y, start, end);
	}

	protected void finalize(DynNetwork<T> currentNetwork) {
		networkSink.finalizeNetwork(currentNetwork);
	}

	@Override
	public void addSink(Sink<T> sink) {
		if (sink instanceof DynNetworkViewFactory<?>)
			this.networkSink = (DynNetworkFactory<T>) sink;
		else if (sink instanceof DynNetworkViewFactory<?>)
			this.viewSink = (DynNetworkViewFactory<T>) sink;
		else if (sink instanceof DynLayoutFactory<?>)
			this.layoutSink = (DynLayoutFactory<T>) sink;
		else if (sink instanceof DynVizMapFactory<?>)
			this.vizMapSink = (DynVizMapFactory<T>) sink;
	}

	@Override
	public void removeSink(Sink<T> sink) {
		if (this.networkSink == sink)
			this.networkSink = null;
		else if (this.viewSink == sink)
			this.viewSink = null;
		else if (this.layoutSink == sink)
			this.layoutSink = null;
		else if (this.vizMapSink == sink)
			this.vizMapSink = null;
	}
	
	@Override
	public void removeSinks(){
	
		this.networkSink = null;
		this.viewSink = null;
		this.layoutSink = null;
		this.vizMapSink = null;
	}
}
