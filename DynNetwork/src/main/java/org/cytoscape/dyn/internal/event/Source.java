package org.cytoscape.dyn.internal.event;


/**
 * <code> Source </code> produces graph events (elements and attributes), but does not
 * contain a graph instance.
 *  
 * @author sabina
 * 
 * @param <T>
 * 
 */
public interface Source<T>
{
	/**
	 * Add a {@link Sink}.
	 * @param sink
	 */
	void addSink(Sink<T> sink);
	
	/**
	 * Remove a {@link Sink}.
	 * @param sink
	 */
	void removeSink(Sink<T> sink);
}
