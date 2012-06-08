package org.cytoscape.dyn.internal.event;

/**
 * <code> CreateGraphDynEvent </code> implements the interface {@link DynEvent}
 * for the generation of graph events.
 * 
 * @author sabina
 *
 * @param <T>
 */
public final class CreateGraphDynEvent<T> implements DynEvent
{
	private final String id;
	private final String label;
	private final String start;
	private final String end;
	private final String directed;
	
	private final Sink<T> sink;

	public CreateGraphDynEvent(
			final String sourceId, 
			final double timeStamp, 
			final String id,
			final String label, 
			final String start, 
			final String end, 
			final String directed,
			final Sink<T> sink) 
	{
		this.id = id;
		this.label = label;
		this.start = start;
		this.end = end;
		this.directed = directed;
		this.sink = sink;
	}
	
	@Override
	public Object call() throws Exception
	{
		return sink.addedGraph(id, label, start, end, directed);
	}

}
