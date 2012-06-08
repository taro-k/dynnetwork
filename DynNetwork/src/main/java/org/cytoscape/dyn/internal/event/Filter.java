package org.cytoscape.dyn.internal.event;


/**
 * <code> Filter </code> filters event that pass between a 
 * a {@link Source} and a {@link Sink}.
 *  
 * @author sabina
 * 
 * @param <T>
 * 
 */
public interface Filter<T> extends Source<T>, Sink<T>
{
	
}
