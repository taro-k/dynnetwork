package org.cytoscape.dyn.internal.stream;


/**
 * <code> Source </code> produces graph events (elements and attributes), but does not
 * contain a graph instance.
 *  
 * @author sabina
 * 
 */
public interface GraphEventSource
{
	void addSink(GraphEventSink sink);
	
	void removeSink(GraphEventSink sink);
	
}
