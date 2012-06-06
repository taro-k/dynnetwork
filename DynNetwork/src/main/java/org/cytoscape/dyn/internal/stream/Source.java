package org.cytoscape.dyn.internal.stream;


/**
 * <code> Source </code> produces graph events (elements and attributes), but does not
 * contain a graph instance.
 *  
 * @author sabina
 * 
 */
public interface Source
{
	void addSink(Sink sink);
	
	void removeSink(Sink sink);
}
