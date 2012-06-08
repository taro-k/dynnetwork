package org.cytoscape.dyn.internal.view.model;

import org.cytoscape.dyn.internal.event.Sink;

/**
 * <code> DynNetworkViewFactory </code> is a the interface for the factory of
 * {@link DynNetworkView}s and is an event sink.
 * 
 * @author sabina
 *
 * @param <T>
 */
public interface DynNetworkViewFactory<T> extends Sink<T>
{

}
