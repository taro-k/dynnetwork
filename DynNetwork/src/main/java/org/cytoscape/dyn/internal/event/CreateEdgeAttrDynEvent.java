package org.cytoscape.dyn.internal.event;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyEdge;

public class CreateEdgeAttrDynEvent<T> implements DynEvent
{
	private final DynNetwork<T> network;
	private final CyEdge edge;
	private final String name;
	private final String value;
	private final String type;
	private final String start;
	private final String end;
	
	private final Sink<T> sink;

	public CreateEdgeAttrDynEvent(
			final DynNetwork<T> network,
			final CyEdge edge,
			final String name,
			final String value, 
			final String type,
			final String start, 
			final String end, 
			final Sink<T> sink) 
	{
		this.network = network;
		this.edge = edge;
		this.name = name;
		this.value = value;
		this.type = type;
		this.start = start;
		this.end = end;
		this.sink = sink;
	}
	
	@Override
	public Object call() throws Exception
	{
		sink.addedEdgeAttribute(network, edge, name, value, type, start, end);
		return null;
	}

}