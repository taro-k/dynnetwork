package org.cytoscape.dyn.internal.event;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.group.CyGroup;

/**
 * <code> CreateNodeDynEvent </code> implements the interface {@link DynEvent}
 * for the generation of node events.
 * 
 * @author sabina
 *
 * @param <T>
 */
public class CreateNodeDynEvent<T> implements DynEvent
{
	private final DynNetwork<T> network;
	private final CyGroup group;
	private final String id;
	private final String label;
	private final String start;
	private final String end;
	
	private final Sink<T> sink;

	public CreateNodeDynEvent(
			final DynNetwork<T> network,
			final CyGroup group,
			final String id,
			final String label, 
			final String start, 
			final String end, 
			final Sink<T> sink) 
	{
		this.network = network;
		this.group = group;
		this.id = id;
		this.label = label;
		this.start = start;
		this.end = end;
		this.sink = sink;
	}
	
	@Override
	public Object call() throws Exception
	{
		return sink.addedNode(network, group, id, label, start, end);
	}

}
