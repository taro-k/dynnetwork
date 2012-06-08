package org.cytoscape.dyn.internal.event;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyNode;

/**
 * <code> CreateNodeAttrDynEvent </code> implements the interface {@link DynEvent}
 * for the generation of node attribute events.
 * 
 * @author sabina
 *
 * @param <T>
 */
public class CreateNodeAttrDynEvent<T> implements DynEvent
{
	private final DynNetwork<T> network;
	private final CyNode node;
	private final String name;
	private final String value;
	private final String type;
	private final String start;
	private final String end;
	
	private final Sink<T> sink;

	public CreateNodeAttrDynEvent(
			final DynNetwork<T> network,
			final CyNode node,
			final String name,
			final String value, 
			final String type,
			final String start, 
			final String end, 
			final Sink<T> sink) 
	{
		this.network = network;
		this.node = node;
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
		sink.addedNodeAttribute(network, node, name, value, type, start, end);
		return null;
	}

}