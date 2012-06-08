package org.cytoscape.dyn.internal.event;

import org.cytoscape.dyn.internal.model.DynNetwork;

/**
 * <code> CreateEdgeDynEvent </code> implements the interface {@link DynEvent}
 * for the generation of edge events.
 * 
 * @author sabina
 *
 * @param <T>
 */
public class CreateEdgeDynEvent<T> implements DynEvent
{
	private final DynNetwork<T> network;
	private final String id;
	private final String label;
	private final String source;
	private final String target;
	private final String start;
	private final String end;
	
	private final Sink<T> sink;

	public CreateEdgeDynEvent(
			final DynNetwork<T> network,
			final String id,
			final String label, 
			final String source,
			final String target,
			final String start, 
			final String end, 
			final Sink<T> sink) 
	{
		this.network = network;
		this.id = id;
		this.label = label;
		this.source = source;
		this.target = target;
		this.start = start;
		this.end = end;
		this.sink = sink;
	}
	
	@Override
	public Object call() throws Exception
	{
		return sink.addedEdge(network, id, label, source, target, start, end);
	}

}
