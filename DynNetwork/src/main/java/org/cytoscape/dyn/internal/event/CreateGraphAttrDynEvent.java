package org.cytoscape.dyn.internal.event;

import org.cytoscape.dyn.internal.model.DynNetwork;

public class CreateGraphAttrDynEvent<T> implements DynEvent
{
	private final DynNetwork<T> network;
	private final String name;
	private final String value;
	private final String type;
	private final String start;
	private final String end;
	
	private final Sink<T> sink;

	public CreateGraphAttrDynEvent(
			final DynNetwork<T> network,
			final String name,
			final String value, 
			final String type,
			final String start, 
			final String end, 
			final Sink<T> sink) 
	{
		this.network = network;
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
		sink.addedGraphAttribute(network, name, value, type, start, end);
		return null;
	}

}