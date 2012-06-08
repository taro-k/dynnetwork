package org.cytoscape.dyn.internal.model;

import org.cytoscape.dyn.internal.event.Sink;


/**
 * <code> DynNetworkFactory </code> is a the interface for the factory of
 * {@link DynNetwork}s and is an event sink.
 * 
 * @author sabina
 *
 * @param <T>
 */
public interface DynNetworkFactory<T> extends Sink<T>
{

}
