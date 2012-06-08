package org.cytoscape.dyn.internal.event;


/**
 * <code> Source </code> produces graph events (elements and attributes), but does not
 * contain a graph instance.
 *  
 * @author sabina
 * 
 */
public interface Source<T>
{
	void addSink(Sink<T> sink);
	
	void removeSink(Sink<T> sink);
}
